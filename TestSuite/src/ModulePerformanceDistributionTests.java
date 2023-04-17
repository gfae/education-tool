import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.PSource;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ModulePerformanceDistributionTests {
    static ReadData data;

    static ModulePerformanceDistribution modulePerformanceDistributionPieChart;
    static ModulePerformanceDistribution modulePerformanceDistributionBarChart;

    static ModulePerformanceDistribution modulePerformanceDistributionHistogram;

    @BeforeAll
    public static void BeforeAll() {
        System.out.println("Setting up test class");
        File file = new File("../DataFileFor2022-23-problem statement.csv");
        data = new ReadData(file);
    }

    @BeforeEach
    public void beforeEach() {
        String[] module = new String[]{"CE101-4-FY"};
        modulePerformanceDistributionPieChart = new ModulePerformanceDistribution().createPieChart(module, data);
        modulePerformanceDistributionBarChart = new ModulePerformanceDistribution().createBarChart(module, data);
        modulePerformanceDistributionHistogram = new ModulePerformanceDistribution().createHistogram(module, data);
    }

    @AfterEach
    public void afterEach() {
        modulePerformanceDistributionPieChart = null;
        modulePerformanceDistributionBarChart = null;
        modulePerformanceDistributionHistogram = null;
    }

    @Test
    public void createPieDatasetTest(){
        int[] check;
        System.out.println("Testing createPieDataset method works");
        check = modulePerformanceDistributionPieChart.getTestIntList();
        assertEquals(check[0], 20);
        assertEquals(check[1], 25);
        assertEquals(check[2], 29);
        assertEquals(check[3], 13);
        assertEquals(check[4], 8);
    }

    @Test
    public void createPieChartTest(){
        System.out.println("Testing createPieChart method works");
        Component[] compList = modulePerformanceDistributionPieChart.getComponents();
        assertEquals(compList.length, 1);
        ChartPanel test = (ChartPanel) compList[0];
        assertNotNull(test);
    }

    @Test
    public void createBarDatasetTest(){
        Map<String, Integer> test = new HashMap<>();
        System.out.println("Testing createBarDataset method works");
        test = modulePerformanceDistributionBarChart.getTestMap();
        List<Integer> list = new ArrayList<Integer>(test.values());
        assertEquals(list.get(0), 20);
        assertEquals(list.get(1), 13);
        assertEquals(list.get(2), 25);
        assertEquals(list.get(3), 29);
        assertEquals(list.get(4), 8);
    }

    @Test
    public void createBarChartTest(){
        System.out.println("Testing createBarChart method works");
        Component[] compList = modulePerformanceDistributionBarChart.getComponents();
        assertEquals(compList.length, 1);
        ChartPanel test = (ChartPanel) compList[0];
        assertNotNull(test);

    }

    @Test
    public void createHistogramDatasetTest(){
        double[] checkList;
        int check;
        System.out.println("Testing createHistogramDataset method works");
        checkList = modulePerformanceDistributionHistogram.getTestDoubleList();
        check = modulePerformanceDistributionHistogram.getTestBins();
        assertEquals(checkList.length, 95);
        assertEquals(check, 9);
    }

    @Test
    public void createHistogram(){
        System.out.println("Testing createHistogram method works");
        Component[] compList = modulePerformanceDistributionHistogram.getComponents();
        assertEquals(compList.length, 1);
        ChartPanel test = (ChartPanel) compList[0];
        assertNotNull(test);
    }
}
