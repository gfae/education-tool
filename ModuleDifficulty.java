import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Map;

public class ModuleDifficulty extends JPanel {

    String[] modules; // Array of modules
    Map<String, String[]> results; // Holds the data from the CSV file
    String[] keys; // Holds the keys for the data in the CSV file
    ReadData data; // Object to read the data from the CSV file

    private String dif;

    /**
     *  Constructor for the ModuleDifficulty class.
     *  @param modules Array of modules you want to display.
     *  @param data Object to read the data from the CSV file
     */

    private ModuleDifficulty(String[] modules, ReadData data) {
        this.modules = modules;
        this.data = data;
    }
    public ModuleDifficulty() {}

    /**
     *  Method to create table, the parameter is also the array of modules you want to display.
     *  This could be one module or more.
     *  @param modules Array of modules you want to display.
     *  @param data Object to read the data from the CSV file
     */
    public ModuleDifficulty createTable(String[] modules, ReadData data) {
        ModuleDifficulty table = new ModuleDifficulty(modules, data);
        table.getDataMap();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (String module : modules) {
            String[] grades = table.results.get(module);
            JPanel subTable = table.createSubTable(grades, false);
            subTable.setBorder(BorderFactory.createTitledBorder(module));
            // Change font size of title within border
            TitledBorder border = (TitledBorder) subTable.getBorder();
            border.setTitleFont(new Font("Arial", Font.PLAIN, 20));
            panel.add(subTable);
        }
        table.add(panel);
        return table;
    }

    /**
     * Method to create the sub table for each module.
     * @param grades Array of grades for the module.
     * @param pieChart Boolean to determine if the sub table is attached to pie chart or not.
     * @return JPanel containing the sub table.
     * */
    private JPanel createSubTable(String[] grades, boolean pieChart) {
        // Difficulty is being classified by the students grade in the module vs their overall average grade.
        int easy = 0;
        int medium = 0;
        int hard = 0;
        int gradeLength = 0;
        for (int i = 0; i < grades.length; i++) {
            int grade = 0;
            int sum = 0;
            int moduleCount = 0;
            try {
                grade = Integer.parseInt(grades[i]);
                gradeLength++;
                for (String module : keys) {
                    String curGrade = results.get(module)[i];
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
            // Check if the grade is above or below the average.
            // Easy means the grade is 10% or more above students average.
            // Medium means the grade is between +10% and -10% below students average.
            // Hard means the grade is 10% or more below students average.
            if (grade != 0) {
                if (grade >= sum / moduleCount * 1.1) {
                    easy++;
                } else if (grade >= sum / moduleCount * 0.9) {
                    medium++;
                } else {
                    hard++;
                }
            }
        }
        // Check if module is easy, medium or hard.
        // Easy means the module is easy for 35% or more of the students.
        // Medium means the module is easy for between 35% and 25% of the students.
        // Hard means the module is easy for 25% or less of the students.
        String difficulty;
        Color color;
        if (easy > gradeLength * 0.35) {
            difficulty = "Easy";
            color = new Color(0, 227, 0);
        } else if (easy < gradeLength * 0.25) {
            difficulty = "Hard";
            color = new Color(211, 0, 0);
        } else {
            difficulty = "Medium";
            color = new Color(253, 253, 50);
        }

        JPanel breakdown = new JPanel();
        breakdown.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        int labelWidth = (pieChart) ? 350 : 250;
        int percentageWidth = (pieChart) ? 150 : 50;

        JLabel text = new JLabel("Percentage of students who scored: ");
        text.setBackground(Color.WHITE);
        text.setOpaque(true);
        text.setPreferredSize(new Dimension(labelWidth+percentageWidth, 20));
        text.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,0,1,Color.BLACK),
                BorderFactory.createEmptyBorder(3,3,3,3)));
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 2;
        breakdown.add(text, con);
        text = new JLabel("+10% above their average grade");
        setLabel(con, labelWidth, text);
        con.gridy = 1;
        con.gridwidth = 1;
        breakdown.add(text, con);
        text = new JLabel( easy * 100 / gradeLength + "%");
        setPercentage(con, percentageWidth, text);
        con.gridy = 1;
        breakdown.add(text, con);

        text = new JLabel("±10% in range of their average grade");
        setLabel(con, labelWidth, text);
        con.gridy = 2;
        breakdown.add(text, con);
        text = new JLabel( medium * 100 / gradeLength + "%");
        setPercentage(con, percentageWidth, text);
        con.gridy = 2;
        breakdown.add(text, con);

        text = new JLabel("-10% below their average grade");
        setLabel(con, labelWidth, text);
        con.gridy = 3;
        breakdown.add(text, con);
        text = new JLabel( hard * 100 / gradeLength + "%");
        setPercentage(con, percentageWidth, text);
        con.gridy = 3;
        breakdown.add(text, con);

