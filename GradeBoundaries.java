import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GradeBoundaries  extends JPanel {

    private String[] courses;
    private int lowerBound;
    private int upperBound;
    Map<String, String[]> results;
    private ReadData data;
    String[] keys;
    private String[] moduleCodes;
    private Map<String, String> courseMap = new HashMap<>() {{
        put("COMPUTER SCIENCE", "Comp Sci");
        put("COMPUTER GAMES", "Comp Games");
        put("COMPUTING", "Comp");
        put("DATA SCI & ANALYTICS", "Data Sci");
        put("NEURAL TECH W PSYCH 3YR BSC", "Neural Tech");
        put("STUDENT SUPPORT", "SS");
    }};

    /**
     * Creates a new GradeBoundaries object.
     * @param courses The courses to be displayed in the table.
     * @param lowerBound The lower bound of the grade.
     * @param upperBound The upper bound of the grade.
     * @param data The data to be used to create the table.
     * */
    private GradeBoundaries(String[] courses, int lowerBound, int upperBound, ReadData data) {
        this.courses = courses;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.data = data;
    }
    public GradeBoundaries(){} // default constructor

    /**
     * Method to create and return a table of students with an overall grade between the lower and upper bounds in the specified courses.
     * @param courses The courses to be displayed in the table.
     * @param lowerBound The lower bound of the grade.
     * @param upperBound The upper bound of the grade.
     * @param data The data to be used to create the table.
     * @return A table of students with an overall grade between the lower and upper bounds in the specified courses.
     * */
    public GradeBoundaries createTable(String[] courses, int lowerBound, int upperBound, ReadData data) {
        GradeBoundaries table = new GradeBoundaries(courses, lowerBound, upperBound, data);
        table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));
        table.getDataMap();

        table.setBorder(BorderFactory.createTitledBorder("<html><b>Students with an overall grade between " + lowerBound + " and " + upperBound + " in the following courses:</b> <br> <i>" + Arrays.toString(courses).replace("[", "").replace("]", "</i></html>")));
        TitledBorder border = (TitledBorder) table.getBorder();
        border.setTitleFont(new Font("Arial", Font.PLAIN, 15));

        ArrayList<Student> grades = StudentPerformance.getGrades(table.keys, table.results);
        ArrayList<Student> filteredGrades = new ArrayList<>();
        for (Student student : grades) {
            if (student.getGrade() >= lowerBound && student.getGrade() <= upperBound && Arrays.asList(courses).contains(student.getCourse())) {
                filteredGrades.add(student);
            }
        }

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        int rowsAdded = 0;
        int x = 0;
        int y = 0;
        JLabel text;
        for (int i = 0; i < filteredGrades.size(); i++) {
            // Create the table
            if (rowsAdded == 0) {
                x = 0;
                y = 0;
                text = new JLabel("Student ID");
                formatLabel(text, 65);
                con.gridx = x;
                con.gridy = y;
                subPanel.add(text, con);
                text = new JLabel("Course");
                formatLabel(text, 80);
                x++;
                con.gridx = x;
                con.gridy = y;
                subPanel.add(text, con);
                x++;
                for (String moduleName : table.moduleCodes) {
                    text = new JLabel(moduleName);
                    formatLabel(text, 50);
                    con.gridx = x;
                    con.gridy = y;
                    subPanel.add(text, con);
                    x++;
                }
                text = new JLabel("Overall Grade");
                formatLabel(text, 90);
                con.gridx = x;
                con.gridy = y;
                subPanel.add(text, con);
                y++;
            }
            Student student = filteredGrades.get(i);
            x = 0;
            text = new JLabel(student.getStudentID());
            formatLabel(text, 65);
            con.gridx = x;
            con.gridy = y;
            subPanel.add(text, con);
            text = new JLabel(courseMap.get(student.getCourse()));
            formatLabel(text, 80);
            x++;
            con.gridx = x;
            con.gridy = y;
            subPanel.add(text, con);
            x++;
            for (String moduleName : table.moduleCodes) {
                text = new JLabel(String.valueOf(student.getModuleGrade(moduleName)));
                formatLabel(text, 50);
                con.gridx = x;
                con.gridy = y;
                subPanel.add(text, con);
                x++;
            }
            text = new JLabel(String.valueOf(student.getGrade()));
            formatLabel(text, 90);
            con.gridx = x;
            con.gridy = y;
            subPanel.add(text, con);
            y++;
            rowsAdded++;
            if (rowsAdded == 50) {
                rowsAdded = 0;
                table.add(subPanel);
                subPanel = new JPanel();
                subPanel.setLayout(new GridBagLayout());
            }
        }
        table.add(subPanel);
        return table;
    }

    /**
     * This method formats a JLabel to be used in the table.
     * @param label the label to be formatted
     * @param width the width of the label */
    void formatLabel(JLabel label, int width) {
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setPreferredSize(new Dimension(width, 20));
        label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));
    }

    /**
     * This method gets the map created by the ReadData class and stores it in the results variable.
     * */
    void getDataMap() {
        this.results = data.getResultsMap();
        this.keys = new String[data.getKeyArray().length -2];
        System.arraycopy(data.getKeyArray(), 2, this.keys, 0, data.getKeyArray().length -2);
        this.moduleCodes = data.getCombinedModuleNames();
    }
}
