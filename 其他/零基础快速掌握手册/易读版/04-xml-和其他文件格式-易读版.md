# 04｜易读版 XML 和其他文件格式

> 根据 `exam-practice-questions` 中的原题编写。只选容易入门的部分：看懂格式、读取简单数据、写出简单 XML、解释不同格式的用途。示例会主动缩小题目规模，不代表整道真题的完整答案。

## 1. 先明白：文件格式到底是什么？

假设程序里有一个人：名字叫 John，年龄是 20。

程序运行时，这些数据可以放在变量里。要让程序关闭之后仍能保留数据，就需要把它保存到文件等存储介质中。这叫 **persistence（持久化）**。

**文件格式，就是大家约定“数据在文件里怎么写”。** 同一个人，可以写成下面三种形式。

**CSV：像一张表，一行一条记录。**

```csv
name,age
John,20
```

第一行告诉你两列分别是什么；第二行才是人的数据。逗号把两列隔开。

**XML：用标签包住数据。**

```xml
<person>
    <name>John</name>
    <age>20</age>
</person>
```

`<name>` 和 `</name>` 把名字夹在中间；`<age>` 和 `</age>` 把年龄夹在中间。

**JSON：用“字段名:值”表示数据。**

```json
{"name": "John", "age": 20}
```

`name` 对应 `John`，`age` 对应 `20`。字符串用双引号；这里的数字 `20` 不加引号。

先记住：**数据没有变，只是写法不同。读取文件，就是把这些写法重新变成程序能使用的数据。**

## 2. 最容易掌握的 XML 考点：标签、属性、文本

下面把 2025 S2 Q4 Task A 的数据缩小，方便看清结构：

```xml
<Locations>
    <State name="Victoria">
        <Population>100</Population>
    </State>
    <State name="Tasmania"/>
</Locations>
```

逐项对齐：

| XML 中的写法 | 术语 | 白话解释 |
|---|---|---|
| `Locations` | 根元素的标签名 | 最外层的大盒子，包住所有地点 |
| `<State ...>...</State>` | element（元素） | 一个地点的小盒子 |
| `State` | tag name（标签名） | 盒子的种类，这里是“州” |
| `name="Victoria"` | attribute（属性） | 写在开始标签里面的信息 |
| `<Population>100</Population>` | child element（子元素） | 州盒子里面的人口盒子 |
| `100` | text content（文本内容） | 人口盒子里的文字 |
| `<State name="Tasmania"/>` | 空元素的简写 | 等价于 `<State name="Tasmania"></State>` |

**最关键的区分：`name` 是属性，`Population` 是子元素，`100` 是文本。它们的读取方式不同。**

### 2.1 为什么说 XML 像一棵树？

因为一个大盒子可以包含多个小盒子：

```text
Locations                         ← 根元素
├── State，属性 name="Victoria"    ← Locations 的直接子元素
│   └── Population，文本 "100"     ← State 的直接子元素
└── State，属性 name="Tasmania"    ← Locations 的直接子元素
```

`Population` 是 `Locations` 的**后代元素**，但不是它的**直接子元素**。中间还隔着一层 `State`。

### 2.2 看格式就能检查的基础规则

1. 整份 XML 只能有一个根元素。多个地点要放进同一个 `<Locations>` 里。
2. 标签名区分大小写：`Population` 和 `population` 不一样。
3. 开始和结束标签要配对：`<age>20</age>`。
4. 嵌套不能交叉：先打开 `person`，再打开 `age`，就要先关闭 `age`，再关闭 `person`。
5. 属性值要加引号，例如 `name="John"`。

```xml
<!-- 正确：先关里面，再关外面 -->
<person><age>20</age></person>
```

## 3. Java 怎么读 XML？先认识这几个工具

**DOM** 在这里可以理解为：Java 先把 XML 文件读进内存，变成一棵可以查询的树。

| Java 名称 | 中文理解 | 在上面的例子中 |
|---|---|---|
| `Document` | 整份 XML 文档 | 整棵树 |
| `Element` | 一个元素 | `State` 或 `Population` 盒子 |
| `Node` | 更一般的“节点” | 可以是元素，也可以是换行等文本节点 |
| `NodeList` | 节点列表 | 一组找到的节点 |

`Node` 的范围比 `Element` 大。**元素是节点，但节点不一定是元素。**

### 3.1 最小完整例子：读出 Victoria 和 100

先把下面内容保存为 `locations.xml`。这里故意只保留一个有完整人口数据的地点。

```xml
<Locations>
    <State name="Victoria">
        <Population>100</Population>
    </State>
</Locations>
```

下面是独立的教学程序，保存为 `ReadXmlDemo.java`。它假设输入就是上面的简单结构。

