# 02 测试

> 这个文件管什么题：给一段已有代码写最少的 JUnit4 测试，让每条分支都走到；或者按文字规格写测试，把正确实现和错误实现区分开。
> 历年出现：2025 S2、2024 S2、2024 S1、2023 S1 是整道编程题；2021 S2、2020 S2、2020 S1、2019 S2、2019 S1 是白盒最少覆盖；选择题里考过数分支和控制流图。
> 今年预测：官方提示里有一道要写代码的测试题，最像 2025 S2 Q2 这种「最少分支覆盖，多一个少一个都扣分」。

## 目录

- [1 先认题型](#1-先认题型)
- [2 零基础速懂](#2-零基础速懂)
- [2.1 一个测试由什么组成](#21-一个测试由什么组成)
- [2.2 分支覆盖和语句覆盖](#22-分支覆盖和语句覆盖)
- [2.3 白盒和黑盒](#23-白盒和黑盒)
- [2.4 一个用例怎么算](#24-一个用例怎么算)
- [3 套路](#3-套路)
- [3.1 白盒最少分支覆盖的步骤](#31-白盒最少分支覆盖的步骤)
- [3.2 数分支个数](#32-数分支个数)
- [3.3 控制流图求最少用例](#33-控制流图求最少用例)
- [3.4 参数化测试模板](#34-参数化测试模板)
- [3.5 黑盒区分多个实现](#35-黑盒区分多个实现)
- [3.6 异常测试的三种写法](#36-异常测试的三种写法)
- [3.7 语句覆盖的步骤](#37-语句覆盖的步骤)
- [3.8 时间不够先拿部分分](#38-时间不够先拿部分分)
- [4 真题套用](#4-真题套用)
- [4.1 2025 S2 Q2 Pricing 最少分支覆盖](#41-2025-s2-q2-pricing-最少分支覆盖)
- [4.2 2025 S1 Q4 MCQ3 控制流图](#42-2025-s1-q4-mcq3-控制流图)
- [4.3 2024 S2 Q2 HashingBox 黑盒](#43-2024-s2-q2-hashingbox-黑盒)
- [4.4 2024 S1 Q2 AVL 最少节点](#44-2024-s1-q2-avl-最少节点)
- [4.5 2024 S1 Q4 MCQ2 和 MCQ3 数分支](#45-2024-s1-q4-mcq2-和-mcq3-数分支)
- [4.6 2023 S1 Q1 Part 1 Riddle 最少分支](#46-2023-s1-q1-part-1-riddle-最少分支)
- [4.7 2021 S2 Q1 findSomething](#47-2021-s2-q1-findsomething)
- [4.8 2020 S2 Q2 保险费和 save](#48-2020-s2-q2-保险费和-save)
- [4.9 2020 S1 Q3 someMethod](#49-2020-s1-q3-somemethod)
- [4.10 2019 S2 Q3 parseDouble 和 isMixedCase](#410-2019-s2-q3-parsedouble-和-ismixedcase)
- [4.11 2019 S1 Q4 parseInt 和 collapseNewlines](#411-2019-s1-q4-parseint-和-collapsenewlines)
- [4.12 散题 q6 DNATreeCalc](#412-散题-q6-dnatreecalc)
- [4.13 散题 q8 四个参数的 findSomething](#413-散题-q8-四个参数的-findsomething)
- [4.14 2022 S1 Q1 和散题 q4 异常测试](#414-2022-s1-q1-和散题-q4-异常测试)
- [5 易错清单](#5-易错清单)

## 1 先认题型

| 题面里看到 | 子题型 | 用哪个套路 | 最像的真题 |
|---|---|---|---|
| branch complete、minimum、每多一个用例扣分 | 白盒，最少分支覆盖 | 3.1，填 `data()` 用 3.4 | [4.1](#41-2025-s2-q2-pricing-最少分支覆盖) |
| 同一个 `test()` 里写断言，并写明 minimum | 白盒，每条断言算一个用例 | 3.1 | [4.6](#46-2023-s1-q1-part-1-riddle-最少分支) |
| code complete、statement coverage | 语句覆盖，不要求每条分支 | 3.7 | [4.10](#410-2019-s2-q3-parsedouble-和-ismixedcase) |
| 给十几个实现，让正确的全过、错误的至少失败一项 | 黑盒 | 3.5 | [4.3](#43-2024-s2-q2-hashingbox-黑盒) |
| 自己造树，比较前序，并要求节点总数最少 | 黑盒加数据结构 | 3.5，旋转见 [04 树](04-trees.md) | [4.4](#44-2024-s1-q2-avl-最少节点) |
| `@Test(expected = ...)` 或题面点名某种异常 | 异常测试 | 3.6 | [4.14](#414-2022-s1-q1-和散题-q4-异常测试) |
| How many branches、control flow graph | 选择题 | 3.2 或 3.3 | [4.2](#42-2025-s1-q4-mcq3-控制流图)、[4.5](#45-2024-s1-q4-mcq2-和-mcq3-数分支) |

先看 README 里的扣分规则，再写代码。2025 S2 是多一个或少一个用例扣 5 分，覆盖不到 50% 没有分。2024 S2 是让正确实现失败扣得比放走错误实现更重。

## 2 零基础速懂

### 2.1 一个测试由什么组成

一个测试用例是：一组输入，一次调用，一个你事先算好的期望结果。

```java
assertEquals(12, Pricing.calculatePrice(3, null, null));
```

`assertEquals(期望, 实际)`。两个参数写反，失败信息会看不懂，结果也对不上。实际值是 `Integer` 时，期望也写成整数对象，或者在实际值上加 `.intValue()`，否则 `assertEquals(int, Integer)` 对不上号，编译失败。

`assertTrue(条件)`、`assertFalse(条件)` 用来测返回 boolean 的方法。`fail("说明")` 表示「执行到这里就算这题失败」。

### 2.2 分支覆盖和语句覆盖

分支（branch）是一个判断的两个出口：条件为真走一条，为假走另一条。分支覆盖（branch coverage）要求每个出口至少走一次。

```java
if (a < 0 || b < 0) {   // 四个出口：a<0 的真和假，b<0 的真和假
    return 1;
}
return 0;
```

这门课把 `&&` 和 `||` 的每个子条件各算一条分支。原话在 [2020 S2 Q2 README](../exam-practice-questions/past-exams/2020-2/Q2/README.md)：`&&` and `||` operators introduce their own branches。所以上面是 4 个出口，不是 2 个。

语句覆盖（statement coverage）只要求每一行语句至少执行一次。一个 `if` 的假出口如果后面没有单独的语句，语句覆盖可以不走它。题面写 code complete 时按语句覆盖做，写 branch complete 时按分支覆盖做。

短路：`a || b` 在 `a` 已经为真时不会算 `b`。`b` 的真和假都要在 `a` 为假的用例里覆盖。

### 2.3 白盒和黑盒

白盒（white box）：题面给你完整代码，按代码里的判断设计用例。代码看起来不合理也照代码测，README 会写 assume the implementation is correct。

黑盒（black box）：不看各个实现的内部，只看 README 的规格。期望值要自己用规格算，不能抄某个实现的输出。抄错的那个实现，等于把错误实现判成正确。

### 2.4 一个用例怎么算

两种口径，看骨架：

- `data()` 里的参数化测试：一行算一个用例。2025 S2 多一行扣 5 分。
- 一个 `test()` 方法里写很多断言：每执行一次断言算一个用例。循环里同一次断言跑 10 次算 10 个。2020 S2、2021 S2 的 README 写明了这一点。

所以最少覆盖题里不要写循环去调用被测方法。

## 3 套路

### 3.1 白盒最少分支覆盖的步骤

适用：题面要求 branch complete 并且要求 minimum。

1. 只看被测的那一个方法。它调用的私有方法，题面说 within 这个方法时，不钻进去数。
2. 把每个 `if`、`else if`、`while`、`for` 的条件拆开。`&&`、`||` 的每个子条件单独成行，标上编号。
3. 每个子条件画两格：真、假。`else` 是前一个条件的假。三元运算符 `? :` 也是两格。
4. 短路填不上的格子，换一组输入单独打中它。
5. 从「只被一条路径打中」的格子倒推。一条路径能顺便打中的格子就合并，合并不了的另起一行。
6. 每行先手算返回值，再写进测试。会抛异常的输入直接扔掉，换一组。`List.of(...).contains(null)` 会抛 `NullPointerException`，这种输入不能留。
7. 行数等于你算出的最少行数。多一行、少一行都按 README 扣分。

部分分：实在合并不动，先保证覆盖过半，再删掉结果重复、没有打中新格子的行。

### 3.2 数分支个数

适用：选择题问 how many branches。

1. 找出每个判断条件。
2. `&&` 和 `||` 拆成子条件，每个子条件 2 个分支。
3. 相加。`if / else if / else if` 是每个条件各 2 个，不是「整条链 2 个」。
4. 没有 `else` 也要算假出口。假出口只是不进那个花括号。

这门课的卷子按这个口径。只数 `if` 的个数会少一半。

### 3.3 控制流图求最少用例

适用：给一张控制流图（control flow graph），问最少几条路径能覆盖所有边。

1. 把图抄成「判断框」和「出口」。
2. 前后两段各自独立时，最少用例数是两段里较大的那个。前面需要 4 条、后面需要 5 条，答案是 5，不是 9。一条路径可以同时用掉前面的一条和后面的一条。
3. 并列的真出口数一下，再加一条「全部为假」的路。
4. 选项里如果同时有「两段相加」和「两段取最大」，取最大。

### 3.4 参数化测试模板

适用：骨架已经有 `@RunWith(Parameterized.class)`。两种骨架不要混用。

骨架一，题目给了构造器。一行数据按顺序传给构造器参数。下面是能编译运行的完整类，考试时只改 `data()` 里的行，类名和方法名用原题的。

```java
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CtorParamTest {
    private final int unit;
    private final Integer qty;
    private final Integer expected;

    public CtorParamTest(int unit, Integer qty, Integer expected) {
        this.unit = unit;
        this.qty = qty;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: price({0},{1}) = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {3, 1, 3},
                {3, 2, 6},
        });
    }

    @Test(timeout = 1000)
    public void test() {
        assertEquals(expected, Calc.price(unit, qty));
    }
}
```

骨架二，题目用 `@Parameter` 字段注入。字段必须是 `public`，括号里是列号，从 0 开始。这种类不能再写带参数的构造器。

```java
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FieldParamTest {
    @Parameter(0) public int unit;
    @Parameter(1) public Integer qty;
    @Parameter(2) public Integer expected;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {3, 1, 3},
                {3, 2, 6},
        });
    }

    @Test(timeout = 1000)
    public void test() {
        assertEquals(expected, Calc.price(unit, qty));
    }
}
```

`data()` 的返回类型照抄原题。有的题是 `Collection<Object[]>`，有的是 `Iterable<某类>`。不要改签名。

### 3.5 黑盒区分多个实现

适用：十几个类实现同一个规格，测试要让对的全过、错的至少红一个。

1. 把 README 的每条规则写成一行：正常情况、边界、题面点名的例外。
2. 每条规则手算一个期望值，写在注释里。算式留在注释里，方便检查。
3. 一个规则一个 `@Test(timeout = 1000)`。期望值写错会让正确实现变红，这种题里红掉正确实现扣分更重。
4. 异常单独一个测试，用 3.6。不要和正常断言放在一起，否则异常一抛，后面的断言就不跑了。
5. 这种题多写几条正确的用例通常不扣分。和 3.1 相反，不要为了「少」而删掉规则。

### 3.6 异常测试的三种写法

适用：题面要求某个输入必须抛出某个异常。三种都在 JUnit 4.13 上编译运行过。

```java
// 写法 1：一个方法只测这一次抛出。骨架里已有 expected 时用这个
@Test(expected = IllegalArgumentException.class, timeout = 1000)
public void throwsByAnnotation() {
    Calc.price(3, null);
}

// 写法 2：要和别的断言写在同一个方法里
@Test(timeout = 1000)
public void throwsByTryCatch() {
    try {
        Calc.price(3, null);
        fail("should throw");
    } catch (IllegalArgumentException e) {
        // 抛了才是对的
    }
}

// 写法 3：JUnit 4.13 的 assertThrows
@Test(timeout = 1000)
public void throwsByAssertThrows() {
    assertThrows(IllegalArgumentException.class, () -> Calc.price(3, null));
}
```

异常类名用题面给的那个，包括包名。`expected` 写成父类 `Exception.class` 会把别的异常也放过去，不要这么写。

### 3.7 语句覆盖的步骤

适用：题面写 code complete、statement coverage，没写 branch。

1. 列出方法里每一条会被执行的语句，包括每个 `throw`、`return`。
2. 让每条语句至少落在一个用例里。`if` 的假出口后面没有语句时，可以不单独为它造用例。
3. 有多个 `throw` 时，每个 `throw` 是一条语句，各需要一次调用。
4. 断言照样要期望值正确。语句覆盖不是「跑到就算」，跑到但断言写错仍然失败。

### 3.8 时间不够先拿部分分

1. 先让测试类编译通过。`data()` 先放一行你确定能过的用例。
2. 白盒题按 3.1 的表从「还没打中的格子」往上加，加到超过一半分支。2025 S2 不到 50% 没分。
3. 不要为了凑数加会抛异常的行，整行会被判无效。
4. 黑盒题先写 README 里的示例，示例的期望值题面已经给了。
5. 每加完一组就跑一次原题的测试类。

## 4 真题套用

### 4.1 2025 S2 Q2 Pricing 最少分支覆盖

**题意**：给 `Pricing.calculatePrice(int unitPrice, Integer quantity, String memberId)` 写最少的参数化用例，达到分支覆盖。多一个或少一个用例各扣 5 分，覆盖不到 50% 没分。不能用会让方法抛异常的输入。原题 [Q2README.md](../exam-practice-questions/past-exams/2025-2/src/Q2/Q2README.md)，填 [CalculatePriceTest.java](../exam-practice-questions/past-exams/2025-2/src/Q2/CalculatePriceTest.java)。

**套路对应**：3.1。构造器骨架用 3.4 的第一种，原题已经写好，只改 `data()`。

原子条件 8 个，出口 16 个：

| 编号 | 条件 | 真 | 假 |
|---|---|---|---|
| A | `quantity == null` | 用例 1、2、3 | 用例 4、5、6、7 |
| B | `memberId == null` | 用例 1 | 用例 2、3 |
| C | `memberId.trim().isEmpty()` | 用例 2 | 用例 3 |
| D | `quantity <= 3` | 用例 1、2、3 | 用例 4、5、6、7 |
| E | `quantity >= 5` | 用例 5、6、7 | 用例 4 |
| F | `List.of("id1","id2","id3").contains(memberId)` | 用例 5、7 | 用例 6 |
| G | `unitPrice >= 3` | 用例 5 | 用例 7 |
| H | `quantity / 6 >= 1` | 用例 7 | 用例 6 |

B 为真时 C 不会被计算，所以 C 的真和假都放在 B 为假的用例里。F 为真且 G 为真时走 `return 10`，H 不会被计算，所以 H 的两个出口放在用例 6 和 7。

**关键代码**（`CalculatePriceTest` 7 个测试全部通过）：

```java
return Arrays.asList(new Object[][]{
        {new Pricing(3, null, null), 12},     // 1 null 数量，null 会员，单价 3 乘 4
        {new Pricing(3, null, ""), 12},       // 2 会员是空串
        {new Pricing(3, null, "id1"), 12},    // 3 会员非空，但数量已被改成 1，直接 return 12
        {new Pricing(3, 4, "id1"), 12},       // 4 数量 4，两个数量判断都为假，3*4=12
        {new Pricing(3, 5, "id1"), 10},       // 5 数量至少 5，会员在名单里，单价至少 3
        {new Pricing(3, 5, "other"), 15},     // 6 会员不在名单，5/6 等于 0，3*5=15
        {new Pricing(2, 6, "id1"), 10},       // 7 单价不够 3，6/6 等于 1，数量变成 5，2*5=10
});
```

**坑**：

- `(3, 2, "id1")` 看起来像边界，其实数量 2 和用例 1 到 3 走的是同一条 `quantity <= 3`。加上它就变成 8 个用例，多扣 5 分。
- `memberId` 传 `null` 只能出现在 `quantity == null` 的用例里。数量不是 null 时，`contains(null)` 会抛异常，这一行作废。
- 数量 4 是唯一能让 `<= 3` 和 `>= 5` 同时为假的整数。不要用 3 或 5 代替。
- 期望值按代码算，不按「合理的定价」算。`discount` 在这些路径上一直是 0 或负数，`Math.min(discount, 0)` 把它变成 0 或更小，最后 `(1 - 负数)` 不会出现在这 7 行的返回值里。

### 4.2 2025 S1 Q4 MCQ3 控制流图

**题意**：单选。图从 start 出来先是一条竖链：`x == 1`、`x == 2`、`x == 3`，每个为真都进一个语句再汇合，全假则直接往下。汇合后是第二条竖链：`y == 1`、`y == 2`、`y == 3`、`y == 4`，同样每个为真进一个语句，全假到 end。问最少几个用例能分支覆盖。选项 4、5、6、9、12、20、21。原题 [MC_Questions_2025_S1.pdf](../exam-practice-questions/past-exams/2025-1/src/Q4/MC_Questions_2025_S1.pdf)。

**套路对应**：3.3。

**推导**：左边 3 个真出口加 1 条全假，共 4 条。右边 4 个真出口加 1 条全假，共 5 条。两段前后串联，一条路径可以各走一段，最少是 `max(4, 5) = 5`。例如 `(x, y)` 取 `(1,1)`、`(2,2)`、`(3,3)`、`(0,4)`、`(0,0)`。

答案：b，5（非官方答案）。

**坑**：把两段相加成 9，会选 d。9 是「每段单独测完再相加」，不是一条路径从前走到后。

### 4.3 2024 S2 Q2 HashingBox 黑盒

**题意**：13 个 `HashingBox`，按 README 的规格测 `hash(Thing)`。正确实现必须全绿，错误实现至少一个测试失败。让正确实现失败，扣分比放走错误实现更重。不测 `Thing` 为 null，也不测它的字段为 null。原题 [Q2README.md](../exam-practice-questions/past-exams/2024-2/src/Q2/Q2README.md)，填 [Q2Test.java](../exam-practice-questions/past-exams/2024-2/src/Q2/Q2Test.java)。`hashingBox` 字段已经注入，不要改 `getImplementations()`。

规格四步：

1. `sizeHash`：不超过 size 的最大质数。size 为 1 时固定是 2。
2. `nameHash`：只取前 6 个字符里的英文字母，ASCII 求和，乘 `sizeHash`，再加整个名字的长度。数字和符号不进求和，但长度照算。
3. `materialShapeHash`：材质值乘形状值。STEEL 1018，SILVER 925，GOLD 999。CIRCLE 1，SEMICIRCLE 2，TRIANGLE 3，SQUARE 4，PENTAGON 5。
4. 两者相加。size 不在 1 到 31 就抛 `ComponentOutOfRangeException`。

**套路对应**：3.5。期望值全部按上面四步手算。

**关键代码**（156 次运行：HashingBox10 全部通过，其余 12 个各自至少失败一项）：

```java
private void check(int expected, Material m, int size, Shape s, String name)
        throws ComponentOutOfRangeException {
    assertEquals(expected, hashingBox.hash(new Thing(m, size, s, name)));
}

@Test(timeout = 1000)
public void readmeExample() throws ComponentOutOfRangeException {
    check(4905, Material.SILVER, 3, Shape.SQUARE, "Comp2100");   // (67+111+109+112)*3+8 + 925*4
}

@Test(timeout = 1000)
public void sizeOneUsesTwo() throws ComponentOutOfRangeException {
    check(1149, Material.STEEL, 1, Shape.CIRCLE, "A");           // 65*2+1+1018
}

@Test(timeout = 1000)
public void oddCompositeSize() throws ComponentOutOfRangeException {
    check(2514, Material.STEEL, 27, Shape.CIRCLE, "A");          // 27 不是质数，用 23
}

@Test(timeout = 1000)
public void evenCompositeSize() throws ComponentOutOfRangeException {
    check(1884, Material.GOLD, 4, Shape.CIRCLE, "abc");          // 4 用 3；(97+98+99)*3+3+999
}

@Test(timeout = 1000)
public void onlyFirstSixCharacters() throws ComponentOutOfRangeException {
    check(2219, Material.STEEL, 2, Shape.CIRCLE, "abcdefg");     // 只加 a 到 f，长度仍是 7
}

@Test(timeout = 1000)
public void nonLettersIgnoredButCounted() throws ComponentOutOfRangeException {
    check(2636, Material.SILVER, 2, Shape.SEMICIRCLE, "1aaaa1aaaa"); // 前 6 个里 4 个 a
}

@Test(timeout = 1000)
public void boundaryLetters() throws ComponentOutOfRangeException {
    check(1770, Material.STEEL, 2, Shape.CIRCLE, "AZaz");        // A、Z、a、z 都是字母
}

@Test(timeout = 1000)
public void symbolsNextToLetters() throws ComponentOutOfRangeException {
    check(1022, Material.STEEL, 3, Shape.CIRCLE, "@[`{");        // 四个都不是字母
}

@Test(timeout = 1000)
public void emptyName() throws ComponentOutOfRangeException {
    check(3700, Material.SILVER, 1, Shape.SQUARE, "");           // 925*4
}

@Test(timeout = 1000)
public void maxSize() throws ComponentOutOfRangeException {
    check(7881, Material.STEEL, 31, Shape.PENTAGON, "Z");        // 90*31+1+1018*5
}

@Test(timeout = 1000, expected = ComponentOutOfRangeException.class)
public void sizeTooLargeThrows() throws ComponentOutOfRangeException {
    hashingBox.hash(new Thing(Material.STEEL, 32, Shape.CIRCLE, "A"));
}

@Test(timeout = 1000, expected = ComponentOutOfRangeException.class)
public void negativeSizeThrows() throws ComponentOutOfRangeException {
    hashingBox.hash(new Thing(Material.STEEL, -1, Shape.CIRCLE, "A"));
}
```

这 13 个实现里，只有 `HashingBox10` 对 1 到 31 的全部 size、三种材质、五种形状、上面这些名字都和规格一致。对照时把 13 个类的 `hash` 结果和手算值逐个比过。

**坑**：

- 名字 `"1aaaa1aaaa"` 的前 6 个字符是 `1 a a a a 1`，只有 4 个字母。第 7 个字符开始的 `aaaa` 不加。
- 名字 `@[` + `{` 四个符号都不是字母，用来抓「把字母旁边的符号也算进去」的实现。`[` 的码点紧挨 `A` 的前面，`{` 紧挨 `z` 的后面。
- size 27 是奇数合数，最近的质数是 23，不是 27 自己。
- 骨架里的 `hash` 只在 size 小于 0 或大于 31 时抛异常。size 为 0 不会抛，会继续算。不要写「size 0 必须抛异常」，正确实现会因此变红。
- 注释里的示例测试期望 4905，和规格一致，可以取消注释留着。

### 4.4 2024 S1 Q2 AVL 最少节点

**题意**：用参数化测试检查 AVL 的 `insert`。每行一个树编号和期望的前序。`createTree` 里按编号把树造出来。所有树的节点个数加起来要最少。不测 null、重复值、非法输入。正确实现必须全过，错误实现至少一个失败。原题 [Q2README.md](../exam-practice-questions/past-exams/2024-1/src/Q2/Q2README.md)。旋转的算法在 [04 树](04-trees.md)。

**套路对应**：3.5。四种失衡各需要一棵 3 个节点的树，少一种就抓不到「只漏了这一种旋转」的实现。2 个节点的树平衡因子是 1 或 -1，不会旋转，没有区分度。所以是 4 棵树、12 个节点。

四棵插完都是根 2、左 1、右 3，前序都是 `2, 1, 3`：

| 编号 | 插入顺序 | 失衡 |
|---|---|---|
| 0 | 3, 2, 1 | 左左，右旋 |
| 1 | 1, 2, 3 | 右右，左旋 |
| 2 | 3, 1, 2 | 左右，先左旋再右旋 |
| 3 | 1, 3, 2 | 右左，先右旋再左旋 |

**关键代码**（用一份会旋转的实现跑，4 个测试通过；用原题自带的 `SampleAVLTree` 跑，4 个全失败）：

```java
@Parameterized.Parameters
public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
            {0, List.of(2, 1, 3)},
            {1, List.of(2, 1, 3)},
            {2, List.of(2, 1, 3)},
            {3, List.of(2, 1, 3)},
    });
}

private AVLTree createTree(int treeIdentifier) {
    int[][] insertions = {
            {3, 2, 1},
            {1, 2, 3},
            {3, 1, 2},
            {1, 3, 2},
    };
    int[] seq = insertions[treeIdentifier];
    AVLTree tree = createTreeNode(seq[0]);
    for (int i = 1; i < seq.length; i++) {
        tree = tree.insert(seq[i]);          // 必须接住返回值，insert 返回的是新根
    }
    return tree;
}
```

**坑**：

- `tree.insert(...)` 不赋值，测的还是旧根。样例实现正好会改自己，所以不赋值也能在样例上变绿，换一个不修改原节点的正确实现就全红。
- 不要在测试里写 `new SampleAVLTree`。用原题给的 `createTreeNode`。
- 四行的期望列表相同是对的，不是复制错了。

### 4.5 2024 S1 Q4 MCQ2 和 MCQ3 数分支

**题意**：MCQ2 问 `Riddle.checkAnswer` 有多少分支，选项 10 到 16。MCQ3 问覆盖全部分支需要多少个不同输入，选项 5、6、7、8。代码和 4.6 是同一个方法。原题 [Q4README.md](../exam-practice-questions/past-exams/2024-1/src/Q4/Q4README.md)。2024 S1 的选择题是多选，全对才得分。这两题各只有一个正确选项。

**套路对应**：3.2 和 3.1。

**推导**：8 个原子条件，每个 2 个出口，共 16。

1. `type == TEXT`
2. `equalsIgnoreCase`
3. `type == NUMERIC`
4. `matches("\\d+")`
5. 两个 `parseInt` 相等（和 4 用 `&&` 连着）
6. `type == ANAGRAM`
7. `Arrays.equals`
8. `isCorrect`

8 个不同输入才能打全，和 4.6 的 8 条断言一一对应。少一条就有一个 `&&` 的出口没人走。

答案：MCQ2 选 F（16），MCQ3 选 D（8）（非官方答案）。

**坑**：把 `&&` 当成一个条件，会得到 14，那是选项 D，正好是干扰项。

### 4.6 2023 S1 Q1 Part 1 Riddle 最少分支

**题意**：在 `RiddleCheckAnswerTest.tests()` 里为 `checkAnswer` 写最少分支覆盖。原题 [Q1README.md](../exam-practice-questions/past-exams/2023-1/Q1/Q1README.md)。一个 `test()` 里每条断言算一个用例。

**套路对应**：3.1。条件和 4.5 相同。

**关键代码**（原题测试类 1 个测试通过，里面 8 条断言）：

```java
assertTrue(new Riddle("q", "Echo", Riddle.RiddleType.TEXT_RIDDLE).checkAnswer("eChO"));
assertFalse(new Riddle("q", "Echo", Riddle.RiddleType.TEXT_RIDDLE).checkAnswer("Echo!"));
assertTrue(new Riddle("q", "42", Riddle.RiddleType.NUMERIC_RIDDLE).checkAnswer("42"));
assertFalse(new Riddle("q", "42", Riddle.RiddleType.NUMERIC_RIDDLE).checkAnswer("4x2"));
assertFalse(new Riddle("q", "42", Riddle.RiddleType.NUMERIC_RIDDLE).checkAnswer("41"));
assertTrue(new Riddle("q", "Listen", Riddle.RiddleType.ANAGRAM_RIDDLE).checkAnswer("Silent"));
assertFalse(new Riddle("q", "Listen", Riddle.RiddleType.ANAGRAM_RIDDLE).checkAnswer("Listen!"));
assertFalse(new Riddle("q", "x", null).checkAnswer("x"));
```

八条分别打中：忽略大小写的真和假；数字格式对并且数值对；格式不对（`&&` 后面不执行）；格式对但数值不对；变位词的真和假；`type` 三种都不是。

**坑**：

- 数字题的 `answer` 本身必须是纯数字。`parseInt(answer)` 在格式检查通过后总会执行，`answer` 不是数字会抛异常。
- `input` 不要传 null，`equalsIgnoreCase(null)` 以外的分支会空指针。`type` 可以是 null，这正是第八条在用的。
- 变位词先转小写再排序，`"Silent"` 和 `"Listen"` 是同一组字母。

### 4.7 2021 S2 Q1 findSomething

**题意**：`findSomething(int a, int b, int c)`，一个 `test()` 里写最少调用。每调用一次算一个用例。原题 [readme.md](../exam-practice-questions/past-exams/2021-2/q1/readme.md)。

**套路对应**：3.1。条件是 `a < 0`、`b < 0`、`a < b`、`a > c`，后两个在 `if` 里面和 `return` 里面各出现，是不同的语句，都要覆盖。`||` 短路，所以 `b < 0` 的真必须在 `a < 0` 为假时打。

**关键代码**（6 条断言，出口都打中过）：

```java
assertTrue(BranchComplete.findSomething(-1, 0, 0));    // a<0 真，a<b 真
assertTrue(BranchComplete.findSomething(0, -1, -1));   // a<0 假，b<0 真，a>c 真
assertFalse(BranchComplete.findSomething(-1, -2, 0));  // a<b 假，a>c 假
assertFalse(BranchComplete.findSomething(1, 0, 0));    // 两个外层都假，a<b 假
assertTrue(BranchComplete.findSomething(1, 2, 0));     // a<b 真，a>c 真
assertFalse(BranchComplete.findSomething(1, 2, 3));    // a<b 真，a>c 假
```

**坑**：`if` 里面的 `a < b || a > c` 和最后一行的 `a < b && a > c` 是两组条件。只写 `a < 0` 的用例，最后一行的 `&&` 一个出口都走不到。

### 4.8 2020 S2 Q2 保险费和 save

**题意**：`Person.calcInsurancePrice` 和 `PersonRepository.save` 都要最少分支覆盖，写在两个测试文件里。原题 [README.md](../exam-practice-questions/past-exams/2020-2/Q2/README.md)。断言看 `getHealthInsurancePrice()`，因为计算方法没有返回值。

**套路对应**：3.1。职业那一层是 `occupation == null || length == 0 || equals("policeman")`，三个子条件。年龄、两种病是内层。`save` 只有 `getId() != null` 的真和假。

**关键代码**：

```java
assertEquals(1100, new Person("A", 50, null, true, true).getHealthInsurancePrice().intValue());
assertEquals(500, new Person("A", 50, "", false, true).getHealthInsurancePrice().intValue());
assertEquals(400, new Person("A", 50, "policeman", true, false).getHealthInsurancePrice().intValue());
assertEquals(360, new Person("A", 50, "policeman", false, false).getHealthInsurancePrice().intValue());
assertEquals(200, new Person("A", 30, "policeman", false, false).getHealthInsurancePrice().intValue());
assertEquals(350, new Person("A", 30, "teacher", false, false).getHealthInsurancePrice().intValue());
```

`save` 两条：一个没 id 的 `Person` 保存后 `getId()` 不是 null；先 `setId(1)` 再保存，期望 `RuntimeException`。用 3.6 的写法 1。

**坑**：`equals("policeman")` 的假出口只有「不是 null、不是空串、也不是 policeman」才能走到，所以最后一条用 `"teacher"`。空串已经把 `length == 0` 的真打过了，不能用它代替 teacher。`intValue()` 是题面提示的写法，用来避开 `assertEquals` 对 `Integer` 和 `int` 的重载冲突。

### 4.9 2020 S1 Q3 someMethod

**题意**：只测 `someMethod` 这一个方法里的分支，最少用例。它调用的 `method1`、`method2`、`method3` 不往下拆。原题在 `past-exams/2020-1/Q3/Something.java`。`VALUE1` 是 36，`VALUE2` 是 15。

**套路对应**：3.1。`method2` 算的是最大公约数，`method3` 算的是最小公倍数。两个判断：公约数是否等于 36，以及内层两个公倍数是否相等；后面还有 `method1` 的结果是否等于 15。

**关键代码**（3 条，三个判断的真假都打中过）：

```java
assertEquals(144, Something.someMethod(36, 36, 36, 36));  // 两个 if 都进，内层也进
assertEquals(155, Something.someMethod(36, 72, 31, 16));  // 外层进，内层不进，第二个 if 进
assertEquals(0, Something.someMethod(1, 2, 1, 1));        // 两个 if 都不进
```

**坑**：`method2` 用辗转相减，参数是 0 会在 `while (n1 != n2)` 里减个不停，直到超时。不要用 0。

### 4.10 2019 S2 Q3 parseDouble 和 isMixedCase

**题意**：`parseDouble` 要最少的语句覆盖。`isMixedCase` 要最少的分支覆盖。原题 PDF 在 `past-exams/2019-2/`。2019 的骨架 import 的是 JUnit 5。今年考试只用 JUnit 4，把 import 改成 `org.junit.Test` 和 `org.junit.Assert` 即可，断言写法不变。

**套路对应**：`parseDouble` 用 3.7，三次调用覆盖 null 的 return、没有数字的 return、以及真正 `parseDouble` 的那一行。`isMixedCase` 用 3.1。

**关键代码**：

```java
assertEquals(0, MyUtil.parseDouble(null), 0.0001);
assertEquals(0, MyUtil.parseDouble("abc"), 0.0001);
assertEquals(1.5, MyUtil.parseDouble("a1.5"), 0.0001);

assertEquals(false, MyStringUtil.isMixedCase(null));
assertEquals(false, MyStringUtil.isMixedCase(""));
assertEquals(true, MyStringUtil.isMixedCase("aBc"));
assertEquals(false, MyStringUtil.isMixedCase("AB"));
assertEquals(true, MyStringUtil.isMixedCase("aB"));
assertEquals(false, MyStringUtil.isMixedCase("a"));
```

**坑**：`assertEquals` 比较两个 double 时要带第三个参数误差，否则编译器选中的是已过时的两参数版本。`isMixedCase` 的循环里，两个 flag 的真假组合要分开打。`"aBc"` 中途就 return，`"aB"` 要走到最后一行的 `&&`。

### 4.11 2019 S1 Q4 parseInt 和 collapseNewlines

**题意**：`parseInt` 语句覆盖，`collapseNewlines` 分支覆盖。原题 PDF 在 `past-exams/2019-1/`。同样是 JUnit 5 的骨架。

**套路对应**：3.7 和 3.1。`parseInt` 有两条不同的 `throw`，各要执行一次，再加上一条成功路径。`collapseNewlines` 长度小于 2 直接返回；长度够的时候，当前字符是不是换行、上一个是不是换行，各有真假。

**关键代码**：

```java
assertEquals(5, MyInteger.parseInt("5"));
// ":" 的码点大于 '9'，走内层 throw；"/" 的码点小于 '0'，走外层 throw
assertThrows(NumberFormatException.class, () -> MyInteger.parseInt(":"));
assertThrows(NumberFormatException.class, () -> MyInteger.parseInt("/"));

assertEquals("a", StringUtil.collapseNewlines("a"));
assertEquals("a\nb", StringUtil.collapseNewlines("a\n\nb"));
```

**坑**：`"a\n\nb"` 一条里同时有「普通字符」「第一个换行要保留」「紧挨着的第二个换行要丢掉」。不要拆成多条，多一条就算多用了用例。

### 4.12 散题 q6 DNATreeCalc

**题意**：`BranchComplete` 的构造器里已经插入了一棵固定的树。`sum(root)` 把全部 key 加起来，等于 12+11+19+62+53+44+7 = 208。`DNATreeCalc(a, b, c)` 按这个 208 和三个参数分支。原题 [readme.md](../exam-practice-questions/misc-questions/q6-bst-testing/readme.md)。

**套路对应**：3.1。先算死 `sum`，后面三个条件和普通白盒一样。`sum < a` 为真时里面还有 `sum > b`，但真假两条最后都被 `ret = sum + a` 盖掉，所以返回值看不出里面走了哪条，仍然要让 `sum > b` 的真和假都发生一次。

**关键代码**（3 条断言通过）：

```java
assertEquals(417, bc.DNATreeCalc(209, 0, 0));    // 208<209，208>0，然后被盖成 208+209
assertEquals(208, bc.DNATreeCalc(209, 300, 0));  // 208<209，208>300 为假
assertEquals(0, bc.DNATreeCalc(0, 0, 0));        // 208<0 为假，b-a>c 为假，ret 保持 0
```

第三条如果改成 `b - a > c` 为真，返回值会变成 `208 + c`，内层那条 `sum > b` 的假就没人打了。两条真路径都用在了前两个用例上。

**坑**：树是写死的，不要自己 `insert`。返回值相同不代表分支相同，前两条都可能让人觉得「结果差不多」，差在 `sum > b`。

### 4.13 散题 q8 四个参数的 findSomething

**题意**：和 4.7 不是同一个方法。这个版本四个参数，先可能改 `x`，再按 `a < b` 分成三路。原题在 `misc-questions/q8-dp-testing/branchComplete/`。

**套路对应**：3.1。第一行 `a<0 || b<0 || c<0 || d<0` 有四个子条件，每个的真出口都要在前面的条件为假时打到。

**关键代码**（5 条调用把 16 个出口都打中过）：

```java
assertEquals(-2, BranchComplete.findSomething(-1, 0, 0, 0));  // a<0
assertEquals(1, BranchComplete.findSomething(0, -1, 0, 0));   // b<0
assertEquals(-2, BranchComplete.findSomething(0, 1, -1, 0));  // c<0，并且 a<c 为假
assertEquals(2, BranchComplete.findSomething(1, 1, 1, -1));   // d<0，走 b>d
assertEquals(1, BranchComplete.findSomething(1, 1, 0, 0));    // 四个都非负，走最后的 else
```

**坑**：`a < c` 的假出口在第三条。只写负数、不控制 `a < c`，内层的 else 走不到。

### 4.14 2022 S1 Q1 和散题 q4 异常测试

**题意**：`State.check(character, event)` 要在参数为 null 时抛异常。`ExceptionTest` 里两个方法已经标好了 `@Test(expected = ...)`，方法体里只要写出会抛的那一次调用。原题 [readme.md](../exam-practice-questions/past-exams/2022-1/q1/readme.md)。散题 q4 的结构和它相同。

**套路对应**：3.6 的写法 1。`Character` 构造后 `getState()` 就是某个状态单例，直接拿来调 `handle`。`handle` 第一行会调 `check`。

**关键代码**（两个测试都通过。`check` 本身要先写成下面这样，否则测试没有异常可抛）：

```java
// State.check 里
if (character == null) {
    throw new exceptions.NullCharacterException();
}
if (event == null) {
    throw new exceptions.NullKeyEventException();
}

// testNullKeyException
this.character.getState().handle(this.character, null);

// testNullCharacterException
this.character.getState().handle(null, Key.UP);
```

**坑**：两个 null 要分开测。一次调用里两个都传 null，只能抛出先写的那一个，另一个测试收不到它的异常。先判断 character 还是先判断 event，要和两个测试方法对得上。上面的顺序和原题两个方法的分工一致。

## 5 易错清单

1. `&&`、`||` 的每个子条件各 2 个分支。合成一个条件会少覆盖，也会把选择题选成 14 而不是 16。
2. 白盒最少题多一行就扣分。写完对着表检查有没有两行打中的格子完全一样。2025 S2 的 `(3, 2, "id1")` 就是这种多余行。
3. 短路没执行到的子条件，不算覆盖到。要另造一行，让它左边的条件为假。
4. 会抛异常的输入在最少覆盖题里整行作废。`contains(null)`、除以 0、`while` 停不下来，都先代换掉。
5. `assertEquals` 的第一个参数是期望值。`Integer` 和 `int` 混用时编译失败，对实际值加 `.intValue()`，或者期望值也装箱。
6. 比较 double 要写误差，三个参数的 `assertEquals(期望, 实际, 0.0001)`。
7. 参数化测试的 `insert`、`parse` 如果返回新对象，必须接住返回值。
8. 黑盒的期望值按 README 手算，不抄某个 `HashingBox` 的输出。抄到错误实现上，正确的那个会被你测红。
9. 一个 `test()` 里的每条断言都算一个用例。不要用 for 循环调用被测方法来「少写几行」。
10. 只在注释标出的区域里写。`@RunWith`、构造器、`@Parameter` 字段不要改。
