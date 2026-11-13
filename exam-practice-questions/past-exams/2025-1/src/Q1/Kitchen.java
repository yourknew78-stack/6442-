import java.util.*;

public class Kitchen {
    List<Recipe> orders;
    Map<Food, Integer> availableFood;

    public Kitchen(List<Recipe> orders, Map<Food, Integer> availableFood) {
        this.orders = orders;
        this.availableFood = availableFood;
    }

    /**
     * Check whether we have the sufficient quantity of food for a recipe.
     *
     * Verifies the availability of sufficient number of available (raw or ready)
     * food as required in the recipe.
     *
     * @return true if there are enough available food to prepare the recipe, and false otherwise.
     */
    public static boolean readyToCook(Recipe recipe, Set<Food> ava) {
        // TODO - Start your code


        return false;
        // TODO - End your code
    }

    /**
     * Check whether we have the sufficient food to complete a recipe.
     *
     * Verifies the availability of sufficient number of food in the ReadyState as required in the recipe.
     *
     * @return true if there are enough available food to complete the recipe, and false otherwise.
     */
    public static boolean readyToServe(Recipe recipe, Set<Food> ava) {
        // TODO - Start your code


        return false;
        // TODO - End your code
    }

    /**
     * Determines the additional ingredients required to complete all orders.
     *
     * Note that burnt food in `availableFood` can not be considered available.
     *
     * Example:
     *      If we need to complete a Wagyu Udon (the recipe requires 3 wagyu and 1 udon) and a biscuit
     *      (requiring 1 biscuit) while we only have one unit of biscuit available,
     *      we will need **3 WAGYUs** and **1 UDON** to complete the orders.
     *
     * @return A map where the key is the ingredient item, and the
     *         value is the additional quantity needed to fulfill all orders.
     *         The map shall contain only the ingredient items that are insufficient.
     */
    public Map<Ingredient, Integer> ingredientsRequired() {
        // TODO - Start your code


        return null;
        // TODO - End your code
    }

}
