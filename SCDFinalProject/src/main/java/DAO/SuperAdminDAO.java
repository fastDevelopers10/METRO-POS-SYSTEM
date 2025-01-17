package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SuperAdminDAO {
    private String username;
    private String password;

    public boolean validateLogin(String username, String password) {
        boolean isValid = false;
        String query = "SELECT * FROM super_admin WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.username = rs.getString("username");
                this.password=rs.getString("password");
                isValid = true;  // Login successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }
    public boolean updatePassword(String username, String newPassword) {
        String query = "UPDATE super_admin SET password = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}