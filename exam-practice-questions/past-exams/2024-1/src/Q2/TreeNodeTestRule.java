import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


/**
 *  For testing only. Please ignore this class.
 *
 *  This class is not necessary for the question, and
 *  it is kept only to ensure auto-marker can be run.
 */

public class TreeNodeTestRule implements TestRule {
    private Class<? extends AVLTree> implementationClass;

    public TreeNodeTestRule(Class<? extends AVLTree> implementationClass) {
        this.implementationClass = implementationClass;
    }

    public AVLTree createTreeNode(int value) {
         try {
             return implementationClass.getDeclaredConstructor(int.class).newInstance(value);
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
            }
        };
    }
}
