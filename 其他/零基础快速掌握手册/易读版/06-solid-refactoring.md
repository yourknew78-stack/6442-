# 06 SOLID 坏味道和重构

> 这个文件管什么题：判断设计符合或违反哪条 SOLID 原则；在代码里找坏味道（code smell）；选重构（refactoring）方案或说明用哪个设计模式重构。
> 历年出现：选择题 2024 S1（OCP、找坏味道、重构建议）、2025 S1（LSP）；短答和开放题 2021 S2、2022 S1、2023 S1、2023 S2、2024 S2、2025 S2。今年预测：官方提示没有开放题，大概率出在选择题里；也可能作为某题的一个带字数限制的小问。

## 目录

- [1 先认题型](#1-先认题型)
- [2 零基础速懂](#2-零基础速懂)
  - [2.1 SOLID 五条原则总表](#21-solid-五条原则总表)
  - [2.2 五条原则的最小反例](#22-五条原则的最小反例)
  - [2.3 坏味道和重构是什么](#23-坏味道和重构是什么)
  - [2.4 常见坏味道总表](#24-常见坏味道总表)
- [3 套路](#3-套路)
  - [3.1 选择题判断法](#31-选择题判断法)
  - [3.2 坏味道到设计模式的对照和改写模板](#32-坏味道到设计模式的对照和改写模板)
  - [3.3 短答题五问模板](#33-短答题五问模板)
  - [3.4 违反哪条 SOLID 的四步写法](#34-违反哪条-solid-的四步写法)
  - [3.5 综合开放题段落结构](#35-综合开放题段落结构)
- [4 真题套用](#4-真题套用)
  - [4.1 2025 S2 Q3 PaymentProcessor 坏味道短答](#41-2025-s2-q3-paymentprocessor-坏味道短答)
  - [4.2 2025 S1 Q4 MCQ8 Vector2 Vector3 与 LSP](#42-2025-s1-q4-mcq8-vector2-vector3-与-lsp)
  - [4.3 2024 S2 Q4 综合开放题](#43-2024-s2-q4-综合开放题)
  - [4.4 2024 S1 Q4 MCQ10 OCP](#44-2024-s1-q4-mcq10-ocp)
  - [4.5 2024 S1 Q4 MCQ11 找坏味道](#45-2024-s1-q4-mcq11-找坏味道)
  - [4.6 2024 S1 Q4 MCQ14 重构建议](#46-2024-s1-q4-mcq14-重构建议)
  - [4.7 2023 S2 Q3 多格式读取和 UML 判断 SOLID](#47-2023-s2-q3-多格式读取和-uml-判断-solid)
  - [4.8 2023 S1 Q3 MDToken 违反的两条 SOLID](#48-2023-s1-q3-mdtoken-违反的两条-solid)
  - [4.9 2022 S1 Q4 计算器代码缺陷和扩展性](#49-2022-s1-q4-计算器代码缺陷和扩展性)
  - [4.10 2021 S2 Q4 综合论述](#410-2021-s2-q4-综合论述)
- [5 易错清单](#5-易错清单)

## 1 先认题型

| 题面里出现什么 | 子题型 | 用哪个套路 | 最像的真题 |
|---|---|---|---|
| 选择题，题干有 `Open/Closed Principle`、`Liskov substitution principle`、`adheres to`、`avoid violating` | 判断哪种设计符合或违反某条原则 | 3.1 | 4.2、4.4 |
| 选择题，给一段代码，题干有 `CODE SMELLS`、`select all code smells present` | 找坏味道 | 3.1 加 2.4 的表 | 4.5 |
| 选择题，给几个类，题干有 `appropriate for refactoring` | 选重构建议 | 3.1 加 3.2 | 4.6 |
| `Q3Answers.java` 里填字符串，`identify one code smell`、`Word limit` | 短答题：坏味道名字、位置、危害、设计模式、怎么改 | 3.3 | 4.1 |
| 给 UML 图或一个类，`Which SOLID principle has been violated` | 指出违反哪条原则并给修正方案 | 3.4 | 4.7、4.8 |
| `What design pattern(s) could be used`、`improve code quality` | 为一个需求选设计模式并说好处 | 3.2 加 3.4 | 4.7 |
| `Open-ended`、`Select two topics`、`deficiencies`、`maximum N sentences` | 综合开放题 | 3.5 | 4.3、4.9、4.10 |

UML 读图和按 UML 写代码在 [03 设计模式和 UML](03-design-patterns-uml.md)。许可证和知识产权在 [07 选择题知识点](07-mcq-topics.md)。

## 2 零基础速懂

### 2.1 SOLID 五条原则总表

SOLID 是五条面向对象设计原则的首字母。题面只会写英文全称，要认得。

| 缩写 | 英文全称 | 一句话含义 | 代码里违反它的信号 | 常见修法 |
|---|---|---|---|---|
| SRP | Single Responsibility Principle 单一职责 | 一个类只负责一件事，只有一个修改理由（one reason to change） | 同一个类里既有计算，又有显示、读写文件；一个类什么都管 | 按职责拆成多个类（Extract Class） |
| OCP | Open/Closed Principle 开闭 | 加新功能靠新增类，不靠改旧类（open for extension, closed for modification） | 按类型判断的 if-else 或 switch；每加一种类型都要改同一个方法 | 接口或抽象类加多态；Strategy、State、Template Method、Factory Method |
| LSP | Liskov Substitution Principle 里氏替换 | 用到父类的地方换成任何子类对象，程序仍然正确 | 子类重写方法后抛异常、返回 `null`、空实现、改变含义；调用方要写 `instanceof` | 不硬继承；两个类并列继承同一个抽象父类；把做不到的方法移出父类 |
| ISP | Interface Segregation Principle 接口隔离 | 接口要小，不强迫实现类写它用不到的方法 | 一个大接口；实现类里有空方法或 `throw new UnsupportedOperationException()` | 拆成几个小接口，每个类只实现需要的 |
| DIP | Dependency Inversion Principle 依赖倒置 | 高层类依赖接口，不依赖具体类（depend on abstractions, not concretions） | 业务类内部直接 `new` 具体类；字段类型是具体类 | 字段类型换成接口，对象从构造器传进来（dependency injection） |

### 2.2 五条原则的最小反例

每段是一条原则的反例，注释里写了错在哪和怎么修。

```java
// SRP 反例：算法变、页面变、存储变都要改这个类。修法：拆成 Report、ReportPrinter、ReportStore
class Report {
    String title;
    double total() { return 0; }                          // 业务计算
    String toHtml() { return "<h1>" + title + "</h1>"; }  // 显示格式
    void saveToFile(String path) { }                      // 存储
}
// OCP 反例：加一种图形就要回来改 area。修法：接口 Shape，每种图形一个实现类，见 3.2
class AreaCalculator {
    double area(String shape, double x) {
        if (shape.equals("CIRCLE")) return Math.PI * x * x;
        else if (shape.equals("SQUARE")) return x * x;
        return 0;
    }
}
// LSP 反例：按 Bird 写的代码调用 bird.fly()，传入 Penguin 就崩溃。修法：fly 移到 Flyable 接口
class Bird { void fly() { System.out.println("flying"); } }
class Penguin extends Bird {
    @Override void fly() { throw new UnsupportedOperationException(); }
}
// ISP 反例：Robot 不吃饭，被迫写空方法。修法：拆成 Workable 和 Eatable 两个接口
interface Worker { void work(); void eat(); }
class Robot implements Worker {
    public void work() { System.out.println("working"); }
    public void eat() { }
}
// DIP 反例：高层类自己 new 具体类，换数据库或写测试都要改它
// 修法：定义 interface Database，字段类型改成 Database，对象由构造器传入
class MySqlDatabase { void save(String s) { } }
class OrderService {
    private MySqlDatabase db = new MySqlDatabase();
    void place(String order) { db.save(order); }
}
```

### 2.3 坏味道和重构是什么

- 代码坏味道（code smell）：代码能运行、结果也对，但结构上有问题，以后难读、难改、难测。它是症状，不是 bug。
- 重构（refactoring）：不改变外部行为（输入输出不变），只改内部结构。重构前后要跑同一批测试，测试是安全网。
- 修 bug、加功能都不算重构，因为行为变了。
- 四个词的关系：坏味道是症状，SOLID 是判断标准，重构是动作，设计模式（design pattern）是重构后的目标结构。
- 答题顺序固定：坏味道名字，在哪一行，有什么后果，用什么手法或模式改。

### 2.4 常见坏味道总表

答题写英文名。括号里是别的常见叫法；2024 S1 选择题的选项用的是 `DRY`、`Hardcoding`、`Mysterious Names`、`Dead Code`、`Long parameter list`，遇到这些词按下表对应。

| 英文名 | 中文 | 代码里的信号 | 重构手法 | 可用设计模式 |
|---|---|---|---|---|
| Long Method（Long Function） | 过长方法 | 一个方法几十行，做了好几件事，要靠注释分段 | Extract Method | Template Method |
| Large Class（God Class） | 过大类 | 一个类字段和方法很多，什么都管；违反 SRP | Extract Class | Facade 加若干小类 |
| Long Parameter List | 过长参数列表 | 方法有 4 个以上参数；`item1, item2, item3` 这类编号参数 | Introduce Parameter Object；改传 `List` 或对象 | 无 |
| Duplicated Code（违反 DRY，Don't Repeat Yourself） | 重复代码 | 两段代码只差几个值；几个类的字段和方法一模一样 | Extract Method；Extract Superclass；Pull Up Method | Template Method |
| Switch Statements（Long if-else chain，Repeated Switches） | 按类型分支 | `if (type.equals("A")) ... else if (type.equals("B")) ...`，加类型就加分支；违反 OCP | Replace Conditional with Polymorphism | Strategy、State、Factory Method、Template Method |
| Magic Number（Hardcoding） | 魔法数和写死的值 | 代码里直接出现 `0.15`、`500`、`"PAYPAL"`，看不出含义 | Replace Magic Number with Symbolic Constant；改成参数、常量、枚举或配置 | 无 |
| Dead Code | 死代码 | 从不被调用的方法、从不使用的字段或变量、执行不到的分支 | Remove Dead Code | 无 |
| Primitive Obsession | 基本类型偏执 | 用 `String` 或 `int` 表示有固定取值或有结构的概念，如位置、地址、金额 | Replace Primitive with Object；Replace Type Code with Enum | 无 |
| Feature Envy | 依恋情结 | 一个方法大量调用另一个类的 getter，很少用自己的字段 | Move Method | 无 |
| Data Class | 纯数据类 | 只有字段和 getter、setter，没有行为；行为散落在别的类里 | Move Method；Encapsulate Field | 无 |
| Mysterious Name | 命名不清 | `a`、`tmp`、`doSomething`、`displaySomething`，看名字不知道用途 | Rename Method；Rename Variable | 无 |
| Refused Bequest | 拒绝继承 | 子类继承了用不上的方法，重写成空方法或抛异常；违反 LSP | Replace Inheritance with Delegation；拆接口 | 无 |
| Shotgun Surgery | 霰弹式修改 | 一个小改动要同时改很多个类 | Move Method；Move Field，把相关代码收到一个类 | Facade |

## 3 套路

### 3.1 选择题判断法

适用场景：选择题问哪些设计符合或违反某条原则，哪些坏味道存在，哪些重构建议合适。

1. 先圈题干的四类词：`CORRECT` 或 `INCORRECT`、`NOT`、`select all`、`best`。2024 S1 是多选，全部选对才得分；2025 S1 是单选。
2. 圈出题干点名的原则或任务（`OCP`、`LSP`、`code smells`、`refactoring`）。只按这一条标准判断，不要因为选项「总体上是好设计」就选。
3. 每个选项单独判断，用下表的问题问自己，在草稿上标 T 或 F。
4. 多选题不比较选项之间谁更好，每个选项都是一道独立判断题。单选题有两个都像对的，选直接回答题干那一条原则的。
5. 给代码找坏味道时，按 2.4 的表从上到下逐行扫：参数个数、重复的代码块、写死的数字和字符串、没被调用的方法、看不懂的名字、if-else 链。
6. 「做某个改动可以消除几个坏味道」这类选项，数一下这个改动涉及的代码身上有几种坏味道。
7. 按题目要求的格式填答案，如 `10. B,C`，再跑 `MCQAnswerSyntaxTest`。

| 原则 | 问自己的一个问题 | 选项这样写是符合 | 选项这样写是违反 |
|---|---|---|---|
| SRP | 这个类有几种不同的修改理由 | 每种职责单独一个类；把统计、显示、存储拆出去 | 把新的无关功能加进已有的类（`should be in the X class`） |
| OCP | 加一种新类型时，要不要打开旧类改代码 | 定义接口或抽象类，每种类型一个新类去实现 | `modify the class`；`add conditional statements`；给旧类为每种类型加一个方法 |
| LSP | 把子类对象交给只认识父类的代码，结果还对吗 | 两个类并列继承同一个抽象父类；子类遵守父类方法的约定 | 为了复用字段硬让 A 继承 B；子类重写后抛异常、返回 `null`、含义变了 |
| ISP | 实现类有没有被迫写用不到的方法 | 多个小接口，各取所需 | 一个包含所有方法的大接口；空方法 |
| DIP | 高层类的字段和参数类型是接口还是具体类 | 依赖接口，对象由外部传入 | 高层类内部直接 `new` 具体类 |

重构建议类选项的快速判断：

| 选项的做法 | 判断 |
|---|---|
| 把 `String` 字段换成类或 `enum` | 合适，消除 Primitive Obsession |
| 提取父类或接口去掉重复字段，而且 is-a 关系成立 | 合适 |
| 把 `private` 改成 `public` | 不合适，破坏封装（encapsulation） |
| 把统计、显示等新职责加进已有的领域类 | 不合适，违反 SRP |
| 删除没人调用的方法 | 合适，Remove Dead Code |

时间不够：先排除明显违反封装、明显往一个类里堆功能、明显加 if-else 的选项。

### 3.2 坏味道到设计模式的对照和改写模板

适用场景：题目要求「用一个设计模式解决这个坏味道」，或者选择题问哪种改法合适。

1. 找出代码里会随需求增加的部分（通常是 if-else 链的各个分支）。
2. 按下表选模式。
3. 说清三件事：新建什么接口或抽象类，原来每个分支的代码搬到哪个新类的哪个方法，原来的类还剩什么。

| 代码里的症状 | 模式 | 怎么搬 |
|---|---|---|
| 按类型字符串或枚举选行为的 if-else 链，各分支是可以互换的算法 | Strategy | 每个分支一个实现类；原类持有接口，调用接口方法 |
| 各分支前后有相同的固定步骤，只有中间一步不同 | Template Method | 抽象父类用 `final` 方法写固定流程，中间一步是抽象方法，子类各自实现 |
| 按当前状态字段分支，方法执行后还会改状态 | State | 每个状态一个类，状态类自己决定下一个状态 |
| if-else 里按类型 `new` 不同对象 | Factory Method 或简单工厂 | 创建对象的代码集中到工厂方法 |
| 一个对象变化后要手动逐个通知别的对象 | Observer | `attach`、`detach`、`notify` |
| 调用方要按顺序调用很多个子系统类 | Facade | 一个入口类把调用顺序封装起来 |
| 到处 `new` 同一个管理对象，或者用全局变量 | Singleton | 私有构造器加 `getInstance()` |

课件看不到。历年代码题确认考过的模式是 State、Singleton、Observer、Iterator、Factory Method、Template Method，散题有 Facade。Strategy 是按类型分支的通用标准答案，但题面如果写了 `covered in the course`，先确认自己的课件里有 Strategy；没有就用 Template Method 或 Factory Method。

Strategy 改写模板（片段，省略了另外两个实现类；完整版和原代码对同样输入的输出逐行一致）：

```java
interface PaymentStrategy { void pay(double amount); }       // 1 新建接口

class PayPalPayment implements PaymentStrategy {              // 2 每个分支一个类
    public void pay(double amount) {
        System.out.println("Connecting to PayPal...");        // 原分支里的代码原样搬过来
        System.out.println("Transferring funds: $" + amount);
    }
}

public class PaymentProcessor {
    private final Map<String, PaymentStrategy> strategies = new HashMap<>();  // 3 名字到策略的表

    public PaymentProcessor() {
        strategies.put("PAYPAL", new PayPalPayment());        // 加新类型只加一行注册
    }

    public void processPayment(String method, double amount) {
        System.out.println("Processing payment of $" + amount);   // 公共步骤留在原类
        PaymentStrategy strategy = strategies.get(method);
        if (strategy == null) System.out.println("Unknown payment method: " + method);
        else strategy.pay(amount);                                // 4 if-else 链换成一次调用
        System.out.println("Payment completed.");
    }
}
```

Template Method 的改法：`PaymentProcessor` 改成抽象类，`processPayment` 加 `final`，中间一步换成 `protected abstract void pay(double amount)`，每种付款方式一个子类实现 `pay`。各模式的完整代码在 [03 设计模式和 UML](03-design-patterns-uml.md)。

### 3.3 短答题五问模板

适用场景：2025 S2 Q3 这种题，在 `Q3Answers.java` 的字符串里回答 A 到 E 五问，每问有严格字数限制。超字数会扣分，而且只批改前 W 个词。

1. 先填 `UID` 和 `NAME`（测试要求 `UID` 去掉空格后正好 8 个字符）。
2. 选一个坏味道。优先选能用设计模式解决的那个（通常是按类型分支的 if-else 链），不要选 Magic Number 这类没有对应模式的。
3. 按下表写五问。A 和 D 只写名字。
4. 数词数：按空格分开数，`PaymentProcessor.processPayment`、`if-else` 各算一个词。每问留 5 到 10 个词的余量，防止阅卷人把连字符或斜杠拆开数。
5. 跑题目给的测试。字符串里不要出现连续两个空格（原题测试用 `split(" ")` 数词，连续空格会多算）。
6. 时间不够：A、B、D 三问最短，先写完；C 和 E 各写两句。

| 问 | 限制 | 英文句型 |
|---|---|---|
| A 坏味道名字 | 4 词以内 | `Long if-else chain`；`Duplicated Code`；`Long Parameter List`；`Long Method` |
| B 位置 | 20 词以内 | `In <Class>.<method>, lines <a>-<b>: the <if-else chain> that <compares the method string using equals>.` |
| C 危害 | 100 词以内 | 四句，每句一个后果，带上题目里的类名和业务词。`Every <type> is hard-coded as a branch of one method, so <Class> violates the Open/Closed Principle.` `Supporting a new <type> means editing and retesting <method>, which risks breaking the working <A>, <B> and <C> flows.` `The chain grows with every <type>, making the code harder to read and maintain.` `A single <behaviour> cannot be unit-tested or reused in isolation.` |
| D 模式名字 | 4 词以内 | `Strategy`；`Template Method`；`State`；`Factory Method` |
| E 怎么改 | 100 词以内 | 五句：新建什么，搬什么到哪里，原类加什么，原方法留什么，好处。`Create an interface <I> declaring <m>(<params>).` `Create classes <A>, <B> and <C> implementing it, and move the statements of each if-else branch into the matching <m> method.` `In <Class>, add a <type> field; the constructor registers the strategies.` `In <method>, keep <the common first and last statements> and replace the chain with a call to <strategy.m(args)>.` `A new <type> then needs only a new class; existing code stays unchanged.` |

数词数的办法：在自己临时建的类的 `main` 里运行 `System.out.println(Q3Answers.SMELL_IMPACT.trim().split("\\s+").length);`，这个临时类不要提交。4.1 是填好的完整例子。

### 3.4 违反哪条 SOLID 的四步写法

适用场景：给 UML 或代码，问 `Which SOLID principle has been violated`、`Why and how`、`how to rectify`。

1. 原则名加定义：写全称，再用一句话定义。
2. 证据：指出具体的类名、方法名、字段名。没有证据不得分。
3. 后果：举一个具体场景，说明会出什么错，或者加什么需求时要改哪里。
4. 改法：新建什么类或接口，哪些成员搬到哪里，改完后谁依赖谁。

选原则的办法：有继承并且子类重写出问题，选 LSP；有按类型的字段、枚举或 if-else，选 OCP；一个类管好几件事，选 SRP；接口太大，选 ISP；直接 `new` 具体类，选 DIP。代码里没有继承就不要写 LSP。

定义句（第 1 步直接抄）：

| 原则 | 英文定义句 |
|---|---|
| SRP | `A class should have only one responsibility, i.e. only one reason to change.` |
| OCP | `Software entities should be open for extension but closed for modification.` |
| LSP | `Objects of a subclass must be usable wherever the superclass is expected without breaking the program.` |
| ISP | `Clients should not be forced to depend on methods they do not use.` |
| DIP | `High-level modules should depend on abstractions, not on concrete classes.` |

第 2 到 4 步句型：

- 证据：`<Class> violates this because <member> ...`；`<Sub>.<method>() returns null although <Super> declares it returns <Type>.`
- 后果：`For example, if we add <new type>, we must modify <Class> and retest <existing features>.`；`A client such as <Client> that calls <method> on a <Super> will throw a NullPointerException when it receives a <Sub>.`
- 改法：`To fix this, I would extract <interface/abstract class> ..., move <member> into <new class>, so that <client> depends only on <abstraction>.`

时间不够：每个原则写三句，原则名加证据一句，后果一句，改法一句。

### 3.5 综合开放题段落结构

适用场景：`Open-ended`，没有固定答案，要求结合课程内容论述，常有句数或词数上限。

1. 先看限制：句数（`maximum 10 sentences`）、词数（`min 100, max 400`）、是否要求 bullet points。
2. 每个论点一小段，固定四句：论点（Claim），课程概念（Concept），例子（Example），好处或后果（Benefit）。只罗列名词不得分。
3. 概念从课程主题里取：design patterns、testing、trees、tokenisation and parsing、persistent data、refactoring、design by contract、performance analysis、intellectual property。
4. 写完数句数，一个句号算一句。

句型：

- 论点：`<Topic> is the practice of ... . It supports software construction by ...`
- 例子：`For example, <concrete class / tool / situation> ...`
- 关系：`<Topic A> depends on <Topic B> because ...`；`In turn, <B> makes <A> easier because ...`
- 质量属性：`This improves reliability / maintainability / performance / testability because ...`
- 道德问题：`One ethical issue is ... . This harms <who> because ... . Engineers should ... (IEEE Code of Ethics: act in the public interest).`

## 4 真题套用

选择题没有官方答案，下面的答案都是非官方答案。

### 4.1 2025 S2 Q3 PaymentProcessor 坏味道短答

**题意**：`PaymentProcessor.processPayment(String method, double amount)` 先打印一行，再用 if-else 链按 `method` 等于 `CREDIT_CARD`、`PAYPAL`、`BANK_TRANSFER` 各打印两行，其他值打印 `Unknown payment method`，最后打印 `Payment completed.`。要求找出一个能用课程里的设计模式解决的坏味道，在 `Q3Answers.java` 的五个字符串里回答 A 到 E。词数上限：A 4、B 20、C 100、D 4、E 100，超出扣分且只批改前 W 个词。20 分，开放题没有官方答案。原题：[Q3README.md](../../exam-practice-questions/past-exams/2025-2/src/Q3/Q3README.md)、[PaymentProcessor.java](../../exam-practice-questions/past-exams/2025-2/src/Q3/PaymentProcessor.java)、[Q3Answers.java](../../exam-practice-questions/past-exams/2025-2/src/Q3/Q3Answers.java)。

**套路对应**：3.3 的六步。坏味道选 if-else 链（它能用模式解决）。B 的位置是 `processPayment` 的第 6 到 17 行。C 套四句危害句型，把 `<type>` 换成 payment method。D 和 E 按 3.2：各分支是可以互换的付款算法，用 Strategy；开头和结尾两行是固定步骤，也可以用 Template Method。

**关键代码**（示范答案，已填进原题文件编译，`Q3Test` 2 个测试通过；注释里是按空格数的实际词数）：

```java
public static final String UID = "u1234567";      // 换成自己的，8 个字符
public static final String NAME = "Your Name";

public static final String CODE_SMELL_NAME = "Long if-else chain";   // A：3 词，上限 4

// B：14 词，上限 20
public static final String SMELL_LOCATION = "In PaymentProcessor.processPayment, lines 6-17: the if-else chain that compares the method string using equals.";

// C：91 词，上限 100
public static final String SMELL_IMPACT = """
        Every payment method is hard-coded as a branch of one method, so PaymentProcessor
        violates the Open/Closed Principle. Supporting a new method such as Apple Pay means
        editing and retesting processPayment, which risks breaking the working credit card,
        PayPal and bank transfer flows. The chain grows with every method, making the code
        harder to read and maintain. A single payment behaviour cannot be unit-tested or
        reused in isolation. Methods are compared as raw strings, so a typo is not caught
        at compile time and only reaches the unknown branch at run time.
        """;

public static final String DESIGN_PATTERN = "Strategy";              // D：1 词，上限 4

// E：89 词，上限 100
public static final String REFACTORING_PLAN = """
        Create an interface PaymentStrategy declaring void pay(double amount). Create classes
        CreditCardPayment, PayPalPayment and BankTransferPayment implementing it, and move the
        two println statements of each if-else branch into the matching pay method. In
        PaymentProcessor, add a Map<String, PaymentStrategy> field; the constructor registers
        the three strategies under their method names. In processPayment, keep the first and
        last println and replace the chain with a map lookup: if no strategy is found, print
        the unknown-method message, otherwise call strategy.pay(amount). A new payment method
        then needs only a new class and one registration.
        """;
```

备选的 D 和 E（课件里没有 Strategy 时用）。D 写 `Template Method`（2 词）。E（91 词）：

```text
Make PaymentProcessor an abstract class. Change processPayment to take only amount and
declare it final as the template method: it prints the first message, calls a new
protected abstract method pay(double amount), then prints the last message. Create
subclasses CreditCardProcessor, PayPalProcessor and BankTransferProcessor; move the two
println statements of each if-else branch into the matching subclass's pay method, then
delete the chain. Callers create the subclass for the method they need instead of
passing a string. A new payment method then needs only a new subclass; the template
method stays unchanged.
```

为什么这样选：

- 题目要求坏味道必须「能用一个设计模式解决」。`"CREDIT_CARD"` 这些写死的字符串（Hardcoding）也是坏味道，但没有对应的模式，选它 D 和 E 就写不下去。
- A 也可以写 `Switch Statements`，这是重构书里的正式名字，if-else 链属于这一类。
- E 必须回答题目点名的三件事：改什么（新建接口和三个类），改哪部分（每个分支的两行 `println`），放到哪里（对应类的 `pay` 方法；原类加字段，`processPayment` 保留首尾两行）。
- 两种改法都按 E 的描述写成了代码并运行过。Strategy 版对 `CREDIT_CARD`、`PAYPAL`、`BANK_TRANSFER`、`paypal`、`CASH` 五种输入的输出和原代码逐行相同；Template Method 版对三种已知方式的输出和原代码相同。

**坑**：

- 把连字符、斜杠、点号全部拆开数，这份答案是 A 4、B 17、C 94、E 95，仍在上限内。自己改写时保持这个余量。
- 文本块（三引号字符串）里换行不算空格，原题的 `Q3Test` 会少数词（C 只数出 85）。测试通过不代表没超，要自己按真实词数数。
- 原代码对未知方式也会打印 `Payment completed.`，这是行为问题，不是题目要的坏味道。重构不改变行为，所以示范改法保留了这个输出。
- Template Method 版改了 `processPayment` 的参数，调用方要自己选子类。E 里要把这一点写出来。
- 只写 `Use Strategy pattern to improve the code` 不得分，必须写出接口名、类名、方法名和搬动的语句。

### 4.2 2025 S1 Q4 MCQ8 Vector2 Vector3 与 LSP

**题意**：单选，题目标签是 `Refactoring`。`Vector2` 有私有字段 `x`、`y`，方法 `getLength()`（到原点的距离）和 `dilate(Vector2 other)`（逐分量相乘，返回新的 `Vector2`）。现在要写一个多了 z 轴的 `Vector3`，问怎样做才不违反 LSP。原题：[MC_Questions_2025_S1.pdf](../../exam-practice-questions/past-exams/2025-1/src/Q4/MC_Questions_2025_S1.pdf) 的 Q8。

**套路对应**：3.1。题干圈 `avoid violating the Liskov substitution principle`。对每个选项问：它让谁成为谁的子类型；把子类型对象交给只认识父类型的代码，结果还对吗。

**推导**：非官方答案 a，把握高。

- a `Vector2` 和 `Vector3` 都继承抽象类 `Vector`：对。两个类并列，谁也不是谁的子类型，不存在互相替换，LSP 无从违反。
- b 让 `Vector2` 继承 `Vector3`：错。`Vector2` 没有 z，按 `Vector3` 写的三维计算拿到它，结果不对。
- c 把 `Vector2` 改成接口，由 `Vector3` 实现：错。仍然声明 `Vector3` 是一种 `Vector2`，替换问题还在。
- d 字段标成 `final`：错。不可变（immutable）管的是对象会不会被修改，不管子类能不能替换父类。

写了一个 `Vector3 extends Vector2` 的小程序验证最直接的做法为什么不行：按 `Vector2` 写的代码拿到 `Vector3(3,4,12)`，`getLength()` 得到 13 而不是平面长度 5；两个 `Vector3` 调 `dilate` 返回的是 `Vector2`，z 丢了。

**坑**：

- 字段多一个不代表是子类。判断继承是否合理看行为能不能替换，不看字段像不像。
- LSP 选择题的正确选项常是「不要让其中一个继承另一个，改成并列继承同一个抽象父类」。

### 4.3 2024 S2 Q4 综合开放题

**题意**：从 9 个主题（Design Patterns、Software Testing、Trees、Tokenisation and Parsing、Persistent Data、Refactoring、Design by Contract、Performance Analysis、Intellectual Property）里选 2 个。A 问解释两个主题以及它们和软件构造（software construction）的关系并举例，最多 10 句；B 问两个主题之间的关系，最多 5 句；C 问提出一个道德问题或对社会、环境的负面影响，最多 5 句。每个论断都要有理由。没有官方答案。原题：[Q4README.md](../../exam-practice-questions/past-exams/2024-2/src/Q4/Q4README.md)，答在 `Q4Answers.md`。

**套路对应**：3.5。选自己例子最多的两个主题，下面选 Refactoring 和 Software Testing。A 问每个主题三句：定义，为什么属于软件构造，例子。B 问写双向关系。C 问四句：问题，谁受害，依据，该怎么做。

**关键代码**（这里是英文要点，A 6 句、B 4 句、C 4 句，都在句数上限内）：

```text
(A) Refactoring means improving the internal structure of code without changing its external
behaviour. It is part of software construction because code is changed far more often than it is
written, and removing smells keeps later changes cheap. For example, replacing an if-else chain
over payment types with the Strategy pattern lets a new type be added as a new class.
Software testing means executing code with chosen inputs and comparing the results with expected
outputs. It is part of construction because developers write unit tests while coding to find
faults early. For example, a JUnit parameterized test with full branch coverage exercises every
decision of a method at least once.

(B) Refactoring depends on testing: tests that pass before and after a change are the evidence
that behaviour was preserved. Without tests, a refactoring can silently introduce regressions.
In turn, refactoring makes testing easier, because small classes that depend on interfaces can be
tested in isolation. Test code also needs refactoring, e.g. merging duplicated test methods into
one parameterized test.

(C) One ethical issue is releasing software that is known to be insufficiently tested because of
deadline pressure. In medical or financial software, an untested branch can cause physical or
financial harm to the public. This conflicts with the IEEE Code of Ethics, which requires
engineers to act in the public interest. Engineers should report the remaining risk honestly
instead of hiding it.
```

**坑**：

- 限制的是句数，不是词数。一个句号算一句，分号可以把两个意思并成一句。
- 只列概念名不得分，每个主题都要有具体例子。
- B 问要写两个方向的关系，只写一个方向内容太薄。

### 4.4 2024 S1 Q4 MCQ10 OCP

**题意**：多选，全对才得分。食谱库的 `Recipe` 负责显示详情，以后会不断加新食谱类型（vegetarian、vegan、gluten-free）和新的显示方式。选出所有最符合 OCP 的设计。原题：[Q4README.md](../../exam-practice-questions/past-exams/2024-1/src/Q4/Q4README.md) 的 MCQ10。

**套路对应**：3.1。题干圈 `Select all` 和 `Open/Closed Principle`。每个选项只问一件事：加一种新食谱时，要不要打开旧的 `Recipe` 改代码。

**推导**：非官方答案 B、C，把握高。

- A 给 `Recipe` 为每种新类型和新格式加方法。违反。新需求要改旧类。
- B 用子类 `VegetarianRecipe`、`VeganRecipe`，各自实现显示方法。符合。新类型是新类。
- C 定义接口 `Displayable`，方法 `displayInfo()`，每种食谱一个实现类。符合。调用方依赖接口，新类型是新类。
- D 在同一个 `displayInfo()` 里加条件分支。违反。新类型要改旧方法。

**坑**：

- B 的 "their own methods" 要写成重写父类或接口已有的显示方法。调用方如果还得 `instanceof` 再调子类专有方法，加类型时调用方也要改，OCP 就不完整。
- 题干写的是 best adheres。A 和 D 都是改旧类，B 和 C 都是加新类。

### 4.5 2024 S1 Q4 MCQ11 找坏味道

**题意**：多选，全对才得分。`RecipeManager.addRecipe` 有 `recipeName` 加 6 个食材参数。`main` 加了两道菜。`calculateCalories` 把两道菜的热量数字写死，各算一遍。`displaySomething` 打印 `This method does nothing...`。常量 `DEFAULT_UNIT` 没有任何引用。选出存在的坏味道，以及能消除坏味道的做法。原题：[Q4README.md](../../exam-practice-questions/past-exams/2024-1/src/Q4/Q4README.md) 的 MCQ11。

**套路对应**：3.1 第 5、6 步，对照 2.4。先数参数，再找重复块、写死的数字、没人用的成员、看不懂的名字。

**推导**：非官方答案 A、B、C、D、E、G。不选 F。H 不确定。

- A DRY：两道菜都是「打印菜名、逐项相加、打印总和」，结构重复。选。
- B Hardcoding：热量 `500`、`300`、`15` 和菜名直接写在方法里，没有用 `addRecipe` 传进来的食材。选。
- C Mysterious Names：`displaySomething` 看不出要显示什么。选。
- D Dead Code：`displaySomething` 在类里没有被调用，`DEFAULT_UNIT` 也没有被读。选。
- E Long parameter list：`addRecipe` 有 7 个参数。选。
- F 没有坏味道。上面五条都在，不选。
- G 写一个接收食材列表的热量方法，可以去掉已有的一种坏味道。两道菜共用这一个方法，重复的累加就没了。选。
- H 删掉 `displaySomething` 可以去掉至少两种坏味道。不确定。这个方法同时是 Mysterious Name 和没人调用的代码，删掉它这两处都消失。`DEFAULT_UNIT` 仍然没人用，Dead Code 这个种类还在。按「种类还在就不算去掉」则 H 不成立。按「这个方法身上的两种都算去掉」则 H 要选。两种涂法不能同时对。本笔记不选 H。

把题面代码存成 `RecipeManager.java` 后用 `javac` 编译：第二处 `addRecipe` 传了 8 个字符串，方法只要 7 个，报实际参数列表和形式参数列表长度不同。这是 E 的直接后果。

**坑**：

- 选项里的 `DRY`、`Hardcoding`、`Mysterious Names` 就是 2.4 的 Duplicated Code、Magic Number、Mysterious Name。
- `displaySomething` 会打印一行。它算 Dead Code，是因为没有任何调用。
- 全对才得分。本笔记的涂法是 A、B、C、D、E、G。

### 4.6 2024 S1 Q4 MCQ14 重构建议

**题意**：多选，全对才得分。`Player` 有 `name`、`position`（String）、`jerseyNumber`。`Team` 有 `name`、`coach`（String）、`List<Player> players`。`Match` 有 `homeTeam`、`awayTeam`、`date`、`location`（String）。选出合适的重构建议。原题：[Q4README.md](../../exam-practice-questions/past-exams/2024-1/src/Q4/Q4README.md) 的 MCQ14。

**套路对应**：3.1 的重构建议表。String 换成类或 enum，看是不是 Primitive Obsession。`private` 改成 `public` 会破坏封装。把显示塞进已有的类，违反 SRP。

**推导**：非官方答案 B、D。A 不确定，不选。C、E 不选。

- A 新建 `Person` 和 `Coach`，让 `Player` 和 `Coach` implement `Person` 这个 Class。不确定。领域上球员和教练都是人，把 `coach` 从 String 换成类，能去掉 Primitive Obsession。选项把 Person 写成 Class，又要求 implement。Java 里类用 extends。现有字段里也没有两份重复的人的数据，3.1 的条件是 is-a 成立而且能去掉重复字段。本笔记不选 A。
- B 为 `Match.location` 建 `Location` 类。合适。地点是一个有结构的概念。选。
- C 把 `players` 从 private 改成 public。不合适。破坏封装。
- D 用 enum 表示球员位置，替换 String。合适。位置是固定的一组取值。选。
- E 比赛统计和可视化的方法、字段都放进 `Match`。不合适。可视化是另一种修改理由，违反 SRP。

**坑**：

- 按 3.1 那张表判断，不按「听起来更面向对象」判断。
- 阅卷如果只看领域模型，可能把 A 算对。全对才得分。本笔记的涂法是 B、D。

### 4.7 2023 S2 Q3 多格式读取和 UML 判断 SOLID

**题意**：企鹅 App。a 问怎样设计「从本地读 JSON、XML、CSV、Bespoke 四种文件」：总体设计、用哪个设计模式、对代码质量有什么好处。b 问看 UML 和 `Penguinologist` 的代码，指出违反了哪一条 SOLID、为什么有问题、怎么改。没有字数上限，没有官方答案。原题：[readme_Q3.md](../../exam-practice-questions/past-exams/2023-2/src/Q3/readme_Q3.md)，图在同目录的 `UML_Q3.png`、`UML_Example_PenguinologistClass.png`。

UML 里 `User` 有 `getPhotos(): Set<Image>` 和 `accessResearch(): Set<Document>`。`Penguinologist` 和 `BasicUser` 继承 `User`。`PhotoViewer.displayPhotos()` 与 `User` 的关系是 displays photos of。代码摘录里 `Penguinologist.getPhotos()` 的方法体是 `return null`，`accessResearch()` 返回字段 `research`。`BasicUser` 的图上写的是 `viewPhotos()`。

**套路对应**：a 问用 3.2。四种格式是可替换的算法，用 Strategy。按扩展名创建对象，用 Factory Method。b 问用 3.4。有继承，而且重写后返回 `null`，选 LSP。

**关键代码**（英文要点。a 7 句 87 词，b 5 句 77 词。按空格分词，行首的 `(a)`、`(b)` 标签不计入。句号只数后面是空格或结尾的那个点）：

```text
(a) Define a DataReader interface with String read(Path path). JsonReader, XmlReader, CsvReader and BespokeReader each implement read for one format. A factory method picks the reader from the file extension, and the app only calls reader.read(path).
Use Strategy, because each format is an interchangeable algorithm. Factory Method only creates the chosen reader. A new format is a new class plus one registration, so existing readers and the app stay unchanged. The app depends on DataReader, which is the Dependency Inversion Principle, and tests can pass a fake reader.

(b) The design violates the Liskov Substitution Principle: a Penguinologist must be usable wherever a User is expected. User.getPhotos() promises a Set of images, and PhotoViewer.displayPhotos() displays the photos of a User. Penguinologist.getPhotos() returns null, so user.getPhotos().size() throws a NullPointerException when the object is a Penguinologist.
Return the photos, or Collections.emptySet() when there are none. If a penguinologist really has no photos, remove getPhotos() from User and put it on a separate interface that only photo users implement.
```

a 问只回答读文件。b 问的证据是子类把父类承诺的集合换成 `null`，而 `PhotoViewer` 只认识 `User`。

**坑**：

- a 和 b 分开写。读文件用 Strategy 或 Factory Method。用户继承用 LSP。
- 课件里如果没有 Strategy 这个名字，a 问改答 Factory Method：抽象创建者的工厂方法返回 `DataReader`，四种格式四个子类。3.2 有这条说明。
- 同一段 `return null` 也可以论证 ISP：`Penguinologist` 用不上 `getPhotos`，却必须实现它。题目只问一条原则。摘录把 `return null` 摆出来，主答 LSP。

### 4.8 2023 S1 Q3 MDToken 违反的两条 SOLID

**题意**：`MDToken` 同时表示 markdown 列表的根、有序项和无序项。字段有 `tokenType`、`content`、`level`、`number`。无参构造器把类型设成 `LIST_ROOT`。四参构造器带 `number`，注释写明给有序项用。三参构造器没有 `number`。要求选两条被违反的 SOLID，每条写定义、代码里怎么违反、一个会出问题的例子，再写怎么改。没有官方答案。原题：[Q3README.md](../../exam-practice-questions/past-exams/2023-1/Q3/Q3README.md)，类在 [MDToken.java](../../exam-practice-questions/past-exams/2023-1/Q2/MDToken.java)。`MDTokenType` 只有 `LIST_ROOT`、`ORDERED_ITEM`、`UNORDERED_ITEM`。

**套路对应**：3.4。这个类没有继承，不写 LSP。没有大接口，不写 ISP。一个类装了三种列表元素，选 SRP。新元素要改这个类和它的构造器，选 OCP。

**关键代码**（英文要点，7 句，111 词，按空格分词）：

```text
The Single Responsibility Principle says a class should have only one reason to change. MDToken represents the list root, ordered items and unordered items, and the field number belongs only to ordered items. A change to numbering forces edits and retests of the class that unordered items also use.
The Open/Closed Principle says code should be open for extension but closed for modification. A task item that needs a checked flag requires a new field and a new constructor on MDToken. Add an abstract token for content and level, put number only on OrderedItemToken, and add TaskItemToken as a new subclass. The parser creates the subclass, and existing classes stay unchanged.
```

**坑**：

- `equals` 只比较 `tokenType` 和 `content`，不比 `level` 和 `number`。这是比较写漏了。把它说成 SOLID 时，要接回「一个类同时管数据和比较规则」这种 SRP 论据，否则阅卷人看不出原则。
- 两条都要有定义、类里的字段或构造器、一个具体例子。只写原则名字没有分。

### 4.9 2022 S1 Q4 计算器代码缺陷和扩展性

**题意**：五个运算类各收两个 int：`Add`、`Subtract`、`Multiply`、`Divide`、`Exponentiate`。`Client` 的五个字段是这五个具体类，`compute()` 返回第一个非 null 运算的结果，五个都是 null 就返回 0。Part 1 指出缺陷并改进。Part 2 说明以后加新运算时怎样扩展。Part 3 许可证归 [07 选择题知识点](07-mcq-topics.md)。要求用简短要点。原题：[readme.md](../../exam-practice-questions/past-exams/2022-1/q4/readme.md)，代码在同目录的 `Client.java`、`Exponentiate.java`、`Divide.java`。

**套路对应**：Part 1 先找算错的方法，再按 2.4 找重复代码和写死的具体类。Part 2 用 3.2，五个运算收成一个接口，调用方式和 Strategy 相同。

**关键代码**（英文要点，9 句，117 词，按空格分词。乘方已用 `main` 跑过：`new Exponentiate(2, 3).calculate()` 是 0，`(2, 1)` 是 2，`(2, 0)` 是 2）：

```text
Exponentiate.calculate multiplies by the loop index i, not by a, so 2 to the power 3 returns 0. Multiply by a instead.
Divide.calculate uses integer division and throws ArithmeticException when b is 0.
Client.compute returns only the first non-null operation, and returns 0 when all five fields are null, so a missing operation looks like a real result.
The five classes repeat the same fields and getters, and Client depends on the five concrete classes. Adding Modulo means a new field, a new constructor parameter and a new if branch, which violates the Open/Closed Principle.
Introduce an Operation interface with int calculate(). Client stores one Operation. A new operation is a new class, and Client stays unchanged.
```

**坑**：

- 先写乘方算错。循环是 `ret *= i`，`i` 从 0 开始，指数大于等于 2 时结果是 0。
- 许可证那一问写到 07，这里不要展开。

### 4.10 2021 S2 Q4 综合论述

**题意**：用课程里的概念说明一个团队怎样做出高可靠、高性能、低维护成本的软件。只列主题名称没有分。词数最少 100，最多 400。原题：[readme.md](../../exam-practice-questions/past-exams/2021-2/q4/readme.md)。

**套路对应**：3.5。可靠、性能、低维护各写一句，再写一句三者的关系。可靠对应测试和契约，性能对应先测量再改，低维护对应重构和设计模式。

**关键代码**（英文要点，13 句，223 词，按空格分词，在 100 到 400 之间）：

```text
A team gets reliability, performance and low maintenance by using the construction practices from this course together, not by listing topic names.
Reliability comes from tests written with the code. JUnit tests should cover every branch, including each side of a condition joined by && or ||, and parameterized tests should check the boundaries named in the specification. Design-by-contract annotations such as JML requires and ensures state what must be true before and after a call, so faults are caught before release.
Performance work starts with measurement. A profiler finds the hot path, and only that path is changed. Replacing an unbalanced search tree with a balanced tree keeps lookup proportional to the log of the number of nodes, and that change is worth making only after measurement shows lookup is the bottleneck.
Low maintenance comes from removing code smells and from design patterns. A long if-else chain over types is replaced by the Strategy pattern, so a new type is a new class and existing classes stay unchanged. That is the Open/Closed Principle. Refactoring must keep the observable behaviour the same, which is why the same tests have to pass before and after the change.
These three support each other. Tests make refactoring safe, and smaller classes that depend on interfaces are easier both to test and to tune once a profiler has pointed at them.
```

**坑**：

- 低于 100 词或高于 400 词都不符合题目限制。上面这段是 223 词。
- 每个质量都要有课程里的具体例子，只写 reliability、performance、maintainability 三个词没有分。

## 5 易错清单

1. 2024 S1 的选择题是多选，少选或多选都是 0 分。先圈 `select all`、`INCORRECT`、`NOT`。
2. OCP 只看一件事：加新类型时旧类改不改。给旧类加方法，或在旧方法里加 if-else，都是违反。新子类或新的接口实现，是符合。
3. MCQ10 的 B 要把新的显示方法写成父类或接口方法的重写。调用方再写 `instanceof`，加类型时调用方也要改。
4. MCQ11 里一个方法可以同时占两种坏味道。`displaySomething` 是 Mysterious Name，也没有被调用。`DEFAULT_UNIT` 同样没有被调用，删掉前者之后 Dead Code 还在。H 因此不确定，本笔记不选。涂法是 A、B、C、D、E、G。
5. MCQ11 的第二处 `addRecipe` 多传了一个字符串，题面代码编译不过。这是 Long parameter list 的证据。
6. MCQ14 把 `private` 改成 `public` 不选。把可视化方法放进 `Match` 不选，那是第二个修改理由。`position` 用 enum、`location` 用类，选。涂法是 B、D。
7. MCQ14 的 A 把 Person 写成 Class，又要求 implement。本笔记不选，并标了不确定。
8. LSP 看替换之后的行为。子类 `return null` 或抛 `UnsupportedOperationException`，就是违反。字段多一个，推不出该让谁继承谁。
9. 代码里没有继承，就不要写 LSP。没有被迫实现的大接口，就不要写 ISP。`MDToken` 写 SRP 和 OCP。
10. 短答按空格数词。`Class.method` 和 `if-else` 各算一个词。连续两个空格会让题目的 `split(" ")` 多算一个空词。句数题里，一个后面是空格或结尾的句号算一句。
11. 2022 的计算器先写 `Exponentiate` 把 `ret` 乘了循环变量 `i`。`new Exponentiate(2, 3).calculate()` 得到 0。然后再写 OCP。许可证写到 07。
12. 重构保持原来的输出。2025 S2 里未知支付方式仍然会打印 `Payment completed.`

