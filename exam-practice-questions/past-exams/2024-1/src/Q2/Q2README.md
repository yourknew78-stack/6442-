## Question 2: Data Structures, Traversal and Software Testing (25 marks)

### What test cases could assist me in verifying the correctness of an AVL tree?

As a software tester, you are tasked with checking the correctness of several implementations of AVL trees created by 400 students in the COMP2100/6442 course at ANU. You **must use parameterized tests** to verify their correctness.

As you do not have time to check each implementation, you must conduct black-box testing and create parameterized test cases to aid in the testing process. An sample AVL tree implementation to be tested is provided with the `SampleAVLTree` class that extends the `AVLTree` abstract class, demonstrating what students were required to implement (i.e., the `insert` and `createNode` methods). The sample class does not contain a correct implementation of an AVL tree, and you do not need it to be correct or to have a correct implementation to complete this question. However, you are free to create AVL tree implementations to assist you in your testing process if you want.

For simplification, it can be assumed that nodes are never removed from the trees, as students only had to implement those two methods; each node stores Integers only; and that you only need the `SampleAVLTree` class to create your parameterized test cases.

To verify the correctness of an AVL tree, you must compare the output of the pre-order traversal of the tree to the expected outcome, which you will determine. The pre-order traversal method can be located in the `AVLTree` abstract class, along with visualization methods to assist you during the testing process. 

**What should you test?** We expect you to understand how AVL trees work, reads the `AVLTree` class, and, consequently, to create test cases to verify their correctness.

* * *

### Your task is to ... 
### ...implement the `data()` method

Implement the static `data()` method by providing parameterized test cases and returning a Collection of Object arrays. 

To test an implementation of an AVL tree you must provide the following sets of parameters:
  1. An identifier used to distinguish the tree to be tested. This identifier corresponds to the index of the tree in a list. For instance, the first tree in the list will have the identifier '0', which also represents its initial position in the list. The identifier is sequential; for example, with four trees, the identifiers will necessarily be 0, 1, 2, and 3.

  2. The expected outcome is composed of a list of integers (representing the nodes of the AVL tree) following the pre-order traversal of the tree.

  - Hint 1: The Java `List.of` method to create lists may be useful.
  - Hint 2: The software testing lecture slides on Wattle may be useful. 

### ...and implement the `createTree()` method
 
Implement the `createTree()` method in the `AVLTreeTest` class. 
`createTree()` is a simple method that creates trees and returns an AVL tree based on the implementation used. The argument is an index which selects the tree to be returned.


### Note that 

- You must **minimize the number of overall nodes used in all the trees you created** to verify whether an AVL tree implementation is correct.
- All test cases should pass if an AVL tree implementation is correct.
- At least one test case should fail if an AVL tree implementation is incorrect.
- You do not need to test the AVLTree by inserting `null`, duplicate values and incorrect inputs.
- You must not change any existing methods, except for those indicated with comments, and delimited by them, that can be modified.
- You do not need to create any additional test methods, and any that are created will not be assessed.
- A sample AVLTree, `SampleAVLTree` class has been provided to you. Note that it may or may not be a correct implementation of an AVLTree. You may change the `insert` method of this class if it helps with your development, but it is optional and will not be marked.
- By default, the `SampleAVLTree.class` is used, but you can change that to any other AVL implementation if you want and have one. Not recommended, optional and will not be marked.

<br>

* * *


#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `AVLTreeTest.java`
  
