package DAO;
import Model.Vendor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorDAO {

    private Connection connection;

    public VendorDAO() {
        // Assume DBConnection.getConnection() is a method that gives a database connection
        this.connection = DBConnection.getConnection();
    }

    // Method to fetch all vendors from the database
    public List<Vendor> getAllVendors() {
        List<Vendor> vendors = new ArrayList<>();
        String query = "SELECT * FROM vendor";  // SQL query to get all vendors

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                int vendorId = resultSet.getInt("vendor_id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                boolean status = resultSet.getBoolean("status");

                // Create Vendor object and add to list
                Vendor vendor = new Vendor(vendorId, name, phone, status);
                vendors.add(vendor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendors;
    }

    // Method to update a vendor in the database
    public boolean updateVendor(Vendor vendor) {
        String query = "UPDATE vendor SET name = ?, phone = ?, status = ? WHERE vendor_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vendor.getName());
            stmt.setString(2, vendor.getPhone());
            stmt.setBoolean(3, vendor.isStatus());
            stmt.setInt(4, vendor.getVendorId());

            return stmt.executeUpdate() > 0;  // Returns true if vendor updated successfully
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean addVendor(Vendor vendor) {
        String query = "INSERT INTO vendor (name, phone, status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vendor.getName());
            stmt.setString(2, vendor.getPhone());
            stmt.setBoolean(3, vendor.isStatus());
            return stmt.executeUpdate() > 0; // Returns true if a row was inserted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
