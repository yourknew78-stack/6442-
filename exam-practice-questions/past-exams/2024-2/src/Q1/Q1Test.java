import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class Q1Test {
    Class<?> targetClass = BST.class;

    public BST<Integer> createTree() {
        if (targetClass.equals(BST.class)) return new BST<>();
        return null;
    }

    public Iterator<Integer> createIterator(BST<Integer> tree) {
        if (targetClass.equals(BST.class)) return new BST.BSTIterator<>(tree);
        return null;
    }

    /**
     * Task (A)
     */
    @Test(timeout = 1000)
    public void test_iterator_values() {
        BST<Integer> tree = createTree();
        tree.insertAll(List.of(5, 7, 3, 8, 6, 2, 4));

        int[] values = new int[]{5, 3, 2, 4, 7, 6, 8};
        Iterator<Integer> iterator = createIterator(tree);
        int i=0;
        while (iterator.hasNext()) {
            int nextValue = iterator.next();
            assertEquals(nextValue, values[i]);
            i++;
        }
        assertEquals(i, 7); // There should be exactly 7 next() calls.
    }

    /*
     * 5
     * |---3
     *     |---2
     *     |---4
     * |---7
     *     |---6
     *     |---8
     */
    @Test(timeout = 1000)
    public void test_iterator_iter_size() {
        BST<Integer> tree = createTree();
        tree.insertAll(List.of(5, 7, 3, 8, 6, 2, 4));

        int[] sizes = new int[]{1, 2, 3, 2, 1, 2, 1};
        Iterator<Integer> iterator = createIterator(tree);
        int i=0;
        while (iterator.hasNext()) {
            assertEquals(iterator.size(), sizes[i]);
            i++; iterator.next();
        }
        assertEquals(i, 7); // There should be exactly 7 next() calls.
    }

}
