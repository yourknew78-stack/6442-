# 02｜测试：每个用例为什么存在

> 写测试先确定输入和期望结果，再看题目要求覆盖代码路径，还是核对文字规格；“一个用例”怎样计数，以原题为准。

## 1. 认题型：拿到题后照着做

先别背“白盒”“黑盒”这些词。把题目的要求翻译成：**我要给方法什么输入？它应该返回什么？我为什么需要这一组输入？**下面从一段能手算的代码开始。

### 情况 A：题目写 `branch complete`

**它是什么意思？**`branch` 是“分支”，指程序遇到一个判断后可能走的不同方向。例如 `if (x > 0)` 有两条路：条件成立，走进 `if`；条件不成立，跳过 `if`。`complete` 是“覆盖完整”。因此 **branch complete（分支覆盖完整）**就是：每条能走的判断方向，至少用一组输入走过一次。测试全部显示通过，也不代表所有方向都走到了。因为要看源码里的判断，这属于**白盒测试**：“盒子”里的代码是看得见的。

题目若还写 **minimum**，意思是“在完成覆盖的前提下，使用最少的用例”；它不是另一种覆盖类型。

假设原题给了这个方法，让你测试它：

```java
static int sign(int x) {
    if (x > 0) {        // 问：x 是否大于 0？
        return 1;       // 回答“是”才走到这里
    }
    return 0;           // 回答“否”才走到这里
}
```

`x` 是你传进去的数字；`return` 是方法给你的结果。把输入**一行一行代入**：

1. 传 `x=5`：`5 > 0` 是真，执行 `return 1`。所以期望结果是 `1`。
2. 传 `x=0`：`0 > 0` 是假，跳过花括号，执行 `return 0`。所以期望结果是 `0`。

测试可以写成：

```java
assertEquals(1, sign(5)); // 期望 1；实际调用 sign(5)；覆盖“真”
assertEquals(0, sign(0)); // 期望 0；实际调用 sign(0)；覆盖“假”
```

这就叫**分支覆盖**：同一个 `if` 问题，回答“是”和“否”的路各走一次。只测 `5`，即使测试通过，仍没有检查“否”这条路。

如果题目还写 `minimum`（最少），这两组已经够了。再加 `sign(8)` 只会重复走“真”，用例变多但没有覆盖新路。**先找到所有需要走的路，再删去只重复旧路的输入。**遇到多个 `if` 或 `&&`、`||` 时，照第 3 节逐个画真 / 假格子；一个输入可能同时覆盖多个格子。

### 情况 B：题目写 `statement coverage` 或 `code complete`

**它是什么意思？**`statement` 是“语句”，就是程序实际执行的动作，例如“把 `x` 加 1”或“返回 `x`”。**statement coverage（语句覆盖）**要求目标语句都至少执行一次。它只问“这句有没有执行过”，不要求每个判断的真、假方向都试过。本手册涉及的历年题中，`code complete` 也按这个语句覆盖要求处理；做新题仍要先读原题的具体定义。

换成这段代码：

```java
static int plusOneIfPositive(int x) {
    if (x > 0) {
        x = x + 1;      // 语句①
    }
    return x;           // 语句②
}
```

如果只要求**语句覆盖**，传 `x=5`：条件是真，语句①把 `5` 改成 `6`，语句②返回 `6`。一次调用就执行了这两条语句：

```java
assertEquals(6, plusOneIfPositive(5));
```

这里不用再传 `0`，因为题目只要求“每条语句执行过”，没有要求 `if` 的真、假两条路都走过。如果同一段代码改问 **branch complete**，就还要加 `assertEquals(0, plusOneIfPositive(0))`，专门走“假”路。**先看题目问的是“语句”还是“分支”，答案可能不同。**

### 情况 C：题目给多个实现，要求正确版通过、错误版失败

