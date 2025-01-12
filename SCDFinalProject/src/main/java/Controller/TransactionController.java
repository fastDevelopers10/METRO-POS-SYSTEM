package Controller;

import Model.Bill;
import Model.Employee;
import Service.TransactionService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TransactionController {
    private TransactionService transactionService;

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
        return transactionService.insertTransactionToDatabase(bill,employee);
    }


    public List<Map.Entry<Integer, Integer>> getProductSalesForBranch(int branchId, String periodType, String year) throws SQLException
    {
        return transactionService.getProductSalesForBranch(branchId, periodType,year);
    }
    public  Map<Integer, Double> getAverageSales(int branchId, String periodType, int year) throws Exception {
        return transactionService.getAverageSales(branchId,periodType,year);
    }

    public Map<Integer, Double> getAverageProfits(int branchId, String timePeriod, int year) throws Exception {
        return transactionService.getAverageProfits(branchId,timePeriod,year);
    }
}