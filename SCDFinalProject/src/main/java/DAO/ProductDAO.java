package DAO;

import Model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductDAO {
    private final Connection connection;

    public ProductDAO() {
        // Initialize the connection from DBConnection class
        this.connection = DBConnection.getConnection();
    }

    // Method to get a product by its name
    public Product getProductByName(String productName) {
        String query = "SELECT * FROM inventory WHERE name = ?";
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


    public void updateStock(String productName, int quantity) throws SQLException {
        // Prepare the query for updating stock
        String updateStockQuery = "UPDATE inventory SET no_of_products = no_of_products - ? WHERE name = ?";

        // Start a transaction
        try (PreparedStatement statement = connection.prepareStatement(updateStockQuery)) {
            connection.setAutoCommit(false);  // Disable auto-commit for transaction handling

            statement.setInt(1, quantity);  // Deduct the quantity from stock
            statement.setString(2, productName);  // Match product by name

            // Execute the update query
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Product not found or insufficient stock.");
            }

            connection.commit();  // Commit the transaction if no errors
            System.out.println("Stock updated for product: " + productName);

        } catch (SQLException e) {
            // Rollback the transaction in case of any error
            connection.rollback();
            System.err.println("Transaction failed, rolling back changes: " + e.getMessage());
            throw e;  // Rethrow the exception after rollback
        } finally {
            connection.setAutoCommit(true);  // Restore auto-commit behavior
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
            statement.setInt(5, product.getQuantity());

            statement.executeUpdate();
            System.out.println("Product added successfully: " + product.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Method to get unique category names from the database
    public List<String> getUniqueCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM inventory"; // Ensure correct table is used

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (connection == null || connection.isClosed()) {
                throw new SQLException("Connection is closed or not initialized.");
            }

            while (resultSet.next()) {
                categories.add(resultSet.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching unique categories: " + e.getMessage());
        }
        return categories;
    }



    public List<Product> getProductsByCategory(String category) throws SQLException {
        String query = "SELECT name, category, original_price, sales_price, no_of_products FROM inventory WHERE category = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, category);

        ResultSet rs = stmt.executeQuery();
        Set<String> uniqueProductNames = new HashSet<>();  // To track unique product names
        List<Product> productList = new ArrayList<>();

        while (rs.next()) {
            String name = rs.getString("name");

            // Skip duplicate product names
            if (uniqueProductNames.contains(name)) {
                continue;  // Skip if product name already exists
            }

            uniqueProductNames.add(name);  // Mark the product name as seen

            String categoryName = rs.getString("category");
            BigDecimal originalPrice = rs.getBigDecimal("original_price");
            BigDecimal salesPrice = rs.getBigDecimal("sales_price");
            int noOfProducts = rs.getInt("no_of_products");

            // Create a Product object and add it to the list
            Product product = new Product(name, categoryName, originalPrice, salesPrice, noOfProducts);
            productList.add(product);
        }
        return productList;
    }

    public BigDecimal getProductPriceByName(String productName) throws SQLException {
        String sql = "SELECT sales_price FROM inventory WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("sales_price");
            } else {
                throw new SQLException("Product not found");
            }
        }
    }


    public int getProductQuantityByName(String productName) {
        String query = "SELECT no_of_products FROM inventory WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productName); // Set the product name as a parameter

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int quantity = resultSet.getInt("no_of_products");

                // Print a message about the stock availability
                if (quantity > 0) {
                    System.out.println("Stock available for product '" + productName + "': " + quantity + " units.");
                } else {
                    System.out.println("Stock for product '" + productName + "' is out.");
                }
                return quantity; // Return the quantity from the result set
            } else {
                // If the product is not found in the database
                System.out.println("Product '" + productName + "' not found in the inventory.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return 0 if product is not found or an error occurs
        return 0;
    }

}


