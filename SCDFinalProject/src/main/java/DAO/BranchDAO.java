package DAO;

import Model.Branch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAO {

    private static final String GET_ALL_BRANCHES_QUERY = "SELECT * FROM branch";
    private static final String GET_ALL_BRANCH_IDS_QUERY = "SELECT branch_id FROM branch";
    private static final String ADD_BRANCH_QUERY = "INSERT INTO branch (city, name, status, address, phone, no_of_employees) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_BRANCH_QUERY = "UPDATE branch SET city = ?, name = ?, status = ?, address = ?, phone = ?, no_of_employees = ? WHERE branch_id = ?";

     public List<Branch> getAllBranches() {
        List<Branch> branches = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(GET_ALL_BRANCHES_QUERY);
            rs = preparedStatement.executeQuery();

            // Accessing the ResultSet inside the try block
            while (rs.next()) {
                Branch branch = new Branch(
                        rs.getInt("branch_id"),
                        rs.getString("city"),
                        rs.getString("name"),
                        rs.getString("status"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getInt("no_of_employees")
                );
                System.out.println(branch);
                branches.add(branch);
            }

        } catch (SQLException ex) {
            System.err.println("SQL Error while retrieving branches: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Unexpected error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Ensuring resources are properly closed
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error while closing resources: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        return branches;
    }




    public List<Integer> getAllBranchIds() {
        List<Integer> branchIds = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BRANCH_IDS_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                branchIds.add(resultSet.getInt("branch_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return branchIds;
    }

    public boolean addBranch(Branch branch) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_BRANCH_QUERY)) {

            preparedStatement.setString(1, branch.getCity());
            preparedStatement.setString(2, branch.getName());
            preparedStatement.setString(3, branch.getStatus());
            preparedStatement.setString(4, branch.getAddress());
            preparedStatement.setString(5, branch.getPhone());
            preparedStatement.setInt(6, branch.getNumberOfEmployees());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateBranch(Branch branch) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BRANCH_QUERY)) {

            preparedStatement.setString(1, branch.getCity());
            preparedStatement.setString(2, branch.getName());
            preparedStatement.setString(3, branch.getStatus());
            preparedStatement.setString(4, branch.getAddress());
            preparedStatement.setString(5, branch.getPhone());
            preparedStatement.setInt(6, branch.getNumberOfEmployees());
            preparedStatement.setInt(7, branch.getBranchId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Branch> fetchBranchesByStatus(String status) {
        List<Branch> result = new ArrayList<>();
        for (Branch branch : getAllBranches()) {
            if (branch.getStatus().equalsIgnoreCase(status)) {
                result.add(branch);
            }
        }
        return result;
    }
}