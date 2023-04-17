import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentPerformanceTests {

    private static ReadData data;
    private Map<String, String[]> results;
    private StudentPerformance studentPerformance;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Setting up test class");
        File file = new File("../DataFileFor2022-23-problem statement.csv");
        data = new ReadData(file);
    }

    @BeforeEach
    public void beforeEach() {
        studentPerformance = new StudentPerformance();
        results = data.getResultsMap();
    }

    @AfterEach
    public void afterEach() {
        studentPerformance = null;
        results = null;
    }

    @Test
    public void testGetGrades() {
        System.out.println("Testing the student grades map is being generated correctly");
        String[] selectedModule = new String[]{"CE101-4-FY"};

        ArrayList<Student> grades = studentPerformance.getGrades(selectedModule, results);

        assertEquals(grades.get(0).getStudentID(), "2500157");
        assertEquals(grades.get(94).getStudentID(), "2500084");
    }

    @Test
    public void testTopPerformers() {
        System.out.println("Testing the top 3 performers are correctly identified");
        String[] selectedModule = new String[]{"CE161-4-AU"};

        ArrayList<Student> grades = studentPerformance.getGrades(selectedModule, results);
        String[][] topPerformers = studentPerformance.topPerformers(grades);

        // 1st
        assertEquals(topPerformers[0][0], "2500213");
        assertEquals(topPerformers[0][1], "94");
        // 2nd
        assertEquals(topPerformers[1][0], "2500159");
        assertEquals(topPerformers[1][1], "91");
        // 3rd
        assertEquals(topPerformers[2][0], "2500205");
        assertEquals(topPerformers[2][1], "90");
    }

    @Test
    public void testBottomPerformers() {
        System.out.println("Testing the bottom 3 performers are correctly identified");
        String[] selectedModule = new String[]{"CE141-4-FY"};

        ArrayList<Student> grades = studentPerformance.getGrades(selectedModule, results);
        String[][] bottomPerformers = studentPerformance.bottomPerformers(grades);

        // 1st
        assertEquals(bottomPerformers[2][0], "2500006");
        assertEquals(bottomPerformers[2][1], "6");
        // 2nd
        assertEquals(bottomPerformers[1][0], "2500118");
        assertEquals(bottomPerformers[1][1], "10");
        // 3rd
        assertEquals(bottomPerformers[0][0], "2500102");
        assertEquals(bottomPerformers[0][1], "12");
    }

    @Test
    public void testAveragePerformers() {
        System.out.println("Testing the 3 average performers are correctly identified");
        String[] selectedModule = new String[]{"CE101-4-SP"};

        ArrayList<Student> grades = studentPerformance.getGrades(selectedModule, results);
        String[][] averagePerformers = studentPerformance.averagePerformers(grades);
        // Average 572/10 = 57.2
        // 1st
        assertEquals(averagePerformers[0][0], "2500023");
        assertEquals(averagePerformers[0][1], "57");
        // 2nd
        assertEquals(averagePerformers[1][0], "2500085");
        assertEquals(averagePerformers[1][1], "54");
        // 3rd
        assertEquals(averagePerformers[2][0], "2500050");
        assertEquals(averagePerformers[2][1], "53");
    }



}
