/**
 * Represents an empty tree, which acts as the terminal (or null) child in a tree structure.
 *
 * An empty tree does not represent a tree/node, but
 * rather a virtual element that signifies the absence of a node below a leaf.
 *
 * It is used as a placeholder for the left and right children of a leaf node,
 * maintaining consistent structure without using null values.
 */
class EmptyTree extends Tree {
    @Override
    public boolean isOrdered() {return true;}


    /**
     * Task C
     */
    @Override
    public int getLevelAndLabel() {return 0;}

    @Override
    public String show() {
        // TODO - Start your code

        return null;
        // TODO - End your code
    }

    @Override
    public String show(int depth) {
        // TODO - Start your code

        return null;
        // TODO - End your code
    }


    // Default methods for an EmptyTree.
    @Override
    public Tree find(int id) {return null;}
    @Override
    public void insert(int id) {}

}
