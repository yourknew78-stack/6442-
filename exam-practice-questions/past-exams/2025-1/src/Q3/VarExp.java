public class VarExp extends Exp {
    String varName;

    public VarExp(String varName) {
        this.varName = varName;
    }

    @Override
    public String show() {
        return varName;
    }
}
