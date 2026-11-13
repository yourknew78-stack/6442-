public class Person extends Node {
    public Person(int id) {
        super(id);
    }

    @Override
    public String toString() {
        return (this.getLabel()==null) ? String.valueOf(this.getId()) : String.format("%d [%s]", this.getId(), this.getLabel());
    }

}
