package Controller;

import Model.Bill;
import Service.CashierService;

import java.sql.SQLException;

public class CashierController {
    private CashierService cashierService;
    public CashierController() {

    }

    // Method to add a product to the cart
//    public void addProductToBill(String productName, int quantity) {
//        if (cashierService.addProductToBill(productName, quantity, employee.getBranchCode())) {
//            JOptionPane.showMessageDialog(null, "Product added to bill!");
//        } else {
//            JOptionPane.showMessageDialog(null, "Product not found or insufficient stock.");
//        }
//    }

//
//
//    public void addProduct(String productName, int quantity, int branchid) {
//        if (cashierService.addProductToBill(productName, quantity,branchid))
//        {
//            JOptionPane.showMessageDialog(null, "Product added to bill!");
//        } else {
//            JOptionPane.showMessageDialog(null, "Product not found or insufficient stock.");
//        }
//    }
//
//    // Method to generate the bill and update the stock in the database
//    public void generateBill() throws SQLException {
//        // Print the bill
//        printBill();
//
//        // Update stock in the database after generating the bill
//        updateStockInDatabase();
//
//        // Reset the bill (cart) after generating it
//        cashierService.resetCart();
//    }

//
//    private void printBill() {
//        PrinterJob printerJob = PrinterJob.getPrinterJob();
//
//        // Set the print job to print the bill
//        printerJob.setPrintable(new Printable() {
//            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//                if (pageIndex > 0) {
//                    return NO_SUCH_PAGE; // Only one page of content
//                }
//
//                // Set up the graphics context for printing
//                Graphics2D g2d = (Graphics2D) graphics;
//                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
//
//                // Print the bill title
//                g2d.setFont(new Font("Arial", Font.BOLD, 16));
//                g2d.drawString("Bill", 100, 100);  // Adjust the X and Y positions as needed
//
//                // Set the font for the body of the bill
//                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
//
//                // Starting Y position for printing
//                int yPosition = 120;
//
//                // Loop through the cart and print product details
//                for (Map.Entry<Product, Integer> entry : cart.getCart().entrySet()) {
//                    String productName = entry.getKey().getName();
//                    int quantity = entry.getValue();
//                    BigDecimal productPrice = entry.getKey().getSalesPrice();
//
//                    // Print the product details on the bill
//                    String line = productName + " x" + quantity + " - $" + productPrice;
//                    g2d.drawString(line, 100, yPosition);
//                    yPosition += 20;  // Move down for the next line
//                }
//
//                // Now, print the stored subtotal, tax, and total
//                g2d.drawString("Subtotal: $" + cart.getSubtotal(), 100, yPosition);
//                yPosition += 20;
//                g2d.drawString("Tax (10%): $" + cart.getTax(), 100, yPosition);
//                yPosition += 20;
//                g2d.drawString("Total: $" + cart.getTotalBill(), 100, yPosition);
//
//                return PAGE_EXISTS;  // Indicating that the page has content
//            }
//        });
//
//        // Show the print dialog
//        if (printerJob.printDialog()) {
//            try {
//                printerJob.print();  // Execute the printing job
//            } catch (PrinterException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Error printing the bill.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }



    public boolean updateStockInDatabase(Bill cart, int branchId) throws SQLException {
        cashierService = new CashierService(); // Ensure the service instance is initialized
        boolean updateSuccessful = false;

             updateSuccessful = cashierService.updateStockInDatabase(cart, branchId);
        return updateSuccessful;
    }


}
