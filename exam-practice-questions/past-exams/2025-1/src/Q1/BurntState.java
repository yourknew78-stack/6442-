public class BurntState implements FoodState {
    /** Singleton */
    private static volatile BurntState instance = new BurntState();
    public static BurntState getState() {
        if (instance == null) {
            synchronized (BurntState.class) {
                if (instance == null) {
                    instance = new BurntState();
                }
            }
        }
        return instance;
    }

    /**
     * Throw a RuntimeException when attempted to prepare
     * food that is already burnt.
     */
    public void prepare(Food food) {
        // TODO - Start your code


        // TODO - End your code
    }
}
