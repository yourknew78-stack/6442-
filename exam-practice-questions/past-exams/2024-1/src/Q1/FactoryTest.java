import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

public class FactoryTest {

    /**
     *  Task (A)
     */
    @Test(timeout = 1000)
    public void withDogCreator_whenGetNextName_thenReturnsName() {
        DogCreator dogCreator = new DogCreator(5, 10);
        assertEquals("Rex", dogCreator.getNextName());
    }

    @Test(timeout = 1000)
    public void withDogCreator_whenGetNextName_thenReturnsName_multiple() {
        DogCreator dogCreator = new DogCreator(5, 10);

        assertEquals("Rex", dogCreator.getNextName());
        assertEquals("Buddy", dogCreator.getNextName());
        assertEquals("Max", dogCreator.getNextName());
        assertEquals("Rocky", dogCreator.getNextName());
    }


    /**
     *  Task (B)
     */
    @Test(timeout = 1000)
    public void withDogCreator_whenCreateAnimal_thenDogHasFieldsSetCorrectly() {
        DogCreator dogCreator = new DogCreator(5, 10);

        String id = UUID.randomUUID().toString();
        String name = "TestDog";
        Animal dog = dogCreator.createAnimal(id, name);

        assertEquals(id, dog.getId());
        assertEquals(name, dog.getName());
        assertEquals(5, dog.getAge());
        assertEquals(10, dog.getLevel());
    }

    @Test(timeout = 1000)
    public void withDogCreator_whenCreateAnimalWithNullAge_thenDogHasFieldsSetCorrectly() {
        DogCreator dogCreator = new DogCreator(null, 10);
        Animal dog = dogCreator.createAnimal("id", "dog");

        assertEquals("dog", dog.getName());
        assertEquals("id", dog.getId());
        assertEquals(10, dog.getLevel());
        assertEquals(0, dog.getAge());  // Hint: use default in AnimalCreator
    }


    /**
     *  Task (C)
     */
    @Test(timeout = 1000)
    public void withDogCreator_whenCreateBatch_ThenDogsSetUpCorrectly() {
        DogCreator dogCreator = new DogCreator(3, 8);
        int numberToCreate = 3;

        List<Animal> dogs = dogCreator.createBatch(numberToCreate);
        assertEquals(numberToCreate, dogs.size());

        for (int i=0; i<numberToCreate; i++) {
            Dog dog = (Dog) dogs.get(i);
            assertEquals(dog.getAge(), 3);
            assertEquals(dog.getLevel(), 8);
        }
    }

    static String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";

    @Test(timeout = 1000)
    public void withDogCreator_whenCreateAnimalTemplate_ThenDogSetUpCorrectly() {
        DogCreator dogCreator = new DogCreator(2, 7);

        Animal dog = dogCreator.createAnimalTemplate();
        assertEquals(dog.getAge(), 2);
        assertEquals(dog.getLevel(), 7);
        assertTrue(dog.getId().matches(UUID_REGEX));
    }
}
