package DAO;

import Model.Branch;

import Model.Product;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;


public class ProductDAO {
    private final Connection connection;

    public ProductDAO() {

        this.connection = DBConnection.getConnection();
    }

    public boolean updateStock(String productName, int quantity, int branchId) throws SQLException {
        String updateStockQuery = "UPDATE product SET total_products = total_products - ? " +
                "WHERE product_name = ? AND branch_id = ? AND total_products >= ?";

        if (!isDatabaseConnected()) {
            // If no internet, save the stock update to a file
            saveUpdateToFile(productName, quantity, branchId);
            return false;
        }

        try (PreparedStatement statement = connection.prepareStatement(updateStockQuery)) {
            connection.setAutoCommit(false);

            statement.setInt(1, quantity);
            statement.setString(2, productName);
            statement.setInt(3, branchId);
            statement.setInt(4, quantity);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // Check for internet connection by attempting a simple query
    public boolean isDatabaseConnected() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeQuery("SELECT 1");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Save the stock update to a file when no internet connection is available
    private void saveUpdateToFile(String productName, int quantity, int branchId) {
        try (FileWriter writer = new FileWriter("stock_updates.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            String updateData = productName + "," + quantity + "," + branchId + "," + System.currentTimeMillis();
            bufferedWriter.write(updateData);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Error saving update to file: " + e.getMessage());
        }
    }

    // Load and process pending updates from the file (if internet is restored)
    public void processPendingUpdates() {
        try (BufferedReader reader = new BufferedReader(new FileReader("stock_updates.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String productName = data[0];
                int quantity = Integer.parseInt(data[1]);
                int branchId = Integer.parseInt(data[2]);
                long timestamp = Long.parseLong(data[3]);

                // Process the saved update
                updateStock(productName, quantity, branchId);
            }

            // After processing, clear the file
            new FileWriter("stock_updates.txt", false).close();  // Clear the file

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    // Method to check if internet is available (by checking database connectivity)
    public boolean isInternetAvailable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeQuery("SELECT 1");  // Simple query to check internet connection
            return true;
        } catch (SQLException e) {
            return false;  // No internet connection
        }
    }



    public List<String> getUniqueCategories(int branchId) {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT product_category FROM product WHERE branch_id = ?";

        // Check connection validity at the start
        try (Connection connection = DBConnection.getConnection()) {
            if (connection == null || connection.isClosed()) {
                System.err.println("Invalid database connection.");
                return categories;
            }

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
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to get connection: " + e.getMessage());
        }

        System.out.println(categories);  // Debugging output
        return categories;
    }

    public List<Product> getProductsByCategory(String category, int branchId) {
        String query = "SELECT * FROM product WHERE product_category = ? AND branch_id = ?";
        List<Product> productList = new ArrayList<>();
        Set<String> uniqueProductNames = new HashSet<>();

        // Fetch branch details before querying the product table
        Branch branch = getBranchById(branchId);
        if (branch == null) {
            System.err.println("Branch not found for branchId: " + branchId);
            return productList; // Return empty list if branch is not found
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, category);
            stmt.setInt(2, branchId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("product_name");

                    // Skip duplicate product names
                    if (uniqueProductNames.contains(name)) {
                        continue;
                    }
                    uniqueProductNames.add(name);

                    // Extract product details
                    int productId = rs.getInt("product_id");
                    String productCategory = rs.getString("product_category");
                    BigDecimal originalPrice = rs.getBigDecimal("original_price");
                    BigDecimal salesPrice = rs.getBigDecimal("sales_price");
                    int quantity = rs.getInt("total_products");
                    boolean status = rs.getBoolean("status");

                    // Create Product object

                    System.out.println("lol"+productId);
                    Product product = new Product(productId, branch, name, productCategory, originalPrice, salesPrice, quantity, status);
                    productList.add(product);

                }
            }
        } catch (SQLException e) {
            // Log detailed error message
            System.err.println("Error retrieving products: " + e.getMessage());
        }
        return productList;
    }



    private Branch getBranchById(int branchId) {
        String query = "SELECT * FROM branch WHERE branch_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, branchId); // Set branch ID parameter

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String city = rs.getString("city");
                    String name = rs.getString("name");
                    String status = rs.getString("status");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");
                    int numberOfEmployees = rs.getInt("no_of_employees");

                    // Create and return the Branch object
                    return new Branch(branchId, city, name, status, address, phone, numberOfEmployees);
                }
            }
        } catch (SQLException e) {
            // Replace with proper logging for production environments
            System.err.println("Error retrieving branch details: " + e.getMessage());
        }
        return null; // Return null if no branch found or in case of an error
    }


    public int getCurrentStock(int productId) throws SQLException {
        String query = "SELECT total_products FROM product WHERE product_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_products");
            }
        }
        return 0;
    }



    public BigDecimal getProductPriceByName(String productName, int branchId) {
        String sql = "SELECT sales_price FROM product WHERE product_name = ? AND branch_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
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
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, productName);
            statement.setInt(2, branchId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_products");
                } else {
                    System.out.println("Product '" + productName + "' not found in branch with ID " + branchId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving product quantity: " + e.getMessage());
        }
        return 0; // Return 0 if no result or an error occurs
    }
    public static int getStockByBranch(int branchId) {
        String query = "SELECT SUM(total_products) AS total_products FROM product WHERE branch_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the branch ID in the query
            statement.setInt(1, branchId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_products"); // Use alias 'total_products'
                } else {
                    System.out.println("No products found in branch with ID " + branchId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving product quantity: " + e.getMessage());
        }
        return 0; // Return 0 if no result or an error occurs
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

        try (Connection connection = DBConnection.getConnection(); // Create a new connection
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
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

    public List<Map<String, Object>> fetchVendorProductsByBranchforvendors(int branchId) {
        String query = """
        SELECT 
            v.name AS vendor_name,
            vp.product_category,
            vp.product_name,
            vp.original_price,
            vp.sales_price,
            vp.cartons_purchased,
            vp.items_per_carton,
            vp.products_purchased,
            vp.purchase_date
        FROM 
            vendor_product vp
        JOIN 
            vendor v ON vp.vendor_id = v.vendor_id
        WHERE 
            vp.branch_id = ?
        ORDER BY 
            v.name, vp.product_category, vp.product_name;
    """;

        List<Map<String, Object>> vendorProductList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> productData = new HashMap<>();
                productData.put("vendor_name", rs.getString("vendor_name"));
                productData.put("product_category", rs.getString("product_category"));
                productData.put("product_name", rs.getString("product_name"));
                productData.put("original_price", rs.getBigDecimal("original_price"));
                productData.put("sales_price", rs.getBigDecimal("sales_price"));
                productData.put("cartons_purchased", rs.getInt("cartons_purchased"));
                productData.put("items_per_carton", rs.getInt("items_per_carton"));
                productData.put("products_purchased", rs.getInt("products_purchased"));
                productData.put("purchase_date", rs.getDate("purchase_date"));
                vendorProductList.add(productData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendorProductList;
    }
    // Method to get product IDs and quantities
    public List<Object[]> getProductIdAndQuantities() {
        List<Object[]> productList = new ArrayList<>();
        String query = "SELECT product_id, total_products FROM product ORDER BY product_id ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query))
        {
            ResultSet rs = stmt.executeQuery(query) ;

            // Process the result set
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int totalProducts = rs.getInt("total_products");

                // Add data to the list as an array of Objects for JTable
                productList.add(new Object[]{productId, totalProducts});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }
    public int getProductRowCount() throws SQLException {
        String query = "SELECT COUNT(*) AS row_count FROM product";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("row_count");
            }
        }
        return 0; // Return 0 if no rows are found or an error occurs
    }

    public int getTotalProductsSum() throws SQLException {
        String query = "SELECT SUM(total_products) AS total_sum FROM product";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_sum");
            }
        }
        return 0; // Return 0 if no products are found or an error occurs
    }
    public Map<String, Integer> getCategorySales(int branchId) {
        Map<String, Integer> categorySales = new HashMap<>();
        int currentYear = LocalDate.now().getYear();  // Get the current year

        // Query to find transactions in the current year, join with the product table, and count total sales per category
        String query = "SELECT p.product_category, COUNT(t.product_id) AS total_sales "
                + "FROM transaction t "
                + "JOIN product p ON t.product_id = p.product_id "
                + "WHERE p.branch_id = ? "
                + "AND YEAR(t.transaction_date) = ? "  // Filter by current year
                + "GROUP BY p.product_category";  // Group by category

        try (Connection connection = DBConnection.getConnection()) {
            if (connection == null || connection.isClosed()) {
                System.err.println("Invalid database connection.");
                return categorySales;
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, branchId);  // Use branchId to filter categories
                statement.setInt(2, currentYear);  // Use the current year to filter transactions

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String category = resultSet.getString("product_category");
                        int totalSales = resultSet.getInt("total_sales");

                        // Store total sales by category name
                        categorySales.put(category, totalSales);

                        // Print the category and total sales to the console for debugging
                        System.out.println("Category: " + category + ", Total Sales: " + totalSales);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching sales data: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to get connection: " + e.getMessage());
        }

        return categorySales;
    }

}