import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.util.PaintList;


public class PerformanceVsAverage extends JPanel {

    String module;
    String studentID;
    ReadData data;
    Map<String, String[]> results;
    String[] keys;
    int aboveAverage; // number of students above average (line of regression)
    int belowAverage; // number of students below average (line of regression)

    private PerformanceVsAverage(String module, String studentID, ReadData data) {
        this.module = module;
        this.studentID = studentID;
        this.data = data;
    }

    private PerformanceVsAverage(String module, ReadData data) {
        this.module = module;
        this.data = data;
    }

    public PerformanceVsAverage() {}

    /**
     * Method to create and return a histogram of the student's performance in the specified module.
     * @param module The module to be displayed in the histogram.
     * @param studentID The student's ID.
     * @param data The data to be used to create the histogram.
     * @return A histogram of the student's performance in the specified module.
     * */
    public PerformanceVsAverage createHistogram(String module, String studentID, ReadData data) {
        PerformanceVsAverage histogram = new PerformanceVsAverage(module, studentID, data);
        histogram.getDataMap();

        IntervalXYDataset dataset = histogram.createDataset(false);

        JFreeChart chart = ChartFactory.createHistogram(
                "Student " + studentID + "'s " + module + " performance",
                "Overall Average Grade",
                "Student " + module + " Grade",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        histogram.configureHistogram(chart, dataset.getSeriesCount() != 1);

        // Create Panel
        histogram.createPanel(chart);
        return histogram;
    }

    /**
     * Method to create and return a histogram of the student's performance in the specified module.
     * @param module The module to be displayed in the histogram.
     * @param data The data to be used to create the histogram.
     * @return A histogram of the student's performance in the specified module.
     * */
    public PerformanceVsAverage createHistogram(String module, ReadData data) {
        PerformanceVsAverage histogram = new PerformanceVsAverage(module, data);
        histogram.getDataMap();

        JFreeChart chart = ChartFactory.createHistogram(
                "Student " + module + " performance",
                "Overall Average Grade",
                "Student " + module + " Grade",
                histogram.createDataset(false),
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        histogram.configureHistogram(chart, false);

        // Create Panel
        histogram.createPanel(chart);
        return histogram;
    }

    /**
     * Method to configure the histogram - sets the range for the x axis and the tick unit.
     * If the chart has multiple series, it sets the bar painter and the colour of the bars.
     * @param chart The chart to be configured.
     * @param multiSeries Whether the chart has multiple series.
     * */
    private void configureHistogram(JFreeChart chart, boolean multiSeries) {
        NumberAxis xAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        xAxis.setRange(0, 100); // set the range for x axis
        xAxis.setTickUnit(new NumberTickUnit(10)); // set the tick unit to 10

        if (multiSeries) {
            XYPlot plot = chart.getXYPlot();
            XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
            renderer.setBarPainter(new StandardXYBarPainter());
            Paint barPaint = new Color(197, 52, 52, 128);
            renderer.setSeriesPaint(0, barPaint);
            barPaint = new Color(52, 120, 197);
            renderer.setSeriesPaint(1, barPaint);
            plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
        }
    }

    /**
     * Method to create and return a scatter plot of the student's performance in the specified module.
     * @param module The module to be displayed in the scatter plot.
     * @param studentID The student's ID.
     * @return A scatter plot of the student's performance in the specified module.
     * */
    public PerformanceVsAverage createScatterPlot(String module, String studentID, ReadData data) {
        PerformanceVsAverage scatterPlot = new PerformanceVsAverage(module, studentID, data);
        scatterPlot.getDataMap();
        // Create dataset
        XYDataset dataset = scatterPlot.createDataset(true);
        // Create chart
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Student " + studentID + "'s " + module + " performance",
                "Overall average grade", "Student " + module + " Grade", dataset);

        if (dataset.getSeriesCount() == 3) {
            scatterPlot.configureScatter(chart, 2);
        } else {
            scatterPlot.configureScatter(chart, 1);
        }

        // Create Panel
        scatterPlot.createPanel(chart);
        return scatterPlot;
    }

    /**
     * Method to create and return a scatter plot of the student's performance in the specified module.
     * @param module The module to be displayed in the scatter plot.
     * @return A scatter plot of the student's performance in the specified module.
     * */
    public PerformanceVsAverage createScatterPlot(String module, ReadData data) {
        PerformanceVsAverage scatterPlot = new PerformanceVsAverage(module, data);
        scatterPlot.getDataMap();
        // Create dataset
        XYDataset dataset = scatterPlot.createDataset(true);

        // Create chart
        JFreeChart chart = ChartFactory.createScatterPlot(
                module + " performance",
                "Overall average grade", "Student " + module + " Grade", dataset);

        scatterPlot.configureScatter(chart, 1);

        // Create Panel
        scatterPlot.createPanel(chart);
        return scatterPlot;
    }

    /**
     * Method to configure the scatter plot - sets the range for the x and y axis and the colour of the points.
     * @param chart The chart to be configured.
     * @param series The series to be configured.
     * */
    private void configureScatter(JFreeChart chart, int series) {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesLinesVisible(series, Boolean.TRUE);
        renderer.setSeriesShapesVisible(series, Boolean.FALSE);
        renderer.setSeriesPaint(series, Color.BLACK);
        ValueAxis domain = plot.getDomainAxis();
        ValueAxis range = plot.getRangeAxis();
        domain.setRange(0, 100);
        range.setRange(0, 100);
        plot.setBackgroundPaint(new Color(246, 246, 246));
        final Marker start = new ValueMarker(100);
        start.setPaint(Color.black);
        start.setLabel("Above average: " + aboveAverage);
        start.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
        start.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        plot.addRangeMarker(start);
        final Marker finish = new ValueMarker(95);
        finish.setPaint(new Color(000, 000 ,000, 0));
        finish.setLabel("Below average: " + belowAverage);
        finish.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
        finish.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        plot.addRangeMarker(finish);

    }

    /**
     * Method to create and return a dataset for the histogram.
     * @param linearRegressionRequired Whether a linear regression line is required.
     * @return A dataset for the histogram.
     * */
    private IntervalXYDataset createDataset(boolean linearRegressionRequired) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries student = null;

        if (studentID != null) {
            student = new XYSeries(studentID);
        }
        XYSeries students = new XYSeries("Students");
        String[] chosenModuleGrades = this.results.get(module);

        ArrayList<Integer> xCords = new ArrayList<>();
        ArrayList<Integer> yCords = new ArrayList<>();
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
                xCords.add(sum);
                yCords.add(grade);
                if (results.get("Student RegNo")[i].equals(studentID)) {
                    student.add(sum, grade);
                } else {
                    students.add(sum, grade);
                }
            }
        }

        dataset.addSeries(students);
        if (studentID != null && !student.isEmpty())
            dataset.addSeries(student);

        if (linearRegressionRequired) {
            XYSeries linearReg = new XYSeries("Line of regression");
            LinearRegression lr = new LinearRegression(xCords, yCords);
            int[][] points = lr.getLinearCords();
            aboveAverage = lr.getAboveAverage();
            belowAverage = lr.getBelowAverage();
            for (int[] point : points) {
                linearReg.add(point[0], point[1]);
            }
            dataset.addSeries(linearReg);
        }

        return dataset;
    }

    private void getDataMap() {
        this.results = data.getResultsMap();
        this.keys = new String[data.getKeyArray().length -2];
        System.arraycopy(data.getKeyArray(), 2, this.keys, 0, data.getKeyArray().length -2);
    }

    private void createPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(500, 400));
        add(panel);
    }
}
