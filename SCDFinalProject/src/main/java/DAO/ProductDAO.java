package DAO;

import Model.Branch;

import Model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class ProductDAO {
    private final Connection connection;

    public ProductDAO() {

        this.connection = DBConnection.getConnection();
    }

//    public Product getProductByName(String productName, int branchId) {
//        String query = "SELECT * FROM product WHERE product_name = ? AND branch_id = ?";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, productName);
//            statement.setInt(2, branchId);  // Use branchId for filtering the products
//
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                // Extract product data from the result set
//                int productId = resultSet.getInt("product_id"); // Get product ID from DB
//                String name = resultSet.getString("product_name");
//                String category = resultSet.getString("product_category");
//                BigDecimal originalPrice = resultSet.getBigDecimal("original_price");
//                BigDecimal salesPrice = resultSet.getBigDecimal("sales_price");
//                int quantity = resultSet.getInt("total_products");
//                boolean status = resultSet.getBoolean("status");
//
//                // Fetch the branch using the branchId
//                Branch branch = getBranchById(branchId);
//
//                // Return Product object created from the database values
//                return new Product(productId, branch, name, category, originalPrice, salesPrice, quantity, status);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null; // Return null if product not found
//    }
//


    public boolean updateStock(String productName, int quantity, int branchId) throws SQLException {
        String updateStockQuery = "UPDATE product SET total_products = total_products - ? " +
                "WHERE product_name = ? AND branch_id = ? AND total_products >= ?";
        try (PreparedStatement statement = connection.prepareStatement(updateStockQuery)) {
            connection.setAutoCommit(false); // Disable auto-commit for transaction handling

            statement.setInt(1, quantity);
            statement.setString(2, productName);
            statement.setInt(3, branchId);
            statement.setInt(4, quantity);


            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {

                connection.rollback();
                System.err.println("Stock update failed for product: " + productName);
                return false;
            }

            connection.commit();
            System.out.println("Stock updated successfully for product: " + productName);
            return true;

        } catch (SQLException e) {

            connection.rollback();
            System.err.println("Transaction failed, rolling back changes: " + e.getMessage());
            throw e;
        } finally {

            connection.setAutoCommit(true);
        }
    }




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

    public List<Product> getProductsByCategory(String category, int branchId) {
        String query = "SELECT * FROM product WHERE product_category = ? AND branch_id = ?";
        List<Product> productList = new ArrayList<>();
        Set<String> uniqueProductNames = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category);
            stmt.setInt(2, branchId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("product_name");

                if (uniqueProductNames.contains(name)) {
                    continue;
                }
                uniqueProductNames.add(name);

                int productId = rs.getInt("product_id");
                String productCategory = rs.getString("product_category");
                BigDecimal originalPrice = rs.getBigDecimal("original_price");
                BigDecimal salesPrice = rs.getBigDecimal("sales_price");
                int quantity = rs.getInt("total_products");
                boolean status = rs.getBoolean("status");

                Branch branch = getBranchById(branchId);

                Product product = new Product(productId, branch, name, productCategory, originalPrice, salesPrice, quantity, status);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    private Branch getBranchById(int branchId) {
        String query = "SELECT * FROM branch WHERE branch_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId); // Set branch ID parameter
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                String city = rs.getString("city");
                String name = rs.getString("name");
                String status = rs.getString("status");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                int numberOfEmployees = rs.getInt("no_of_employees");

                return new Branch(branchId, city, name, status, address, phone, numberOfEmployees);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
            statement.setInt(2, branchId);

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
    public List<Product> getProductsByBranchWithSearch(int branchId, String searchQuery) {
        String query = "SELECT * FROM product WHERE branch_id = ? AND " +
                "(product_name LIKE ? OR product_category LIKE ?)";
        List<Product> products = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, branchId);
            statement.setString(2, "%" + searchQuery + "%");
            statement.setString(3, "%" + searchQuery + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("product_name");
                String category = resultSet.getString("product_category");
                BigDecimal originalPrice = resultSet.getBigDecimal("original_price");
                BigDecimal salesPrice = resultSet.getBigDecimal("sales_price");
                int quantity = resultSet.getInt("total_products");
                boolean status = resultSet.getBoolean("status");

                Branch branch = getBranchById(branchId); // Existing method in ProductDAO
               Product product = new Product(productId, branch, name, category, originalPrice, salesPrice, quantity, status);
               products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<String> fetchVendors() {
        List<String> vendors = new ArrayList<>();
        String query = "SELECT vendor_id, name FROM vendor WHERE status = TRUE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String vendorName = rs.getString("name");
                vendors.add(vendorName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendors;
    }

    public List<String> fetchProductsByBranch(int branchId) {
        String query = "SELECT DISTINCT product_name FROM product WHERE branch_id = ?";
        List<String> productList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("product_name");
                productList.add(name.trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public boolean addNewProduct(int branchId, int vendorId, String name, String category, int totalProducts,
                                 double originalPrice, double salesPrice, Date purchaseDate) {
        String productQuery = "INSERT INTO product (branch_id, product_name, product_category, total_products, original_price, sales_price, status) VALUES (?, ?, ?, ?, ?, ?, TRUE)";
        String vendorProductQuery = "INSERT INTO vendor_product (vendor_id, branch_id, product_name, product_category, cartons_purchased, items_per_carton, original_price, sales_price, status, purchase_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, TRUE, ?)";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            // Insert into the product table
            try (PreparedStatement productStmt = connection.prepareStatement(productQuery, Statement.RETURN_GENERATED_KEYS)) {
                productStmt.setInt(1, branchId);
                productStmt.setString(2, name);
                productStmt.setString(3, category);
                productStmt.setInt(4, totalProducts);
                productStmt.setDouble(5, originalPrice);
                productStmt.setDouble(6, salesPrice);
                productStmt.executeUpdate();

                ResultSet rs = productStmt.getGeneratedKeys();
                if (rs.next()) {
                    int productId = rs.getInt(1);

                    try (PreparedStatement vendorProductStmt = connection.prepareStatement(vendorProductQuery)) {
                        vendorProductStmt.setInt(1, vendorId);
                        vendorProductStmt.setInt(2, branchId);
                        vendorProductStmt.setString(3, name);
                        vendorProductStmt.setString(4, category);
                        vendorProductStmt.setInt(5, totalProducts);
                        vendorProductStmt.setInt(6, 1);
                        vendorProductStmt.setDouble(7, originalPrice);
                        vendorProductStmt.setDouble(8, salesPrice);
                        vendorProductStmt.setDate(9, new java.sql.Date(purchaseDate.getTime()));
                        vendorProductStmt.executeUpdate();
                    }
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateExistingProduct(int branchId, int vendorId, int productId, int totalProducts,
                                         double originalPrice, double salesPrice, Date purchaseDate) {
        String updateProductQuery = "UPDATE product SET total_products = total_products + ?, original_price = ?, sales_price = ? WHERE product_id = ? AND branch_id = ?";
        String vendorProductQuery = "INSERT INTO vendor_product (vendor_id, branch_id, product_id, product_name, product_category, cartons_purchased, items_per_carton, original_price, sales_price, status, purchase_date) " +
                "SELECT ?, ?, product_id, product_name, product_category, ?, ?, ?, ?, TRUE, ? FROM product WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);


            try (PreparedStatement productStmt = connection.prepareStatement(updateProductQuery)) {
                productStmt.setInt(1, totalProducts);
                productStmt.setDouble(2, originalPrice);
                productStmt.setDouble(3, salesPrice);
                productStmt.setInt(4, productId);
                productStmt.setInt(5, branchId);
                productStmt.executeUpdate();
            }

            try (PreparedStatement vendorProductStmt = connection.prepareStatement(vendorProductQuery)) {
                vendorProductStmt.setInt(1, vendorId);
                vendorProductStmt.setInt(2, branchId);
                vendorProductStmt.setInt(3, totalProducts);
                vendorProductStmt.setInt(4, 1); // Default items per carton
                vendorProductStmt.setDouble(5, originalPrice);
                vendorProductStmt.setDouble(6, salesPrice);
                vendorProductStmt.setDate(7, new java.sql.Date(purchaseDate.getTime())); // Set the purchase date
                vendorProductStmt.setInt(8, productId);
                vendorProductStmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean productExistsForBranch(int branchId, String productName, String category) {
        String query = "SELECT COUNT(*) FROM product WHERE branch_id = ? AND product_name = ? AND product_category = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            stmt.setString(2, productName);
            stmt.setString(3, category);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean addOrUpdateProduct(int branchId, int vendorId, int productId, String name, String category,
                                      int cartons, int itemsPerCarton, double originalPrice, double salesPrice,
                                      Date purchaseDate) {
        int totalProducts = cartons * itemsPerCarton;

        if (productId == -1) {
            if (productExistsForBranch(branchId, name, category)) {

                int existingProductId = getProductId(branchId, name, category);
                return updateExistingProduct(branchId, vendorId, existingProductId, totalProducts, originalPrice, salesPrice, purchaseDate);
            } else {

                return addNewProduct(branchId, vendorId, name, category, totalProducts, originalPrice, salesPrice, purchaseDate);
            }
        } else {

            return updateExistingProduct(branchId, vendorId, productId, totalProducts, originalPrice, salesPrice, purchaseDate);
        }

    }

    public int getProductId(int branchId, String productName, String category) {
        String query = "SELECT product_id FROM product WHERE branch_id = ? AND product_name = ? AND product_category = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            stmt.setString(2, productName);
            stmt.setString(3, category);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("product_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Product> fetchProductsByBranchForTable(int branchId, String searchQuery) {
        String query = "SELECT product_id, product_name, product_category, total_products, original_price, sales_price, status FROM product WHERE branch_id = ? AND product_name LIKE ?";
        List<Product> productList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            stmt.setString(2, "%" + searchQuery + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                String category = rs.getString("product_category");
                int quantity = rs.getInt("total_products");
                double originalPrice = rs.getDouble("original_price");
                double salesPrice = rs.getDouble("sales_price");
                boolean status = rs.getBoolean("status");

                Product product = new Product(id, name, category, quantity, originalPrice, salesPrice, status);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }
    public boolean deleteProductById(int productId) {
        String query = "DELETE FROM product WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getProductCountByBranch(int branchId) {
        String query = "SELECT COUNT(*) FROM product WHERE branch_id = ?";
        int count = 0;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
    public int getVendorCount() {
        String query = "SELECT COUNT(*) FROM vendor";
        int count = 0;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }



}