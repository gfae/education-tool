import java.io.*;
import java.util.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.*;


//I will provide the jar file to be able to use the itext library. I think Itext might be a paid service, tho i do have a jar file that others are seeming to be allowed to use.
public class PDFGenerator {
    //Initializing two objects, one is the document, and one is the PDFWriter object.
    private static Document document;
    private static PdfWriter writer;
    static String titleTest; // This is a test variable for the title of the document.
    static Map<Integer, String> toc = new LinkedHashMap<>();
    static String fileName;


    //This is the constructor for the PDFGenerator class, It creates a new document.
    public PDFGenerator() {
        document = new Document();
    }


    //This is the function called createPDF, this is how we create a blank PDF document, Simply pass in a preffered file name into the function for the document to be created.
    //This will also call the add new page method so that the page number is tracked.
    public void createPDF(String fileName) {
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            this.fileName = fileName;

            // add first page and set page number to 1
            addNewPage();
            pageNumber = 2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        pageNumber++;
    }


    //This is the function that allows for you to add a paragraph of text, this is also where you should be able to choose the font and style of the paragraph.
    //This is how we add paragraphs to the document.

    //Note: If you want to have a lines worth of space inbetween elements that are added to the document then we can simply add in an empty paragraph.
    public static void addParagraph(String text) {
        try {
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
            Paragraph paragraph = new Paragraph(text, font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingBefore(10);
            paragraph.setSpacingAfter(10);
            document.add(paragraph);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addToc() throws DocumentException {
        Paragraph dummyParagraph = new Paragraph(" ");
        dummyParagraph.setSpacingAfter(300);
        document.add(dummyParagraph);
        Paragraph titleParagraph = new Paragraph();
        Chunk underlinedTitle = new Chunk("Overview of educational statistics");
        underlinedTitle.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD, 30));
        underlinedTitle.setUnderline(0.1f, -2f);
        titleParagraph.add(underlinedTitle);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(150);
        document.add(titleParagraph);
        addTitle("Table of Contents");
        try {
            String tocString = "";
            for (Map.Entry<Integer, String> entry : toc.entrySet()) {
                tocString += "P" + entry.getKey() + ".  " + entry.getValue() + "\n";
            }
            addParagraph(tocString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //This function is going to allow for the creation of a new page to the end of the document.
    //This is simply calling a method that is called newPage. Part of the Itext library.
    //Additionally this method is keeping track of the page number, we will need to manually add pages onto the document so that the page number is always tracked.
    private static int pageNumber = 2;

    public static void addNewPage() {
        try {
            // Create a new page
            document.newPage();
            // Add page number to the bottom-right corner
            PdfContentByte canvas = writer.getDirectContent();
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            canvas.beginText();
            canvas.setFontAndSize(baseFont, 10);
            canvas.showTextAligned(Element.ALIGN_RIGHT, "Page " + pageNumber, document.getPageSize().getWidth() - 36, 20, 0);
            canvas.endText();
            pageNumber++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //This function is going to allow the ability to add a title to a page. atm it does not specify what page that the title is added.
    public static void addTitle(String title) {
        try {
            Paragraph titleParagraph = new Paragraph();
            Chunk underlinedTitle = new Chunk(title);
            underlinedTitle.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD, 16));
            underlinedTitle.setUnderline(0.1f, -2f);
            titleParagraph.add(underlinedTitle);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            titleTest = title;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //This is how we will close the document once all of the graphs and information has been inputted into the PDF document.
    public static void closeDocument() {
        document.close();
    }

    //This is a basic way to add an image, i believe this is where the organisation of layout for the PDF will come most important due to sizing issues that may occur with the charts, and possibly other formatting issues.
    //This will mean that EVERY graph will likely need a unique jpanel name so that the robot is able to take all of the intended screenshots.
    //Along side this solution we will need the report window to automatically position the jpanels on screen when they are ready to be screenshotted.
    //Lastly, we will need a way to forcibly prevent the user interacting witrh the program while the report is generating, possibly with a loading bar at the topm
    public static void addImage(String imagePath) {
        try {
            PdfPTable table = new PdfPTable(new float[] {2, 2});
            table.setWidthPercentage(100);

            // Add the image to the left column
            Image image = Image.getInstance(imagePath);
            image.scalePercent(200);
            PdfPCell cell1 = new PdfPCell(image, true);
            cell1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell1);

            // Add a blank cell to the right column
            PdfPCell cell2 = new PdfPCell();
            cell2.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell2);

            // Add the table to the document
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTableImage(String imagePath) {
        try {
            PdfPTable table = new PdfPTable(new float[] {1});
            table.setWidthPercentage(100);

            // Add the image to the cell
            Image image = Image.getInstance(imagePath);
            image.setAlignment(Element.ALIGN_CENTER);
            PdfPCell cell1 = new PdfPCell(image, true);
            cell1.setBorder(PdfPCell.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            // Add the table to the document
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //This method is how we are going to add images side by side in the pdf document, this is important so that we can fit as much content as possible onto the pdf document.
    //You can change the size of the images by extending the width of the columns that the images are held inside.
    public static void addImagesSideBySide(String imagePath1, String imagePath2) {
        try {
            PdfPTable table = new PdfPTable(new float[] {2, 2});
            //This is where you can change the width of the colmuns as mentioned before.
            table.setWidthPercentage(100);

            //Image 1
            Image image1 = Image.getInstance(imagePath1);
            image1.scalePercent(200);
            PdfPCell cell1 = new PdfPCell(image1, true);
            cell1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell1);

            //Image 2
            Image image2 = Image.getInstance(imagePath2);
            image2.scalePercent(200);
            PdfPCell cell2 = new PdfPCell(image2, true);
            cell2.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell2);

            //Adding table.
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds graphs to a PDF document based on the data in the panelMap.
     * @param panelMap A map containing panel titles as keys and lists of PNG files as values.
     */
    public static void addToPDF(Map<String, ArrayList<String>> panelMap) throws DocumentException, IOException {
        //Get the key set and convert it to an array
        Set<String> keySet = panelMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //Iterate through each key in the key array
        for (int k = 0; k < keyArray.length; k++) {
            String key = keyArray[k];
            ArrayList<String> pngFiles = panelMap.get(key);
            int graphCount = 0;
            //Add graphs based on key type
            if (key.equals("Student Performance vs Average") || key.equals("Course Comparison") || key.equals("Module Performance Distribution")) {
                //If this isn't the first panel, add a new page
                if (k > 0) {
                    addNewPage(); //Adds a new page to the PDF document
                }
                //Add the panel title and description
                addTitle(key); //Adds the panel title to the PDF document
                addParagraph(MainGUI.getStatDescriptions(key)); // Adds a description paragraph to the PDF document
                toc.put(pageNumber-1, key); //Adds the panel title and page number to the table of contents
                //Iterate through each PNG file in the list
                for (int i = 0; i <= pngFiles.size() - 1; i += 2) {
                    try {
                        //Try to add two images side by side
                        String imgOne = pngFiles.get(i);
                        String imgTwo = pngFiles.get(i + 1);
                        addImagesSideBySide(imgOne, imgTwo);//Adds two images side by side to the PDF document
                        graphCount += 2;
                    } catch (Exception e) {
                        //If an exception is thrown, add a single image instead
                        addImage(pngFiles.get(i)); //Adds a single image to the PDF document
                        graphCount++;
                    }
                    //Check if the page is full and add a new one if necessary
                    if ((graphCount == 6 && i != pngFiles.size() - 2) || graphCount == 7) {
                        addNewPage(); //Adds a new page to the PDF document
                        addTitle(key); //Adds the panel title to the PDF document
                        addParagraph("Continued from previous page..."); //Adds a continuation paragraph to the PDF document
                        //Add the final graph if there is an odd number of graphs
                        if (i == pngFiles.size() - 3) {
                            addImage(pngFiles.get(i + 2));//Adds the final graph to the PDF document
                            graphCount++;
                            break;
                        }
                        graphCount = 0;
                    }
                }
            }
            if (key.equals("Module Difficulty")) {//If the key is "Module Difficulty", add graphs in a different layout
                if (k > 0) {//If this isn't the first panel, add a new page
                    addNewPage();//Adds a new page to the PDF document
                }
                //Add the panel title and description
                addTitle(key);//Adds the panel title to the PDF document
                addParagraph( MainGUI.getStatDescriptions(key)); //Adds a description paragraph to the PDF document
                toc.put(pageNumber-1, key);//Adds the panel title and page number to the table of contents
                // Check size of first graph to determine how many graphs to add per page
                Image image = null;
                try {
                    image = Image.getInstance(pngFiles.get(0));
                } catch (BadElementException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                float height = image.getHeight();
                int maxGraphs = (height > 150) ? 4 : 10;
                for (int i = 0; i < pngFiles.size() - 1; i += 2) {//Iterate through each PNG file in the list
                    try { //Try to add two images side by side
                        String imgOne = pngFiles.get(i);
                        String imgTwo = pngFiles.get(i + 1);
                        addImagesSideBySide(imgOne, imgTwo);
                        graphCount += 2;
                    } catch (Exception e) {
                        addImage(pngFiles.get(i));
                        graphCount++;
                    }
                    if (graphCount == maxGraphs) {// Check if the page is full and add a new one
                        addNewPage();
                        addTitle(key);
                        addParagraph("Continued from previous page...");
                        graphCount = 0;
                    }
                }
            }
            if (key.equals("Categorised Student Performance")){
                if (k > 0) {//If this isn't the first panel, add a new page
                    addNewPage();//Adds a new page to the PDF document
                }
                //Add the panel title and description
                addTitle(key);//Adds the panel title to the PDF document
                addParagraph( MainGUI.getStatDescriptions(key)); // Adds a description paragraph to the PDF document
                toc.put(pageNumber-1, key);//Adds the panel title and page number to the table of contents
                // Check size of first graph to determine how many graphs to add per page
                for(int i = 0; i < pngFiles.size(); i++){
                    try {
                        addTableImage(pngFiles.get(i));
                        graphCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (graphCount == 4) {// Check if the page is full and add a new one
                        addNewPage();
                        addTitle(key);
                        addParagraph("Continued from previous page...");
                        graphCount = 0;
                    }
                }
            }
            if (key.equals("Students by Grade Range")){
                if (k > 0) {//If this isn't the first panel, add a new page
                    addNewPage();//Adds a new page to the PDF document
                }
                //Add the panel title and description
                addTitle(key);//Adds the panel title to the PDF document
                addParagraph( MainGUI.getStatDescriptions(key)); // Adds a description paragraph to the PDF document
                toc.put(pageNumber-1, key);
                // Check size of first graph to determine how many graphs to add per page
                for(int i = 0; i < pngFiles.size()-1; i++){
                    try {
                        addTableImage(pngFiles.get(i));
                        graphCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (graphCount == 1 && i != pngFiles.size()-2) {// Check if the page is full and add a new one
                        addNewPage();
                        addTitle(key);
                        addParagraph("Continued from previous page...");
                        graphCount = 0;
                    }

                }
            }
        }
        // Set the page number to 2 and add the table of contents
        pageNumber = 1;
        addNewPage();
        addToc();
        closeDocument();
        if (clearPNGDirectory() && moveToc()) {
            toc.clear();
            JOptionPane.showMessageDialog(null, "PDF successfully created!");
        }
    }

    /**
     * Method to move table of contents to the correct position in the PDF
     * @return boolean - True if the TOC was moved successfully, false otherwise
     */
    private static boolean moveToc() throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (FileInputStream inputStream = new FileInputStream(fileName)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        PdfReader reader = new PdfReader(baos.toByteArray());
        int n = reader.getNumberOfPages();
        reader.selectPages(String.format("%d, 1-%d", n, n-1));
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(fileName));
        stamper.close();
        reader.close();
        return true;
    }

    /**
     * Clears the PNG files from the directory
     * @return boolean - True if the directory was cleared successfully, false otherwise
     */
    private static boolean clearPNGDirectory() {
        File dir = new File("PNGs");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
        if (dir.delete()) {
            return true;
        }
        return false;
    }


    //This is a sample main method that will be used to show an example format of how the PDF are to be generated.
    //I will need to figure out the correct way to do this as it is important for the creation of nice PDFs.
    //I need t figure out the correct way to loop over all of the images and add them according to the catergory of data that they exist inside.
//    public static void main(String[] args) {
//        PDFGenerator generator = new PDFGenerator();
//        //Create PDF
//        generator.createPDF("Example.pdf");
//        //Title
//        addTitle("Example PDF Document");
//        //Paragraph
//        addParagraph("This is a sample paragraph in the PDF document.This paragraph will be written to explain the content displayed in the graphs below this paragraph, This sample paragraph is a way for us to guage how many panesl we can fit alongside a text description of the content. The more text i add the more characters we will");
//        //Image
//        addImagesSideBySide("1.png", "2.png");
//        addImagesSideBySide("1.png", "2.png");
//        addImagesSideBySide("1.png", "2.png");
//
//        //New Page
//        addNewPage();
//        addTitle("Second Page");
//        addParagraph("This is a sample paragraph in the PDF document.This paragraph will be written to explain the content displayed in the graphs below this paragraph, This sample paragraph is a way for us to guage how many panesl we can fit alongside a text description of the content. The more text i add the more characters we will");
//        addImagesSideBySide("41.png", "42.png");
//        addImagesSideBySide("41.png", "42.png");
//        addImagesSideBySide("41.png", "42.png");
//        addImagesSideBySide("41.png", "42.png");
//        addImagesSideBySide("41.png", "42.png");
//
//        addNewPage();
//        addTitle("");
//
//        closeDocument();
//
//    }
}
