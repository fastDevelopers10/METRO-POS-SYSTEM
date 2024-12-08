package DAO;


import Model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static DAO.DBConnection.getConnection;

public class TransactionDAO {

    private static Connection connection;

    // Constructor to initialize the connection
    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to insert a transaction
    public static boolean insertTransactions(int branchId, int productId, int quantitySold, Date transactionDate, BigDecimal profit) throws SQLException {
        String query = "INSERT INTO transaction (branch_id, product_id, quantity_sold, transaction_date, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, branchId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantitySold);
            pstmt.setDate(4, transactionDate);
            pstmt.setBigDecimal(5, profit);
            pstmt.setBoolean(6, true);

            return pstmt.executeUpdate() > 0;  // Returns true if a row is inserted
        }
    }

    // Method to get a transaction by ID
    public Transaction getTransactionById(int transactionId) throws SQLException {
        String query = "SELECT * FROM transaction WHERE transaction_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRowToTransaction(rs);
            }
        }
        return null;  // Return null if no transaction found
    }

    // Method to get all transactions for a specific branch
    public List<Transaction> getTransactionsByBranchId(int branchId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transaction WHERE branch_id = ? ORDER BY transaction_date DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, branchId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        }
        return transactions;
    }

    // Method to update a transaction (e.g., updating profit or status)
    public boolean updateTransaction(int transactionId, BigDecimal profit, boolean status) throws SQLException {
        String query = "UPDATE transaction SET profit = ?, status = ? WHERE transaction_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setBigDecimal(1, profit);
            pstmt.setBoolean(2, status);
            pstmt.setInt(3, transactionId);
            return pstmt.executeUpdate() > 0;  // Returns true if rows are updated
        }
    }

    // Method to delete a transaction
    public boolean deleteTransaction(int transactionId) throws SQLException {
        String query = "DELETE FROM transaction WHERE transaction_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, transactionId);
            return pstmt.executeUpdate() > 0;  // Returns true if a row is deleted
        }
    }

    public static List<Integer> getBranchIds() {
        List<Integer> branchIds = new ArrayList<>();
        String query = "SELECT branch_id FROM branch WHERE status = 'active'";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                branchIds.add(rs.getInt("branch_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return branchIds;
    }


    // Helper method to map ResultSet to a Transaction object
    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        int transactionId = rs.getInt("transaction_id");
        int branchId = rs.getInt("branch_id");
        int productId = rs.getInt("product_id");
        int quantitySold = rs.getInt("quantity_sold");
        Date transactionDate = rs.getDate("transaction_date");
        BigDecimal profit = rs.getBigDecimal("profit");
        boolean status = rs.getBoolean("status");

        return new Transaction(transactionId, branchId, productId, quantitySold, transactionDate, profit, status);
    }

    public static Map<Integer, Double> getTodaysProfit() {
        String query = "SELECT branch_id, SUM(profit) AS total_profit " +
                "FROM transaction " +
                "WHERE transaction_date = CURDATE() AND status = TRUE " +
                "GROUP BY branch_id";
        System.out.println("[DEBUG] Fetching today's profit with query: " + query);
        return fetchProfitData(query);
    }

    // Fetch weekly profit
    public static Map<Integer, Double> getWeeklyProfit() {
        String query = "SELECT branch_id, SUM(profit) AS total_profit " +
                "FROM transaction " +
                "WHERE transaction_date BETWEEN CURDATE() - INTERVAL 7 DAY AND CURDATE() AND status = TRUE " +
                "GROUP BY branch_id";
        System.out.println("[DEBUG] Fetching weekly profit with query: " + query);
        return fetchProfitData(query);
    }

    // Fetch monthly profit
    public static Map<Integer, Double> getMonthlyProfit() {
        String query = "SELECT branch_id, SUM(profit) AS total_profit " +
                "FROM transaction " +
                "WHERE MONTH(transaction_date) = MONTH(CURDATE()) AND YEAR(transaction_date) = YEAR(CURDATE()) AND status = TRUE " +
                "GROUP BY branch_id";
        System.out.println("[DEBUG] Fetching monthly profit with query: " + query);
        return fetchProfitData(query);
    }

    // Fetch yearly profit
    public static Map<Integer, Double> getYearlyProfit() {
        String query = "SELECT branch_id, SUM(profit) AS total_profit " +
                "FROM transaction " +
                "WHERE YEAR(transaction_date) = YEAR(CURDATE()) AND status = TRUE " +
                "GROUP BY branch_id";
        System.out.println("[DEBUG] Fetching yearly profit with query: " + query);
        return fetchProfitData(query);
    }

    // Fetch profit for a specified date range
    public static Map<Integer, Double> getProfitForDateRange(Date startDate, Date endDate) {
        String query = "SELECT branch_id, SUM(profit) AS total_profit " +
                "FROM transaction " +
                "WHERE transaction_date BETWEEN ? AND ? AND status = TRUE " +
                "GROUP BY branch_id";
        System.out.println("[DEBUG] Fetching profit for date range from " + startDate + " to " + endDate + " with query: " + query);

        Map<Integer, Double> result = new HashMap<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int branchId = rs.getInt("branch_id");
                double totalProfit = rs.getDouble("total_profit");
                System.out.printf("[DEBUG] Branch ID: %d | Total Profit: %.2f%n", branchId, totalProfit);
                result.put(branchId, totalProfit);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error fetching profit for date range: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // Common method to fetch profit data
    private static Map<Integer, Double> fetchProfitData(String query) {
        System.out.println("[DEBUG] Executing fetchProfitData with query: " + query);

        Map<Integer, Double> result = new HashMap<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int branchId = rs.getInt("branch_id");
                double totalProfit = rs.getDouble("total_profit");
                System.out.printf("[DEBUG] Branch ID: %d | Total Profit: %.2f%n", branchId, totalProfit);
                result.put(branchId, totalProfit);
            }
            System.out.println("[DEBUG] fetchProfitData execution complete. Result: " + result);
        } catch (SQLException e) {
            System.err.println("[ERROR] Error executing fetchProfitData: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    public static Map<Integer, Double> FetchProfitForDateRange(Date startDate, Date endDate) {
        String query = "SELECT branch_id, SUM(profit) AS total_profit " +
                "FROM transaction " +
                "WHERE transaction_date BETWEEN ? AND ? AND status = TRUE " +
                "GROUP BY branch_id";
        System.out.println("[DEBUG] Fetching profit for date range from " + startDate + " to " + endDate + " with query: " + query);

        Map<Integer, Double> result = new HashMap<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the start and end date parameters dynamically at runtime
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int branchId = rs.getInt("branch_id");
                double totalProfit = rs.getDouble("total_profit");
                System.out.printf("[DEBUG] Branch ID: %d | Total Profit: %.2f%n", branchId, totalProfit);
                result.put(branchId, totalProfit);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error fetching profit for date range: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
