# 04 树：易读版

**本章只练两件事：先练查找 `find`，再练前序遍历。** 目标是掌握逻辑固定、只需补少量代码的基础部分。

插入、删除、高度、条件求和、AVL、红黑树、迭代器都不在本版学习范围内。下面只覆盖真题中的对应小部分，不代表做完整道大题。

## 1. 先看懂这棵树

```text
        5
       / \
      3   7
     / \
    2   4
```

| 词或代码 | 中文意思 | 图里的例子 |
|---|---|---|
| node | 节点，一个存数据的位置 | 5、3、7 都是节点 |
| root | 根，最上面的节点 | 5 |
| left | 左孩子 | 5 的左孩子是 3 |
| right | 右孩子 | 5 的右孩子是 7 |
| subtree | 子树：一个节点及它下面的所有节点 | 3、2、4 是一棵子树 |
| null | 空位置，没有节点 | 7 的左右都是空的 |

这是一棵普通的二叉搜索树，简称 **BST**：每个节点左边子树里的数字都更小，右边子树里的数字都更大。所以找一个数时，每次只需要选一边。

本章教学代码统一使用下面的节点定义，先认识它即可，不需要背：

```java
class TreeBasics {
    static class Node {
        int value;          // 节点里存的数字
        Node left, right;   // 左右孩子，默认是 null

        Node(int value) {
            this.value = value;
        }
    }
    // 后面标有 static 的教学方法放在这里
}
```

`Node n` 表示变量 n 指向一个节点；`n.value` 是它的数字，`n.left` 是它的左孩子。`new Node(5)` 就是新建一个存着 5 的节点。

## 2. 第一优先：查找 find

### 2.1 题目让你做什么？

给一个目标数字，判断树里有没有它。

例如：

```text
找 4 → 有，返回 true
找 6 → 没有，返回 false
```

`true` 表示“是”，`false` 表示“否”。方法返回类型 `boolean` 表示结果只能是这两种之一。

### 2.2 先用眼睛找，再写代码

找 4：

1. 从根 5 开始：4 比 5 小，去左边。
2. 到 3：4 比 3 大，去右边。
3. 到 4：相等，找到，返回 `true`。

找 6：

1. 从根 5 开始：6 比 5 大，去右边。
2. 到 7：6 比 7 小，去左边。
3. 左边是空位置，返回 `false`。

**查找只记四句话：空就没有，等于就找到，小往左，大往右。**

### 2.3 最短的递归模板

```java
static boolean find(Node n, int target) {
    if (n == null) return false;           // 空位置：没找到
    if (target == n.value) return true;    // 相等：找到了
    if (target < n.value) return find(n.left, target);
    return find(n.right, target);
}
```

| 代码 | 中文意思 |
|---|---|
| `int target` | 要找的目标数字 |
| `if (...)` | 如果括号里的条件成立，就执行后面的语句 |
| `==` | 判断两边是否相等 |
| `return` | 结束当前方法，并交回结果 |
| `find(n.left, target)` | 换到左孩子，继续找同一个目标 |
| `find(n.right, target)` | 换到右孩子，继续找同一个目标 |

方法内部再次调用自己，叫**递归**。这里没有新的找法，只是每次换到下面一个节点，重复刚才的四句话。

找 4 的调用和返回过程：

```text
find(节点5, 4)
  → find(节点3, 4)
      → find(节点4, 4)
          → 相等，返回 true
      ← 节点3那次调用收到 true，再返回 true
  ← 节点5那次调用收到 true，最终结果是 true
```

**递归调用前不要漏 `return`。** 它负责把下面找到的结果交回上面。

### 2.4 对应真题：2019 S2 Q2，只做 find

[打开原题 BST.java](../exam-practice-questions/past-exams/2019-2/Q2/BST.java)

原题已经提供 `root`、`Node`、`value`、`left`、`right`。方法签名是：

```java
public Boolean find(int value)
```

这里的大写 `Boolean` 也可以接收 `true` 和 `false`。**保持题目签名不变。**

#### 写法一：循环查找，只填原来的方法

为了只填一个方法、无需添加辅助方法，可以用循环写。它和刚才的递归执行完全相同的查找步骤：

```java
public Boolean find(int value) {
    Node current = root;                  // 从根出发
    while (current != null) {             // 还有节点，就继续检查
        if (value == current.value) return true;
        if (value < current.value) current = current.left;
        else current = current.right;
    }
    return false;                         // 走到空位置，没找到
}
```

