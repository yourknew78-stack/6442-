public abstract class Exp {

    public abstract String show();

    @Override
    public String toString() {
        return this.show();
    }

}
