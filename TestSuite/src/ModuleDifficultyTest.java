import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.PieDataset;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModuleDifficultyTest {

    static ReadData data;

    public ModuleDifficulty moduleDifficulty;

    @BeforeAll
    public static void BeforeAll(){
        System.out.println("Setting up test class");
        File file = new File("../DataFileFor2022-23-problem statement.csv");
        data = new ReadData(file);
    }

    @BeforeEach
    public void BeforeEach(){
        moduleDifficulty = new ModuleDifficulty();
    }

    @AfterEach
    public void AfterEach(){
        moduleDifficulty = null;
    }

    @Test
    public void createTableTest(){
        JPanel table = moduleDifficulty.createTable(new String[]{"CE101-4-FY"}, data);
        System.out.println("Testing the module difficulty table is being generated correctly");
        Component[] components = table.getComponents();
        JPanel panel = (JPanel) components[0];
        Component[] components1 = panel.getComponents();
        JPanel panel1 = (JPanel) components1[0];
        Component[] components2 = panel1.getComponents();
        JLabel aboveGrade = (JLabel) components2[2];
        JLabel belowGrade = (JLabel) components2[6];
        JLabel averageGrade = (JLabel) components2[4];
        JLabel difficulty = (JLabel) components2[7];

        assertEquals(aboveGrade.getText(), "43%");
        assertEquals(belowGrade.getText(), "28%");
        assertEquals(averageGrade.getText(), "28%");
        assertEquals(difficulty.getText(), "Module difficulty: Easy");
    }

    @Test
    public void createPieChartTest() {
        System.out.println("Testing the pie chart is being generated correctly");
        JPanel pieChart = moduleDifficulty.createPieChart(new String[]{"CE101-4-FY"}, data);
        Component[] components = pieChart.getComponents();
        ChartPanel chartPanel = (ChartPanel) components[0];

        JFreeChart chart = chartPanel.getChart();
        assertEquals(chart.getTitle().getText(), "CE101-4-FY Difficulty Breakdown");

        PiePlot plot = (PiePlot) chart.getPlot();
        PieDataset dataset = plot.getDataset();
        assertEquals(dataset.getValue(0), 91.0);
        assertEquals(dataset.getValue(1), 8.0);
    }


}