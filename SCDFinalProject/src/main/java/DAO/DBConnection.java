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
                            "password VARCHAR(50) NOT NULL" +
                            ")"
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
                            "no_of_employees INT DEFAULT 0, " +
                            "status BOOLEAN DEFAULT TRUE" +
                            ")"
            );

            // Create employee table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS employee (" +
                            "employee_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "password VARCHAR(50) UNIQUE NOT NULL, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "position VARCHAR(50) NOT NULL, " +
                            "status BOOLEAN DEFAULT TRUE, " +
                            "first_time_joined BOOLEAN DEFAULT TRUE" +
                            ")"
            );

            // Create vendor table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS vendor (" +
                            "vendor_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "phone VARCHAR(20), " +
                            "status BOOLEAN DEFAULT TRUE" +
                            ")"
            );

            // Create product table
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
                            "FOREIGN KEY (branch_id) REFERENCES branch(branch_id), " +
                            "UNIQUE(product_name, product_category)" +
                            ")"
            );

            // Create vendor_product table
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
                            "products_purchased INT NOT NULL, " + // Fixed to avoid GENERATED syntax issues
                            "original_price DECIMAL(10, 2) NOT NULL, " +
                            "sales_price DECIMAL(10, 2) NOT NULL, " +
                            "purchase_date DATE NOT NULL, " +
                            "status BOOLEAN DEFAULT TRUE, " +
                            "FOREIGN KEY (branch_id) REFERENCES branch(branch_id), " +
                            "FOREIGN KEY (vendor_id) REFERENCES vendor(vendor_id), " +
                            "FOREIGN KEY (product_id) REFERENCES product(product_id)" +
                            ")"
            );

            // Create transaction table
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

            // Trigger: Update Product Table After Vendor Product Insert
            stmt.executeUpdate(
                    "DROP TRIGGER IF EXISTS update_product_on_purchase;" +
                            "CREATE TRIGGER update_product_on_purchase " +
                            "AFTER INSERT ON vendor_product " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "DECLARE prod_id INT; " +
                            "SELECT product_id INTO prod_id " +
                            "FROM product " +
                            "WHERE product_name = NEW.product_name AND product_category = NEW.product_category; " +
                            "IF prod_id IS NULL THEN " +
                            "INSERT INTO product (product_name, product_category, total_products, original_price, sales_price, status) " +
                            "VALUES (NEW.product_name, NEW.product_category, NEW.products_purchased, NEW.original_price, NEW.sales_price, TRUE); " +
                            "SET prod_id = LAST_INSERT_ID(); " +
                            "ELSE " +
                            "UPDATE product " +
                            "SET total_products = total_products + NEW.products_purchased " +
                            "WHERE product_id = prod_id; " +
                            "END IF; " +
                            "UPDATE vendor_product " +
                            "SET product_id = prod_id " +
                            "WHERE relation_id = NEW.relation_id; " +
                            "END"
            );

            // Trigger: Update Product Quantity After a Sale
            stmt.executeUpdate(
                    "CREATE TRIGGER update_product_on_sale " +
                            "AFTER INSERT ON transaction " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "UPDATE product " +
                            "SET total_products = total_products - NEW.quantity_sold " +
                            "WHERE product_id = NEW.product_id; " +
                            "END"
            );

            System.out.println("Database and tables created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


        public static void main(String[] args) {
            // Establish the database connection and create the database/tables
            try {
                DBConnection.getConnection();
                System.out.println("Database connection established and tables initialized.");
            } catch (Exception e) {
                System.err.println("An error occurred during database initialization.");
                e.printStackTrace();
            }
        }
}
