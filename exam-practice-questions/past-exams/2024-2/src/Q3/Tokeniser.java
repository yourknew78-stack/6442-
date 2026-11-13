public class Tokeniser {
    private String buffer;
    private GitToken currentToken;

    public Tokeniser(String input) {
        this.buffer = input;
        this.next();
    }

    public GitToken getCurrentToken() {
        return this.currentToken;
    }


    /**
     * Task (A)
     * Tokenise the remaining buffer to get the next token.
     * <p>
     * Assign the next token to this.currentToken after it is found, and
     * update the buffer to exclude the tokenised part.
     * If the upcoming buffer does not represent a valid next token,
     * assign null to `this.currentToken` and the buffer should remain unchanged.
     * <p>
     * Notes: The tokenisation process should be case-sensitive on the token strings.
     */
    public GitToken next() {
        // Please feel free to change or remove any code provided within your coding area.
        // TODO - Your code starts here


        return new GitToken(null);
        // TODO - Your code ends here
    }

    public boolean hasNext() {
        return (this.currentToken != null);
    }

    public boolean isEmpty() {
        return this.buffer.isEmpty();
    }

}
