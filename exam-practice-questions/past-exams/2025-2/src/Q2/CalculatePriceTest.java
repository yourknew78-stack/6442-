import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class CalculatePriceTest {

    private final Pricing parameters;
    private final Integer expected;

    public CalculatePriceTest(Pricing parameters, Integer expected) {
        this.parameters = parameters;
        this.expected = expected;
    }


    /**
     * Provides test data for the .isValid() method for Question instances.
     *
     * Each element in the returned collection is an array of two elements:
     * -  a Question instance with specific values.
     * -  a boolean representing the expected result of isValid().
     *
     * This data is used to generate multiple test cases that validate the behavior
     * of the isValid() across a variety of input conditions. The goal is to achieve
     * complete branch coverage using the minimal set of test cases.
     *
     * Example:
     *         return Arrays.asList(new Object[][]{
     *                  {new Pricing(3, 1, "id1"), 10},
     *                  ...
     *         }
     *
     * @return a collection of test cases to be used in the parameterized test.
     */
    @Parameterized.Parameters(name = "{index}: isValid({0}) = {1}")
    public static Collection<Object[]> data() {
        // TODO - Start your code here

        return Arrays.asList(new Object[][]{
                //  Add your test cases here, e.g.
                // {new Pricing(3, 1, "id1"), 10},

        });

        // TODO - End your code here
    }

    public Integer callCalculatePrice(Pricing pricing) {
        return Pricing.calculatePrice(pricing.unitPrice, pricing.quantity, pricing.memberId);
    }

    @Test(timeout = 1000)
    public void testIsValid() {
        assertEquals(expected, callCalculatePrice(parameters));
    }
}