package Controller;

import Model.Bill;
import Model.Employee;
import Service.TransactionService;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController() {
        this.transactionService = new TransactionService();
    }


    public List<Map<String, Object>> generateAndDisplayReport(String period) {

        return transactionService.generateAndDisplayReport(period);
    }

    public double getTotalSalesForCurrentYearBM(int branchId) throws SQLException {
        return transactionService.getTotalSalesForCurrentYearBM(branchId);
    }

    public int getNetProfitForCurrentYearBM(int branchId) throws SQLException {
        return this.transactionService.getNetProfitForCurrentYearBM(branchId);
    }

    public boolean insertTransactionToDatabase(Bill bill, Employee employee) {
        return transactionService.insertTransactionToDatabase(bill, employee);
    }

    public List<Map.Entry<Integer, Integer>> getProductSalesForBranch(int branchId, String periodType, String year) throws SQLException {
        return transactionService.getProductSalesForBranch(branchId, periodType, year);
    }

    public Map<Integer, Double> getAverageSales(int branchId, String periodType, int year) throws Exception {
        return transactionService.getAverageSales(branchId, periodType, year);
    }

    public Map<Integer, Double> getAverageProfits(int branchId, String timePeriod, int year) throws Exception {
        return transactionService.getAverageProfits(branchId, timePeriod, year);
    }

    public List<Integer> fetchActiveBranchIds() {
        return transactionService.fetchActiveBranchIds();
    }

    private void displayProfit(String title, Map<Integer, Double> profit) {
        System.out.println("===== " + title + " =====");
        if (profit.isEmpty()) {
            System.out.println("No profit data found.");
        } else {
            profit.forEach((branchId, totalProfit) ->
                    System.out.printf("Branch ID: %d | Total Profit: %.2f%n", branchId, totalProfit)
            );
        }
        System.out.println("==========================");
    }

    public Map<Integer, Double> fetchProfit(String title) {
        return transactionService.fetchProfit(title);
    }


    public Map<Integer, Double> fetchProfitForDateRange(Date startDate, Date endDate) {
        Map<Integer, Double> profit = transactionService.fetchProfitForDateRange(startDate, endDate);
        displayProfit("Profit for Date Range: " + startDate + " to " + endDate, profit);
        return profit;
    }

    public static double getOverallProfit() {
        TransactionService transactionService = new TransactionService();
        return transactionService.fetchOverallProfit();
    }

    public BigDecimal getTotalSales() {
        return transactionService.getTotalSales();
    }


    private void displaySales(String title, Map<Integer, BigDecimal> sales) {
        System.out.println("===== " + title + " =====");
        if (sales.isEmpty()) {
            System.out.println("No sales data found.");
        } else {
            sales.forEach((branchId, totalSales) ->
                    System.out.printf("Branch ID: %d | Total Sales: %.2f%n", branchId, totalSales)
            );
        }
        System.out.println("==========================");
    }


    public Map<Integer, BigDecimal> fetchSalesForDateRange(Date startDate, Date endDate) {
        Map<Integer, BigDecimal> sales = transactionService.fetchSalesForDateRange(startDate, endDate);
        displaySales("Profit for Date Range: " + startDate + " to " + endDate, sales);
        return sales;
    }

    public static BigDecimal getOverallSales() {
        TransactionService transactionService = new TransactionService();
        return transactionService.fetchOverallSales();
    }

    public Map<Integer, BigDecimal> fetchSales(String selectedTimeRange) {
        return transactionService.fetchSales(selectedTimeRange);
    }
    public double fetchProfitForYear(int year) {


        double profit = transactionService.fetchProfitForYear(year);

        return profit;
    }
}