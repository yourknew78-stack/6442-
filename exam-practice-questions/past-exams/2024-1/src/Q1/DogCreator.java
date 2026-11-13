import java.util.List;
import java.util.Random;


public class DogCreator extends AnimalCreator {
    public static final List<String> NAMES = List.of("Rex", "Buddy", "Max", "Rocky");
    private int nextNameIndex = 0;

    /**
     *  See README.
     *  Hint: you may use `nextNameIndex`
     */
    public String getNextName() {
        // TODO - Your code starts here


        return null;  // feel free to change this line
        // TODO - Your code ends here
    }

    public String getRandomName() {
        return NAMES.get(new Random().nextInt(NAMES.size()));
    }

    public DogCreator(Integer age, Integer level) {
        // TODO - Your code starts here
        super(null, null);  // feel free to change this line


        // TODO - Your code ends here
    }

    @Override
    public Animal createAnimal(String id, String name) {
        // TODO - Your Code Starts Here


        return null;  // feel free to change this line
        // TODO - Your Code Ends Here
    }

}

