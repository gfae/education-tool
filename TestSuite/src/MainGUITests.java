import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainGUITests {

    static MainGUI gui;

    @BeforeAll
    public static void beforeAll() throws Exception {
        System.out.println("Setting up test class");
        gui = new MainGUI();
    }

    @Test
    public void correctTitle() {
        System.out.println("Testing title of MainGUI class");
        assertEquals("UOE Education Management Tool", gui.getTitle());
    }

    @Test
    public void testStatsPanels() {
        System.out.println("Testing all stats panels are added correctly");
        File file = new File("../DataFileFor2022-23-problem statement.csv");
        ReadData data = new ReadData(file);
        JPanel leftPanel = gui.leftPanel;
        Component[] components = leftPanel.getComponents();
        assertEquals(0, components.length); // No components added yet

        gui.addStatsPanels(data);
        components = leftPanel.getComponents();

        assertEquals(6, components.length); // 6 components added
        assertEquals("Module Difficulty", components[0].getName());
        assertEquals("Module Performance Distribution", components[1].getName());
        assertEquals("Categorised Student Performance", components[2].getName());
        assertEquals("Student Performance vs Average", components[3].getName());
        assertEquals("Students by Grade Range", components[4].getName());
        assertEquals("Course Comparison", components[5].getName());
    }
}