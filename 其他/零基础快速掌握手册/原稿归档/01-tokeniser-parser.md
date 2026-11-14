# 01 Tokeniser 和 Parser

> 这个文件管什么题：按题目给的文法（CFG）写 `Tokeniser.next()` 和 `Parser.parse()`，以及文法相关的选择题。
> 历年出现：12 套真题里 11 套考了代码大题（2019 S1 到 2025 S1），2025 S2 只在选择题里考。今年预测：编程题之一，把握最高；选择题里大概率还有一道文法题。

## 目录

- [1 先认题型](#1-先认题型)
- [2 零基础速懂](#2-零基础速懂)
  - [2.1 Token 是什么](#21-token-是什么)
  - [2.2 分词的过程](#22-分词的过程)
  - [2.3 CFG 文法怎么读](#23-cfg-文法怎么读)
  - [2.4 手工判断字符串合不合文法](#24-手工判断字符串合不合文法)
  - [2.5 递归下降是什么](#25-递归下降是什么)
  - [2.6 语法树](#26-语法树)
- [3 套路](#3-套路)
  - [3.1 动手前先读骨架](#31-动手前先读骨架)
  - [3.2 三种骨架风格怎么认怎么写](#32-三种骨架风格怎么认怎么写)
  - [3.3 Tokeniser next 通用模板](#33-tokeniser-next-通用模板)
  - [3.4 Parser 递归下降模板](#34-parser-递归下降模板)
  - [3.5 只返回 boolean 的识别器](#35-只返回-boolean-的识别器)
  - [3.6 选择题套路](#36-选择题套路)
  - [3.7 时间不够先拿部分分](#37-时间不够先拿部分分)
- [4 真题套用](#4-真题套用)
  - [4.1 2025 S2 Q1 MCQ3 fetch 文法](#41-2025-s2-q1-mcq3-fetch-文法)
  - [4.2 2025 S1 Q3 数学表达式](#42-2025-s1-q3-数学表达式)
  - [4.3 2025 S1 Q4 MCQ6 语法树根节点](#43-2025-s1-q4-mcq6-语法树根节点)
  - [4.4 2024 S2 Q3 Git 命令](#44-2024-s2-q3-git-命令)
  - [4.5 2024 S1 Q3 文字处理器输入](#45-2024-s1-q3-文字处理器输入)
  - [4.6 2024 S1 Q4 MCQ7 和 MCQ13](#46-2024-s1-q4-mcq7-和-mcq13)
  - [4.7 2023 S2 Q1 类 SPARQL 查询](#47-2023-s2-q1-类-sparql-查询)
  - [4.8 2023 S1 Q2 Markdown 列表](#48-2023-s1-q2-markdown-列表)
  - [4.9 2022 S1 Q3 LOAD SAVE SUM 命令](#49-2022-s1-q3-load-save-sum-命令)
  - [4.10 2021 S2 Q2 LOAD FROM 命令](#410-2021-s2-q2-load-from-命令)
  - [4.11 2020 S2 Q1 单词分类 Tokenizer](#411-2020-s2-q1-单词分类-tokenizer)
  - [4.12 2020 S1 Q4 SQL INSERT INTO](#412-2020-s1-q4-sql-insert-into)
  - [4.13 2019 S2 Q4 和 2019 S1 Q2 LOGO 语言](#413-2019-s2-q4-和-2019-s1-q2-logo-语言)
  - [4.14 散题 q2 q5 q10 识别器](#414-散题-q2-q5-q10-识别器)
- [5 易错清单](#5-易错清单)

## 1 先认题型

| 题面里出现什么 | 子题型 | 用哪个套路 | 最像的真题 |
|---|---|---|---|
| README 有 `Grammar` 或 CFG，Task A 写 `Tokeniser.next()`，Task B 写 `Parser.parse()`，要求 `Return null if the tokens do not conform with the grammar` | 分词加解析，Parser 返回对象 | 3.1 到 3.4 | 4.2（表达式）、4.4（命令序列） |
| 只有 token 表，没有文法；Parser 方法叫 `getAllTokens()` | 分词加收集 | 3.3，加 3.4 第 1 步 | 4.5 |
| `parseExp()` 返回 `boolean`，注释里写 `recognize the language specified by the following grammar` | 识别器，只判断合不合文法 | 3.5 | 4.14 |
| Parser 要建树，token 带 `level` | 分词加建树 | 3.3，父节点路径表 | 4.8 |
| Parser 拿到 token 后去筛选数据，数据来自 XML | 分词加解析加查询 | 3.3、3.4 | 4.7 |
| 有 `Executor`、`Command` 子类，命令如 `LOAD x FROM f.xml;`，题面写 `All the commands are valid` | 分词加解析成命令对象，再执行 | 3.3、3.4 的序列写法 | 4.9、4.10 |
| 选择题：`which instruction is NOT acceptable for this grammar` | 判断输入合不合文法 | 3.6 | 4.1、4.6 |
| 选择题：`what node would be at the root of the parse tree` | 找语法树根节点 | 3.6 | 4.3 |
| 选择题：`Tokenisation - Select all INCORRECT options` | 概念题 | 2.1、2.2 | 4.6 |

## 2 零基础速懂

### 2.1 Token 是什么

Token（记号）是输入里一个不可再分的小段，由类型（type）和值（value）组成。

输入 `x + 23;` 分成 4 个 token：

| 原文 | type | value |
|---|---|---|
| `x` | `VARIABLE` | `"x"` |
| `+` | `OPERATOR` | `"+"` |
| `23` | `INT` | `"23"` |
| `;` | `EOL` | `";"` |

规则：类型用题目给的枚举（enum）；有的类型没有值（如 `GIT`）；值是字符串还是整数看 `Token` 的构造器。

### 2.2 分词的过程

分词（tokenisation）把字符序列变成 token 序列。做这件事的类叫 Tokeniser，它用字段 `buffer` 保存还没处理的字符串。

每调用一次 `next()` 做五件事：跳过空白；看第一个字符判断类型；读到这个 token 结束；创建 `Token`；把读过的部分从 `buffer` 截掉。

规则：

- 最长匹配：连续数字 `2000` 是一个 token，不是四个。
- Tokeniser 只管每个 token 自己合不合法，不管顺序对不对。`+ + 3` 能分成三个合法 token，顺序错由 Parser 判。
- Tokeniser 不求值。算出 `(1+2)*3` 等于 9 不属于分词。
- 大小写是否敏感（case-sensitive）、哪些字符算空白，每道题规定不同。

### 2.3 CFG 文法怎么读

上下文无关文法（context-free grammar，CFG）是一组规则，规定 token 按什么顺序出现才合法。

```
<SeqExp>    := <SingleExp> | <SingleExp> <EOL> | <SingleExp> <EOL> <SeqExp>
<SingleExp> := <Value> <OP> <SingleExp> | <Value>
<Value>     := <VAR> | <INT>
```

| 记号 | 读法 |
|---|---|
| `:=`、`->`、`→` | 左边可以展开成右边 |
| 竖线 | 或者，几种展开方式任选一种 |
| `<Value>` 或首字母大写的词 | 非终结符（non-terminal），还要继续展开 |
| `+`、`git`、`SELECT` 这类字面量 | 终结符（terminal），输入里必须原样出现 |
| `ε` | 空，什么都不出现 |
| 右边又出现左边的名字 | 递归规则，表示可以重复任意多次 |
| 第一条规则的左边，或题目写的 `start variable`、`initial symbol` | 开始符号，整个输入必须由它展开得到 |

`<SingleExp> := <Value> <OP> <SingleExp> | <Value>` 读作：一个 Value，后面可以跟一个运算符和另一个 SingleExp。也就是 `值 运算符 值 运算符 值` 这样的链。

### 2.4 手工判断字符串合不合文法

例：用 2.3 的文法判断 `x + 3;`。

1. 把输入分成 token：`VAR(x)` `OP(+)` `INT(3)` `EOL(;)`。
2. 从开始符号出发。`<SeqExp>` 的三个分支都以 `<SingleExp>` 开头，先匹配它。
3. `<SingleExp>` 先要一个 `<Value>`，`x` 是 `<VAR>`，匹配。下一个 token 是 `+`，所以走第一个分支，吃掉 `+`，再匹配一个 `<SingleExp>`，得到 `3`。
4. 回到 `<SeqExp>`，下一个是 `;`，匹配 `<EOL>`。
5. token 用完，规则也走完，合法。

不合法的两种情况：规则要求某个 token 但输入里不是它（`x + ;`）；规则走完了但输入还有剩余（`3 4`）。

### 2.5 递归下降是什么

递归下降（recursive descent）是 Parser 的写法：每个非终结符写成一个方法，方法体照着规则的右边写。

方法里只有三种动作：

| 动作 | 含义 | 代码（token 放在 `List` 里时） |
|---|---|---|
| 看（peek） | 看下一个 token，不拿走，用来选分支 | `tokens.get(0)` |
| 吃（consume） | 确认类型后拿走 | `tokens.remove(0)` |
| 调用 | 规则右边出现非终结符，就调用它对应的方法 | `parseSingleExp(tokens)` |

规则右边出现自己，方法就调用自己，这就是递归。

### 2.6 语法树

语法树（parse tree）记录输入是怎样由文法展开得到的。根节点是开始符号，叶子是 token。

`x + y * 5` 按 2.3 的文法得到的结构（右递归，没有乘法优先）：

```
SingleExp
├─ Value x
├─ OP +
└─ SingleExp
   ├─ Value y
   ├─ OP *
   └─ SingleExp
      └─ Value 5
```

对应的对象是 `OpExp(x, ADD, OpExp(y, MULTIPLY, 5))`。结构只由文法决定，不要按数学的先乘后加去改。

## 3 套路

### 3.1 动手前先读骨架

先打开 `Tokeniser.java`、`Token.java`、token 类型枚举、`Parser.java` 和测试文件，按下表逐项确认。

| 要确认的事 | 去哪里看 | 影响什么 |
|---|---|---|
| 构造器里有没有调用 `next()`，有没有 `trim()` | `Tokeniser` 构造器 | 决定骨架风格，见 3.2 |
| `next()` 返回 `void` 还是 `Token` | `next()` 签名 | 每个 `return` 怎么写 |
| 谁负责截 `buffer` | `next()` 里答题区后面的代码，以及 `takeNext()` | 你的代码要不要截 |
| 到结尾时怎么办 | `next()` 的注释、`hasNext()`、`isEmpty()` | 通常 `currentToken = null` |
| 遇到非法输入怎么办 | `next()` 的注释 | 近年都是 `currentToken = null`，`buffer` 保持不变 |
| `Token` 有哪些构造器，值是 `String` 还是 `int` | `Token.java` | `new Token(...)` 怎么写 |
| 类型枚举有哪些 | 枚举文件 | 每个枚举值都要有一个分支 |
| 测试怎样比较 token | 测试文件里的 `toString()` 或 `equals` | 值要不要保留原文大小写 |
| Parser 能调用 Tokeniser 的哪些方法 | `Tokeniser` 的 public 方法 | 3.4 第 1 步怎么收集 token |
| 空白、大小写、非法输入的规定 | README 的 Notes | 分支条件 |
| 能不能加辅助方法 | README 的 Note 和卷首 README | 不能加就把辅助方法内联 |

### 3.2 三种骨架风格怎么认怎么写

| 风格 | 怎么认 | `next()` 要做什么 | Parser 怎么取 token | 出现在 |
|---|---|---|---|---|
| A 预读式 | 构造器最后一行是 `this.next()`；有 `getCurrentToken()` 或 `current()` | 找到 token，自己截 `buffer`，存进 `currentToken` | 先读 `getCurrentToken()`，再调 `next()` 前进 | 2024 S1、2024 S2、2022 S1、2019 S2 |
| A2 预读式，骨架代截 | 同 A，但答题区前面已有 `buffer.trim()`，后面已有 `buffer.substring(tokenLen)` | 只给 `currentToken` 赋值，不截 | 同 A | 2021 S2、2020 S2 |
| B 偷看加取走 | 有 `takeNext()`；`next()` 注释写 `without changing the buffer`，或 `takeNext()` 里自己截 `buffer` | 只识别并返回，不改 `buffer` | 调 `takeNext()` | 2025 S1、2020 S1、2019 S1、散题 |
| C 直接取 | 没有 `currentToken` 预读；`hasNext()` 只看 `buffer` 是否为空 | 找到 token，自己截 `buffer`，返回它 | `while (hasNext()) next()` | 2023 S1、2023 S2 |

风格 A2 和 B 由骨架按 token 的字符串长度截 `buffer`，所以 token 里存的字符串必须和原文一模一样，不能转大小写，不能带空格。

### 3.3 Tokeniser next 通用模板

适用：所有要写 `next()` 的题。模板按风格 A 写，其他风格只改第 1、2、5 步，见代码后的表。

步骤：

1. 跳过题目允许的空白。
2. 如果到了结尾，按骨架要求收尾。
3. 看首字符 `c`，分支判断类型，算出 token 的结束位置 `j`。数字和单词用 `while` 读到最长。
4. 所有分支都不匹配，是非法输入，`currentToken = null`，不动 `buffer`。
5. 成功才截掉 `buffer` 的前 `j` 个字符。

```java
public Token next() {
    // 1 跳过空白
    int i = 0;
    while (i < buffer.length() && buffer.charAt(i) == ' ') i++;
    // 2 到结尾
    if (i == buffer.length()) {
        buffer = "";
        currentToken = null;
        return null;
    }
    // 3 按首字符分支；token 是 buffer 的第 i 到第 j-1 个字符
    char c = buffer.charAt(i);
    int j = i + 1;
    Token token = null;
    if (c == ';') {                                   // 单字符符号
        token = new Token(TokenType.SEMI, ";");
    } else if (c == '+') {
        token = new Token(TokenType.PLUS, "+");
    } else if (c >= '0' && c <= '9') {                // 数字读到最长
        while (j < buffer.length() && Character.isDigit(buffer.charAt(j))) j++;
        token = new Token(TokenType.INT, buffer.substring(i, j));
    } else if (c >= 'a' && c <= 'z') {                // 单词读到最长，再比对关键字
        while (j < buffer.length() && buffer.charAt(j) >= 'a' && buffer.charAt(j) <= 'z') j++;
        String word = buffer.substring(i, j);
        if (word.equals("print")) token = new Token(TokenType.PRINT, word);
        else token = new Token(TokenType.WORD, word);
    }
    // 4 非法输入：buffer 不动
    if (token == null) {
        currentToken = null;
        return null;
    }
    // 5 成功：截掉已读部分
    buffer = buffer.substring(j);
    currentToken = token;
    return token;
}
```

按风格改写：

| 风格 | 第 1、2 步 | 第 5 步 |
|---|---|---|
| A，`next()` 返回 `void` | 不变 | 三处 `return null` 和 `return token` 都改成 `return` |
| A2 | 骨架已做，删掉，令 `i = 0` | 只写 `currentToken = token`，不截 |
| B | 看构造器和 `takeNext()` 有没有 `trim()`；有就令 `i = 0`，到结尾返回 `null` | 不截 `buffer`，`currentToken = token; return token` |
| C | 不变，去掉 `currentToken` 相关行 | `buffer = buffer.substring(j); return token` |

常用的分支写法（片段）：

| 需要 | 写法 |
|---|---|
| 固定字符串，如 `[CTRL][Z]` | `buffer.startsWith("[CTRL][Z]", i)`，然后 `j = i + 9` |
| 固定字符串，不分大小写 | `buffer.regionMatches(true, i, "[CTRL][Z]", 0, 9)` |
| 关键字不分大小写 | `word.equalsIgnoreCase("load")` |
| 英文字母 | `(c >= 'a' && c <= 'z') \|\| (c >= 'A' && c <= 'Z')`。`Character.isLetter` 会把中文也算进去 |
| 读到空格或分隔符为止 | `while (j < buffer.length() && buffer.charAt(j) != ' ' && buffer.charAt(j) != ';') j++;` |
| 数字转整数 | `Integer.parseInt(text)`，数字太长会抛 `NumberFormatException`，要 `try` 住 |

### 3.4 Parser 递归下降模板

适用：给了文法，`parse()` 返回对象，不合文法返回 `null`。

步骤：

1. 把所有 token 收进一个 `List<Token>`。收集时遇到非法 token 直接 `return null`。写法按骨架风格选，见下表。
2. 每个非终结符写一个辅助方法，参数是这个 `List`，返回解析出的对象，失败返回 `null`。位置信息就存在 `List` 里（吃掉一个就 `remove(0)`），不需要加字段。
3. 方法体照规则右边写：终结符就吃掉并检查类型；非终结符就调用对应方法；有多个分支就先看下一个 token 再决定。
4. 每次 `get(0)` 或 `remove(0)` 之前先判断 `tokens.isEmpty()`。
5. 每个调用的返回值都检查，是 `null` 就立刻 `return null`。
6. 开始符号解析完后，`List` 里还有剩余 token 就 `return null`。

第 1 步的三种写法（片段）：

```java
List<Token> tokens = new ArrayList<>();
// 风格 A：当前 token 已预读
while (tokeniser.getCurrentToken() != null) {
    tokens.add(tokeniser.getCurrentToken());
    tokeniser.next();
}
if (!tokeniser.isEmpty()) return null;        // 停下时 buffer 还有内容，说明遇到非法 token

// 风格 B：next() 偷看，takeNext() 取走
while (tokeniser.hasNext()) {
    if (tokeniser.next() == null) return null; // 先偷看，非法就整体无效
    tokens.add(tokeniser.takeNext());
}

// 风格 C：next() 直接取
while (tokenizer.hasNext()) {
    Token t = tokenizer.next();
    if (t == null) return null;
    tokens.add(t);
}
```

风格 A 的骨架如果没有 `isEmpty()`，说明题目保证输入合法（2022 S1、2021 S2），去掉那一行。

示例文法和对应的完整 Parser：

```
<Stmts> := <Stmt> ; | <Stmt> ; <Stmts>
<Stmt>  := print <Exp>
<Exp>   := <Value> + <Exp> | <Value>
<Value> := <INT> | <WORD>
```

```java
import java.util.ArrayList;
import java.util.List;

public class Parser {
    Tokeniser tokeniser;

    public Parser(Tokeniser tokeniser) { this.tokeniser = tokeniser; }

    public List<String> parse() {
        List<Token> tokens = new ArrayList<>();
        while (tokeniser.getCurrentToken() != null) {      // 第 1 步，风格 A
            tokens.add(tokeniser.getCurrentToken());
            tokeniser.next();
        }
        if (!tokeniser.isEmpty()) return null;
        return parseStmts(tokens);
    }

    // <Stmts> := <Stmt> ; | <Stmt> ; <Stmts>   每项后面必须有分隔符
    private List<String> parseStmts(List<Token> tokens) {
        List<String> result = new ArrayList<>();
        if (tokens.isEmpty()) return null;                 // 至少要有一项
        while (!tokens.isEmpty()) {                        // 循环到 token 用完，不会有剩余
            String stmt = parseStmt(tokens);
            if (stmt == null) return null;
            if (tokens.isEmpty() || tokens.remove(0).getType() != TokenType.SEMI) return null;
            result.add(stmt);
        }
        return result;
    }

    // <Stmt> := print <Exp>   固定开头的规则
    private String parseStmt(List<Token> tokens) {
        if (tokens.isEmpty() || tokens.remove(0).getType() != TokenType.PRINT) return null;
        String exp = parseExp(tokens);
        if (exp == null) return null;
        return "print " + exp;
    }

    // <Exp> := <Value> + <Exp> | <Value>   右递归
    private String parseExp(List<Token> tokens) {
        String left = parseValue(tokens);
        if (left == null) return null;
        if (tokens.isEmpty() || tokens.get(0).getType() != TokenType.PLUS) return left; // 第二个分支
        tokens.remove(0);                                  // 吃掉 +
        String right = parseExp(tokens);                   // 右边递归
        if (right == null) return null;
        return "(" + left + " + " + right + ")";
    }

    // <Value> := <INT> | <WORD>   几种 token 任选一种
    private String parseValue(List<Token> tokens) {
        if (tokens.isEmpty()) return null;
        Token t = tokens.remove(0);
        if (t.getType() == TokenType.INT || t.getType() == TokenType.WORD) return t.getValue();
        return null;
    }
}
```

序列规则的三种形态，只是循环体不同（片段，`item` 是一项的解析结果）：

| 文法形态 | 含义 | 循环写法 |
|---|---|---|
| `S := A ; \| A ; S` | 每项后面必须有分隔符 | 上面的 `parseStmts` |
| `S := A \| A ; \| A ; S` | 末尾分隔符可有可无 | 见下方代码 |
| `S := A \| A , S` | 分隔符只能夹在两项中间 | 下方代码删掉最后一个 `break` 行，改成 `if (tokens.isEmpty()) return null;` |

```java
while (true) {                                         // 末尾分隔符可有可无
    String item = parseItem(tokens);
    if (item == null) return null;
    result.add(item);
    if (tokens.isEmpty()) break;                       // 末尾没有分隔符
    if (tokens.remove(0).getType() != TokenType.SEMI) return null;
    if (tokens.isEmpty()) break;                       // 末尾有分隔符
}
```

题目不允许加辅助方法时：序列规则本来就是循环，直接写在 `parse()` 里；右递归规则改成先用循环把值和运算符依次收进两个 `List`，再从最右边往左两两组合。

`import` 写在文件顶部。不想动答题区以外的地方，就写全名 `java.util.List`、`java.util.ArrayList`。

### 3.5 只返回 boolean 的识别器

适用：`parseExp()` 返回 `true` 或 `false`，骨架是风格 B，带 `isNextValid()`。

骨架已写好开头两行：下一个 token 非法就 `false`；没有 token 了就 `true`。你的代码接在后面。

步骤：

1. `takeNext()` 取一个 token，检查类型，不对就 `return false`。
2. 用 `isNextValid()` 和 `hasNext()` 判断后面的情况：非法返回 `false`；没有了，看文法允不允许在这里结束。
3. 文法要求后面还有内容时，先确认 `hasNext()` 为真，再递归调用 `parseExp()`。骨架开头把「没有 token」当作 `true`，不先确认的话 `2+` 会被误判为合法。

模板，文法 `S := Y X S | Y`（片段，接在骨架两行之后）：

```java
Token y = _tokeniser.takeNext();
if (y.type != Token.Type.NUMBER) return false;      // S 必须以 Y 开头
if (!_tokeniser.isNextValid()) return false;        // 后面是非法字符
if (!_tokeniser.hasNext()) return true;             // S := Y，到此结束
Token x = _tokeniser.takeNext();
if (x.type != Token.Type.MATH_OPERATION) return false;
if (!_tokeniser.hasNext()) return false;            // X 后面必须还有 S
return parseExp();                                  // 递归解析剩下的 S
```

### 3.6 选择题套路

判断某个输入合不合文法：

1. 从开始符号的规则入手，把其中的终结符列出来，这些词必须按顺序原样出现在输入里。
2. 逐个选项对照，先找缺了终结符或多了词的选项。
3. 再检查非终结符的位置：用 `|` 列出了候选的，输入必须是候选之一；没有列出候选的（如 `<string_value>`），任意同类值都行。
4. 有递归规则的位置可以重复多次，重复不算错。
5. 看清题目问的是 `NOT acceptable` 还是 acceptable，是单选还是 `select all`。

找语法树的根节点：

1. 根节点一定是开始符号对应的节点，先排除所有不是开始符号的选项。
2. 把开始符号的规则套到整个输入上，确定最外层怎样切分。
3. 从句、括号里的内容都在更深的层，不会出现在根上。

### 3.7 时间不够先拿部分分

1. 先保证能编译。没写完的方法保留骨架原来的 `return null`。
2. 先写 Tokeniser。它的测试和 Parser 的测试分开算，而且 Parser 依赖它。
3. Tokeniser 里先写单字符符号和关键字，再写数字和单词，最后处理非法输入。
4. Parser 先让最简单的输入通过（单个值、单条命令），再加序列，最后加「不合文法返回 `null`」。
5. 每写完一步就跑一次题目给的测试。

## 4 真题套用

所有答案都是非官方答案。代码题的解答都按原题骨架编译并跑过原题测试。

### 4.1 2025 S2 Q1 MCQ3 fetch 文法

**题意**：给一个查询文法，问四条指令里哪一条不合文法（单选）。原题：[MCQQuestions.pdf](../exam-practice-questions/past-exams/2025-2/src/Q1/MCQQuestions.pdf) 第 3 题。

```
<command>    := fetch <fields> in <table_name> having id = <id_value>
<fields>     := <field>, <fields> | <field>
<table_name> := <string_value>
<id_value>   := <int_value>
<field>      := <string_value>
```

**套路对应**：3.6 判断合不合文法。`<command>` 里的终结符依次是 `fetch`、`in`、`having`、`id`、`=`，每个选项都要有这五个词。

**推导**：

| 选项 | 指令 | 判断 |
|---|---|---|
| a | `fetch name, age in Person having id = -12` | 合法。`<fields>` 递归展开成两个字段；`-12` 是整数 |
| b | `fetch street in Address having id = 1` | 合法。`<fields>` 走单个 `<field>` 分支 |
| c | `fetch name, age, height in Person having id 2` | 不合法。`id` 后面缺终结符 `=` |
| d | `fetch name in Person having id = 4` | 合法 |

答案：c（非官方答案）。

**坑**：a 里的负数是干扰项。文法没有规定 `<int_value>` 必须非负，而 c 明确缺了 `=`，单选题选 c。

### 4.2 2025 S1 Q3 数学表达式

**题意**：输入是 `x + 3; y - x + 5; y;` 这样的表达式串。Task A 写 `Tokeniser.next()`，分出 `INT`、`VARIABLE`、`OPERATOR`、`EOL` 四种 token。Task B 写 `Parser.parse()`，生成 `IntExp`、`VarExp`、`OpExp`、`SeqExp`，不合文法返回 `null`。变量只能是单个小写字母（大小写敏感）；token 之间可以有空白，也可以没有。可以加辅助方法，不能加字段。原题：[Q3README.md](../exam-practice-questions/past-exams/2025-1/src/Q3/Q3README.md)。

```
<SeqExp>    := <SingleExp> | <SingleExp> <EOL> | <SingleExp> <EOL> <SeqExp>
<SingleExp> := <Value> <OP> <SingleExp> | <Value>
<Value>     := <VAR> | <INT>
<VAR> := a 到 z    <INT> := an integer    <OP> := + | - | *    <EOL> := ;
```

**套路对应**：

- 3.1 读骨架：风格 B。构造器 `trim()`；`takeNext()` 先调 `next()`，再按 `getValue().length()` 截 `buffer` 并 `trim()`。所以 `next()` 不截 `buffer`，token 的值必须是原文。
- 3.3：开头不会有空白，`i = 0`。数字读到最长；变量、运算符、分号都只占一个字符。
- 3.4：第 1 步用风格 B 的收集写法。`<SeqExp>` 是末尾分隔符可有可无的序列，`<SingleExp>` 是右递归。

**关键代码**：

```java
// Tokeniser.java
public Token next() {
    if (buffer.isEmpty()) {                    // 到结尾
        currentToken = null;
        return null;
    }
    char c = buffer.charAt(0);
    Token token = null;
    if (c >= '0' && c <= '9') {                // INT：数字读到最长
        int j = 1;
        while (j < buffer.length() && buffer.charAt(j) >= '0' && buffer.charAt(j) <= '9') j++;
        token = new Token(TokenType.INT, buffer.substring(0, j));
    } else if (c >= 'a' && c <= 'z') {         // VAR：一个小写字母
        token = new Token(TokenType.VARIABLE, String.valueOf(c));
    } else if (c == '+' || c == '-' || c == '*') {
        token = new Token(TokenType.OPERATOR, String.valueOf(c));
    } else if (c == ';') {
        token = new Token(TokenType.EOL, ";");
    }
    currentToken = token;                      // 非法字符时是 null；buffer 始终不动
    return token;
}
```

```java
// Parser.java，文件顶部加 import java.util.ArrayList; import java.util.List;
public Exp parse() {
    List<Token> tokens = new ArrayList<>();
    while (tokeniser.hasNext()) {
        if (tokeniser.next() == null) return null;     // 先偷看：非法字符，整体无效
        tokens.add(tokeniser.takeNext());
    }
    return parseSeqExp(tokens);
}

private Exp parseSeqExp(List<Token> tokens) {
    List<Exp> exps = new ArrayList<>();
    while (true) {                                     // 末尾分隔符可有可无
        Exp exp = parseSingleExp(tokens);
        if (exp == null) return null;
        exps.add(exp);
        if (tokens.isEmpty()) break;                   // 末尾没有 ;
        if (tokens.remove(0).getType() != TokenType.EOL) return null;
        if (tokens.isEmpty()) break;                   // 末尾有 ;
    }
    if (exps.size() == 1) return exps.get(0);          // 只有一个表达式，直接返回它
    return new SeqExp(exps);
}

private Exp parseSingleExp(List<Token> tokens) {
    if (tokens.isEmpty()) return null;
    Token value = tokens.remove(0);
    Exp left;
    if (value.getType() == TokenType.INT) {
        try {
            left = new IntExp(Integer.valueOf(value.getValue()));
        } catch (NumberFormatException e) {            // 数字太长，超出 int
            return null;
        }
    } else if (value.getType() == TokenType.VARIABLE) {
        left = new VarExp(value.getValue());
    } else {
        return null;
    }
    if (tokens.isEmpty() || tokens.get(0).getType() != TokenType.OPERATOR) return left;
    String op = tokens.remove(0).getValue();           // 吃掉运算符
    Exp right = parseSingleExp(tokens);                // 右边递归
    if (right == null) return null;
    OpExp.OpType type = OpExp.OpType.ADD;
    if (op.equals("-")) type = OpExp.OpType.SUBTRACT;
    if (op.equals("*")) type = OpExp.OpType.MULTIPLY;
    return new OpExp(left, type, right);
}
```

**坑**：

- `next()` 的注释写了 `update the buffer`，但 `takeNext()` 自己会截。`next()` 里再截一次会丢 token，原题测试 `tokenise_evaluation` 过不了。以代码为准。
- `next()` 返回 `null` 时 `takeNext()` 会抛 `NullPointerException`，所以 Parser 里先调 `next()` 偷看。
- 结果是右结合：`x + y * 5 + 2 - 1` 得到 `(x ADD (y MULTIPLY (5 ADD (2 SUBTRACT 1))))`。
- `3 4`、`x +`、`A + 1`、`x;;`、`xy`、空输入都返回 `null`。
- 不确定：只有一个表达式时（`3` 或 `y;`）返回它本身还是包一层 `SeqExp`，题面没说，两种写法 `show()` 的输出相同，这里返回它本身。负数（`-3`）题面也没说，这里 `-` 一律当运算符。

### 4.3 2025 S1 Q4 MCQ6 语法树根节点

**题意**：文法的开始符号是 `sentence`。Tokeniser 忽略大小写和标点，Parser 是递归下降。问输入 `Dogs chase gray rabbits that talk to white rabbits.` 的语法树根节点是什么（单选）。原题：[MC_Questions_2025_S1.pdf](../exam-practice-questions/past-exams/2025-1/src/Q4/MC_Questions_2025_S1.pdf) Q6。

```
sentence    → noun_phrase verb noun_phrase
noun_phrase → adjective noun_phrase | noun | noun_phrase "that" verb noun_phrase
noun → "cats" | "dogs" | "rabbits" | "people"      adjective → "white" | "black" | "gray" | "brown"
verb → "like" | "chase" | "talk to" | "see"
```

选项：a. Sentence，verb 是 chase；b. Sentence，verb 是 talk to；c. Verb chase；d. Verb talk to。

**套路对应**：3.6 找根节点。

**推导**：

1. 根节点是开始符号 `sentence`，排除 c 和 d。
2. 把 `sentence → noun_phrase verb noun_phrase` 套到整句上。第一个 `noun_phrase` 只能是 `Dogs`，因为 `Dogs chase` 不是名词短语。所以根上的 verb 是 `chase`。
3. 剩下的 `gray rabbits that talk to white rabbits` 是第二个 `noun_phrase`，由 `noun_phrase "that" verb noun_phrase` 展开，`talk to` 在这一层里面。

答案：a（非官方答案）。

**坑**：`talk to` 在 `that` 从句里，层次更深，不在根上。

### 4.4 2024 S2 Q3 Git 命令

**题意**：输入是一串简化 Git 命令，例如 `git add ./src/Q1; git commit;`。Task A 写 `Tokeniser.next()`，分出 `GIT`、`ADD`、`COMMIT`、`PUSH`、`PATH`、`SEPARATOR`。`PATH` 以 `.` 开头，到空格或 `;` 为止，值要保留。Task B 写 `Parser.parse()`，返回 `List<GitCommand>`，不合文法返回 `null`。关键字大小写敏感。相邻 token 可以贴在一起，也可以隔一个或多个空格。可以在这两个类里加辅助方法，不能加字段。原题：[Q3README.md](../exam-practice-questions/past-exams/2024-2/src/Q3/Q3README.md)。

```
<GitCommands> := <GitCommand><Sep> | <GitCommand><Sep> <GitCommands>
<GitCommand>  := <GitAddCommand> | <GitCommitCommand> | <GitPushCommand>
<GitAddCommand> := git add <path>
<GitCommitCommand> := git commit
<GitPushCommand> := git push
<path> := .<any_characters_without_space/separator>
<Sep> := ;
```

**套路对应**：

- 3.1：风格 A。构造器里已经调用 `next()`。`next()` 返回 `GitToken`，自己截 `buffer`。非法时 `currentToken = null`，`buffer` 不动。
- 3.3：先跳过空格。`;` 单独成 token。以 `.` 开头的读到空格或 `;`，类型是 `PATH`。其余读到空格或 `;`，整词必须正好等于 `git`、`add`、`commit`、`push`。
- 3.4：第 1 步用风格 A 的收集写法，停下时 `buffer` 还有内容就 `return null`。`<GitCommands>` 是每项后面必须有 `;` 的序列。

**关键代码**：

```java
// Tokeniser.java
public GitToken next() {
    int i = 0;
    while (i < buffer.length() && buffer.charAt(i) == ' ') i++;
    if (i == buffer.length()) {                 // 只剩空格，当成结尾
        buffer = "";
        currentToken = null;
        return null;
    }
    char c = buffer.charAt(i);
    int j = i + 1;
    GitToken token = null;
    if (c == ';') {
        token = new GitToken(GitTokenType.SEPARATOR);
    } else if (c == '.') {                      // 路径读到空格或分号前
        while (j < buffer.length() && buffer.charAt(j) != ' ' && buffer.charAt(j) != ';') j++;
        token = new GitToken(GitTokenType.PATH, buffer.substring(i, j));
    } else {
        while (j < buffer.length() && buffer.charAt(j) != ' ' && buffer.charAt(j) != ';') j++;
        String word = buffer.substring(i, j);  // 整词匹配，不用 startsWith
        if (word.equals("git")) token = new GitToken(GitTokenType.GIT);
        else if (word.equals("add")) token = new GitToken(GitTokenType.ADD);
        else if (word.equals("commit")) token = new GitToken(GitTokenType.COMMIT);
        else if (word.equals("push")) token = new GitToken(GitTokenType.PUSH);
    }
    if (token == null) {                        // 非法：buffer 保持原样
        currentToken = null;
        return null;
    }
    buffer = buffer.substring(j);
    currentToken = token;
    return token;
}
```

```java
// Parser.java，文件里已有 import java.util.ArrayList 和 List
public List<GitCommand> parse() {
    List<GitToken> tokens = new ArrayList<>();
    while (tokeniser.hasNext()) {
        tokens.add(tokeniser.getCurrentToken());
        tokeniser.next();
    }
    if (!tokeniser.isEmpty()) return null;      // 中途遇到非法词
    return parseCommands(tokens);
}

private List<GitCommand> parseCommands(List<GitToken> tokens) {
    if (tokens.isEmpty()) return null;          // 至少一条命令
    List<GitCommand> result = new ArrayList<>();
    while (!tokens.isEmpty()) {
        GitCommand command = parseCommand(tokens);
        if (command == null) return null;
        if (tokens.isEmpty() || tokens.remove(0).getType() != GitTokenType.SEPARATOR) return null;
        result.add(command);
    }
    return result;
}

private GitCommand parseCommand(List<GitToken> tokens) {
    if (tokens.isEmpty() || tokens.remove(0).getType() != GitTokenType.GIT) return null;
    if (tokens.isEmpty()) return null;
    GitToken op = tokens.remove(0);
    if (op.getType() == GitTokenType.ADD) {
        if (tokens.isEmpty() || tokens.get(0).getType() != GitTokenType.PATH) return null;
        return new GitCommand(GitCommand.GitCommandType.GIT_ADD, tokens.remove(0).getValue());
    }
    if (op.getType() == GitTokenType.COMMIT) return new GitCommand(GitCommand.GitCommandType.GIT_COMMIT);
    if (op.getType() == GitTokenType.PUSH) return new GitCommand(GitCommand.GitCommandType.GIT_PUSH);
    return null;
}
```

**坑**：

- `github`、`gitcommit` 是一整词，不等于 `git`。应返回 `null`，并且 `buffer` 仍是原字符串。`startsWith("git")` 会把 `github` 切成 `GIT` 再剩 `hub`，不符合「space-delimited string 对不上就整段留下」。
- README 的 `git add .abcdegit commit;` 只说明 path 在空格处停下。token 是 `GIT`、`ADD`、`PATH(.abcdegit)`、`COMMIT`、`SEPARATOR`。`add` 和后面的 `commit` 之间没有 `;`，Parser 返回 `null`。
- 每条命令后面都必须有一个 `;`。`git commit`、`git commit;;`、`git add ;`、空串都返回 `null`。`git    push   ;` 合法。`Git` 不合法。
