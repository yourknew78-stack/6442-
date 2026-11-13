public class InputToken {
    private TokenType type;
    private Integer value = null;

    public InputToken(TokenType type, int value) {
        this.type = type;
        this.value = value;
    }

    public InputToken(TokenType type) {
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value  = value;
    }

    public String toString() {
        return String.format("[%s] %s", this.type, this.value!=null ? this.value : "").trim();
    }
}
