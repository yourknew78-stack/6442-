# Question 5: UML, Design Patterns (25 marks)


## Introduction 

You are given a UML diagram that demonstrate the design of a favourites management system that applies the Observer design pattern, where a user’s favourites list notifies observers (like a notification service) whenever items change.


## Tasks

Implement the class(es) and interface(s) according to the provided UML diagram (see src/Q5/UML_ob.png).

Your work will be assessed on three main areas:

### Task A: Class Structure

Create the classes in accordance with the provided UML diagram. You will be marked based on
how accurately your code reflects the structure and relationships defined in the UML, for
  - `Observer`, `NotificationService`, `Item`, `User`, and `FavouriteList`.

As a hint, a partial implementation for the `FavouriteList` class has already been provided in `FavouriteList.java`. 
You can uncomment the code and get started from there, or delete the provided code and get started with your own 
implementation. Note that some parts of the provided code does not follow the UML and you should fix it.

Tip: In IntelliJ, you can comment/uncomment code with the shortcut `Ctrl`+`/` (Linux) 
after selecting multiple lines.


Notes
  - Ensure that all classes, fields, and methods **exactly match the names and signatures** specified in the provided UML diagram. 
    Ensure there are no spelling mistakes and that you use the correct case for all names. 
  - Do not introduce any extra fields or methods, except that **you may implement constructor(s),
    getters, and setters** in any of the classes as needed to complete the method implementation.

### Task B: Design Pattern Implementation

Implement methods in the respective classes:

  - In `FavouriteList`,
    - `attach` adds the observer once, without duplicates. No-op if the parameter is null.
      (Note that "no-op" means no-operations, i.e., does nothing when executed)
    - `detach` remove the observer passed in if present, no-op otherwise.
    - `addItem`: adds the `item` to the list of items if no items with the same id exists.
       No-op otherwise. If successfully added, call `notify("added", item)`.
    - `removeItem`: removes the `item` from the list of items if an item with the same id and name exists.
      No-op otherwise. If successfully removed, call `notify("removed", item)`.
  - In `NotificationService`,
    - `update` should print a message which includes the channel, 
      event, item id and item name. For instance, the printed message may look like 
      `[channel ABC] event: added, item #I1 (exam question)`


### Task C: Demonstration / Main Program

In the (static) main function of `Main.java`, 
-  Create two Users (with id = "1" and id = "2", respectively) and attach them (through their FavouriteList) 
   to two observers that are NotificationService with `channel` "ABC" and "DEF", respectively.
-  Add items to the favouriteList of the users:
   -  Add an item (**item1**) with id `I1` and name `Item 1` to both users' favouriteList.
   -  Add an item (**item2**) with id `I2` and name `Item 2A` to user2's favouriteList.
   -  Add an item (**item3**) with id `I2` and name `Item 2B` to user2's favouriteList.
-  Detach the observer from the second user (with id = "2"), then
    -  Remove **item1** from both users' favouriteLists.


-----------

#### Additional Notes:

- The basic tests in `Q5Test.java` provide basic name-checkings. 
  Note that the checking are NOT comprehensive and you should read and understand the 
  specification above instead of relying on the tests.
  Note that passing the tests does not necessarily mean you will receive marks for the tasks, although it is a good indicator. You must follow the specifications in the UML.

* * *


#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `Main.java`, and
* all the Java files corresponding to the classes and interfaces shown in the UML diagram, and ONLY those files. 