`current` 是“当前检查的节点”；`while` 表示重复执行；`current = current.left` 表示把当前检查位置移到左孩子。

这道题的 `value` 参数是 `int`，与节点的 `Integer value` 比较时会拆成数值比较。这种写法不要直接搬去比较两个字符串。

#### 写法二：递归查找，增加一个辅助方法

这道原题明确允许在 `BST` 或 `Node` 中增加方法，因此也可以用下面的递归写法。**两种写法任选一种，不要同时放入两个 `public Boolean find(int value)`。**

原方法只有目标值 `value` 这一个参数，第一次从 `root` 开始。递归时还需要记录“现在查到哪个节点”，所以增加一个接收 `Node current` 的辅助方法：

```java
// 原题方法：负责从根节点开始查找，签名保持不变。
public Boolean find(int value) {
    return findFrom(root, value);
}

// 辅助方法：放在 BST 类内、上面的 find 方法外。
private boolean findFrom(Node current, int value) {
    if (current == null) return false;         // 走到空位置：没有找到
    if (value == current.value) return true;   // 当前节点就是目标：找到

    if (value < current.value) {
        return findFrom(current.left, value); // 目标更小：把左孩子交给下一次调用
    }
    return findFrom(current.right, value);    // 目标更大：把右孩子交给下一次调用
}
```

这里的几个名字分别表示：

| 写法 | 中文意思 |
|---|---|
| `find(value)` | 对外的入口：请从整棵树里找这个值 |
| `findFrom(current, value)` | 从 current 这个节点开始找 value |
| `root` | 整棵树的根，只在入口作为起点传入 |
| `current.left` / `current.right` | 下一次调用要检查的节点 |
| `return findFrom(...)` | 把下一次查找得到的结果，原样交回上一层 |

辅助方法返回小写 `boolean`；原方法返回大写 `Boolean`，Java 会自动把 `true` / `false` 转成相应的包装类型，不需要额外处理。

**用本章的树找 4，逐次代入：**

```text
find(4)
  → findFrom(节点5, 4)：4 < 5，查左边
      → findFrom(节点3, 4)：4 > 3，查右边
          → findFrom(节点4, 4)：相等，返回 true
      ← 节点3那一层收到 true，返回 true
  ← 节点5那一层收到 true，返回 true
find(4) 最终返回 true
```

**再找不存在的 6：** `节点5 → 节点7 → 7的左孩子null`。最后一次调用命中 `current == null`，返回 `false`；每一层都把这个 `false` 继续返回，最终 `find(6)` 得到 `false`。

循环版通过 `current = current.left` 移动位置；递归版通过 `findFrom(current.left, value)` 把新位置传给下一次调用。**目标值一直不变，检查位置逐步向下移动。**

不要在递归分支写 `find(value)`，因为它会重新从 `root` 开始；也不要省略递归调用前的 `return`，否则下面找到的结果没有被正确交回。

**考场填空：原 `find` 的可编辑区只填方法体，不要把整个方法嵌套进去；递归版的 `findFrom` 另放在 BST 类内、find 方法外。** 本轮跳过同一文件里的 `delete` 和 `sumEvenNodes`。

### 2.5 自己填一次

```java
Node current = root;
while (current != null) {
    if (value == current.value) return ______;
    if (value < current.value) current = current.______;
    else current = current.______;
}
return ______;
```

答案依次是：`true`、`left`、`right`、`false`。

## 3. 第二优先：前序遍历

### 3.1 题目让你做什么？

查找只需要找到一个目标；遍历要按顺序把每个节点都访问一次。

**前序 pre-order 的固定顺序：根 → 左子树 → 右子树。** “前”表示根最先处理。

还是这棵树：

```text
        5
       / \
      3   7
     / \
    2   4
```

按前序读：

1. 先记录根 `5`。
2. 处理左子树：先 `3`，再它的左边 `2`，再它的右边 `4`。
3. 左子树全部结束，再处理右子树 `7`。

结果是 **`5 3 2 4 7`**。

不要写成 `5 3 7 2 4`。前序要先把左子树整块处理完，不是一层一层地读。

### 3.2 代码就是三个动作

先认识保存结果的列表：

