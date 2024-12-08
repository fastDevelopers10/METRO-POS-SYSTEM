package Service;


import java.sql.Date;
import java.util.Map;

import static DAO.TransactionDAO.*;


public class ProfitService {
    public Map<Integer, Double> TodaysProfit() {
        return getTodaysProfit();
    }

    public Map<Integer, Double> WeeklyProfit() {
        return getWeeklyProfit();
    }

    public Map<Integer, Double> MonthlyProfit() {
        return getMonthlyProfit();
    }

    public Map<Integer, Double> YearlyProfit() {
        return getYearlyProfit();
    }

    public Map<Integer, Double> ProfitForDateRange(Date startDate, Date endDate) {
        return FetchProfitForDateRange(startDate, endDate);
    }



}
