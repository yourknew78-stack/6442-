import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class Q1Test {

    static Recipe wagyuUdon = new Recipe(
            "Wagyu Udon",
            Map.of(
                    Ingredient.WAGYU, 3,
                    Ingredient.UDON, 1
            )
    );

    static Recipe biscuit = new Recipe(
            "Biscuit",
            Map.of(Ingredient.BISCUIT, 1)
    );

    static Recipe cupNoodles = new Recipe(
            "Cup Noodles",
            Map.of(Ingredient.CUP_NOODLES, 1)
    );


    /**
     * Task A
     */
    @Test(timeout = 1000)
    public void Q1a_1_readyToCook() {
        Set<Food> partial = Set.of(
                FoodFactory.getRawFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getRawFood(Ingredient.UDON)
        );
        assertFalse(Kitchen.readyToCook(wagyuUdon, partial));

        Set<Food> init = Set.of(
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getRawFood(Ingredient.WAGYU),
                FoodFactory.getRawFood(Ingredient.WAGYU),
                FoodFactory.getRawFood(Ingredient.UDON)
        );
        assertTrue(Kitchen.readyToCook(wagyuUdon, init));

        Set<Food> ready = Set.of(
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.UDON)
        );
        assertTrue(Kitchen.readyToCook(wagyuUdon, ready));

        Set<Food> unready = Set.of(
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getBurntFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.UDON)
        );
        assertFalse(Kitchen.readyToCook(wagyuUdon, unready));

        assertFalse(Kitchen.readyToServe(wagyuUdon, partial));
        assertFalse(Kitchen.readyToServe(wagyuUdon, init));
        assertTrue(Kitchen.readyToServe(wagyuUdon, ready));
    }

    @Test(timeout = 1000)
    public void Q1a_2_readyToServe() {
        Set<Food> partial = Set.of(
                FoodFactory.getRawFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getRawFood(Ingredient.UDON)
        );
        assertFalse(Kitchen.readyToServe(wagyuUdon, partial));

        Set<Food> init = Set.of(
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getRawFood(Ingredient.WAGYU),
                FoodFactory.getRawFood(Ingredient.WAGYU),
                FoodFactory.getRawFood(Ingredient.UDON)
        );
        assertFalse(Kitchen.readyToServe(wagyuUdon, init));

        Set<Food> ready = Set.of(
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.UDON)
        );
        assertTrue(Kitchen.readyToServe(wagyuUdon, ready));

        Set<Food> unready = Set.of(
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.WAGYU),
                FoodFactory.getBurntFood(Ingredient.WAGYU),
                FoodFactory.getReadyFood(Ingredient.UDON)
        );
        assertFalse(Kitchen.readyToServe(wagyuUdon, unready));
    }

    @Test(timeout = 1000)
    public void Q1a_3_ingredientsRequired_trivial() {
        Kitchen serviceWindow = new Kitchen(
                List.of(biscuit),
                Map.of()
        );
        assertEquals(Map.of(Ingredient.BISCUIT, 1), serviceWindow.ingredientsRequired());

        serviceWindow = new Kitchen(
                List.of(biscuit),
                Map.of(FoodFactory.getReadyFood(Ingredient.BISCUIT), 1)
        );
        assertEquals(Map.of(), serviceWindow.ingredientsRequired());
    }

    /**
     * This test checks the correctness of the `ingredientsRequired()` method in the `Kitchen` class.
     * It ensures that the method correctly computes the additional ingredients needed to fulfill the orders,
     * considering the current available food in the service window.
     *
     * Scenario explanation:
     *
     * Case 1: No food is available.
     *   Orders: [wagyuUdon (3 WAGYU + 1 UDON), biscuit (1 BISCUIT)]
     *   Expected ingredients required: 3 WAGYU, 1 UDON, 1 BISCUIT
     *
     * Case 2: 1 Ready BISCUIT is already available.
     *   Orders: [wagyuUdon, biscuit]
     *   Available food: 1 Ready BISCUIT
     *   Expected ingredients required: 3 WAGYU, 1 UDON (BISCUIT is already satisfied)
     *
     * Case 3: 1 Burnt BISCUIT is available (not usable)
     *   Orders: [wagyuUdon, biscuit]
     *   Available food: 1 Burnt BISCUIT
     *   Expected ingredients required: 3 WAGYU, 1 UDON, 1 BISCUIT (burnt food does not count)
     */
    @Test(timeout = 1000)
    public void Q1a_4_ingredientsRequired_simple() {
        Kitchen serviceWindow = new Kitchen(
                List.of(wagyuUdon, biscuit),
                Map.of()
        );
        assertEquals(Map.of(Ingredient.WAGYU, 3, Ingredient.UDON, 1, Ingredient.BISCUIT, 1), serviceWindow.ingredientsRequired());

        serviceWindow = new Kitchen(
                List.of(wagyuUdon, biscuit),
                Map.of(FoodFactory.getReadyFood(Ingredient.BISCUIT), 1)
        );
        assertEquals(Map.of(Ingredient.WAGYU, 3, Ingredient.UDON, 1), serviceWindow.ingredientsRequired());

        serviceWindow = new Kitchen(
                List.of(wagyuUdon, biscuit),
                Map.of(FoodFactory.getBurntFood(Ingredient.BISCUIT), 1)
        );
        assertEquals(Map.of(Ingredient.UDON, 1, Ingredient.WAGYU, 3, Ingredient.BISCUIT, 1), serviceWindow.ingredientsRequired());
    }

    /**
     * Task B
     */
    @Test(timeout = 1000)
    public void Q1b_1_foodStates_basic() {
        Food rawUdon = FoodFactory.getRawFood(Ingredient.UDON);

        // Becomes Ready after prepare is called the third time.
        rawUdon.prepare();
        assertEquals(RawState.getState(), rawUdon.state);
        rawUdon.prepare();
        assertEquals(RawState.getState(), rawUdon.state);
        rawUdon.prepare();
        assertEquals(ReadyState.getState(), rawUdon.state);

        rawUdon.prepare();
        assertEquals(ReadyState.getState(), rawUdon.state);

        rawUdon.prepare();
        assertEquals(BurntState.getState(), rawUdon.state);
    }


    @Test(timeout = 1000)
    public void Q1b_2_foodStates_burnt() {
        Food burntUdon = FoodFactory.getBurntFood(Ingredient.UDON);
        assertThrows(RuntimeException.class, burntUdon::prepare);
    }

}
