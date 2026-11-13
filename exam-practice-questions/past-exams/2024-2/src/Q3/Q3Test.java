import org.junit.Test;


import static org.junit.Assert.*;


public class Q3Test {
    /**
     * Task (A) Tokeniser
     */
    @Test(timeout = 1000)
    public void tokenise_single_git() {
        String buffer = "git";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[GIT]", tokeniser.getCurrentToken().toString());
    }

    @Test(timeout = 1000)
    public void tokenise_single_separator() {
        String buffer = ";";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[SEPARATOR]", tokeniser.getCurrentToken().toString());
    }

    @Test(timeout = 1000)
    public void tokenise_single_path() {
        String buffer = "./src/Q1";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[PATH] ./src/Q1", tokeniser.getCurrentToken().toString());
    }

    @Test(timeout = 1000)
    public void tokenise_invalid() {
        String buffer = "git";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[GIT]", tokeniser.getCurrentToken().toString());

        buffer = "oh mcdonald had da farm";
        tokeniser = new Tokeniser(buffer);
        assertNull(tokeniser.getCurrentToken());
    }

    @Test(timeout = 1000)
    public void tokenise_invalid_partially() {
        String buffer = "git add abc;";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[GIT]", tokeniser.getCurrentToken().toString());
        assertEquals("[ADD]", tokeniser.next().toString());
        assertNull(tokeniser.next());
        assertNull(tokeniser.next());
    }

    @Test(timeout = 1000)
    public void tokenise_add_tokens() {
        String buffer = "git add .;";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[GIT]", tokeniser.getCurrentToken().toString());
        assertEquals("[ADD]", tokeniser.next().toString());
        assertEquals("[PATH] .", tokeniser.next().toString());
        assertEquals("[SEPARATOR]", tokeniser.next().toString());
    }

    /**
     * Task (B) Parser
     */
    @Test(timeout = 1000)
    public void parse_add() {
        String buffer = "git add .;";
        Parser parser = new Parser(new Tokeniser(buffer));
        assertEquals("[Added files to the staging area (.).]", parser.parse().toString());
    }

    @Test(timeout = 1000)
    public void parse_commit() {
        String buffer = "git commit;";
        Parser parser = new Parser(new Tokeniser(buffer));
        assertEquals("[All staged files have been committed.]", parser.parse().toString());
    }

    @Test(timeout = 1000)
    public void parse_push() {
        String buffer = "git push;";
        Parser parser = new Parser(new Tokeniser(buffer));
        assertEquals("[All commits have been pushed.]", parser.parse().toString());
    }

    // TODO - Note:
    //  you are welcome to add more tests to ensure your solution is correct,
    //  but it is completely OPTIONAL. To manage time effective and avoid losing significant marks,
    //  we encourage you prioritise completing basic tests (where provided) or make reasonable
    //  progress on each question BEFORE aiming for a perfect solution on any one question.
}

