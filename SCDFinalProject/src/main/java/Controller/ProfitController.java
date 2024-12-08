package Controller;

import Service.ProfitService;
import static DAO.TransactionDAO.getBranchIds;


import java.sql.Date;
import java.util.List;
import java.util.Map;


public class ProfitController {

    private final ProfitService profitService;

    public ProfitController() {
        this.profitService = new ProfitService();
    }

    public void displayTodaysProfit() {
        Map<Integer, Double> profit = profitService.TodaysProfit();
        displayProfit("Today's Profit", profit);
    }

    public void displayWeeklyProfit() {
        Map<Integer, Double> profit = profitService.WeeklyProfit();
        displayProfit("Weekly Profit", profit);
    }

    public void displayMonthlyProfit() {
        Map<Integer, Double> profit = profitService.MonthlyProfit();
        displayProfit("Monthly Profit", profit);
    }

    public void displayYearlyProfit() {
        Map<Integer, Double> profit = profitService.YearlyProfit();
        displayProfit("Yearly Profit", profit);
    }

    public Map<Integer, Double> displayProfitForDateRange(Date startDate, Date endDate) {
        Map<Integer, Double> profit = profitService.ProfitForDateRange(startDate, endDate);
        displayProfit("Profit for Date Range: " + startDate + " to " + endDate, profit);
        return profit;
    }
    public List<Integer> fetchActiveBranchIds() {
        return getBranchIds();
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
        switch (title) {
            case "Today's Profit":
                return profitService.TodaysProfit();
            case "Weekly Profit":
                return profitService.WeeklyProfit();
            case "Monthly Profit":
                return profitService.MonthlyProfit();
            case "Yearly Profit":
                return profitService.YearlyProfit();
            default:
                throw new IllegalArgumentException("Invalid profit type: " + title);
        }
    }

    public Map<Integer, Double> fetchProfitForDateRange(Date startDate, Date endDate) {
        Map<Integer, Double> profit = profitService.ProfitForDateRange(startDate, endDate);
        displayProfit("Profit for Date Range: " + startDate + " to " + endDate, profit);
        return profit;
    }


}
