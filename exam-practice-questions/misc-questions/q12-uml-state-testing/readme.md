## UML & Design Pattern & Software Testing

In this question, you are expected to implement a vending machine program by applying the `state` design pattern based on the UML diagram and the state transition diagram in the file `diagrams.pdf`.

Note that it is normal that you see compile errors in the `StateTest.java` at first because you are expected to create all the necessary classes and methods to remove all the errors and pass the test cases.

The three files `Event.java`, `State.java` and `VendingMachine.java` are already provided. In the `VendingMachine.java`, the state has been initialised as `Idle` and you are required to complete the getState() and the setState() methods.

There are 6 different events that trigger the state transitions of the vending machine:
* `AnyButton` : user-triggered event
* `ItemSelected` : user-triggered event
* `ItemPaid` : user-triggered event
* `ItemDispensed` : machine-triggered event
* `Timeout` : machine-triggered event
* `Cancelled` : user-triggered event

`IMPORTANT NOTES`:
* Please do not add any package names or third-party libraries at the head of any of the above uploaded files. Besides, you are only allowed to use the standard Java libraries. Otherwise, your code may fail to be compiled by the auto-marker and you may lose marks due to this type of compile error.
* Please make sure your solutions do compile. We will NOT help correct your solutions if your solutions fail to compile. You will get `ZERO` mark if your solutions have compile errors that are not caused by adding unknown package names or third-party libraries.
* Some test cases are provided to assist your understanding, but passing them does not guarantee you will get full marks. Remember that we will use different test cases to mark your solutions. You are free to add your own test cases to increase your confidence that your solution is correct and robust.

* * *
* * *

##### You are expected to submit the following 6 files:
* `VendingMachine.java`
* `Idle.java`
* `WaitingToSelect.java`
* `WaitingToPay.java`
* `WaitingToDispense.java`
* `ReadyToCollect.java`

Please make sure you submit the `CORRECT` files with the `CORRECT` names. Good luck!
