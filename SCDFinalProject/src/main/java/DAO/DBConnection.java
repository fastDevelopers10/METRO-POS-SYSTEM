package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);


                Statement stmt = connection.createStatement();
                createDatabaseAndTables();            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void createDatabaseAndTables() {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS METRO_POS_SYSTEM;");
            stmt.executeUpdate("USE METRO_POS_SYSTEM;");


            createSuperAdminTable(stmt);
            createBranchTable(stmt);
            createVendorTable(stmt);
            createProductTable(stmt);
            createTransactionTable(stmt);
            createVendorProductTable(stmt);
            createEmployeeTable(stmt);

            System.out.println("Tables and triggers created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createSuperAdminTable(Statement stmt) throws SQLException {
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS super_admin (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(50) NOT NULL, " +
                        "password VARCHAR(50) NOT NULL" +
                        ")"
        );
    }

    private static void createBranchTable(Statement stmt) throws SQLException {
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS branch (" +
                        "branch_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "city VARCHAR(50) NOT NULL, " +
                        "name VARCHAR(50) NOT NULL, " +
                        "status ENUM('active', 'closed') NOT NULL, " +
                        "address VARCHAR(255) NOT NULL, " +
                        "phone VARCHAR(20), " +
                        "no_of_employees INT DEFAULT 0" +
                        ")"
        );
    }

    private static void createVendorTable(Statement stmt) throws SQLException {
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS vendor (" +
                        "vendor_id INT AUTO_INCREMENT PRIMARY KEY, " +  // Unique ID for the vendor
                        "name VARCHAR(100) NOT NULL, " +  // Vendor name (required field)
                        "phone VARCHAR(20), " +  // Phone number of the vendor
                        "status BOOLEAN DEFAULT TRUE" +  // Status to indicate if the vendor is active
                        ")"
        );
    }

    private static void createProductTable(Statement stmt) throws SQLException {
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS product (" +
                        "product_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "branch_id INT NOT NULL, " +
                        "product_name VARCHAR(100) NOT NULL, " +
                        "product_category VARCHAR(50) NOT NULL, " +
                        "total_products INT DEFAULT 0, " +
                        "original_price DECIMAL(10, 2), " +
                        "sales_price DECIMAL(10, 2), " +
                        "status BOOLEAN DEFAULT TRUE, " +
                        "FOREIGN KEY (branch_id) REFERENCES branch(branch_id) " +

                        ")"
        );
    }

    private static void createTransactionTable(Statement stmt) throws SQLException {
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS transaction (" +
                        "transaction_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "branch_id INT NOT NULL, " +
                        "product_id INT NOT NULL, " +
                        "quantity_sold INT NOT NULL, " +
                        "transaction_date DATE NOT NULL, " +
                        "profit DECIMAL(10, 2), " +
                        "status BOOLEAN DEFAULT TRUE, " +
                        "FOREIGN KEY (branch_id) REFERENCES branch(branch_id), " +
                        "FOREIGN KEY (product_id) REFERENCES product(product_id)" +
                        ")"
        );
    }

    private static void createVendorProductTable(Statement stmt) throws SQLException {
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS vendor_product (" +
                        "relation_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "vendor_id INT NOT NULL, " +
                        "branch_id INT NOT NULL, " +
                        "product_id INT, " +
                        "product_name VARCHAR(100) NOT NULL, " +
                        "product_category VARCHAR(50) NOT NULL, " +
                        "cartons_purchased INT NOT NULL, " +
                        "items_per_carton INT NOT NULL, " +
                        "products_purchased INT AS (cartons_purchased * items_per_carton) STORED, " +
                        "original_price DECIMAL(10, 2) NOT NULL, " +
                        "sales_price DECIMAL(10, 2) NOT NULL, " +
                        "purchase_date DATE NOT NULL, " +
                        "status BOOLEAN DEFAULT TRUE, " +
                        "FOREIGN KEY (branch_id) REFERENCES branch(branch_id), " +
                        "FOREIGN KEY (vendor_id) REFERENCES vendor(vendor_id), " +
                        "FOREIGN KEY (product_id) REFERENCES product(product_id)" +
                        ")"
        );
    }

    private static void createEmployeeTable(Statement stmt) throws SQLException {
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS employee (\n" +
                        "    employee_id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "    name VARCHAR(100) NOT NULL,\n" +
                        "    position ENUM('Branch Manager', 'Cashier', 'Data Operator') NOT NULL,\n" +
                        "    email VARCHAR(100),\n" +
                        "    branch_id INT,\n" +
                        "    address VARCHAR(255),\n" +
                        "    phone_number VARCHAR(15),\n" +
                        "    salary DECIMAL(10, 2),\n" +
                        "    joining_date DATE DEFAULT CURRENT_DATE,\n" +
                        "username VARCHAR(50) UNIQUE NOT NULL DEFAULT 'User', " +
                        "password VARCHAR(50) UNIQUE NOT NULL DEFAULT '12345', " +
                        "    status ENUM('active', 'inactive') DEFAULT 'active',\n" +
                        "    first_time_joined BOOLEAN DEFAULT TRUE,\n" +
                        "    FOREIGN KEY (branch_id) REFERENCES branch(branch_id)\n" +
                        ") ENGINE=InnoDB;\n"
        );
    }


    // Main method to initialize the DB connection and setup
    public static void main(String[] args) {
        System.out.println("Initializing database setup...");

        // Get the connection, which will trigger database and table creation
        try (Connection connection = DBConnection.getConnection()) {
            if (connection != null) {

                System.out.println("Database setup complete. Connection established successfully.");
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}