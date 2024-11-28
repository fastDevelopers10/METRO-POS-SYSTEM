package DAO;

import Model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        // Initialize the connection from DBConnection class
        this.connection = DBConnection.getConnection();
    }

    // Method to get a product by its name
    public Product getProductByName(String productName) {
        String query = "SELECT * FROM product WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                BigDecimal originalPrice = resultSet.getBigDecimal("original_price");
                BigDecimal salesPrice = resultSet.getBigDecimal("sales_price");
                int noOfProducts = resultSet.getInt("no_of_products");

                // Return Product object created from the database values
                return new Product(name, category, originalPrice, salesPrice, noOfProducts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if product not found
    }

    // Method to get products by category
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE category = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                BigDecimal price = resultSet.getBigDecimal("sales_price");
                int quantity = resultSet.getInt("no_of_products");

                products.add(new Product(name, category, price, price, quantity));  // Adjusted constructor call
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Method to update stock in the database
    public void updateStock(String productName, int quantity) {
        String updateStockQuery = "UPDATE product SET no_of_products = no_of_products - ? WHERE name = ?";

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

    // Method to add a new product to the database
    public void addProduct(Product product) {
        String insertProductQuery = "INSERT INTO product (name, category, original_price, sales_price, no_of_products) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertProductQuery)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setBigDecimal(3, product.getOriginalPrice());
            statement.setBigDecimal(4, product.getSalesPrice());
            statement.setInt(5, product.getNoOfProducts());

            statement.executeUpdate();
            System.out.println("Product added successfully: " + product.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
