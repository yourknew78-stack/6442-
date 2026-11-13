## Question 3: Tokeniser and Parser

### Implement a tokeniser and parser for simple mathematical expressions.

Several types of simple mathematical expressions are handled by this software.

This includes simple expressions with a single value, i.e.,
an **integer expression** (e.g. for the value 23) or **variable expression** (e.g. "x").

**Operation expressions** can be used to create expressions which add, subtract, or
multiply two expressions, forming expressions like:
- `3 + 2`,
- `5 - y`, or
- `x + y * 2`
 
Finally, we have **sequence expressions** that comprises a list of expressions, like
`3 + 2; 5 - y; x + y * 2`


### Process

The tokeniser converts strings (`buffer`) that represent elements in an expression 
into a series of tokens (`Token` class), each of one of the token types 
(in `TokenType` enum): `INT`, `VARIABLE`, `OPERATOR`, and `EOL` (end of line).

The parser then creates a corresponding expression using the subclasses of `Exp`,
namely, `IntExp`, `VarExp`, `OpExp` (operation expression) and `SeqExp`.


### Grammar
The grammar for an expression can be defined recursively as follows:
```
<S>     :=  <SeqExp>
<SeqExp>   :=  <SingleExp> | <SingleExp> <EOL> | <SingleExp> <EOL> <SeqExp>
<SingleExp>   :=  <Value> <OP> <SingleExp> | <Value>
<Value> :=  <VAR> | <INT>
<VAR>   :=  a | b | c | ... | x | y | z
<INT>   :=  an integer
<OP>    :=  + | - | *
<EOL>   :=  ;
```

Note that the tokens for variables (`<VAR>`) are case-sensitive, i.e.,
`a`, ..., `z` may correspond to a token of `VARIABLE` type, but
not `A`.

`EOL`: end-of-line symbol.

Whitespaces are acceptable between tokens, but not within a single token 
(e.g. `3 4` must be tokenised as two tokens).

* * *

### Task A: Tokeniser.

Implement the `next()` method in the `Tokeniser` class.
-  Tokenise the buffer string and create corresponding `Token`s with the 
   suitable `type` and `value`.


### Task B: Parser.

Implement the `parse()` method in the `Parser` class.

- Create expressions based on the grammar.
  -  Return `null` if the tokens do not conform with the grammar rules (invalid).
  -  Check the simple test cases in `Q3Test.java` for examples.


***


#### Note
- The basic tests in `Q3Test.java` may help with your code development.
- The docStrings of the methods you need to complete provide
  specific additional requirements. (And in general, you should read the 
  provided code since your solution needs to work in the context of those files).
- You may create helper methods within (and only within) 
  `Tokeniser.java` and `Parser.java`.
- You must not create helper fields or make change to any existing method/field signatures.

<br>

* * *

#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `Tokeniser.java`
* `Parser.java`
