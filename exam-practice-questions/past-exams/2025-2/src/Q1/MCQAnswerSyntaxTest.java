import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import java.util.stream.IntStream;


public class MCQAnswerSyntaxTest {

    private static final String FILE_PATH = "src/Q1/Q1Answers.txt";
    private static final Integer NO_QUESTIONS = 5;
    private static BufferedReader reader;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        // TODO - You may change the file path or the code to ensure
        //  the correct file is checked on the Lab Computer.
        reader = new BufferedReader(new FileReader(FILE_PATH));
    }

    @Test(timeout = 1000)
    public void testBasicSyntax() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            assertNotNull("Failed at line: " + line, parseAnswer(line));
        }
    }

    @Test(timeout = 1000)
    public void testNoRepeatInRange() throws IOException {
        Map<Integer, Set<Character>> answers = new HashMap<>();
        IntStream.rangeClosed(1, NO_QUESTIONS).boxed().forEach(i -> answers.put(i, new HashSet<>()));

        String line;
        while ((line = reader.readLine()) != null) {
            Answer answer = parseAnswer(line);
            assertNotNull("Failed at line: " + line, parseAnswer(line));
            assertTrue("MCQ index out of range: " + line, (answer.mcq >= 1) && (answer.mcq <= NO_QUESTIONS));
            assertFalse("Repeated MCQ (#" + answer.mcq + ") " + line, answers.containsKey(answer.mcq) && !answers.get(answer.mcq).isEmpty());
            answers.put(answer.mcq, answer.answers);
        }
        System.out.println("Your Answers:\n"+answers);
        System.out.println("[] Means no attempt");
    }

    @Test(timeout = 1000)
    public void testAnswersOrdered() throws IOException {
        String line;
        int mcqIndex = 1;
        while ((line = reader.readLine()) != null) {
            Answer answer = parseAnswer(line);
            assertNotNull("Failed at line: " + line, answer);
            assert mcqIndex==answer.mcq;
            mcqIndex++;
        }
    }


    public static final Pattern r = Pattern.compile("^(\\d+)\\.\\s*((?:[A-M]\\s*(?:,\\s*[A-M])*)?)$");

    public static Answer parseAnswer(String line) {
        if (line == null) return null;
        line = line.toUpperCase();
        Matcher m = r.matcher(line.trim());
        if (!m.matches()) return null;

        int mcq = Integer.parseInt(m.group(1));
        Set<Character> answers = new HashSet<>();
        String answerPart = m.group(2);
        if (!answerPart.isEmpty()) {
            for (String ans : answerPart.split(",")) {
                ans = ans.trim();
                if (ans.length() != 1) return null;
                answers.add(ans.charAt(0));
            }
        }
        return new Answer(mcq, answers);
    }

    static class Answer {
        int mcq;
        Set<Character> answers;

        public Answer(int mcq, Set<Character> answers) {
            this.mcq = mcq;
            this.answers = answers;
        }
    }

}
