public class RawState implements FoodState {
    private static volatile RawState instance = new RawState();
    public static RawState getState() {
        if (instance == null) {
            synchronized (RawState.class) {
                if (instance == null) {
                    instance = new RawState();
                }
            }
        }
        return instance;
    }


    /**
     * Prepare the food.
     *
     * Increasing elapsedPrepTime for the food,
     * and transition it to the ReadyState if reached the required
     * preparation time (as recorded in the Ingredient enum).
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
