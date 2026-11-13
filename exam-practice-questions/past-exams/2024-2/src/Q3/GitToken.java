public class GitToken {
    private GitTokenType type;
    private String value = null;

    public GitToken(GitTokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public GitToken(GitTokenType type) {
        this.type = type;
    }

    public GitTokenType getType() {
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
