# 08 Java 基础和考场操作

> 这个文件管什么题：写任何一道代码题时随手查的速查表。语法怎么写、常用 API 叫什么、异常怎么抛、编译不过怎么查、考场上怎么提交。
> 历年出现：Java OOP 基础（继承、抽象类、多态、`Comparable`、集合）在 2021 S2、2022 S1、2023 S2、2024 S1、2025 S1 的代码题里考过；异常处理在 2019 S1、2021 S2、2022 S1、2024 S2 考过。今年预测：五道题里四道要写 Java，不会单独出基础题，但每道题都用得到。

## 目录

- [1 先认题型](#1-先认题型)
- [2 零基础速懂](#2-零基础速懂)
  - [2.1 类 字段 方法 构造器](#21-类-字段-方法-构造器)
  - [2.2 继承 抽象类 接口 多态](#22-继承-抽象类-接口-多态)
  - [2.3 enum 枚举](#23-enum-枚举)
  - [2.4 基本类型 包装类型 和 equals](#24-基本类型-包装类型-和-equals)
  - [2.5 重写 equals hashCode toString](#25-重写-equals-hashcode-tostring)
  - [2.6 泛型的读法](#26-泛型的读法)
  - [2.7 String Character StringBuilder 速查](#27-string-character-stringbuilder-速查)
  - [2.8 数字和 Math 速查](#28-数字和-math-速查)
  - [2.9 集合速查](#29-集合速查)
  - [2.10 遍历时删除元素](#210-遍历时删除元素)
  - [2.11 异常](#211-异常)
  - [2.12 Comparable 和 Comparator](#212-comparable-和-comparator)
  - [2.13 考场操作 git 和 IntelliJ](#213-考场操作-git-和-intellij)
- [3 套路](#3-套路)
  - [3.1 填空式 OOP 题的步骤](#31-填空式-oop-题的步骤)
  - [3.2 抽象类加子类](#32-抽象类加子类)
  - [3.3 自定义异常加参数检查](#33-自定义异常加参数检查)
  - [3.4 compareTo 的写法](#34-compareto-的写法)
  - [3.5 用 Map 计数 判断够不够 求差集](#35-用-map-计数-判断够不够-求差集)
  - [3.6 编译不过的排查](#36-编译不过的排查)
  - [3.7 时间快到时的保分做法](#37-时间快到时的保分做法)
- [4 真题套用](#4-真题套用)
  - [4.1 2025 S1 Q1 Task A Kitchen 集合运算](#41-2025-s1-q1-task-a-kitchen-集合运算)
  - [4.2 2024 S1 Q1 子类构造器和循环取名](#42-2024-s1-q1-子类构造器和循环取名)
  - [4.3 2022 S1 Q2 快递公司报价](#43-2022-s1-q2-快递公司报价)
  - [4.4 2022 S1 Q1 check 抛异常](#44-2022-s1-q1-check-抛异常)
  - [4.5 2021 S2 Q3 compareTo 按距离排序](#45-2021-s2-q3-compareto-按距离排序)
- [5 易错清单](#5-易错清单)

## 1 先认题型

这里不分题型，按「遇到什么情况」查。

| 遇到的情况 | 查哪一节 | 最像的真题 |
|---|---|---|
| 看不懂类的写法、`this`、`static`、`final` | 2.1 | |
| 不会写子类构造器、`super(...)`、`@Override` | 2.2、3.2 | 4.2、4.3 |
| 题目给了 `enum`，要遍历或取名字 | 2.3 | |
| 参数是 `Integer`，可能是 `null`，要给默认值 | 2.4、3.2 | 4.2 |
| 两个字符串或两个对象明明一样，比较却是 `false` | 2.4、2.5 | |
| 忘了字符串、字符、数字、`Math` 的方法名 | 2.7、2.8 | |
| 忘了 List、Map、Set、栈、队列的方法名 | 2.9 | |
| 循环里删元素报 `ConcurrentModificationException` | 2.10 | |
| 要抛自定义异常，要检查参数是不是 `null` | 2.11、3.3 | 4.4、4.3 |
| 方法签名不让加 `throws`，里面的调用却会抛受检异常 | 2.11 | |
| 要比较两个对象的大小，要排序 | 2.12、3.4 | 4.5 |
| 要数每种东西有几个，判断够不够，求还缺多少 | 3.5 | 4.1 |
| 抽象类加几个子类，分段计费，算距离 | 3.2 | 4.3 |
| 拿到填空式代码题不知道从哪下手 | 3.1 | |
| 代码编译不过 | 3.6 | |
| 不知道怎么提交，怎么确认提交成功 | 2.13 | |
| 时间快到了还没写完 | 3.7 | |

## 2 零基础速懂

### 2.1 类 字段 方法 构造器

类（class）是对象的图纸。字段（field）是对象里存的数据，方法（method）是对象能做的事，构造器（constructor）在 `new` 的时候给字段赋初值。

```java
public class Dog {
    private final String name;         // 字段。final：赋值一次后不能再改
    private int age;
    private static int count = 0;      // static：属于类，所有对象共用一份

    public Dog(String name, int age) { // 构造器：名字和类名相同，没有返回类型
        this.name = name;              // this.name 是字段，name 是参数
        this.age = age;
        count++;
    }
    public Dog(String name) { this(name, 0); }      // this(...)：调用本类另一个构造器，必须是第一句
    public String getName() { return name; }        // 方法：返回类型 方法名(参数)
    public static int getCount() { return count; }  // 静态方法：用 Dog.getCount() 调用
}
```

- 用法：`Dog d = new Dog("Rex", 3); d.getName();`
- 可见性：`private` 只有本类能用；不写是同一个包能用；`protected` 再加上子类；`public` 谁都能用。
- 静态方法里没有 `this`，不能直接用非静态的字段和方法。`main` 是静态方法。
- `final` 字段必须在声明处或构造器里赋值；`final` 的 List 不能换成另一个 List，但可以往里 `add`。

### 2.2 继承 抽象类 接口 多态

```java
public abstract class Animal {                 // 抽象类（abstract class）：不能 new
    protected final String name;
    public Animal(String name) { this.name = name; }
    public abstract String speak();            // 抽象方法：没有方法体，子类必须实现
    public String intro() { return name + " says " + speak(); }  // 普通方法：子类直接继承
}
public class Dog extends Animal {              // extends：继承，只能有一个父类
    public Dog(String name) { super(name); }   // super(...)：调用父类构造器，必须是第一句
    @Override
    public String speak() { return "Woof"; }   // 重写（override）：方法名和参数与父类完全一样
}
public interface Shape { double area(); }      // 接口（interface）：只列方法，自动是 public abstract
public class Circle implements Shape {         // implements：实现接口，可以同时实现多个
    private final double r;
    public Circle(double r) { this.r = r; }
    @Override
    public double area() { return Math.PI * r * r; }  // 实现接口方法必须写 public
}
```

- 多态（polymorphism）：`Animal a = new Dog("Rex"); a.intro();` 得到 `"Rex says Woof"`。变量的类型决定能调用哪些方法，对象的实际类型决定运行哪个版本。
- 父类没有无参构造器时，子类构造器第一句必须写 `super(参数)`。
- 判断实际类型再转型：`if (a instanceof Dog) { Dog d = (Dog) a; }`。不判断就转，类型不对会抛 `ClassCastException`。
- 接口里带方法体的方法要写 `default`（2022 S1 Q1 的 `State.check` 就是）。
- `@Override` 不是必须的，但写上以后签名抄错会直接报编译错，建议都写。

### 2.3 enum 枚举

枚举（enum）是一组固定的常量。

```java
public enum Level {
    LOW(1), HIGH(3);                              // 常量列表，带字段时在括号里传值，分号结束
    private final int weight;
    Level(int weight) { this.weight = weight; }   // enum 的构造器不能写 public
    public int getWeight() { return weight; }
}
```

| 写法 | 结果 |
|---|---|
| `Level.values()` | 数组 `[LOW, HIGH]`，用 `for (Level l : Level.values())` 遍历 |
| `Level.HIGH.name()`、`Level.HIGH.toString()` | `"HIGH"` |
| `Level.HIGH.ordinal()` | `1`（声明顺序，从 0 开始） |
| `Level.valueOf("LOW")` | `Level.LOW`；名字不存在抛 `IllegalArgumentException` |
| `Level.HIGH.getWeight()` | `3` |
| `a == Level.LOW` | 枚举用 `==` 比较即可；也能用在 `switch` 里（`case LOW:` 不写 `Level.`） |

### 2.4 基本类型 包装类型 和 equals

| 基本类型（primitive） | 包装类型（wrapper） | 区别 |
|---|---|---|
| `int` `long` `double` `boolean` `char` | `Integer` `Long` `Double` `Boolean` `Character` | 包装类型是对象，可以是 `null`；集合里只能放包装类型，如 `List<Integer>` |

- 自动拆箱：`Integer x = null; int y = x;` 抛 `NullPointerException`。`x + 1`、`x > 0` 也会。先写 `x != null` 再用。
- `==` 对基本类型比较值，对对象比较「是不是同一个对象」。字符串和包装类型比较内容必须用 `equals`。
  - `new String("ab") == "ab"` 是 `false`，`"ab".equals(s)` 才对。
  - `Integer a = 1000, b = 1000;` 时 `a == b` 是 `false`，`a.equals(b)` 是 `true`。
  - 两边都可能是 `null` 时用 `Objects.equals(a, b)`（`import java.util.Objects;`）。
- 整数除法丢小数：`7 / 2` 是 `3`，`7 / 2.0` 是 `3.5`，`7 % 2` 是 `1`。两个 `int` 要小数结果写 `(double) a / b`。
- `(int) 3.9` 是 `3`（直接截掉小数）。`double` 赋给 `int` 必须写 `(int)`。
- 字符运算：`'7' - '0'` 是 `7`；`(char) ('a' + 1)` 是 `'b'`。

### 2.5 重写 equals hashCode toString

不重写时，`equals` 等同于 `==`。`assertEquals`、`list.contains`、`HashMap` 的键、`HashSet` 都靠 `equals` 和 `hashCode`。

```java
@Override
public boolean equals(Object o) {                 // 参数类型必须是 Object
    if (this == o) return true;
    if (!(o instanceof Point)) return false;      // o 是 null 时这里也返回 false
    Point p = (Point) o;
    return x == p.x && y == p.y && Objects.equals(label, p.label);  // 基本类型用 ==，对象用 Objects.equals
}
@Override
public int hashCode() { return Objects.hash(x, y, label); }   // 用到的字段和 equals 一致
@Override
public String toString() { return "Point(" + x + ", " + y + ")"; }
```

只重写 `equals` 不重写 `hashCode`，对象放进 `HashSet` 或当 `HashMap` 的键时会找不到。

### 2.6 泛型的读法

| 写法 | 读作 |
|---|---|
| `List<String>` | 元素是 `String` 的列表 |
| `Map<String, Integer>` | 键是 `String`、值是 `Integer` 的映射 |
| `Map<String, List<Integer>>` | 值是一个整数列表 |
| `new ArrayList<>()` | 尖括号留空，类型由左边推断 |
| `class Tree<T extends Comparable<T>>` | `T` 是占位的类型，必须能用 `compareTo` 比较 |
| `Iterator<Parcel>` | `next()` 返回 `Parcel` 的迭代器 |

尖括号里不能写基本类型：`List<int>` 编译错，写 `List<Integer>`。

### 2.7 String Character StringBuilder 速查

`String` 不可变：所有方法都返回新字符串，要写 `s = s.trim();` 才有效果。下表设 `s = "Hello World"`。

| 写法 | 结果 |
|---|---|
| `s.length()` | `11` |
| `s.charAt(0)` | `'H'`（下标从 0 开始，越界抛 `StringIndexOutOfBoundsException`） |
| `s.substring(6)`、`s.substring(0, 5)` | `"World"`、`"Hello"`（含头不含尾） |
| `s.indexOf("o")`、`s.indexOf("o", 5)`、`s.indexOf("z")` | `4`、`7`、`-1`（找不到是 -1） |
| `"a,b,,c".split(",")` | `["a", "b", "", "c"]`；末尾的空串会被丢掉：`"a,b,,".split(",")` 是 `["a", "b"]` |
| `"a  b c".split("\\s+")`、`"a.b".split("\\.")` | 按一个或多个空白切；按点切要转义（参数是正则） |
| `"  hi ".trim()` | `"hi"`（去掉两端空白） |
| `"".isEmpty()`、`" ".isBlank()` | `true`、`true`（`" ".isEmpty()` 是 `false`） |
| `s.equals("hello world")`、`s.equalsIgnoreCase("hello world")` | `false`、`true` |
| `s.startsWith("He")`、`s.endsWith("ld")`、`s.contains("lo W")` | 都是 `true` |
| `s.toLowerCase()`、`s.toUpperCase()` | `"hello world"`、`"HELLO WORLD"` |
| `"ab".repeat(3)`、`"  ".repeat(depth)` | `"ababab"`；按深度生成缩进 |
| `s.toCharArray()` | `char[]`，用 `for (char c : s.toCharArray())` 逐字符遍历 |
| `String.valueOf(42)`、`"" + 42`、`String.join(",", list)` | `"42"`、`"42"`、用逗号拼接列表 |
| `"apple".compareTo("banana")` | 负数（字典序在前） |

| `Character` 写法 | 结果 |
|---|---|
| `Character.isDigit('7')`、`Character.isLetter('a')` | `true`、`true` |
| `Character.isUpperCase('A')`、`Character.isLowerCase('A')` | `true`、`false` |
| `Character.isWhitespace(' ')`（空格、`\t`、`\n` 都算） | `true` |
| `Character.isLetterOrDigit('_')` | `false` |
| `Character.toUpperCase('a')`、`Character.getNumericValue('7')` | `'A'`、`7` |

`StringBuilder` 是可变的字符串，循环里拼接用它：

```java
StringBuilder sb = new StringBuilder();
sb.append("ab").append(1).append('c');   // "ab1c"，append 可以连着写
sb.length();  sb.charAt(0);              // 4，'a'
sb.setLength(sb.length() - 1);           // 删掉最后一个字符，变成 "ab1"
sb.insert(0, "x");  sb.reverse();        // "xab1"，再反转成 "1bax"
String result = sb.toString();           // 最后转回 String
```

### 2.8 数字和 Math 速查

| 写法 | 结果 |
|---|---|
| `Integer.parseInt("42")`、`Integer.parseInt("-5")` | `42`、`-5` |
| `Integer.parseInt("4a")`、`("")`、`(" 4")`、`("3.5")` | 都抛 `NumberFormatException` |
| `Double.parseDouble("3.5")` | `3.5` |
| `Math.sqrt(16)` | `4.0`（返回 `double`） |
| `Math.pow(2, 10)` | `1024.0`（返回 `double`） |
| `Math.ceil(3.1)`、`Math.floor(3.9)` | `4.0`、`3.0`（返回 `double`，要整数写 `(int) Math.ceil(x)`） |
| `Math.round(2.5)` | `3`（返回 `long`） |
| `Math.max(3, 7)`、`Math.min(3, 7)`、`Math.abs(-3)` | `7`、`3`、`3` |
| `Integer.MAX_VALUE`、`Integer.MIN_VALUE` | `2147483647`、`-2147483648`（求最小值时的初值） |
| `Integer.compare(a, b)`、`Double.compare(a, b)` | 负数、0、正数，分别表示 a 小于、等于、大于 b |

字符串转数字失败时不让程序崩（片段）：

```java
int n;
try { n = Integer.parseInt(text); } catch (NumberFormatException e) { return null; }  // 按题目要求返回 null 或别的值
```

小数计算有误差：`3 * 1.3` 是 `3.9000000000000004`。要和期望值精确相等时，用整数算完再除（见 4.3）。

### 2.9 集合速查

都在 `java.util` 包里，文件开头写 `import java.util.*;`。

| 类型和创建 | 常用方法 |
|---|---|
| `List<String> list = new ArrayList<>();` 有顺序，可重复 | `add(x)`、`add(0, x)`、`get(i)`、`set(i, x)`、`size()`、`isEmpty()`、`contains(x)`、`indexOf(x)`、`remove(i)`、`addAll(other)`、`clear()` |
| `Map<String, Integer> map = new HashMap<>();` 键值对，键不重复，无顺序 | `put(k, v)`、`get(k)`（没有这个键返回 `null`）、`getOrDefault(k, 0)`、`containsKey(k)`、`remove(k)`、`size()`、`keySet()`、`values()`、`entrySet()` |
| `Set<String> set = new HashSet<>();` 不重复，无顺序 | `add(x)`（已存在返回 `false`）、`contains(x)`、`remove(x)`、`size()` |
| `Deque<Integer> stack = new ArrayDeque<>();` 当栈用 | `push(x)` 压栈、`pop()` 弹出、`peek()` 看栈顶、`isEmpty()` |
| `Deque<Integer> queue = new ArrayDeque<>();` 当队列用 | `offer(x)` 入队尾、`poll()` 出队头、`peek()` 看队头、`isEmpty()` |
| `int[] arr = new int[5];`、`int[] arr = {1, 2, 3};` 数组 | `arr.length`（没有括号）、`arr[i]`、`Arrays.sort(arr)`、`Arrays.toString(arr)` |

- 遍历 Map：`for (Map.Entry<String, Integer> e : map.entrySet()) { e.getKey(); e.getValue(); }`。
- 要保持放入顺序用 `LinkedHashMap`、`LinkedHashSet`；要按键排序用 `TreeMap`、`TreeSet`。
- `List<Integer>` 的 `remove(1)` 删的是下标 1；按值删写 `list.remove(Integer.valueOf(1))`。
- 空栈 `pop()` 抛 `NoSuchElementException`；空的 `poll()`、`peek()` 返回 `null`。`ArrayDeque` 不能放 `null`，要放 `null` 用 `LinkedList`。
- 复制一份：`new ArrayList<>(other)`、`new HashMap<>(other)`。
- 排序：`Collections.sort(list)`（元素要实现 `Comparable`）；`list.sort(比较器)`（见 2.12）；`Collections.reverse(list)` 反转。
- `Arrays.asList(1, 2, 3)`：长度固定，`add`、`remove` 抛 `UnsupportedOperationException`。
- `List.of(1, 2)`、`Set.of(...)`、`Map.of("a", 1)`：不可变（immutable），`add`、`put`、`remove` 都抛 `UnsupportedOperationException`；`List.of(1, 2).contains(null)` 和 `Map.of("a", 1).get(null)` 抛 `NullPointerException`。要能改就包一层：`new ArrayList<>(List.of(1, 2))`。

### 2.10 遍历时删除元素

在 `for (x : list)` 里调用 `list.remove(x)` 会抛 `ConcurrentModificationException`。三种正确写法任选：

```java
list.removeIf(x -> x < 0);                         // 1 最短：删掉所有满足条件的

Iterator<Integer> it = list.iterator();            // 2 用迭代器自己的 remove
while (it.hasNext()) {
    if (it.next() < 0) it.remove();
}

for (int i = list.size() - 1; i >= 0; i--) {       // 3 按下标倒着删
    if (list.get(i) < 0) list.remove(i);
}
```

Map 同理：`map.entrySet().removeIf(e -> e.getValue() == 0);`。

### 2.11 异常

异常（exception）是运行时出错的信号。`throw new XxxException(...)` 抛出后，当前方法立刻结束，一层层往外传，直到被 `catch` 接住。

| 种类 | 父类 | 编译器要求 | 例子 |
|---|---|---|---|
| 受检异常（checked） | `Exception`（不经过 `RuntimeException`） | 调用处必须 `try/catch`，或者方法签名写 `throws` | `IOException`、`ParserConfigurationException`、4.3 的 `IllegalParcelItemException` |
| 非受检异常（unchecked） | `RuntimeException` | 没要求 | 下表全部 |

```java
public class EmptyNameException extends Exception {          // 改成 extends RuntimeException 就是非受检
    public EmptyNameException(String message) { super(message); }
}
public void setName(String name) throws EmptyNameException { // throws：写在签名上，声明可能抛
    if (name == null || name.isEmpty()) throw new EmptyNameException("empty");  // throw：真的抛
    this.name = name;
}
```

```java
try {
    obj.setName(text);            // 这里抛了异常，try 里后面的语句不再执行
} catch (EmptyNameException e) {  // 接住这一种和它的子类；多个 catch 时子类写前面
    return null;
} finally {
    // 不管有没有异常都会执行，可以不写
}
```

签名不让加 `throws`，里面的调用却抛受检异常：用 `try/catch` 包住，在 `catch` 里按题目要求返回（常见是 `return null;`）。题目没说怎么处理时写 `throw new RuntimeException(e);`。

| 运行时异常 | 什么时候出现 |
|---|---|
| `NullPointerException` | 对 `null` 调方法或取字段；`null` 的 `Integer` 拆箱；`List.of(...).contains(null)` |
| `IndexOutOfBoundsException` | `list.get(list.size())`、`arr[-1]`、`s.charAt(s.length())`、`s.substring(3, 1)` |
| `NumberFormatException` | `Integer.parseInt("abc")`；它是 `IllegalArgumentException` 的子类 |
| `ClassCastException` | `(Dog) animal` 而对象其实是 `Cat` |
| `NoSuchElementException` | 迭代器走完了还调 `next()`；空栈 `pop()`。自己写迭代器时也要抛它 |
| `IllegalArgumentException` | 参数不合法时自己抛；`Level.valueOf("XX")` |
| `ArithmeticException` | 整数除以 0 |
| `ConcurrentModificationException` | for-each 遍历时删元素 |
| `UnsupportedOperationException` | 对 `List.of`、`Arrays.asList` 的结果 `add` |
| `StackOverflowError` | 递归没有出口 |

### 2.12 Comparable 和 Comparator

`Comparable` 写在类自己里面，定义「自然顺序」；`Comparator` 是类外面单独的比较器。两者返回值含义一样：

| `a.compareTo(b)` 或 `compare(a, b)` 的返回值 | 含义 |
|---|---|
| 负数 | a 排在 b 前面 |
| 0 | 一样大 |
| 正数 | a 排在 b 后面 |

```java
public class Student implements Comparable<Student> {
    private final String name;
    private final int mark;
    public Student(String name, int mark) { this.name = name; this.mark = mark; }
    public String getName() { return name; }
    public int getMark() { return mark; }
    @Override
    public int compareTo(Student other) { return Integer.compare(this.mark, other.mark); }  // 按 mark 升序
}
```

```java
Collections.sort(students);                                          // 用 compareTo 排序
students.sort((a, b) -> Integer.compare(b.getMark(), a.getMark()));  // 比较器：b 写前面是降序
students.sort(Comparator.comparing(Student::getName));               // 按名字升序
students.sort(Comparator.comparing(Student::getMark).reversed().thenComparing(Student::getName));  // mark 降序，再按名字
```

### 2.13 考场操作 git 和 IntelliJ

最小流程，在 IntelliJ 底部的 Terminal 里敲（当前目录是考试仓库）：

```
git status                  # 看改了哪些文件
git add -A                  # 把所有改动放进暂存区
git commit -m "Q1 task A"   # 提交到本地，引号里随便写说明
git push                    # 推到考试 GitLab，只有这一步之后老师才看得到
```

- 第一次 `git push` 如果报 `fatal: The current branch xxx has no upstream branch.`，照它下面给出的那一行敲：`git push --set-upstream origin xxx`。以后直接 `git push`。
- 确认推送成功，三样都看：
  1. `git status` 显示 `Your branch is up to date with 'origin/xxx'.` 和 `nothing to commit, working tree clean`。出现 `Your branch is ahead of 'origin/xxx' by 1 commit.` 说明还没 push。
  2. `git log origin/xxx -1` 显示的是你最后一次提交的说明（`xxx` 是分支名，`git status` 第一行有）。
  3. 浏览器打开考试 GitLab，点进文件，看到的是你写的代码。README 要求必须在考试时间内做这一步。
- 多久提交一次：每写完一个 Task 且能编译，就 `add`、`commit`、`push` 一次。最后 10 到 15 分钟服务器很慢，README 要求最后一次推送至少提前 5 分钟。
- `git push` 被拒绝并提示 `fetch first`：先 `git pull --rebase`，再 `git push`。
- IntelliJ 运行一个测试类：打开测试文件，点类名那一行左边的绿色三角，选 Run。只跑一个测试就点那个方法左边的三角。
- IntelliJ 检查整个项目能不能编译：菜单 Build 里的 Build Project。别的题有编译错时，这道题的测试也跑不起来，先把那边改到能编译（见 3.7）。

交卷前检查清单：

| 检查 | 怎么做 |
|---|---|
| 整个项目能编译 | Build Project 没有红色错误 |
| 每道题的示例测试都跑过 | 逐个运行测试类 |
| 没改方法签名、没加字段、没加 `throws`、没改 `package` 行、没移动或改名文件 | `git diff` 或 `git status` 看改动的文件是不是只有题目要求提交的那些 |
| 没 `import` 标准库和 JUnit4 以外的东西 | 看每个文件开头的 `import`（IntelliJ 自动补全有时会加进奇怪的包） |
| 调试用的 `System.out.println` 不影响返回值 | 删掉或留着都行，但不能改变方法的结果 |
| 已经推送 | 上面的三样确认 |

## 3 套路

### 3.1 填空式 OOP 题的步骤

适用：给了一堆类，只让在 `YOUR CODE STARTS HERE` 之类的注释之间写代码。

1. 读 README 末尾的任务清单和「要提交的文件」，记下要写哪几个方法。README 里「不能加字段」「能不能加辅助方法」「不能改签名」的句子逐条看。
2. 读要改的类：字段有哪些，构造器怎么赋值，方法上面的注释（常常比 README 更细）。
3. 读它的父类和接口：哪些方法已经写好可以直接调用，哪些是 `abstract` 要你实现。
4. 读测试：看对象是怎么 `new` 出来的，方法怎么调用，期望值是什么。期望值能帮你确认对题意的理解。
5. 只在标出的区域里写。已有的 `return null;` 如果注释说可以改（feel free to change），就改成你的返回值。
6. 每写完一个方法就编译一次并跑测试，不要全写完再编译。
7. 测试过了以后，回到 README 逐条对照文字要求，测试没覆盖到的情况（`null`、空集合、边界值、相等的情况）自己想一遍。
8. `commit` 并 `push`。

拿部分分：先写最简单的方法（getter、构造器、单个判断）；没写完的方法保留原来的 `return null;`，保证能编译。

### 3.2 抽象类加子类

适用：一个 `abstract` 父类加几个子类，子类只有少数地方不同（4.2、4.3）。

1. 分清东西放哪：

| 放父类 | 留给子类 |
|---|---|
| 所有子类都有的字段 | 只有这个子类有的字段、常量 |
| 父类构造器：检查参数、给共同字段赋值 | 子类构造器：第一句 `super(...)`，把参数传上去 |
| 算法完全相同的方法 | 每个子类算法不同的方法（父类里声明成 `abstract`） |
| 步骤相同、只有某一步不同的方法：父类写步骤，那一步调用抽象方法 | 那一步的具体做法（`@Override`） |

2. 父类构造器的参数是 `Integer` 时，先判 `null` 再比较大小，`null` 时保留字段的默认值。
3. 子类有自己的默认值时，在 `super(...)` 的括号里用三元表达式 `x == null ? 默认值 : x`。Java 17 里 `super(...)` 前面不能写任何语句。
4. 子类方法里可以直接用父类的 `protected` 和不写修饰符的字段（同一个包）；`private` 的要通过 getter。

```java
public abstract class Shape {
    protected final String name;
    protected int size = 1;                               // 默认值写在字段上
    public Shape(String name, Integer size) {             // Integer 才能接收 null
        if (size != null && size < 0) throw new IllegalArgumentException("size < 0");  // 先判 null 再比较
        this.name = name;
        if (size != null) this.size = size;               // null 时保留默认值 1
    }
    public abstract double area();                        // 每个子类算法不同
    public String describe() { return name + " " + area(); }  // 步骤相同，放父类
}
public class Square extends Shape {
    public Square(Integer size) {
        super("square", size == null ? 2 : size);         // 子类自己的默认值是 2
    }
    @Override
    public double area() { return size * size; }
}
```

拿部分分：构造器只写 `super(参数原样传上去);` 就能编译，默认值以后再补。

### 3.3 自定义异常加参数检查

适用：题面出现 throw、exception、invalid、`null` 参数、precondition。

1. 看异常类是题目给好的还是要自己建。给好的就看它有哪些构造器（很多只有无参的，只能 `new XxxException()`）。
2. 看它继承谁。继承 `Exception` 的是受检异常，方法签名上必须已经有 `throws` 才能抛；签名没有又不让改时，说明题目要的是非受检异常。
3. 在方法最开头检查，一个条件一个 `if`，条件成立就 `throw`。检查顺序按题目列出的顺序。
4. 对可能是 `null` 的东西，先写 `x == null`，再写 `x.方法()`，用 `||` 连接。
5. 检查都通过后再写正常逻辑。

```java
public class InvalidOrderException extends RuntimeException {   // 题目要受检异常就改成 extends Exception
    public InvalidOrderException(String message) { super(message); }
}
// 片段：方法开头先检查，再做正事
public void addOrder(String name, Integer amount) {
    if (name == null || name.isEmpty()) throw new InvalidOrderException("name is empty");
    if (amount == null || amount <= 0) throw new IllegalArgumentException("amount must be positive");
    orders.put(name, amount);
}
```

测试里验证会抛异常的写法在 [02 测试](02-testing.md)。

### 3.4 compareTo 的写法

适用：类声明里有 `implements Comparable<X>`，或题面出现 natural order、ascending、descending、sort。

1. 找出按什么比较，升序（ascending，小的在前）还是降序（descending）。
2. 算出 `this` 和 `other` 各自的那个数。
3. 升序写 `Xxx.compare(this的数, other的数)`；降序把两个参数对调。`int` 用 `Integer.compare`，`double` 用 `Double.compare`，`String` 用 `a.compareTo(b)`。
4. 有多个比较条件时，前一个结果不是 0 就直接返回，是 0 再比下一个。

```java
@Override
public int compareTo(Student other) {                  // 片段：Student 有 mark、gpa、name 三个字段
    int c = Integer.compare(other.mark, this.mark);    // 第一条件：mark 降序（other 写前面）
    if (c != 0) return c;
    c = Double.compare(this.gpa, other.gpa);           // 第二条件：gpa 升序（this 写前面）
    if (c != 0) return c;
    return this.name.compareTo(other.name);            // 第三条件：名字按字典序
}
```

不要写 `return (int) (this.gpa - other.gpa);`：差是 0.5 时强转成 0，被当成相等。`int` 相减在数很大时会溢出。统一用 `compare`。

### 3.5 用 Map 计数 判断够不够 求差集

适用：题面出现 sufficient、enough、required、missing、additional、how many。

1. 计数：建 `Map<种类, Integer>`，遍历每个东西，`put(k, getOrDefault(k, 0) + 数量)`。
2. 判断够不够：遍历「需要」的每一项，手上的数量小于需要的数量就返回 `false`；循环结束返回 `true`。
3. 求还缺多少：遍历「需要」的每一项，`缺 = 需要 - 手上`，大于 0 才放进结果。
4. 手上没有的种类用 `getOrDefault(k, 0)` 当成 0，不要用 `get(k)`（返回 `null`，拆箱时抛异常）。

```java
static Map<String, Integer> count(List<String> items) {           // 1 计数
    Map<String, Integer> have = new HashMap<>();
    for (String item : items) {
        have.put(item, have.getOrDefault(item, 0) + 1);
    }
    return have;
}
static boolean enough(Map<String, Integer> need, Map<String, Integer> have) {   // 2 够不够
    for (Map.Entry<String, Integer> e : need.entrySet()) {
        if (have.getOrDefault(e.getKey(), 0) < e.getValue()) return false;
    }
    return true;
}
static Map<String, Integer> missing(Map<String, Integer> need, Map<String, Integer> have) {  // 3 还缺多少
    Map<String, Integer> result = new HashMap<>();
    for (Map.Entry<String, Integer> e : need.entrySet()) {
        int lack = e.getValue() - have.getOrDefault(e.getKey(), 0);
        if (lack > 0) result.put(e.getKey(), lack);               // 只放不够的
    }
    return result;
}
```

题目不许加辅助方法时，把方法体直接写进要实现的方法里，`have`、`need` 换成局部变量。拿部分分：先只写第 2 步的循环。

### 3.6 编译不过的排查

1. 只看第一条错误。后面的错误常常是第一条引起的，改完第一条重新编译。
2. 点错误信息跳到那一行。括号或分号的错误，问题常常在上一行。
3. 对照下表改。
4. 实在改不好：把刚写的方法体注释掉，恢复成 `return null;`，先回到能编译的状态。

| javac 的报错（原文） | 意思 | 怎么改 |
|---|---|---|
| `cannot find symbol` | 名字不认识 | 看下面的 `symbol:` 行。变量没声明或拼错；方法名大小写错；类没 `import`（如 `List`、`Map` 要 `import java.util.*;`）；用了声明在 `{}` 里面的变量 |
| `incompatible types: String cannot be converted to int` | 类型对不上 | 字符串转数字用 `Integer.parseInt`；数字转字符串用 `String.valueOf` |
| `incompatible types: possible lossy conversion from double to int` | 小数赋给整数 | 加 `(int)`，如 `(int) Math.ceil(x)` |
| `missing return statement` | 有的路径走到方法末尾没有 `return` | 在方法最后补一个 `return`；`if/else if` 链最后要有 `else` |
| `unreported exception java.io.IOException; must be caught or declared to be thrown` | 调用了会抛受检异常的方法 | 用 `try/catch` 包住（不能改签名时只能这样），见 2.11 |
| `non-static method f() cannot be referenced from a static context`（或 `non-static variable x ...`） | 在静态方法里直接用了非静态成员 | 先 `new` 一个对象再调用；或者确认是不是把 `对象.方法()` 写成了 `类名.方法()` |
| `class Foo is public, should be declared in a file named Foo.java` | 类名和文件名不一致 | 一个文件一个 `public` 类，文件名和类名完全相同（含大小写） |
| `';' expected`、`')' expected`、`reached end of file while parsing` | 少分号、少括号、少了结尾的 `}` | 看报错行和上一行；数一下 `{` 和 `}` 是否成对 |
| `constructor Animal in class Animal cannot be applied to given types;` | 调用构造器的参数个数或类型不对；或子类构造器没写 `super(...)` 而父类没有无参构造器 | 对照父类构造器的参数表写 `super(...)` |
| `Dog is not abstract and does not override abstract method speak() in Animal` | 子类少实现了一个抽象方法或接口方法 | 把那个方法补上，签名照抄 |
| `method does not override or implement a method from a supertype` | 写了 `@Override`，但父类没有签名相同的方法 | 核对方法名、参数类型、参数个数 |
| `variable x might not have been initialized` | 局部变量没赋初值就用 | 声明时给初值，如 `int x = 0;` |
| `unreachable statement` | `return` 或 `throw` 后面还有语句 | 删掉后面的语句 |
| `variable x is already defined in method f()` | 同一个方法里重复声明了同名变量 | 改名，或去掉第二次声明前面的类型 |
| `cannot assign a value to final variable x` | 给 `final` 字段二次赋值 | 用局部变量，不改这个字段 |

### 3.7 时间快到时的保分做法

1. 先让整个项目能编译。没写完的方法：返回对象的写 `return null;`，返回 `int` 的写 `return 0;`，返回 `boolean` 的写 `return false;`，`void` 的留空。写了一半又编译不过的代码整段注释掉。
2. 立刻 `git add -A`、`git commit -m "wip"`、`git push`，按 2.13 确认。
3. 然后再继续写。每多通过一个测试就再提交推送一次。
4. 剩 15 分钟时停止开新题，只做编译检查和最后一次推送。

## 4 真题套用

### 4.1 2025 S1 Q1 Task A Kitchen 集合运算

**题意**：原题 [Q1README.md](../../exam-practice-questions/past-exams/2025-1/src/Q1/Q1README.md)。`readyToCook` 看一份菜谱要的食材，手上的 Raw 或 Ready 够不够。`readyToServe` 只算 `ReadyState`。`ingredientsRequired` 把 `orders` 里每份菜谱的需求加总，减去 `availableFood` 里没烧糊的数量，只返回还缺的种类和数量。烧糊的不能用。状态机（Task B）归 [03 设计模式](03-design-patterns-uml.md)。

**套路对应**：3.5。`Set<Food>` 里每个对象算 1 份，`availableFood` 用 Map 的 value 当数量。先按 `Ingredient` 放进 `HashMap`。够不够：遍历菜谱，`getOrDefault(k, 0)` 小于需要的数量就返回 `false`。还缺多少：`缺 = 需要 - 手上`，大于 0 才 `put`。题目只写了可以在 `Food.java` 加辅助方法，所以计数写在这三个方法里面。

**关键代码**（填进 `Kitchen.java`。已跑通 `Q1Test` 的 4 个 Task A 测试）：

```java
public static boolean readyToCook(Recipe recipe, Set<Food> ava) {
    Map<Ingredient, Integer> have = new HashMap<>();
    for (Food food : ava) {                              // Set 里每个对象算 1 份
        if (food.state instanceof RawState || food.state instanceof ReadyState) {
            Ingredient k = food.getIngredient();
            have.put(k, have.getOrDefault(k, 0) + 1);    // 烧糊的不计数
        }
    }
    for (Map.Entry<Ingredient, Integer> e : recipe.getIngredients().entrySet()) {
        if (have.getOrDefault(e.getKey(), 0) < e.getValue()) return false;
    }
    return true;
}
public static boolean readyToServe(Recipe recipe, Set<Food> ava) {
    Map<Ingredient, Integer> have = new HashMap<>();
    for (Food food : ava) {
        if (food.state instanceof ReadyState) {          // 只算 Ready
            Ingredient k = food.getIngredient();
            have.put(k, have.getOrDefault(k, 0) + 1);
        }
    }
    for (Map.Entry<Ingredient, Integer> e : recipe.getIngredients().entrySet()) {
        if (have.getOrDefault(e.getKey(), 0) < e.getValue()) return false;
    }
    return true;
}
public Map<Ingredient, Integer> ingredientsRequired() {
    Map<Ingredient, Integer> need = new HashMap<>();
    for (Recipe order : orders) {                        // 多份订单里同一种食材要加总
        for (Map.Entry<Ingredient, Integer> e : order.getIngredients().entrySet()) {
            need.put(e.getKey(), need.getOrDefault(e.getKey(), 0) + e.getValue());
        }
    }
    Map<Ingredient, Integer> have = new HashMap<>();
    for (Map.Entry<Food, Integer> e : availableFood.entrySet()) {
        if (e.getKey().state instanceof BurntState) continue;
        Ingredient k = e.getKey().getIngredient();
        have.put(k, have.getOrDefault(k, 0) + e.getValue());   // 加的是 Map 里的数量
    }
    Map<Ingredient, Integer> missing = new HashMap<>();
    for (Map.Entry<Ingredient, Integer> e : need.entrySet()) {
        int lack = e.getValue() - have.getOrDefault(e.getKey(), 0);
        if (lack > 0) missing.put(e.getKey(), lack);    // 全够则返回空 Map，不是 null
    }
    return missing;
}
```

**坑**：
- `Food` 没有 `getState()`。状态是同一个包里能读的字段 `food.state`。用 `instanceof`。`== ReadyState.getState()` 会漏掉另外 `new` 出来的状态对象，这三个状态类的构造器是 public。
- `Food` 没有重写 `equals`。`Set` 里同一食材的两份是两个对象，都要数。测试能用 `Set.of` 放进多份 WAGYU，就是因为它们不相等。
- `readyToCook` 把 Raw 和 Ready 都算上。`readyToServe` 不算 Raw。烧糊的两边都不算。
- 一样都不缺时，测试期望 `Map.of()`。留下原来的 `return null` 会失败。
- README 写 `elapsedPrepTime` 为准，括号里限定 for the purpose of the prepare function。上面按当前 `state` 计数，不调用 `checkState`。隐藏测试如果给了状态和时间不一致的 `Food`，先 `checkState` 再计数的结果会不同。不确定。

### 4.2 2024 S1 Q1 子类构造器和循环取名

**题意**：原题 [Q1README.md](../../exam-practice-questions/past-exams/2024-1/src/Q1/Q1README.md)。这里只记四个语法点：子类构造器、`super(...)`、`null` 时的默认值、按名单循环取名。工厂方法和模板方法的完整代码归 [03 设计模式](03-design-patterns-uml.md)。

**套路对应**：3.2 第 2、3 步。父类构造器先看是不是 `null`，再看是不是负数。`DogCreator` 把参数原样交给 `super`，`null` 就留下父类字段上的 0。`PenguinCreator` 的默认值是 age 1、level 2，写在 `super(...)` 的括号里。取名用下标，加一后对 `NAMES.size()` 取余。

**关键代码**（片段。下面的构造器和 `getNextName` 放进原题后，`FactoryTest` 6 个测试通过。Penguin 的默认值、名单转一圈、`static` 下标另写了检查）：

```java
public AnimalCreator(Integer age, Integer level) {
    if (age != null && age < 0) throw new IllegalArgumentException("age < 0");  // 先判 null 再比较
    if (level != null && level < 0) throw new IllegalArgumentException("level < 0");
    if (age != null) this.ageConfig = age;             // null 时保持字段上的 0
    if (level != null) this.levelConfig = level;
}
public DogCreator(Integer age, Integer level) {
    super(age, level);                                 // null 用父类默认值 0
}
public PenguinCreator(Integer age, Integer level) {
    super(age == null ? 1 : age, level == null ? 2 : level);  // 默认 age 1、level 2
}
public String getNextName() {                          // 两个子类同一段
    String name = NAMES.get(nextNameIndex);
    nextNameIndex = (nextNameIndex + 1) % NAMES.size();  // 到末尾回到 0
    return name;
}
```

**坑**：
- 参数是 `Integer`。写成 `age < 0` 时，`null` 会拆箱抛 `NullPointerException`。题目要求 `null` 不抛，负数才抛 `IllegalArgumentException`。0 合法。
- Java 17 里 `super(...)` 必须是第一句。Penguin 的默认值写在括号里。
- Dog 的名单是 Rex、Buddy、Max、Rocky，第五个又是 Rex。Penguin 的名单是 Ace、Alaska、Aurora、Bam-Bam、Banjo。
- `DogCreator.nextNameIndex` 每个对象一份。`PenguinCreator.nextNameIndex` 是 `static`，后创建的企鹅创造器接着往下取名。

### 4.3 2022 S1 Q2 快递公司报价

**题意**：原题 [readme.md](../../exam-practice-questions/past-exams/2022-1/q2/readme.md)。抽象类 `ExpressCompany` 有两个子类 `CompanyA`、`CompanyB`。要写五个方法：`safetyCheckOnItems`（包裹里有违禁物品就抛 `IllegalParcelItemException`）、`calculateDistance`（公司到目的地的欧氏距离，向上取整）、两个子类的 `calculateQuote`（按重量分段计费，距离大于 `deliveryRadius` 时再加「每单位费率乘以距离」）、`QuoteCalculator.bestQuote`（返回报价低的那家，相等选 A）。允许加辅助方法。

**套路对应**：3.2 的分工在这题是：算距离、查违禁品对两家公司都一样，写在父类；`calculateQuote` 两家不同，写在子类，里面直接调用父类的 `calculateDistance` 和字段 `deliveryRadius`。3.3 的检查：遍历包裹里的物品，在禁运表里就 `throw`。分段计费的写法：每个区间一个 `else if`，前面的区间按整段算，当前区间只算超出的部分。

**关键代码**（已跑通 `ParcelTest` 4 个测试）：

```java
// ExpressCompany.java
public void safetyCheckOnItems(Parcel parcel) throws IllegalParcelItemException {
    for (ItemType item : parcel.getItems()) {
        if (illegalItems.contains(item)) {
            throw new IllegalParcelItemException();   // 这个异常类只有无参构造器
        }
    }
}
public int calculateDistance(Parcel parcel) {
    int distance = 0;
    Point dest = parcel.getDestination();
    double dx = location.x - dest.x;                  // Point 的 x、y 是 protected，同一个包里能直接用
    double dy = location.y - dest.y;
    distance = (int) Math.ceil(Math.sqrt(dx * dx + dy * dy));   // 向上取整，再转成 int
    return distance;
}

// CompanyA.java
public double calculateQuote(Parcel parcel) {
    double quote = 0;
    int w = parcel.getWeight();
    int tenths;                                       // 以 0.1 元为单位用整数算，最后再除以 10.0
    if (w <= 20) {
        tenths = w * 13;                              // 费率 1.3
    } else if (w <= 40) {
        tenths = 20 * 13 + (w - 20) * 17;             // 前 20kg 按 1.3，超出部分按 1.7
    } else if (w <= 60) {
        tenths = 20 * 13 + 20 * 17 + (w - 40) * 24;
    } else {
        tenths = 20 * 13 + 20 * 17 + 20 * 24 + (w - 60) * 32;
    }
    int d = calculateDistance(parcel);
    if (d > deliveryRadius) {
        tenths += d * 10;                             // 超出半径：整段距离都收费，每单位 1 元
    }
    quote = tenths / 10.0;
    return quote;
}

// CompanyB.java 的 calculateQuote，只有数字不同
    if (w <= 30) {
        tenths = 460;                                 // 30kg 以内固定 46 元
    } else if (w <= 40) {
        tenths = 460 + (w - 30) * 15;
    } else if (w <= 50) {
        tenths = 460 + 10 * 15 + (w - 40) * 21;
    } else if (w <= 60) {
        tenths = 460 + 10 * 15 + 10 * 21 + (w - 50) * 29;
    } else {
        tenths = 460 + 10 * 15 + 10 * 21 + 10 * 29 + (w - 60) * 36;
    }
    int d = calculateDistance(parcel);
    if (d > deliveryRadius) {
        tenths += d * 11;                             // 每单位 1.1 元
    }
    quote = tenths / 10.0;

// QuoteCalculator.java
public static Quote bestQuote(Parcel parcel, ExpressCompany a, ExpressCompany b) throws IllegalParcelItemException {
    Quote quote = null;
    double qa = a.handle(parcel);                     // handle 先查违禁品，再算报价
    double qb = b.handle(parcel);
    if (qa <= qb) {                                   // 相等时选 A，所以是 <=
        quote = new Quote(a, qa);
    } else {
        quote = new Quote(b, qb);
    }
    return quote;
}
```

**坑**：
- `Quote.equals` 用 `==` 比较两个 `double`。直接写 `w * 1.3` 时，重量 3 算出 `3.9000000000000004`，和期望的 `3.9` 不相等。所以用整数按 0.1 元算，最后除以 `10.0`。我用精确小数对照了重量 1 到 150、距离 0 到 40 的全部组合，整数写法全部一致；直接用 `double` 累加的写法有两千多组不一致。
- 超出半径时收的是整段距离的钱，不是超出的部分（示例测试 1：距离 8、半径 5，加了 8 元）。距离等于半径不加钱（题面写的是 greater than）。
- `bestQuote` 要调 `handle`，直接调 `calculateQuote` 不会做违禁品检查。
- 只有一家公司禁运该物品时怎么办，README 没有明说。上面的写法是任何一家禁运都抛异常（依据：README 写「any items prohibited by the companies」就抛）。不确定。
- B 公司 30kg 以内是固定 46 元，不是重量乘费率。

### 4.4 2022 S1 Q1 check 抛异常

**题意**：原题 [readme.md](../../exam-practice-questions/past-exams/2022-1/q1/readme.md)。`State.check(Character character, Key event)` 在方法开头检查参数。`event` 是 `null` 抛 `NullKeyEventException`，`character` 是 `null` 抛 `NullCharacterException`。状态切换归 [03 设计模式](03-design-patterns-uml.md)，异常测试的写法归 [02 测试](02-testing.md)。

**套路对应**：3.3。两个异常类题目已经给了，都 `extends RuntimeException`，只有无参构造器，写 `new 类名()`。方法签名上没有 `throws`，不要加。检查放在 `YOUR CODE STARTS HERE` 和 `YOUR CODE ENDS HERE` 之间。

**关键代码**（已跑通 `ExceptionTest` 的 2 个测试）：

```java
if (event == null) {
    throw new exceptions.NullKeyEventException();   // README 写的 NullKeyException 不是类名
}
if (character == null) {
    throw new exceptions.NullCharacterException();
}
```

**坑**：
- 类名是 `NullKeyEventException`，文件在 `exceptions` 包里。README 正文写成了 `NullKeyException`，按那个名字写会 `cannot find symbol`。
- 只能改标记区域。用 `exceptions.类名`，就不用在文件开头加 `import`。自己的文件不要加 `package` 声明。
- 两个都是非受检异常。`check` 是接口里的 `default` 方法，不能改签名去加 `throws`。
- 先查 `event` 再查 `character`。两个都是 `null` 时抛 `NullKeyEventException`。题目没说同时为 `null` 怎么算，提供的测试是分开测的。不确定。

### 4.5 2021 S2 Q3 compareTo 按距离排序

**题意**：原题 [readme.md](../../exam-practice-questions/past-exams/2021-2/q3/readme.md)。`Parcel.compareTo` 规定自然顺序：仓库 `[20, 10]` 到收件人的欧氏距离，升序，小的在前。距离相等返回 0。寄件人的位置不参与比较。迭代器归 [03 设计模式](03-design-patterns-uml.md)。

**套路对应**：3.4，按一个 `double` 升序。距离用 `CartesianCoordinate.getDistance`，再 `Double.compare(this的距离, other的距离)`。

**关键代码**（已跑通 `ParcelTreeTest` 的 7 个排序测试）：

```java
CartesianCoordinate warehouse = new CartesianCoordinate(20, 10);
double mine = warehouse.getDistance(getRecipient().getLocation());
double theirs = warehouse.getDistance(other.getRecipient().getLocation());
return Double.compare(mine, theirs);   // 负数：this 更近，排在前面
```

**坑**：
- 只算仓库到收件人。把寄件人到仓库的距离加进去，排序结果会错。
- 不要写 `return (int) (mine - theirs);`。差小于 1 时变成 0。`parcelOrderTest4` 的差距很小，这样写会当成相等。
- 对照排序：题面例子的收件人距离是 130 和 340，130 的包裹在前。`parcelOrderTest6` 把 10 个包裹 `sort(null)` 之后的 id 顺序是 1, 5, 6, 4, 8, 9, 3, 7, 10, 2。按仓库 `(20, 10)` 算出的距离从小到大就是这个顺序。

## 5 易错清单

1. 代码编译不过还交上去。这道题 0 分。没写完的方法先留 `return null;`、`return 0;` 或 `return false;`。
2. 改了方法签名、加了字段、给方法加了 `throws`、改了 `package` 或文件名。
3. 用 `==` 比较字符串或 `Integer`。对 `null` 的 `Integer` 做比较或运算，拆箱抛 `NullPointerException`。
4. `super(...)` 不是构造器的第一句。父类没有无参构造器时，子类构造器漏写 `super(...)`。
5. 调用会抛受检异常的方法，签名又不让加 `throws`，方法里没有 `try/catch`。
6. 用 `map.get(k)` 的结果直接加减。键不存在时得到 `null`。用 `getOrDefault(k, 0)`。
7. 在 for-each 里 `remove`。对 `List.of` 的结果 `add`，或对它 `contains(null)`。
8. `compareTo` 写成减法，或 `(int) (a - b)`。小数差变成 0，很大的 `int` 相减会溢出。
9. 报价用 `double` 直接乘费率。`Quote.equals` 用 `==` 比较两个 `double`。见 4.3。
10. 只 `commit` 没有 `push`。`git status` 出现 `ahead of 'origin/...'` 就是还没推上去。最后要在 GitLab 网页上看到文件。
11. 2025 S1 Q1：烧糊的食材算进可用数量；库存 Map 的 value 当成 1；一个都不缺时返回 `null`。
12. 2022 S1 Q1：按 README 正文抛 `NullKeyException`。类名是 `NullKeyEventException`。
13. 2021 S2 Q3：`compareTo` 把寄件人到仓库的距离也加进去。自然序只看仓库到收件人。
14. 2024 S1 Q1：Penguin 的 `null` 用了父类默认值 0。它的默认值是 age 1、level 2。`nextNameIndex` 是 `static`。

还不能确定的地方：

- 2025 S1 Q1 Task A 要不要先 `checkState` 再计数。见 4.1。提供的测试里状态和时间是一致的，两种做法都过。
- 2022 S1 Q1 的 `character` 和 `event` 同时为 `null` 时先抛哪一个。见 4.4。上面的代码先抛 `NullKeyEventException`。
- 2022 S1 Q2 只有一家公司禁运该物品时是否抛异常。见 4.3。
