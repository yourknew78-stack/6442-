import java.util.Map;

/** An enum of Ingredient Types */
public enum Ingredient {
    WAGYU, UDON, SALMON, CUP_NOODLES, EGG, LETTUCE, TOMATO, SASHIMI_GRADE_SALMON, BISCUIT, MILK;

    private static final Map<Ingredient, Integer> prepTimeMap = Map.of(
            WAGYU, 1,
            UDON, 3,
            CUP_NOODLES, 3,
            SALMON, 2,
            EGG, 1,
            LETTUCE, 0,
            TOMATO, 0,
            SASHIMI_GRADE_SALMON, 0,
            BISCUIT, 0,
            MILK, 0
    );

    public static int getPrepTime(Ingredient ingredient) {
        return prepTimeMap.getOrDefault(ingredient, 0);
    }

}
