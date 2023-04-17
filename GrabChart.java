/*
Atm I cannot figure out how I can call this method after the graphs have been painted from the mainGUI.
I believe that was the problem I had with the previous method that i attempted to use. ive put this at the bottom of this file.

I believe both methods should work if they are called after the repaint, the problem is that the method doesn't seem to work if called from directly after the repaint.
Adding a wait before the screenshot is taken seems to prevent the screen from painting.

It's possible that im misunderstanding something about the way the graphs are added because of the "invoke later" function that they are generated inside.

We could create another button that will take the screenshots called "save report" So that the screenshot and conversion to PDF is done after they are displayed to the screen rather under the same button press.

Another problem that i'm trying to solve is that the window im trying to screenshot is limited to our screens size, which of course is not the same length as an a4 paper.
For this we could possibly shape the report window the same aspect ratio of a piece of paper but make it enlarged to the largest scale that we can fit into the screen.
This way we can arrange he graphs and edit their fontsizes to increase visibility and maximise resolution from the screenshots.

I did something like this in my old code folder that was intended for capturing JFXpanels, It made sure that the entire paper shaped report panel was visible on the screen.

        //This is a set-up for a window with the aspect ratio the same as a piece of paper.
        final double A5_WIDTH = 148 * 4;
        final double A5_HEIGHT = 210 * 4;
        //This can be altered in size by incrementing the number 4 up or down. This can be looked at to see if we can fit an entire page inside the right panel.

It would probably also be sensible to separate the right panel into a series of panels that indicate each page.
This will make it easier for us to center each page ready for its screenshot to be captured.
possibly a limit to the amount of graphs on each page so that they can be divided and displayed to the screen.
*/
//Because we are taking screenshots we should ensure that graphs have bold features that won't be lost to a lower res screengrab.
/*It is possible that the initial way I tried to retrieve the capture is the best way because one time I tested it, The method produced a paper sized blank screengrab, which im assuming was the report content window without anything painted to it.*/

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
/*
Make a call to the class like this.

File currentDirectory = new File("Report.png");
PDF.captureRightPanel(rightPanel, currentDirectory);

*/
public class GrabChart {

    /**
     * Save JPanel to buffered image (For chart panels)
     * @param panel
     * @param file
     */
    public static void captureChart(JPanel panel, File file) {
        try {
            //Grabing the parent JFrame of the JPanel.
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
            //Bring forward the application to the front of the screen.
            frame.toFront();
            frame.repaint();
            //Grabbing the location of the top-left corner of the right panel on the screen.
            Point panelLocation = panel.getLocationOnScreen();
            //Grab the size of the right panel
            Dimension panelSize = panel.getSize();
            //Robot instance that will take a screenshot of the section of the screen with the right panel.
            Robot robot = new Robot();
            //Take a screenshot of the right panel.
            BufferedImage PanelScreenshot = robot.createScreenCapture(new Rectangle(panelLocation, panelSize));
            //Saving the screenshot to a file with the format png.
            ImageIO.write(PanelScreenshot, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save JPanel to buffered image (For non chart panels)
     * @param panel
     * @param file
     */
    public static void capturePanel(JPanel panel, File file) {
        BufferedImage bi = new BufferedImage(panel.getSize().width, panel.getSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        panel.paint(g);
        g.dispose();
        try {
            ImageIO.write(bi,"png",file);
        }catch (Exception ex) {}
    }
}
