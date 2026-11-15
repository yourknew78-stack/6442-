#!/usr/bin/env python3
"""把「最终资料」里的全部 markdown 合并成一个自带导航的单文件 HTML。

用法：python3 tools/build-handbook.py
产物：最终资料/6442-复习-合订本.html
依赖：pandoc、Pillow（图片压缩）
"""

from __future__ import annotations

import base64
import io
import os
import re
import subprocess
import sys
import urllib.parse
from datetime import datetime

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC_DIR = os.path.join(ROOT, "最终资料")
OUT = os.path.join(SRC_DIR, "6442-复习-合订本.html")

PARTS = [
    ("p1", "第 1 部分 · 测试与词法"),
    ("p2", "第 2 部分 · 设计与格式"),
    ("p3", "第 3 部分 · 应试题库"),
    ("p4", "附录"),
]

DOCS = [
    dict(part="p1", num="1.1", label="软件测试代码速查", file="1.1-testing-易读版.md"),
    dict(part="p1", num="1.2", label="Tokeniser 与 Parser", file="1.2-tokeniser-易读版.md"),
    dict(part="p2", num="2.1", label="UML 与设计模式", file="2.1-design-patterns-uml-易读版.md"),
    dict(part="p2", num="2.2", label="XML 与文件格式", file="2.2-xml-和其他文件格式-易读版.md"),
    dict(part="p3", num="3.1", label="选择题真题与 Quiz 2", file="../0选择题/选择题真题和 quiz 参考.md"),
    dict(part="p3", num="3.3", label="速查资料与考点预测", file="../0选择题/3.3-选择题速查资料-考点预测.md"),
    dict(part="p4", num="05", label="树结构（可略读）", file="基本不用看05-trees易读版.md"),
    dict(part="p4", num="git", label="Git 使用方式", file="git-使用方式.md"),
]

HEAD_RE = re.compile(r'<h([1-6]) id="([^"]*)">(.*?)</h\1>', re.S)
IMG_RE = re.compile(r'<img\b[^>]*>', re.S)
SRC_RE = re.compile(r'src="([^"]*)"')
ID_RE = re.compile(r'id="([^"]+)"')
HREF_RE = re.compile(r'href="#([^"]+)"')
TAG_RE = re.compile(r"<[^>]+>")


def esc(s: str) -> str:
    return (
        s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace('"', "&quot;")
    )


def pandoc_to_html(path: str) -> str:
    cmd = [
        "pandoc",
        "-f",
        "markdown+raw_html+pipe_tables+task_lists+footnotes+smart",
        "-t",
        "html5",
        "--wrap=none",
        "--highlight-style=tango",
        path,
    ]
    res = subprocess.run(cmd, capture_output=True)
    if res.returncode != 0:
        sys.exit("pandoc failed on %s:\n%s" % (path, res.stderr.decode()))
    return res.stdout.decode("utf-8")


# ---------------------------------------------------------------- images

_img_cache: dict[str, tuple[str, int]] = {}
_total_img_bytes = 0


def data_uri(abs_path: str) -> str | None:
    global _total_img_bytes
    if abs_path in _img_cache:
        uri, size = _img_cache[abs_path]
        _total_img_bytes += size
        return uri
    try:
        raw = open(abs_path, "rb").read()
    except OSError:
        return None

    mime = {".png": "image/png", ".jpg": "image/jpeg", ".jpeg": "image/jpeg",
            ".gif": "image/gif", ".webp": "image/webp"}.get(
        os.path.splitext(abs_path)[1].lower(), "image/png")

    payload, mime = compress(raw, mime)
    uri = "data:%s;base64,%s" % (mime, base64.b64encode(payload).decode())
    _img_cache[abs_path] = (uri, len(payload))
    _total_img_bytes += len(payload)
    return uri


def compress(raw: bytes, mime: str) -> tuple[bytes, str]:
    """统一转 WebP 压体积；小图（<28KB）或已经是 webp 的原样保留。"""
    if len(raw) < 28 * 1024 or mime == "image/webp":
        return raw, mime
    try:
        from PIL import Image
    except ImportError:
        return raw, mime
    try:
        im = Image.open(io.BytesIO(raw))
        im.load()
    except Exception:
        return raw, mime

    w, h = im.size
    if w > 1600:
        im = im.resize((1600, max(1, round(h * 1600 / w))), Image.LANCZOS)

    buf = io.BytesIO()
    try:
        im.save(buf, "WEBP", quality=80, method=6)
    except Exception:
        return raw, mime
    out = buf.getvalue()
    if len(out) >= len(raw):
        return raw, mime
    return out, "image/webp"


def embed_images(html: str, md_dir: str, doc_id: str) -> str:
    def repl(m: re.Match) -> str:
        tag = m.group(0)
        src_m = SRC_RE.search(tag)
        if not src_m:
            return tag
        src = src_m.group(1)
        if src.startswith("data:"):
            return tag
        rel = urllib.parse.unquote(src)
        abs_path = os.path.normpath(os.path.join(md_dir, rel))
        uri = data_uri(abs_path) if os.path.exists(abs_path) else None
        if uri is None:
            print("  ! 图片缺失：%s -> %s" % (doc_id, src))
            return tag.replace('class="zoomable"', "").replace(
                "<img ", '<img class="img-missing" alt="[图片缺失] ' + esc(src) + '" ')
        return tag.replace(src_m.group(0), 'src="%s"' % uri)

    html = IMG_RE.sub(repl, html)
    return html.replace("<img ", '<img class="zoomable" loading="lazy" decoding="async" ')


# ---------------------------------------------------------------- ids / toc