| 代码 | 中文意思 |
|---|---|
| `List<Integer>` | 保存整数的列表 |
| `new ArrayList<>()` | 创建一张空列表 |
| `out.add(n.value)` | 把当前数字追加到列表末尾 |

代码使用完整的 `java.util` 名称，所以这里不需要另外写 `import`。

```java
static void preOrder(Node n, java.util.List<Integer> out) {
    if (n == null) return;   // 空位置不用处理
    out.add(n.value);       // 1. 根：记录自己
    preOrder(n.left, out);   // 2. 左：处理整个左子树
    preOrder(n.right, out);  // 3. 右：处理整个右子树
}
```

`void` 表示这个方法不返回结果值。结果直接加进传入的列表，所以空位置只写 `return;`，不写 `return false;`。

调用示意（放在方法体内，`root` 指向已经建好的树）：

```java
java.util.List<Integer> result = new java.util.ArrayList<>();
preOrder(root, result);
System.out.println(result); // [5, 3, 2, 4, 7]
```

每次递归都用同一张 `out` 列表，变化如下：

```text
先是空表：[]
记录 5：[5]
记录 3：[5, 3]
记录 2：[5, 3, 2]
记录 4：[5, 3, 2, 4]
记录 7：[5, 3, 2, 4, 7]
```

### 3.3 对应真题：2022 S1 Q3，反前序只交换左右

[打开原题说明的 Part 3](../exam-practice-questions/past-exams/2022-1/q3/readme.md) · [打开 BST.java](../exam-practice-questions/past-exams/2022-1/q3/BST.java)

这道题明确要求 **inverted pre-order（反前序）：根 → 右 → 左**。

所以你只需要把刚才的“左、右”两行交换。本章样例树的反前序是 `5 7 3 4 2`，不是把前序答案倒着写。

还有一个区别：原题要求 `List<Node>`，列表保存的是**节点对象**，因此写 `list.add(n)`，不是 `list.add(n.value)`。

在原题的 `invertedPreOrder()` 方法中，列表已经创建好了，只需在可编辑区填：

```java
walk(root, list);
```

原方法自带的 `return list;` 保留。在 `BST` 类内、原方法外添加下面的辅助方法（题目允许添加辅助方法）：

```java
private void walk(Node n, List<Node> list) {
    if (n == null) return;
    list.add(n);          // 根：保存节点本身
    walk(n.right, list);  // 右
    walk(n.left, list);   // 左
}
```

原题已经导入 `java.util.List`。`private` 表示这个辅助方法供当前类内部使用，不改变原题要求的方法签名。

### 3.4 自己填一次

普通前序：

```java
if (n == null) return;
out.add(n.value);
preOrder(n.______, out);
preOrder(n.______, out);
```

答案：`left`、`right`。

真题反前序：

```java
if (n == null) return;
list.add(______);
walk(n.______, list);
walk(n.______, list);
```

答案：`n`、`right`、`left`。

## 4. 最后只记这张表

| 要求 | 固定步骤 |
|---|---|
| 查找 find | 空就没找到；相等就找到；小往左；大往右 |
| 普通前序 | 空就结束；记录自己；递归左；递归右 |
| 反前序 | 空就结束；记录自己；递归右；递归左 |

做题前确认三件事：

1. **比较方向。** 本章按普通左小右大；题目若规定大的放左边，要相应改方向。
2. **空树表示。** 本章用 `null`；如果题目用 `EmptyTree`，不能直接照抄空值判断。
3. **返回或保存什么。** 找到后返回布尔值还是节点？列表要数字还是节点？按原题签名写。

练习顺序：**2019 S2 Q2 的 `find` → 手写普通前序 → 2022 S1 Q3 的 `invertedPreOrder`。** 能不看答案填出上面的代码，再学习其他树题。

---

## 往年题逐题讲解（年份倒序）

继续保持本章“查找 + 前序遍历”的基础范围。以下只讲各真题的对应小问，不要求先掌握插入、删除、旋转或平衡树。自拟的小树用于手算，具体字段和返回类型以链接中的原题为准。

### 2025 S1｜Q2 Task B 的 find：较大的 ID 放左边

#### 题目（原题内容）

**作答任务（中文）：** 按“左大右小”的树结构实现 find(id)，返回匹配的 Tree，找不到返回 null；空位置使用 EmptyTree。只做 find。

**原文题干／本节对应小问：**

