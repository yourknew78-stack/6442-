import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TokeniserParserTest {
    /**
     * Task (A)
     */
    @Test(timeout = 1000)
    public void tokenise_TYPE() {
        String buffer = "a";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[TYPE]", tokeniser.getCurrentToken().toString());
    }

    @Test(timeout = 1000)
    public void tokenise_OCCURRENCE() {
        String buffer = "3";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[OCCURRENCES] 3", tokeniser.getCurrentToken().toString());
    }

    @Test(timeout = 1000)
    public void tokenise_UNDO() {
        String buffer = "[CTRL][Z]";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[UNDO]", tokeniser.getCurrentToken().toString());
    }

    @Test(timeout = 1000)
    public void tokenise_SEPARATOR() {
        String buffer = "\n";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[SEPARATOR]", tokeniser.getCurrentToken().toString());
    }

    @Test(timeout = 1000)
    public void tokenise_CAPITALISE() {
        String buffer = "{CapsLock}";
        Tokeniser tokeniser = new Tokeniser(buffer);
        assertEquals("[CAPITALISE]", tokeniser.getCurrentToken().toString());
    }


    /**
     * Task (B)
     */
    @Test(timeout = 1000)
    public void getAllTokens_typing() {
        String buffer = "abc";
        Tokeniser tokeniser = new Tokeniser(buffer);

        Parser parser = new Parser(tokeniser);
        var tokens = parser.getAllTokens();
        assertEquals("[[TYPE], [TYPE], [TYPE]]", tokens.toString());
    }

    @Test(timeout = 1000)
    public void getAllTokens_typeAndOccurrence() {
        String buffer = "a3";
        Tokeniser tokeniser = new Tokeniser(buffer);

        Parser parser = new Parser(tokeniser);
        var tokens = parser.getAllTokens();
        assertEquals("[[TYPE], [OCCURRENCES] 3]", tokens.toString());
    }

    @Test(timeout = 1000)
    public void getAllTokens_typeAndUndo() {
        String buffer = "a[CTRL][Z]";
        Tokeniser tokeniser = new Tokeniser(buffer);

        Parser parser = new Parser(tokeniser);
        var tokens = parser.getAllTokens();
        assertEquals("[[TYPE], [UNDO]]", tokens.toString());
    }

    @Test(timeout = 1000)
    public void getAllTokens_redo() {
        String buffer = "[CTRL][Y]b";
        Tokeniser tokeniser = new Tokeniser(buffer);

        Parser parser = new Parser(tokeniser);
        var tokens = parser.getAllTokens();
        assertEquals("[[REDO], [TYPE]]", tokens.toString());
    }

}

