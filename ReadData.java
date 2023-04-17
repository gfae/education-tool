import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;


/**
 * This class is used to read the data from the CSV file and store it in a map.
 * The map is then used to get the data for the different modules.
 */
public class ReadData {
    private  Map<String, String[]> resultsMap;
    private  String[] keyArray; // Holds keys
    private boolean readSuccess; // True if the file was read successfully, false otherwise.
    private File file;

    ReadData(File file) {
        this.file = file;
        populateMap();
    };

    ReadData() throws FileNotFoundException {
        // Open file explorer and select the file
        JFileChooser fileChooser = new JFileChooser();
        // Get current directory and set it as the default directory
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setDialogTitle("Select the data file");
        // Only allow the user to select one file
        fileChooser.setMultiSelectionEnabled(false);
        // Only allow .csv files to be selected
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.showOpenDialog(null);
        this.file = fileChooser.getSelectedFile();
        //close the file chooser
        fileChooser.setVisible(false);
        populateMap();
    }

    private void populateMap() {
        resultsMap = new HashMap<>();
        try {
            Scanner scan = new Scanner(file);
            scan.useDelimiter(",|\n");
            keyArray = new String[17]; // Holds keys
            int column = 0;
            int row = 0;
            // Read the file
            while (scan.hasNext()) {
                String data = scan.next();
                if (data.contains("\r")) {
                    data = data.replace("\r", "");
                }
                if (row == 0) {
                    resultsMap.put(data, new String[127]);
                    keyArray[column] = data;
                } else {
                    if (column == 1) {
                        // Remove any text within parentheses.
                        if (data.contains("(")) {
                            data = data.substring(0, data.indexOf("(")-1);
                        }
                    }
                    resultsMap.get(keyArray[column])[row - 1] = data;
                }
                if (column == 16) {
                    column = 0;
                    row++;
                } else {
                    column++;
                }
            }
            scan.close();
            // Removes the key, and it's value from the map if all values are empty.
            // This will avoid exceptions being thrown when trying to turn empty data into a graph.
            for (String key : keyArray) {
                boolean empty = true;
                for (String value : resultsMap.get(key)) {
                    if (value != null && !value.isEmpty() && !value.isBlank()) {
                        empty = false;
                        break;
                    }
                }
                if (empty) {
                    resultsMap.remove(key);
                    keyArray = Arrays.stream(keyArray).filter(s -> !s.equals(key)).toArray(String[]::new);
                }
            }
            readSuccess = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading file. Please try again.");
            readSuccess = false;
        }
    }

    /**
     * This method is used to check if the file was read successfully.
     * @return readSuccess - True if the file was read successfully, false otherwise.
     */
    public boolean isReadSuccess() {
        return readSuccess;
    }

    /**
     * This method is used to get the data for the different modules.
     * @return resultsMap - The map that holds the data for the different modules.
     */
    public Map<String, String[]> getResultsMap() {
        return resultsMap;
    }

    /**
     * This method is used to get the keys for the data map
     * @return keyArray - The array that holds the keys for the map
     */
    public String[] getKeyArray() {
        return keyArray;
    }

    /**
     * This method is used to get the module names from the key array.
     * @return modules - The array that holds the module names.
     */
    public String[] getCombinedModuleNames() {
        Set<String> moduleNames = new HashSet<>();
        for (int i = 0; i < keyArray.length-2; i++) {
            // Get the module name
            String module = keyArray[i+2];
            // Extract the first 5 characters
            String moduleCode = module.substring(0, 5);
            moduleNames.add(moduleCode);
        }
        // Convert the set to an array
        return moduleNames.toArray(new String[0]);
    }
}
