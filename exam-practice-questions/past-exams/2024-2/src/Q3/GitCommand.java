public class GitCommand {
    GitCommandType type;

    String value;

    public GitCommand(GitCommandType type) {
        this.type = type;
    }

    public GitCommand(GitCommandType type, String value) {
        this.type = type;
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return String.format("%s%s", this.type.getString(), this.value!=null ? " (" + this.value + ")." : ".").trim();
    }



    public enum GitCommandType {
        GIT_ADD("Added files to the staging area"),
        GIT_COMMIT("All staged files have been committed"),
        GIT_PUSH("All commits have been pushed");

        String s;
        GitCommandType(String s) {
            this.s = s;
        }

        String getString() {
            return this.s;
        }

    }
}
