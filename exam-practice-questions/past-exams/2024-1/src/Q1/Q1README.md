## Question 1: Code Understanding, Design Patterns, SOLID principles (20 marks)

Implement methods in a set of classes that simulate the creation of different animals, which
is part of a larger system that manages different types of animals using
factory methods.

- _Animal_: An abstract class representing the general concept of an animal.
- Dog: A concrete subclass of Animal representing a dog.
- Penguin: A concrete subclass of Animal representing a penguin.
- _AnimalCreator_: An abstract class for creating Animal objects.
- DogCreator: A concrete subclass of  AnimalCreator for creating Dog instances.
- PenguinCreator: A concrete subclass of  AnimalCreator for creating Penguin instances.

***

Each subclass of AnimalCreator is used to create a specific type of Animal and set their attributes:
1. `ageConfig` sets the age whereas `levelConfig` sets the activity level (i.e., the total time that animals spend being active, measured in hours per day) of an animal. These attributes must be defined for the creation of new Animal instances (e.g., a DogCreator with ageConfig=3 and levelConfig=3 will always create dogs with an activity level of '3' and an age of '3').
2. Each subclass of AnimalCreator must iterate through a fixed list of `NAMES` in order to assign names to the newly created animals.
  The names are predefined within the corresponding AnimalCreator subclasses. For example, in the `DogCreator` subclass, there are four predefined names: `Rex`, `Buddy`, `Max` and `Rocky`, which means that the first `Dog` created by the `DogCreator` should be named `Rex` and the next `Buddy`, and so on.
  The naming process continues by iterating through the list. For example, after iterating through the four names, the fifth name will be `Rex` again.

### Task A) Implement the `getNextName()` method
- Implement the method `getNextName()` in `DogCreator` and `PenguinCreator` classes according to the provided specification (see item 2 above).

### Task B) Implement the constructor and the `createAnimal()` method in the `DogCreator` and `PenguinCreator` classes
- Implement the constructor in the `DogCreator` and `PenguinCreator` classes to correctly update `ageConfig` and `levelConfig` attributes.
  Note that if any of the attributes (`age` and `level`) are `null`, an appropriate default value must be used:
  - For `DogCreator`, use the default `ageConfig` and `levelConfig` defined in the  `AnimalCreator` superclass.

  - For `PenguinCreator`, use the following default values: `ageConfig=1` and `levelConfig=2`.

  - **Hint**: a subclass can call a constructor defined by its superclass using `super(parameter-list)`.
- Implement `createAnimal()` in the `DogCreator` and `PenguinCreator` classes.

Note that the `AnimalCreator` constructor has a precondition for creating animals. It validates whether the values passed in the `age` and `level` parameters are non-negative (>=0).
  - if any of the values are negative, throw an `IllegalArgumentException`.
  - `null` values are allowed and no exceptions should be thrown based on `null` values.
  - Example 1: `AnimalCreator(null, null)` is valid and no exception is thrown. 
  - Example 2: `AnimalCreator(-1, null)` and `AnimalCreator(2, -1)` are invalid. In these cases, the `IllegalArgumentException` must be thrown.

### Task C) Implement the `createAnimalTemplate()` and `createBatch()` methods in the abstract `AnimalCreator` class

- Implement the template method `createAnimalTemplate()`, which is used to create one animal instance for each call (See specification as comment in the `AnimalCreator` class).

- Implement the `createBatch()` method, which returns a list of _n_ animals. The method receives a `n` parameter to create _n_ animals using the `createAnimalTemplate()` method.


#### Additional Notes:
- The basic tests in `FactoryTest.java` can assist with your code development and understanding.
- Do not alter any of the method signatures, as doing so may result in receiving zero marks for the entire question.

<br><br>


* * *


#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `DogCreator.java`
* `PenguinCreator.java`
* `AnimalCreator.java`
