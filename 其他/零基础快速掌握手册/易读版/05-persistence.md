# 05 持久化 XML JSON CSV

> 这个文件管什么题：把文件（XML、JSON、CSV、纯文本）读成 Java 对象，或把 Java 对象按指定格式写回文件；以及选择题里比较几种格式。
> 历年出现：XML 代码题 6 次（2020 S1、2020 S2、2021 S2、2022 S1、2023 S2、2025 S2），读 CSV 1 次（2020 S2），读纯文本 1 次（2023 S1），格式比较选择题 1 次（2025 S1）。JSON 只在散题 q1、q11 出现。今年预测：可能和树或 Tokeniser Parser 合成一道编程题，XML 读取的可能性最大。

## 目录

- [1 先认题型](#1-先认题型)
- [2 零基础速懂](#2-零基础速懂)
  - [2.1 持久化是什么](#21-持久化是什么)
  - [2.2 XML 的四样东西](#22-xml-的四样东西)
  - [2.3 DOM 的四个类型](#23-dom-的四个类型)
  - [2.4 空白文本节点和过滤](#24-空白文本节点和过滤)
  - [2.5 getElementsByTagName 取到所有后代](#25-getelementsbytagname-取到所有后代)
  - [2.6 JSON 和 Gson](#26-json-和-gson)
  - [2.7 CSV 纯文本 Java 序列化](#27-csv-纯文本-java-序列化)
  - [2.8 格式对比表](#28-格式对比表)
- [3 套路](#3-套路)
  - [3.1 XML 读取模板](#31-xml-读取模板)
  - [3.2 递归读嵌套 XML 建对象树](#32-递归读嵌套-xml-建对象树)
  - [3.3 XML 写出模板](#33-xml-写出模板)
  - [3.4 可选字段和出错的处理规则](#34-可选字段和出错的处理规则)
  - [3.5 JSON 读 处理 写模板](#35-json-读-处理-写模板)
  - [3.6 CSV 和纯文本读写模板](#36-csv-和纯文本读写模板)
  - [3.7 选择题比较格式](#37-选择题比较格式)
  - [3.8 时间不够先拿部分分](#38-时间不够先拿部分分)
- [4 真题套用](#4-真题套用)
  - [4.1 2025 S2 Q4 读平铺 XML 和嵌套 XML](#41-2025-s2-q4-读平铺-xml-和嵌套-xml)
  - [4.2 2025 S1 Q4 MCQ7 哪种格式适合 sharding](#42-2025-s1-q4-mcq7-哪种格式适合-sharding)
  - [4.3 2023 S2 Q1 Part 1 TripleReader](#43-2023-s2-q1-part-1-triplereader)
  - [4.4 2023 S1 Q2 Part 1 读 Markdown 文件](#44-2023-s1-q2-part-1-读-markdown-文件)
  - [4.5 2022 S1 Q1 XML read 和 write](#45-2022-s1-q1-xml-read-和-write)
  - [4.6 2021 S2 Q2 Executor 读写 Person 列表](#46-2021-s2-q2-executor-读写-person-列表)
  - [4.7 2020 S2 Q3 读 CSV 再按层序写 XML](#47-2020-s2-q3-读-csv-再按层序写-xml)
  - [4.8 2020 S1 Q4 XMLTable load 和 insert](#48-2020-s1-q4-xmltable-load-和-insert)
  - [4.9 散题 q14 学生列表 XML 读写](#49-散题-q14-学生列表-xml-读写)
  - [4.10 散题 q7 树木列表 XML 读写](#410-散题-q7-树木列表-xml-读写)
  - [4.11 散题 q1 公司 JSON 读入合并写回](#411-散题-q1-公司-json-读入合并写回)
  - [4.12 散题 q11 学院 JSON](#412-散题-q11-学院-json)
- [5 易错清单](#5-易错清单)

## 1 先认题型

| 题面里出现 | 子题型 | 套路 | 最像的真题 |
|---|---|---|---|
| `org.w3c.dom`、`.xml`、根元素下一排同级元素、返回 `Map` 或 `List` | 读平铺 XML | 3.1 | 4.1 Task A、4.3 |
| hierarchical、tree of、`children`、元素套元素且层数不定 | 读嵌套 XML 建对象树 | 3.2 | 4.1 Task B |
| `save`、`write`、`saveToFile`、follow the format of `example.xml` | 写 XML | 3.3 | 4.9、4.5 |
| optional、missing、blank、may not have | 带可选字段的读写 | 3.4，配合 3.1 或 3.3 | 4.1、4.9、4.8 |
| `.json`、`gson`、`loadFromJsonFile`、merge | JSON 读、处理、写 | 3.5 | 4.11 |
| `.csv`、separated by `;`、`.md`、`.txt`、returns them as a String | 读 CSV 或纯文本 | 3.6 | 4.7、4.4 |
| `LOAD x FROM f.xml`、`SAVE x TO f.xml`、`Executor` | 和 Tokeniser Parser 合成一题，Executor 里读写 XML | 3.1 加 3.3；Parser 部分见 [01 Tokeniser Parser](01-tokeniser-parser.md) | 4.6、4.8 |
| 选择题问 which format is suitable | 比较格式 | 3.7 | 4.2 |

## 2 零基础速懂

### 2.1 持久化是什么

持久化（persistence）：把内存里的对象存进文件，程序关掉后数据还在，下次再从文件读回对象。

- 文件到对象：题面叫 load、read、parse。
- 对象到文件：题面叫 save、write、store。

考试只考这两个方向。读的时候还要检查数据：必需的值在不在，数字能不能转换，文件存不存在。

### 2.2 XML 的四样东西

```xml
<?xml version="1.0" encoding="utf-8"?>
<people>
    <person id="7" city="Canberra">
        <name>Ann</name>
        <age>20</age>
    </person>
    <person id="8"/>
</people>
```

| 名称 | 英文 | 上面的例子 |
|---|---|---|
| 元素 | element | `<person> ... </person>`。`person` 是标签名（tag name）。`<person id="8"/>` 是自闭合写法，里面什么都没有 |
| 属性 | attribute | `id="7"`、`city="Canberra"`，写在开始标签里。值永远是字符串 |
| 文本 | text | `Ann`、`20`，夹在开始标签和结束标签中间 |
| 嵌套 | nesting、hierarchy | `name` 写在 `person` 里面，`person` 是父（parent），`name` 是子（child） |

规则：整个文件只有一个最外层元素，叫根元素（root），这里是 `people`。标签名区分大小写。开始标签必须有对应的结束标签，否则是格式损坏（malformed XML），读的时候会抛异常。第一行 `<?xml ... ?>` 是声明，不是元素。

### 2.3 DOM 的四个类型

DOM（Document Object Model）：把整个 XML 文件读进内存，变成一棵树。标准库自带，包名是 `org.w3c.dom`。

| 类型 | 是什么 | 常用方法 |
|---|---|---|
| `Document` | 整份文档 | `getDocumentElement()` 拿根元素；`createElement(tag)` 新建元素；`getElementsByTagName(tag)` |
| `Element` | 一个元素 | `getTagName()`、`getAttribute(name)`、`hasAttribute(name)`、`setAttribute(name, value)`、`getTextContent()`、`setTextContent(text)`、`getChildNodes()`、`appendChild(child)` |
| `Node` | 树上任何一种节点的统称。元素是 `Node`，文本也是 `Node` | `getNodeType()`、`getNodeName()`、`getTextContent()`、`getParentNode()` |
| `NodeList` | 一串 `Node`。它不是 Java 的 `List`，不能用 for each | `getLength()`、`item(i)` |

`NodeList.item(i)` 的类型是 `Node`。确认它是元素之后，用 `(Element)` 强制转换，才能调用 `getAttribute`。

### 2.4 空白文本节点和过滤

标签之间的换行和缩进也算文本节点。2.2 的例子里，`people` 的 `getChildNodes()` 长度是 5，不是 2：

```text
空白文本  person  空白文本  person  空白文本
```

所以遍历孩子时必须先过滤，只留元素：

```java
NodeList kids = root.getChildNodes();
for (int i = 0; i < kids.getLength(); i++) {
    if (kids.item(i).getNodeType() != Node.ELEMENT_NODE) continue;   // 不是元素就跳过
    Element e = (Element) kids.item(i);
}
```

不过滤直接转 `(Element)` 会抛 `ClassCastException`。`getTextContent()` 取到的文本也可能带换行和空格，用之前先 `trim()`。

### 2.5 getElementsByTagName 取到所有后代

`x.getElementsByTagName("City")` 返回 `x` 里面所有层的 `City` 元素，不只是直接孩子。`getElementsByTagName("*")` 返回里面所有层的全部元素。顺序是它们在文件里出现的先后。

| 方法 | 取到什么 | 要不要过滤空白 | 什么时候用 |
|---|---|---|---|
| `getChildNodes()` | 只有直接孩子，含空白文本 | 要 | 标签名任意，或者要保持层级（嵌套结构） |
| `getElementsByTagName(tag)` | 所有后代里标签名等于 `tag` 的元素 | 不要 | 标签名固定，并且这个标签名不会出现在更深的层 |

例：`<Region><City name="Hobart"/></Region>` 放在 `<State>` 里，`state.getElementsByTagName("City")` 会取到 Hobart，虽然它是孙子不是孩子。

### 2.6 JSON 和 Gson

```json
{
  "name": "ACT Software",
  "employees": [
    { "name": "John", "skills": ["Java", "PHP"] }
  ]
}
```

- 对象（object）：`{ }`，里面是 `"键": 值`，用逗号隔开。
- 数组（array）：`[ ]`，里面是一串值。
- 值（value）：字符串、数字、`true`、`false`、`null`、对象、数组。

Gson 是 Google 的 JSON 库，不在标准库里，题目会给 jar。它按字段名自动对应：JSON 的键等于 Java 的字段名，对象对应类，数组对应 `List`。

```java
class Company { String name; List<Employee> employees; }
class Employee { String name; List<String> skills; }
```

`new Gson().fromJson(reader, Company.class)` 把上面的 JSON 变成 `Company` 对象；`gson.toJson(company, writer)` 反过来。不需要 getter、setter。JSON 里没有的键，对应字段是 `null`。

### 2.7 CSV 纯文本 Java 序列化

CSV（comma separated values）：一行一条记录，字段用分隔符隔开（逗号或题目指定的 `;`），第一行常是表头（header）。没有层级，读出来全是字符串。

```text
id;name;age;occupation
33;John;20;Student
```

纯文本：没有固定格式，题目只要求把整个文件读成一个 `String`（2023 S1）。

Java 序列化（serialization）：把对象直接写成二进制。类要写 `implements Serializable`。只考过选择题。

```java
try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("p.ser"))) {
    out.writeObject(person);                         // 写
}
try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("p.ser"))) {
    Person p = (Person) in.readObject();             // 读，要强制转换
}
```

### 2.8 格式对比表

| | XML | JSON | CSV | Java 序列化 |
|---|---|---|---|---|
| 有没有层级（hierarchy） | 有 | 有 | 没有，一行一条记录 | 有，整个对象连同它引用的对象 |
| 人能不能直接读 | 能，但啰嗦 | 能 | 能 | 不能，是二进制 |
| 体积 | 最大，标签成对出现 | 中等 | 文本格式里最小 | 中等，带类的信息 |
| 需要的库 | 标准库 `org.w3c.dom`、`javax.xml` | 标准库没有，要 Gson 或 json-simple | 不需要，`split` 即可 | 标准库 `java.io` |
| 数据类型 | 全是字符串，自己转换 | 字符串、数字、布尔、`null` | 全是字符串，自己转换 | 保留 Java 类型 |
| 别的语言能不能读 | 能 | 能 | 能 | 只有 Java |
| 能不能从中间切成几个文件 | 不能，切开后标签不成对 | 不能，切开后括号不成对 | 能，按行切 | 不能直接切 |
| 典型用途 | 配置文件、文档、Android 的资源和布局 | Web 接口、配置文件 | 表格数据、导出 | 同一个 Java 程序临时保存对象 |

## 3 套路

### 3.1 XML 读取模板

适用：根元素下面是一排同级元素，每个元素变成一个对象或 `Map` 里的一项。

需要的 import（读和写都在这里，整段复制到文件顶部）：

```java
import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
```

步骤：

1. 整个方法体包在 `try { } catch (Exception e) { return null; }` 里。
2. `File` 读成 `Document`，拿根元素。
3. 新建要返回的集合。
4. 遍历根的直接孩子，跳过不是元素的节点。
5. 对每个元素：读属性，读子元素文本，转数字，建对象，放进集合。
6. 返回集合。

模板（方法片段，放进题目的类里；假设题目给了 `Person(String id, String name, Integer age)`，XML 是 2.2 的例子）：

```java
public static List<Person> load(File f) {
    try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f); // 文件读成 DOM 树
        Element root = doc.getDocumentElement();                  // 根元素
        if (!root.getTagName().equals("people")) return null;     // 题目规定了根标签名才写这行
        List<Person> result = new ArrayList<>();
        NodeList kids = root.getChildNodes();                     // 根的直接孩子
        for (int i = 0; i < kids.getLength(); i++) {
            if (kids.item(i).getNodeType() != Node.ELEMENT_NODE) continue;   // 跳过空白文本
            Element e = (Element) kids.item(i);
            if (!e.hasAttribute("id")) return null;               // 必需属性缺失，按题目写 return null 或 continue
            String id = e.getAttribute("id");                     // 读属性
            String name = childText(e, "name");                   // 读子元素文本，没有则是 null
            String ageText = childText(e, "age");
            Integer age = (ageText == null || ageText.isEmpty()) ? null : Integer.parseInt(ageText); // 可选数字
            result.add(new Person(id, name, age));
        }
        return result;                                            // 没有孩子时返回空集合，不是 null
    } catch (Exception ex) {                                      // 文件不存在、XML 损坏、数字非法都到这里
        return null;
    }
}

// 取 parent 的直接子元素 tag 的文本，已去掉首尾空白；没有这个子元素返回 null
private static String childText(Element parent, String tag) {
    NodeList kids = parent.getChildNodes();
    for (int i = 0; i < kids.getLength(); i++) {
        Node n = kids.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(tag)) return n.getTextContent().trim();
    }
    return null;
}
```

题目不允许加辅助方法时，把 `childText(e, "age")` 换成下面两行（片段）：

```java
NodeList l = e.getElementsByTagName("age");
String ageText = l.getLength() == 0 ? null : l.item(0).getTextContent().trim();
```

常用写法速查：

| 要做的事 | 写法 |
|---|---|
| 读属性 | `e.getAttribute("id")`。属性不存在时得到空串 `""`，不是 `null` |
| 判断属性在不在 | `e.hasAttribute("id")` |
| 读标签名 | `e.getTagName()` |
| 读元素里的文本 | `e.getTextContent().trim()` |
| 文本转数字 | `Integer.parseInt(s.trim())`、`Double.parseDouble(s.trim())` |
| 标签名固定，直接取全部 | `doc.getElementsByTagName("person")`，结果全是元素，不用过滤 |
| 参数是 `String` 路径 | `parse(new File(path))`。不要写 `parse(path)`，路径里有空格或中文时它会失败 |
| 方法签名不能加 `throws` | `parse` 会抛受检异常，所以必须用 try catch 包住。出错返回什么看题目：`null`、空集合，或只打印 |

### 3.2 递归读嵌套 XML 建对象树

适用：元素里面还有同类元素，层数不定，每个元素变成一个带 `children` 的对象。

步骤：

1. 写一个辅助方法 `build(Element e)`，负责把一个元素变成一个对象。
2. 在里面先用 `e` 的标签名和属性建出当前对象。
3. 遍历 `e` 的直接子元素，对每个孩子调用 `build`，结果挂到当前对象的 `children` 上。
4. 主方法按 3.1 拿到根，对根的每个直接子元素调用 `build`，结果放进要返回的集合。

模板（方法片段；假设题目给了 `Item(String type, String name)` 和字段 `children`）：

```java
private static Item build(Element e) {
    if (!e.hasAttribute("name")) return null;                     // 不合格：这个元素和它的后代都不要
    Item item = new Item(e.getTagName(), e.getAttribute("name")); // 先建自己
    NodeList kids = e.getChildNodes();
    for (int i = 0; i < kids.getLength(); i++) {                  // 再处理每个直接子元素
        if (kids.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
        Item child = build((Element) kids.item(i));               // 递归
        if (child != null) item.children.add(child);              // 挂到自己下面
    }
    return item;
}
```

题目不允许加辅助方法时，用 `getElementsByTagName("*")` 加 `getParentNode()`。前者按文件里出现的顺序返回所有后代元素，父亲一定排在孩子前面，所以用一个 `Map` 记住「元素对应哪个对象」就能找到父对象（片段，放在主方法的 try 里，`root` 是根元素，`result` 是要返回的集合）：

```java
Map<Node, Item> made = new HashMap<>();                 // 元素 对应 已建好的对象
NodeList all = root.getElementsByTagName("*");
for (int i = 0; i < all.getLength(); i++) {
    Element e = (Element) all.item(i);
    Node parent = e.getParentNode();
    if (parent != root && !made.containsKey(parent)) continue;   // 父亲被跳过了，自己也跳过
    if (!e.hasAttribute("name")) continue;                       // 自己不合格
    Item item = new Item(e.getTagName(), e.getAttribute("name"));
    made.put(e, item);
    if (parent == root) result.add(item);                        // 顶层
    else made.get(parent).children.add(item);                    // 挂到父对象下面
}
```

### 3.3 XML 写出模板

适用：把一个对象列表写成 XML 文件。

步骤：

1. 新建空 `Document`。
2. 建根元素，挂到 `doc` 上。
3. 每个对象建一个元素，挂到根上；属性用 `setAttribute`，子元素用 `createElement` 加 `setTextContent` 加 `appendChild`。
4. 值是 `null` 的可选字段不建元素。
5. 用 `Transformer` 写到文件。

模板（方法片段，import 见 3.1；写出的就是 2.2 的格式）：

```java
public static void save(List<Person> people, File f) {
    try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(); // 空文档
        Element root = doc.createElement("people");       // 根元素
        doc.appendChild(root);                            // 根要挂到 doc 上
        for (Person p : people) {
            Element pe = doc.createElement("person");     // 每个对象一个元素
            root.appendChild(pe);
            pe.setAttribute("id", p.id);                  // 属性，值必须是 String
            Element name = doc.createElement("name");     // 子元素加文本，三行一组
            name.setTextContent(p.name);
            pe.appendChild(name);
            if (p.age != null) {                          // 可选字段是 null 就不建
                Element age = doc.createElement("age");
                age.setTextContent(String.valueOf(p.age)); // 数字先转 String
                pe.appendChild(age);
            }
        }
        Transformer t = TransformerFactory.newInstance().newTransformer();   // 写文件，固定四行
        t.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        t.setOutputProperty(OutputKeys.INDENT, "yes");    // 换行并缩进
        t.transform(new DOMSource(doc), new StreamResult(f));
    } catch (Exception e) {
        e.printStackTrace();                              // 返回类型是 void，出错只打印
    }
}
```

要点：

- 元素只能用 `doc.createElement` 建，不能 `new Element()`。建完必须 `appendChild` 挂上去，否则不会出现在文件里。
- 标签名、属性名、大小写、顺序照题目给的样例文件写。
- 老题常把建 `Document` 和 `Transformer` 的代码给好，只让你填中间第 2 到 4 步（4.5、4.6）。

### 3.4 可选字段和出错的处理规则

| 情况 | 做法 |
|---|---|
| 可选子元素不存在 | 字段留 `null`（3.1 的 `childText` 返回 `null`） |
| 可选子元素存在，但文本为空或只有空白 | `trim()` 后 `isEmpty()`，字段设 `null` |
| 可选属性不存在或只有空白 | `getAttribute` 得到 `""`，`trim()` 后 `isEmpty()`，字段设 `null` |
| 必需属性不存在 | 看题目原话。写 skip 就 `continue` 或返回 `null` 让上层不挂它，后代也一起不要；写 must have 且出错返回 `null`，就整体 `return null` |
| 数字文本不是数字 | `Integer.parseInt` 抛 `NumberFormatException`，被外层 catch 接住，整体返回 `null` |
| 文件不存在、路径是 `null`、XML 损坏 | `parse` 抛异常，被外层 catch 接住，整体返回 `null` |
| 文件合法但里面没有记录 | 返回空集合，不是 `null` |
| 多余的标签和属性 | 题目说 ignore 就不管；没说时也不管 |
| 写出时字段是 `null` | 不建这个元素、不设这个属性。不要写出字符串 `"null"` |
| 读回来要和原对象 `equals` | 写的时候没建的字段，读的时候保持 `null`，不要变成 `""`、`0` 或空列表 |

判断空白的固定写法：`String s = text.trim(); Integer v = s.isEmpty() ? null : Integer.parseInt(s);`

### 3.5 JSON 读 处理 写模板

适用：题目给了 Gson 的 jar，要读 JSON、合并重名记录、写回。

步骤：

1. 确认类的字段名和 JSON 的键一样（题目一般已写好）。
2. 读：`new Gson().fromJson(reader, 类名.class)`。
3. 处理：用 `LinkedHashMap` 按名字合并，保持第一次出现的顺序。
4. 写：`toJson(对象, writer)`。样例文件有换行缩进时，用 `new GsonBuilder().setPrettyPrinting().create()`。
5. 读和写都用 try with resources，保证文件被关闭；不关闭，写出的文件可能是空的。

模板（方法片段，放进 `Company` 类；`Employee(String name, List<String> skills)` 由题目给出）：

```java
// 文件顶部加：import com.google.gson.Gson; import com.google.gson.GsonBuilder; import java.io.*; import java.util.*;
public static Company loadFromJsonFile(File file) {
    try (FileReader r = new FileReader(file)) {
        return new Gson().fromJson(r, Company.class);            // JSON 变对象
    } catch (Exception e) {
        return null;
    }
}

public void mergeEmployees() {
    Map<String, Employee> byName = new LinkedHashMap<>();        // 名字 对应 合并后的那一条
    for (Employee e : employees) {
        Employee kept = byName.get(e.getName());
        if (kept == null) {                                      // 第一次见这个名字
            kept = new Employee(e.getName(), new ArrayList<>());
            byName.put(e.getName(), kept);
        }
        for (String s : e.getSkills()) {
            if (!kept.getSkills().contains(s)) kept.getSkills().add(s);   // 去重，保持出现顺序
        }
    }
    employees = new ArrayList<>(byName.values());
}

public void saveToJsonFile(File file) {
    try (FileWriter w = new FileWriter(file)) {
        new GsonBuilder().setPrettyPrinting().create().toJson(this, w);   // 对象变 JSON，带缩进
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

补充：

- 最外层是数组时：`List<Person> list = new Gson().fromJson(r, new com.google.gson.reflect.TypeToken<List<Person>>(){}.getType());`
- import 报红说明 jar 没加进项目：IntelliJ 里右键 jar，选 Add as Library。
- json-simple 是另一个 JSON 库（2025 S2 仓库里的 `json-simple-1.1.jar`）。它不会自动对应到类，`new JSONParser().parse(reader)` 得到 `JSONObject`（当作 `Map` 用）和 `JSONArray`（当作 `List` 用），要自己 `get("name")` 再强制转换。有 Gson 时用 Gson。

### 3.6 CSV 和纯文本读写模板

适用：逐行读 CSV 建对象；把整个文件读成 `String`；写文本文件。

逐行读 CSV（片段；import `java.io.*`）：

```java
List<Person> list = new ArrayList<>();
try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
    br.readLine();                                   // 跳过表头；没有表头就删掉这行
    String line;
    while ((line = br.readLine()) != null) {         // 读到文件末尾得到 null
        if (line.trim().isEmpty()) continue;         // 跳过空行
        String[] p = line.split(";", -1);            // 分隔符照题目；-1 表示保留末尾的空字段
        list.add(new Person(Integer.parseInt(p[0].trim()), p[1].trim(), Integer.parseInt(p[2].trim()), p[3].trim()));
    }
} catch (Exception e) {
    e.printStackTrace();                             // 题目要求出错返回 null 时改成 return null;
}
```

把整个文件读成 `String`，两种写法（片段）：

```java
// 写法一：一行。import java.nio.file.Files; import java.nio.file.Path; 内容原样返回
try { return Files.readString(Path.of(filePath)); } catch (Exception e) { return null; }

// 写法二：逐行读，每行后面补系统换行符
StringBuilder sb = new StringBuilder();
try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
    String line;
    while ((line = br.readLine()) != null) sb.append(line).append(System.lineSeparator());
} catch (Exception e) {
    return null;
}
return sb.toString();
```

写文本文件（片段）：

```java
try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
    out.println("id;name;age");                      // 一次写一行
} catch (Exception e) {
    e.printStackTrace();
}
```

`split` 的参数是正则表达式：

| 分隔符 | 写法 |
|---|---|
| 逗号、分号 | `split(",")`、`split(";")` |
| 竖线、点 | `split("\\|")`、`split("\\.")`，要加两个反斜杠 |
| 一个或多个空白 | `split("\\s+")` |

### 3.7 选择题比较格式

步骤：

1. 找出题目要的性质：能不能切分、要不要层级、人要不要读、别的语言要不要读、体积。
2. 对照 2.8 的表选格式。
3. 检查选项里的理由。理由是编出来的功能或数字，这个选项就是错的，例如「Gson 原生支持某功能」「对象小于 128 字节才行」。

| 题目要的性质 | 选 |
|---|---|
| 数据是一条条独立记录，要切分、追加、体积小 | CSV |
| 数据有层级，要跨语言，人要能读 | XML 或 JSON |
| 只在 Java 程序内部用，要保留对象的类型 | Java 序列化 |
| 要带属性、要和 Android 资源一致 | XML |

### 3.8 时间不够先拿部分分

1. 先保证能编译：方法里至少有一个 `return null;` 或 `return 空集合;`。不能编译整题零分。
2. 写上 try catch、`parse`、拿根元素、返回空集合。文件不存在和 XML 损坏的测试这时已经能过。
3. 写最普通的情况：每个元素都有全部字段。
4. 再加可选字段和空白的判断（3.4）。
5. 嵌套题先只建顶层对象（不递归），顶层的测试能过；然后再加递归。
6. 写 XML 的题先保证文件能生成（给的测试常常只检查文件存在），再对照样例调标签名和顺序。
7. 每加一步就跑一次题目给的测试。

## 4 真题套用

### 4.1 2025 S2 Q4 读平铺 XML 和嵌套 XML

**题意**：原题 [Q4README.md](../../exam-practice-questions/past-exams/2025-2/src/Q4/Q4README.md)，只改 [LocationLoader.java](../../exam-practice-questions/past-exams/2025-2/src/Q4/LocationLoader.java)。Task A：根 `<Locations>` 的每个直接子元素是一个地点，标签名任意，有 `name` 属性，里面可以有一个 `<Population>` 整数；返回 `Map<String, Integer>`（地名对应人口），`<Population>` 缺失或空白时值是 `null`。Task B：元素层层嵌套，标签名是地点类型，`name` 属性是名字，`population` 属性可选；返回顶层 `Location` 的 `Set`，每个 `Location` 的 `children` 里挂好它的子地点。两个任务出错都返回 `null`。可以 import、可以加辅助方法，不能加字段、不能改签名、不能加 `throws`。

**套路对应**：

- Task A 用 3.1。集合是 `HashMap`；必需属性是 `name`；可选数字来自子元素 `<Population>`，按 3.4 处理空白。
- Task B 用 3.2。`build` 在这里叫 `readLocation`；类型是 `e.getTagName()`；`population` 是可选属性，按 3.4 处理；缺 `name` 返回 `null`，上层不挂它，它的后代也就一起跳过。
- 两个方法都整体包在 try catch 里，catch 里 `return null`。

**关键代码**（Q4Test 3 个测试通过，另外自测了 README 的各条规则）：

```java
// 文件顶部加 import
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public static Map<String, Integer> loadPopulationInfo(File f) {
    try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
        Element root = doc.getDocumentElement();
        if (!root.getTagName().equals("Locations")) return null;       // 根不对，格式错误
        Map<String, Integer> result = new HashMap<>();
        NodeList kids = root.getChildNodes();
        for (int i = 0; i < kids.getLength(); i++) {
            if (kids.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element loc = (Element) kids.item(i);                      // 标签名任意，所以不检查标签名
            if (!loc.hasAttribute("name")) return null;                // must have name，按格式错误处理
            Integer population = null;
            int count = 0;
            NodeList inner = loc.getChildNodes();
            for (int j = 0; j < inner.getLength(); j++) {
                if (inner.item(j).getNodeType() != Node.ELEMENT_NODE) continue;
                Element e = (Element) inner.item(j);
                if (!e.getTagName().equals("Population")) continue;    // 别的标签不管
                count++;
                if (count > 1) return null;                            // 最多一个 Population
                String text = e.getTextContent().trim();
                if (!text.isEmpty()) population = Integer.parseInt(text);   // 空白保持 null
            }
            result.put(loc.getAttribute("name"), population);
        }
        return result;
    } catch (Exception e) {
        return null;
    }
}

public static Set<Location> loadLocations(File f) {
    try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
        Element root = doc.getDocumentElement();
        if (!root.getTagName().equals("Locations")) return null;
        Set<Location> result = new LinkedHashSet<>();
        NodeList kids = root.getChildNodes();
        for (int i = 0; i < kids.getLength(); i++) {
            if (kids.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Location loc = readLocation((Element) kids.item(i));
            if (loc != null) result.add(loc);
        }
        return result;
    } catch (Exception e) {
        return null;
    }
}

private static Location readLocation(Element e) {                      // 辅助方法，不是字段，题目允许
    if (!e.hasAttribute("name")) return null;                          // 缺 name：跳过整个元素
    Location loc = new Location(e.getTagName(), e.getAttribute("name"));
    String p = e.getAttribute("population").trim();                    // 属性不存在时是 ""
    if (!p.isEmpty()) loc.population = Integer.parseInt(p);            // 缺失或空白保持 null
    NodeList kids = e.getChildNodes();
    for (int i = 0; i < kids.getLength(); i++) {
        if (kids.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
        Location child = readLocation((Element) kids.item(i));
        if (child != null) loc.addChild(child);
    }
    return loc;
}
```

**坑**：

- Task A 的值可以是 `null`，所以用 `HashMap`。`Map.of(...)` 不允许 `null` 值。
- Task A 不能写 `getElementsByTagName("State")`，标签名任意。Task B 不能用 `getElementsByTagName` 直接当孩子，它会把孙子也取出来。
- 缺 `name` 在两个任务里处理不同：Task B 的 README 明说跳过；Task A 只说 must have，上面按格式错误返回 `null`。这一条 README 没写清，不确定，评分也可能按跳过算。
- 同样不确定的还有：`<Population>` 出现两个（上面返回 `null`）、里面不是整数（返回 `null`）、地点里有别的标签（忽略）、Task B 的 `population` 属性不是整数（返回 `null`）。
- XML 损坏时控制台会打印一行 `[Fatal Error]`，这是解析器自己打印的，不影响返回 `null`。
- 测试用相对路径 `src/Q4/data/`，要从项目根目录运行。

### 4.2 2025 S1 Q4 MCQ7 哪种格式适合 sharding

**题意**：原题 [MC_Questions_2025_S1.pdf](../../exam-practice-questions/past-exams/2025-1/src/Q4/MC_Questions_2025_S1.pdf) 第 7 题，单选。sharding 指把一个大文件拆成很多小文件，各自可以并发访问，例如 10000 条记录拆成 10 个文件。问哪种格式适合。

**套路对应**：3.7。要的性质是「能从中间切开」，查 2.8 的表得到 CSV；再逐个检查选项的理由。

**推导**：

- a. CSV 适合，因为没有层级，解析器不需要知道一个分片开头处有哪些标签还没关闭。理由成立：CSV 一行一条记录，从任意两行之间切开，每一片仍是合法 CSV。
- b. XML 适合，因为属性可以标出分片的起止。属性只是元素上的数据，切开后标签不成对，每一片都不是合法 XML。错。
- c. JSON 适合，因为 Gson 原生支持 sharding。Gson 没有这个功能，是编出来的。错。
- d. 序列化适合，但对象要小于 128 字节。Java 序列化没有 128 字节的限制，是编出来的。错。

答案：a（非官方答案）。

**坑**：b、c、d 的句式都是「某格式适合，因为某功能」，要判断的是理由真假，不是格式好坏。

### 4.3 2023 S2 Q1 Part 1 TripleReader

**题意**：原题 [readme_Q1.md](../../exam-practice-questions/past-exams/2023-2/src/Q1/readme_Q1.md)，改 [TripleReader.java](../../exam-practice-questions/past-exams/2023-2/src/Q1/Triples/TripleReader.java)。XML 根是 `<triples>`，每个 `<triple>` 里有 `<subject>`、`<predicate>`、`<object>` 三个子元素。`read(String filePath)` 返回 `Set<Triple>`；不能抛异常，出异常时返回 `null`。题目保证输入合法。Tokenizer 和 Parser 部分见 [01 Tokeniser Parser](01-tokeniser-parser.md)。

**套路对应**：3.1 的简化版。标签名固定且不嵌套，所以用 `getElementsByTagName("triple")` 直接取全部，不用过滤空白；三个子元素也用 `getElementsByTagName(...).item(0)` 取文本；集合是 `HashSet`。

**关键代码**（TripleReaderTest 4 个测试通过；原文件已有全部 import）：

```java
public static Set<Triple> read(String filePath) {
    try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new java.io.File(filePath));                 // String 路径先包成 File
        Set<Triple> triples = new HashSet<>();
        NodeList list = doc.getElementsByTagName("triple");
        for (int i = 0; i < list.getLength(); i++) {
            Element t = (Element) list.item(i);
            String s = t.getElementsByTagName("subject").item(0).getTextContent().trim();
            String p = t.getElementsByTagName("predicate").item(0).getTextContent().trim();
            String o = t.getElementsByTagName("object").item(0).getTextContent().trim();
            triples.add(new Triple(
                    new TripleComponent(TripleComponent.TripleComponentType.SUBJECT, s),
                    new TripleComponent(TripleComponent.TripleComponentType.PREDICATE, p),
                    new TripleComponent(TripleComponent.TripleComponentType.OBJECT, o)));
        }
        return triples;
    } catch (Exception e) {
        return null;
    }
}
```

**坑**：

- 每个 `TripleComponent` 要用两个参数的构造器并传对类型。`equals` 比较的是带类型的字符串，用单参数构造器建出来的对象和测试里的不相等。
- `parse(filePath)` 把字符串当成网址，路径里有空格时抛 `MalformedURLException`（实测），结果是返回 `null`。写 `parse(new java.io.File(filePath))`。
- XML 里的 `type="STRING"` 属性用不到。
- 根标签 `triples` 和记录标签 `triple` 只差一个字母，`getElementsByTagName` 是精确匹配，不会混。

### 4.4 2023 S1 Q2 Part 1 读 Markdown 文件

**题意**：原题 [Q2README.md](../../exam-practice-questions/past-exams/2023-1/Q2/Q2README.md)，改 [MDFileReader.java](../../exam-practice-questions/past-exams/2023-1/Q2/MDFileReader.java)。`read(String filePath)` 把文件内容读成一个 `String` 返回。题面提示可以用 `java.io`。Tokenizer、Parser、多叉树部分见 [01 Tokeniser Parser](01-tokeniser-parser.md) 和 [04 树](04-trees.md)。

**套路对应**：3.6「把整个文件读成 String」的写法二。

**关键代码**（MDFileReaderTest 1 个测试通过；5 个数据文件的结果和 `Files.readString` 相同）：

```java
public static String read(String filePath)  {
    StringBuilder sb = new StringBuilder();
    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(System.lineSeparator());     // 每行后面补换行，最后一行也补
        }
    } catch (Exception e) {
        return null;
    }
    return sb.toString();
}
```

**坑**：

- 给的测试期望每一行后面都是 `System.lineSeparator()`。写法二在 Windows 上也满足；写法一 `Files.readString` 原样返回文件内容，文件里是 `\n` 而系统换行是 `\r\n` 时会和测试不一致。
- `readLine()` 返回的行不带换行符，不补就全部连成一行，后面按行分词会错。
- 不要对内容 `trim()`，行首的空格表示缩进层级。
- 文件第一行是 `package Q2;`，不能删。测试里的路径 `Q2/data/data0A.md` 是相对路径，README 说可以按自己的环境改。

### 4.5 2022 S1 Q1 XML read 和 write

**题意**：原题 [readme.md](../../exam-practice-questions/past-exams/2022-1/q1/readme.md)，改 [XMLProcessor.java](../../exam-practice-questions/past-exams/2022-1/q1/XMLProcessor.java)。`example.xml` 根是 `<States>`，每个子元素的标签名是按键、文本是状态，例如 `<DOWN>LieProneState</DOWN>`。`read` 读出这些对；`write(fileName, keys, states)` 按相同下标写回。状态模式见 [03 设计模式 UML](03-design-patterns-uml.md)。

**套路对应**：读用 3.1，写用 3.3。建 `Document`、`Transformer`、文件不存在时返回空列表，题目都写好了，只填中间。

**关键代码**（片段）：

```java
List<String> pair = Arrays.asList(e.getTagName(), e.getTextContent().trim());
result.add(pair);
// write：createElement(keys.get(i))，setTextContent(states.get(i))，挂到根 States
```

**坑**：文件不存在时骨架已经返回空列表，不要改成 `null`。`read` 的返回类型是 `List<List<String>>`，题目没有测试，内层是「每个元素一对」还是「两个列表：全部按键、全部状态」不确定。上面按一对来写。标签名就是按键，不要再包一层固定标签。

### 4.6 2021 S2 Q2 Executor 读写 Person 列表

**题意**：原题 [README.md](../../exam-practice-questions/past-exams/2021-2/q2/README.md)，改 [Executor.java](../../exam-practice-questions/past-exams/2021-2/q2/Executor.java) 的 `loadFrom` 和 `saveTo`。根 `<persons>`，每个 `<person>` 里是 `<name>` `<gender>` `<age>` `<occupation>`，标签名用 `Person` 的 `KEY_*`。Parser 见 [01 Tokeniser Parser](01-tokeniser-parser.md)。

**套路对应**：读用 3.1，写用 3.3。样板已写好。`age` 是 `int`，用 `Integer.parseInt`。读完放进方法里已有的 `persons`，后面的 `db.save` 会把它存进数据库。

**关键代码**（片段）：

```java
Element root = doc.createElement(Person.KEY_ROOT);
doc.appendChild(root);
Element p = doc.createElement(Person.KEY_ELEMENT);
// 四个子元素 setTextContent；读的时候 getElementsByTagName(Person.KEY_AGE).item(0)
```

**坑**：文件不存在时 `loadFrom` 直接 return，不会 `db.save`。给的测试只检查文件在不在和对象 `equals`，不检查缩进。`INDENT` 那行可以不打开。根必须 `appendChild` 到 `doc`，否则文件是空的。

### 4.7 2020 S2 Q3 读 CSV 再按层序写 XML

**题意**：原题 [README.md](../../exam-practice-questions/past-exams/2020-2/Q3/README.md)，改 [Application.java](../../exam-practice-questions/past-exams/2020-2/Q3/Application.java)。`readCsv` 读分号 CSV（第一行是表头）得到 `Person` 列表；`createTree` 按 id 插入红黑树；`saveXML` 按层序写到固定文件名 `people.xml`。层序见 [04 树](04-trees.md)。

**套路对应**：读用 3.6，写用 3.3。顺序用 `tree.levelTraversal()` 的结果，不要按 CSV 原顺序。`createTree(null)` 时不要遍历，骨架会返回空树，测试允许。

**关键代码**（片段）：

```java
String[] f = line.split(";", -1);
list.add(new Person(Integer.valueOf(f[0].trim()), f[1].trim(), Integer.valueOf(f[2].trim()), f[3].trim()));
// XML 根 people，每个 person 下 id、name、age、occupation，全小写
```

**坑**：先 `readLine()` 丢掉表头。测试文件末尾有空行，空行要跳过，否则 `parseInt` 抛异常。文件名必须是 `people.xml`。样例从右到左的层序是 33、88、11、43、16、10。

### 4.8 2020 S1 Q4 XMLTable load 和 insert

**题意**：原题 [XMLTable.java](../../exam-practice-questions/past-exams/2020-1/Q4/XMLTable.java)。`save` 已经写好。`load` 读回 `Customer`；`insert` 再插入一条。每人必有 `ID`，`Name`、`Address`、`City`、`Postcode`、`Country` 可以没有。标签名用 `Customer.KEY_*`。文件名是表名加 `.xml`。Parser 见 [01 Tokeniser Parser](01-tokeniser-parser.md)。

**套路对应**：读用 3.1 加 3.4。`insert` 就是 load、add、再 save。`parse` 会抛受检异常，方法签名不能加 `throws`，所以 try catch 包住。

**关键代码**（片段）：

```java
List<Customer> all = load(tableName);
all.add(customer);
save(tableName, all);
// load：某个可选标签的 NodeList 长度是 0 就不要 set，字段保持 null
```

**坑**：文件不存在时骨架返回空列表。缺字段必须保持 `null`，写成 `""` 会和测试里的 `equals` 不符。`getElementsByTagName("Customer")` 不会匹配根 `Customers`。

### 4.9 散题 q14 学生列表 XML 读写

**题意**：原题 [StudentCollection.java](../../exam-practice-questions/misc-questions/q14-xml/StudentCollection.java)。`saveToFile` 把学生列表写成 XML，`loadFromFile` 再读回来，和原对象 `equals`。`name`、`age` 是属性。可选的是 `height`、`weight`、`courses`。`courses` 里每个 `course` 有 `name` 属性和成绩文本。可以缺其中几项。

**套路对应**：写用 3.3，读用 3.1，可选用 3.4。`null` 的 height、weight、courses 不建元素。读的时候没有这个元素，字段保持 `null`，不要变成 `0` 或空列表。

**关键代码**（StudentsTest 5 个测试通过。import 用 3.1 那一组）：

```java
public void saveToFile(File file) {
    try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement("students");
        doc.appendChild(root);
        for (Student s : students) {
            Element se = doc.createElement("student");
            root.appendChild(se);
            se.setAttribute("name", s.getName());
            se.setAttribute("age", String.valueOf(s.getAge()));
            if (s.getHeight() != null) {
                Element h = doc.createElement("height");
                h.setTextContent(String.valueOf(s.getHeight()));
                se.appendChild(h);
            }
            if (s.getWeight() != null) {
                Element w = doc.createElement("weight");
                w.setTextContent(String.valueOf(s.getWeight()));
                se.appendChild(w);
            }
            if (s.getCourses() != null) {
                Element courses = doc.createElement("courses");
                se.appendChild(courses);
                for (Course c : s.getCourses()) {
                    Element ce = doc.createElement("course");
                    ce.setAttribute("name", c.getName());
                    ce.setTextContent(String.valueOf(c.getGrade()));
                    courses.appendChild(ce);
                }
            }
        }
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(file));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static StudentCollection loadFromFile(File file) {
    try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        List<Student> list = new ArrayList<>();
        NodeList nodes = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element se = (Element) nodes.item(i);
            Student s = new Student().withName(se.getAttribute("name"));
            String age = se.getAttribute("age").trim();
            if (!age.isEmpty()) s.withAge(Integer.parseInt(age));
            NodeList kids = se.getChildNodes();
            for (int j = 0; j < kids.getLength(); j++) {
                if (kids.item(j).getNodeType() != Node.ELEMENT_NODE) continue;
                Element child = (Element) kids.item(j);
                String tag = child.getTagName();
                if (tag.equals("height") || tag.equals("weight")) {
                    String text = child.getTextContent().trim();
                    if (text.isEmpty()) continue;                 // 空白保持 null
                    if (tag.equals("height")) s.withHeight(Integer.parseInt(text));
                    else s.withWeight(Integer.parseInt(text));
                } else if (tag.equals("courses")) {
                    NodeList courses = child.getChildNodes();
                    for (int k = 0; k < courses.getLength(); k++) {
                        if (courses.item(k).getNodeType() != Node.ELEMENT_NODE) continue;
                        Element c = (Element) courses.item(k);
                        s.addCourse(new Course(c.getAttribute("name"),
                                Integer.parseInt(c.getTextContent().trim())));
                    }
                }
            }
            list.add(s);
        }
        return new StudentCollection(list);
    } catch (Exception e) {
        return null;
    }
}
```

**坑**：没有 `course` 子元素时不要 `new` 空列表。`Student.equals` 把 `null` 和空列表当成不同。根标签名题目没给样例，上面用 `students`。给定测试只检查自己写出再读回，不比对固定文件。文件不存在或 XML 损坏时上面返回 `null`，题目没写，不确定。

### 4.10 散题 q7 树木列表 XML 读写

**题意**：原题 [TreeCollection.java](../../exam-practice-questions/misc-questions/q7-xml/TreeCollection.java)。同样是 `saveToFile` 和 `loadFromFile`，对象换成树。没有样例文件，给定测试只做写出再读回。

**套路对应**：仍是 4.9 那套，3.3 写、3.1 读、3.4 处理可选字段。和 4.9 的差别：

- 属性只有 `kind`（对应学生的 `name`）。没有 `age`。
- 可选的是 `dimension`、`color`、`types`。`dimension` 不是文本，是一个元素，上面两个属性 `diameter`、`height`。题目提示用 `setAttribute`。
- `color` 是子元素的文本。
- `types` 对应 `courses`，但每个 `<type>` 只有文本，没有 `name` 和成绩。
- 缺失时同样保持 `null`。`Tree.equals` 也把 `null` 和空列表当成不同。

**关键代码**（片段，只写和 4.9 不同的那一块）：

```java
Element d = doc.createElement("dimension");
d.setAttribute("diameter", String.valueOf(tree.getDimension().getDiameter()));
d.setAttribute("height", String.valueOf(tree.getDimension().getHeight()));
```

**坑**：`diameter`、`height` 是属性，不要写成子元素文本。这道题没有单独再跑测试，结构是按题目注释对应 4.9 的。

### 4.11 散题 q1 公司 JSON 读入合并写回

**题意**：原题 [Company.java](../../exam-practice-questions/misc-questions/q1-json/Company.java)。从 `company.json` 读出公司，把同名员工的 `skills` 合并成一条（去重，保持第一次出现的顺序），再写成和 `company_processed.json` 一样的文件。8 人合并成 5 人。John 的技能是 Java、PHP、C++、HTML。

**套路对应**：就是 3.5。字段名已经对上，三个方法原样放进 `Company`。

**关键代码**（CompanyTest 5 个测试通过；写出的文件和 `company_processed.json` 逐字节相同）：

```java
// 文件顶部加：import com.google.gson.Gson; import com.google.gson.GsonBuilder;
// import java.io.*; import java.util.*;
public static Company loadFromJsonFile(File file) {
    try (FileReader r = new FileReader(file)) {
        return new Gson().fromJson(r, Company.class);
    } catch (Exception e) {
        return null;
    }
}

public void mergeEmployees() {
    Map<String, Employee> byName = new LinkedHashMap<>();
    for (Employee e : employees) {
        Employee kept = byName.get(e.getName());
        if (kept == null) {
            kept = new Employee(e.getName(), new ArrayList<>());
            byName.put(e.getName(), kept);
        }
        for (String s : e.getSkills()) {
            if (!kept.getSkills().contains(s)) kept.getSkills().add(s);
        }
    }
    employees = new ArrayList<>(byName.values());
}

public void saveToJsonFile(File file) {
    try (FileWriter w = new FileWriter(file)) {
        new GsonBuilder().setPrettyPrinting().create().toJson(this, w);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

**坑**：要用 `LinkedHashMap`，不能用 `HashMap`，否则人和技能的顺序会乱，和样例对不上。必须 `setPrettyPrinting()`，样例是两空格缩进，文件末尾没有多余空行。文件不存在或 JSON 损坏时上面返回 `null`，题目没写，不确定。

### 4.12 散题 q11 学院 JSON

**题意**：原题 [College.java](../../exam-practice-questions/misc-questions/q11-json/College.java)。读 `college.json`，把同名学生的 `courses` 合并后写回，结果要和 `college_processed.json` 一样。

**套路对应**：和 4.11 同一套。只改名字：类是 `College`，列表字段是 `students`，方法是 `mergeStudents`，每人的列表叫 `courses`，类型仍是 `List<String>`。`LinkedHashMap`、去重、`setPrettyPrinting()` 都不变。

**关键代码**：把 4.11 里的 `employees` 换成 `students`，`skills` 换成 `courses`，`Employee` 换成 `Student`，`mergeEmployees` 换成 `mergeStudents`。

**坑**：CollegeTest 5 个测试通过。按这个替换写出的文件和 `college_processed.json` 逐字节相同。John 的课程是 Network、Database、Programming。David 第二次出现的 Database 要丢掉。

## 5 易错清单

| 容易错的地方 | 正确做法 |
|---|---|
| `getChildNodes()` 不看类型就转成 `Element` | 先判断 `ELEMENT_NODE`，空白文本要跳过 |
| 属性缺失当成 `null` | `getAttribute` 得到 `""`。用 `hasAttribute`，或 `trim()` 后看 `isEmpty()` |
| 嵌套结构用 `getElementsByTagName` 当直接孩子 | 它会把孙子也取出来。要层级就用 `getChildNodes()` |
| `parse(路径字符串)` | 会当成网址。写成 `parse(new File(path))` |
| 可选字段缺失写成 `"null"`、`0` 或空列表 | 保持 `null`。写出时不建这个元素、不设这个属性 |
| 文件合法但里面没有记录，返回 `null` | 返回空集合。`null` 只用于题目说的出错。文件不存在时要看骨架：有的已经返回空列表 |
| `Map.of(...)` 里放 `null` 人口 | 用 `HashMap` |
| 空白文本直接 `parseInt` | 先 `trim()`，空串就留 `null` |
| 元素建了没 `appendChild`，根没挂到 `doc` | 文件是空的，或少一块 |
| 标签名大小写和样例不一致 | XML 区分大小写，照样例抄 |
| CSV 把表头当数据，空行也去转数字 | 先 `readLine()` 丢掉表头，空行 `continue` |
| `split("|")`、`split(".")` | 参数是正则。写成 `split("\\|")`、`split("\\.")` |
| `readLine()` 不补换行，或把 Markdown 整段 `trim()` | 每行后面补 `System.lineSeparator()`。行首空格是缩进，不能删 |
| 合并用 `HashMap`，或不用 `setPrettyPrinting()` | 顺序会乱，缩进和样例对不上。用 `LinkedHashMap`，样例有缩进就 `setPrettyPrinting()` |
| 写文件不用 try-with-resources | 流没关，文件可能是空的 |
| 方法上加 `throws`，或加了字段 | 很多题不允许。受检异常放进 try catch |
| `TripleComponent` 用单参数构造器 | 和测试里的对象不相等。要传类型和文本两个参数 |
| 选择题只看格式名 | 先查 2.8 的表，再看理由。编出来的功能或数字，这个选项就是错的 |
