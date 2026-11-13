public class Tokeniser {
    private String buffer;
    private Token currentToken;

    public Tokeniser(String input) {
        this.buffer = input.trim();
    }


    /**
     * Task (A)
     * Tokenise the remaining buffer to get the next token.
     * <p>
     * Assign the next token to this.currentToken after it is found, and
     * update the buffer to exclude the tokenised part. Return the
     * next token (i.e., the same as this.currentToken).
     *
     * If the upcoming buffer does not represent a valid next token,
     * assign null to `this.currentToken` and the buffer should remain unchanged.
     * <p>
     * Notes: The tokenisation process should be case-sensitive on the token strings.
     */
    public Token next() {
        // TODO - Start your code

        return null;
        // TODO - End your code
    }

    public Token takeNext() {
        if (!this.hasNext()) {
            this.currentToken = null;
            return null;
        }

        Token nextToken = this.next();

        this.buffer = this.buffer.substring(nextToken.getValue().length());
        this.buffer = this.buffer.trim();
        this.currentToken = nextToken;
        return nextToken;
    }

    public Token current() {
        return this.currentToken;
    }


    public boolean hasNext() {
        return (!this.buffer.isEmpty());
    }

}
