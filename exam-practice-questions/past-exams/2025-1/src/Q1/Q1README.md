# Question 1: UML, Design Patterns

## Kitchen Management System

You are hired by a restaurant which is planning to monitor their meal preparation
steps electronically. The system needs to manage meals that are prepared following 
standard recipes.

You are expected to apply the state and singleton design patterns and complete 
the program.


* * *

## Introduction

A `Recipe` specifies the type and number of `Ingredient`s required for a dish.

The different types of ingredients are listed in the `Ingredient` enum.
An ingredient may (or may not) need to be prepared before it can be used. The 
required preparation time is stored in the `prepTimeMap` in `Ingredient.java`.

The **state** of food is represented by the corresponding state class under the 
`FoodState` interface. The available states include `RawState`, `ReadyState`, and
`BurntState`.

In the `Food` class, the instance field `ingredient` stores the type of ingredient; 
`elapsedPrepTime` indicates the amount of preparation time that has elapsed; 
and `state` represents the preparation state of the food.

Note that `elapsedPrepTime` is considered the source of truth, i.e.,
if `prepTimeDone` mismatches the `state` of an ingredient instance, `elapsedPrepTime` is
considered accurate and (for the purpose of the `prepare` function) `state` should be updated
accordingly.

A recipe is completed if and only if all required ingredients are in the `ReadyState`.


### Transition of Ingredient States
- `RawState`: represents ingredients that are not yet ready.
- `ReadyState`: represents ingredients that are ready (and not burnt), i.e., 
   if `prepare()` has been triggered `<required PrepTime>` or `<required PrepTime> + 1` times.
  (i.e., the ingredient can be prepared for 1 extra unit of time as buffer before it becomes overcooked).
- `BurntState`: represents ingredients that are burnt.

For example, the `prepTimeMap` shows that `UDON` requires **3 units** of preparation time.
So, an UDON ingredient (initially with a `elapsedPrepTime` of 0 and in the `RawState`) remains
in the raw state after `prepare` is triggered twice. When `prepare` is triggered a third time,
the ingredient should transition to the `ReadyState`.
- The ingredient should remain in the `ReadyState` after being prepared a fourth time, and 
  should transition to the `BurntState` after being prepared for 2 additional units of time 
  beyond the required prep time.

* * *

## Tasks

### Task A: Recipe and Food Problem Statement.

Complete the following methods in the `Kitchen` class
- `readyToCook` which takes in a `Recipe` instance and a `Set<Food>` 
  and checks whether we have the sufficient food (either Raw or Ready) required;
- `readyToServe` which takes in a `Recipe` instance and a `Set<Food>`
  and checks whether we have the sufficient Ready-state food required for the recipe; and
- the instance method `ingredientsRequired` which compares the `availableFood` against the `orders`,
  and find the additional food required to complete all orders.
   - For instance, if we have wagyu, udon, and biscuit in our orders while we only have a biscuit available,
     we will need **3 WAGYUs** and **1 UDON** to complete the orders. 
   - (see the test case named `Q1a_4_ingredientsRequired_simple` for details)


### Task B: Food State Management.

- **Complete the Classes**:
  - Complete the `checkState` method in `Food`.
    - Check and fix the ingredient's `state` by referring to `elapsedPrepTime`.
      Note that only updates in `state` is expected.
    - For instance, The prep-time required for the `CUP_NOODLES` ingredient is 3.
      An instance of Food with `CUP_NOODLES` as the ingredient, a elapsedPrepTime
      of 2 but a state `ReadyState` is in the wrong state, and the method should
      correct the food so that it is in a `RawState` instead.

  - Complete the `prepare` method in `RawState`, `ReadyState`, and `BurntState`.
    - In `RawState` or `ReadyState`, the `prepare` method should perform Transition of FoodState, 
      and update `elapsedPrepTime` and `state` (if needed) of the food.
    - In `BurntState`, however, the method only needs to throw a `RuntimeException`.
    - Read the docString in the methods of the food-state classes for detailed specifications.

  - Complete the `prepare` method in `Food` and all concrete classes that implements `FoodState`.
    - `prepare` in `Food` must first ensure the accuracy of `state` (same requirement for `checkState`),
      then complete preparation by triggering `prepare()` from the corresponding food-state classes.
    - After each call to `prepare`, the state of the food should change to reflect any required change in 
      `elapsedPrepTime` and `state`.

-----------

#### Additional Notes:

- You may create helper methods within `Food.java`, but **NOT** helper fields.
- You must not create or make changes to any existing fields in ANY of the classes.
- Do not alter any of the method signatures, as doing so may result in receiving 
  zero marks for the entire question. This means that you can not add, for example, 
  `throw IOException` to any methods.
- You must not change any existing methods, except for those explicitly marked
  as editable by comments. Only modify sections of code that are clearly
  indicated and delimited by these comments.
- The basic tests in `Q1Test.java` can assist with your code development and understanding.
<br><br>


* * *


#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `Kitchen.java`
* `Ingredient.java`
* `RawState.java`
* `ReadyState.java`
* `BurntState.java`
