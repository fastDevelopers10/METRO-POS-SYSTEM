package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

public class BranchDAO {

    private static final String GET_ALL_BRANCH_IDS_QUERY = "SELECT branch_id FROM branch";

    // Modify this method to extract all branch IDs from the database
    public List<Integer> getAllBranchIds() {
        List<Integer> branchIds = new ArrayList<>();
        String query = "SELECT branch_id FROM branch"; // Adjust your query to match the table structure

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                branchIds.add(resultSet.getInt("branch_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return branchIds; // Return the list of branch IDs
    }

}