> ## Introduction
>
> Your teammate has found some existing code for a generic tree data structure
> and developed some code skeleton. The code consists of the following classes:
> - `Tree`: an abstract base class that represent the hierarchical data structure.
>   - `root` stores the value of the root node, where `left`, `right` points to the left and right subtrees, respectively. 
>   - **abstract methods** - see docStrings
> - `FamilyTree`: a tree-subclass that represents a root node of a tree and perform tree operations.
> - `EmptyTree`: a concrete tree sub-class that acts as a placeholder in absence of a node, maintaining the structure.
>
> - `Node`: an abstract class that represent data in a tree node.
> - `Person`: a concrete sub-class of `Node` representing a person in the family tree. 
>
>
> Implement a family tree data structure that:
> 1.	Maintains hierarchical relationships based on unique IDs.
> 2.	Supports insertion, searching, validation, and structured display.
> 3.	Correctly labels the hierarchy levels (e.g., Kid, Parent, Grandparent).
>
>
> ### Family Tree
>
> **Ordering**: The family tree is a binary search tree with a 
> custom right-left insertion rule: during insertion, If the new person’s ID 
> is greater than the current node’s ID, insert it into the left subtree.
> Otherwise, if it’s smaller or equal, insert it into the right subtree.
>
> ### Task B: Class Implementation (FamilyTree)
>
> - Complete methods related to basic tree operations in `FamilyTree`: 
>   `find`, `isOrdered`, `insert`.
>   - **Check docStrings in the relevant classes** for specification of the methods.

**Tree.find 的契约和方法签名（原文摘录）**

```java
/**
     * Finds a node with the specified ID in the tree.
     *
     * @param id The ID to search for.
     * @return The tree node containing the specified ID, or null if not found.
     */
    public abstract Tree find(int id);
```

**FamilyTree.find 待实现；EmptyTree.find 已给出**

```java
public Tree find(int id) {
        // TODO - Start your code


        return null;
        // TODO - End your code
    }

// EmptyTree 中：
public Tree find(int id) {return null;}
```

#### 逐步讲解与做题套路


原题：[说明](../exam-practice-questions/past-exams/2025-1/src/Q2/Q2README.md) · [FamilyTree.java](../exam-practice-questions/past-exams/2025-1/src/Q2/FamilyTree.java) · [EmptyTree.java](../exam-practice-questions/past-exams/2025-1/src/Q2/EmptyTree.java)。

**题目要什么？** 找到指定 ID 所在的树对象。找不到返回 null。这里不是返回 true/false，也不是返回 Person。

**第一步：先圈出特殊规定。** 这棵树是“左大右小”，与最常见的 BST 相反。原题例子插入 5、9、2、4、1 后，结构是：

```text
        5
       / \
      9   2
         / \
        4   1
```

**第二步：手算找 4。**

1. 在 5：4 比 5 小，去右边的 2。
2. 在 2：4 比 2 大，去左边的 4。
3. 在 4：相等，返回“当前这棵 FamilyTree”，即 `this`。

**第三步：认清空树。** 原题用 `new EmptyTree()` 表示没有孩子；空树对象不是 null。`EmptyTree.find` 已经返回 null，因此递归走到它就自然结束。

在构造函数已经正确建立左右子树、空位置使用 EmptyTree 的前提下，`FamilyTree.find` 可写成：

```java
public Tree find(int id) {
    if (id == root.getId()) return this;
    if (id > root.getId()) return left.find(id);
    return right.find(id);
}
```

这里 `root` 存的是 Person/Node 数据，`this` 才是当前 Tree 对象。返回 `root` 会与方法要求的 `Tree` 类型不符。

**找不到时再走一遍：** 找 3：5→右边2→左边4→右边EmptyTree→返回 null，结果沿调用链返回到最外面。

**做题套路：先抄排序方向 → 看空树表示 → 看返回类型 → 相等返回当前对象 → 否则选一边递归。** 本节不讲同题的 `isOrdered`、插入、层级标签和打印。

### 2023 S1｜Q2 Part 4：多叉树前序，只是孩子从两个变成一列

#### 题目（原题内容）

**作答任务（中文）：** 树已经构建好，补全前序遍历，按孩子原顺序访问；返回结果不能包含人工根 LIST_ROOT。

**原文题干／本节对应小问：**

