### Part A

A Binary Search Tree (BST) is used to generate a sequence of bases (A, G, C, T) in a DNA molecule. The sequence is generated based on the information stored in each node (character base, see node representation) and the following rules:
1. All bases of all nodes that have an odd number of direct children under the
key node (inclusive) are concatenated;
2. If the given key is not found, return "CGTA";
3. If the given key is found, but rule 1 is not satisfied, return null.

The method signature (the method that will be tested) is `public String DNAGenerator(Integer key)`. You can define additional methods in the BST class to complete the task.

Note that the order in which the nodes are concatenated is irrelevant. As long as the rules are met, the sequence of bases generated will be considered valid.

For example, consider the following tree:

[Example tree](example-tree.png)

| Key | Expected result | Reason |
| --- | --------------- | ------ |
| 25 | T | This example includes the key node 25-T (rule 1), but not the node 12-A as it has no children. |
| 30 | T | In this example only the key node 25-T (rule 1) has an odd number of direct children. |
| 17 | CGTA | In this example the key node is not found (rule 2). |
| 70 | AG (other answers possible) | In this example the key node 70-G has an odd number of direct children, and the node 75-A, under the key node, has an odd number of direct children. |
| 12 | null | In this example the key node 12-A has no children (rule 3). |

### Part B

Implement the minimum number of JUnit test cases for DNATreeCalc() that is branch complete. Read the code of DNATreeCalc() in the `BranchComplete.java` file and implement the minimum number of test cases in the `BSTBranchCompleteTest.java` file.

Note that each execution of an assertion counts as a single test case, therefore loops that execute the same assertion multiple times count as multiple tests.
