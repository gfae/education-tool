import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CourseComparisonTests {

    static ReadData data;

    static CourseComparison courseComparison;

    @BeforeAll
    public static void BeforeAll() {
        System.out.println("Setting up test class");
        File file = new File("../DataFileFor2022-23-problem statement.csv");
        data = new ReadData(file);
    }

    @BeforeEach
    public void beforeEach() {
        courseComparison = new CourseComparison().createScatterPlot("CE101-4-SP", data); }

    @AfterEach
    public void afterEach() {
        courseComparison = null;
    }

    @Test
    public void createDatabaseTest(){
        System.out.println("Testing createDatabase method works");
        XYSeriesCollection testSeries = courseComparison.getXySeriesCollection();
        List seriesList = testSeries.getSeries();
        assertEquals(seriesList.size(), 2);
        XYSeries firstSeries = (XYSeries) seriesList.get(0);
        assertEquals(firstSeries.getKey(), "COMPUTER SCIENCE");
        XYSeries secondSeries = (XYSeries) seriesList.get(1);
        assertEquals(secondSeries.getKey(), "COMPUTING");
    }

    @Test
    public void createScatterPlotTest(){
        System.out.println("Testing createScatterPlot method works");
        Component[] compList = courseComparison.getComponents();
        assertEquals(compList.length, 1);
        ChartPanel test = (ChartPanel) compList[0];
        assertNotNull(test);
    }



}
