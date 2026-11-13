/**
 * Abstract representation of a Tree data structure.
 */
public abstract class Tree {
    Node root;
    Tree left;
    Tree right;

    /**
     * Inserts a new node with the specified ID into the tree.
     *
     * A mutable implementation is expected.
     *
     * Maintain the tree order during insertion, while assuming that
     * the tree is properly ordered prior to insertion.
     *
     * @param id The ID of the new node to insert.
     */
    public abstract void insert(int id);

    /**
     * Finds a node with the specified ID in the tree.
     *
     * @param id The ID to search for.
     * @return The tree node containing the specified ID, or null if not found.
     */
    public abstract Tree find(int id);

    /**
     * Checks whether the tree maintains a correct ordering.
     * @return True if the tree is correctly ordered, false otherwise.
     */
    public abstract boolean isOrdered();

    /**
     * Calculates and returns the level of the current node
     * in the tree while labelling it.
     *
     * Apply label to all descendant nodes from the root of the current tree.
     *
     * @return The level of the current tree node.
     */
    public abstract int getLevelAndLabel();

    /**
     * Displays the tree structure starting from the root.
     * @return A formatted string representing the tree structure.
     */
    public abstract String show();

    /**
     * Displays the tree structure starting from the root.
     * @param depth The depth of the current node, with the root starting at depth=0.
     * @return A formatted string representation of the tree from the specified layer.
     */
    public abstract String show(int depth);


    @Override
    public String toString() {
        return this.show();
    }

    protected int getId() {
        return this.root.getId();
    }
}
