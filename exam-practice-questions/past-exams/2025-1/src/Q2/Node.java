public abstract class Node {
    private final int id;
    private String label;

    public Node(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
