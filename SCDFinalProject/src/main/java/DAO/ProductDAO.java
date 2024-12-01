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

    public Product getProductByName(String productName, int branchId) {
        String query = "SELECT * FROM product WHERE product_name = ? AND branch_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productName);
            statement.setInt(2, branchId);  // Use branchId for filtering the products

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Extract product data from the result set
                String name = resultSet.getString("product_name");
                String category = resultSet.getString("product_category");
                BigDecimal originalPrice = resultSet.getBigDecimal("original_price");
                BigDecimal salesPrice = resultSet.getBigDecimal("sales_price");
                int quantity = resultSet.getInt("total_products");
                boolean status = resultSet.getBoolean("status");

                // Fetch the branch using the branchId
                Branch branch = getBranchById(branchId);

                // Return Product object created from the database values
                return new Product(branch, name, category, originalPrice, salesPrice, quantity, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if product not found
    }



    public boolean updateStock(String productName, int quantity, int branchId) throws SQLException {
        String updateStockQuery = "UPDATE product SET total_products = total_products - ? " +
                "WHERE product_name = ? AND branch_id = ? AND total_products >= ?";
        try (PreparedStatement statement = connection.prepareStatement(updateStockQuery)) {
            connection.setAutoCommit(false); // Disable auto-commit for transaction handling

            // Set parameters
            statement.setInt(1, quantity);        // Deduct the quantity from stock
            statement.setString(2, productName); // Match product by name
            statement.setInt(3, branchId);       // Match branch by branchId
            statement.setInt(4, quantity);       // Ensure stock is sufficient before deduction

            // Execute the update query
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                // No rows were updated; this indicates insufficient stock or invalid product/branch
                connection.rollback(); // Rollback the transaction
                System.err.println("Stock update failed for product: " + productName);
                return false;
            }

            // Commit the transaction if the update was successful
            connection.commit();
            System.out.println("Stock updated successfully for product: " + productName);
            return true;

        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            connection.rollback();
            System.err.println("Transaction failed, rolling back changes: " + e.getMessage());
            throw e;
        } finally {
            // Restore auto-commit behavior
            connection.setAutoCommit(true);
        }
    }



    // Method to add a new product
    public void addProduct(Product product, int branchId) {
        String insertProductQuery = "INSERT INTO product (branch_id, product_name, product_category, original_price, sales_price, total_products, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertProductQuery)) {
            // First fetch the branch using branchId, if needed
            Branch branch = getBranchById(branchId);  // A method that retrieves Branch by its ID (explained below)

            if (branch == null) {
                System.out.println("Branch not found!");
                return; // Exit if the branch doesn't exist
            }

            // Set the parameters for the product insertion
            statement.setInt(1, branchId);  // Store branchId in the product
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
    public List<String> getUniqueCategories(int branchId) {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT product_category FROM product WHERE branch_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, branchId);  // Use branchId to filter categories
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    categories.add(resultSet.getString("product_category"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching unique categories: " + e.getMessage());
        }
        return categories;
    }

    // Method to get products by category and branch
    public List<Product> getProductsByCategory(String category, int branchId) {
        String query = "SELECT * FROM product WHERE product_category = ? AND branch_id = ?";
        List<Product> productList = new ArrayList<>();
        Set<String> uniqueProductNames = new HashSet<>(); // To track unique product names

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category); // Set category parameter
            stmt.setInt(2, branchId); // Set branch ID parameter

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

                // Fetch the Branch object using branchId
                Branch branch = getBranchById(branchId);  // Assuming you have a method to get Branch by ID

                // Create the Product with the Branch object
                Product product = new Product(branch, name, productCategory, originalPrice, salesPrice, quantity, status);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly
        }
        return productList;
    }

    // Helper method to fetch a branch by ID
    private Branch getBranchById(int branchId) {
        String query = "SELECT * FROM branch WHERE branch_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId); // Set branch ID parameter
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Assuming Branch class has these fields, adjust as per your schema
                String city = rs.getString("city");
                String name = rs.getString("name");
                String status = rs.getString("status");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                int numberOfEmployees = rs.getInt("no_of_employees");

                // Create and return Branch object
                return new Branch(branchId, city, name, status, address, phone, numberOfEmployees);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the branch doesn't exist
    }





    public BigDecimal getProductPriceByName(String productName, int branchId) {
        String sql = "SELECT sales_price FROM product WHERE product_name = ? AND branch_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productName);
            ps.setInt(2, branchId); // Use branchId to filter by branch
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("sales_price");
            } else {
                throw new SQLException("Product not found in branch");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int getProductQuantityByName(String productName, int branchId) {
        String query = "SELECT total_products FROM product WHERE product_name = ? AND branch_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productName);
            statement.setInt(2, branchId); // Use branchId for filtering

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("total_products");
            } else {
                System.out.println("Product '" + productName + "' not found in branch with ID " + branchId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
