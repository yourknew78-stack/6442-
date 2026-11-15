import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * A parameterized test class for AVLTree implementations.
 * <p>
 * Verifies the correctness of the AVLTree based on the pre-order traversal
 * results of the tree.
 * <p>
 * Parameterized to run multiple tests with different trees and expected outcomes,
 * as defined in the {@link #data()} method.
 */
@RunWith(Parameterized.class)
public class AVLTreeTest {
    private final int treeIdentifier;
    private final List<Integer> expectedPreorder;
    private static final Class<? extends AVLTree> class2Test = SampleAVLTree.class;

    public AVLTreeTest(int treeIdentifier, List<Integer> expectedPreorder) {
        this.treeIdentifier = treeIdentifier;
        this.expectedPreorder = expectedPreorder;
    }


    @Test(timeout = 1000)
    public void testTreePreorder() {
        // Do not amend this method.
        // (A reminder only - many other things shouldn't be amended too)
        AVLTree tree = createTree(treeIdentifier);
        assertEquals(expectedPreorder, tree.preOrder());
    }

    /**
     * Parameters for the AVLTreeTest.
     * <p>
     * Each parameter array includes:
     *      1. An integer tree identifier/index, used to identify the tree used in the tests.
     *      2. A list of integers representing the expected pre-order traversal results of the tree.
     * <p>
     * Example:
     *      Suppose your first test case is for an AVL tree
     *      with the value 5 at the root node and the value 6 inserted to it.
     *
     *      Then you should include this parameter array in data(): { 0, {5, 6} },
     *      and the corresponding tree,
     *              5
     *               \
     *                6
     *      should be returned when createTree(0) is called.
     *
     * @return A collection of object arrays, where each array contains a parameter array.
     */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        // TODO - Start your code here


        return null;  // Feel free to change this line of code.
        // TODO - End your code here
    }


    /**
     * Creates an AVL tree based on the specified tree identifier.
     * <p>
     * Facilitates the construction of (concrete) AVL trees to be used in tests.
     * Return a tree corresponding to the tree identifier and expected values defined in data().
     * <p>
     * Note:
     *     -  You may create a concrete AVLTree with a single node using `createTreeNode()`.
     *     -  Currently, using createTreeNode creates a SampleAVLTree,
     *        but the actual AVLTree class to be used will change when assessment is done.
     *        You must not call `SampleAVLTree` in your tests directly.
     *     -  You do not need to create test cases for an empty Tree.
     *
     * @param treeIdentifier The identifier representing which tree configuration to create.
     * @return The constructed AVLTree.
     */
    private AVLTree createTree(int treeIdentifier) {
        // TODO - Start your code here


        return createTreeNode(2100);  // Feel free to change or remove this line of code.
        // TODO - End your code here
    }



    // ---------------------------------------------------------------------------------
    // Note - Helpers for marking. Do not amend them, and you may safely ignore them.
    @ClassRule
    public static TreeNodeTestRule treeNodeRule = new TreeNodeTestRule(class2Test);
    private AVLTree createTreeNode(Integer value) {
        return treeNodeRule.createTreeNode(value);
    }

    // ---------------------------------------------------------------------------------
}
