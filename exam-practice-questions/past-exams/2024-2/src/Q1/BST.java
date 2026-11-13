import java.util.List;
import java.util.Stack;

class BST<T extends Comparable<T>> implements IterableCollection<T> {
    T value;
    BST<T> left = null;
    BST<T> right = null;

    public BST(T value) {
        this.value = value;
    }

    public BST() {
        this.value = null;
    }

    public BST<T> insert(T value) {
        if (value.equals(this.value)) return null;
        if (this.value==null) {
            this.value = value;
            return this;
        }
        if (value.compareTo(this.value) < 0) {
            if (this.left == null) return this.left = new BST<>(value);
            else return this.left.insert(value);
        } else {
            if (this.right == null) return this.right = new BST<>(value);
            else return this.right.insert(value);
        }
    }

    public void insertAll(List<T> values) {
        for (var v : values) {
            this.insert(v);
        }
    }

    @Override
    public Iterator<T> createIterator() {
        return new BSTIterator<>(this);
    }

    public void display(int level) {
        String prefix = (level<=1) ? "" : " ".repeat(4*(level-1));
        prefix += (level==0) ? "" : "|" + "-".repeat(4-1);
        System.out.println(prefix + this.value);
        if (this.left != null) this.left.display(level+1);
        if (this.right != null) this.right.display(level+1);
    }


    /**
     * Concrete Iterator for the BST
     */
    public static class BSTIterator<T extends Comparable<T>> implements Iterator<T> {
        /*
         * Recall that a stack is a simple data structure that follows the
         * Last In, First Out (LIFO) principle. The following basic operations
         * are useful in this task:
         * -  push: add an element to the top of the stack.
         * -  pop: remove the element from the top of the stack.
         * -  isEmpty: check whether the stack is empty.
         *
         * Example: After pushing two elements.
         *    stack.push(1);
         *    stack.push(2);
         *
         * You will get `2` when you call `stack.pop();`, then `1` when you pop again.
         */
        private final Stack<BST<T>> stack = new Stack<>();

        /**
         * Constructs a BSTIterator for the given tree.
         *
         * - `Initialization`: Push the root node onto the stack if it has a non-null value.
         *
         * @param root the root of the BST to iterate over
         */
        public BSTIterator(BST<T> root) {
            // TODO - Start your code here



            // TODO - End your code here
        }

        /**
         * @return true if there are more elements to iterate over, false otherwise.
         */
        @Override
        public boolean hasNext() {
            // TODO - Start your code here


            return false; // Feel free to remove this line
            // TODO - End your code here
        }

        /**
         * Continue with pre-order traversal and return the next element.
         * <p>
         * Whenever the method is triggered,
         * -  Pop the top node from the stack.
         * -  Push the node's children (if exists) in the correct order.
         * and return the value of the top node.
         * <p>
         * Pushing the children in the correct order ensures that when you pop
         * the next node from the stack, you always get the left child first,
         * maintaining the correct pre-order sequence.
         *
         * @return the next element in the pre-order traversal.
         */
        @Override
        public T next() {
            // TODO - Start your code here


            return null; // Feel free to remove this line
            // TODO - End your code here
        }

        public int size() {
            return this.stack.size();
        }
    }

}
