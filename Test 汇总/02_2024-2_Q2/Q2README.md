## Question 2: Software Testing

### Test different implementations of `HashingBox`.

As a software tester, you are tasked with checking the correctness of different
implementations of `HashingBox`. 


#### The Thing class

The `hash` function of `HashingBox` takes in a `Thing` object, which has the following properties:
* a specific `size`, which should be an integer with a range between 1 and 31 (inclusive),
* a String `name`,
* enums `material` and `shape`.

Each type of material and shape corresponds to a value, as shown in the table below.

| Material | Value |     | Shape      | Value |
|----------|-------|-----|------------|-------|
| STEEL    | 1018  |     | CIRCLE     | 1     |
| SILVER   | 925   |     | SEMICIRCLE | 2     |
| GOLD     | 999   |     | TRIANGLE   | 3     |
|          |       |     | SQUARE     | 4     |
|          |       |     | PENTAGON   | 5     |


* * *

#### Expected Hash value returned

A `HashingBox`, if implemented correctly, must produce the correct results 
from its `hash` function given a valid (non-null) `Thing`. The hash value is 
computed as follows: 

1) The `sizeHash` is the largest prime number not larger than the `size` of the Thing,
   with an exception where the sizeHash for a Thing with size==1 is *2*.
   - E.g., the `sizeHash` of 6 is 5. 
   - E.g., the `sizeHash` of 3 is 3.

2) The `nameHash` computes a hash for a given string (name) by summing the
   product of each letter's ASCII value (considering only the first 6 characters
   for the ASCII-sum) and multiplying it by the specified `sizeHash`, then 
   adding to it the length of the name.
  - Note that only alphabetic characters (English letters) are included in the ASCII-sum.
  - E.g., if sizeHash==3, the `nameHash` of "Comp2100" is
    `<ASCII-sum for letters in the first 6 characters> * <sizeHash> + <length of name> =`
    `(67 + 111 + 109 + 112) * 3 + 8 = 1205`
  - E.g., if sizeHash==1, the `nameHash` of "1aaaa1aaaa" is
    `(97 x 4) * 1 + 10 = 398`

3) The `materialSizeHash` is the product of the **values** of the `material`
   and `shape` of the Thing.
   - E.g., a Square-shaped Silver will have a materialSizeHash of `925 x 4 = 3700`

4) Finally, the overall `hash` value is the sum of `nameHash` and `materialShapeHash`.
   - Note that when the `size` of a Thing instance is out of bound, `hash` is expected 
     to throw a `ComponentOutOfRangeException`.
   - E.g., a `Thing` with fields in the above example (a Square-shaped Silver 
     named "Comp2100" and has a size of 3) has a hash value of `1205 + 3700 = 4905`

<br><br>

* * *

#### Task:

There are 13 classes that extend the `HashingBox` abstract class. You 
must complete the parameterised tests in `Q2Test.java` to identify the 
correct and incorrect HashingBox(es) respectively.

- You may create one or more JUnit test method(s) within the test class.

- You are STRONGLY ADVISED to perform **Black Box Testing**.

For each test method you write, you must 
include the following annotation: `@Test(timeout = 1000)`.
<br><br>

* * *



#### Notes
- For your information, there are between one and three correct HashingBox(es).

- You will get full marks if, when the test class is run, ALL the incorrect
  HashingBox(es) are flagged as incorrect (i.e., fail at least one test) 
  and ALL the correct HashingBox(es) pass all the tests (i.e., with a green tick).

- You will lose some marks if you (a) failed some of the correct HashingBox(es) 
  and/or (b) passed some of the incorrect HashingBox(es). Note that you will 
  lose many more marks by failing a correct HashingBox than passing an incorrect one.

- You will not get any marks if your tests is a dummy implementation that:
  - passes all the 13 HashingBoxes; 
  - fails all the 13 HashingBoxes; or
  - fails (all) the correct HashingBox(es).

- You do not need to consider the following cases:
  - When the `Thing` parameter passed to the `hash` function is null.
  - When any of the fields of the `Thing` parameter passed to the `hash` function is null.

- An example test method, `exampleTestCase`, has been provided for your 
  reference in the `Q2Test` class.

Note that since the skeleton for the parameterised test is already set up, 
your test only needs to be run on the `hashingBox` parameter, and they 
will be run against all 13 HashingBoxes.
<br><br>

* * *

#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `Q2Test.java`

