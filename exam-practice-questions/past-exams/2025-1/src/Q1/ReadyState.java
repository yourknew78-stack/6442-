public class ReadyState implements FoodState {
    /** Singleton */
    private static volatile ReadyState instance = new ReadyState();
    public static ReadyState getState() {
        if (instance == null) {
            synchronized (ReadyState.class) {
                if (instance == null) {
                    instance = new ReadyState();
                }
            }
        }
        return instance;
    }


    /**
     * Prepare the food.
     *
     * Increasing elapsedPrepTime for the food,
     * and transition it to the BurntState if exceeded the required
     * preparation time (as recorded in the Ingredient enum) by 2.
     *
     * Note that food with elapsedPrepTime being the required prepTime
     * plus one can remain in the ReadyState.
     *
     * Assumption: the food is already in a correct state
     * with respect to its elapsedPrepTime.
     */
    @Override
    public void prepare(Food food) {
        // TODO - Start your code


        // TODO - End your code
    }
}
