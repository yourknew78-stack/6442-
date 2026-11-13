## Question 3: Tokeniser and Parser (30 marks)

#### You must implement a tokeniser and parser for a simple Word Processor.

The tokeniser needs to take user inputs which are represented by strings of 
characters (i.e., as `buffer` passed to the Tokeniser), and process then
into tokens (represented by the `InputToken` class).

* * *

The different types of tokens are listed in the `TokenType` class and summarised
in the following table.

| Token Strings                          | Enum Value                                  |
|----------------------------------------|---------------------------------------------|
| "\n" (Note: `\n` is one character)     | SEPARATOR                                   | 
| "[CTRL][Z]"                            | UNDO (Undo action)                          |
| "[CTRL][Y]"                            | REDO (Redo action)                          |
| A non-negative integer less than 1000  | OCCURRENCES (with the number being `value`) |
| "{CapsLock}"                           | CAPITALISE (Capitalise)                     |
| Any English letters (not listed above) | TYPE                                        |

- Token strings can either be placed together (e.g., "a[CTRL][Z]" represents two 
  tokens. A user who typed "a" then pressed the Ctrl + Z keys) 
  or be separated by spaces (e.g., "a b  C" represents three tokens).
- The tokeniser must consider any consecutive digits to be a candidate for 
  `OCCURRENCES` (e.g., "2000" in "a 2000 b" must be treated as for an
  `OCCURRENCES` token, which will be invalid since it exceeds the upper limit).


For example,
- The buffer "abc" should be tokenised into three `TYPE` tokens. (Note that the 
  actual character is not recorded).
- The buffer "a3" (used to represent typing "a" three times) should be tokenised 
  into two tokens: `TYPE` with no value assigned and `OCCURRENCES` with a value of 3.

* * *

### Your tasks are:

-  (A) Implement the `next()` method in the `Tokeniser` class.
-  (B) Implement the `getAllTokens()` method in the `Parser` class.

You may need to refer to the docstrings of the methods for more information.

#### Note that
- The basic tests in `TokeniserParserTest.java` may help with your code development.
- You may create helper methods within (and only) within `Tokeniser.java` and `Parser.java` files.

<br>

* * *

#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `Tokeniser.java`
* `Parser.java`