**它是什么意思？**“实现”（implementation）指同一个功能的**不同代码版本**：对外的方法一样，内部写法不同，其中一部分是正确的，一部分被故意改坏。题目给你一批这样的版本，让你写一份测试；评分时把你的测试分别跑在每一个版本上——正确版本必须全部通过，错误版本至少要有一条失败。这类题通常叫**黑盒测试**，原因见本节末尾。

**先用一段能手算的规则看结构。**README 写：“订单金额达到 100 元，运费为 0；不足 100 元，运费为 10 元。”按文字算出两个期望值，写成两条断言：

```java
assertEquals(10, shipping(99));  // 99 不到 100，应付 10
assertEquals(0, shipping(100));  // 100 已达到门槛，应付 0
```

假设题目给了四个版本的 `shipping`，评分时每一版都要跑这两条：

| 版本 | 内部条件 | `shipping(99)` 期望 10 | `shipping(100)` 期望 0 | 该版本跑完 | 应判 |
|---|---|---|---|---|---|
| 正确版 | `amount >= 100` | 10，通过 | 0，通过 | 全部通过 | 正确 |
| 错误甲 | `amount > 100` | 10，通过 | 10，**失败** | 一条失败 | 错误 |
| 错误乙 | `amount >= 90` | 0，**失败** | 0，通过 | 一条失败 | 错误 |
| 错误丙 | 一律返回 0 | 0，**失败** | 0，**失败** | 两条都失败 | 错误 |

“至少有一条失败”里的“至少”就在这张表里：错误甲只失败一条，照样算被抓到，不需要每条都失败。反过来，若某个错误版本在你的**所有**断言上全部通过，说明你没抓到它，要扣分。`99` 和 `100` 也不是随便选的：它们卡在“达到”与“超过”的分界上，只测其中一个，错误甲或错误乙就会漏过去。

**再看它在文件里长什么样。**以 [2024 S2 Q2](../../exam-practice-questions/past-exams/2024-2/src/Q2/Q2README.md) 为例：

```
Q2/HashingBox.java                        抽象父类，定义 hash(Thing) 这个功能
Q2/HashingBox0.java ~ HashingBox12.java   13 个版本，都实现同一个 hash
Q2/Q2Test.java                            唯一要交的文件
```

13 个类对外提供同一个 `hash(Thing)`，从调用方看不出区别；README 说明其中有 1～3 个是正确的。评分程序把你的 `Q2Test` 跑 13 遍，每遍换一个版本，于是得到 13 个结果：

| 某个版本跑完的结果 | 等于你判定它 |
|---|---|
| 全部通过 | 正确 |
| 至少一条失败 | 错误 |

13 个版本全部判对才有满分。所以你要做的是：**让正确版本全绿，让每个错误版本至少红一条。**

**为什么不能从代码里抄答案。**13 份代码里有对有错，你事先并不知道哪份对。若拿 `HashingBox3` 跑出的数字当期望值，而它恰好是错的，你就把错误答案写进了断言，正确版本反而变红。原题写明：把正确版本判错，比放过一个错误版本扣得更多。期望值只有一个来源——**README 的文字规则加自己手算**。

**这就是黑盒。**“盒子”指被测代码。白盒是打开盒子，照着 `if`、循环设计输入（情况 A、B 的做法）；黑盒是不打开盒子，只从外面喂输入、看输出，按说明书判对错。13 个盒子没法逐个打开读，所以 2024 S2 Q2 直接写 *STRONGLY ADVISED to perform Black Box Testing*。

**这类题的动手顺序。**

1. 把 README 的规则抄成短句，圈出“怪规则”。2024 S2 有三个：`size == 1` 时 `sizeHash` 是 `2`（不是 1）；`nameHash` 只算前 6 个字符、且只算字母；`size` 越界要抛 `ComponentOutOfRangeException`。错误版本往往就栽在这些地方。
2. 每条规则配一个**最小可区分输入**：这条规则被写错时，结果一定会跟着变。
3. 手算期望值，写成断言，每条注明“它查的是哪条规则”。
4. 刻意挑边界：`size` 取 `1`、`2`、`31`、`32`；名字取正好 6 个字符和 7 个字符；名字里混数字、空格；material 与 shape 取两端值。
5. 断言写完后回头自查：有没有哪个版本可能所有断言都通过？有，就补一条。

