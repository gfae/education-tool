import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to generate a JPanel that displays the top, average and bottom 3 students for the given modules.
 */
public class StudentPerformance extends JPanel {

    String[] modules; // the modules that the user has selected
    ReadData data;
    Map<String, String[]> results;
    String[] keys;
    boolean isGrouped = false; // if the user has grouped the modules

    private StudentPerformance(String[] modules, ReadData data) {
        this.modules = modules;
        this.data = data;
    }

    public StudentPerformance() {}

    /**
     * Method to create and return a bar chart of the top, average and bottom 3 students for the given modules.
     * @param modules The modules that the user has selected.
     * @param data The data to be used to create the bar chart.
     * @return A bar chart of the top, average and bottom 3 students for the given modules.
     */
    public StudentPerformance createBarChart(String[] modules, ReadData data) {
        StudentPerformance barChart = new StudentPerformance(modules, data);
        barChart.getDataMap();

        JFreeChart chart = ChartFactory.createBarChart(
                "Student Performance",
                "Student ID",
                "Grade",
                barChart.createDataset(),
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        if (modules.length == 1) {
            chart.setTitle(modules[0] + " Student Performance");
        }

        barChart.createPanel(chart);
        barChart.configureBarChart(chart);
        return barChart;
    }

    /**
     * Method to configure the bar chart. This includes setting the tick unit for the y axis, setting the y axis to be 0 to 100,
     * @param chart The bar chart to be configured.
     * */
    private void configureBarChart(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(-1);
        // Set tick unit for y axis
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // set y axis to be 0 to 100
        yAxis.setRange(0, 100);
        // set background colour
        plot.setBackgroundPaint(new Color(241, 241, 241));
    }

    /**
     * Method will creat the dataset for the bar chart.
     * @return The dataset for the bar chart.
     * */
    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<Student> grades = getGrades(modules, results);
        String[][] topPerformers = topPerformers(grades);
        String[][] averagePerformers = averagePerformers(grades);
        String[][] bottomPerformers = bottomPerformers(grades);
        for (int i = 0; i < 3; i++) {
            dataset.addValue(Integer.parseInt(topPerformers[i][1]),topPerformers[i][0], "Top Performers");
            dataset.addValue(Integer.parseInt(averagePerformers[i][1]), averagePerformers[i][0], "Average Performers");
            dataset.addValue(Integer.parseInt(bottomPerformers[i][1]), bottomPerformers[i][0], "Bottom Performers");
        }
        return dataset;
    }

