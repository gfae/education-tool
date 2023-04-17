import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * StatsPanel class
 * Creates a panel for each type of statistic
 * Contains checkboxes to select which stats to include
 * Contains dropdown to select graph type
 * Contains checkboxes to select which modules/courses to include
 * Contains text fields to enter student ID and min/max grade
 */
class StatsPanel extends JPanel {

    private final MainGUI mainGUI;
    JCheckBox titleCheckBox; // Checkbox to indicate whether to include panel stats
    JCheckBox groupedCheckBox; // Checkbox to indicate whether to group stats by module
    JComboBox<String> graphType; // Dropdown to select graph type
    Box selectionBox; // Box to hold selection checkboxes (either modules or courses)
    JCheckBox studentIDCheckBox; // Checkbox to indicate whether to include student ID
    JTextField studentIDTextField; // Text field to enter student ID
    JTextField minGradeTextField; // Text field to enter min grade
    JTextField maxGradeTextField; // Text field to enter max grade

    StatsPanel(MainGUI mainGUI, String title, String[] graphTypes, boolean groupModules, boolean studentIDRequired, String toolTip) {
        this.mainGUI = mainGUI;
        setName(title);
        GridBagConstraints c = new GridBagConstraints();
        setBorder(new LineBorder(new Color(40, 40, 40)));
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(302, 400));

