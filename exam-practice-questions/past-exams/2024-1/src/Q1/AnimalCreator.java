import java.util.*;

public abstract class AnimalCreator {
    int ageConfig = 0;  // default `ageConfig` and `levelConfig`
    int levelConfig = 0;

    public AnimalCreator(Integer age, Integer level) {
        // TODO - Your code starts here


        // TODO - Your code ends here
    }

    public abstract Animal createAnimal(String id, String name);

    public abstract String getNextName();

    /**
     * A template method used to create the ID, Name of an animal,
     * then returns the Animal created using these information.
     * <p>
     * 1) Create the ID using the string representation of `randomUUID()` from `UUID`.
     *    (Reference: https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html#randomUUID--)
     * 2) Get the next name for the animal.
     *    (Use the appropriate helper method which should be implemented in the subclasses)
     * 3) Create the animal, passing the ID and Name
     *    (Hint: `createAnimal()` should be implemented in the subclasses)
     */
    public Animal createAnimalTemplate() {
        // TODO - Your code starts here


        return null; // feel free to change this line
        // TODO - Your code ends here
    }

    /**
     * Return a list of `number` animals.
     * @param number: number of animals to be created.
     */
    public List<Animal> createBatch(int number) {
        // TODO - Your code starts here


        return null; // feel free to change this line
        // TODO - Your code ends here
    }

}
