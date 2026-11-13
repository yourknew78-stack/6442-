## Question 3: Tokeniser and Parser

#### Implement a simple tokeniser and parser for Git commands.

The tokeniser needs to take user inputs, which are strings that represent 
simple Git commands (i.e., as a `buffer` passed to the Tokeniser), and process 
them into tokens (represented by the `GitToken` class).
The parser then takes the tokens and creates `GitCommand` instances.

* * *

The syntax of Git commands are represented in the following CFG 
(Context-Free Grammar):

```
<GitCommands> := <GitCommand><Sep> | <GitCommand><Sep> <GitCommands>
<GitCommand> := <GitAddCommand> | <GitCommitCommand> | <GitPushCommand>
<GitAddCommand> := git add <path>
<GitCommitCommand> := git commit
<GitPushCommand> := git push
<path> := .<any_characters_without_space/separator>
<Sep> := ;
```

Notes about `<path>`:
-  This is a simplified version of path (different from what is used in actual 
   `git add` commands). We accept any string that starts with `.` as a valid path.
-  While tokenising, you can assume any whitespace-delimited string starting 
   with "." is a `<path>`, ending immediately before the next space or 
   separator (";"). For example, if you encounter `git add .abcdegit commit;`, 
   you should add a `git` token, followed by an `add` token, then a `path` token 
   with value being `.abcdegit`, and finally a `commit` and a `separator` token.


Notes about spaces:
- Tokens that are written together in the CFG can either be placed together or 
  be separated by spaces (" "), e.g. both `git commit;` or `git commit ;` 
  are valid for `<GitCommand><Sep>`
- Furthermore, token strings can be separated by **one or more** spaces, 
  e.g., `git    add    ;` is also valid.
- You do not need to consider the occurrence of non-space whitespace characters
  (e.g. tabs, linebreaks).


* * *

### Task A: Tokeniser.

- Implement the `next()` method in the `Tokeniser` class.
  -  Tokenise the buffer string and create corresponding `GitToken`s with the 
     suitable `type` and `value` (only for `PATH`s).

Notes: 
-  The tokenisation process should be case-sensitive on the token strings, 
   i.e., `Git` or `Add` is not acceptable.
-  If you encounter a space-delimited string that does not match any
   of the tokens, set `currentToken` to `null` and return `null`.

### Task B: Parser.

- Implement the `parse()` method in the `Parser` class.
  -  Get tokens from the tokeniser, parse it based on the grammar for
     `<GitCommands>` and return a list of `GitCommand` instances accordingly.
  -  Return `null` if the tokens do not conform with the grammar. For example,
     with `git add .; git add .; git git commit;`, even though the tokens can 
     yield two valid `GitAddCommand` instances at the start, having
     `git git` means that the entire input is invalid, and thus null should be returned.

***


#### Note
- The basic tests in `Q3Test.java` may help with your code development.
- The docStrings of the methods you need to complete provide
  specific additional requirements. (And in general, you should read the 
  provided code since your solution needs to work in the context of those files).
- You may create helper methods within (and only within) `Tokeniser.java` and `Parser.java`.

<br>

* * *

#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `Tokeniser.java`
* `Parser.java`
