package SCDFinalProject.src.main.java.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static SCDFinalProject.src.main.java.DAO.DBConnection.getConnection;


public class SuperAdmin {
    private String username; // Store the username here

    public boolean validateLogin(String username, String password) {
        boolean isValid = false;
        String query = "SELECT * FROM super_admin WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.username = rs.getString("username"); // Set the username when login is successful
                isValid = true;  // Login successful
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public String getUsername() {
        return username;
    }
}
