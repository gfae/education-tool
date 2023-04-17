import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PDFGeneratorTests {

    static ReadData data;
    static PDFGenerator pdfGenerator;
    static ModuleDifficulty moduleDifficulty;

    @BeforeAll
    // Create a new ReadData object
    public static void beforeAll() throws Exception {
        System.out.println("Creating the test class");
        File file = new File("../DataFileFor2022-23-problem statement.csv");
        data = new ReadData(file);
    }

    @BeforeEach
    // Create a new pdfGenerator and gradeBoundaries object
    public void beforeEach() {
        System.out.println("Creating the test object");
        pdfGenerator = new PDFGenerator();
        moduleDifficulty = new ModuleDifficulty();
    }

    @AfterEach
    // Reset the pdfGenerator and gradeBoundaries objects
    public void afterEach() {
        System.out.println("Resetting the test object");
        pdfGenerator.closeDocument();
        File pdf = new File("test.pdf");
        try {
            Files.deleteIfExists(pdf.toPath());
        } catch (Exception e) {
            System.out.println("Error deleting file");
        }
        pdfGenerator = null;
        moduleDifficulty = null;

    }

    @Test
    // Test that the createPDF method works
    public void createPDFTest() {
        System.out.println("Testing the createPDF method");

        pdfGenerator.createPDF("test.pdf");
        File pdf = new File("test.pdf");
        pdfGenerator.closeDocument();

        assertEquals(true, pdf.exists());
    }

    @Test
    public void addTitleTest() throws IOException {
        System.out.println("Testing the addTitle method");
        pdfGenerator.createPDF("test.pdf");

        pdfGenerator.addTitle("Test Title");

        assertEquals("Test Title", pdfGenerator.titleTest);
    }
}