def make_text(html_fragment: str) -> str:
    return re.sub(r"\s+", " ", TAG_RE.sub("", html_fragment)).strip()


def rewrite_ids(html: str, prefix: str) -> tuple[str, dict[str, str]]:
    mapping: dict[str, str] = {}
    order: list[str] = []

    def collect(m: re.Match) -> str:
        old = m.group(1)
        if old not in mapping:
            mapping[old] = "%s-%s" % (prefix, old)
            order.append(old)
        return 'id="%s"' % mapping[old]

    html = ID_RE.sub(collect, html)

    def href(m: re.Match) -> str:
        old = m.group(1)
        if old in mapping:
            return 'href="#%s"' % mapping[old]
        return m.group(0)

    html = HREF_RE.sub(href, html)
    return html, mapping


# ---------------------------------------------------------------- build

def build_doc(index: int, doc: dict) -> dict:
    path = os.path.join(SRC_DIR, doc["file"])
    md = open(path, encoding="utf-8").read()
    html = pandoc_to_html(path)

    doc_id = doc["num"].replace(".", "-")
    prefix = "d%s" % doc_id.replace("-", "")

    html = embed_images(html, os.path.dirname(path), doc["num"])
    html, mapping = rewrite_ids(html, prefix)

    # 第一处 h1 当标题，其余 h1 降级成「大节」
    heads: list[dict] = []
    seen_title = False

    def head_repl(m: re.Match) -> str:
        nonlocal seen_title
        level, hid, inner = int(m.group(1)), m.group(2), m.group(3)
        text = make_text(inner)
        new_id = mapping.get(hid, hid)
        if level == 1 and not seen_title:
            seen_title = True
            doc["title"] = text
            return '<h1 class="doc-title" id="%s">%s</h1>' % (new_id, inner)
        if level == 1:
            level = 2
            inner = '<span class="secnum">§</span>' + inner
        heads.append(dict(level=level, id=new_id, text=text))
        return '<h%d id="%s">%s</h%d>' % (level, new_id, inner, level)

    html = HEAD_RE.sub(head_repl, html)

    # 正文小目录（移动端友好）
    sec_links = "".join(
        '<a href="#%s">%s</a>' % (h["id"], esc(h["text"])) for h in heads if h["level"] == 2
    )
    toc_block = (
        '<details class="doc-toc"><summary>本节目录（%d 节）</summary><div class="doc-toc-grid">%s</div></details>'
        % (sum(1 for h in heads if h["level"] == 2), sec_links)
        if sec_links
        else ""
    )

    doc["id"] = doc_id
    doc["prefix"] = prefix
    doc["abs"] = path
    doc["heads"] = heads
    doc["html"] = toc_block + html
    doc["chars"] = len(md)
    doc["path"] = os.path.relpath(path, ROOT)
    return doc


def rewrite_cross_links(html: str, md_dir: str, by_abs: dict) -> str:
    """把指向其它 md 的链接改成同一页内的锚点。"""
    def repl(m: re.Match) -> str:
        href = m.group(1)
        if href.startswith(("http", "#", "mailto:", "data:")):
            return m.group(0)
        path_part, _, anchor = href.partition("#")
        target = os.path.normpath(os.path.join(md_dir, urllib.parse.unquote(path_part)))
        doc = by_abs.get(target)
        if not doc:
            return m.group(0)
        if anchor:
            cand = "%s-%s" % (doc["prefix"], urllib.parse.unquote(anchor))
            if cand in doc["ids"]:
                return 'href="#%s"' % cand
        return 'href="#doc-%s"' % doc["id"]

    return re.sub(r'href="([^"]+)"', repl, html)


def build_nav_final(docs: list[dict]) -> str:
    """二级 + 三级嵌套目录。"""
    out: list[str] = ['<nav id="toc" aria-label="目录">']
    current_part = None
    for doc in docs:
        if doc["part"] != current_part:
            if current_part is not None:
                out.append("</div>")
            current_part = doc["part"]
            out.append('<div class="grp"><div class="grp-h">%s</div>' % esc(dict(PARTS)[current_part]))
        nsec = sum(1 for h in doc["heads"] if h["level"] == 2)
        out.append(
            '<div class="docnav" data-doc="%s">'
            '<a class="docnav-t" href="#doc-%s"><span class="dn">%s</span>'
            '<span class="dt">%s</span><span class="dc">%d</span></a>'
            % (doc["id"], doc["id"], esc(doc["num"]), esc(doc["label"]), nsec)
        )
        out.append('<ul class="secs">')
        for h in doc["heads"]:
            if h["level"] == 2:
                out.append('<li><a href="#%s">%s</a>' % (h["id"], esc(h["text"])))
            elif h["level"] == 3:
                out.append('<li class="sub"><a href="#%s">%s</a></li>' % (h["id"], esc(h["text"])))
        out.append("</ul></div>")
    out.append("</div></nav>")
    return "".join(out)


