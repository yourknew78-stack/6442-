public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value  = value;
    }

    public String toString() {
        return String.format("[%s] %s", this.type, this.value!=null ? this.value : "").trim();
    }
}
