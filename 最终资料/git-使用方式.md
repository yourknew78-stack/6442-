# 10｜Git 使用方式：普通三部曲与分支三部曲

> 核心：**日常改动走「pull → add + commit → push」；要试新东西就开分支，走「switch -c → commit + push → merge」。**

## 一分钟例子

只想把改好的一段笔记传上去：

```bash
git pull                      # 先拿远端最新
git add .                     # 把改动放进暂存区
git commit -m "补充 1.2 的 tokeniser 例子"   # 存成本地一个版本
git push                      # 推到 GitHub
```

想大改一章、又怕改坏 main：先 `git switch -c rewrite-1.2` 单开一条线，改完 push 到远端，满意了再 `merge` 回 main。

## 0. 先认清四个位置

Git 的所有命令，本质都是在搬东西：**把内容从一个位置搬到另一个位置。**

| 位置 | 是什么 | 对应命令 |
|---|---|---|
| 工作区（working tree） | 你在文件夹里能直接看到、直接改的文件 | 用编辑器改这里 |
| 暂存区（staging area / index） | 已经挑好、准备放进这次提交的改动 | `git add` |
| 本地仓库（local repo） | 每次 commit 累积成的本地历史 | `git commit` |
| 远程仓库（remote，GitHub） | 网上的那一份，别人和别的电脑看到的就是它 | `git pull` / `git push` |

四个位置的关系：

```text
工作区 --add--> 暂存区 --commit--> 本地仓库 --push--> 远程仓库
工作区 <--restore-- 暂存区       本地仓库 <--pull---- 远程仓库
```

> 记住一句话：**`add` 只管「选哪些改动」，`commit` 只管「存一个版本」，`push` 才「传上网」。** 三者少一个，文件都不会真的出现在 GitHub 上。

## 1. 普通三部曲：`pull → add + commit → push`

这是我们这个复习仓库（`6442 复习`）日常最常用的流程：**开电脑先拉、改完就存、存好就推。**

### 第 1 步：`pull`——先把远端最新拿下来

```bash
cd ~/Desktop/"桌面 - 陈振宇的MacBook Air (2)"/"6442 复习"
git pull
```

为什么第一步是 pull，而不是直接改？

- 你可能在另一台电脑（或 GitHub 网页上）改过文件，本地还是旧的。
- 如果本地有旧提交、远端也有新提交，直接 `push` 会被拒绝（`non-fast-forward`），还得回头再合并一次。
- 先 pull，冲突在**你面前的这一刻**就暴露出来，好处理；等到 push 时才炸，心理压力大得多。

`git pull` 实际等于 `git fetch` + `git merge`：先下载远端的新提交，再并进当前分支。

### 第 2 步：`add` + `commit`——把改动存成一个版本

```bash
git status                 # 先看清改了哪些文件，红字=未暂存，绿字=已暂存
git add .                  # 把当前目录下所有改动放进暂存区
git commit -m "说明这次改了什么"
```

几个常用变体：

```bash
git add 最终资料/1.2-tokeniser-易读版.md   # 只暂存某一个文件
git add -p                                # 逐个改动块确认要不要暂存（改得很杂时好用）
git diff --staged                         # 提交前再看一眼「这次到底要提交什么」
git commit --amend                        # 提交信息写错/漏了文件，补进上一次提交（还没 push 时可用）
```

`commit -m` 的信息建议写成「**改了什么 + 为什么**」，例如：

- ✅ `修正 T4 参数化测试的 paste 位置说明`
- ❌ `update` / `改了一下` / `111`

理由：以后 `git log` 就是你的改动目录，信息写清楚了，回滚时才找得到「该退回到哪一次」。

### 第 3 步：`push`——传到远端

```bash
git push
```

第一次推一个新分支时，要告诉 Git 推到远端的哪条分支（见第 2 节 `-u`）：

```bash
git push -u origin main
```

### 完整走一遍

```bash
cd ~/Desktop/"桌面 - 陈振宇的MacBook Air (2)"/"6442 复习"

git pull
git status
git add 最终资料/git-使用方式.md
git commit -m "新增 git 使用方式说明"
git push
```

推完用 `git log --oneline -3` 确认最新提交已在最上面，再打开 GitHub 网页看一眼文件在不在——**两次确认，比事后怀疑强。**

## 2. 分支三部曲：`switch -c → commit + push → merge`

什么时候用分支？**不确定改完会不会保留、或者一次要改很多东西、或者想保留 main 随时可用**的时候。

典型场景：想把 2.1 设计模式那章整个重写一遍，但旧版还想留着对照；或者某次改动要跨好几天，中途 main 保持干净。

### 第 1 步：`switch -c`——开一条新分支并切过去

```bash
git switch -c rewrite-design-patterns      # 新建分支 + 立刻切过去
git branch                                  # 确认：当前分支前有 * 号
```

- `-c` = create。`git switch -c 名字` ≈ 老写法 `git checkout -b 名字`（Git 2.23+ 推荐 `switch`，因为它只干「切分支」一件事，不会像 `checkout` 那样误把文件也换掉）。
- 新分支是从**当前所在的分支**分出去的，所以先确认自己在 `main` 上（`git status` 第一行会写 `On branch main`）。
- 分出去之后，两边的提交互不影响：分支上 commit 多少次，`main` 都停在分叉点不动。

### 第 2 步：`commit + push`——在分支上正常存、推到远端