        text = new JLabel("Module difficulty: " + difficulty);
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setPreferredSize(new Dimension(labelWidth+percentageWidth, 20));
        text.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK),
                BorderFactory.createEmptyBorder(3,3,3,3)));
        text.setBackground(color);
        text.setOpaque(true);
        con.gridx = 0;
        con.gridy = 4;
        con.gridwidth = 2;
        breakdown.add(text, con);
        return breakdown;
    }

    /**
     * Method to style the percentage labels.
     * @param con GridBagConstraints for the panel.
     * @param percentageWidth Width of the percentage label.
     * @param text JLabel to be styled.
     * */
    private void setPercentage(GridBagConstraints con, int percentageWidth, JLabel text) {
        text.setBackground(Color.WHITE);
        text.setOpaque(true);
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setPreferredSize(new Dimension(percentageWidth, 20));
        text.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,0,1,Color.BLACK),
                BorderFactory.createEmptyBorder(3,3,3,3)));
        con.gridx = 1;
    }

    /**
     * Method to style the labels.
     * @param con GridBagConstraints for the panel.
     * @param labelWidth Width of the label.
     * @param text JLabel to be styled.
     * */
    private void setLabel(GridBagConstraints con, int labelWidth, JLabel text) {
        text.setBackground(Color.WHITE);
        text.setOpaque(true);
        text.setPreferredSize(new Dimension(labelWidth, 20));
        text.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,0,0,Color.BLACK),
                BorderFactory.createEmptyBorder(3,3,3,3)));
        con.gridx = 0;
    }

    /**
     * Method to create the pie chart.
     * @param modules Array of modules.
     * @param data ReadData object.
     * @return ModuleDifficulty object.
     * */
    public ModuleDifficulty createPieChart(String[] modules, ReadData data)  {
        ModuleDifficulty pieChart = new ModuleDifficulty(modules, data);
        pieChart.setLayout(new BoxLayout(pieChart, BoxLayout.Y_AXIS));
        pieChart.getDataMap();
        String[] grades = pieChart.results.get(modules[0]);
        JFreeChart chart = ChartFactory.createPieChart(
                modules[0] + " Difficulty Breakdown", //chart title
                pieChart.createDataset(grades), //not sure where does this link to
                true,
                true,
                false
        );
        //set chart background color
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(231, 231, 231));
        // add percentage to pie chart
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0}: {2}", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(labelGenerator);
        pieChart.createPanel(chart);
        pieChart.add(pieChart.createSubTable(grades, true));
        return pieChart;
    }

    /**
     * Method to create the dataset for the pie chart.
     * @param grades Array of grades.
     * @return PieDataset object.
     * */
    private PieDataset<String> createDataset(String[] grades) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        int[] count = new int[2];
        for (String studentGrades : grades) {
            try {
                int check = Integer.parseInt(studentGrades);
                if (check >= 40) {
                    count[0]++; //increase value at index 0 (no.StudentsPassed)
                } else {
                    count[1]++; //increase value at index 1 (no.StudentsFailed)
                }
            } catch (NumberFormatException e) {
                //do nothing
            }
        }
        int pStudentsPassed = count[0] * 100 / (count[0] + count[1]); //divide by total no. of students
        int pStudentsFailed = count[1] * 100 / (count[0] + count[1]);
        dataset.setValue("Passed", pStudentsPassed);
        dataset.setValue("Failed", pStudentsFailed);

        return dataset;
    }

    /**
     * This method gets the map created by the ReadData class and stores it in the results variable.
     * */
    private void getDataMap() {
        this.results = data.getResultsMap();
        this.keys = new String[data.getKeyArray().length -2];
        System.arraycopy(data.getKeyArray(), 2, this.keys, 0, data.getKeyArray().length -2);
    }

    /**
     * This method creates a chart panel and adds it to the JPanel.
     * @param chart The chart to be added to the panel.
     * */
    private void createPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(500, 400));
        add(panel);
    }

    String getDif(){
        return dif;
    }
}

// For testing purposes only. Run this to see the graph.
//class TestMD {
//    public static void main(String[] args) throws FileNotFoundException {
//        JFrame frame = new JFrame();
//        ReadData data = new ReadData(frame);
//        JPanel panel = new ModuleDifficulty().createPieChart((new String[]{"CE101-4-FY"}), data);
//        JPanel panel2 = new ModuleDifficulty().createTable(new String[]{"CE101-4-FY"}, data);
//        frame.add(panel2);
//        frame.add(panel);
//        frame.pack();
//        frame.setVisible(true);
//    }
//}