### 情况 D：题目明确要求某种异常

**它是什么意思？**“异常”是程序遇到不允许的情况时发出的错误信号，例如商品数量传了负数。题目若说“负数数量要抛 `IllegalArgumentException`”，测试就要确认：传入负数时，方法确实发出这个指定类型的错误信号，而不是正常返回一个结果。

待测的是一个**“输入负数会抛异常”的计算器**：每件商品 10 元，**数量为负就报错**。我们要写的**测试，就是验证它拿到负数时确实会抛**，而不是照常返回一个价格。实现如下：

```java
static int calculatePrice(int quantity) {
    if (quantity < 0) {
        throw new IllegalArgumentException();
    }
    return quantity * 10;
}
```

三处要点：`if (quantity < 0)` 判断参数是否非法；`throw` 是主动抛出；`IllegalArgumentException` 是“非法参数”异常，表示传进来的参数不合规。于是：

| 调用 | 行为 |
|---|---|
| `calculatePrice(3)` | 正常返回 `30` |
| `calculatePrice(-1)` | 抛出 `IllegalArgumentException` |

**如何实现测试抛出异常。**JUnit 4 把期望写在注解属性里：

```java
@Test(expected = IllegalArgumentException.class)
public void negativeQuantityThrows() {
    calculatePrice(-1);
}
```

- `@Test`：告诉 JUnit 这是一条测试。
- `expected = IllegalArgumentException.class`：整句意思是“本方法必须抛出 `IllegalArgumentException` 才算通过”。传的是类型，所以要写 `.class`，不是 `new IllegalArgumentException()`。
- `calculatePrice(-1)`：故意传非法值。方法体里不写 `try/catch`——异常要抛出来给 JUnit 看见，自己吞掉就等于“正常返回”，测试反而失败。
- 方法名 `negativeQuantityThrows` = “负数数量会抛异常”。名字不影响判定，只影响可读性。

**什么情况下通过。**

| 实际行为 | 结果 | 原因 |
|---|---|---|
| 抛出 `IllegalArgumentException` | 通过 | 符合规格 |
| 返回 `-10` | 失败 | 该抛没抛 |
| 返回 `0` | 失败 | 该抛没抛 |
| 抛出 `NullPointerException` | 失败 | 异常类型不对 |

**反直觉但常考：抛异常不等于测试失败。**规格要求抛异常时，抛出正确类型恰恰是通过条件；真正让测试失败的是“该抛没抛”。

**两种 `expected` 不要混。**普通变量 `expected` 只表示期望的返回值：

```java
int expected = 30;
int actual = calculatePrice(3);
assertEquals(expected, actual);
```

而 `@Test(expected = X.class)` 里的 `expected` 是注解属性，表示期望的**异常类型**。看到 `expected` 这个词不能判定是异常题，要看它出现在哪。

**速记。**

| 写法 | 含义 |
|---|---|
| `throw` | 抛出异常 |
| `new IllegalArgumentException()` | 创建非法参数异常对象 |
| `@Test` | 声明测试方法 |
| `expected = X.class` | 期望抛出 X 类型异常 |
| `assertEquals(a, b)` | 检查实际值是否等于期望值 |

一句话：`@Test(expected = X.class)` = 不抛失败、抛错类型失败、抛对类型才通过。若题目还要求检查异常消息，这个注解不够用，要改成 `try/catch` 加 `fail()`。JUnit 5 的等价写法是 `assertThrows(IllegalArgumentException.class, () -> calculatePrice(-1))`，本课程仍用 JUnit 4。

### 最后看“一个用例”怎样计数

**“用例”是什么？**可以先把它想成“一次有目的的检查”：给方法一组输入，事先确定应该发生什么，再运行并检查结果。但考试的**计数规则**可能不同：有的数数据行，有的数断言执行次数，有的数方法调用次数。所以“写在同一个测试方法里”不表示只算一个。

