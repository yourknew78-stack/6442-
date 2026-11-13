public abstract class Animal {
    private final String id;
    private final String name;
    private final int age;
    private final int level;


    public Animal(String id, String name, int age, int level) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.level = level;
    }

    abstract String speak();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return this.getId() + ": " + this.getName() + " says " + this.speak() +
                " (age: " + this.age + ", level: " + this.level + ")";
    }

}

