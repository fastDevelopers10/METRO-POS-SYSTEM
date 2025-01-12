package DAO;


import Model.Bill;
import Model.Employee;
import Model.Product;
import Model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static DAO.DBConnection.getConnection;

public class TransactionDAO {

    private static Connection connection;

    // Constructor to initialize the connection
    public TransactionDAO() {
        this.connection = connection;
    }

     public double getTotalSalesForCurrentYearBM(int branchId) throws SQLException {
        String query = "SELECT SUM(t.quantity_sold) AS total_sales " +
                "FROM transaction t " +
                "WHERE t.branch_id = ? " +
                "AND YEAR(t.transaction_date) = YEAR(CURDATE())";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_sales");
            }
        }
        return 0.0;
    }


    public int getNetProfitForCurrentYearBM(int branchId) throws SQLException {
        String query = "SELECT SUM(t.profit) AS net_profit " +
                "FROM transaction t " +
                "WHERE t.branch_id = ? " +
                "AND YEAR(t.transaction_date) = YEAR(CURDATE())";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("net_profit");
            }
        }
        return 0;  // Return 0.0 if no profit
    }
    public List<Map.Entry<Integer, Integer>> getProductSalesForBranch(int branchId, String periodType, String year) throws SQLException {
        // Base query
        String query = "SELECT t.product_id, SUM(t.quantity_sold) AS total_sales " +
                "FROM transaction t " +
                "WHERE t.branch_id = ? " +
                "AND YEAR(t.transaction_date) = ? ";

        // Modify query based on period type
        switch (periodType.toLowerCase()) {
            case "monthly":
                query += "AND MONTH(t.transaction_date) = MONTH(CURDATE()) ";
                break;
            case "weekly":
                query += "AND WEEK(t.transaction_date) = WEEK(CURDATE()) ";
                break;
            case "yearly":
                // No additional filter required for yearly
                break;
            default:
                throw new IllegalArgumentException("Invalid period type: " + periodType);
        }

        query += "GROUP BY t.product_id ORDER BY total_sales DESC"; // Group by product_id and order by sales

        // List to store results
        List<Map.Entry<Integer, Integer>> salesData = new ArrayList<>();

        // Execute query
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            stmt.setString(2, year);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int totalSales = rs.getInt("total_sales");
                salesData.add(Map.entry(productId, totalSales));
                System.out.println(productId+" "+totalSales);
            }
        }
        System.out.println(salesData);
        return salesData; // Return the list of product IDs and sales
    }

    // Method to get average sales (quantity sold) for each product in a branch, by period (weekly, monthly, yearly), and year
    public  Map<Integer, Double> getAverageSales(int branchId, String periodType, int year) throws Exception {
        String query = buildQuery(periodType);

        Map<Integer, Double> productSalesMap = new HashMap<>();
        try (Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters: branch_id and year
            statement.setInt(1, branchId);
            statement.setInt(2, year);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Get product_id and total sales for each product
                    int productId = resultSet.getInt("product_id");
                    double totalSales = resultSet.getDouble("total_sales");
                    int periodCount = resultSet.getInt("period_count");

                    // Calculate average sales for each product
                    double averageSales = periodCount > 0 ? totalSales / periodCount : 0;

                    // Store the result in a map with product_id as key and average sales as value
                    productSalesMap.put(productId, averageSales);
                }
            }
        }
        return productSalesMap;  // Return the map containing average sales for each product
    }

    // Build query based on the period type (weekly, monthly, or yearly)
    private static String buildQuery(String periodType) {
        String query = "SELECT product_id, SUM(quantity_sold) AS total_sales, COUNT(DISTINCT %s(transaction_date)) AS period_count "
                + "FROM transaction WHERE branch_id = ? AND YEAR(transaction_date) = ? AND status = 'completed' GROUP BY product_id";

        switch (periodType) {
            case "weekly":
                return String.format(query, "WEEK");
            case "monthly":
                return String.format(query, "MONTH");
            case "yearly":
                return String.format(query, "YEAR");
            default:
                throw new IllegalArgumentException("Invalid period type: " + periodType);
        }
    }

    // Method to get average profits for each product in a branch, by period (weekly, monthly, yearly), and year
    public Map<Integer, Double> getAverageProfits(int branchId, String periodType, int year) throws Exception {
        String query = buildProfitQuery(periodType);

        Map<Integer, Double> productProfitsMap = new HashMap<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters: branch_id and year
            statement.setInt(1, branchId);
            statement.setInt(2, year);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Get product_id and total profit for each product
                    int productId = resultSet.getInt("product_id");
                    double totalProfit = resultSet.getDouble("total_profit");
                    int periodCount = resultSet.getInt("period_count");

                    // Calculate average profit for each product
                    double averageProfit = periodCount > 0 ? totalProfit / periodCount : 0;

                    // Store the result in a map with product_id as key and average profit as value
                    productProfitsMap.put(productId, averageProfit);
                }
            }
        }
        return productProfitsMap;  // Return the map containing average profits for each product
    }

    // Build query based on the period type (weekly, monthly, or yearly) for profit data
    private static String buildProfitQuery(String periodType) {
        String query = "SELECT product_id, SUM(profit) AS total_profit, COUNT(DISTINCT %s(transaction_date)) AS period_count "
                + "FROM transaction WHERE branch_id = ? AND YEAR(transaction_date) = ? AND status = 'completed' GROUP BY product_id";

        switch (periodType) {
            case "weekly":
                return String.format(query, "WEEK");
            case "monthly":
                return String.format(query, "MONTH");
            case "yearly":
                return String.format(query, "YEAR");
            default:
                throw new IllegalArgumentException("Invalid period type: " + periodType);
        }
    }




    // Method to generate reports based on time period
        public List<Map<String, Object>> generateReport(String period) throws SQLException {
            // Base SQL query to get product-wise sales and profit
            String query = "";
            String dateCondition = "";

            // Determine the date range based on period
            switch (period.toLowerCase()) {
                case "weekly":
                    dateCondition = "WHERE transaction_date >= CURDATE() - INTERVAL 1 WEEK";
                    break;
                case "monthly":
                    dateCondition = "WHERE transaction_date >= CURDATE() - INTERVAL 1 MONTH";
                    break;
                case "yearly":
                    dateCondition = "WHERE transaction_date >= CURDATE() - INTERVAL 1 YEAR";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid period. Please specify 'weekly', 'monthly', or 'yearly'.");
            }

            // Query to fetch product sales and profit
            query = "SELECT product_id, SUM(quantity_sold) AS total_sold, SUM(profit) AS total_profit " +
                    "FROM transaction " + dateCondition + " " +
                    "GROUP BY product_id";

            // Prepare statement and execute
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                List<Map<String, Object>> reports = new ArrayList<>();

                // Iterate over result set and collect data
                while (rs.next()) {
                    Map<String, Object> report = new HashMap<>();
                    report.put("product_id", rs.getInt("product_id"));
                    report.put("total_sold", rs.getInt("total_sold"));
                    report.put("total_profit", rs.getBigDecimal("total_profit"));
                    reports.add(report);
                }

                return reports;
            }
        }


    public boolean insertTransactionToDatabase(Bill bill, Employee employee) {
        try {
            // Get the branch ID from the employee object
            int branchId = employee.getBranchId();
            System.out.println(bill.getCart());
            // Get the current date for the transaction
            Date transactionDate = new Date(System.currentTimeMillis());

            // Iterate through the cart and insert each product as a transaction
            for (Map.Entry<Product, Integer> entry : bill.getCart().entrySet()) {
                Product product = entry.getKey();
                int quantitySold = entry.getValue();
                System.out.println(product+" "+quantitySold);
                // Calculate profit as the difference between the original price and sales price
                BigDecimal profitPerUnit = product.getSalesPrice().subtract(product.getOriginalPrice());
                BigDecimal totalProfit = profitPerUnit.multiply(BigDecimal.valueOf(quantitySold));
                System.out.println(totalProfit);
                // Status can be set dynamically; for now, setting as "Completed"
                boolean status = true;

                // Prepare the SQL query
                String sql = "INSERT INTO transaction (branch_id, product_id, quantity_sold, transaction_date, profit, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                // Execute the query
                try (Connection connection = DBConnection.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, branchId);
                    preparedStatement.setInt(2, product.getId());
                    preparedStatement.setInt(3, quantitySold);
                    preparedStatement.setDate(4, transactionDate);
                    preparedStatement.setBigDecimal(5, totalProfit);
                    preparedStatement.setBoolean(6, status);

                    // Execute the insertion
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted <= 0) {
                        System.err.println("Failed to insert transaction for product ID: " + product.getId());
                        return false; // Stop if any transaction fails
                    }
                }
            }

            return true; // Return true if all transactions were inserted successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
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
    // Fetch profit for a specific year based on the given parameter (e.g., 0 for current year, 1 for last year)
    public double fetchProfitForYear(int years) {
        double profit = 0.0;

        // Get the current year
        // Calculate the year based on the number of years ago
        // SQL query to fetch profit data for the calculated year
        String query = "SELECT SUM(profit) AS total_profit " +
                "FROM transaction " +
                "WHERE YEAR(transaction_date) = ? AND status = TRUE";

        // Connect to the database and execute the query
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, years);  // Set the calculated year in the query

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    profit = resultSet.getDouble("total_profit");  // Get the total profit for the year
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return profit;
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
