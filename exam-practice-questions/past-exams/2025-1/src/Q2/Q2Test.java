import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;


public class Q2Test {
    @Test(timeout = 1000)
    public void insert_simple() {
        FamilyTree tree = new FamilyTree(new Person(5));

        String expected = "5\n";
        Assert.assertEquals(expected, tree.show());

        tree.insert(6);
        tree.insert(8);
        expected = """
                5
                  --6
                  ------8
                  """;
        Assert.assertEquals(expected, tree.show());
    }

    @Test(timeout = 1000)
    public void ordered_simple() {
        FamilyTree tree = new FamilyTree(new Person(5), new FamilyTree(new Person(9)), new FamilyTree(new Person(2), new FamilyTree(new Person(4)), new FamilyTree(new Person(1))));
        assertTrue(tree.isOrdered());

        tree = new FamilyTree(new Person(5), new FamilyTree(new Person(9)), new FamilyTree(new Person(10)));
        assertFalse(tree.isOrdered());
    }

    @Test(timeout = 1000)
    public void ordered_equal() {
        FamilyTree tree = new FamilyTree(new Person(5), new FamilyTree(new Person(9)), new FamilyTree(new Person(5)));
        assertTrue(tree.show(), tree.isOrdered());

        tree = new FamilyTree(new Person(5), new FamilyTree(new Person(5)), new FamilyTree(new Person(3)));
        assertFalse(tree.show(), tree.isOrdered());
    }

    @Test(timeout = 1000)
    public void find_simple() {
        FamilyTree tree = new FamilyTree(new Person(5), new FamilyTree(new Person(9)), new FamilyTree(new Person(2), new FamilyTree(new Person(4)), new FamilyTree(new Person(1))));
        Tree rTree = tree.find(2);

        String expected = """
                2
                  --4
                  --1
                """;
        assertEquals(expected, rTree.show());
    }

    @Test(timeout = 1000)
    public void label_simple() {
        FamilyTree tree = new FamilyTree(new Person(5));
        tree.getLevelAndLabel();
        assertEquals("5 [Kid]\n", tree.show());

        tree = new FamilyTree(new Person(5), tree, tree);
        tree.getLevelAndLabel();
        String expected = """
                5 [Parent]
                  --5 [Kid]
                  --5 [Kid]
                """;
        assertEquals(expected, tree.show());

        tree = new FamilyTree(new Person(5), tree, tree);
        tree.getLevelAndLabel();
        assertEquals("""
                5 [Grandparent]
                  --5 [Parent]
                  ------5 [Kid]
                  ------5 [Kid]
                  --5 [Parent]
                  ------5 [Kid]
                  ------5 [Kid]
                """, tree.show());
    }

}
