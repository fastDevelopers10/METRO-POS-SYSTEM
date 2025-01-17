package Model;

import java.awt.*;
import java.awt.print.*;

public class MyPrinter implements Printable {

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return Printable.PAGE_EXISTS;
    }

    public static class MyPrinterWithTableData implements Printable {
        private String[] columnHeaders;
        private String[][] tableData;
        private String title;

        public void setTableData(String[] columnHeaders, String[][] tableData, String title) {
            this.columnHeaders = columnHeaders;
            this.tableData = tableData;
            this.title = title;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex >= 1) {
                return Printable.NO_SUCH_PAGE; // Only one page
            }

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.setFont(new Font("Century Gothic", Font.PLAIN, 12));

            g2d.setFont(new Font("Century Gothic", Font.BOLD, 14));
            g2d.drawString(title, 100, 100);

            int x = 100;
            for (String header : columnHeaders) {
                g2d.drawString(header, x, 130); // Adjust position based on column
                x += 100; // Adjust column width
            }
            int y = 150;
            for (String[] row : tableData) {
                x = 100; // Reset x position for each row
                for (String cell : row) {
                    g2d.drawString(cell, x, y);
                    x += 100; // Adjust column width
                }
                y += 20; // Adjust row height
            }
            return Printable.PAGE_EXISTS;
        }
    }

    public static class MyPrinterWithSalesData implements Printable {
        private String data;

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex >= 1) {
                return Printable.NO_SUCH_PAGE; // No pages beyond the first one
            }
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            g2d.drawString(data, 100, 100);
            return Printable.PAGE_EXISTS; // Indicate that the page exists
        }
    }
}
