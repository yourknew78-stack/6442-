public class SampleAVLTree extends AVLTree {

    public SampleAVLTree(int x) {
        super(x);
    }
    public SampleAVLTree(Integer value, AVLTree leftNode, AVLTree rightNode) {
        super(value, leftNode, rightNode);
    }

    @Override
    protected SampleAVLTree createNode(int value, AVLTree leftNode, AVLTree rightNode) {
        return new SampleAVLTree(value, leftNode, rightNode);
    }

    /**
     * Note that it may or may not be a correct implementation of an AVLTree,
     * and may or may not be inline with the specifications in the question/code.
     *
     * You may change the `insert` method of this class and/or add helper methods,
     * if it helps with your development, but it is optional and will not be marked.
     */
    @Override
    public SampleAVLTree insert(Integer value) {
        if (this.value == null) {
            this.value = value;
        } else {
            SampleAVLTree newNode = createNode(value, null, null);
            if (value < this.value) {
                if (this.leftNode == null) this.leftNode = newNode;
                else this.leftNode.insert(value);
            } else {
                if (this.rightNode == null) this.rightNode = newNode;
                else this.rightNode.insert(value);
            }
        }
        return this;
    }

}