>  Part 4) Data Structure 
>
> Implement the `preOrderWalk()` method within the `ListElementsTree` class.
> Hint: You may add helper methods within the `Node` class if needed. 
>
> Pre-Order traversal at a node entails: visit the current node, then perform pre-order traversal from its leftmost children to the rightmost children.
>
> For instance, the expected order of nodes in the previous example is illustrated as follows:
> <img src="../exam-practice-questions/past-exams/2023-1/Q2/MDTree_Example_Walk.png" style="max-width: 500px;" alt="Example illustrating the pre-order traversal of the MDTree (find `MDTree_Example_Walk.png` in the folder if it is not shown)." >
>
> Note that you should exclude the root node of the tree (of `LIST_ROOT` type) from the returned list.
> You may find more details in the given `ListElementsTreeTest.java`.
>
> 
> ***

**已给代码：`ListElementsTree.java`（原文件）**

```java
package Q2;

import java.util.ArrayList;
import java.util.List;

public class ListElementsTree {
    Node root;

    public ListElementsTree() {
        this.root = new Node(MDTokenType.LIST_ROOT.toString());
    }

    public Node insert(MDToken child) {
        return this.root.insert(child.getContent());
    }

    public List<String> preOrderWalk() {
        // TODO - Your Code Starts Here


        return null;      // You may make changes to this line
        // TODO - Your Code Ends Here
    }



    /** Note: this method is created for testing ONLY. It should not be used in your submissions. */
    public ListElementsTree(String rootValue, List<String> childrenValues) {
        this.root = new Node(rootValue);
        for (String childValue : childrenValues) {
            Node child = new Node(childValue);
            this.root.children.add(child);
        }
    }

    /** Note: this method is created for testing ONLY. It should not be used in your submissions. */
    public Node insertTree(ListElementsTree tree) {
        this.root.children.add(tree.root);
        return tree.root;
    }



    /**
     * Node
     */
    public class Node {
        private String value;
        private List<Node> children;

        public Node(String value) {
            this.value = value;
            this.children = new ArrayList<>();
        }


        public Node insert(String value) {
            Node child = new Node(value);
            children.add(child);
            return child;
        }


        public List<Node> getChildren() {
            return children;
        }

        public String getValue() {
            return value;
        }

    }


}
```

#### 逐步讲解与做题套路


原题：[说明](../exam-practice-questions/past-exams/2023-1/Q2/Q2README.md) · [ListElementsTree.java](../exam-practice-questions/past-exams/2023-1/Q2/ListElementsTree.java)。本节假设 Parser 已经把树建好，只完成 `preOrderWalk()`。

**题目要什么？** 按“当前节点 → 从左到右的所有孩子”输出内容；最上面的人工根 `LIST_ROOT` 不放进结果。

用小例子理解：

```text
LIST_ROOT（人工根，不输出）
├── A
│   ├── B
│   └── C
└── D
```

答案是 `A, B, C, D`。不是 `A, D, B, C`，因为前序要先把 A 的子树整块处理完。

**解题步骤：**

1. 创建一个空 `List<String>`，保存输出。
2. 不记录人工根，直接按顺序处理它的孩子 A、D。
3. 对每个普通节点：先记录自己的 value，再按顺序递归所有 children。
4. 返回同一张结果表。

在 `ListElementsTree` 类中，方法可写为下面这样；辅助方法放在方法外、类内：

```java
public List<String> preOrderWalk() {
    List<String> result = new ArrayList<>();
    for (Node child : root.getChildren()) {
        walk(child, result);
    }
    return result;
}

private void walk(Node node, List<String> result) {
    result.add(node.getValue());
    for (Node child : node.getChildren()) {
        walk(child, result);
    }
}
```

原题的 `children` 初始化为空列表，所以叶节点的循环自动执行 0 次，不必人为造一个 null 孩子。

结果变化：`[] → [A] → [A,B] → [A,B,C] → [A,B,C,D]`。

**做题套路：先确定人工根是否计入 → 当前节点先记录 → 用循环替代“左、右”两次递归 → 保持 children 原顺序。** 不要把节点按字母排序，原题要保留列表关系和顺序。

### 2022 S1｜Q3 Part 3：字符串查找 + 反前序

#### 题目（原题内容）

**作答任务（中文）：** 实现按字符串键查找节点，以及根→右→左的反前序遍历。这里只做 Part 3 的两个 BST 方法。

