package DAO;

import Model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to fetch a product from the database
    public Product getProductByName(String productName)
    {
        String query = "SELECT * FROM products WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double originalPrice = resultSet.getDouble("original_price");
                double salePrice = resultSet.getDouble("sale_price");
                double pricePerUnit = resultSet.getDouble("price_per_Unit");
                double pricePerCarton = resultSet.getDouble("price_per_Carton");
                int quantity = 1; // Set a default quantity for retrieval, or handle this based on usage
                int stock = resultSet.getInt("stock");

                // Create and return the product instance with all 8 parameters
                return new Product(name, category, originalPrice, salePrice, pricePerUnit, pricePerCarton, quantity, stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the product is not found
    }
    // Method to update stock in the database
    public void updateStock(String productName, int quantity) {
        String updateStockQuery = "UPDATE products SET stock = stock - ? WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateStockQuery)) {
            statement.setInt(1, quantity);  // Deduct quantity from stock
            statement.setString(2, productName);  // Match product by name
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Product not found or insufficient stock.");
            } else {
                System.out.println("Stock updated for product: " + productName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
