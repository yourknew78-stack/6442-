public class IntExp extends Exp {
    Integer value;

    public IntExp(Integer value) {
        this.value = value;
    }

    @Override
    public String show() {
        return Integer.toString(this.value);
    }
}
