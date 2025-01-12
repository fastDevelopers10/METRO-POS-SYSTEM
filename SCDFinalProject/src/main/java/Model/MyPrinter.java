package Model;

import java.awt.*;
import java.awt.print.*;

public class MyPrinter implements Printable {

    private String data;  // Sales data to be printed

    // Setter method for sales data
    public void setData(String data) {
        this.data = data;
    }

    // This is where you define the content to be printed.
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        // Handle pagination (only one page for simplicity)
        if (pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE; // No pages beyond the first one
        }

        // Set up graphics for printing (adjust for page format)
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Print the sales data
        g2d.drawString(data, 100, 100);  // Customize how data is printed

        return Printable.PAGE_EXISTS; // Indicate that the page exists
    }
}
