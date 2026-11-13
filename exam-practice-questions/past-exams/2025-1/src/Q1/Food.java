/** A class that represent ingredients that have been prepared (cooked) or are to be prepared. */
public class Food {
    Ingredient ingredient;
    FoodState state;
    int elapsedPrepTime;

    public Food(Ingredient ingredient, FoodState state, int elapsedPrepTime) {
        this.ingredient = ingredient;
        this.state = state;
        this.elapsedPrepTime = elapsedPrepTime;
    }

    public Food(Ingredient ingredient) {
        this(ingredient, RawState.getState(), 0);
    }


    /**
     * Check and fix the state of the food, then
     * prepare the food.
     *
     * Update the food by increasing the prep-time done,
     * and update state if there is a change in the state.
     */
    public void prepare() {
        // TODO - Start your code


        // TODO - End your code
    }


    /** check and update state if needed */
    public void checkState() {
        // TODO - Start your code


        // TODO - End your code
    }


    public void setState(FoodState state) {
        this.state = state;
    }

    public void setPrepTimeDone(int elapsedPrepTime) {
        this.elapsedPrepTime = elapsedPrepTime;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

}
