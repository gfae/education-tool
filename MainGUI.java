import com.itextpdf.text.DocumentException;
import org.jfree.chart.ChartPanel;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Main GUI class
 */
public class MainGUI  extends JFrame {
    public static void main(String[] args) throws FileNotFoundException {
        MainGUI gui = new MainGUI();
        gui.setVisible(true);
        ReadData readData = new ReadData();
        if (readData.isReadSuccess()) {
            gui.addStatsPanels(readData);
        }
    }

    private String[] moduleList; // List of modules
    private String[] courseList; // List of courses
    private final Map<String, StatsPanel> statsPanels = new LinkedHashMap<>() {{
        put("Student Performance vs Average", null);
        put("Course Comparison", null);
        put("Module Performance Distribution", null);
        put("Categorised Student Performance", null);
        put("Module Difficulty", null);
        put("Students by Grade Range", null);
    }}; // Map of stat name to panel
    private static final Map<String, String> statDescriptions = new HashMap<>(){}; // Map of stat name to description

    private ReadData readData; // Data object
    protected final JPanel leftPanel; // Left panel (where stats are selected)
    JSplitPane centerPanel; // Center panel (Parent of left and right panels)

    // Constructor
    MainGUI() {
        // Fullscreen frame with title
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("UOE Education Management Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // North panel - title and description
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        JTextPane descText = new JTextPane();
        descText.setContentType("text/html");
        descText.setText("<html><body style = 'font-family: Arial Nova Light; text-align: center'>" +
                "<b style = 'font-size: 22'>Education Management Tool</b><br>" +
                "<p style = 'font-size: 18'><br></p>Select the statistics you wish to add to the report via the left" +
                " panel. Once you've made your selection, click the 'Generate Report' button to see a report preview. You " +
                "can expand/contract the report window by dragging the divider in the middle left or right. " +
                "Hover over each option in the customisation menu to view more information. The data file can be changed " +
                "via the 'Change Data File' button.</body><html>");
        descText.setEditable(false);
        // reduce width of text pane
        descText.setMaximumSize(new Dimension(1000, 200));
        descText.setBackground(northPanel.getBackground());
        northPanel.add(descText);
        northPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        // Add buttons to generate report and save report
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton openFileButton = new JButton("Change Data File");
        JButton generateReportButton = new JButton("Generate Report");
        JButton saveReportButton = new JButton("Save");
        JPanel rightPanel = new JPanel();
        generateReportButton.addActionListener(new ButtonHandle(rightPanel, this));
        saveReportButton.addActionListener(new ButtonHandle(rightPanel, this));
        openFileButton.addActionListener(new ButtonHandle(this));
        buttonPanel.add(generateReportButton);
        buttonPanel.add(saveReportButton);
        buttonPanel.add(openFileButton);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        northPanel.add(buttonPanel);
        add(northPanel, BorderLayout.NORTH);

        // Left panel - Customization options
        leftPanel = new JPanel();
        leftPanel.setLayout(new WrapLayout());
        JScrollPane leftScrollPane = new JScrollPane(leftPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftScrollPane.setPreferredSize(new Dimension(700, 400));
        leftScrollPane.setMinimumSize(new Dimension(600, 400));

        // Right panel - Report
        rightPanel.setLayout(new WrapLayout());
        // Add title border
        rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Report Preview"));
        // Change border title font size
        ((TitledBorder) rightPanel.getBorder()).setTitleFont(new Font("Arial Nova Light", Font.BOLD, 20));
        // Make title border text centered
        ((TitledBorder) rightPanel.getBorder()).setTitleJustification(TitledBorder.CENTER);
        JScrollPane rightScrollPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setPreferredSize(new Dimension(700, 400));
        rightScrollPane.setMinimumSize(new Dimension(400, 400));

        // Split pane - Left and right panels
        centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
        centerPanel.setDividerLocation(0.5);
        centerPanel.setResizeWeight(0.5);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Adds the stats panels to the left panel
     * @param readData the data to be used in the stats panels
     */
    public void addStatsPanels(ReadData readData) {
        this.readData = readData;
        this.repaint();
        this.revalidate();

        moduleList = Arrays.copyOfRange(readData.getKeyArray(), 2, readData.getKeyArray().length);
        Set<String> courses = new HashSet<>(Arrays.asList(readData.getResultsMap().get("Course")));
        courseList = courses.toArray(new String[0]);

        String toolTipText = "<html><body style = 'font-family: Arial Nova Light; text-align: center; width: 150px;'>" +
                "Shows the performance of each student in a module compared to the average performance of all students in that module." +
                " The average performance is calculated by taking the average of all the students' grades in that module. " +
                "There is an optional parameter to provide a student ID, this student will be highlighted on the graphs</body></html>";
        JPanel performanceVsAverage = new StatsPanel(this, "Student Performance vs Average", new String[]{"Scatter-plot", "Histogram"},
                false, true, toolTipText);
        toolTipText = "<html><body style = 'font-family: Arial Nova Light; text-align: center; width: 150px;'>" +
                "Shows the difficulty of each module by comparing a student's performance in that module to their overall performance in all modules." +
                " A rating is assigned based on how much better/worse the student performed in that module compared to their overall performance. " +
                "Difficulty is determined by the rating that occurs most frequently.</body></html>";
        JPanel moduleDifficulty = new StatsPanel(this, "Module Difficulty", new String[]{"Table", "Pie Chart"},
                false, false, toolTipText);
        toolTipText = "<html><body style = 'font-family: Arial Nova Light; text-align: center; width: 150px;'>" +
                "Shows grade distribution for a module. The grade distribution is calculated by counting the number of " +
                "students who received each grade in a module. The <b>grouped</b> parameter will group the modules " +
                "together and provide a single graph based on overall performance in the combined modules.</body></html>";
        JPanel modulePerformance = new StatsPanel(this, "Module Performance Distribution", new String[]{"Bar Chart", "Pie Chart", "Histogram"},
                true, false, toolTipText);
        toolTipText = "<html><body style = 'font-family: Arial Nova Light; text-align: center; width: 150px;'>" +
                "Shows the 3 top, middle and bottom performing students in a module. The <b>grouped</b> parameter will " +
                "group all the selected modules together and provide the the top, middle and bottom students based on their" +
                "grade average across those modules.</body></html>";
        JPanel studentPerformance = new StatsPanel(this, "Categorised Student Performance", new String[]{"Table", "Bar Chart"},
                true, false, toolTipText);
        toolTipText = "<html><body style = 'font-family: Arial Nova Light; text-align: center; width: 150px;'>" +
                "Shows the students in the courses selected who achieved a grade within the specified range. " +
                "The range is defined by the min and max grade values entered in the input fields. </body></html>";
        JPanel gradeRange = new StatsPanel(this, "Students by Grade Range", new String[]{"Table"},
                false, false, toolTipText);
        toolTipText = "<html><body style = 'font-family: Arial Nova Light; text-align: center; width: 150px;'>" +
                "Shows students performance in modules, categorised by the course they are enrolled in.</body></html>";
        JPanel courseComparison = new StatsPanel(this, "Course Comparison", new String[]{"Scatter-plot", "Histogram", "Bar Chart"}, false, false, toolTipText);

        // Adding stats panels to left panel
        leftPanel.add(moduleDifficulty);
        leftPanel.add(modulePerformance);
        leftPanel.add(studentPerformance);
        leftPanel.add(performanceVsAverage);
        leftPanel.add(gradeRange);
        leftPanel.add(courseComparison);
        // revalidate and repaint to update the scroll pane
        this.revalidate();
        this.repaint();
    }

    /**
     * Method to get the readData object
     * @return the readData object
     */
    public ReadData getReadData() {
        return readData;
    }

    /**
     * Method to get the stats Panels Map
     * @return the stat descriptions Map
     */
    public Map<String, StatsPanel> getStatsPanels() {
        return statsPanels;
    }

    /**
     * Method to add a stat panel to the statsPanels Map
     * @param title the title of the stat (to be used as a key)
     * @param statsPanel the stats panel (to be used as a value)
     */
    public void addStatsPanel(String title, StatsPanel statsPanel) {
        statsPanels.put(title, statsPanel);
    }

    /**
     * Method to get the course list
     * @return the course list
     */
    public String[] getCourseList() {
        return courseList;
    }

    /**
     * Method to get the module list
     * @return the module list
     */
    public String[] getModuleList() {
        return moduleList;
    }

    /**
     * Method to clear the stat descriptions Map (used when a new report is selected)
     */
    public void clearStatDescriptions() {
        statDescriptions.clear();
    }

    /**
     * Method to add a description to the stat descriptions Map
     * @param title the title of the stat (to be used as a key)
     * @param description the description of the stat (to be used as a value)
     */
    public void addStatDescription(String title, String description) {
        statDescriptions.put(title, description);
    }

    /**
     * Method to get the description of a stat
     * @param title the title of the stat
     * @return the description of the stat
     */
    public static String getStatDescriptions(String title) {
        return statDescriptions.get(title);
    }

}

/**
 * Class to handle the button clicks on the main GUI
 */
class ButtonHandle implements ActionListener {

    JPanel reportContent;
    MainGUI mainGUI;
    ReadData readData;

    public ButtonHandle(JPanel reportContent, MainGUI mainGUI) {
        this.reportContent = reportContent;
        this.mainGUI = mainGUI;
    }

    public ButtonHandle(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Save")) { // Check if the "Save" command was invoked
            // Pop-up asks for the file name, with confirm and cancel buttons. Appends .pdf to the end of the file name
            String fileName = JOptionPane.showInputDialog(null, "Enter file name", "Save Report", JOptionPane.PLAIN_MESSAGE);
            while (Objects.equals(fileName, "")) {
                // Tell the user file name cannot be empty
                JOptionPane.showMessageDialog(null, "File name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                fileName = JOptionPane.showInputDialog(null, "Enter file name", "Save Report", JOptionPane.PLAIN_MESSAGE);
            }
            // Append .pdf to the end of the file name if it doesn't already have it
            if (!fileName.endsWith(".pdf")) {
                fileName = fileName + ".pdf";
            }
            Component[] components = reportContent.getComponents();
            // Reset the divider location, slightly off center to avoid smaller panels wrapping
            mainGUI.centerPanel.setDividerLocation(0.55);
            String finalFileName = fileName;
            // Delay allowing for resize shuffling to finish
            Timer timer = new Timer(300, e1 -> saveToPDF(components, finalFileName));
            timer.setRepeats(false);
            timer.start();
        }
        if (command.equals("Generate Report")) {
            readData = mainGUI.getReadData();
            generateReport();
        }
        if (command.equals("Change Data File")) {
            try {
                ReadData newReadData = new ReadData();
                if (newReadData.isReadSuccess()) {
                    mainGUI.addStatsPanels(newReadData);
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Generates the report based on the selected options
     */
    void generateReport() {
        reportContent.removeAll();
        mainGUI.clearStatDescriptions();

        for (Map.Entry<String, StatsPanel> entry : mainGUI.getStatsPanels().entrySet()) {
            String key = entry.getKey();
            StatsPanel statsPanel = entry.getValue();
            if (statsPanel == null) {
                continue;
            }

            String graphType = (String) statsPanel.graphType.getSelectedItem();
            ArrayList<String> selectedModules = statsPanel.getSelection();
            if (selectedModules.size() == 0) { // Check if any modules are selected
                continue;
            }

            if (key.equals("Student Performance vs Average")) {
                boolean studentIDSelected = statsPanel.studentIDCheckBox.isSelected();
                String studentID = statsPanel.studentIDTextField.getText();
                // Check student ID is valid and can be found in the results map.
                if (studentIDSelected) {
                    try {
                        Integer.parseInt(studentID);
                        boolean studentIDExists = false;
                        Map<String, String[]> results = readData.getResultsMap();
                        String[] ID = results.get("Student RegNo");
                        for (String id : ID) {
                            if (id != null && id.equals(studentID)) {
                                studentIDExists = true;
                                mainGUI.addStatDescription("Student Performance vs Average", (graphType.equals("Scatter-plot")
                                        ? "The scatter-plot below shows the student's (" +id+ ") performance in the specific module chosen against their overall grade in the course. The x-axis shows the overall grade, while the y-axis shows the module grade. " +
                                        "The labels in the top-left corner of each chart show the number of students above, and below the line of best fit."
                                        : "The histogram below shows the student's (" +id+ ") performance in the specific module chosen against their overall grade in the course. The x-axis shows the overall grade, while the y-axis shows the module grade. "));
                                break;
                            }
                        }
                        if (!studentIDExists) {
                            JOptionPane.showMessageDialog(null, "Student ID does not exist.");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter an Integer for the student ID.");
                        return;
                    }
                } else {
                    mainGUI.addStatDescription("Student Performance vs Average", (graphType.equals("Scatter-plot")
                            ? "The scatter-plot below shows student performance in the specific module chosen against their overall grade in the course. " +
                            "The x-axis shows the overall grade, while the y-axis shows the module grade. The labels in the top-left corner of each chart show the" +
                            " number of students above, and below the line of best fit."
                            : "The histogram below shows student performance in the specific module chosen against their overall grade in the course." +
                            "The x-axis shows the overall grade, while the y-axis shows the module grade."));
                }
                PerformanceVsAverage performanceVsAverage = new PerformanceVsAverage();
                for (String selectedModule : selectedModules) {
                    JPanel panel = null;
                    if (graphType.equals("Scatter-plot")) {
                        panel = (studentIDSelected && !studentID.equals("Enter student ID"))
                                ? performanceVsAverage.createScatterPlot(selectedModule, studentID, readData)
                                : performanceVsAverage.createScatterPlot(selectedModule, readData);
                    } else if (graphType.equals("Histogram")) {
                        panel = (studentIDSelected && !studentID.equals("Enter student ID"))
                                ? performanceVsAverage.createHistogram(selectedModule, studentID, readData)
                                : performanceVsAverage.createHistogram(selectedModule, readData);
                    }
                    panel.setName("Student Performance vs Average");
                    reportContent.add(panel);
                }
            }
            if (key.equals("Course Comparison")) {
                CourseComparison courseComparison = new CourseComparison();
                JPanel panel = null;
                for (String selectedModule : selectedModules) {
                    switch (graphType) {
                        case "Scatter-plot" -> panel = courseComparison.createScatterPlot(selectedModule, readData);
                        case "Histogram" -> panel = courseComparison.createHistogram(selectedModule, readData);
                        case "Bar Chart" -> panel = courseComparison.createBarChart(selectedModule, readData);
                    }
                    panel.setName("Course Comparison");
                    reportContent.add(panel);
                }
                mainGUI.addStatDescription("Course Comparison",
                        (graphType.equals("Scatter-plot") ? "The scatter plots show how students performed in a given module " +
                                "vs their overall performance. Each data point represents one student, the colour indicates " +
                                "the course of a student as specified in the legend."
                        : (graphType.equals("Histogram") ? "The histograms show how students performed in a given module vs " +
                                "their overall performance. The x-axis shows the student average grade, the y-axis shows " +
                                "the grade a student achieved. The colour of the bars indicates the course of a student as " +
                                "specified in the legend."
                        : "The bar charts show the breakdown of awarded grades in a given module. Each bar represents a course. " +
                                "The bars are split into segments, each showing the percentage of students from that course who " +
                                "achieved the award denoted by the legend.")));
            }
            if (key.equals("Module Performance Distribution")) {
                boolean isGrouped = statsPanel.groupedCheckBox.isSelected();
                ModulePerformanceDistribution modulePerformanceDistribution = new ModulePerformanceDistribution();
                // Convert array list to array, for passing to the class.
                String[] selectedModulesArray = new String[selectedModules.size()];
                selectedModulesArray = selectedModules.toArray(selectedModulesArray);
                JPanel panel = null;
                // If the user wants to group modules, only one chart is returned.
                if (isGrouped) {
                    switch (graphType) {
                        case "Histogram" -> panel = modulePerformanceDistribution.createHistogram(selectedModulesArray, readData);
                        case "Bar Chart" -> panel = modulePerformanceDistribution.createBarChart(selectedModulesArray, readData);
                        case "Pie Chart" -> panel = modulePerformanceDistribution.createPieChart(selectedModulesArray, readData);
                    }
                    panel.setName("Module Performance Distribution");
                    mainGUI.addStatDescription("Module Performance Distribution",
                            (graphType.equals("Histogram") ? "The Histogram shows the grades of students who took " +
                                    "the collective modules which have been separated into a number of bins, these bins are " +
                                    "determined by the range of grades given, where the height of each bin dictates the frequency."
                            : (graphType.equals("Bar Chart") ? "The bar chart shows the number of students in the collective " +
                                    "modules that have achieved a specific grade bracket, including those who failed."
                            : "The Pie chart shows the ratio of students in the collective modules that have achieved a " +
                                    "specific grade bracket, including those who failed.")));
                    reportContent.add(panel);
                } else { // Generate a chart for each selected module.
                    for (String selectedModule : selectedModules) {
                        panel = switch (graphType) {
                            case "Histogram" -> modulePerformanceDistribution.createHistogram(new String[]{selectedModule}, readData);
                            case "Bar Chart" -> modulePerformanceDistribution.createBarChart(new String[]{selectedModule}, readData);
                            case "Pie Chart" -> modulePerformanceDistribution.createPieChart(new String[]{selectedModule}, readData);
                            default -> panel;
                        };
                        panel.setName("Module Performance Distribution");
                        reportContent.add(panel);
                    }
                    mainGUI.addStatDescription("Module Performance Distribution",
                            (graphType.equals("Histogram") ? "The Histogram graphs show the grades of students who took " +
                                    "the given module which have been separated into a number of bins, these bins are " +
                                    "determined by the range of grades given, where the height of each bin dictates the frequency."
                            : (graphType.equals("Bar Chart") ? "The bar charts show the number of students in a given module" +
                                    " that have achieved a specific grade bracket, including those who failed."
                            : "The Pie charts shows the ratio of students in a given module that have achieved a" +
                                    " specific grade bracket, including those who failed.")));
                }
            }
            if (key.equals("Categorised Student Performance")) {
                boolean isGrouped = statsPanel.groupedCheckBox.isSelected();
                StudentPerformance studentPerformance = new StudentPerformance();
                JPanel panel = null;
                if (isGrouped) {
                    // Covert the selected modules to an array
                    String[] selectedModulesArray = new String[selectedModules.size()];
                    selectedModulesArray = selectedModules.toArray(selectedModulesArray);
                    if (graphType.equals("Table")) {
                        panel = studentPerformance.createTable(selectedModulesArray, readData);
                    } else if (graphType.equals("Bar Chart")) {
                        panel = studentPerformance.createBarChart(selectedModulesArray, readData);
                    }
                    panel.setName("Categorised Student Performance");
                    mainGUI.addStatDescription("Categorised Student Performance", (graphType.equals("Table") ?
                            "The table shows the three top, average and bottom students in " +
                                    "the collective modules. The average grade is determined by the mean of the grades."
                            : "The barchart shows the three top, average and bottom students in the collective modules. " +
                                    "The average grade is determined by the mean of the grades. The legend highlights " +
                                    "the ID of the students represented by the graph."));
                    reportContent.add(panel);
                } else { // Generate a chart for each selected module.
                    for (String selectedModule : selectedModules) {
                        if (graphType.equals("Table")) {
                            panel = studentPerformance.createTable(new String[]{selectedModule}, readData);
                        } else if (graphType.equals("Bar Chart")) {
                            panel = studentPerformance.createBarChart(new String[]{selectedModule}, readData);
                        }
                        panel.setName("Categorised Student Performance");
                        reportContent.add(panel);
                    }
                    mainGUI.addStatDescription("Categorised Student Performance", (graphType.equals("Table") ?
                            "The tables show the three top, average and bottom students in " +
                                    "a given module. The average grade is determined by the mean of the grades."
                            : "The bar charts show the three top, average and bottom students in a given module. " +
                                    "The average grade is determined by the mean of the grades. The legend highlights " +
                                    "the ID of the students represented by the graph."));
                }
            }
            if (key.equals("Module Difficulty")) {
                JPanel panel = null;
                ModuleDifficulty moduleDifficulty = new ModuleDifficulty();
                for (String selectedModule : selectedModules) {
                    if (graphType.equals("Table")) {
                        panel = moduleDifficulty.createTable(new String[]{selectedModule}, readData);
                    } else if (graphType.equals("Pie Chart")) {
                        panel = moduleDifficulty.createPieChart(new String[]{selectedModule}, readData);
                    }
                    panel.setName("Module Difficulty");
                    reportContent.add(panel);
                }
                mainGUI.addStatDescription("Module Difficulty", (graphType.equals("Table") ?
                        "The table below shows how students scored in comparison to their average grade. The " +
                        "module difficulty is determined based on the percentage of students who scored " +
                        "10% above or below their average mark. The module is considered easy if the majority " +
                        "of the students scored 10% above their average grade. A majority scoring 10% below " +
                        "their average would denote a hard module. If majority of the students scored 10% in " +
                        "range of their average grade, the module is then considered as average."
                        : "The pie chart shows the percentage of students who passed vs failed the module. The passing " +
                        "rate is deemed as 40% and above. The table below shows how students scored in comparison to " +
                        "their average grade. The module difficulty is determined based on the percentage of students " +
                        "who scored 10% above or below their average mark. The module is considered easy if the majority " +
                        "of the students scored 10% above their average grade. A majority scoring 10% below their average " +
                        "would denote a hard module. If majority of the students scored 10% in range of their average grade, " +
                        "the module is then considered as average."));
            }
            if (key.equals("Students by Grade Range")) {
                int lowerBoundary;
                int upperBoundary;
                // Check integers have been entered.
                try {
                    lowerBoundary = Integer.parseInt(statsPanel.minGradeTextField.getText());
                    upperBoundary = Integer.parseInt(statsPanel.maxGradeTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Only whole numbers will, be accepted.");
                    return;
                }
                // Check the lower boundary is not greater than the upper boundary
                if (lowerBoundary > upperBoundary) {
                    JOptionPane.showMessageDialog(null, "The lower boundary must be less than the upper boundary.");
                    return;
                }
                JPanel panel = null;
                GradeBoundaries gradeBoundaries = new GradeBoundaries();
                String[] courseNames = new String[selectedModules.size()];
                courseNames = selectedModules.toArray(courseNames);
                if (graphType.equals("Table")) {
                    panel = gradeBoundaries.createTable(courseNames, lowerBoundary, upperBoundary, readData);
                }
                panel.setName("Students by Grade Range");
                mainGUI.addStatDescription("Students by Grade Range", "The table lists students data for " +
                        "all students on the specified courses who achieved an overall grade between " + lowerBoundary +
                        " and " + upperBoundary + " inclusive. This data includes a students ID, course, individual " +
                        "module grades, and their overall grade. The data is in descending order, with the top performing " +
                        "students listed first.");
                reportContent.add(panel);
            }
        }
        // Update methods.
        reportContent.revalidate();
        reportContent.repaint();
    }

    /**
     * Saves the report content to a PDF file
     * @param components The components to save to the PDF file
     */
    private void saveToPDF(Component[] components, String fileName) {
        // Create a new directory to store the PNG files
        File pngDir = new File("PNGs");
        if (!pngDir.exists()) {
            pngDir.mkdir();
        }
        // Map holds the statistic headers and their corresponding PNG files
        Map<String, ArrayList<String>> panelMap = new HashMap<>();
        // Check if there is at least one component
        if (components.length > 0) {
            // Scroll to the top of the report content
            reportContent.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
            JPanel firstPanel = (JPanel) components[0]; // Get the first JPanel
            Rectangle firstPanelRect = SwingUtilities.convertRectangle(
                    firstPanel.getParent(), firstPanel.getBounds(), reportContent); // Get the bounds of the first panel relative to the report content

            final int[] currentPanelIndex = {0}; // Initialize the index of the current panel to 0
            final int[] yDiff = {0}; // Initialize the vertical distance between panels to 0
            final int[] pngNum = {0}; // Initialize the number of PNG files saved to 0

            // Create a timer that runs every 120 milliseconds
            Timer timer = new Timer(120, e -> {
                // Check if all panels have been saved
                if (currentPanelIndex[0] >= components.length) {
                    ((Timer) e.getSource()).stop(); // Stop the timer if all panels have been saved

                    PDFGenerator pdfGenerator = new PDFGenerator();
                    pdfGenerator.createPDF(fileName);
                    try {
                        PDFGenerator.addToPDF(panelMap);
                    } catch (DocumentException ex) {
                        ex.printStackTrace();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return;
                }
                JPanel panel = (JPanel) components[currentPanelIndex[0]]; // Get the current JPanel
                pngNum[0]++; // Increment the PNG file number
                String panelName = panel.getName(); // Get the name of the current JPanel
                // Check if panel name exists in the map (as a key)
                if (panelMap.containsKey(panelName)) {
                    // Add the PNG file name to the list of PNG files for the current panel name
                    panelMap.get(panelName).add("PNGs/" + pngNum[0] + ".png");
                } else {
                    // Create a new list of PNG files for the current panel name
                    ArrayList<String> pngFiles = new ArrayList<>();
                    pngFiles.add("PNGs/" + pngNum[0] + ".png");
                    // Add the panel name and the list of PNG files to the map
                    panelMap.put(panelName, pngFiles);
                }
                // Check if panel contains a chart
                if (panel.getComponentCount() > 0 &&
                        panel.getComponentCount() <= 1 && panel.getComponent(0) instanceof ChartPanel) {
                    scrollToChart(panel, currentPanelIndex, yDiff, firstPanelRect, pngNum, components);
                } else {
                    String pngFileName = "PNGs/" + pngNum[0] + ".png"; // Create a filename for the PNG file
                    File pngFile = new File(pngFileName); // Create a new File object for the PNG file
                    // Check if panel is Students by Grade Range as it contains multiple child panels due to the size of the table
                    if (panel.getName().equals("Students by Grade Range")) {
                        // Save each child component of the panel to a PNG file
                        for (Component component : panel.getComponents()) {
                            JPanel childPanel = (JPanel) component;
                            GrabChart.capturePanel(childPanel, pngFile);
                            panelMap.get(panelName).add("PNGs/" + pngNum[0] + ".png");
                            pngNum[0]++;
                            pngFile = new File("PNGs/" + pngNum[0] + ".png");
                        }
                    } else {
                        // Save the panel to a PNG file
                        GrabChart.capturePanel(panel, pngFile); // Save the panel to a PNG file
                    }
                }
                currentPanelIndex[0]++; // Increment the index of the current panel
            });
            timer.start(); // Start the timer
        }
    }

    /**
     * Scrolls to the chart panel and saves the chart to a PNG file.
     * @param panel The chart panel.
     * @param currentPanelIndex The index of the current panel.
     * @param yDiff The vertical distance between panels.
     * @param firstPanelRect The bounds of the first panel relative to the report content.
     * @param pngNum The number of PNG files saved.
     * @param components The components in the report content.
     */
    private void scrollToChart(JPanel panel, int[] currentPanelIndex, int[] yDiff, Rectangle firstPanelRect, int[] pngNum, Component[] components) {
        Rectangle panelRect = SwingUtilities.convertRectangle(
                panel.getParent(), panel.getBounds(), reportContent); // Get the bounds of the current panel relative to the report content

        // If this is the first panel, calculate the vertical distance between panels
        if (currentPanelIndex[0] == 1) {
            yDiff[0] = firstPanelRect.y - panelRect.y;
        }

        Rectangle visibleRect = reportContent.getVisibleRect(); // Get the visible rectangle of the report content
        visibleRect.translate(0, -yDiff[0]); // Translate the visible rectangle to the correct vertical position
        reportContent.scrollRectToVisible(visibleRect); // Scroll to the correct position

        String pngFileName = "PNGs/" + pngNum[0] + ".png"; // Create a filename for the PNG file
        File pngFile = new File(pngFileName); // Create a new File object for the PNG file
        // check if we're on the last panel
        if (currentPanelIndex[0] == components.length - 1) {
            //Scroll to the bottom of the report content
            reportContent.scrollRectToVisible(new Rectangle(0, reportContent.getHeight() - 1, 1, 1));
        }
        // Minute delay allowing for scrolling
        Timer timer = new Timer(0, e -> {
            GrabChart.captureChart(panel, pngFile); // Save the current panel as a PNG file
            ((Timer) e.getSource()).stop(); // Stop the timer
        });
        timer.start(); // Start the timer
    }
}

