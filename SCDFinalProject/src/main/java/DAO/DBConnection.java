package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static Connection connection;

    private DBConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/",
                        "root", "");
                createDatabaseAndTables();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    private static void createDatabaseAndTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create database if not exists
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
                    "CREATE TABLE IF NOT EXISTS product (" +
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


            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS transaction (" +
                            "transaction_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "product_id INT NOT NULL, " +
                            "quantity_sold INT NOT NULL, " +
                            "transaction_date DATE NOT NULL, " +
                            "profit DECIMAL(10, 2), " +
                            "FOREIGN KEY (product_id) REFERENCES product(product_id))"
            );


            System.out.println("Database and tables created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}