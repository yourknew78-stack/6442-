/**
 * A concrete implementation of tree.
 *
 * Ordered in a right-to-left manner based on the id of nodes.
 * During insertion, a new node with a greater ID should go to the left sub-tree,
 * whereas nodes with a smaller or equal ID goes to the right.
 */
public class FamilyTree extends Tree {

    /**
     * Task A
     */
    public FamilyTree(Person value) {
        // TODO - Start your code

        // TODO - End your code
    }

    public FamilyTree(Person value, Tree left, Tree right) {
        // TODO - Start your code


        // TODO - End your code

    }


    /**
     * Task B
     */
    public Tree find(int id) {
        // TODO - Start your code


        return null;
        // TODO - End your code
    }

    public boolean isOrdered() {
        // TODO - Start your code


        return false;
        // TODO - End your code
    }

    public void insert(int newId) {
        // TODO - Start your code


        // TODO - End your code
    }


    /**
     * Label the nodes in the tree by its family role.
     *
     * Family role for a node is assigned relative to its youngest descendant.
     *
     * The labeling follows a bottom-up approach where:
     * - Leaf nodes (deepest non-empty nodes) are at level 1 and are labelled as "Kid".
     * - Their direct parents are at level 2 and labelled as "Parent".
     * - The next level up is labelled "Grandparent" (level 3).
     * - Subsequent levels continue with "Great-Grandparent" (level 4), "Great-Great-Grandparent" (level 5), and so on.
     *
     * @return the level of the current node.
     */
    public int getLevelAndLabel() {
        // TODO - Start your code

        return -1;
        // TODO - End your code
    }


    /**
     * Task C
     */
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

}
