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

[打开原题 BST.java](../../exam-practice-questions/past-exams/2019-2/Q2/BST.java)

原题已经提供 `root`、`Node`、`value`、`left`、`right`。方法签名是：

```java
public Boolean find(int value)
```

这里的大写 `Boolean` 也可以接收 `true` 和 `false`。**保持题目签名不变。**

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

**考场填空只填方法体，不要把整个方法再嵌套进原方法里。** 本轮跳过同一文件里的 `delete` 和 `sumEvenNodes`。

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

[打开原题说明的 Part 3](../../exam-practice-questions/past-exams/2022-1/q3/readme.md) · [打开 BST.java](../../exam-practice-questions/past-exams/2022-1/q3/BST.java)

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
