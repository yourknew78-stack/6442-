public class OpExp extends Exp {
    Exp left;
    OpType op;
    Exp right;

    public OpExp(Exp left, OpType op, Exp right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    enum OpType {
        ADD, SUBTRACT, MULTIPLY;
    }


    @Override
    public String show() {
        if (op == null) return left.show();
        return String.format("(%s %s %s)", left.show(), op, right.show());
    }

}