def main() -> None:
    docs = [build_doc(i, dict(d)) for i, d in enumerate(DOCS)]
    by_abs = {os.path.normpath(d["abs"]): d for d in docs}
    for d in docs:
        d["ids"] = set(ID_RE.findall(d["html"]))
    for d in docs:
        d["html"] = rewrite_cross_links(d["html"], os.path.dirname(d["abs"]), by_abs)

    n_sec = sum(1 for d in docs for h in d["heads"] if h["level"] == 2)
    n_sub = sum(1 for d in docs for h in d["heads"] if h["level"] == 3)
    n_chars = sum(d["chars"] for d in docs)

    nav = build_nav_final(docs)

    cards_parts: list[str] = []
    cur_part = None
    for d in docs:
        nsec = sum(1 for h in d["heads"] if h["level"] == 2)
        if d["part"] != cur_part:
            if cur_part is not None:
                cards_parts.append("</div></div>")
            cur_part = d["part"]
            cards_parts.append(
                '<div class="card-group"><div class="cg-h">%s</div><div class="cards">'
                % esc(dict(PARTS)[cur_part])
            )
        cards_parts.append(
            '<a class="card" href="#doc-%s"><span class="card-num">%s</span>'
            '<span class="card-title">%s</span><span class="card-meta">%d 节 · 约 %.1f 千字</span></a>'
            % (d["id"], esc(d["num"]), esc(d["label"]), nsec, d["chars"] / 1000)
        )
    cards_parts.append("</div></div>")
    cards = "".join(cards_parts)

    articles = []
    last_part = None
    for idx, d in enumerate(docs):
        prev_doc = docs[idx - 1] if idx > 0 else None
        next_doc = docs[idx + 1] if idx + 1 < len(docs) else None
        if d["part"] != last_part:
            last_part = d["part"]
            articles.append(
                '<div class="part-head" id="part-%s"><span>%s</span></div>'
                % (d["part"], esc(dict(PARTS)[d["part"]]))
            )
        articles.append(
            '<article class="doc" id="doc-%s" data-doc="%s">'
            '<div class="doc-kicker">资料 %s · %d 节 · 约 %.1f 千字</div>'
            "<div class='doc-body'>%s</div>"
            '<div class="doc-foot">'
            '<a class="foot-nav prev" href="#%s">%s</a>'
            '<span class="foot-src">源文件 <code>%s</code></span>'
            '<a class="foot-nav next" href="#%s">%s</a>'
            "</div>"
            "</article>"
            % (d["id"], d["id"], esc(d["num"]),
               sum(1 for h in d["heads"] if h["level"] == 2), d["chars"] / 1000,
               d["html"],
               ("doc-" + prev_doc["id"]) if prev_doc else "top",
               ("← " + prev_doc["num"] + " " + prev_doc["label"]) if prev_doc else "回到开头",
               esc(d["path"]),
               ("doc-" + next_doc["id"]) if next_doc else "top",
               ((next_doc["num"] + " " + next_doc["label"] + " →") if next_doc else "回到开头"))
        )

    stamp = datetime.now().strftime("%Y-%m-%d %H:%M")
    stats = "%d 份资料 · %d 个章节 · %d 个小节 · 约 %.1f 万字" % (
        len(docs), n_sec, n_sub, n_chars / 10000)

    html = (
        TEMPLATE.replace("{{TITLE}}", "COMP6442 复习合订本")
        .replace("{{STAMP}}", stamp)
        .replace("{{STATS}}", stats)
        .replace("{{NAV}}", nav)
        .replace("{{CARDS}}", cards)
        .replace("{{DOCS}}", "\n".join(articles))
        .replace("{{COUNT}}", str(len(docs)))
    )

    with open(OUT, "w", encoding="utf-8") as f:
        f.write(html)

    print("写出：%s" % os.path.relpath(OUT, ROOT))
    print("  %s" % stats)
    print("  内嵌图片 %d 张，压缩后约 %.1f MB" % (len(_img_cache), _total_img_bytes / 1e6))
    print("  HTML 大小 %.2f MB" % (os.path.getsize(OUT) / 1e6))