**原文题干／本节对应小问：**

> Part 3) Implement the `find()` and `invertedPreOrder()` methods of the `BST` class to realise the underlying operations of binary search tree. `find()` aims to find a particular node based on a given key. `invertedPreOrder()` aims to perform an inverted pre-order traversal of the tree. Note that the traversal of an inverted pre-order should be: root, right child, left child.
>
> Please find more details in the given `BSTTest.java`.

**已给代码：`BST.java`（原文件）**

```java
import java.util.LinkedList;
import java.util.List;

public class BST {

	private Node root;

	public BST() {
		this.root = null;
	}

	public void insert(Node node) {
		if (this.root == null) {
			this.root = node;
		} else {
			Node parent = null;
			Node current = this.root;
			while (current != null) {
				if (current.key.compareTo(node.key) < 0) {
					parent = current;
					current = current.right;
				} else if (current.key.compareTo(node.key) > 0) {
					parent = current;
					current = current.left;
				} else {
					break;
				}
			}

			if (parent.key.compareTo(node.key) < 0) {
				parent.right = node;
			} else if (parent.key.compareTo(node.key) > 0) {
				parent.left = node;
			}
		}
	}

	public Node find(String key) {

		Node node = null;

		// TODO
		// ########## YOUR CODE STARTS HERE ##########

		// ########## YOUR CODE ENDS HERE ##########

		return node;
	}

	public List<Node> invertedPreOrder() {

		List<Node> list = new LinkedList<>();

		// TODO
		// ########## YOUR CODE STARTS HERE ##########

		// ########## YOUR CODE ENDS HERE ##########

		return list;
	}

}
```

**已给代码：`Node.java`（原文件）**

```java
public class Node {

	public String key;
	public int value;
	public Node left;
	public Node right;

	public Node(String key, int value) {
		this.key = key;
		this.value = value;
		this.left = null;
		this.right = null;
	}
}
```

#### 逐步讲解与做题套路


原题：[说明](../exam-practice-questions/past-exams/2022-1/q3/readme.md) · [BST.java](../exam-practice-questions/past-exams/2022-1/q3/BST.java)。

**题目要什么？** `find(String key)` 返回匹配的 Node；`invertedPreOrder()` 返回节点列表，顺序是根→右→左。

**先做 find。** 字符串不能写 `target < current.key`。使用 `compareTo`，只看负、零、正，不要假设只有 −1、0、1。

在 `find` 的可编辑区中填下面代码，保留外面的 `Node node=null` 和最后 `return node`：

```java
Node current = root;
while (current != null) {
    int cmp = key.compareTo(current.key);
    if (cmp == 0) {
        node = current;
        break;
    }
    if (cmp < 0) current = current.left;
    else current = current.right;
}
```

对自拟树“根 m，左 c，右 t，c 的右孩子 f”，查找 f：`f<m` 去左；`f>c` 去右；`f=f` 命中。注意这里是目标 `key` 与当前 key 比较，比较两边调换后正负号也会反过来。

**再做反前序。** 对下面这棵树：

```text
        m
       / \
      c   t
       \
        f
```

依次记录 m→t→c→f。普通前序则是 m→c→f→t；反前序不是把普通答案直接倒着抄。

原方法里已经有 `List<Node> list`，填 `walk(root, list);`，再在 BST 类中添加：

```java
private void walk(Node n, List<Node> list) {
    if (n == null) return;
    list.add(n);
    walk(n.right, list);
    walk(n.left, list);
}
```

**易错点：** 要的是 `List<Node>`，因此加的是 `n`；若加 `n.key`，就成了字符串列表。命令的 LOAD/SAVE/SUM 和通配符匹配属于同题其他部分，这里不展开。

**做题套路：字符串比较只看符号 → 返回匹配节点 → 遍历顺序翻成三行代码 → 按泛型决定保存对象还是值。**

### 2019 S2｜Q2 Task 1：普通整数 BST 的 find

#### 题目（原题内容）

**作答任务（中文）：** 只实现整数 BST 的 find(int value)：存在返回 true，不存在返回 false。insert 已提供，保持 package tree 不变。

**原文题干／本节对应小问：**