**一个 `@Test` 不一定等于一个用例。**拿下面这个方法举例：

```java
static int sign(int x) {
    if (x > 0) return 1;
    if (x < 0) return -1;
    return 0;
}
```

| 输入 `x` | 期望 | 为什么 |
|---|---|---|
| `5` | `1` | 正数 |
| `-3` | `-1` | 负数 |
| `0` | `0` | 零 |

三组“输入 + 期望”就是 3 个用例。但如果只写成：

```java
@Test
public void testSign() {
    assertEquals(1, sign(5));
    assertEquals(0, sign(0));
}
```

整个类只有一个 `@Test`，却检查了两组不同输入：2 次断言、2 次调用。**到底算几个，取决于题目口径，不是 Java 规定的。**

**三种口径。**先在原题 README 中找 *test case*、*assertion*、*invocation*、*minimum*：

| 题目出现 | 数什么 |
|---|---|
| 按数据行数（`data()`、*test case*） | 有几组“输入、期望” |
| 按断言次数（*assertion*） | `assertEquals` 等断言执行了几次 |
| 按方法调用次数（*invocation*） | 被测方法被调了几次 |

**口径一：按数据行数。**参数化测试的 `data()` 里放几行，就是几组：

```java
@Parameters
public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {5, 1},
        {0, 0}
    });
}
```

`{5, 1}` 是“输入 5、期望 1”，`{0, 0}` 是“输入 0、期望 0”，两行就是 **2 个用例**，即使整类只有一个 `@Test` 方法也是如此。[2025 S2 Q2](../../exam-practice-questions/past-exams/2025-2/src/Q2/Q2README.md)要求 `data()` 放最少用例，多一行或少一行都会扣分。

**口径二：按断言次数。**同一个 `test()` 中执行 `assertEquals(1, sign(5))` 和 `assertEquals(0, sign(0))`，是 **2 次断言、2 个用例**。[2020 S2 Q2](../../exam-practice-questions/past-exams/2020-2/Q2/README.md)明确采用这个口径。

**口径三：按被测方法调用次数。**下面这段最容易数错，因为它只有一条断言：

```java
@Test
public void test() {
    int a = sign(5);
    int b = sign(0);
    assertEquals(1, a);
}
```

| 数什么 | 数量 |
|---|---|
| `@Test` 方法 | 1 |
| `sign()` 调用 | 2 |
| `assertEquals()` 执行 | 1 |

哪怕 `sign(0)` 的结果没被断言，它也是一次调用，按调用口径就是 **2 个用例**。[2021 S2 Q1](../../exam-practice-questions/past-exams/2021-2/q1/readme.md)明确采用这个口径。

**两个会改变计数的细节。**`for` 循环里的一行断言，执行几次就算几次；JUnit 4 在第一条断言失败处就终止该方法，后面的断言不会执行，实际计数可能少于写下的条数。

记住答题顺序：**读要求 → 代入输入手算期望 → 说明这一组覆盖了哪条路或哪条规则 → 按原题口径数用例。**

## 2. 读懂断言

```java
assertEquals(12, Pricing.calculatePrice(3, 4, "id1"));
```

左边是**预先算出的期望**，右边是实际结果。`assertTrue` / `assertFalse` 检查布尔值；`assertSame` 检查是否同一个对象。对象类型需与 JUnit 重载匹配，必要时用 `Integer.valueOf(12)` 或对实际 `Integer` 调 `.intValue()`。

## 3. 分支覆盖：画真 / 假表

```java
if (a < 0 || b < 0) return 1;
return 0;
```

本课程相关题目把 `&&`、`||` 的子条件也计入分支。`||` 会短路：`a<0` 为真时，`b<0` 不执行。

| 输入 (a,b) | a<0 | b<0 | 返回 |
|---|---|---|---|
| (-1,0) | 真 | 未执行 | 1 |
| (0,-1) | 假 | 真 | 1 |
| (0,0) | 假 | 假 | 0 |