TEMPLATE = r"""<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="generator" content="build-handbook.py · {{STAMP}}">
<title>{{TITLE}}</title>
<style>
:root{
  --bg:#f7f5f0; --panel:#fffdf9; --ink:#1c1d21; --ink2:#43454d; --muted:#7c7d86;
  --line:#e6e1d5; --line2:#efeae0; --accent:#a3452b; --accent2:#c4694a;
  --tint:#f7e9e2; --code:#f4f1e9; --ok:#2f7a52;
  --shadow:0 1px 2px rgba(30,25,20,.05),0 8px 24px rgba(30,25,20,.05);
}
*{box-sizing:border-box}
html{scroll-behavior:smooth}
@media (prefers-reduced-motion:reduce){html{scroll-behavior:auto}}
body{
  margin:0; overflow-x:hidden; background:var(--bg); color:var(--ink);
  font-family:"Songti SC","Noto Serif CJK SC","Source Han Serif SC",Georgia,"Times New Roman",serif;
  -webkit-font-smoothing:antialiased; text-rendering:optimizeLegibility;
}
a{color:var(--accent); text-decoration:none}
a:hover{text-decoration:underline}
code,kbd,pre,.mono{font-family:"SF Mono",ui-monospace,Menlo,Consolas,"Liberation Mono",monospace}

/* ---------- 进度条 / 顶栏 ---------- */
#progress{position:fixed; top:0; left:0; height:3px; width:0; z-index:60;
  background:linear-gradient(90deg,#c4694a,#a3452b); transition:width .1s linear}
.topbar{
  position:sticky; top:0; z-index:50; height:58px; display:flex; align-items:center; gap:16px;
  padding:0 20px; background:rgba(247,245,240,.92); backdrop-filter:saturate(180%) blur(10px);
  border-bottom:1px solid var(--line);
}
.brand{display:flex; align-items:baseline; gap:9px; font-family:-apple-system,"Helvetica Neue",Arial,sans-serif}
.brand b{font-size:15px; letter-spacing:.02em}
.brand span{font-size:12px; color:var(--muted)}
.tb-spacer{flex:1}
.search{position:relative; display:flex; align-items:center}
.search input{
  width:250px; height:34px; padding:0 34px 0 32px; border-radius:9px; border:1px solid var(--line);
  background:#fff; font-family:-apple-system,"Helvetica Neue",Arial,sans-serif; font-size:13px; color:var(--ink);
  outline:none; transition:border-color .15s, box-shadow .15s;
}
.search input:focus{border-color:var(--accent2); box-shadow:0 0 0 3px rgba(196,105,74,.14)}
.search .ico{position:absolute; left:10px; color:var(--muted); font-size:13px; pointer-events:none}
.search kbd{position:absolute; right:9px; font-size:11px; color:var(--muted); border:1px solid var(--line);
  border-radius:4px; padding:1px 5px; background:var(--bg)}
#menuBtn{display:none; border:1px solid var(--line); background:#fff; border-radius:8px;
  width:34px; height:34px; font-size:15px; cursor:pointer; color:var(--ink2)}
.tb-count{font-family:-apple-system,"Helvetica Neue",Arial,sans-serif; font-size:12px; color:var(--muted)}

/* ---------- 布局 ---------- */
.layout{display:flex; align-items:flex-start}
aside{
  flex:0 0 306px; width:306px; position:sticky; top:58px; height:calc(100vh - 58px);
  overflow-y:auto; overscroll-behavior:contain; border-right:1px solid var(--line);
  background:linear-gradient(180deg,#faf8f3,#f7f5f0); padding:14px 10px 40px;
  font-family:-apple-system,"Helvetica Neue",Arial,"PingFang SC",sans-serif;
}
aside::-webkit-scrollbar{width:9px}
aside::-webkit-scrollbar-thumb{background:#d9d3c6; border-radius:9px; border:2px solid transparent; background-clip:content-box}
.nav-tools{display:flex; gap:8px; padding:0 6px 12px}
.nav-tools button{
  flex:1; font-size:11.5px; color:var(--ink2); background:#fff; border:1px solid var(--line);
  border-radius:7px; padding:5px 6px; cursor:pointer; font-family:inherit;
}
.nav-tools button:hover{border-color:var(--accent2); color:var(--accent)}
.grp{margin:14px 0 4px}
.grp-h{font-size:10.5px; letter-spacing:.1em; color:var(--muted); padding:6px 12px; font-weight:600}
.docnav{margin:1px 0 6px}
.docnav-t{
  display:flex; align-items:center; gap:8px; padding:7px 10px; border-radius:8px;
  color:var(--ink); font-size:13.5px; font-weight:600; line-height:1.3;
}
.docnav-t:hover{background:#f0ece2; text-decoration:none}
.docnav-t .dn{font-size:11px; color:var(--accent); font-variant-numeric:tabular-nums; min-width:25px}
.docnav-t .dt{flex:1}
.docnav-t .dc{font-size:10.5px; color:var(--muted); font-weight:400}
.docnav.on .docnav-t{background:var(--tint); box-shadow:inset 2px 0 0 var(--accent)}
.docnav.on .docnav-t .dt{color:#7d2f18}
.secs{list-style:none; margin:2px 0 0; padding:0 0 0 14px; display:none}
.docnav.open .secs{display:block}
.secs li{margin:0}
.secs a{
  display:block; padding:4.5px 9px 4.5px 11px; font-size:12.5px; color:var(--ink2);
  border-left:2px solid transparent; border-radius:0 6px 6px 0; line-height:1.35;
}
.secs a:hover{background:#f0ece2; text-decoration:none; color:var(--ink)}
.secs li.sub a{padding-left:20px; font-size:11.8px; color:var(--muted)}
.secs li.sub a:hover{color:var(--ink2)}
.secs a.active{color:var(--accent); background:#fdf1ec; border-left-color:var(--accent); font-weight:600}
.secs li.sub a.active{color:var(--accent)}
.noresult{display:none; padding:13px; font-size:12.5px; color:var(--muted); text-align:center}
.searching .noresult{display:block}

/* ---------- 正文 ---------- */
main{flex:1; min-width:0}
.page{max-width:830px; margin:0 auto; padding:0 34px 140px}
.hero{padding:64px 0 8px}
.hero .eyebrow{font-family:-apple-system,"Helvetica Neue",Arial,sans-serif; font-size:11.5px;
  letter-spacing:.16em; color:var(--accent); text-transform:uppercase}
.hero h1{font-size:clamp(30px,4.4vw,44px); line-height:1.25; margin:14px 0 12px; letter-spacing:-.01em}
.hero p.lead{font-size:15.5px; color:var(--ink2); line-height:1.9; margin:0 0 6px}
.hero .stat{font-family:-apple-system,"Helvetica Neue",Arial,sans-serif; font-size:12.5px; color:var(--muted); margin-top:16px}
.card-group{margin:26px 0 6px}
.cg-h{font-family:-apple-system,"Helvetica Neue",Arial,sans-serif; font-size:11px; letter-spacing:.1em;
  color:var(--muted); margin:0 0 10px; padding-bottom:6px; border-bottom:1px solid var(--line2)}
.cards{display:grid; grid-template-columns:repeat(auto-fill,minmax(215px,1fr)); gap:12px}
.card{
  display:flex; flex-direction:column; gap:5px; padding:15px 16px; border:1px solid var(--line);
  border-radius:12px; background:var(--panel); color:var(--ink); box-shadow:var(--shadow);
  transition:transform .15s ease, border-color .15s ease, box-shadow .15s ease;
  font-family:-apple-system,"Helvetica Neue",Arial,sans-serif;
}
.card:hover{text-decoration:none; transform:translateY(-2px); border-color:#dccfbe}
.card .card-num{font-size:11px; color:var(--accent); letter-spacing:.06em}
.card .card-title{font-size:14.5px; font-weight:600; line-height:1.45}
.card .card-meta{font-size:11.5px; color:var(--muted)}

.part-head{margin:76px 0 0; display:flex; align-items:center; gap:14px;
  font-family:-apple-system,"Helvetica Neue",Arial,sans-serif}
.part-head span{font-size:11.5px; letter-spacing:.14em; color:var(--accent); white-space:nowrap}
.part-head::after{content:""; flex:1; height:1px; background:linear-gradient(90deg,var(--line),transparent)}

.doc{padding-top:26px; scroll-margin-top:72px}
.doc-kicker{font-family:-apple-system,"Helvetica Neue",Arial,sans-serif; font-size:11.5px;
  color:var(--muted); letter-spacing:.04em; padding-bottom:2px}
.doc-body{font-size:16.3px; line-height:1.9; color:#232428}
.doc-body h1.doc-title{font-size:clamp(25px,3vw,31px); line-height:1.35; margin:8px 0 18px; letter-spacing:-.01em}
.doc-body h1.doc-title::after{content:""; display:block; width:56px; height:3px; background:var(--accent);
  border-radius:3px; margin-top:16px; opacity:.75}
.doc-body h1:not(.doc-title){font-size:22px; margin:44px 0 14px}
.doc-body h2{
  font-size:21px; line-height:1.5; margin:46px 0 14px; padding-left:12px;
  border-left:4px solid var(--accent); scroll-margin-top:74px;
}
.doc-body h3{font-size:17px; line-height:1.6; margin:30px 0 10px; color:#2b2c31; scroll-margin-top:74px}
.doc-body h4{font-size:15.5px; margin:22px 0 8px; color:var(--ink2)}
.doc-body h5{font-size:14.6px; margin:19px 0 6px; color:#3a3b41; font-weight:600}
.doc-body h6{font-size:13.8px; margin:16px 0 6px; color:#4a4b52}
.doc-body h2 .secnum{margin-right:6px; color:var(--accent); font-size:15px; vertical-align:2px}
.doc-body p{margin:0 0 14px}
.doc-body ul,.doc-body ol{padding-left:1.45em; margin:0 0 15px}
.doc-body li{margin:5px 0}
.doc-body li>ul,.doc-body li>ol{margin:5px 0}
.doc-body strong{color:#111}
.doc-body hr{border:none; border-top:1px solid var(--line); margin:34px 0}
.doc-body blockquote{
  margin:18px 0; padding:12px 16px; border-left:3px solid var(--accent2);
  background:#fbf5f0; border-radius:0 8px 8px 0; color:var(--ink2);
}
.doc-body blockquote p:last-child{margin-bottom:0}
.doc-body :not(pre)>code{
  background:#f1ede3; padding:.12em .38em; border-radius:4px; font-size:.875em;
  color:#8a3a1e; border:1px solid #eae4d6;
}
.doc-body pre{
  background:var(--code); border:1px solid var(--line); border-radius:10px;
  padding:14px 16px; overflow-x:auto; font-size:13px; line-height:1.65; margin:16px 0 20px;
  -webkit-overflow-scrolling:touch;
}
.doc-body pre code{background:none; border:none; padding:0; color:#2b2b2b; font-size:13px}
.doc-body .codeblock{position:relative}
.doc-body .codelang{
  position:absolute; top:8px; right:10px; font-size:10px; letter-spacing:.06em; color:#9a9382;
  text-transform:uppercase; background:rgba(255,253,249,.85); padding:1px 6px; border-radius:4px;
  font-family:-apple-system,"Helvetica Neue",Arial,sans-serif; pointer-events:none;
}
.doc-body table.dense{font-size:13.2px}
.doc-body table.dense th,.doc-body table.dense td{padding:6px 8px}
.doc-body table.wide{font-size:13.4px}
.doc-body table.wide th,.doc-body table.wide td{padding:7px 9px}
.doc-body table{width:100%; border-collapse:collapse; margin:18px 0 22px; font-size:14.2px; line-height:1.65}
.table-wrap{width:100%; overflow-x:auto; margin:18px 0 22px; -webkit-overflow-scrolling:touch}
.table-wrap table{margin:0}
.doc-body th,.doc-body td{border:1px solid var(--line); padding:8px 11px; text-align:left; vertical-align:top}
.doc-body thead th{background:#f3efe4; font-weight:600; white-space:nowrap}
.doc-body tbody tr:nth-child(even){background:#fbf9f4}
.doc-body tbody tr:hover{background:#f8f2e8}
.doc-body table a{text-decoration:none; color:var(--accent);
  border-bottom:1px solid rgba(163,69,43,.32)}
.doc-body table a:hover{background:#fdf1ec; border-bottom-color:var(--accent)}
.doc-body caption{caption-side:top; font-size:12.5px; color:var(--muted); padding-bottom:6px}
.doc-body img{max-width:100%!important; height:auto!important; display:block; margin:16px auto; border-radius:8px;
  border:1px solid var(--line2); background:#fff}
.doc-body img.zoomable{cursor:zoom-in}
.doc-body .img-missing{color:var(--muted); font-size:12.5px}
.doc-body figure{margin:18px 0}
.doc-body figcaption{font-size:12.5px; color:var(--muted); text-align:center; margin-top:6px}
.doc-body details{
  margin:14px 0; border:1px solid var(--line); border-radius:10px; background:var(--panel);
  padding:10px 14px;
}
.doc-body details>summary{cursor:pointer; font-size:14.5px; font-weight:600; color:var(--ink2); outline:none}
.doc-body details>summary::marker{color:var(--accent)}
.doc-body details[open]>summary{margin-bottom:10px; padding-bottom:8px; border-bottom:1px dashed var(--line)}
.doc-body dl{margin:0 0 16px}
.doc-body dt{font-weight:600; margin-top:10px}
.doc-body dd{margin:4px 0 0 1.2em; color:var(--ink2)}
.doc-body a.anchor{
  opacity:0; margin-left:8px; color:var(--muted); font-weight:400; font-size:.8em; text-decoration:none;
}
.doc-body h2:hover a.anchor,.doc-body h3:hover a.anchor{opacity:1}
.doc-body mark{background:#fde9b8; color:inherit; padding:0 3px; border-radius:3px}
.doc-body kbd{border:1px solid var(--line); border-bottom-width:2px; border-radius:4px; padding:1px 5px;
  font-size:11.5px; background:#fff}

.doc-toc{
  margin:6px 0 26px; background:var(--panel); border:1px solid var(--line); border-radius:12px;
  padding:10px 16px; font-family:-apple-system,"Helvetica Neue",Arial,sans-serif;
}
.doc-toc>summary{font-size:12.5px; color:var(--ink2); cursor:pointer; letter-spacing:.02em}
.doc-toc-grid{display:grid; grid-template-columns:1fr 1fr; gap:2px 18px; padding-top:10px}
.doc-toc-grid a{font-size:12.8px; color:var(--ink2); padding:3px 0; line-height:1.4}
.doc-toc-grid a:hover{color:var(--accent)}
.doc-foot{
  display:flex; justify-content:space-between; align-items:center; gap:12px; margin-top:34px;
  padding-top:14px; border-top:1px solid var(--line);
  font-family:-apple-system,"Helvetica Neue",Arial,sans-serif; font-size:11.5px; color:var(--muted);
}
.doc-foot code{font-size:11px; background:#efe9dd; padding:1px 5px; border-radius:4px}
.doc-foot .foot-src{flex:1; text-align:center; overflow:hidden; text-overflow:ellipsis; white-space:nowrap}
.doc-foot .foot-nav{color:var(--ink2); font-size:12px; padding:5px 10px; border:1px solid var(--line);
  border-radius:7px; background:#fff; white-space:nowrap; transition:border-color .15s, color .15s}
.doc-foot .foot-nav:hover{border-color:var(--accent2); color:var(--accent); text-decoration:none}
.doc-foot .foot-nav.prev{min-width:120px}
.doc-foot .foot-nav.next{min-width:120px; text-align:right}

/* ---------- 代码高亮（tango 近似） ---------- */
pre code span.kw{color:#8f2f68; font-weight:600}
pre code span.dt{color:#0f7b6c}
pre code span.dv,pre code span.bn,pre code span.fl{color:#b4541f}
pre code span.ch,pre code span.st{color:#3f7d20}
pre code span.co{color:#8a8577; font-style:italic}
pre code span.fu{color:#1f5fa8}
pre code span.va,pre code span.at{color:#b4541f}
pre code span.op{color:#5a5a5a}
pre code span.bu{color:#0f7b6c}
pre code span.cf{color:#8f2f68}
pre code span.im{color:#8f2f68}
pre code span.er{color:#b3261e}
pre code span.al{color:#b3261e}

/* ---------- 浮动控件 / 灯箱 ---------- */
#backtop{
  position:fixed; right:24px; bottom:24px; width:40px; height:40px; border-radius:50%;
  border:1px solid var(--line); background:var(--panel); color:var(--ink2); cursor:pointer;
  box-shadow:var(--shadow); opacity:0; pointer-events:none; transition:opacity .2s, transform .2s;
  z-index:40; font-size:15px;
}
#backtop.show{opacity:1; pointer-events:auto}
#backtop:hover{transform:translateY(-2px); border-color:var(--accent2); color:var(--accent)}
#lightbox{
  position:fixed; inset:0; background:rgba(20,17,14,.9); display:none; align-items:center;
  justify-content:center; z-index:80; padding:24px; cursor:zoom-out;
}
#lightbox.show{display:flex}
#lightbox img{max-width:94vw; max-height:88vh; border-radius:6px; box-shadow:0 20px 60px rgba(0,0,0,.5)}
#lightbox .lb-hint{position:absolute; bottom:18px; color:#cfc8bd; font-size:12px;
  font-family:-apple-system,"Helvetica Neue",Arial,sans-serif}

@media (max-width:1080px){
  aside{flex-basis:272px; width:272px}
  .page{padding:0 24px 120px}
  .doc-toc-grid{grid-template-columns:1fr}
}
@media (max-width:880px){
  #menuBtn{display:block}
  .tb-count{display:none}
  .topbar{padding:0 10px; gap:8px}
  .brand span{display:none}
  .brand{min-width:0}
  .search{min-width:0}
  .search input{width:138px}
  .search kbd{display:none}
  .doc-body table{font-size:13.2px}
  .doc-body th,.doc-body td{padding:6px 8px}
  aside{
    position:fixed; top:58px; left:0; height:calc(100vh - 58px); width:290px; flex:none;
    transform:translateX(-100%); transition:transform .22s ease; z-index:45; box-shadow:8px 0 24px rgba(0,0,0,.08);
  }
  body.nav-open aside{transform:none}
  body.nav-open::after{content:""; position:fixed; inset:58px 0 0; background:rgba(20,17,14,.28); z-index:44}
  .page{padding:0 18px 100px}
  .hero{padding:38px 0 4px}
  .doc-body{font-size:15.8px}
  .cards{grid-template-columns:1fr 1fr}
}
@media (max-width:520px){
  .cards{grid-template-columns:1fr}
  .search input{width:104px}
  .doc-body pre{font-size:12.3px}
  .page{padding:0 14px 90px}
  .hero h1{font-size:30px}
}
@media (max-width:640px){
  .doc-foot{flex-wrap:wrap}
  .doc-foot .foot-src{display:none}
  .doc-foot .foot-nav{flex:1; min-width:0}
}
@media print{
  .topbar,aside,#backtop,#progress,#lightbox,.doc-toc,.doc-foot .tiny-top{display:none!important}
  .page{max-width:none; padding:0}
  .doc{page-break-before:always}
  .part-head{page-break-before:always}
  .doc-body{font-size:11pt}
  a{color:#000; text-decoration:none}
}
</style>
</head>
<body id="top">
<div id="progress"></div>

<header class="topbar">
  <button id="menuBtn" aria-label="打开目录">☰</button>
  <div class="brand"><b>COMP6442</b><span>复习合订本</span></div>
  <div class="tb-spacer"></div>
  <div class="search">
    <span class="ico">⌕</span>
    <input id="q" type="search" placeholder="搜索章节标题…" aria-label="搜索章节">
    <kbd>/</kbd>
  </div>
  <div class="tb-count">{{COUNT}} 份资料</div>
</header>

<div class="layout">
<aside id="sidebar">
  <div class="nav-tools">
    <button id="expandAll" type="button">展开全部</button>
    <button id="collapseAll" type="button">收起全部</button>
  </div>
  {{NAV}}
  <div class="noresult">没有匹配的章节</div>
</aside>

<main>
<div class="page">

  <section class="hero">
    <div class="eyebrow">COMP6442 · Software Construction</div>
    <h1>复习合订本</h1>
    <p class="lead">把「最终资料」里的全部笔记合并成一页：左侧目录可展开到小节，点标题即跳转，滚动时自动高亮当前章节。</p>
    <div class="stat">{{STATS}} · 生成于 {{STAMP}}</div>
    <div class="card-groups">{{CARDS}}</div>
  </section>

  {{DOCS}}

</div>
</main>
</div>

<button id="backtop" aria-label="回到顶部">↑</button>
<div id="lightbox"><img alt=""><span class="lb-hint">点击任意处关闭</span></div>

<script>
(function () {
  var body = document.body;
  var sidebar = document.getElementById('sidebar');
  var progress = document.getElementById('progress');
  var backtop = document.getElementById('backtop');
  var q = document.getElementById('q');

  /* ---------- 目录展开 / 折叠 ---------- */
  document.querySelectorAll('.docnav').forEach(function (dn) {
    dn.querySelector('.docnav-t').addEventListener('click', function () {
      dn.classList.add('open');
      body.classList.remove('nav-open');
    });
  });
  document.getElementById('expandAll').addEventListener('click', function () {
    document.querySelectorAll('.docnav').forEach(function (d) { d.classList.add('open'); });
  });
  document.getElementById('collapseAll').addEventListener('click', function () {
    document.querySelectorAll('.docnav').forEach(function (d) { d.classList.remove('open'); });
  });

  /* ---------- 标题锚点 ---------- */
  document.querySelectorAll('.doc-body h2, .doc-body h3').forEach(function (h) {
    if (!h.id) return;
    var a = document.createElement('a');
    a.className = 'anchor';
    a.href = '#' + h.id;
    a.textContent = '#';
    a.setAttribute('aria-label', '本节链接');
    h.appendChild(a);
  });

  /* ---------- 表格横向滚动 + 代码语言角标 ---------- */
  document.querySelectorAll('.doc-body table').forEach(function (t) {
    if (t.parentElement.classList.contains('table-wrap')) return;
    var cols = t.querySelectorAll('thead th').length || (t.querySelector('tr') || {children: []}).children.length || 0;
    if (cols >= 6) t.style.minWidth = (cols >= 8 ? 1000 : 780) + 'px';
    if (cols >= 5) t.classList.add('dense');
    if (cols >= 7) t.classList.add('wide');
    var w = document.createElement('div');
    w.className = 'table-wrap';
    t.parentNode.insertBefore(w, t);
    w.appendChild(t);
  });
  document.querySelectorAll('.doc-body pre').forEach(function (pre) {
    var code = pre.querySelector('code');
    if (!code) return;
    var m = /(?:^|\s)language-([\w+#-]+)/.exec(code.className || '');
    if (!m) return;
    pre.classList.add('codeblock');
    var s = document.createElement('span');
    s.className = 'codelang';
    s.textContent = m[1];
    pre.appendChild(s);
  });

  /* ---------- 阅读进度 & 回顶 ---------- */
  var ticking = false;
  function onScroll() {
    var h = document.documentElement;
    var max = h.scrollHeight - h.clientHeight;
    var y = h.scrollTop || window.pageYOffset;
    progress.style.width = (max > 0 ? (y / max) * 100 : 0) + '%';
    backtop.classList.toggle('show', y > 700);
    ticking = false;
  }
  backtop.addEventListener('click', function () {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  });

  /* ---------- 滚动高亮 ---------- */
  var arts = [].slice.call(document.querySelectorAll('article.doc'));
  var docHeads = arts.map(function (a) {
    return [].slice.call(a.querySelectorAll('.doc-body h1, .doc-body h2, .doc-body h3'))
      .filter(function (h) { return h.id; });
  });
  var navLinks = {};
  document.querySelectorAll('#toc a[href^="#"]').forEach(function (a) {
    navLinks[a.getAttribute('href').slice(1)] = a;
  });
  var current = null, lastDn = null;

  function absTop(el) { return el.getBoundingClientRect().top + window.scrollY; }

  function spy() {
    var y = window.scrollY + 120;
    var artIdx = -1;
    for (var i = 0; i < arts.length; i++) {
      if (absTop(arts[i]) <= y) artIdx = i; else break;
    }
    if (artIdx < 0) return;

    var found = null, hs = docHeads[artIdx];
    for (var j = 0; j < hs.length; j++) {
      if (absTop(hs[j]) <= y) found = hs[j]; else break;
    }
    var key = found ? found.id : 'doc-' + arts[artIdx].getAttribute('data-doc');
    if (key === current) return;
    current = key;

    document.querySelectorAll('#toc a.active').forEach(function (a) { a.classList.remove('active'); });
    var li = navLinks[key];
    var dn = li ? li.closest('.docnav') : null;
    document.querySelectorAll('.docnav').forEach(function (d) {
      var on = d === (dn || byDoc(arts[artIdx]));
      d.classList.toggle('on', on);
      if (on) d.classList.add('open');
    });
    if (dn && dn !== lastDn) {
      lastDn = dn;
      sidebar.scrollTop = Math.max(0, dn.offsetTop - 96);
    }
    if (li) {
      li.classList.add('active');
      var secs = dn && dn.querySelector('.secs');
      if (secs) {
        var r = li.getBoundingClientRect(), sr = secs.getBoundingClientRect();
        if (r.top < sr.top + 24) sidebar.scrollTop += r.top - sr.top - 60;
        else if (r.bottom > sr.bottom - 24) sidebar.scrollTop += r.bottom - sr.bottom + 60;
      }
    }
  }

  function byDoc(art) {
    var id = art.getAttribute('data-doc');
    return [].filter.call(document.querySelectorAll('.docnav'), function (d) {
      return d.getAttribute('data-doc') === id;
    })[0];
  }

  var ticking = false;
  window.addEventListener('scroll', function () {
    if (!ticking) { ticking = true; requestAnimationFrame(function () { spy(); onScroll(); }); }
  }, { passive: true });
  window.addEventListener('resize', function () { spy(); }, { passive: true });

  /* ---------- 搜索过滤 ---------- */
  function applyFilter() {
    var term = q.value.trim().toLowerCase();
    sidebar.classList.toggle('searching', term.length > 0);
    document.querySelectorAll('.docnav').forEach(function (dn) {
      var t = dn.querySelector('.docnav-t');
      var titleHit = !term || t.textContent.toLowerCase().indexOf(term) > -1;
      var any = titleHit;
      dn.querySelectorAll('.secs a').forEach(function (a) {
        var hit = !term || a.textContent.toLowerCase().indexOf(term) > -1;
        a.parentElement.style.display = hit ? '' : 'none';
        if (hit) any = true;
      });
      dn.style.display = any ? '' : 'none';
      if (term && any) dn.classList.add('open');
      else if (!term) dn.classList.remove('open');
    });
  }
  q.addEventListener('input', applyFilter);
  q.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') { q.value = ''; applyFilter(); q.blur(); }
    if (e.key === 'Enter') {
      var first = sidebar.querySelector('.docnav:not([style*="none"]) .secs a:not([style*="none"]), .docnav:not([style*="none"]) .docnav-t');
      if (first) { first.click(); body.classList.remove('nav-open'); }
    }
  });
  document.addEventListener('keydown', function (e) {
    if (e.key === '/' && document.activeElement !== q) { e.preventDefault(); q.focus(); q.select(); }
  });

  /* ---------- 灯箱 ---------- */
  var lb = document.getElementById('lightbox');
  var lbImg = lb.querySelector('img');
  document.addEventListener('click', function (e) {
    var img = e.target.closest && e.target.closest('.doc-body img');
    if (img && img.src && !img.classList.contains('img-missing')) {
      e.preventDefault();
      lbImg.src = img.src;
      lbImg.alt = img.alt || '';
      lb.classList.add('show');
    } else if (e.target === lb || e.target === lbImg) {
      lb.classList.remove('show');
    }
  });
  document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') lb.classList.remove('show');
  });

  /* ---------- 移动端目录 ---------- */
  document.getElementById('menuBtn').addEventListener('click', function (e) {
    e.stopPropagation();
    body.classList.toggle('nav-open');
  });
  document.addEventListener('click', function (e) {
    if (body.classList.contains('nav-open') && !e.target.closest('#sidebar') && !e.target.closest('#menuBtn')) {
      body.classList.remove('nav-open');
    }
  });

  /* ---------- 打开时定位 hash ---------- */
  function jumpTo(id, smooth) {
    var el = document.getElementById(id);
    if (!el) return false;
    var top = el.getBoundingClientRect().top + window.scrollY - 66;
    window.scrollTo({ top: top, behavior: smooth ? 'smooth' : 'auto' });
    return true;
  }
  function jumpHash() {
    var id = decodeURIComponent(location.hash.slice(1));
    if (!id) return;
    var el = document.getElementById(id);
    if (!el) return;
    var dn = el.closest('.docnav');
    if (dn) dn.classList.add('open');
    jumpTo(id, false);
    setTimeout(function () { jumpTo(id, false); }, 220);
    setTimeout(function () { jumpTo(id, false); }, 700);
    setTimeout(function () { jumpTo(id, false); }, 1500);
  }
  window.addEventListener('hashchange', jumpHash);
  jumpHash();
  onScroll();
})();
</script>
</body>
</html>
"""


if __name__ == "__main__":
    main()
