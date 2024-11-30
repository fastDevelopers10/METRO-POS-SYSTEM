package Model;

import java.awt.*;
import java.awt.print.*;

public class MyPrinter implements Printable {

    // This is where you define the content to be printed.
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        // You can handle pagination here.
        if (pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE; // No pages beyond the first one.
        }

        // Set up graphics for printing (adjust for page format)
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Here, you draw the content you want to print.
        g2d.drawString("Hello, World!", 100, 100); // Example: Simple text printing

        return Printable.PAGE_EXISTS; // Indicate that the page exists.
    }
}
