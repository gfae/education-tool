import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadDataTests {

    static ReadData data;

    @BeforeAll
    public static void beforeAll() throws Exception {
        System.out.println("Setting up test class");
        File file = new File("../DataFileFor2022-23-problem statement.csv");
        data = new ReadData(file);
    }

    @Test
    public void fileRead() throws FileNotFoundException {
        System.out.println("Testing the file is read correctly");
        assertEquals(true, data.isReadSuccess());
    }

    @Test
    public void getResultsMap() {
        System.out.println("Testing the results map is populated correctly");
        Map<String, String[]> resultsMap = data.getResultsMap();
        int testValue = Integer.parseInt(resultsMap.get("Student RegNo")[2]);
        assertEquals(2500006, testValue);
    }

    @Test
    public void getKeyArray() {
        System.out.println("Testing the key array is populated correctly");
        String[] keyArray = data.getKeyArray();
        assertEquals("Student RegNo", keyArray[0]);
    }

    @Test
    public void getResultsMapSize() {
        System.out.println("Testing the results map is the correct size");
        Map<String, String[]> resultsMap = data.getResultsMap();
        assertEquals(16, resultsMap.size());
    }

    @Test
    public void correctModuleNames() {
        System.out.println("Testing the module names are formatted correct");
        String[] moduleNames = data.getCombinedModuleNames();
        assertEquals("CE161", moduleNames[1]);
    }
}
