import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PerformanceVsAverageTests {
    static PerformanceVsAverage performanceVsAverage;
    static ReadData data;


    @BeforeAll
    public static void beforeAll() throws Exception {
        System.out.println("Creating the test class");
        File file = new File("../DataFileFor2022-23-problem statement.csv");
        data = new ReadData(file);
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("Creating the test object");
        performanceVsAverage = new PerformanceVsAverage();
    }

    @AfterEach
    public void afterEach() {
        System.out.println("Resetting the test object");
        performanceVsAverage = null;
    }

    @Test
    public void createHistogramTest() {
        System.out.println("Testing the createHistogram method");
        assertNotNull(performanceVsAverage.createHistogram("CE101-4-FY", "2500085", data));
    }

    @Test
    public void createScatterPlotTest() {
        System.out.println("Testing the createScatterPlot method");
        assertNotNull(performanceVsAverage.createScatterPlot("CE101-4-FY", "2500108", data));
    }
}
