package Service;

import DAO.ProductDAO;
import DAO.TransactionDAO;
import Model.Bill;
import Model.Product;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class CashierService {
    private Bill cart;  // Assuming Cart is a class that holds the items added to the cart

    public CashierService() {
        this.cart = new Bill();
    }


    public boolean updateStockInDatabase(Bill cart, int branchId) throws SQLException {
        StringBuilder failedProducts = new StringBuilder(); // To store names of failed products
        boolean updateSuccessful=false;
        // Iterate through each product in the cart and update stock
        for (Map.Entry<Product, Integer> entry : cart.getCart().entrySet()) {
            Product product = entry.getKey();
            int quantitySold = entry.getValue();

            // Call the ProductDAO to update the stock in the database
            ProductDAO productDAO = new ProductDAO();
            updateSuccessful = productDAO.updateStock(product.getName(), quantitySold, branchId);

            if (!updateSuccessful) {
                failedProducts.append(product.getName()).append(", "); // Add failed product to list
                JOptionPane.showMessageDialog(null,
                        "Failed to update stock for product: " + product.getName()+
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        return updateSuccessful; // Return true if all updates were successful, false otherwise
    }
    // Method to insert transaction records via TransactionDAO
    public boolean insertTransaction(int branchCode, int productId,
                                     int quantitySold, Date transactionDate, BigDecimal profit) throws SQLException {
        return TransactionDAO.insertTransactions(branchCode, productId,
                quantitySold,transactionDate, profit);
    }
}
