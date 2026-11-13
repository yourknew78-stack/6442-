# Question 2: Data structure


## Family Tree

You and your friend have started a personal project related to family hierarchy.
To get started, you would like to develop an algorithm to determine the family roles of individuals within families.

Having learned about the binary tree data structure, your teammate believes it would be fun to build a tree for that purpose.

* * *

## Introduction

Your teammate has found some existing code for a generic tree data structure
and developed some code skeleton. The code consists of the following classes:
- `Tree`: an abstract base class that represent the hierarchical data structure.
  - `root` stores the value of the root node, where `left`, `right` points to the left and right subtrees, respectively. 
  - **abstract methods** - see docStrings
- `FamilyTree`: a tree-subclass that represents a root node of a tree and perform tree operations.
- `EmptyTree`: a concrete tree sub-class that acts as a placeholder in absence of a node, maintaining the structure.

- `Node`: an abstract class that represent data in a tree node.
- `Person`: a concrete sub-class of `Node` representing a person in the family tree. 


Implement a family tree data structure that:
1.	Maintains hierarchical relationships based on unique IDs.
2.	Supports insertion, searching, validation, and structured display.
3.	Correctly labels the hierarchy levels (e.g., Kid, Parent, Grandparent).


### Family Tree

**Ordering**: The family tree is a binary search tree with a 
custom right-left insertion rule: during insertion, If the new person’s ID 
is greater than the current node’s ID, insert it into the left subtree.
Otherwise, if it’s smaller or equal, insert it into the right subtree.

**Levels** and **Labels**:
- A leaf node (with no children) is at level 1.
- A non-leaf node is at the level of (1 plus the level of its deepest descendant).
- The label at level 1 is "Kid", and level 2: "Parent", level 3: "Grandparent",
  then "Great-Grandparent", "Great-Great-Grandparent", "Great-Great-Great-Grandparent",
  and so on.


Example: Suppose we insert the following values (IDs) into the tree, 
in order: root: 5, then 9, 2, 4, 1. The tree is built using the 
right-left ordering rule, and each person is labelled based on their level.
The labelled tree will look like
```
5 [Grandparent]
  --9 [Kid]
  --2 [Parent]
  ------4 [Kid]
  ------1 [Kid]
```


* * *

## Tasks

### Task A: Implement constructors

Complete the constructors in the `FamilyTree` class
- `FamilyTree(Person value)`, `FamilyTree(Person value, Tree left, Tree right)`
  for appropriate instantiation of trees. That is, provide appropriate values for the various 
  `Tree` fields based on the parameters.


### Task B: Class Implementation (FamilyTree)

- Complete methods related to basic tree operations in `FamilyTree`: 
  `find`, `isOrdered`, `insert`.
  - **Check docStrings in the relevant classes** for specification of the methods.


### Task C: Find levels and label the family members

Complete `getLevelAndLabel()` in the `FamilyTree` and `EmptyTree` class.
- Label all descendant nodes from the root of the tree with the family roles, and return
  the level of the root node in the tree.
  - **Check docStrings in the relevant classes** for specification of the methods.

    
Complete the `show` methods (`show()`, `show(int depth)`) in the `FamilyTree` class. 
The methods display the tree structure starting from the root.
- The value of each node is displayed on its own line.
- The value of each (non-empty) node, from the root, is shown in a new line
  as from the `toString` method of the node (from the `Person` class). The
  depth of the node shown is reflected by the level of indentation in
  the prefix text before the node value:
  - Depth 0 (root): no indentation.
  - For depth ≥ 1, each value is prefixed by 2 spaces and a sequence of dashes.
    There is a total of `4 * depth` characters (including spaces and dashes) in 
    the prefix.

You may optionally update the implementation of `show()`, `show(int depth)` in
the `EmptyTree` class. The show methods for an empty tree may return null or
an empty string (i.e., ignore the depth parameter).


-----------

#### Additional Notes:

- You may create helper methods within `FamilyTree.java`, but **NOT** helper fields.
- The basic tests in `Q2Test.java` can assist with your code development and understanding.
- You must not create or make changes to any existing methods and fields in 
  ANY of the classes, except for those explicitly marked
  as editable by comments. Only modify sections of code that are clearly
  indicated and delimited by these comments.
- Altering any of the method/field signatures, may result in receiving 
  zero marks for the entire question. This means that you can not add, for example, 
  `throw IOException` to any methods.
- The docStrings of the methods you need to complete provide
  specific additional requirements. (And in general, you should read the
  provided code since your solution needs to work in the context of those files).
<br><br>


* * *


#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `FamilyTree.java`
* `EmptyTree.java`
