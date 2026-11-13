public class Dog extends Animal {

    public Dog(String id, String name, int age, int level) {
        super(id, name, age, level);
    }

    @Override
    public String speak() {
        return "Woof";
    }

}

