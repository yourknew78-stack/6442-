public class FoodFactory {
    public static Food getFood(Ingredient ingredient, FoodState state) {
        if (state instanceof RawState) {
            return getRawFood(ingredient);
        }
        if (state instanceof ReadyState) {
            return getReadyFood(ingredient);
        }
        if (state instanceof BurntState) {
            return getBurntFood(ingredient);
        }
        return null;
    }

    /** By State */
    public static Food getRawFood(Ingredient ingredient) {
        return new Food(ingredient, RawState.getState(), 0);
    }

    public static Food getReadyFood(Ingredient ingredient) {
        return new Food(ingredient, ReadyState.getState(), Ingredient.getPrepTime(ingredient));
    }

    public static Food getBurntFood(Ingredient ingredient) {
        return new Food(ingredient, BurntState.getState(), Ingredient.getPrepTime(ingredient)+2);
    }
}
