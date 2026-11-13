public class Tokeniser {
    private String buffer;
    private InputToken currentToken;

    public Tokeniser(String input) {
        this.buffer = input;
        this.next();
    }

    public InputToken getCurrentToken() {
        return this.currentToken;
    }


    /**
     * Task (A)
     * Tokenise the remaining buffer to get the next token.
     * <p>
     * Assign the next token to this.currentToken after it is found, and
     * update the buffer to exclude the tokenised part. Otherwise, if there
     * is not a valid next token, this.currentToken should be null and the
     * buffer should remain unchanged.
     * <p>
     * Notes:
     *   -  The tokenisation process should be case-insensitive on the token strings.
     */
    public void next() {
        // TODO - Your code starts here


        // TODO - Your code ends here
    }


    public boolean isEmpty() {
        return this.buffer.isEmpty();
    }


}
