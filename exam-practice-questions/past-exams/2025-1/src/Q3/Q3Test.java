import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class Q3Test {
    /**
     * Task (A) Tokeniser
     */
    @Test(timeout = 1000)
    public void tokenise_single_value() {
        String buffer = "3";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[INT] 3", tokeniser.takeNext().toString());
        assertNull(tokeniser.takeNext());
    }

    @Test(timeout = 1000)
    public void tokenise_expression() {
        String buffer = "x + y * 5 + 2 - 1";
        Tokeniser tokeniser = new Tokeniser(buffer);

        assertEquals("[VARIABLE] x", tokeniser.takeNext().toString());
        assertEquals("[OPERATOR] +", tokeniser.takeNext().toString());
        assertEquals("[VARIABLE] y", tokeniser.takeNext().toString());
        assertEquals("[OPERATOR] *", tokeniser.takeNext().toString());
        assertEquals("[INT] 5", tokeniser.takeNext().toString());
        assertEquals("[OPERATOR] +", tokeniser.takeNext().toString());
        assertEquals("[INT] 2", tokeniser.takeNext().toString());
        assertEquals("[OPERATOR] -", tokeniser.takeNext().toString());
        assertEquals("[INT] 1", tokeniser.takeNext().toString());
        assertNull(tokeniser.takeNext());
    }

    @Test(timeout = 1000)
    public void tokenise_evaluation() {
        String buffer = "x - 3; y + x + 5; y;";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[VARIABLE] x", tokeniser.takeNext().toString());
        assertEquals("[OPERATOR] -", tokeniser.takeNext().toString());
        assertEquals("[INT] 3", tokeniser.takeNext().toString());
        assertEquals("[EOL] ;", tokeniser.takeNext().toString());
        assertEquals("[VARIABLE] y", tokeniser.takeNext().toString());
        assertEquals("[OPERATOR] +", tokeniser.takeNext().toString());
        assertEquals("[VARIABLE] x", tokeniser.takeNext().toString());
        assertEquals("[OPERATOR] +", tokeniser.takeNext().toString());
        assertEquals("[INT] 5", tokeniser.takeNext().toString());
        assertEquals("[EOL] ;", tokeniser.takeNext().toString());
        assertEquals("[VARIABLE] y", tokeniser.takeNext().toString());
        assertEquals("[EOL] ;", tokeniser.takeNext().toString());
        assertNull(tokeniser.takeNext());
    }


    /**
     * Task (B) Parser
     */
    @Test(timeout = 1000)
    public void parse_value() {
        String buffer = "3";
        Parser parser = new Parser(new Tokeniser(buffer));
        assertEquals("3", parser.parse().show());
    }

    @Test(timeout = 1000)
    public void parse_single_operation() {
        String buffer = "x + 1";
        Parser parser = new Parser(new Tokeniser(buffer));
        assertEquals("(x ADD 1)", parser.parse().show());
    }

    @Test(timeout = 1000)
    public void parse_exps_recursive() {
        String buffer = "x + y * 5 + 2 - 1";
        Parser parser = new Parser(new Tokeniser(buffer));
        assertEquals("(x ADD (y MULTIPLY (5 ADD (2 SUBTRACT 1))))", parser.parse().show());
    }

    @Test(timeout = 1000)
    public void parse_exps() {
        String buffer = "x + 3; y - x + 5; y;";
        Parser parser = new Parser(new Tokeniser(buffer));
        assertEquals("(x ADD 3); (y SUBTRACT (x ADD 5)); y", parser.parse().show());
    }

}

