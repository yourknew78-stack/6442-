public enum TokenType {
    TYPE("x"),
    SEPARATOR("\n"),
    UNDO("[CTRL][Z]"),
    REDO("[CTRL][Y]"),
    OCCURRENCES("<numeric-value-following-the-rule>"),
    CAPITALISE("{CapsLock}");

    private final String str;

    TokenType(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
