## Question 1: Data Structures, Design Patterns

Complete the implementation of an iterator for a Binary Search Tree (BST). 
The iterator should traverse the tree in pre-order (root, left, right) and allow 
users to iterate through the elements using the provided _Iterator_ interface methods.

Note: The `Iterator` defined in the question follows the Iterator design pattern. 
It does not fully implement the Java Iterator interface


#### Overview of Classes and Methods
`BST` Class: represents a generic binary search tree (BST) with the following key methods:
- `insert(T value)`: inserts a value into the BST.
- `createIterator()`: Returns an instance of `BSTIterator` for the tree.


`BSTIterator` Class:
- This is an inner class of BST and implements the `Iterator` interface to 
  traverse the tree in **pre-order**. You need to complete the implementation of this class.

Note that as always, you must also read the code and documentation to 
understand the structure of the given code.


***

### Tasks - Complete the `BSTIterator`.

Complete the `BSTIterator` class within BST.java. The iterator should 
correctly implement pre-order traversal of the BST.
- The `BSTIterator` constructor: initialise the stack.
  - Hint: use the root of the tree if it is not null.

- `hasNext()`: check whether there are any more nodes to visit in the tree by examining the stack.

- `next()`: return the next element in the pre-order traversal of the tree, then push new elements to the tree (if appropriate).

-----------
#### Pre-Order Traversal Using Stack

#### Why is a stack used?

We use recursion when we pre-order traverse through a BST. A stack naturally 
supports this, where nodes are visited in a depth-first manner (process current 
node before children).

#### Example - Using a Stack for Pre-Order Traversal
Consider a tree with the following structure:
```
      5
     / \
    3   7
   / \ / \
  2  4 6  8
```

The steps of pre-order traversal using a stack follow.

##### Initialisation
When we initialise `BSTIterator`, push the root node (5) onto the stack.

##### Traversal
- When `next` is first triggered, Pop and visit the last node (5), 
  push its right and left child (i.e., push 7, then push 3); then return it (5).
- When `next` is triggered again, repeat the process.
  Now the last node is 3: pop it, push its children (4, 2); then return it (3).
- When `next` is triggered again, repeat the process with the last node (2).
  Note that "2" has no children to push, so we just return 2.

... and repeat the process with `4`, then `7`, and so on until `hasNext` is false.


The Iterator does not actively track modifications to the BST. If the BST
is modified while an iteration is ongoing:
-  New elements inserted *before* the current iterator position will be IGNORED; and
-  New elements inserted *after* the current iterator position may be included
   if they fall within the remaining traversal path.


#### Additional Notes:

- You may create helper methods within `BST.java`, but NOT helper fields.
- The basic tests in `Q1Test.java` can assist with your code development and understanding.
- Do not alter any of the method signatures, as doing so may result in receiving 
  zero marks for the entire question. This means that you can not add, for example, 
  `throw IOException` to any methods.
- You must not change any existing methods, except for those explicitly marked
  as editable by comments. Only modify sections of code that are clearly
  indicated and delimited by these comments.
<br><br>

* * *


#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `BST.java`