这三行让两个子条件的真、假都出现。最少用例的做法是：

1. 列出每个判断和短路子条件。
2. 每个条件画“真 / 假”两格。
3. 从难到达的格子倒推输入；让同一输入顺便覆盖别的格子。
4. 手算每行期望值。
5. 删除没有新增覆盖的行，再检查是否仍全部覆盖。

一次调用可经过多个条件，所以**出口数不等于最少用例数**。

## 4. 白盒与黑盒的区别

白盒看已有代码路径。代码逻辑即使古怪，也按原题实现计算期望。2025 S2 Pricing 的七组输入分别覆盖空数量、空会员、数量 4 的中间路径、会员名单、单价门槛和整数除法。详细覆盖矩阵见[原稿 2025 S2 小节](../原稿归档/02-testing.md#41-2025-s2-q2-pricing-最少分支覆盖)。

黑盒从 README 的**规格**算期望。把正常情况、边界、无效输入列成表；不能运行某个待测实现后把它的输出抄为正确答案，因为它可能是错误版本。

## 5. JUnit 4 的两种常见写法

### 参数化测试：一个 `@Test` 跑 N 组数据

核心：JUnit 用**每组数据新建一个对象**，再运行那条测试。

```java
@RunWith(Parameterized.class)
public class SignTest {

    @Parameters                                 // ① 数据来源
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {5, 1},
            {0, 0}
        });
    }

    int input;                                  // ② 成员变量
    int expected;

    public SignTest(int input, int expected) {  // ③ 构造器：每组参数轮流传入
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void testSign() {                    // ④ 每个对象跑一次
        assertEquals(expected, sign(input));
    }
}
```

JUnit 把 `data()` 的每一行**按列顺序轮流传给构造器**：第一列进第一个参数，第二列进第二个参数，一行造一个对象。

各部分的职责：

| 代码 | 作用 |
|---|---|
| `@RunWith(Parameterized.class)` | 开启参数化，让 JUnit 用下面的数据表驱动测试 |
| `@Parameters` | 标记 `data()` 是数据来源 |
| `data()` | 返回所有“输入 + 期望”的行 |
| `Collection<Object[]>` | 一行就是一个 `Object[]` |
| `int input; int expected;` | 存放当前这行的参数 |
| 构造器 | 接住轮流传入的一组参数，存进对象 |
| `@Test` | 真正的测试，只有一份 |
| `assertEquals` | 用当前这行的期望值比较 |

JUnit 内部相当于：

```java
SignTest t1 = new SignTest(5, 1);  t1.testSign();
SignTest t2 = new SignTest(0, 0);  t2.testSign();
```

| 第一组数据 | 第二组数据 |
|---|---|
| `input = 5` | `input = 0` |
| `expected = 1` | `expected = 0` |
| `assertEquals(1, sign(5))` | `assertEquals(0, sign(0))` |

考试口径：**1 个 `@Test` + 2 组参数 = 2 次执行、2 个用例**。流程是 `data()` → 读一行 → 调构造器 → 存成员变量 → 跑 `@Test` → 读下一行。每组参数创建的都是**新对象**，不是在同一个对象上改字段。

考试时骨架通常已给，你只填 `data()`；每行的列顺序要和构造器参数（或 `@Parameter(0)` 字段）一致。

### 异常测试

检查一次异常：

```java
@Test(expected = IllegalArgumentException.class)
public void rejectsNegativeQuantity() {
    Calc.price(3, -1);
}
```

异常类要用题目指定的具体类型。语句覆盖题还要分别执行每个目标 `throw`、`return`，但不一定需要每个判断的真、假都走到。

## 6. 提交前检查

确定覆盖口径和计数规则；逐行标明“这组输入为何需要”；手算期望；运行原题测试。最少用例题再核查是否有冗余行和意外异常。

更多历年题见[原稿详细版](../原稿归档/02-testing.md)。