        // Create the title label, checkbox and help icon
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial Nova Light", Font.BOLD, 15));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setSize(200, 50);
        titleCheckBox = new JCheckBox();
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(titleCheckBox, BorderLayout.EAST);
        titlePanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        titlePanel.setPreferredSize(new Dimension(300, 30));
        // Add help icon
        JLabel helpIcon = new JLabel("?");
        helpIcon.setFont(new Font("Arial Nova Light", Font.BOLD, 15));
        helpIcon.setForeground(new Color(94, 94, 94));
        helpIcon.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(94, 94, 94)), BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        helpIcon.setToolTipText(toolTip);
        // set tooltip to not disappear after 5 seconds
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        titlePanel.add(helpIcon, BorderLayout.WEST);

        // Add the title label and checkbox to the top row
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        add(titlePanel, c);

        // Create glass pane
        JPanel glassPane = new JPanel();
        mainGUI.setGlassPane(glassPane);
        glassPane.setOpaque(true);
        glassPane.setBackground(new Color(0, 0, 0, 120));
        glassPane.setVisible(true);

        // Consumes all mouse events when checkbox is not selected
        glassPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!titleCheckBox.isSelected()) {
                    e.consume();
                }
            }
        });

        // Enables/disables options based on checkbox.
        // Also adds/removes value from statsPanels map. Title is used as key.
        titleCheckBox.addActionListener(e -> {
            if (!titleCheckBox.isSelected()) {
                mainGUI.addStatsPanel(title, null);
                glassPane.setVisible(true);

            } else {
                mainGUI.addStatsPanel(title, this);
                glassPane.setVisible(false);

            }
        });

        // Set the glassPane as the glassPane of the StatsPanel and add it to the StatsPanel
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 5;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        add(glassPane, c);

        // Create the dropdown list and add it to the panel
        graphType = new JComboBox<>(graphTypes);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2; // Span both columns
        c.insets = new Insets(10, 5, 0, 5); // Add some vertical space between the title and the combo box
        c.anchor = GridBagConstraints.WEST; // Align the combo box to the left
        c.fill = GridBagConstraints.HORIZONTAL; // Make the combo box fill the entire row
        add(graphType, c);

        // Create the scroll pane containing the list of modules
        selectionBox = (title.equals("Students by Grade Range")) ? courseSelection() : moduleSelection();
        JScrollPane scrollPane = new JScrollPane(selectionBox);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        // remove horizontal scrollbar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2; // Span both columns
        c.fill = GridBagConstraints.BOTH; // Make the scroll pane fill the available space
        c.weighty = 1.0; // Give the scroll pane the most weight in the vertical direction
        add(scrollPane, c);

        // Create the "Select All" and "Deselect" buttons in a panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton selectAllButton = new JButton("Select All");
        // Select all modules when the "Select All" button is clicked
        // Works by getting the box component of the scroll pane and iterating through its children (the checkboxes)
        selectAllButton.addActionListener(e -> {
            for (Component component : selectionBox.getComponents()) {
                if (component instanceof JCheckBox) {
                    ((JCheckBox) component).setSelected(true);
                }
            }
        });
        JButton deselectButton = new JButton("Deselect All");
        // Deselect all modules when the "Deselect All" button is clicked
        deselectButton.addActionListener(e -> {
            for (Component component : selectionBox.getComponents()) {
                if (component instanceof JCheckBox) {
                    ((JCheckBox) component).setSelected(false);
                }
            }
        });

        // Add the buttons to the panel
        buttonPanel.add(selectAllButton);
        buttonPanel.add(deselectButton);

        // Add the button panel to the bottom row
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2; // Span both columns
        c.fill = GridBagConstraints.HORIZONTAL; // Make the panel fill the entire row
        c.anchor = GridBagConstraints.CENTER; // Align the panel in the center
        add(buttonPanel, c);

        int gridy = 3;
        if (groupModules) {
            // Create the "Grouped" label and checkbox in a panel
            JPanel groupedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel groupedLabel = new JLabel("Grouped?");
            groupedCheckBox = new JCheckBox();

            // Add the label and checkbox to the panel
            groupedPanel.add(groupedLabel);
            groupedPanel.add(groupedCheckBox);

            // Add the panel to the bottom row
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = gridy + 1;
            c.gridwidth = 2; // Span both columns
            c.fill = GridBagConstraints.HORIZONTAL; // Make the panel fill the entire row
            c.anchor = GridBagConstraints.CENTER; // Align the panel in the center
            add(groupedPanel, c);

            gridy++;
        }

        // Create the "Student ID" label, textbox and checkbox in a panel
        if (studentIDRequired) {
            JPanel studentIDPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            studentIDPanel.setPreferredSize(new Dimension(100, 60));
            JLabel studentIDLabel = new JLabel("Highlight specific student ID? ");
            studentIDCheckBox = new JCheckBox();
            studentIDTextField = new JTextField("Enter student ID", 10);
            studentIDTextField.setEnabled(false);
            // Removes placeholder text when the user clicks on the text field
            studentIDTextField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (studentIDCheckBox.isSelected() && studentIDTextField.getText().equals("Enter student ID")) {
                        studentIDTextField.setText("");
                    }
                }
            });
            // Enables/disables the text field based on the checkbox
            studentIDCheckBox.addItemListener(e -> studentIDTextField.setEnabled(studentIDCheckBox.isSelected()));

            // Add the label, textbox and checkbox to the panel
            studentIDPanel.add(studentIDLabel);
            studentIDPanel.add(studentIDCheckBox);
            studentIDPanel.add(studentIDTextField);

            // Add the panel to the bottom row
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = gridy + 1;
            c.gridwidth = 2; // Span both columns
            c.fill = GridBagConstraints.HORIZONTAL; // Make the panel fill the entire row
            c.anchor = GridBagConstraints.CENTER; // Align the panel in the center
            add(studentIDPanel, c);
        }

        if (title.equals("Students by Grade Range")) {
            // Add input fields to for min and max grade
            JPanel gradeRangePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel minGradeLabel = new JLabel("Min Grade: ");
            minGradeTextField = new JTextField("0", 3);
            JLabel maxGradeLabel = new JLabel("Max Grade: ");
            maxGradeTextField = new JTextField("100", 3);
            gradeRangePanel.add(minGradeLabel);
            gradeRangePanel.add(minGradeTextField);
            gradeRangePanel.add(maxGradeLabel);
            gradeRangePanel.add(maxGradeTextField);
            c.gridx = 0;
            c.gridy = 5;
            c.gridwidth = 2; // Span both columns
            c.fill = GridBagConstraints.HORIZONTAL; // Make the panel fill the entire row
            c.anchor = GridBagConstraints.CENTER; // Align the panel in the center
            add(gradeRangePanel, c);
        }
    }

    /**
     * Method to return an array of the selected items (either modules, or courses)
     *
     * @return array of the selected items
     */
    public ArrayList<String> getSelection() {
        // Create a list to store the selected modules
        ArrayList<String> selected = new ArrayList<>();
        // Iterate through the box component of the scroll pane
        for (Component component : selectionBox.getComponents()) {
            // If the component is a checkbox and is selected, add it to the list
            if (component instanceof JCheckBox && ((JCheckBox) component).isSelected()) {
                selected.add(((JCheckBox) component).getText());
            }
        }
        // Convert the list to an array and return it
        return selected;
    }

    /**
     * Method to create the checkboxes for each module
     *
     * @return a box containing the checkboxes
     */
    private Box moduleSelection() {
        Box moduleSelection = Box.createVerticalBox();
        //add black border to the box
        moduleSelection.setBorder(new LineBorder(Color.BLACK));
        String[] moduleList = mainGUI.getModuleList();
        // Creating the checkboxes for each module, checking if it's the last module in the list
        // to add a larger bottom border
        for (int i = 0; i < moduleList.length; i++) {
            JCheckBox listItem = new JCheckBox(moduleList[i]);
            if (i == moduleList.length - 1) {
                listItem.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 100));
            } else {
                listItem.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
                        BorderFactory.createEmptyBorder(5, 2, 5, 120)));
            }
            listItem.setBorderPainted(true);
            listItem.setBackground(new Color(250, 250, 250));
            listItem.setFont(new Font("Segoe UI Light", Font.BOLD, 15));
            moduleSelection.add(listItem);
        }
        return moduleSelection;
    }

    /**
     * Method to create the box containing the course checkboxes
     *
     * @return the box containing the course checkboxes
     */
    private Box courseSelection() {
        Box courseSelection = Box.createVerticalBox();
        //add black border to the box
        courseSelection.setBorder(new LineBorder(Color.BLACK));
        String[] courseList = mainGUI.getCourseList();
        // Creating the checkboxes for each module, checking if it's the last module in the list
        for (int i = 0; i < courseList.length; i++) {
            // Check the course isn't empty
            if (courseList[i] == null) {
                continue;
            }
            JCheckBox listItem = new JCheckBox(courseList[i]);
            if (i == courseList.length - 1) {
                listItem.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 120));
            } else {
                listItem.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
                        BorderFactory.createEmptyBorder(10, 2, 10, 120)));
            }
            listItem.setBorderPainted(true);
            listItem.setBackground(new Color(250, 250, 250));
            listItem.setFont(new Font("Segoe UI Light", Font.BOLD, 15));
            courseSelection.add(listItem);
        }
        return courseSelection;
    }

}