```bash
git add .
git commit -m "重写 2.1：先讲 UML 再看模式"
git push -u origin rewrite-design-patterns
```

`-u`（`--set-upstream`）的作用：把本地分支和远端同名分支**绑定**起来。绑定过一次之后，以后在这个分支上直接 `git push`、`git pull` 就行，不用再写 `origin 分支名`。

分支推上去不会影响 main，也不会影响别人——它只是远端多了一条并行的线。这时 GitHub 页面上可以开 Pull Request 让别人 review，也可以自己确认没问题后再 merge。

### 第 3 步：`merge`——把分支合回 main

合并的方向永远是「**把别人并进我**」：先把要接收的分支切回来，再 merge 那条分支。

```bash
git switch main                 # 回到 main（我要把分支并进这里）
git pull                        # 保险起见，先让 main 是最新的
git merge rewrite-design-patterns
git push                        # 把合并结果推上去
```

- `git merge 分支名` = 把「分支名」的改动并进**当前**分支。
- 如果 main 在分叉后没动过，Git 会做「快进合并」（fast-forward），只是把 main 的指针往前挪，不产生新提交。
- 如果两边都动过，Git 会生成一个合并提交（merge commit），历史里能看到「两条线汇合」的岔口。

合并完成后，远端那条分支就没用了，可以清掉：

```bash
git branch -d rewrite-design-patterns              # 删本地
git push origin --delete rewrite-design-patterns   # 删远端
```

### 完整走一遍

```bash
git switch -c rewrite-design-patterns      # 1. 开分支
git add .
git commit -m "重写 2.1"
git push -u origin rewrite-design-patterns # 2. 存 + 推
git switch main                            # 3. 切回并合并
git pull
git merge rewrite-design-patterns
git push
```

## 3. 冲突（conflict）怎么办

冲突只在**同一文件的同一处**被两边改过时才出现。Git 不会替你猜，它会在文件里写标记：

```text
<<<<<<< HEAD
这是当前分支（比如 main）的内容
=======
这是要并进来的分支的内容
>>>>>>> rewrite-design-patterns
```

处理只有三步：

1. 打开被标记的文件，**手工改成你最终想要的样子**，把 `<<<<<<<`、`=======`、`>>>>>>>` 三行全删掉。
2. `git add 冲突文件`——add 在这里的意思是「我改好了」。
3. `git commit`（合并冲突时通常直接 `git commit` 即可，Git 已备好提交信息）。

不想手工解决、想直接放弃这次合并：`git merge --abort`，回到合并前的状态。

## 4. 命令速查表

| 目的 | 命令 |
|---|---|
| 看当前在哪个分支、改了哪些文件 | `git status` |
| 看还没暂存的改动内容 | `git diff` |
| 看已暂存、即将提交的内容 | `git diff --staged` |
| 拉取并合并远端更新 | `git pull` |
| 暂存全部改动 | `git add .` |
| 提交并写说明 | `git commit -m "..."` |
| 推送到远端 | `git push` |
| 列出本地分支 | `git branch` |
| 新建并切换分支 | `git switch -c 分支名` |
| 切换已有分支 | `git switch 分支名` |
| 首次推送新分支并绑定 | `git push -u origin 分支名` |
| 把某分支并进当前分支 | `git merge 分支名` |
| 看历史（一行一条） | `git log --oneline --graph` |
| 看某文件的改动历史 | `git log -p -- 文件路径` |

## 5. 做错了怎么退

先分清三种「撤销」，别一上来就 `reset --hard`（它会真的删掉你还没提交的改动）。

| 场景 | 命令 | 结果 |
|---|---|---|
| 文件改了但**没 add**，想还原成上次提交的样子 | `git restore 文件名` | 丢弃工作区改动（不可恢复） |
| 已 `add`，想从暂存区拿出来（改动还留着） | `git restore --staged 文件名` | 文件回到「未暂存」状态 |
| 刚 commit，想改说明或补文件 | `git commit --amend` | 改写最后一次提交（**已 push 的别用**） |
| 想撤销上一次 commit，但内容留着再改 | `git reset --soft HEAD~1` | 提交没了，改动回到暂存区 |
| 已 push 的提交要撤销 | `git revert 提交号` | 生成一个「反向提交」，安全，适合共享分支 |

## 6. 本仓库备忘

| 项 | 值 |
|---|---|
| 本地路径 | `~/Desktop/桌面 - 陈振宇的MacBook Air (2)/6442 复习` |
| 远端 | `https://github.com/yourknew78-stack/6442-.git` |
| 主分支 | `main` |
| 提交习惯 | 每完成一段就 commit 一次，信息写清改了什么 |

日常一条龙（复制即用）：

```bash
cd ~/Desktop/"桌面 - 陈振宇的MacBook Air (2)"/"6442 复习" \
  && git pull \
  && git add . \
  && git commit -m "说明这次改了什么" \
  && git push
```

## 7. 一分钟自测

1. `git add` 和 `git commit` 分别把内容从哪搬到哪？
2. 为什么日常要先 `pull` 再改，而不是改完直接 `push`？
3. `git switch -c x` 是从哪个分支分出去的？怎么确认？
4. `git merge x` 是把 `x` 并进谁？
5. `git push -u origin x` 里的 `-u` 省掉了以后哪部分重复输入？
6. 冲突标记 `<<<<<<<` 和 `>>>>>>>` 之间的三段，删哪两行、留什么？

参考答案就在上文各节里，对不上就回去重读那一节。
