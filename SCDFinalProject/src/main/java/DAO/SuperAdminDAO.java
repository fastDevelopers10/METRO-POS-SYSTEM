package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class SuperAdminDAO {
    private String username; // Store the username here
    private final Connection connection;


    public SuperAdminDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean validateLogin(String username, String password) throws SQLException {
        boolean isValid = false;
        String query = "SELECT * FROM super_admin WHERE username = ? AND password = ?";


        PreparedStatement stmt = connection.prepareStatement(query);

        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            this.username = rs.getString("username"); // Set the username when login is successful
            isValid = true;  // Login successful
        }
        return isValid;

    }



    public String getUsername() {
        return username;
    }

}