    /**
     * This method will be called to create the table.
     * @param modules   the modules that the user has selected
     * @return the JPanel that contains the table
     */
    public StudentPerformance createTable(String[] modules, ReadData data) {
        StudentPerformance table = new StudentPerformance(modules, data);

        table.getDataMap();
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        ArrayList<Student> grades = table.getGrades(modules, table.results);
        if (modules.length > 1) {
            table.isGrouped = true;
            panel.setLayout(new GridBagLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Student Performance"));
            // Change font size of title within border
            TitledBorder border = (TitledBorder) panel.getBorder();
            border.setTitleFont(new Font("Arial", Font.PLAIN, 20));
            JPanel subPanel = new JPanel();
            subPanel.setBackground(Color.WHITE);
            subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
            table.addSubTables(subPanel, grades);
            // Add label to the top of the table right aligned
            JLabel label = table.createLabel();
            label.setAlignmentX(Component.RIGHT_ALIGNMENT);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            panel.add(label, c);
            c.gridy = 1;
            panel.add(subPanel, c);
        } else {
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                JPanel subPanel = new JPanel();
                subPanel.setBackground(Color.WHITE);
                subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
                subPanel.setBorder(BorderFactory.createTitledBorder(modules[0] + " Student Performance"));
                // Change font size of title within border
                TitledBorder border = (TitledBorder) subPanel.getBorder();
                border.setTitleFont(new Font("Arial", Font.PLAIN, 20));
                addSubTables(subPanel, grades);
                panel.add(subPanel);
        }
        table.add(panel);
        table.setBackground(Color.WHITE);
        return table;
    }

    /**
     * Creates subtitle label for the table.
     * @return the label
     */
    private JLabel createLabel() {
        JLabel label = new JLabel();
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        // Label lists all the modules that the user has selected, if 14 modules then it will just say all modules
        if (modules.length == 14) {
            label.setText("For all modules");
        } else {
            label.setText("Modules: ");
            int count = 0;
            for (String module : modules) {
                if (count == 6) {
                    label.setText(label.getText() + "<br>");
                    count = 0;
                } else {
                    label.setText(label.getText() + module + ", ");
                    count++;
                }
            }
            // Remove the last comma and space
            label.setText(label.getText().substring(0, label.getText().length() - 2));
        }
        label.setText("<html>" + label.getText() + "</html>");
        return label;
    }

    /**
     * This method will add the subtables to the main table.
     * @param subPanel the panel that the subtables will be added to
     * @param grades   the grades of the students
     */
    private void addSubTables(JPanel subPanel, ArrayList<Student> grades) {
        String[][] topPerformers = topPerformers(grades);
        String[][] averagePerformers = averagePerformers(grades);
        String[][] bottomPerformers = bottomPerformers(grades);
        subPanel.add(createSubTable("Top Performers", topPerformers));
        subPanel.add(createSubTable("Average Performers", averagePerformers));
        subPanel.add(createSubTable("Bottom Performers", bottomPerformers));
    }

    /**
     * This method will create the subtables. It's a custom table using GridBagLayout instead of JTable.
     * This was done to allow for easier formatting of the table.
     * @param title the title of the subtable
     * @param data  the data that will be displayed in the subtable
     * @return the subtable
     */
    private JPanel createSubTable(String title, String[][] data) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JLabel text = new JLabel(title);
        text.setFont(new Font("Arial", Font.BOLD, 15));
        text.setBackground(Color.WHITE);
        text.setOpaque(true);
        text.setPreferredSize(new Dimension(166, 20));
        text.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK),
                BorderFactory.createEmptyBorder(3, 4, 3, 4)));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(text, c);
        text = new JLabel("Student ID");
        text.setBackground(Color.WHITE);
        text.setOpaque(true);
        text.setPreferredSize(new Dimension(83, 20));
        text.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 0, Color.BLACK),
                BorderFactory.createEmptyBorder(3, 4, 3, 4)));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        panel.add(text, c);
        text = new JLabel("Grade");
        text.setBackground(Color.WHITE);
        text.setOpaque(true);
        text.setPreferredSize(new Dimension(83, 20));
        text.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
                BorderFactory.createEmptyBorder(3, 4, 3, 4)));
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        panel.add(text, c);
        for (int i = 0; i < 3; i++) {
            text = new JLabel(data[i][0]);
            text.setFont(new Font("Arial", Font.PLAIN, 12));
            text.setBackground(Color.WHITE);
            text.setOpaque(true);
            text.setPreferredSize(new Dimension(83, 20));
            text.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK),
                    BorderFactory.createEmptyBorder(3, 4, 3, 4)));
            c.gridx = 0;
            c.gridy = i + 2;
            c.gridwidth = 1;
            panel.add(text, c);
            text = new JLabel(data[i][1]);
            text.setFont(new Font("Arial", Font.PLAIN, 12));
            text.setBackground(Color.WHITE);
            text.setOpaque(true);
            text.setPreferredSize(new Dimension(83, 20));
            text.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK),
                    BorderFactory.createEmptyBorder(3, 4, 3, 4)));
            c.gridx = 1;
            c.gridy = i + 2;
            c.gridwidth = 1;
            panel.add(text, c);
        }

        return panel;
    }

    /**
     * This method will get the grades of the students for the given modules.
     * @param selectedModules the modules that the user has selected
     * @return the grades of the students
     */
    public static ArrayList<Student> getGrades(String[] selectedModules, Map<String, String[]> results) {
        ArrayList<Student> grades = new ArrayList<>();
        /*
         * For each student, get their overall average grade for the selected modules and add them to the grades ArrayList.
         * Null values are ignored and will not count towards the average calculation.
         */
        for (int index = 0; index < results.get("Student RegNo").length; index++) {
            int studentGradesTotal = 0;
            int modulesTaken = selectedModules.length;
            String course = results.get("Course")[index];
            Map<String, Integer> moduleGrades = new HashMap<>();
            for (String selectedModule : selectedModules) {
                String moduleName;
                int moduleGrade;
                try {
                    moduleGrade = Integer.parseInt(results.get(selectedModule)[index]);
                    studentGradesTotal += moduleGrade;
                    moduleName = selectedModule.substring(0, 5);
                    moduleGrades.put(moduleName, moduleGrade);
                } catch (NumberFormatException e) {
                    modulesTaken--; // if the student has not taken a module, it will not count towards the average
                }
            }
            // if the student has not taken any of the selected modules, they will not be added to the ArrayList
            if (modulesTaken != 0) {
                grades.add(new Student(results.get("Student RegNo")[index], studentGradesTotal / modulesTaken, moduleGrades, course));
            }
        }
        Collections.sort(grades); // sort the grades ArrayList in ascending order
        Collections.reverse(grades); // reverse the grades ArrayList so that it is in descending order

        return grades;
    }

    /**
     * This method will get the top 3 performers.
     * Package-private for testing purposes.
     * @param grades the grades of the students
     * @return the top 3 performers
     */
    String[][] topPerformers(ArrayList<Student> grades) {
        String[][] topPerformers = new String[3][];
        for (int i = 0; i < 3; i++) {
            String studentID = grades.get(i).getStudentID();
            String studentGrade = String.valueOf(grades.get(i).getGrade());
            topPerformers[i] = new String[]{studentID, studentGrade};
        }
        return topPerformers;
    }

    /**
     * This method will get the average performers.
     * Package-private for testing purposes.
     * @param grades the grades of the students
     * @return the average performers
     */
    String[][] averagePerformers(ArrayList<Student> grades) {
        String[][] averagePerformers = {new String[]{"", "101"}, new String[]{"", "101"}, new String[]{"", "101"}};
        // Find the mean grade of the students
        int meanGrade = 0;
        for (Student grade : grades) {
            meanGrade += grade.getGrade();
        }
        meanGrade /= grades.size();
        // Find the 3 student with the grade closest to the mean grade
        for (Student grade : grades) {
            int studentGrade = grade.getGrade();
            for (int i = 0; i < 3; i++) {
                int closestGrade = Integer.parseInt(averagePerformers[i][1]);
                if (Math.abs(studentGrade - meanGrade) < Math.abs(closestGrade - meanGrade)) {
                    String studentGradeString = String.valueOf(studentGrade);
                    averagePerformers[i] = new String[]{grade.getStudentID(), studentGradeString};
                    break;
                }
            }
        }


        return averagePerformers;
    }

    /**
     * This method will get the bottom 3 performers.
     * Package-private for testing purposes.
     * @param grades the grades of the students
     * @return the bottom 3 performers
     */
    String[][] bottomPerformers(ArrayList<Student> grades) {
        String[][] bottomPerformers = new String[3][];
        for (int i = 0; i < 3; i++) {
            String studentID = grades.get(grades.size() - i - 1).getStudentID();
            String studentGrade = String.valueOf(grades.get(grades.size() - i - 1).getGrade());
            bottomPerformers[2-i] = new String[]{studentID, studentGrade};
        }
        return bottomPerformers;
    }

    /**
     * This method gets the map created by the ReadData class and stores it in the results variable.
     */
    private void getDataMap() {
        results = data.getResultsMap();
        this.keys = new String[data.getKeyArray().length - 2];
        System.arraycopy(data.getKeyArray(), 2, this.keys, 0, data.getKeyArray().length - 2);
    }

    /**
     * This method creates a chart panel and adds it to the JPanel.
     *
     * @param chart The chart to be added to the panel.
     */
    private void createPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(500, 400));
        add(panel);
    }
}

