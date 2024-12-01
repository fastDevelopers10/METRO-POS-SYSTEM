package DAO;

import Model.Branch;
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

    // Method to get a product by its name and branch, passing Product and Branch as parameters
    public Product getProductByName(String productName, Branch branch) {
        String query = "SELECT * FROM product WHERE product_name = ? AND branch_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productName);  // Use product's name to query
            statement.setInt(2, branch.getBranchId());  // Use branch's ID to query
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Extract product data from the result set
                String name = resultSet.getString("product_name");
                String category = resultSet.getString("product_category");
                BigDecimal originalPrice = resultSet.getBigDecimal("original_price");
                BigDecimal salesPrice = resultSet.getBigDecimal("sales_price");
                int quantity = resultSet.getInt("total_products");
                boolean status = resultSet.getBoolean("status");

                // Return Product object created from the database values and the Branch object
                return new Product(branch, name, category, originalPrice, salesPrice, quantity, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if product not found
    }


    // Method to update stock for a product
    public void updateStock(String productName, int quantity) throws SQLException {
        String updateStockQuery = "UPDATE product SET total_products = total_products - ? WHERE product_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateStockQuery)) {
            connection.setAutoCommit(false);  // Disable auto-commit for transaction handling

            statement.setInt(1, quantity);  // Deduct the quantity from stock
            statement.setString(2, productName);  // Match product by name

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Product not found or insufficient stock.");
            }

            connection.commit();  // Commit the transaction if no errors
            System.out.println("Stock updated for product: " + productName);

        } catch (SQLException e) {
            connection.rollback();  // Rollback the transaction in case of error
            System.err.println("Transaction failed, rolling back changes: " + e.getMessage());
            throw e;
        } finally {
            connection.setAutoCommit(true);  // Restore auto-commit behavior
        }
    }

    // Method to add a new product
    public void addProduct(Product product) {
        String insertProductQuery = "INSERT INTO product (branch_id, product_name, product_category, original_price, sales_price, total_products, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertProductQuery)) {
            statement.setInt(1, product.getBranch().getBranchId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getCategory());
            statement.setBigDecimal(4, product.getOriginalPrice());
            statement.setBigDecimal(5, product.getSalesPrice());
            statement.setInt(6, product.getQuantity());
            statement.setBoolean(7, product.isStatus());

            statement.executeUpdate();
            System.out.println("Product added successfully: " + product.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get unique categories
    public List<String> getUniqueCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT product_category FROM product";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(resultSet.getString("product_category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching unique categories: " + e.getMessage());
        }
        return categories;
    }

    // Method to get products by category and branch
    public List<Product> getProductsByCategory(String category, Branch branch) {
        String query = "SELECT * FROM product WHERE product_category = ? AND branch_id = ?";
        List<Product> productList = new ArrayList<>();
        Set<String> uniqueProductNames = new HashSet<>(); // To track unique product names

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category); // Set category
            stmt.setInt(2, branch.getBranchId()); // Set branch ID

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("product_name");

                // Skip duplicate product names
                if (uniqueProductNames.contains(name)) {
                    continue;
                }
                uniqueProductNames.add(name);

                String productCategory = rs.getString("product_category");
                BigDecimal originalPrice = rs.getBigDecimal("original_price");
                BigDecimal salesPrice = rs.getBigDecimal("sales_price");
                int quantity = rs.getInt("total_products");
                boolean status = rs.getBoolean("status");

                // Create Product object using the Branch and other data
                Product product = new Product(branch, name, productCategory, originalPrice, salesPrice, quantity, status);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }


    // Method to get product price by name
    public BigDecimal getProductPriceByName(String productName) {
        String sql = "SELECT sales_price FROM product WHERE product_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("sales_price");
            } else {
                throw new SQLException("Product not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to get product quantity by name
    public int getProductQuantityByName(String productName) {
        String query = "SELECT total_products FROM product WHERE product_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("total_products");
            } else {
                System.out.println("Product '" + productName + "' not found in the inventory.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