```java
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ReadXmlDemo {
    public static void main(String[] args) throws Exception {
        // 1. 准备一个能读取 XML 的工具。
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 2. 读取文件，得到内存里的 XML 文档。
        Document doc = builder.parse(new File("locations.xml"));

        // 3. 找到所有标签名为 State 的元素。
        NodeList states = doc.getElementsByTagName("State");

        // 4. 取第一个 State；列表下标从 0 开始。
        Element state = (Element) states.item(0);

        // 5. 从开始标签里面取 name 属性。
        String name = state.getAttribute("name");

        // 6. 在这个 State 里面找 Population 元素。
        NodeList populations = state.getElementsByTagName("Population");
        String text = populations.item(0).getTextContent().trim();

        // 7. 把文字 "100" 转成可以计算的整数 100。
        int population = Integer.parseInt(text);

        System.out.println(name);
        System.out.println(population);
    }
}
```

把两个文件放在同一文件夹，在这个文件夹执行 `javac ReadXmlDemo.java`，再执行 `java ReadXmlDemo`，输出：

```text
Victoria
100
```

这里的 `import` 是告诉 Java 使用哪些现成工具；`main` 是程序的入口。教学程序中的 `throws Exception` 表示把读取失败等异常交给调用方处理。**考试时不能擅自给题目提供的方法加上它**；例如 2025 S2 Q4 明确禁止修改方法签名，并要求出错时返回 `null`。

### 3.2 把核心代码一个词一个词拆开

```java
Element state = (Element) states.item(0);
```

- `states`：刚才找到的 State 节点列表。
- `.item(0)`：取列表第一个节点。
- `(Element)`：把这个节点当作元素来使用；这里按标签查到的节点确实是元素。
- `Element state`：用名为 `state` 的变量保存它。

```java
String text = populations.item(0).getTextContent().trim();
```

从左到右执行：

1. `populations.item(0)` → 找到 `<Population>100</Population>`。
2. `.getTextContent()` → 拿到文字 `"100"`。
3. `.trim()` → 去掉文字两端的空白；若原来是 `" 100 "`，就变成 `"100"`。
4. `String text = ...` → 用变量 `text` 保存这段文字。

```java
int population = Integer.parseInt(text);
```

`Integer.parseInt("100")` 得到整数 `100`。此后 `population + 1` 是 `101`；而字符串 `"100" + 1` 会拼成 `"1001"`。

## 4. 在简单读取上，再掌握三个小变化

### 4.1 多个元素：用循环逐个读

下面接在前面取得 `states` 的代码之后使用：

```java
for (int i = 0; i < states.getLength(); i++) {
    Element state = (Element) states.item(i);
    String name = state.getAttribute("name");
    System.out.println(name);
}
```

假如有 Victoria、Tasmania 两个 State：

| 执行到哪一步 | `i` 的值 | 做什么 |
|---|---|---|
| 第一轮 | `0` | 取第一个 State，打印 Victoria |
| 第二轮 | `1` | 取第二个 State，打印 Tasmania |
| 下一次判断 | `2` | `2 < 2` 不成立，循环结束 |

`getLength()` 是列表长度；`i++` 是把 `i` 加 1。

### 4.2 人口可能没有：先判断，再取值

2025 S2 Q4 Task A 要求：人口缺失或空白时，对应的值为 `null`，**不能自己改成 0**。`0` 表示确定人口是零；`null` 表示这里没有人口数据。

下面是处理一个 `state` 的局部片段，假设它符合题目的平铺结构，至多有一个 Population：

```java
NodeList populations = state.getElementsByTagName("Population");
Integer population = null;

if (populations.getLength() > 0) {
    String text = populations.item(0).getTextContent().trim();
    if (!text.isEmpty()) {
        population = Integer.parseInt(text);
    }
}
```

- `Integer` 可以保存整数，也可以保存 `null`；`int` 不能保存 `null`。
- `getLength() > 0`：确实找到了 Population，才去取第一个。
- `text.isEmpty()`：文字是不是空的；前面的 `!` 表示“不是”。

代入三种输入：

| 输入 | 执行过程 | 最后得到 |
|---|---|---|
| 没有 Population | 列表长度为 0，不进入外层 `if` | `null` |
| `<Population> </Population>` | 去空白后是 `""`，不进入内层 `if` | `null` |
| `<Population>100</Population>` | 找到元素，文字非空，转换成整数 | `100` |

这段只处理“缺失和空白”。如果文字是 `abc`，整数转换会抛出异常；如果出现多个 Population，也需要按原题另做格式检查。

### 4.3 只要直接子元素时，别把所有后代都找出来

`getElementsByTagName("State")` 会向下寻找所有同名后代，**不是只找下一层**。

