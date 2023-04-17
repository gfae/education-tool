import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class CourseComparison  extends JPanel {

    String module;
    Map<String, String[]> results;
    String[] keys;
    ReadData data;

    private XYSeriesCollection xySeriesCollection;
    private CourseComparison(String module, ReadData data) {
        this.module = module;
        this.data = data;
    }

    public CourseComparison() {}

    /**
     * Method creates a histogram for the module passed in.
     * @param module The module to create a histogram for.
     * @param data The data to create the histogram with.
     * @return The histogram for the module.
     * */
    public CourseComparison createHistogram(String module, ReadData data) {
        CourseComparison histogram = new CourseComparison(module, data);
        histogram.getDataMap();

        XYSeriesCollection dataset = histogram.createDataset();

        JFreeChart chart = ChartFactory.createHistogram(
                "Course Comparison for " + module,
                "Overall Average Grade",
                "Student " + module + " Grade",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        histogram.configureChart(chart, dataset);

        histogram.createPanel(chart);
        return histogram;
    }

    /**
     * Method configures the histogram chart.
     * @param chart The chart to configure.
     * @param dataset The dataset to configure the chart with.
     * */
    private void configureChart(JFreeChart chart, XYSeriesCollection dataset) {
        NumberAxis xAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        xAxis.setRange(0, 100); // set the range for x axis
        xAxis.setTickUnit(new NumberTickUnit(10)); // set the tick unit to 10

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(246, 246, 246));
        List seriesList = dataset.getSeries();
        // Iterate through the series and set the bar width

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setMargin(0.0);
        // Set colour for each bar by series name
        for (int i = 0; i < seriesList.size(); i++) {
            XYSeries series = (XYSeries) seriesList.get(i);
            Paint barPaint;
            if (series.getKey().equals("COMPUTER SCIENCE")) {
                barPaint = new Color(197, 52, 52, 128);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("DATA SCI & ANALYTICS")) {
                barPaint = new Color(255, 203, 0, 224);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("COMPUTING")) {
                barPaint = new Color(52, 153, 197, 128);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("NEURAL TECH W PSYCH 3YR BSC")) {
                barPaint = new Color(72, 0, 255, 128);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("COMPUTER GAMES")) {
                barPaint = new Color(156, 52, 197, 128);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("STUDENT SUPPORT")) {
                barPaint = new Color(79, 197, 52, 128);
                renderer.setSeriesPaint(i, barPaint);
            }
        }
    }

    /**
     * Method creates a scatter plot for the module passed in.
     * @param module The module to create a scatter plot for.
     * @param data The data to create the scatter plot with.
     * @return The scatter plot for the module.
     * */
    public CourseComparison createScatterPlot(String module, ReadData data) {
        CourseComparison scatterPlot = new CourseComparison(module, data);
        scatterPlot.getDataMap();
        // Create dataset
        XYSeriesCollection dataset = scatterPlot.createDataset();
        // Create chart
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Course Comparison for " + module,
                "Overall Average Grade",
                "Student " + module + " Grade", dataset);

        scatterPlot.configureScatterPlot(chart, dataset);
        scatterPlot.createPanel(chart);
        return scatterPlot;
    }

    /**
     * Method configures the scatter plot chartMainGUI.
     * @param chart The chart to configure.
     * @param dataset The dataset to configure the chart with.
     * */
    private void configureScatterPlot(JFreeChart chart, XYSeriesCollection dataset) {
        NumberAxis xAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        xAxis.setRange(0, 100); // set the range for x axis
        xAxis.setTickUnit(new NumberTickUnit(10)); // set the tick unit to 10
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(236, 236, 236));
        plot.setDomainGridlinePaint(new Color(94, 94, 94));
        plot.setRangeGridlinePaint(new Color(94, 94, 94));
        // change series shape to be a circle
        List seriesList = dataset.getSeries();
        // Iterate through the series and set the bar width
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        for (int i = 0; i < seriesList.size(); i++) {
            XYSeries series = (XYSeries) seriesList.get(i);
            renderer.setSeriesShape(i, new Ellipse2D.Double(-3, -3, 5, 5));
            Paint barPaint;
            // Set colour for each bar by series name
            if (series.getKey().equals("COMPUTER SCIENCE")) {
                barPaint = new Color(197, 52, 52);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("DATA SCI & ANALYTICS")) {
                barPaint = new Color(255, 203, 0);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("COMPUTING")) {
                barPaint = new Color(52, 153, 197);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("NEURAL TECH W PSYCH 3YR BSC")) {
                barPaint = new Color(72, 0, 255);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("COMPUTER GAMES")) {
                barPaint = new Color(156, 52, 197);
                renderer.setSeriesPaint(i, barPaint);
            } else if (series.getKey().equals("STUDENT SUPPORT")) {
                barPaint = new Color(79, 197, 52);
                renderer.setSeriesPaint(i, barPaint);
            }
        }
    }

    /**
     * Method creates a bar chart for the module passed in.
     * @param module The module to create a bar chart for.
     * @param data The data to create the bar chart with.
     * @return The bar chart for the module.
     * */
    public CourseComparison createBarChart(String module, ReadData data) {
        CourseComparison barChart = new CourseComparison(module, data);
        barChart.getDataMap();
        // Create dataset
        DefaultCategoryDataset dataset = barChart.createBarDataset();
        // Create chart
        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Course Comparison for " + module,
                "",
                "Total Students (per course)",
                dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );

        barChart.configureBarChart(chart);
        barChart.createPanel(chart);
        return barChart;
    }

    /**
     * Method configures the bar chart.
     * @param chart The chart to configure.
     * */
    private void configureBarChart(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(0);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        DecimalFormat pctFormat = new DecimalFormat("#%");
        rangeAxis.setNumberFormatOverride(pctFormat);
        // Override number format to show percentage on hover
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", pctFormat));
        renderer.setDefaultItemLabelsVisible(true);
    }

    /**
     * Method creates a dataset for the bar chart.
     * @return The dataset for the bar chart.
     * */
    private DefaultCategoryDataset createBarDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] chosenModuleGrades = this.results.get(module);
        // Mapping the grades to the course
        Map<String, ArrayList<Integer>> courseGrades = new HashMap<>();
        for (int i = 0; i < chosenModuleGrades.length; i++) {
            int grade;
            try {
                grade = Integer.parseInt(chosenModuleGrades[i]);
                courseGrades.putIfAbsent(results.get("Course")[i], new ArrayList<>());
                courseGrades.get(results.get("Course")[i]).add(grade);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        // Adding the grades to the dataset
        for (String course : courseGrades.keySet()) {
            Map<String, Integer> grades = new LinkedHashMap<>();
            grades.put("1st", 0);
            grades.put("2:1", 0);
            grades.put("2:2", 0);
            grades.put("3rd", 0);
            grades.put ("Fail", 0);
            for (int grade : courseGrades.get(course)) {
                if (grade >= 70) {
                    grades.put("1st", grades.get("1st") + 1);
                } else if (grade >= 60) {
                    grades.put("2:1", grades.get("2:1") + 1);
                } else if (grade >= 50) {
                    grades.put("2:2", grades.get("2:2") + 1);
                } else if (grade >= 40) {
                    grades.put("3rd", grades.get("3rd") + 1);
                } else {
                    grades.put("Fail", grades.get("Fail") + 1);
                }
            }
            int totalNumOfStudents = courseGrades.get(course).size();
            for (String grade : grades.keySet()) {
                if (grades.get(grade) == 0) {
                    continue;
                }
                double percentage = (double) grades.get(grade) / totalNumOfStudents;
                dataset.addValue(percentage, grade, course);
            }
        }
        return dataset;
    }

    /**
     * Method to create the dataset for the scatter plot.
     * @return The dataset for the scatter plot.
     * */
    private XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        Map<String, XYSeries> series = new HashMap<>();
        // Populate the dataset with the series for each course, found using the course key
        for (String course : results.get("Course")) {
            if (course != null) {
                series.put(course, new XYSeries(course));
            }
        }

        String[] chosenModuleGrades = this.results.get(module);

        // Mapping the grades to the course
        for (int i = 0; i < chosenModuleGrades.length; i++) {
            int grade = 0;
            int sum = 0;
            int moduleCount = 0;
            try {
                grade = Integer.parseInt(chosenModuleGrades[i]);
                for (String moduleName: keys) {
                    String curGrade = results.get(moduleName)[i];
                    try {
                        sum += Integer.parseInt(curGrade);
                        moduleCount++;
                    } catch (Exception e) {
                        // Do nothing.
                    }
                }
            } catch (Exception e) {
                // Do nothing.
            }
            if (moduleCount > 1) {
                sum -= grade;
                sum = sum/(moduleCount-1);
                String course = results.get("Course")[i];
                series.get(course).add(sum, grade);
            }
        }

        for (XYSeries courseSeries : series.values()) {
            if (courseSeries.getItemCount() > 0) {
                dataset.addSeries(courseSeries);
            }
        }

        xySeriesCollection = dataset;
        return dataset;
    }

    /**
     * Method to get the data map from the ReadData class.
     */
    private void getDataMap() {
        this.results = data.getResultsMap();
        this.keys = new String[data.getKeyArray().length -2];
        System.arraycopy(data.getKeyArray(), 2, this.keys, 0, data.getKeyArray().length -2);
    }

    /**
     * Method creates a panel for the chart.
     * @param chart The chart to create a panel for.
     * */
    private void createPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(500, 400));
        add(panel);
    }
    // For unit testing
    XYSeriesCollection getXySeriesCollection() {
        return xySeriesCollection;
    }
}
