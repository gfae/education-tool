import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModulePerformanceDistribution extends JPanel {

    String[] modules;
    Map<String, String[]> results;
    String[] keys;
    ReadData data;

    private int[] testIntList;

    private Map<String, Integer> testMap;

    private double[] testDoubleList;

    private int testBins;

    private ModulePerformanceDistribution(String[] module, ReadData data) {
        this.modules = module;
        this.data = data;
    }

    public ModulePerformanceDistribution() {
    }

    /**
     * Method to create a histogram for the module performance distribution
     * @param module the module to base the data on
     * @param data the ReadData class accessor
     * @return histogram
     * */
    public ModulePerformanceDistribution createHistogram(String[] module, ReadData data) {
        ModulePerformanceDistribution histogram = new ModulePerformanceDistribution(module, data);
        histogram.getDataMap();

        JFreeChart chart = ChartFactory.createHistogram(
                "",
                "Grades",
                "Frequency",
                histogram.createHistogramDataset(module),
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        setChartTitle(module, chart);

        NumberAxis xAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        xAxis.setRange(0, 100); // set the range for x axis
        xAxis.setTickUnit(new NumberTickUnit(10));


        histogram.createPanel(chart);
        return histogram;
    }

    /**
     * Method to create a pie chart for the module performance distribution
     * @param module the module to base the data on
     * @param data the ReadData class accessor
     * @return pieChart
     * */
    public ModulePerformanceDistribution createPieChart(String[] module, ReadData data) {
        ModulePerformanceDistribution pieChart = new ModulePerformanceDistribution(module, data);
        pieChart.getDataMap();


        JFreeChart chart = ChartFactory.createPieChart("",
                pieChart.createPieDataset(module), true, true, false);
        setChartTitle(module, chart);

        //set chart background color
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(231, 231, 231));
        // add percentage to pie chart
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0}: {2}", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(labelGenerator);
        pieChart.createPanel(chart);
        return pieChart;
    }

    /**
     * Method to create the dataset for the pie chart
     * @param modules the modules the dataset is based on
     * @return dataset PieDataset object
     * */
    private PieDataset<String> createPieDataset(String[] modules) {

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        int[] count = new int[5];

        for (String m : modules) {
            String[] grades = results.get(m);
            for (String grade : grades) {
                try {
                    int check = Integer.parseInt(grade);
                    if (check >= 70) {
                        count[0]++;
                    } else if (check >= 60) {
                        count[1]++;
                    } else if (check >= 50) {
                        count[2]++;
                    } else if (check >= 40) {
                        count[3]++;
                    } else {
                        count[4]++;
                    }
                    testIntList = count;
                } catch (NumberFormatException e) {
                    // Do nothing
                }
            }
        }
        if(count[0] != 0){
            dataset.setValue("First", count[0]);
        }
        if(count[1] != 0){
            dataset.setValue("Upper Second", count[1]);
        }
        if(count[2] != 0) {
            dataset.setValue("Lower Second", count[2]);
        }
        if(count[3] != 0){
            dataset.setValue("Third", count[3]);
        }
        if(count[4] != 0){
            dataset.setValue("Fail", count[4]);
        }
        return dataset;

    }

    /**
     * Method to create a bar chart for the module performance distribution
     * @param module the module to base the bar chart on
     * @return ModulePerformanceDistribution object (barChart)
     * */
    public ModulePerformanceDistribution createBarChart(String[] module, ReadData data) {
        ModulePerformanceDistribution barChart = new ModulePerformanceDistribution(module, data);
        barChart.getDataMap();

        JFreeChart chart = ChartFactory.createBarChart(
                "",
                "Grades",
                "Frequency",
                barChart.createBarDataset(),
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        setChartTitle(module, chart);

        barChart.configureBarChart(chart);
        barChart.createPanel(chart);
        return barChart;
    }

    /**
     * Method to create the dataset for the bar chart
     * @return dataset
     * */
    private DefaultCategoryDataset createBarDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> awardMap = new HashMap<>();
        awardMap.put("Fail", 0);
        awardMap.put("3rd", 0);
        awardMap.put("2:2", 0);
        awardMap.put("2:1", 0);
        awardMap.put("1st", 0);
        for (String m : modules) {
            String[] grades = results.get(m);
            for (String grade : grades) {
                try{
                    double check = Double.parseDouble(grade);
                    if (check >= 70){
                        awardMap.put("1st", awardMap.getOrDefault("1st", 0) + 1);
                    } else if (check >= 60) {
                        awardMap.put("2:1", awardMap.getOrDefault("2:1", 0) + 1);
                    } else if (check >= 50) {
                        awardMap.put("2:2", awardMap.getOrDefault("2:2", 0) + 1);
                    } else if (check >= 40) {
                        awardMap.put("3rd", awardMap.getOrDefault("3rd", 0) + 1);
                    } else {
                        awardMap.put("Fail", awardMap.getOrDefault("Fail", 0) + 1);
                    }
                }
                catch(Exception e){
                    // Do nothing
                }
            }
        }
        testMap = awardMap;
        dataset.addValue(awardMap.get("Fail"), "Fail", "Fail");
        dataset.addValue(awardMap.get("3rd"), "Third", "3rd");
        dataset.addValue(awardMap.get("2:2"), "Lower Second", "2:2");
        dataset.addValue(awardMap.get("2:1"), "Upper Second", "2:1");
        dataset.addValue(awardMap.get("1st"), "First", "1st");

        return dataset;
    }

    /**
     * Method to configure the bar chart
     * @param chart JFreeChart to be configured
     * */
    private void configureBarChart(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(-3);
        // Set tick unit for y axis
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.setBackgroundPaint(Color.WHITE);
    }

    /**
     * Retrieves string grades from the lists of strings in the hash map converts to doubles and adds to new
     * double list, and calculating number of bins for use in the dataset in order to create the histogram.
     * @param modules - array of modules
     * @return - dataset for histogram
     */
    private HistogramDataset createHistogramDataset (String [] modules)
    {
        ArrayList<Double> values = new ArrayList<>();
        for (String m : modules) {
            String[] grades = results.get(m);
            for (String grade : grades) {
                try{
                    double check = Double.parseDouble(grade);
                    values.add(check);
                }
                catch(Exception e){
                    // Do nothing
                }
            }
        }
        double[] value = new double[values.size()];
        double min = values.get(0);
        double max = values.get(0);
        for(int i = 0; i < values.size(); i++){
            value[i] = values.get(i);
            min = Math.min(min, value[i]);
            max = Math.max(max, value[i]);
        }
        testDoubleList = value;
        int bins = (int) Math.ceil((max - min)/10);
        testBins = bins;
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", value, bins);
        return dataset;
    }

    /**
     * Method to set the title of the chart
     * @param module list of modules to add to chart title
     * @param chart JFreeChart object to set the title for
     * */
    private void setChartTitle(String[] module, JFreeChart chart) {
        if (module.length == 1) {
            chart.setTitle(module[0] + " Performance Distribution");
        } else if (module.length == 14){
            chart.setTitle("Overall Performance Distribution");
        } else {
            chart.setTitle("Module Performance Distribution");
            StringBuilder subTitle = new StringBuilder("for ");
            for (int i = 0; i < module.length; i++) {
                if (i == module.length - 1) {
                    subTitle.append(module[i]);
                } else {
                    subTitle.append(module[i]).append(", ");
                }
            }
            chart.addSubtitle(new TextTitle(subTitle.toString()));
        }
    }

    /**
     * This method gets the map created by the ReadData class and stores it in the results variable.
     */
    private void getDataMap() {
        this.results = data.getResultsMap();
        this.keys = new String[data.getKeyArray().length - 2];
        System.arraycopy(data.getKeyArray(), 2, this.keys, 0, data.getKeyArray().length - 2);
    }

    /**
     * This method creates a chart panel and adds it to the JPanel.
     * @param chart The chart to be added to the panel.
     */
    private void createPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(500, 400));
        add(panel);
    }

    // For unit testing
    int[] getTestIntList(){ return testIntList; }

    Map<String, Integer> getTestMap(){ return testMap; }

    double[] getTestDoubleList(){ return testDoubleList; }

    int getTestBins(){ return testBins; }

}

// For testing purposes only. Run this to see the graph.
//class TestMPD {
//    public static void main(String[] args) throws FileNotFoundException {
//        String[] test = new String[]{"CE152-4-SP"};
//        JFrame frame = new JFrame();
//        ReadData data = new ReadData(frame);
//        JPanel panel = new ModulePerformanceDistribution().createHistogram(test, data);
//        JPanel panel2 = new ModulePerformanceDistribution().createPieChart(test, data);
//        frame.add(panel);
//        frame.add(panel2);
//        frame.pack();
//        frame.setVisible(true);
//    }
//}
