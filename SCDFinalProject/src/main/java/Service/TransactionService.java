package Service;

import DAO.TransactionDAO;
import Model.Bill;
import Model.Employee;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TransactionService {
    private TransactionDAO transactionDAO;
    public TransactionService() {
        this.transactionDAO=new TransactionDAO();
    }

    // Controller method to handle report generation
    public List<Map<String, Object>> generateAndDisplayReport(String period) {
        List<Map<String, Object>> report = null;
        try {
            // Fetch report data based on period (weekly, monthly, or yearly)
            report = transactionDAO.generateReport(period);

            // Check if report is empty
            if (report.isEmpty()) {
                System.out.println("No transactions found for the specified period: " + period);
            } else {
                // Display the report
                System.out.println("Report for " + period + " sales and profits:");

                // Displaying each product's sales and profit
                for (Map<String, Object> record : report) {
                    System.out.println("Product ID: " + record.get("product_id"));
                    System.out.println("Total Sold: " + record.get("total_sold"));
                    System.out.println("Total Profit: " + record.get("total_profit"));
                    System.out.println("----------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while generating report: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid period specified: " + e.getMessage());
        }
        return report;
    }
    public int getNetProfitForCurrentYearBM(int branchId) throws SQLException {
        return this.transactionDAO.getNetProfitForCurrentYearBM(branchId);
    }
    public double getTotalSalesForCurrentYearBM(int branchId) throws SQLException {
        return transactionDAO.getTotalSalesForCurrentYearBM(branchId);
    }
    public boolean insertTransactionToDatabase(Bill bill, Employee employee) {
        return transactionDAO.insertTransactionToDatabase(bill,employee);
    }


    public List<Map.Entry<Integer, Integer>> getProductSalesForBranch(int branchId, String periodType, String year) throws SQLException {
        return transactionDAO.getProductSalesForBranch(branchId,periodType, year);
    }
    public  Map<Integer, Double> getAverageSales(int branchId, String periodType, int year) throws Exception {
        return transactionDAO.getAverageSales(branchId,periodType,year);
    }

    public Map<Integer, Double> getAverageProfits(int branchId, String timePeriod, int year) throws Exception {
        return transactionDAO.getAverageProfits(branchId,timePeriod,year);
    }
    public List<Integer> fetchActiveBranchIds() {
        return transactionDAO.getBranchIds();
    }

    public Map<Integer, Double> fetchProfit(String title) {
        switch (title) {
            case "Today":
                return transactionDAO.getTodaysProfit();
            case "Weekly":
                return transactionDAO.getWeeklyProfit();
            case "Monthly":
                return transactionDAO.getMonthlyProfit();
            case "Yearly":
                return transactionDAO.getYearlyProfit();
            default:
                throw new IllegalArgumentException("Invalid profit type: " + title);
        }
    }

    public Map<Integer, Double> fetchProfitForDateRange(Date startDate, Date endDate) {
        return transactionDAO.getProfitForDateRange(startDate, endDate);
    }

    public double fetchOverallProfit() {
        return transactionDAO.fetchOverallProfit();
    }

    public BigDecimal getTotalSales() {
        try {
            return transactionDAO.getTotalSales();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch total sales data.");
        }
    }

    public Map<Integer, BigDecimal> fetchSales(String title) {
        switch (title) {
            case "Today":
                return transactionDAO.getTodaysSales();
            case "Weekly":
                return transactionDAO.getWeeklySales();
            case "Monthly":
                return transactionDAO.getMonthlySales();
            case "Yearly":
                return transactionDAO.getYearlySales();
            default:
                throw new IllegalArgumentException("Invalid Sales type: " + title);
        }
    }

    public Map<Integer, BigDecimal> fetchSalesForDateRange(Date startDate, Date endDate) {
        return transactionDAO.getSalesForDateRange(startDate, endDate);
    }

    public BigDecimal fetchOverallSales() {

        return transactionDAO.fetchOverallSales();
    }
    public double fetchProfitForYear(int year) {


        double profit = transactionDAO.fetchProfitForYear(year);

        return profit;
    }
}