package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/METRO_POS_System";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // This method will create and return a new connection each time it's called
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to create the database and tables if they don't already exist
    public static void createDatabaseAndTables() {
        try (Connection connection = getConnection(); Statement stmt = connection.createStatement()) {
            // Create the database if it doesn't exist
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS METRO_POS_System");
            stmt.executeUpdate("USE METRO_POS_System");

            // Create super_admin table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS super_admin (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "username VARCHAR(50) NOT NULL, " +
                            "password VARCHAR(50) NOT NULL)"
            );

            // Insert initial values into super_admin table
            stmt.executeUpdate(
                    "INSERT IGNORE INTO super_admin (username, password) VALUES " +
                            "('aleena', '1001'), " +
                            "('sumayya', '1002'), " +
                            "('anas', '1003')"
            );

            // Create branch table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS branch (" +
                            "branch_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "city VARCHAR(50) NOT NULL, " +
                            "name VARCHAR(50) NOT NULL, " +
                            "status ENUM('active', 'closed') NOT NULL, " +
                            "address VARCHAR(255) NOT NULL, " +
                            "phone VARCHAR(20), " +
                            "no_of_employees INT DEFAULT 0)"
            );

            // Create product table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS inventory (" +
                            "product_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "category VARCHAR(50) NOT NULL, " +
                            "original_price DECIMAL(10, 2) NOT NULL, " +
                            "sales_price DECIMAL(10, 2) NOT NULL, " +
                            "no_of_products INT DEFAULT 0)"
            );

            // Create vendor table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS vendor (" +
                            "vendor_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "phone VARCHAR(20), " +
                            "no_of_cartons INT NOT NULL, " +
                            "no_of_products_in_carton INT NOT NULL, " +
                            "category VARCHAR(50), " +
                            "carton_price DECIMAL(10, 2) NOT NULL" + ")"
            );

            // Create transaction table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS transaction (" +
                            "transaction_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "product_id INT NOT NULL, " +
                            "quantity_sold INT NOT NULL, " +
                            "transaction_date DATE NOT NULL, " +
                            "profit DECIMAL(10, 2), " +
                            "FOREIGN KEY (product_id) REFERENCES inventory(product_id))"
            );

            System.out.println("Database and tables created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
