public enum GitTokenType {
    GIT("git"),
    ADD("add"),
    COMMIT("commit"),
    PUSH("push"),
    PATH("<file-path>"),
    SEPARATOR(";");

    private final String str;

    GitTokenType(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
