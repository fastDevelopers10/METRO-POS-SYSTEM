package Service;

import DAO.ProductDAO;
import Model.Bill;
import Model.Product;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

public class CashierService {
    private Bill cart;  // Assuming Cart is a class that holds the items added to the cart

    public CashierService() {
        this.cart = new Bill();
    }

    // Method to add a product to the cart and check for sufficient stock
    public boolean addProductToBill(Product product, int quantity) {
        // Logic to add a product to the cart
        // Assuming there's a method in the Cart class to handle this
        return cart.addProduct(product, quantity);
    }

    // Method to get the total bill (subtotal + tax)
    public BigDecimal getTotalBill() {
        // Return the total bill (sum of products in cart)
        return cart.getTotalBill();
    }

    // Method to reset the cart after generating the bill
    public void resetCart() {
        cart.resetBill();  // Assuming clear() removes all products from the cart
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
}
