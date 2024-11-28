package Controller;

import java.sql.*;

public class SuperAdmin {
    private static int idCounter = 1000;  //count for increment in id over each object creation
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    public static Connection conn = null;

    public SuperAdmin(String name, String address, String phoneNumber, String email, String password) {
        this.id = idCounter++;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = name.toLowerCase().replace(" ", "") + id;
        this.password = password;
    }



    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/POSsystem", "root", "");
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // Method to create the SuperAdmin table if it does not already exist
    private static void createTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS SuperAdmin (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100) NOT NULL,
                    address VARCHAR(255),
                    phoneNumber VARCHAR(15),
                    email VARCHAR(100),
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(50) NOT NULL
                );
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("SuperAdmin table found successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveToDatabase() {
        String insertSQL = """
                INSERT INTO SuperAdmin (name, address, phoneNumber, email, username, password)
                VALUES (?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, email);
            pstmt.setString(5, username);
            pstmt.setString(6, password);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("SuperAdmin data saved successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Establish connection and create table if it doesn't exist
        Connection conn = SuperAdmin.connect();

        SuperAdmin admin = new SuperAdmin("sumayya yasin", "123 Main St", "03228976634", "sumayyabintyasin@gmail.com", "sumayya@2024");
        System.out.println("SuperAdmin created with ID: " + admin.getId());
        System.out.println("Username: " + admin.getUsername());

        admin.saveToDatabase();

        // Close the connection (optional but recommended)
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
