# 03 设计模式和 UML

> 这个文件管什么题：题面给一张 UML 类图或状态图，要求按图新建类、补全方法，实现某个设计模式（design pattern）。Singleton 和 UML 关系的选择题也在这里。
> 历年出现：2022 S1、2025 S1（State 加 Singleton），2023 S1（State 加 Observer），2023 S2（工厂），2024 S1（工厂方法加模板方法），2021 S2、2024 S2（Iterator），2025 S2（Observer）。今年预测：三道编程题之一，题型把握高，具体哪个模式猜不准，所以每个模式都备了骨架。

## 目录

- [1 先认题型](#1-先认题型)
- [2 零基础速懂](#2-零基础速懂)
  - [2.1 UML 类图怎么读](#21-uml-类图怎么读)
  - [2.2 UML 到 Java 对照表](#22-uml-到-java-对照表)
  - [2.3 Singleton 单例](#23-singleton-单例)
  - [2.4 State 状态](#24-state-状态)
  - [2.5 Observer 观察者](#25-observer-观察者)
  - [2.6 Iterator 迭代器](#26-iterator-迭代器)
  - [2.7 Factory Method 和 Template Method](#27-factory-method-和-template-method)
  - [2.8 Facade 外观](#28-facade-外观)
  - [2.9 Strategy 策略](#29-strategy-策略)
- [3 套路](#3-套路)
  - [3.1 按 UML 建类](#31-按-uml-建类)
  - [3.2 State 题](#32-state-题)
  - [3.3 Observer 题](#33-observer-题)
  - [3.4 Iterator 题](#34-iterator-题)
  - [3.5 工厂方法加模板方法题](#35-工厂方法加模板方法题)
  - [3.6 Facade 题](#36-facade-题)
  - [3.7 时间不够先拿部分分](#37-时间不够先拿部分分)
- [4 真题套用](#4-真题套用)
  - [4.1 2025 S2 Q5 按 UML 实现 Observer](#41-2025-s2-q5-按-uml-实现-observer)
  - [4.2 2025 S2 Q1 MCQ5 Universe 单例](#42-2025-s2-q1-mcq5-universe-单例)
  - [4.3 2025 S1 Q1 Kitchen 的 State 加 Singleton](#43-2025-s1-q1-kitchen-的-state-加-singleton)
  - [4.4 2025 S1 Q4 MCQ2 Singleton 的构造器](#44-2025-s1-q4-mcq2-singleton-的构造器)
  - [4.5 2024 S2 Q1 用栈实现 BST 前序迭代器](#45-2024-s2-q1-用栈实现-bst-前序迭代器)
  - [4.6 2024 S1 Q1 工厂方法加模板方法](#46-2024-s1-q1-工厂方法加模板方法)
  - [4.7 2024 S1 Q4 MCQ9 UML 关系判断](#47-2024-s1-q4-mcq9-uml-关系判断)
  - [4.8 2023 S2 Q2 Part c ClassifierFactory](#48-2023-s2-q2-part-c-classifierfactory)
  - [4.9 2023 S1 Q1 Part 2 State 加 Observer](#49-2023-s1-q1-part-2-state-加-observer)
  - [4.10 2022 S1 Q1 State 加 Singleton 加异常检查](#410-2022-s1-q1-state-加-singleton-加异常检查)
  - [4.11 2021 S2 Q3 BST 前序迭代器](#411-2021-s2-q3-bst-前序迭代器)
  - [4.12 散题 q12 自动售货机](#412-散题-q12-自动售货机)
  - [4.13 散题 q4 角色状态](#413-散题-q4-角色状态)
  - [4.14 散题 q8 parcel 的 Singleton](#414-散题-q8-parcel-的-singleton)
  - [4.15 散题 q13 Iterator](#415-散题-q13-iterator)
  - [4.16 散题 q15 Facade](#416-散题-q15-facade)
- [5 易错清单](#5-易错清单)

## 1 先认题型

| 题面或代码里出现 | 子题型 | 套路 | 最像的真题 |
|---|---|---|---|
| 一张类图，`implement ... according to the UML`，`exactly match the names and signatures` | 按 UML 建类 | 3.1 | 4.1、4.9 |
| `attach` `detach` `notify` `update` `registerObserver`，`Observer` 接口 | Observer | 3.3 | 4.1、4.9 |
| 状态转换图（圆角框加箭头），`XxxState` 类，`handle(event)`，`state.prepare(this)` | State | 3.2 | 4.3、4.9、4.12 |
| `getInstance()`，`each state has only one unique instance`，`private static ... instance` | Singleton | 2.3、3.2 | 4.3、4.10 |
| `hasNext()` `next()` `createIterator()`，`Stack`，`pre-order` | Iterator | 3.4 | 4.5 |
| `Creator`，`createXxx()`，`factory`，`template method`，父类里有 `abstract` 方法等子类实现 | 工厂方法加模板方法 | 3.5 | 4.6 |
| 一个类的方法只是转调几个小类，`not allowed to directly perform computations` | Facade | 3.6 | 4.16 |
| 选择题问 composition、aggregation、multiplicity | UML 读图 | 2.1 | 4.7 |
| 选择题问 Singleton 的构造器、`static`、`assertSame` | Singleton | 2.3 | 4.2、4.4 |

## 2 零基础速懂

第 2、3 节的代码块为了省地方，把几个类型写在同一块里。考试时每个 `public` 类型单独一个文件，文件名等于类型名，`import` 放在各自文件最上面。

### 2.1 UML 类图怎么读

类图（class diagram）里一个框是一个类，从上到下三格：类名、字段（attribute）、方法（operation）。字段写成 `可见性 名字 : 类型`，方法写成 `可见性 名字(参数名: 类型) : 返回类型`。例：`# id : String`，`+ setFavourites(f: FavouriteList): void`。

| 符号 | 意思 | Java |
|---|---|---|
| `+` / `-` / `#` | 公开 / 私有 / 受保护 | `public` / `private` / `protected` |
| `~` 或不写 | 包内可见 | 不写修饰符 |
| 下划线 | 静态 | `static` |
| 斜体，或 `<<abstract>>` | 抽象类、抽象方法 | `abstract` |
| `<<interface>>` / `<<enum>>` | 接口 / 枚举 | `interface` / `enum` |

六种线：

| 线的样子 | 名字 | 意思 | Java 里怎么写 |
|---|---|---|---|
| 实线加空心三角，三角指向父类 | 继承 generalization | 子类 is a 父类 | `class Dog extends Animal` |
| 虚线加空心三角，三角指向接口 | 实现 realization | 类实现接口 | `class GameMaster implements Observer` |
| 实线，可带普通箭头，箭头指向被持有的类 | 关联 association | A 长期持有 B | A 里有 B 类型的字段 |
| 实线加空心菱形，菱形在整体一端 | 聚合 aggregation | 整体有部分，部分能独立存在 | 整体里有字段，对象可以从外面传入 |
| 实线加实心菱形，菱形在整体一端 | 组合 composition | 整体有部分，整体销毁时部分也销毁 | 整体里有字段，通常整体自己 `new` |
| 虚线加普通箭头 | 依赖 dependency | A 临时用到 B（参数、局部变量） | 不加字段 |

多重性（multiplicity）是线两端的数字，表示这一端有几个对象：`1` 恰好一个，`0..1` 零或一个，`0..*` 或 `*` 零到多个，`1..*` 至少一个。`ParkingLot 1 ◇— 0..* Floor` 读作「一个 ParkingLot 有零到多个 Floor」。对端是 `*` 的，字段写成 `List<...>`。

### 2.2 UML 到 Java 对照表

| UML 写法 | Java 代码 |
|---|---|
| `- name : String` | `private String name;` |
| `# id : String` | `protected String id;` |
| `- items : List<Item>` | `private List<Item> items;`，文件头 `import java.util.List;` |
| 带下划线的 `- instance : Config` | `private static Config instance;` |
| `+ getName() : String` | `public String getName() { return name; }` |
| `+ setFavourites(f: FavouriteList): void` | `public void setFavourites(FavouriteList f) { this.favourites = f; }` |
| 接口框里的 `+ update(event: String, item: Item): void` | `void update(String event, Item item);` |
| 斜体的 `+ handle(Event): void` | `public abstract void handle(Event event);` |
| 斜体类名 `State` | `public abstract class State` |
| `+ State(VendingMachine)`，名字和类名相同 | 构造器 `public State(VendingMachine machine)` |
| A 实线空心三角指向 B / A 虚线空心三角指向 B | `class A extends B` / `class A implements B` |
| A 用菱形或箭头连到 B，B 端是 `1` | A 里 `private B b;` |
| A 用菱形或箭头连到 B，B 端是 `0..*` | A 里 `private List<B> bs;` |
| 虚线箭头从 A 指向 B | 不加字段，B 只出现在参数或方法体里 |

- UML 是「名字 : 类型」，Java 是「类型 名字」，顺序相反。
- 字段格里已经列出的字段，不要因为有连线再加一遍。线只说明这个字段是哪种关系。
- 接口里的方法在实现类里必须写 `public`。

### 2.3 Singleton 单例

单例（Singleton）：一个类在整个程序里只有一个对象。信号：`getInstance()`，`only one instance`。

```java
public class Config {
    private static Config instance;           // 1 静态字段保存唯一的对象
    private Config() { }                      // 2 私有构造器，类外不能 new
    public static Config getInstance() {      // 3 静态方法，每次返回同一个对象
        if (instance == null) { instance = new Config(); }
        return instance;
    }
}
```

- 三样缺一不可：`private static` 字段、`private` 构造器、`public static getInstance()`。
- 不写构造器时 Java 自动补一个 public 无参构造器，类外就能 `new`。所以必须显式写 private 构造器。
- 去掉 `static` 就不成立：没有对象就没法调 `getInstance()`，类外又不能 `new`。
- `assertSame(a, b)` 检查是不是同一个对象（`==`）。`assertEquals(a, b)` 调 `equals`，没重写时也是比 `==`。两次 `getInstance()` 的结果，两个断言都通过。
- 上面是懒汉写法（lazy，第一次用到才创建）。饿汉写法（eager）更短：`private static final Config instance = new Config();`，`getInstance()` 里只有 `return instance;`。题目给了哪种骨架就保留哪种。

### 2.4 State 状态

状态模式（State）：同一个方法在对象处于不同状态时行为不同。每个状态写成一个类；拥有状态的对象叫上下文（context），它把调用转交给当前的状态对象。信号：状态图，`XxxState`。

```java
public interface State {
    void handle(Light light);                 // 每种事件一个方法
}
public class OffState implements State {
    public void handle(Light light) { light.setState(new OnState()); }    // 从 Off 出发的转换
}
public class OnState implements State {
    public void handle(Light light) { light.setState(new OffState()); }
}
public class Light {                          // 上下文
    private State state = new OffState();     // 初始状态
    public void press() { state.handle(this); }   // 委托，不写 if (state == ...)
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
}
```

转换有两种写法，看题目给的接口签名：状态方法里调 `context.setState(下一个状态)`（2025 S1、2022 S1、q12）；或者状态方法返回下一个状态，上下文写 `state = state.xxx(...)`（2023 S1）。

### 2.5 Observer 观察者

观察者模式（Observer）：一个对象（subject，被观察者）发生变化时，通知所有登记过的对象（observer）。信号：`attach`、`detach`、`notify`、`update`，一个 `List<Observer>` 字段。

```java
import java.util.ArrayList;
import java.util.List;

public interface Observer {
    void update(String event);
}
public class Subject {
    private List<Observer> observers = new ArrayList<>();
    public void attach(Observer o) {                          // 登记：不收 null，不收重复
        if (o != null && !observers.contains(o)) { observers.add(o); }
    }
    public void detach(Observer o) { observers.remove(o); }   // 不在列表里时 remove 什么都不做
    public void notifyObservers(String event) {               // 通知：逐个调 update
        for (Observer o : observers) { o.update(event); }
    }
}
public class Logger implements Observer {
    public void update(String event) { System.out.println("got " + event); }
}
```

- subject 只认识 `Observer` 接口，不认识具体的 observer 类。
- 变化真的发生之后才通知。方法名照题目的 UML 写（`notify`、`registerObserver` 等）。

### 2.6 Iterator 迭代器

迭代器模式（Iterator）：不暴露集合内部结构，用 `hasNext()`（还有没有）和 `next()`（取下一个）逐个取元素。

```java
import java.util.ArrayList;
import java.util.List;

public interface Iterator<T> {
    boolean hasNext();
    T next();
}
public class Shelf {
    private List<String> books = new ArrayList<>();
    public void add(String book) { books.add(book); }
    public Iterator<String> createIterator() { return new ShelfIterator(); }

    private class ShelfIterator implements Iterator<String> {   // 内部类能直接读外部类的字段
        private int index = 0;                                  // 记住走到第几个
        public boolean hasNext() { return index < books.size(); }
        public String next() {
            if (!hasNext()) { throw new java.util.NoSuchElementException(); }
            return books.get(index++);                          // 取出当前的，下标加一
        }
    }
}
```

- `hasNext()` 只判断，不移动位置。
- 题目自己定义了 `Iterator` 接口时（2024 S2、q13），不要 `import java.util.Iterator`。2021 S2 用的是 `java.util.Iterator`。

### 2.7 Factory Method 和 Template Method

工厂方法（Factory Method）：父类声明一个创建对象的抽象方法，由子类决定 `new` 哪个具体类。模板方法（Template Method）：父类用一个普通方法固定步骤顺序，其中几步是抽象方法，留给子类实现。两者常一起考。信号：`XxxCreator`，`createXxx()`。

```java
public abstract class Shape { public abstract String name(); }
public class Circle extends Shape { public String name() { return "circle"; } }

public abstract class ShapeCreator {
    public abstract Shape createShape();      // 工厂方法：子类决定 new 什么
    public abstract String nextLabel();       // 留给子类的一步
    public String createLabelled() {          // 模板方法：顺序固定在父类
        String label = nextLabel();
        Shape shape = createShape();
        return label + ":" + shape.name();
    }
}
public class CircleCreator extends ShapeCreator {
    private int count = 0;
    public Shape createShape() { return new Circle(); }
    public String nextLabel() { return "c" + (count++); }
}
```

- 父类的模板方法只调抽象方法，不写 `new Circle()`，不写 `instanceof`。
- 父类没有无参构造器时，子类构造器第一句必须是 `super(...)`。
- 简单工厂也叫 factory：一个 `public static` 方法按参数决定 `new` 谁，返回父类型（2023 S2 的 `ClassifierFactory.createClassifier`，2025 S1 的 `FoodFactory`）。

### 2.8 Facade 外观

外观模式（Facade）：一个类提供几个简单方法，内部去调多个子系统类，调用者不用认识子系统。

```java
public class Engine { public String start() { return "engine on"; } }
public class Lights { public String on() { return "lights on"; } }
public class Car {                            // Facade
    private Engine engine = new Engine();
    private Lights lights = new Lights();
    public String drive() { return engine.start() + ", " + lights.on(); }   // 一个方法包住多个调用
}
```

Facade 自己不做运算，只创建或持有子系统对象，调它们的方法，把结果返回。

### 2.9 Strategy 策略

策略模式（Strategy）：把可以互换的算法抽成接口，每种算法一个类，使用者只持有接口。用来消掉按类型分支的长 `if/else`。2025 S2 Q3 的重构答案用它，详见 [06 SOLID 和重构](06-solid-refactoring.md)。

```java
public interface PaymentStrategy { double fee(double amount); }
public class CardPayment implements PaymentStrategy {
    public double fee(double amount) { return amount * 0.02; }
}
public class Checkout {
    private PaymentStrategy strategy;
    public Checkout(PaymentStrategy strategy) { this.strategy = strategy; }
    public double total(double amount) { return amount + strategy.fee(amount); }   // 不再判断支付方式
}
```

和 State 的结构相同。区别：Strategy 由外部选定后一般不换；State 的状态对象自己触发转换。

## 3 套路

### 3.1 按 UML 建类

适用：题面给类图，要求按图实现，多数文件要自己新建（2025 S2 Q5、2023 S1 Q1、q12）。

1. 列清单。把图里每个框抄成一行：是 class、abstract class、interface 还是 enum，名字，字段，方法。用 README 的 `Submission` 文件列表和测试文件里出现的类名核对有没有漏。
2. 逐个建文件，顺序是：接口和枚举，只有字段的数据类，实现接口的类，最后是持有别人的类。IntelliJ 里右键题目目录，New，Java Class，名字照抄 UML，大小写一致。已给的文件有 `package` 行就照抄到新文件第一行，没有就不加。
3. 字段逐字照抄：可见性、名字、类型，按 2.2 的表翻译。
4. 方法签名逐字照抄：名字、参数个数、参数类型和顺序、返回类型。
5. 翻译每条线：三角是 `extends` 或 `implements`；菱形和实线箭头对应一个字段，字段格里已有就不再加；虚线箭头不加字段。
6. 补构造器、getter、setter，只补方法体里用得到的。
7. 按 README 的文字规则逐条写方法体（no-op、去重、何时通知）。
8. 跑提供的测试检查命名。这类测试用反射（reflection）查类名、字段名、方法签名，哪条红了就对照 UML 改哪条。行为要另外写 `Main` 跑一遍看输出。

题面写 `do not introduce any extra fields or methods, except constructors, getters, and setters` 时：

| 可以加 | 不可以 |
|---|---|
| 构造器，可以多个 | UML 没有的字段，包括 `static` 的 |
| `getX()`、`setX()` | UML 没有的辅助方法，`private` 的也不行，逻辑要内联 |
| `@Override`、`import`、局部变量 | 改名字、大小写、参数类型或顺序、返回类型 |
| 在声明处给字段赋初值 | 把 `List<Item>` 写成 `ArrayList<Item>` 或 `Item[]` |

集合字段在声明处初始化最稳：`private List<Item> items = new ArrayList<>();`，这样哪个构造器都不会留下 `null`。

### 3.2 State 题

适用：题面给状态图，或用文字规定每个状态下某个操作怎么做。

1. 把状态图抄成转换表，一行一条箭头：当前状态、事件、下一个状态。图上没有的组合是留在原状态。`Any State` 出发的箭头对每个状态都生效。
2. 看题目给的状态接口或抽象类：事件怎么传进来（一个 `handle(event)`，还是每种事件一个方法）；转换怎么生效（调 `setState`，还是返回下一个状态）。
3. 一个状态一个类，一个文件，类名照抄题面和测试。
4. 每个状态类只写从本状态出发的箭头：`switch (event)` 命中就转换，`default` 什么都不做。
5. 题面说每个状态只有一个实例，或测试里有 `XxxState.getInstance()`：每个状态类加 Singleton 三样（2.3），转换时写 `XxxState.getInstance()`，不写 `new`。
6. 上下文类：一个 `state` 字段带初始状态，事件方法里只写委托。题面要求先校正状态或先检查参数时，检查写在委托前面。
7. 拿测试里的事件序列对着转换表手动走一遍。

模板（State 加 Singleton，用 `setState` 转换）：

```java
public enum Key { UP, DOWN, RESET }

public interface State {
    void handle(Player player, Key key);
}
public class StandState implements State {
    private static StandState instance;                  // 懒汉写法
    private StandState() { }
    public static StandState getInstance() {
        if (instance == null) { instance = new StandState(); }
        return instance;
    }
    @Override
    public void handle(Player player, Key key) {
        switch (key) {                                   // 只写从 Stand 出发的箭头
            case DOWN: player.setState(SquatState.getInstance()); break;
            default: break;                              // 其他事件留在原状态
        }
    }
}
public class SquatState implements State {
    private static final SquatState instance = new SquatState();   // 饿汉写法，更短
    private SquatState() { }
    public static SquatState getInstance() { return instance; }
    @Override
    public void handle(Player player, Key key) {
        switch (key) {
            case UP:
            case RESET: player.setState(StandState.getInstance()); break;   // 两个事件去同一个状态
            default: break;
        }
    }
}
public class Player {                                    // 上下文
    private State state = StandState.getInstance();
    public void press(Key key) { state.handle(this, key); }
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
}
```

变体：

| 情况 | 改法 |
|---|---|
| 状态类要记住上下文，不是单例（q12） | 构造器收上下文：`machine.setState(new Idle(machine));` |
| 转换靠返回值（2023 S1） | 转换写 `return new HelpState();`，留在原状态写 `return this;` |
| 某状态下操作非法 | `throw new RuntimeException("...");` 或题目指定的异常 |
| 状态由数值决定（2025 S1） | 上下文先按数值校正 `state`，再委托 |

### 3.3 Observer 题

1. 找出三样：observer 接口（方法名和参数），subject（持有 `List<Observer>` 的类），具体 observer（`implements` 接口的类）。
2. subject 里的 `List<Observer>` 字段在声明处 `new ArrayList<>()`。
3. 登记方法（`attach`、`registerObserver`）：`null` 不加，已在列表里不加。
4. 注销方法（`detach`）：`observers.remove(o);`。
5. 通知方法：`for` 循环调每个 observer 的更新方法。UML 里没有单独的通知方法时（2023 S1），循环直接写在触发事件的方法里。
6. 在每个「修改成功」的位置之后调通知。失败、重复、no-op 的分支在通知之前 `return`。
7. 具体 observer 的更新方法：按题面打印，或记到自己的列表里。
8. 要写 `Main` 时：题面一句话对应一行代码，顺序不变，跑一遍看输出。

骨架见 2.5，完整版见 4.1。不能加辅助方法时，查找逻辑直接内联：

```java
// 片段：按 id 去重后添加，成功才通知
for (Item old : items) {
    if (old.getId().equals(item.getId())) { return; }   // 已有同 id：no-op
}
items.add(item);
notify("added", item);
```

### 3.4 Iterator 题

1. 看 `Iterator` 接口是题目自定义的，还是 `java.util.Iterator`。
2. 看迭代器类已有哪些字段（下标，`Stack`），题目让不让加字段。
3. 线性集合用下标（2.6）：`hasNext()` 是 `index < size`，`next()` 是 `get(index++)`。
4. 树的前序遍历（pre-order：根、左、右）用栈（stack，后进先出）：构造器里根不为空就 `push`；`hasNext()` 是 `!stack.isEmpty()`；`next()` 先 `pop` 一个节点，`push` 右孩子，再 `push` 左孩子，返回弹出节点的值。左孩子后进，所以先出。
5. 取完之后再调 `next()`：题面要求抛 `NoSuchElementException` 就抛，没规定就保留骨架的默认返回值。
6. 拿一棵三层的树手动走一遍栈，核对输出顺序。

模板（用栈做前序）：

```java
import java.util.Stack;

public class Node {
    int value;
    Node left, right;
    Node(int value) { this.value = value; }
}
public class PreOrderIterator {
    private final Stack<Node> stack = new Stack<>();
    public PreOrderIterator(Node root) {
        if (root != null) { stack.push(root); }
    }
    public boolean hasNext() { return !stack.isEmpty(); }
    public int next() {
        if (stack.isEmpty()) { throw new java.util.NoSuchElementException(); }
        Node node = stack.pop();
        if (node.right != null) { stack.push(node.right); }   // 先压右
        if (node.left != null) { stack.push(node.left); }     // 后压左，左先弹出
        return node.value;
    }
}
```

| 换一种遍历 | 改法 |
|---|---|
| 反前序（根、右、左） | 先压左，再压右 |
| 多叉树前序 | 孩子列表从最后一个到第一个倒着压 |
| 层序（level order） | `Stack` 换成 `java.util.LinkedList` 当队列，`add` 进 `poll` 出，先放左再放右 |
| 中序，或题目没限定做法 | 构造器里递归把值按顺序收进一个 `List`，之后用下标迭代。要求用栈或测试检查栈大小时不能这样做 |

### 3.5 工厂方法加模板方法题

1. 分清两棵继承树：产品（`Animal`、`Dog`）和创建者（`AnimalCreator`、`DogCreator`）。
2. 具体创建者的工厂方法只有一句：`return new Dog(...)`，实参来自方法参数和父类保存的配置字段。
3. 具体创建者的构造器：第一句 `super(...)`。子类默认值和父类不同时，在 `super(...)` 的实参里用三元表达式把 `null` 换掉。
4. 父类构造器：先校验，不合法就 `throw new IllegalArgumentException(...)`；再赋值，`null` 表示用默认值，跳过赋值。
5. 父类模板方法：按注释写的步骤一步一行，只调抽象方法。
6. 批量方法：`for` 循环调 n 次模板方法，结果放进 `ArrayList` 返回。

骨架见 2.7，完整版见 4.6。循环取名的片段：

```java
// 片段：按顺序循环取名
String name = NAMES.get(nextNameIndex);
nextNameIndex = (nextNameIndex + 1) % NAMES.size();   // 到末尾回到 0
return name;
```

### 3.6 Facade 题

1. 找出子系统类和它们的方法，例如 `new Addition(a, b).evaluate()`。
2. Facade 的每个方法只有一句：`new` 对应的子系统对象，调它的方法，`return` 结果。
3. Facade 里不出现 `+ - * /` 这类运算。
4. 子系统里留空的方法按测试补全，注意要抛的异常。

骨架见 2.8，完整版见 4.16。

### 3.7 时间不够先拿部分分

1. 先让所有文件能编译：类、接口、字段、方法签名全部建好，方法体先写 `return null;`、`return false;` 或留空。编译不过整题零分。
2. 跑提供的命名测试，结构分先到手。
3. 按从易到难补方法体：getter 和 setter，`getInstance()`，`attach`、`detach`、通知循环，`hasNext()`。
4. State 题先写测试里出现的那几条转换，再补其余箭头。
5. 要写 `Main` 的题，方法体没写完也先把对象创建和调用顺序按题面写全。

## 4 真题套用

### 4.1 2025 S2 Q5 按 UML 实现 Observer

**题意**：按 [UML_ob.png](../exam-practice-questions/past-exams/2025-2/src/Q5/UML_ob.png) 新建五个类型。`User` 用实心菱形组合一个 `FavouriteList`。`FavouriteList` 用空心菱形聚合多个 `Item`，并用实线箭头关联多个 `Observer`。`NotificationService` 用虚线空心三角实现 `Observer`。类名、字段名、方法签名和 UML 逐字一致。除构造器、getter、setter 外不能多加字段或方法。原题 [Q5README.md](../exam-practice-questions/past-exams/2025-2/src/Q5/Q5README.md)。`Q5Test` 只查名字，行为以 README 为准。

| 类 | 字段 | 方法 |
|---|---|---|
| `User` | `# id : String`，`- name : String`，`- favourites : FavouriteList` | `getFavourites()`，`setFavourites(f)`，`getName()` |
| `FavouriteList` | `- items : List<Item>`，`- observers : List<Observer>` | `addItem`，`removeItem`，`attach`，`detach`，`notify(event, item)` |
| `Item` | `# id : String`，`- name : String` | UML 没写，getter 可以加 |
| `<<interface>> Observer` | 无 | `update(event: String, item: Item): void` |
| `NotificationService` | `- channel : String` | 实现 `update` |

**套路对应**：3.1 的第 1 到 6 步就是上面这张表。3.3 在这题里是：`attach` 不收 `null`、不收重复；`detach` 用 `remove`；`addItem` 按 `id` 去重，成功才 `notify("added", item)`；`removeItem` 要 `id` 和 `name` 都相同，成功才 `notify("removed", item)`；失败的分支在 `notify` 之前 `return`。`Main` 按 Task C 的句子从上到下写，一句一行。

**关键代码**（每个 `public` 类型一个文件，没有 `package`。通过原题 `Q5Test` 的 22 个测试）：

```java
public interface Observer {
    void update(String event, Item item);
}

public class Item {
    protected String id;
    private String name;
    public Item(String id, String name) { this.id = id; this.name = name; }
    public String getId() { return id; }
    public String getName() { return name; }
}

public class User {
    protected String id;
    private String name;
    private FavouriteList favourites;
    public User(String id, String name) { this(id, name, new FavouriteList()); }
    public User(String id, String name, FavouriteList favourites) {
        this.id = id; this.name = name; this.favourites = favourites;
    }
    public FavouriteList getFavourites() { return favourites; }
    public void setFavourites(FavouriteList f) { this.favourites = f; }
    public String getName() { return name; }
}

public class NotificationService implements Observer {
    private String channel;
    public NotificationService(String channel) { this.channel = channel; }
    @Override
    public void update(String event, Item item) {
        System.out.println("[channel " + channel + "] event: " + event
                + ", item #" + item.getId() + " (" + item.getName() + ")");
    }
}

import java.util.ArrayList;
import java.util.List;

public class FavouriteList {
    private List<Item> items = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();
    public FavouriteList() { }
    public FavouriteList(List<Item> items, List<Observer> observers) { // 注释骨架里的构造器，留着
        this.items = items; this.observers = observers;
    }
    public void addItem(Item item) {
        if (item == null) return;
        for (Item old : items) if (old.getId().equals(item.getId())) return; // 同 id：no-op
        items.add(item);
        notify("added", item);
    }
    public void removeItem(Item item) {
        if (item == null) return;
        for (int i = 0; i < items.size(); i++) {
            Item old = items.get(i);
            if (old.getId().equals(item.getId()) && old.getName().equals(item.getName())) {
                items.remove(i);
                notify("removed", item); // 参数用传进来的 item
                return;
            }
        }
    }
    public void attach(Observer o) {
        if (o != null && !observers.contains(o)) observers.add(o);
    }
    public void detach(Observer o) { observers.remove(o); }
    public void notify(String event, Item item) {
        for (Observer o : observers) o.update(event, item);
    }
}

public class Main {
    public static void main(String[] args) {
        User user1 = new User("1", "User 1");
        User user2 = new User("2", "User 2");
        Observer abc = new NotificationService("ABC");
        Observer def = new NotificationService("DEF");
        user1.getFavourites().attach(abc);
        user2.getFavourites().attach(def);
        Item item1 = new Item("I1", "Item 1");
        Item item2 = new Item("I2", "Item 2A");
        Item item3 = new Item("I2", "Item 2B");
        user1.getFavourites().addItem(item1);
        user2.getFavourites().addItem(item1);
        user2.getFavourites().addItem(item2);
        user2.getFavourites().addItem(item3); // 同 id I2，no-op
        user2.getFavourites().detach(def);
        user1.getFavourites().removeItem(item1);
        user2.getFavourites().removeItem(item1);
    }
}
```

**坑**：

- 注释掉的骨架把方法写成包级可见，还多了一个 UML 里没有的 `getItem`。按 UML 改成 `private` 字段、`public` 方法，删掉 `getItem`。
- `id` 是 `#`，写成 `protected`。`name`、`channel`、两个 `List` 是 `-`，写成 `private`。`Q5Test` 不查 `getName()`，UML 上有，要写。
- `Main` 跑出来应是四行：`[channel ABC] event: added, item #I1 (Item 1)`，然后 DEF 的 added I1，DEF 的 added I2，最后 ABC 的 removed I1。没有 Item 2B，也没有 DEF 的 removed。
- 两个 `List` 在声明处 `new ArrayList<>()`。用户名 README 没规定，只要 id 是 `"1"` 和 `"2"`。

### 4.2 2025 S2 Q1 MCQ5 Universe 单例

**题意**：看 `Universe` 的懒汉单例，选哪一句是对的。原题在 [Q1README.md](../exam-practice-questions/past-exams/2025-2/src/Q1/Q1README.md) 指向的 MCQ 第 5 题。类是 `private static Universe INSTANCE`，`private Universe() { }`，`getInstance()` 在 `INSTANCE == null` 时 `new` 一次再返回。

**套路对应**：2.3 的三样和 `assertSame`、`assertEquals` 那一条。

**推导**：非官方答案 c。

- a 说 `assertSame(getInstance(), getInstance())` 会失败。两次返回同一个对象，`assertSame` 通过。a 错。
- b 说 `assertEquals` 会失败。这个类没重写 `equals`，`assertEquals` 也是比 `==`，通过。b 错。
- c 说类外不能直接用构造器 `new`。构造器是 `private`。c 对。
- d 说可以去掉 `INSTANCE` 和 `getInstance()` 的 `static`。去掉之后，没有对象就调不到 `getInstance()`，类外又不能 `new`。d 错。

**坑**：a 和 b 都是「断言会失败」。题目问的是哪句正确，不要因为两个断言都通过就把 a、b 选上。

### 4.3 2025 S1 Q1 Kitchen 的 State 加 Singleton

**题意**：食物有 `RawState`、`ReadyState`、`BurntState` 三个单例状态。`elapsedPrepTime` 是准的，`state` 可能是错的。`prepare` 先按时间把状态改对，再交给当前状态。厨房三个方法按配方清点食物。原题 [Q1README.md](../exam-practice-questions/past-exams/2025-1/src/Q1/Q1README.md)。只改注释标出的区域，不改已有字段和方法签名。三个状态类的 `getState()` 已经写好。

准备时间：`WAGYU`、`EGG` 是 1，`SALMON` 是 2，`UDON`、`CUP_NOODLES` 是 3，其余是 0。时间小于所需是 Raw；等于所需或所需加 1 是 Ready；再多是 Burnt。`UDON` 从 0 开始，`prepare` 第 1、2 次仍是 Raw，第 3 次 Ready，第 4 次仍是 Ready，第 5 次 Burnt，再调就抛 `RuntimeException`。

**套路对应**：3.2 的「状态由数值决定」那一行。`Food.prepare` 先 `checkState()`，再 `state.prepare(this)`。状态类里只改时间和 `setState`，转换写 `XxxState.getState()`。`readyToCook` 数 Raw 和 Ready，`readyToServe` 只数 Ready，`ingredientsRequired` 把所有订单的数量加总，减去没烧焦的库存，只返回还缺的。

**关键代码**（通过原题 `Q1Test` 的 6 个测试。另外核对过：`CUP_NOODLES` 时间 2 却标着 Ready 时，`checkState` 改回 Raw；`UDON` 时间已经是 4 再 `prepare` 会变成 Burnt 且时间为 5；时间已经是 5 再 `prepare` 抛异常；准备时间 0、已用时间 0 校正后是 Ready）：

```java
public void prepare() {
    checkState();
    state.prepare(this);
}

public void checkState() {
    int need = Ingredient.getPrepTime(ingredient);
    if (elapsedPrepTime < need) state = RawState.getState();
    else if (elapsedPrepTime <= need + 1) state = ReadyState.getState();
    else state = BurntState.getState();
}

// RawState.prepare。ReadyState 把条件改成 elapsed >= need + 2，目标改成 BurntState
public void prepare(Food food) {
    food.elapsedPrepTime++;
    if (food.elapsedPrepTime >= Ingredient.getPrepTime(food.ingredient)) {
        food.setState(ReadyState.getState());
    }
}

// BurntState.prepare
public void prepare(Food food) {
    throw new RuntimeException("burnt food cannot be prepared");
}

public static boolean readyToCook(Recipe recipe, Set<Food> ava) {
    Map<Ingredient, Integer> have = new HashMap<>();
    for (Food food : ava) {
        if (!(food.state instanceof BurntState)) { // Raw 和 Ready 都算
            have.put(food.getIngredient(), have.getOrDefault(food.getIngredient(), 0) + 1);
        }
    }
    for (Map.Entry<Ingredient, Integer> need : recipe.getIngredients().entrySet()) {
        if (have.getOrDefault(need.getKey(), 0) < need.getValue()) return false;
    }
    return true;
}

public static boolean readyToServe(Recipe recipe, Set<Food> ava) {
    Map<Ingredient, Integer> have = new HashMap<>();
    for (Food food : ava) {
        if (food.state instanceof ReadyState) {
            have.put(food.getIngredient(), have.getOrDefault(food.getIngredient(), 0) + 1);
        }
    }
    for (Map.Entry<Ingredient, Integer> need : recipe.getIngredients().entrySet()) {
        if (have.getOrDefault(need.getKey(), 0) < need.getValue()) return false;
    }
    return true;
}

public Map<Ingredient, Integer> ingredientsRequired() {
    Map<Ingredient, Integer> need = new HashMap<>();
    for (Recipe order : orders) {
        for (Map.Entry<Ingredient, Integer> e : order.getIngredients().entrySet()) {
            need.put(e.getKey(), need.getOrDefault(e.getKey(), 0) + e.getValue());
        }
    }
    Map<Ingredient, Integer> have = new HashMap<>();
    for (Map.Entry<Food, Integer> e : availableFood.entrySet()) {
        if (!(e.getKey().state instanceof BurntState)) {
            Ingredient key = e.getKey().getIngredient();
            have.put(key, have.getOrDefault(key, 0) + e.getValue()); // 地图的值是数量
        }
    }
    Map<Ingredient, Integer> missing = new HashMap<>();
    for (Map.Entry<Ingredient, Integer> e : need.entrySet()) {
        int lack = e.getValue() - have.getOrDefault(e.getKey(), 0);
        if (lack > 0) missing.put(e.getKey(), lack);
    }
    return missing;
}
```

**坑**：

- `Set<Food>` 里一个对象算 1 份。`availableFood` 是 `Map<Food, Integer>`，要加的是那个整数。烧焦的看 `instanceof BurntState`，这三个方法里不要调用 `checkState`，否则会改掉传入的食物。
- `food.elapsedPrepTime`、`food.state`、`food.ingredient` 在骨架里没有写成 `private`，同目录的类可以直接读写。不要改字段的修饰符。
- `getState()` 是饿汉单例，已经写好。转换用 `getState()`。题目只允许改 TODO 区域，这里不加构造器、不加字段。
- `Kitchen.java` 顶部已有 `import java.util.*`。不要在这三个方法外面再加辅助方法，循环就写在 TODO 里面。

### 4.4 2025 S1 Q4 MCQ2 Singleton 的构造器

**题意**：公开类 `Authenticator` 要做成单例，该声明哪种构造器。原题 [Q4README.md](../exam-practice-questions/past-exams/2025-1/src/Q4/Q4README.md) 的 MCQ2。选项：a 一个构造器都不写；b 私有构造器；c 受保护构造器；d 一个 public 构造器再加一个不写修饰符的构造器。

**套路对应**：2.3 的第二点。不写构造器时，编译器补一个 public 无参构造器。

**推导**：非官方答案 b。

- a 会得到 public 构造器，类外可以 `new`。
- b 类外不能 `new`，只能走 `getInstance()`。
- c 同一个包和子类仍然可以 `new`。
- d 两种构造器类外都能用。

**坑**：这题问的是构造器，不是要不要 `static`。`private` 构造器单独就能回答这题。

### 4.5 2024 S2 Q1 用栈实现 BST 前序迭代器

**题意**：补全 `BST` 的内部类 `BSTIterator`。前序是根、左、右。构造时如果根不为 `null` 且 `value` 不为 `null`，把根压栈。`hasNext` 看栈是否空。`next` 弹出栈顶，先压右孩子再压左孩子，返回弹出节点的 `value`。迭代过程中新插入的节点：插在已经弹出的节点下面的，这次迭代看不到；插在还留在栈里的节点下面的，弹出那个节点时会一起压进去。原题 [Q1README.md](../exam-practice-questions/past-exams/2024-2/src/Q1/Q1README.md)。接口是题目自己的 `Iterator`，不要 `import java.util.Iterator`。

**套路对应**：3.4 的第 4 步。示例树 `insertAll(5, 7, 3, 8, 6, 2, 4)` 的前序是 `5, 3, 2, 4, 7, 6, 8`。每次 `next` 之前栈的大小依次是 `1, 2, 3, 2, 1, 2, 1`。

**关键代码**（只填三个 TODO。通过原题 `Q1Test` 的 2 个测试。另测过：空树 `hasNext` 为 false，`next` 返回 `null`；先走出 `5, 3, 2` 再 `insert(1)`，后面只剩 `4`；弹出根之后插入 `1, 4, 6, 9`，后面是 `3, 1, 4, 7, 6, 9`）：

```java
public BSTIterator(BST<T> root) {
    if (root != null && root.value != null) stack.push(root);
}

@Override
public boolean hasNext() {
    return !stack.isEmpty();
}

@Override
public T next() {
    if (stack.isEmpty()) return null;          // 题面没要求抛异常，骨架默认是 null
    BST<T> node = stack.pop();
    if (node.right != null) stack.push(node.right); // 先右
    if (node.left != null) stack.push(node.left);   // 后左，左先弹出
    return node.value;
}
```

**坑**：

- 空的 `BST()` 里 `value` 是 `null`。只判断 `root != null` 会把这个空节点压进去，`next` 会返回 `null` 并把迭代次数弄乱。
- 先压左再压右，顺序会变成根、右、左，`size()` 的测试也会对不上。
- 耗尽后再调 `next`，提供的测试不覆盖。题面没写抛异常，所以上面返回 `null`。不确定阅卷会不会改成要求 `NoSuchElementException`。两种写法都能过现有的两个测试。
- 可以在 `BST.java` 里加辅助方法，不能加字段。栈这个字段骨架里已经有了。

### 4.6 2024 S1 Q1 工厂方法加模板方法

**题意**：`AnimalCreator` 是创建者父类。`DogCreator`、`PenguinCreator` 实现工厂方法 `createAnimal` 和取名 `getNextName`。父类的 `createAnimalTemplate` 固定三步，`createBatch` 调它 n 次。年龄或活跃度为负数就抛 `IllegalArgumentException`。`null` 表示用默认值：Dog 用父类字段初值 0；Penguin 用年龄 1、活跃度 2。原题 [Q1README.md](../exam-practice-questions/past-exams/2024-1/src/Q1/Q1README.md)。

**套路对应**：3.5。子类构造器第一句 `super(...)`。Penguin 的默认值写在 `super` 的实参里，因为父类看到 `null` 会保留 0。模板方法按注释的 1、2、3 步，不写 `new Dog`。取名用 3.5 的循环片段，名单用子类自己的 `NAMES`。

**关键代码**（通过原题 `FactoryTest` 的 6 个测试。另测过：`new PenguinCreator(null, null)` 造出年龄 1、活跃度 2；只把其中一个传 `null` 时另一个用传入值；第六个名字回到 `Ace`；第二个 `PenguinCreator` 接着往下取名；`new DogCreator(null, null)` 是 0 和 0，并且自己的名字从 `Rex` 重新开始；`DogCreator(-1, 0)`、`PenguinCreator(1, -2)`、`DogCreator(null, -1)` 都抛 `IllegalArgumentException`）：

```java
public AnimalCreator(Integer age, Integer level) {
    if ((age != null && age < 0) || (level != null && level < 0)) {
        throw new IllegalArgumentException("age and level must be >= 0");
    }
    if (age != null) this.ageConfig = age;       // null 保留字段初值 0
    if (level != null) this.levelConfig = level;
}

public Animal createAnimalTemplate() {
    String id = UUID.randomUUID().toString();
    String name = getNextName();
    return createAnimal(id, name);
}

public List<Animal> createBatch(int number) {
    List<Animal> animals = new ArrayList<>();
    for (int i = 0; i < number; i++) animals.add(createAnimalTemplate());
    return animals;
}

public DogCreator(Integer age, Integer level) {
    super(age, level);
}
@Override
public Animal createAnimal(String id, String name) {
    return new Dog(id, name, ageConfig, levelConfig);
}
public String getNextName() {                    // PenguinCreator 同样写，用它自己的 NAMES
    String name = NAMES.get(nextNameIndex);
    nextNameIndex = (nextNameIndex + 1) % NAMES.size();
    return name;
}

public PenguinCreator(Integer age, Integer level) {
    super(age == null ? 1 : age, level == null ? 2 : level);
}
@Override
public Animal createAnimal(String id, String name) {
    return new Penguin(id, name, ageConfig, levelConfig);
}
```

**坑**：

- `FactoryTest` 只测了 Dog，没测负数，也没测 Penguin 的默认值。README 这两条仍然要写。
- Penguin 的 `nextNameIndex` 在骨架里是 `static`，所有企鹅创建者共用一个下标。Dog 的下标是每个对象自己的。不要改这两个字段的修饰符。
- 父类模板方法里出现 `new Dog` 或 `instanceof`，就不是模板方法了。
- `ageConfig`、`levelConfig` 是包级可见的 `int`，子类直接读。构造器参数是 `Integer`，才能表示 `null`。

### 4.7 2024 S1 Q4 MCQ9 UML 关系判断

**题意**：看 [UML-MCQ9.png](../exam-practice-questions/past-exams/2024-1/src/Q4/UML-MCQ9.png)，选出错误的那一句。原题 [Q4README.md](../exam-practice-questions/past-exams/2024-1/src/Q4/Q4README.md) 的 MCQ9。图上：`ParkingLot` 空心菱形连 `Floor`，停车场一端是 `1`，楼层一端是 `0..*`。`Floor` 实心菱形连 `Spot`，楼层一端是 `1`，车位一端是 `1..*`。`Spot` 空心菱形连 `Vehicle`，两端都是 `1`。`Car` 和 `Motorcycle` 用空心三角指向 `Vehicle`。`Vehicle` 有 `- name`、`- color`，以及 `getName()`、`getColor()`。

**套路对应**：2.1 的六种线和多重性。

**推导**：非官方答案 B。

- A 说 Floor 和 Spot 是组合，Floor 销毁时 Spot 也销毁。实心菱形在 Floor 一端。A 对。
- B 说 Car、Motorcycle 是 Vehicle，并且这种关系是聚合。空心三角是继承。B 错，这就是要选的。
- C 说一个 ParkingLot 有零到多个 Floor。多重性是 `1` 对 `0..*`。C 对。
- D 说 Vehicle 是父类，Car 和 Motorcycle 继承 `getName()`、`getColor()`，并且 `name`、`color` 是 private。方法是 `+`，字段是 `-`。D 对。

**坑**：题目问的是 INCORRECT，选错误的那句。D 里「private 字段」这句话本身是对的，不要因为子类访问不到 private 字段就把 D 判错。

### 4.8 2023 S2 Q2 Part c ClassifierFactory

**题意**：`ClassifierFactory.createClassifier(Set<Spider> possibilities)` 返回一棵能区分这些蜘蛛的决策树，类型是父类 `Node`。输入非空，每只蜘蛛的 `speciesName` 不同。既要分类正确，又要让 `maximumDecisions` 尽量小，矮树给部分分。原题 [readme_Q2.md](../exam-practice-questions/past-exams/2023-2/src/Q2/readme_Q2.md)。怎么选属性、怎么把树建矮，见 [04 树](04-trees.md)。

**套路对应**：2.7 里的简单工厂。这题的工厂就是一个 `public static` 方法，按输入 `new` 出节点，返回类型是父类 `Node`。

**关键代码**（片段，只到工厂这一层）：

```java
public static Node createClassifier(Set<Spider> possibilities) {
    // 在这里 new DecisionNode 或 TerminalNode，返回 Node
    // 建树的递归放在这个方法里，或放到 Node 的子类里
}
```

**坑**：返回类型保持 `Node`。不要按蜘蛛名字写一串 `if`。Part a 的 `classify` 和 Part b 的 `toString` 也归 [04 树](04-trees.md)。

### 4.9 2023 S1 Q1 Part 2 State 加 Observer

**题意**：Part 2a 用状态模式写参与者解谜。初始是 `InProgressState`：答案对了才解开这题，全部解开就变成 `EscapedState`。`askForHelp` 进入 `HelpState`：不管输入对错都解开这题，还有题就回到 `InProgressState`，没有就进入 `EscapedState`。已经解开的题再交一次，抛 `RiddleException`。`EscapedState` 里再调用 `solveRiddle` 也抛。Part 2b 新建 `GameMaster`。求助时通知 `updateHelp`，成功逃脱时通知 `updateEscape`。图在 [UML.png](../exam-practice-questions/past-exams/2023-1/Q1/UML.png)。原题 [Q1README.md](../exam-practice-questions/past-exams/2023-1/Q1/Q1README.md)。Part 1 的分支覆盖归 [02 测试](02-testing.md)。

**套路对应**：3.2 的「转换靠返回值」：状态方法 `return` 下一个状态，留在原地写 `return this`，`Participant` 写 `state = state.solveRiddle(...)`。3.3 的第 5 步：UML 没有单独的 `notify`，循环写在 `askForHelp` 和 `solveRiddle` 里。`# observers`、`# helpedParticipants`、`# escapedParticipants` 写成 `protected`。

**关键代码**（每个类第一行都是 `package Q1;`。`GameMaster` 要新建。通过 `ParticipantTest` 和 `ObserverTest` 共 8 个测试）：

```java
package Q1;

public class InProgressState implements ParticipantState {
    @Override
    public ParticipantState solveRiddle(String input, Riddle[] riddleList, int whichRiddle) throws RiddleException {
        Riddle riddle = riddleList[whichRiddle];
        if (riddle.isSolved()) throw new RiddleException("riddle already solved");
        riddle.checkAnswer(input);                 // 答错不会 setSolved
        for (Riddle r : riddleList) if (!r.isSolved()) return this;
        return new EscapedState();
    }
}

public class HelpState implements ParticipantState {
    @Override
    public ParticipantState solveRiddle(String input, Riddle[] riddleList, int whichRiddle) throws RiddleException {
        Riddle riddle = riddleList[whichRiddle];
        if (riddle.isSolved()) throw new RiddleException("riddle already solved");
        riddle.setSolved();                        // 不看 input
        for (Riddle r : riddleList) if (!r.isSolved()) return new InProgressState();
        return new EscapedState();
    }
}

public class EscapedState implements ParticipantState {
    @Override
    public ParticipantState solveRiddle(String input, Riddle[] riddleList, int whichRiddle) throws RiddleException {
        throw new RiddleException("participant has already escaped");
    }
}

package Q1;

import java.util.ArrayList;
import java.util.List;

public class GameMaster implements Observer {
    protected List<Participant> helpedParticipants = new ArrayList<>();
    protected List<Participant> escapedParticipants = new ArrayList<>();
    public void updateHelp(Participant p) {
        if (!helpedParticipants.contains(p)) helpedParticipants.add(p);
    }
    public void updateEscape(Participant p) {
        if (!escapedParticipants.contains(p)) escapedParticipants.add(p);
    }
    public boolean hasAskedForHelp(Participant p) { return helpedParticipants.contains(p); }
    public boolean hasEscaped(Participant p) { return escapedParticipants.contains(p); }
}
```

`Participant` 里补这些。`askForHelp` 里那行 `this.state = new HelpState();` 只取消注释，不改内容。`solveRiddle` 里已有的 `this.state = this.state.solveRiddle(...)` 留着，通知写在它后面。

```java
protected java.util.List<Observer> observers;

public void registerObserver(Observer o) {
    if (o != null && !observers.contains(o)) observers.add(o);
}

public Participant(Riddle[] riddleList) {
    this.riddleList = riddleList;
    this.state = new InProgressState();
    this.observers = new java.util.ArrayList<>();
}

public void askForHelp() {
    this.state = new HelpState();
    for (Observer o : observers) o.updateHelp(this);
}

public void solveRiddle(String input, int whichRiddle) throws RiddleException {
    this.state = this.state.solveRiddle(input, riddleList, whichRiddle);
    if (this.state instanceof EscapedState) {
        for (Observer o : observers) o.updateEscape(this);
    }
}
```

**坑**：

- 三个状态类的空文件已经有 `package Q1;`，补上 `implements ParticipantState`。`GameMaster.java` 第一行自己写 `package Q1;`。
- 本地跑 `ObserverTest` 前，取消注释 `observer = new GameMaster();` 和 `participant.registerObserver(observer);`。提交清单里没有这个测试文件。
- `solveRiddle` 抛异常时，赋值还没发生，后面的逃脱通知也不会执行。
- `whichRiddle` 是 `riddleList` 的下标。参与者可以先解任意一题，不要假定必须从 0 按顺序解。

### 4.10 2022 S1 Q1 State 加 Singleton 加异常检查

**题意**：五个状态各一个单例，按 [Graph.pdf](../exam-practice-questions/past-exams/2022-1/q1/Graph.pdf) 转换。任何状态遇到 `Key.RESET` 回到 `StandState`。`State.check` 里，key 为 `null` 抛 `NullKeyEventException`，character 为 `null` 抛 `NullCharacterException`。XML 的 `read`、`write` 归 [05 持久化](05-persistence.md)。原题 [readme.md](../exam-practice-questions/past-exams/2022-1/q1/readme.md)。

**套路对应**：3.2 的单例模板。`handle` 骨架会先 `check` 再 `handleInput`，转换只写在 `handleInput`。

| 当前状态 | 事件 | 下一个状态 |
|---|---|---|
| Stand | DOWN / RIGHT | LieProne / Run |
| LieProne | UP / RIGHT / S | Stand / Crawl / Shoot |
| Run | LEFT | Stand |
| Crawl | LEFT | LieProne |
| Shoot | L | LieProne |
| 任何状态 | RESET | Stand |

**关键代码**（片段。`StateTest` 的 3 个按键序列和 `ExceptionTest` 的 2 个通过）：

```java
default void check(Character character, Key event) {
    if (event == null) throw new exceptions.NullKeyEventException();
    if (character == null) throw new exceptions.NullCharacterException();
}
// 每个状态类：private 构造器，private static final 实例，getInstance() 返回它
// handleInput 里 switch，未列出的 case 什么都不做，RESET 去 StandState.getInstance()
```

**坑**：`S` 是从 LieProne 到 Shoot，`L` 是从 Shoot 回到 LieProne。图上没有的键留在原状态。状态类不要写 `package`，`getStateName()` 用的是 `getClass().getTypeName()`，加了包名就会变成 `xx.StandState`。`ExceptionTest` 里要自己写调用：`handle(character, null)` 和 `handle(null, Key.UP)`。两个参数同时为 `null` 时，上面先抛 key 的异常。提供的测试是分开测的。

### 4.11 2021 S2 Q3 BST 前序迭代器

**题意**：补 `ParcelBST` 内部类 `IteratorPreOrder` 的 `hasNext` 和 `next`，前序遍历。孩子不是 `null`，空孩子是 `EmptyTree`。耗尽后 `next` 抛 `NoSuchElementException`。`compareTo` 归 [08 Java 基础](08-java-basics.md)。原题 [readme.md](../exam-practice-questions/past-exams/2021-2/q3/readme.md)。

**套路对应**：3.4。构造时当前节点不是 `EmptyTree` 就压栈。弹出后先压右孩子、再压左孩子，空节点不压。

**关键代码**（片段。`ParcelTreeTest` 的 `iteratorTest1` 到 `iteratorTest4` 通过）：

```java
private final java.util.Deque<Tree<Parcel>> stack = new java.util.ArrayDeque<>();
public IteratorPreOrder() { pushIfReal(ParcelBST.this); }
private void pushIfReal(Tree<Parcel> node) {
    if (node != null && !(node instanceof EmptyTree)) stack.push(node);
}
public boolean hasNext() { return !stack.isEmpty(); }
public Parcel next() {
    if (!hasNext()) throw new NoSuchElementException();
    Tree<Parcel> node = stack.pop();
    pushIfReal(node.rightNode); // 先右
    pushIfReal(node.leftNode);  // 后左
    return node.value;
}
```

**坑**：这题用的是 `java.util.Iterator`，文件里已经 import。把 `EmptyTree` 压进栈的话，`next` 会返回 `null`。可以在这个内部类里加字段和方法。

### 4.12 散题 q12 自动售货机

**题意**：按 [diagrams.pdf](../exam-practice-questions/misc-questions/q12-uml-state-testing/diagrams.pdf) 从零建 5 个状态类。`State` 是抽象类，构造器收下 `VendingMachine`，字段 `machine` 已经有了。每个状态记住这台机器，不是单例。原题 [readme.md](../exam-practice-questions/misc-questions/q12-uml-state-testing/readme.md)。

**套路对应**：3.2 的「状态类要记住上下文」。`VendingMachine` 的初始状态已经是 `new Idle(this)`，把空的 `getState`、`setState` 补成返回和赋值。

| 当前状态 | 事件 | 下一个状态 |
|---|---|---|
| Idle | AnyButton | WaitingToSelect |
| WaitingToSelect | ItemSelected | WaitingToPay |
| WaitingToSelect | Cancelled 或 Timeout | Idle |
| WaitingToPay | ItemPaid | WaitingToDispense |
| WaitingToPay | Cancelled 或 Timeout | Idle |
| WaitingToDispense | ItemDispensed | ReadyToCollect |
| ReadyToCollect | Timeout | Idle |

**关键代码**（`Idle` 是示例，另外四个类同样写，只改 `if`。`getState`、`setState` 各一行。通过 `StateTest` 的 5 个测试）：

```java
public State getState() { return state; }
public void setState(State state) { this.state = state; }

public class Idle extends State {
    public Idle(VendingMachine machine) { super(machine); }
    public void handle(Event event) {
        if (event == Event.AnyButton) machine.setState(new WaitingToSelect(machine));
    }
}
```

**坑**：图上没有的事件留在原状态。`WaitingToDispense` 只认 `ItemDispensed`，`ReadyToCollect` 只认 `Timeout`。转换写 `new` 下一个状态并把 `machine` 传进去。不要写成单例。文件不要加 `package`。

### 4.13 散题 q4 角色状态

**题意**：和 4.10 同一套写法，状态换成 Stand、Suspend、Walk、Squat，没有 RESET。图在 [Graph.pdf](../exam-practice-questions/misc-questions/q4-state-testing-bst/Graph.pdf)，原题 [readme.md](../exam-practice-questions/misc-questions/q4-state-testing-bst/readme.md)。

**套路对应**：3.2。Stand 上 UP 到 Suspend，DOWN 到 Squat，RIGHT 到 Walk。Suspend 的 DOWN、Squat 的 UP、Walk 的 LEFT 回到 Stand。`check` 同 4.10。

**关键代码**：单例和 `check` 照 4.10 写，类名换成这四个。`StateTest` 的 3 个按键序列通过。

**坑**：不要把 4.10 的五个状态和 `RESET` 抄过来。枚举里还有 D、A、W、S、Z、X、C，图上没有，留在原状态。这题的 BST 反前序归 [04 树](04-trees.md)。

### 4.14 散题 q8 parcel 的 Singleton

**题意**：运输办公室全程序只有一个，用 `getInstance()`。`transportBy` 在能满足重量和天数的运输方式里选最便宜的：Ship 7 天、不限重，Train 3 天、不限重，Airplane 1 天、重量不超过 20。选不到就退回。`receive` 时，收件人对象必须是包裹上记录的那一个。原题 [readme.md](../exam-practice-questions/misc-questions/q8-dp-testing/parcel/readme.md)。

**套路对应**：2.3 的懒汉三样，构造器骨架已经是 `private`。选择写在 `transportBy` 里。改状态用 `Status.XXX.setStatus(parcel)`，和 `PostOffice.registerParcel` 一样。

**关键代码**（通过 `ParcelTest` 的 6 个测试）：

```java
public static TransportOffice getInstance() {
    if (instance == null) instance = new TransportOffice();
    return instance;
}
public Transport transportBy(Parcel parcel) {
    int days = parcel.getAllowedDays();
    int weight = parcel.getWeight();
    Transport chosen = null;
    if (days >= 7) chosen = Transport.SHIP;
    else if (days >= 3) chosen = Transport.TRAIN;
    else if (weight <= 20) chosen = Transport.AIRPLANE;
    if (chosen == null) { Status.RETURNED.setStatus(parcel); return null; }
    Status.TRANSFERRING.setStatus(parcel);
    return chosen;
}
public void receive(Parcel parcel) { // Recipient
    if (parcel.getRecipient() == this) Status.DELIVERED.setStatus(parcel);
    else Status.RETURNED.setStatus(parcel);
}
```

**坑**：20 和天数边界都包含等号。重量 20、只给 1 天，选 Airplane。重量 15、给 8 天，选 Ship，不选 Airplane。收件人用 `==` 比对象，另一个 `new Recipient()` 算错的人。

### 4.15 散题 q13 Iterator

**题意**：`GreenGrocery.restock` 把水果放进列表。内部类 `FruitIterator` 用下标做 `hasNext` 和 `next`。接口是题目自己的 `Iterator`。原题 [readme.md](../exam-practice-questions/misc-questions/q13-iterator/readme.md)。

**套路对应**：3.4 的线性集合。`index` 字段已经有了，默认是 0。`hasNext` 是 `index < size`，`next` 是 `get(index++)`。

**关键代码**（通过 `GroceryTest` 的 1 个测试）：

```java
public void restock(List<Fruit> fruits) { this.fruits.addAll(fruits); }
public boolean hasNext() { return index < grocery.getFruits().size(); }
public Fruit next() { return grocery.getFruits().get(index++); }
```

**坑**：不要 `import java.util.Iterator`，会和题目的接口撞名。提供的测试只 `restock` 一次。这里用 `addAll` 追加。不确定第二次调用是追加还是先清空。

### 4.16 散题 q15 Facade

**题意**：`Calculator` 的加减乘除各自只创建对应的运算对象并调用 `evaluate`，自己不做运算。`Division.evaluate` 在除数是 0 时抛 `ArithmeticException`。原题 [readme.md](../exam-practice-questions/misc-questions/q15-facade/readme.md)。

**套路对应**：3.6。加、减、乘的 `evaluate` 骨架里已经写好。

**关键代码**（通过 `CalculatorTest` 的 2 个测试，含除以 0）：

```java
public double add(double a, double b) { return new Addition(a, b).evaluate(); }
public double subtract(double a, double b) { return new Subtraction(a, b).evaluate(); }
public double multiply(double a, double b) { return new Multiplication(a, b).evaluate(); }
public double divide(double a, double b) { return new Division(a, b).evaluate(); }

public double evaluate() { // Division
    if (b == 0) throw new ArithmeticException("divide by zero");
    return a / b;
}
```

**坑**：`double` 除以 0 得到 `Infinity`，不会自己抛异常，所以要先判断 `b == 0`。`Calculator` 的方法体里不要出现 `+ - * /`。

## 5 易错清单

1. 类名、字段名、方法名和 UML 差一个字母或大小写，命名测试就挂。写完逐个对照。
2. 接口方法在实现类里漏写 `public`，编译不过。
3. 集合字段没初始化，第一次 `add` 就 `NullPointerException`。
4. Singleton 漏写 private 构造器，或 `instance`、`getInstance()` 漏写 `static`。
5. State 的转换写在上下文里的 `if` 链，而不是状态类里。转换时用了 `new` 而题目要求 `getInstance()`，测试用 `==` 比较会挂。
6. Observer 在失败的操作后也通知；`attach` 没挡 `null` 和重复。
7. 前序迭代器压栈顺序写反（应先右后左）。
8. 子类构造器里 `super(...)` 不在第一句。
