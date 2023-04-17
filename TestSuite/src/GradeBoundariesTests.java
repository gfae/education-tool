import org.junit.jupiter.api.*;

import javax.swing.*;

import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GradeBoundariesTests {

    static ReadData data;
    static GradeBoundaries gradeBoundaries;

    @BeforeAll
    public static void beforeAll() throws Exception {
        System.out.println("Setting up test class");
        File file = new File("../DataFileFor2022-23-problem statement.csv");
        data = new ReadData(file);
    }

    @BeforeEach
    public void beforeEach() {
        gradeBoundaries = new GradeBoundaries();
    }

    @AfterEach
    public void afterEach() {
        gradeBoundaries = null;
    }

    @Test
    public void createTableTest() {
        System.out.println("Testing createTable method works");
        String[] courses = {"COMPUTER SCIENCE", "DATA SCI & ANALYTICS"};
        int lowerBound = 50;
        int upperBound = 100;

        GradeBoundaries table = gradeBoundaries.createTable(courses, lowerBound, upperBound, data);

        assertNotNull(table);
    }

    @Test
    public void formatLabelTest() {
        System.out.println("Testing formatLabel method works");
        JLabel label = new JLabel();
        int width = 50;

        gradeBoundaries.formatLabel(label, width);

        Font expectedFont = new Font("Arial", Font.PLAIN, 12);
        assertTrue(label.isOpaque());
        assertEquals(Color.WHITE, label.getBackground());
        assertEquals(new Dimension(width, 20), label.getPreferredSize());
        assertNotNull(label.getBorder());
        assertEquals(expectedFont, label.getFont());
    }

    @Test
    public void getDataMapTest() {
        System.out.println("Testing getDataMap method works");
        String[] courses = {"COMPUTER SCIENCE", "DATA SCI & ANALYTICS"};
        int lowerBound = 50;
        int upperBound = 100;

        GradeBoundaries table = gradeBoundaries.createTable(courses, lowerBound, upperBound, data);
        table.getDataMap();

        assertNotNull(table.keys);
        assertNotNull(table.results);
    }
}