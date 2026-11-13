public class Penguin extends Animal {

    public Penguin(String id, String name, int age, int level) {
        super(id, name, age, level);
    }

    @Override
    public String speak() {
        return "... ... ARKKAKRKRKARKRKAKKK!!!";
    }

}
