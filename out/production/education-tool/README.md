# Education Management Tool

The purpose of this tool is to provide a simple way to generate reports for academic institutions. The tool allows the user to select a CSV file containing student data and then generate a report containing statistics about the students. The tool is designed to be used by an academic administrator who needs to generate reports for a number of different modules and courses.


### Third-Party Libraries Acknowledgements
#### For Functionality
* jfreechart (http://www.jfree.org/jfreechart/)
* itextpdf (http://itextpdf.com/)

#### For Testing
* junit (http://junit.org/)

### How to Run
#### From the Command Line
1. Download the jar file.
2. Run the following command: `java -jar "Education Management Tool.jar"`
#### From an IDE
1. Clone the repository.
2. Open the project in your IDE.
3. Add the functionally libraries to the classpath.
   - [For Intellij](https://www.geeksforgeeks.org/how-to-add-external-jar-file-to-an-intellij-idea-project/)
   - [For Eclipse](https://wiki.eclipse.org/FAQ_How_do_I_add_an_extra_library_to_my_project%27s_classpath%3F)
4. Run from the main method found in the `MainGUI` class.

### How to Use
1. Select the CSV file containing the student data from the file chooser window. 
   - *The file chooser starts in the directory of the project. For ease of use, it is recommended to place the CSV file in 
    this same directory. However, the file chooser will allow you to navigate to any directory on your computer.* 
>
> **IMPORTANT NOTE!**  
> For the program to work correctly, the CSV file must be formatted as follows:
>- The first row must contain in order the column headers: `Student RegNo`, `Course`, and `Module` headers.
>- There must be no more than 15 module headers.
>- There must be no more than 126 students.

2. Make your selection from the menu.
   - Click the checkbox for a statistic to enable it.
   - Select the modules or courses (whichever is applicable)
   - Enable any further options (where applicable)
   - Hover over the question mark on each panel for more information.
3. Click the `Generate Report` button.
    - You can expand the report pane by dragging the divider.
4. Click the `Save Report` button to save the report as a PDF file.
    - Provide your desired file name (you do not need to include the `.pdf` extension).
        - If the file already exists, it will be overwritten.
    - The file will be saved in the directory of the project.
   