2025 S2 Q4 Task A 还明确说：地点的标签名不一定叫 `State`。因此，完整答题不能只搜 `State`，应遍历根元素的直接子节点，筛出元素：

```java
Element root = doc.getDocumentElement(); // 拿到最外层 Locations
NodeList children = root.getChildNodes(); // 只拿下一层节点

for (int i = 0; i < children.getLength(); i++) {
    org.w3c.dom.Node node = children.item(i);
    if (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
        continue; // 不是元素就跳过，例如缩进换行产生的文本节点
    }
    Element location = (Element) node;
    String name = location.getAttribute("name");
    System.out.println(name);
}
```

**为什么要筛选？** XML 为了好看写了换行和空格，这些也可能进入节点列表。不能直接把所有 `Node` 都强制转换为 `Element`。

知道这个区别即可。本节没有展开 Task A 的完整格式校验，也不展开 Task B 的递归建树。

## 5. 写 XML：创建盒子 → 放内容 → 接到树上 → 保存

读文件是“文件 → 内存”；写文件是“内存 → 文件”。

下面是独立程序，保存为 `WriteXmlDemo.java`。它把一个人的名字写到 `person.xml`。

```java
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteXmlDemo {
    public static void main(String[] args) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();

        Element person = doc.createElement("person"); // 创建 person 盒子
        doc.appendChild(person);                      // 把它接成根元素

        Element name = doc.createElement("name");     // 创建 name 盒子
        name.setTextContent("John");                  // 把 John 放进去
        person.appendChild(name);                     // 把 name 接到 person 里面

        Transformer writer = TransformerFactory.newInstance().newTransformer();
        writer.transform(new DOMSource(doc), new StreamResult(new File("person.xml")));
    }
}
```

执行 `javac WriteXmlDemo.java`，再执行 `java WriteXmlDemo`，会生成 `person.xml`。忽略 XML 声明和排版空白，其结构是：

```xml
<person><name>John</name></person>
```

最后两行可作为保存文件的固定写法理解：`DOMSource(doc)` 是要保存的文档；`StreamResult(...)` 是输出到哪里；`transform(...)` 执行输出。

**最容易漏的是 `appendChild`。** `createElement` 只是新建一个盒子；还要把它接到文档树上，才能随这棵树一起保存。

如果题目要求的是属性，例如 `<person name="John"/>`，则在保存前使用 `person.setAttribute("name", "John")`，而不是创建 `<name>` 子元素。两种写法不能随意互换，必须照题目给的格式。

## 6. CSV 最容易掌握的考点：分隔符、表头、类型转换

2020 S2 Q3 给的是下面这种格式，**实际分隔符是分号 `;`**：

```text
id;name;age;occupation
33;John;20;Student
```

第一行是 **header（表头）**，说明每一列的含义。读取人的数据时要跳过它，否则把 `"id"` 当整数转换就会报错。

先只练第二行，不急着写整个文件读取程序：

```java
String line = "33;John;20;Student";
String[] parts = line.split(";");

int id = Integer.parseInt(parts[0]);
String name = parts[1];
int age = Integer.parseInt(parts[2]);
String occupation = parts[3];
```

`String[]` 表示一组字符串；`split(";")` 表示“遇到分号就拆开”。拆开之后是：

| 数组位置 | 得到的文字 | 转换后的变量 |
|---|---|---|
| `parts[0]` | `"33"` | 整数 `id = 33` |
| `parts[1]` | `"John"` | 字符串 `name = "John"` |
| `parts[2]` | `"20"` | 整数 `age = 20` |
| `parts[3]` | `"Student"` | 字符串 `occupation = "Student"` |

原题已经提供 `Person` 类，构造方法按 `id, name, age, occupation` 的顺序接收四个值，因此在原题环境中可以接着写：

```java
Person person = new Person(id, name, age, occupation);
```

它表示“用刚才拆出来的四个值创建一个人”。

**做这类基础题，先检查三个地方：有没有跳过表头、分隔符是什么、哪些列需要转成数字。**

这里的 `split` 适用于题目展示的简单记录。遇到带引号、字段内部含分隔符的复杂 CSV，不能把这个写法当作通用解析器。2020 S2 Q3 后续还涉及红黑树和指定遍历顺序，本篇只学习容易掌握的拆行部分。

## 7. 其他格式：先会看、会说用途

2023 S2 Q3 明确列出 JSON、XML、CSV 和 Bespoke。这里先掌握最基本的区别。