/**
 * Type class for storing student data.
 */
record Student(String studentID, int grade, Map<String, Integer> moduleGrades,
               String course) implements Comparable<Student> {

    /**
     * Constructor for the Student class.
     *
     * @param studentID    the student's ID
     * @param grade        the student's overall grade
     * @param moduleGrades the student's grades for each module
     * @param course       the student's course
     */
    Student {
    }

    /**
     * This method will get the grade of a specific module.
     *
     * @param module the module to get the grade of
     * @return the grade of the module
     */
    public int getModuleGrade(String module) {
        try {
            return moduleGrades.get(module);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    // Getters
    public String getStudentID() {
        return studentID;
    }

    public int getGrade() {
        return grade;
    }

    public String getCourse() {
        return course;
    }


    // Override sort method to sort by grade
    @Override
    public int compareTo(Student o) {
        return Integer.compare(this.grade, o.grade);
    }
}

// Main method for testing purposes only
//class Main {
//    public static void main(String[] args) throws FileNotFoundException {
//        JFrame testFrame = new JFrame();
//        testFrame.setPreferredSize(new Dimension(500, 300));
//        testFrame.setVisible(true);
//        ReadData data = new ReadData(testFrame);
//        int[] arr = {10, 2, 3, 4, 5, 6, 7, 8, 9};
//        int studentID = 12345;
//        String[] modules = {"CE101-4-FY", "CE141-4-FY"};
//        testFrame.add(new StudentPerformance().createTable(modules, data));
//        testFrame.pack();
//    }
//}

