import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;


public class Q3Test {

    @Test(timeout = 1000)
    public void Q3_1_personal_info() {
        assertEquals(8, Q3Answers.UID.strip().length());
        assertNotEquals("<Your Name>", Q3Answers.NAME.strip());
    }

    static Map<String, Integer> wordLimits = Map.of(
            "CODE_SMELL_NAME", 4,
            "SMELL_LOCATION", 20,
            "SMELL_IMPACT", 100,
            "DESIGN_PATTERN", 4,
            "REFACTORING_PLAN", 100
    );

    @Test(timeout = 1000)
    public void Q3_2_word_count() throws NoSuchFieldException, IllegalAccessException {
        for (String fieldName : wordLimits.keySet()) {
            Field field = Q3Answers.class.getDeclaredField(fieldName);
            String value = (String) field.get(null);
            int wordCount = value.isEmpty() ? 0 : value.split(" ").length;
            int wordLimit = wordLimits.get(fieldName);
            assertTrue(String.format("Wordcount (%d) exceeded the word limit (%d)", wordCount, wordLimit), wordCount <= wordLimit);
        }
    }


}




