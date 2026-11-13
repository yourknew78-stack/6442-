# Question 2: Software Testing (20 marks)

You are testing the `calculatePrice` method in the provided `Pricing` class. 
The `calculatePrice` method uses multiple decisions (if/else chains) that affect the final price. 

* * *

### Task: Branch-complete Coverage using parameterized tests

Design test cases for the `calculatePrice` method of the `Pricing` class.

Your goal is to design a minimum set of test cases that achieves branch-complete coverage.
- Create the test cases within the `data()` method of the `CalculatePriceTest` class.
- You must use the parameterized test framework already provided.

**Note that we are assessing your ability to design test cases for a given piece of code (white-box testing), rather than your ability to critique the code itself.
Focus on analysing the code’s control flow and decision points to design test cases that achieve complete branch coverage, rather than interpreting or questioning the underlying logic.**

Your work will be assessed based on how close you are to achieving complete branch coverage. 
- You must cover at least **50% of the branches** to be eligible for marks for this question. 
- if you use more than the minimum required number of test cases to achieve complete branch coverage, 5 marks will be deducted for each additional test case.
- if you use fewer than the minimum required number of test cases to achieve complete branch coverage, 5 marks will be deducted for each missing test case.

Notes 
- Do not include any test cases that cause the calculatePrice() method throw an exception.
- You must assume that `calculatePrice()` is implemented correctly (even if it does not make sense to you).
- You may want to create a CFG to help you design test cases.
- All your designed test cases should pass when executed against the provided implementation.
- You do not need to create any additional test methods, and any that are created will not be assessed.
- You must create test cases based on the code provided (white-box testing).
- You are not allowed to alter the signatures of the given methods and the package structures of the given classes.
- You must not change any existing methods, except for those indicated with comments, and delimited by them, that can be modified.

#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `CalculatePriceTest.java`
