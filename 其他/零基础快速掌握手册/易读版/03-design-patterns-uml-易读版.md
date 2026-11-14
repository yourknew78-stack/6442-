# 03｜UML 和设计模式：先读图，再写类

> UML 说**有哪些类、字段、方法**；设计模式说**这些类怎样分工**。

## 1. 一眼认题型

| 看到 | 先想 |
|---|---|
| `according to UML` | 精确照图建类 |
| `getInstance()` | Singleton 单例 |
| 状态图、`XxxState` | State 状态 |
| `attach`、`detach`、`update` | Observer 观察者 |
| `hasNext()`、`next()` | Iterator 迭代器 |
| `Creator`、`createXxx()` | Factory Method 工厂方法 |

## 2. UML 框怎么读

```text
Account
----------------------
- balance : int
----------------------
+ deposit(amount : int) : void
```

对应 `private int balance;` 和 `public void deposit(int amount)`。UML 写“**名字 : 类型**”，Java 写“**类型 名字**”。

| UML | Java |
|---|---|
| `+` / `-` / `#` | `public` / `private` / `protected` |
| 下划线 | `static` |
| 斜体或 `<<abstract>>` | `abstract` |
| `<<interface>>` | `interface` |
| `0..*` | 零到多个，常用 `List<T>` |

## 3. UML 连线怎么读

| 线 | Java 中通常体现为 |
|---|---|
| 实线空心三角指向 B | `class A extends B` |
| 虚线空心三角指向 B | `class A implements B` |
| 实线关联 | A 长期持有 B 字段 |
| 空心菱形在 A 端 | A 聚合 B，B 可独立存在 |
| 实心菱形在 A 端 | A 组合 B，B 依附 A |
| 虚线普通箭头 | A 临时使用 B，常为参数或局部变量 |

字段格已有的字段不要因连线再加一遍。题目若禁止额外字段或方法，严格核对 UML 和 README。

## 4. 几个模式如何分工

**Singleton**：一个类只提供同一个实例。需要 `private static` 实例、`private` 构造器、`public static getInstance()`。例如：

```java
class Config {
    private static final Config INSTANCE = new Config();
    private Config() {}
    public static Config getInstance() { return INSTANCE; }
}
```

**State**：对象把事件交给当前状态处理。先将状态图抄为“当前状态 / 事件 / 下一状态”表，再把每行写入对应状态类。看接口签名：有的状态方法返回下一状态，有的调用上下文的 `setState`。若题目要求状态单例，用 `getInstance()` 转换，不能 `new`。

**Observer**：subject 保存 `List<Observer>`。通过 `attach` 登记，`detach` 移除；**成功修改后**逐个调用 `update`。是否拒绝 null、是否去重、通知参数以题目规格为准。

**Iterator**：`hasNext()` 只判断，不移动；`next()` 返回一个元素并前进。树的前序遍历为“根、左、右”。用栈时先压右孩子、再压左孩子，左孩子才会先弹出。

**Factory Method**：父类声明创建方法，由子类决定具体 `new` 什么。**Template Method**：父类固定流程，某几步交给子类。两者可一起出现。

**Facade**：一个简单入口转调多个子系统。**Strategy**：多种可互换算法各自实现同一接口，由调用方选择。

## 5. 按图实现的顺序

1. 列清所有 class、abstract class、interface、enum。
2. 逐个照抄字段、方法签名、可见性和大小写。
3. 翻译继承、实现、持有关系；检查重复字段。
4. State 画转换表；Observer 标通知时机；Iterator 手走输出顺序。
5. 运行原题测试：先修编译与签名，再修行为。
6. 一个 `public` 类一个同名文件，接口方法的实现必须写 `public`。

更完整的代码骨架和历年题在[原稿详细版](../原稿归档/03-design-patterns-uml.md)。