> /**
> 	 * Q2 - Task1 TODO: Implement "find" method. The method should return "true" if
> 	 * a tree contains the node with value, otherwise return "false". You can define
> 	 * additional functions in class BST or Node if you need.
> 	 * 
> 	 * @param value
> 	 * @return return true if the tree contains the node with value otherwise false
> 	 */
> 	public Boolean find(int value) {
>
> 		// start your code
>
> 		// end your code
> 	}

**节点定义及根字段（原文摘录）**

```java
Node root;

public class Node {
		public Integer value;
		public Node parent;
		public Node left;
		public Node right;

		public Node(Integer value) {
			this.value = value;
			this.parent = null;
			this.left = null;
			this.right = null;
		}
	}
```

#### 逐步讲解与做题套路


原题：[BST.java](../exam-practice-questions/past-exams/2019-2/Q2/BST.java)。本节只做 find；不要求同时完成 delete 和 sumEvenNodes。

**题目要什么？** 指定值是否存在，返回 Boolean。原题已经给了 insert，可以用它建立小树进行验证。

先画：

```text
        5
       / \
      3   7
       \
        4
```

**从四种情况推出代码：**

| 当前情况 | 应该做什么 |
|---|---|
| current 是 null | 不存在，返回 false |
| target 等于 current.value | 找到，返回 true |
| target 小于 current.value | 改查左孩子 |
| target 大于 current.value | 改查右孩子 |

```java
public Boolean find(int value) {
    Node current = root;
    while (current != null) {
        if (value == current.value) return true;
        if (value < current.value) current = current.left;
        else current = current.right;
    }
    return false;
}
```

**手算两个方向：** 找 4：5→3→4→true；找 6：5→7→null→false。空树从一开始 current 就为 null，循环不执行，返回 false。

**如何验证不是写死了答案？** 至少想清楚空树、根命中、左侧命中、右侧命中、缺失五种情况。这里是检查自己代码，不是在声称这些就是某种覆盖标准的最少测试数。

**做题套路：从根开始 → 每轮排除一整边 → 命中立即返回 → 走到空位置失败。** 不需要遍历所有节点，也不需要修改树。

### 2019 S1｜Q3 的 find：把数字比较换成字符串比较

#### 题目（原题内容）

**作答任务（中文）：** 只补内部 Node.find(String s)：返回存着目标字符串的节点。外层 BST.find 已处理空根并委托给 Node。

**原文题干／本节对应小问：**

> /**
> 		 * @param s
> 		 * @return the node that has the given value.
> 		 */
> 	    public Node find(String s) {
> 			// TODO: Add your implementation here.
> 	        return this;
> 	    }

**已给节点字段、构造函数（原文摘录）**

```java
public class Node {
		String value;
		Node parent;
		Node left;
		Node right;

		public Node(String value) {
			this.value = value;
			this.parent = null;
			this.left = null;
			this.right = null;
		}
}
```

**已给外层 BST.find（原文摘录）**

```java
public Node find(String s) {
		if (root == null) {
			return null;
		}
		
		return root.find(s);
	}
```

#### 逐步讲解与做题套路


原题：[BST.java](../exam-practice-questions/past-exams/2019-1/Q3/BST.java)。只讲 `Node.find(String s)`，不展开插入或条件输出。

**题目要什么？** 找到保存字符串 s 的节点，找不到返回 null。外层 `BST.find` 已处理空根，并调用 `root.find(s)`；现在补的是内部 Node 类的方法。

假设根是 `"m"`，左孩子 `"c"`，右孩子 `"t"`：

- 找 `"c"`：`"c".compareTo("m")<0`，查左孩子。
- 到 `"c"`：比较为 0，返回当前节点 `this`。
- 找 `"z"`：先向右到 t，再向右；右孩子不存在，返回 null。

```java
public Node find(String s) {
    int cmp = s.compareTo(value);
    if (cmp == 0) return this;
    if (cmp < 0) {
        return left == null ? null : left.find(s);
    }
    return right == null ? null : right.find(s);
}
```

`条件 ? A : B` 表示：条件成立取 A，否则取 B。`left == null ? null : left.find(s)` 的意思是“左边不存在就返回 null，否则去左边找”。

**最容易混淆的两个点：** 这题返回 Node，不是 true；字符串内容与次序使用 equals/compareTo，不用 `==` 来判断文字是否相同。

**做题套路：保留原来查找的四种情况，只替换比较方式和返回类型。** 不要把不同年份的函数签名混在一起背。