| 格式 | 最容易记的特点 | 适合表达什么 |
|---|---|---|
| CSV | 行和列，通常较简洁 | 每条记录有相同字段的表格，例如学生名单 |
| XML | 标签、属性，可以层层包含 | 有层级的数据，例如国家包含州、州包含城市 |
| JSON | 字段名与值，支持对象和数组 | 有结构的数据，例如一个学生及其课程列表 |
| Bespoke | 专门为某个应用约定的格式 | 按该应用自己规定的规则读写 |

**JSON 再认识两个符号就够了：**

```json
{
  "name": "John",
  "courses": ["Java", "Maths"]
}
```

- `{ ... }`：一个对象，可以先理解为“一条包含多个字段的记录”。
- `[ ... ]`：一个数组，可以先理解为“按顺序列出的多个值”。
- `courses` 的值是一组课程，里面有 `Java` 和 `Maths`。

**Bespoke 不代表某个统一的语法。** 例如某应用规定用 `John|20` 保存一个人，这就是一种自定义格式；读取时必须知道 `|` 是分隔符，第一项是名字，第二项是年龄。这是自拟例子，不是原题规定。

### 7.1 真题问“多种格式怎么设计”，最容易理解的回答

不要先背模式名。先想：XML、CSV 和 JSON 的写法不一样，因此读取步骤也不同；但读完以后，我们都希望得到应用能使用的数据。

可以约定所有读取器都有一个 `read` 操作，再分别编写 `XmlReader`、`CsvReader` 和 `JsonReader`。主程序选择合适的读取器，调用相同的操作。

```text
XML 文件  → XmlReader  ┐
CSV 文件  → CsvReader  ├→ 转成应用约定的数据形式
JSON 文件 → JsonReader ┘
```

这种把不同读取方法做成可替换实现的思路，可以用 **Strategy（策略模式）** 表达。选择创建哪个读取器，还可以交给工厂处理。本篇只记住“不同格式各自负责解析、对外使用共同接口”这个核心。

对应 2023 S2 Q3a，可以这样写一段基础回答（这是答题示例，不是官方答案）：

> 定义统一的数据读取接口，让 XML、JSON 和 CSV 读取器分别实现它。主程序通过接口调用读取方法，可以替换不同的解析策略。这属于策略模式的思路。添加新的文件格式时，可以新增读取器实现，减少对已有解析代码的修改，并方便分别测试各个读取器。

这只是基础论述；完整题目仍要按其要求解释具体设计及代码质量收益。

## 8. 最后用六道小题检查自己

**① `<student age="20">John</student>` 中，哪个是标签名、属性、文本？**

答案：标签名是 `student`；属性是 `age="20"`；文本是 `John`。

**② 对上面的 student 元素，`getAttribute("age")` 返回什么？**

答案：字符串 `"20"`，不是整数。需要数字时再调用 `Integer.parseInt`。

**③ 为什么 `item(0)` 前面有时要检查列表长度？**

答案：因为可能没有找到任何节点。空列表的 `item(0)` 返回 `null`，继续调用它的方法会报错。

**④ `getChildNodes()` 得到的每一项都是元素吗？**

答案：不是，可能还包含文本节点，例如换行和缩进。需要检查节点类型。

**⑤ 新建了 `<name>`，为什么保存结果里没有它？**

答案：可能只调用了 `createElement`，却没有用 `appendChild` 把它接到文档树上。

**⑥ `33;John;20;Student` 应使用 `split(",")` 吗？**

答案：不应该。这道题实际使用分号，应写 `split(";")`。

## 9. 对应原题：学完后去看这些位置

以下链接均指向本地题库。先看简单部分，不要求现在完成整道题。

| 原题来源 | 本篇使用的内容 | 暂时不展开的内容 |
|---|---|---|
| [2025 S2 Q4](../../exam-practice-questions/past-exams/2025-2/src/Q4/Q4README.md) | Task A 的属性、人口文本、缺失值、直接子元素 | 完整格式校验、Task B 递归构建层级树 |
| [2020 S2 Q3](../../exam-practice-questions/past-exams/2020-2/Q3/README.md) | 分号分隔的 CSV、表头、XML 输出格式 | 红黑树、从右到左的层序遍历 |
| [q14-xml](../../exam-practice-questions/misc-questions/q14-xml/StudentCollection.java) | XML 属性的 `getAttribute` / `setAttribute` | 可选身高、体重和嵌套课程列表的完整读写 |
| [2023 S2 Q3a](../../exam-practice-questions/past-exams/2023-2/src/Q3/readme_Q3.md) | JSON / XML / CSV / Bespoke，以及多格式读取的基础设计 | Q3b 的 UML 和 SOLID 分析 |

复习顺序：**先分清标签、属性和文本 → 跟着读出一个值 → 学会循环和缺失值 → 看懂简单写入 → 拆 CSV → 记住格式区别。**
