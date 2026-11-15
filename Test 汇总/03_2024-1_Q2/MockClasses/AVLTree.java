import java.util.LinkedList;
import java.util.List;


public abstract class AVLTree {
    Integer value;
    AVLTree leftNode;
    AVLTree rightNode;

    public AVLTree(Integer value) {
        this.value = value;
    }

    public AVLTree(Integer value, AVLTree leftNode, AVLTree rightNode) {
        this.value = value;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    /**
     * Conducts a left rotation on the current node.
     *
     * @return the new "current" or "top" node after rotation.
     */
    public AVLTree leftRotate() {
        AVLTree newParent = this.rightNode;
        AVLTree newRightOfCurrent = newParent.leftNode;

        AVLTree newLeft = createNode(this.value, this.leftNode, newRightOfCurrent);
        newParent = createNode(newParent.value, newLeft, newParent.rightNode);
        return newParent;
    }

    /**
     * Conducts a right rotation on the current node.
     *
     * @return the new "current" or "top" node after rotation.
     */
    public AVLTree rightRotate() {
        AVLTree newParent = this.leftNode;
        AVLTree newLeftOfCurrent = newParent.rightNode;

        AVLTree newRight = createNode(this.value, newLeftOfCurrent, this.rightNode);
        newParent = createNode(newParent.value, newParent.leftNode, newRight);
        return newParent;
    }


    protected abstract AVLTree createNode(int value, AVLTree leftNode, AVLTree rightNode);


    /**
     * Inserts the element and returns a new instance of itself with the new node.
     * Must be an immutable implementation. That is, the state of the object cannot
     * be modified after it is created.
     */
    public abstract AVLTree insert(Integer value);


    /**
     * @return Balance factor for the current node.
     */
    public int getBalanceFactor() {
        int leftHeight = (leftNode==null) ? -1 : leftNode.getHeight();
        int rightHeight = (rightNode==null) ? -1 : rightNode.getHeight();
        return leftHeight - rightHeight;
    }

    /**
     * @return Height of current node.
     */
    public int getHeight() {
        int leftNodeHeight = (leftNode==null) ? 0 : 1 + leftNode.getHeight();
        int rightNodeHeight = (rightNode==null) ? 0 : 1 + rightNode.getHeight();
        return Math.max(leftNodeHeight, rightNodeHeight);
    }

    @Override
    public String toString() {
        return "{" +
                "value=" + value +
                ", leftNode=" + leftNode +
                ", rightNode=" + rightNode +
                '}';
    }

    /**
     * Graphically visualises the tree for human readability.
     * Feel free to edit this display methods.
     *
     * @return graph of the tree.
     */
    public String display() {
        return display(0);
    }

    /**
     * Graphically visualises the tree for human readability.
     * Feel free to edit this display methods
     *
     * @param tabs from the left side of the screen.
     * @return graph of the tree.
     */
    public String display(int tabs) {
        assert value != null;
        StringBuilder s = new StringBuilder(value.toString());
        s.append("\n").append("\t".repeat(tabs)).append("├─").append(leftNode==null ? "N/A" : leftNode.display(tabs + 1));
        s.append("\n").append("\t".repeat(tabs)).append("├─").append(rightNode==null ? "N/A" : rightNode.display(tabs + 1));
        return s.toString();
    }

    /**
     * List the elements of the tree from a pre-order traversal.
     */
    public List<Integer> preOrder() {
        return AVLTree.treeToListPreOrder(this);
    }

    private static List<Integer> treeToListPreOrder(AVLTree tree) {
        List<Integer> list = new LinkedList<>();
        if (tree.value != null) list.add(tree.value);
        if (tree.leftNode != null) list.addAll(treeToListPreOrder(tree.leftNode));
        if (tree.rightNode != null) list.addAll(treeToListPreOrder(tree.rightNode));
        return list;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
