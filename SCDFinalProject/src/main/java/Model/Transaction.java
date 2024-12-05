package Model;

import java.math.BigDecimal;
import java.sql.Date;

public class Transaction {

    private int transactionId;  // Corresponds to transaction_id in the table
    private int branchId;       // Corresponds to branch_id in the table
    private int productId;      // Corresponds to product_id in the table
    private int quantitySold;   // Corresponds to quantity_sold in the table
    private Date transactionDate; // Corresponds to transaction_date in the table
    private BigDecimal profit;  // Corresponds to profit in the table
    private boolean status;     // Corresponds to status in the table

    // Constructor to initialize the Transaction object
    public Transaction(int transactionId, int branchId, int productId, int quantitySold, Date transactionDate, BigDecimal profit, boolean status) {
        this.transactionId = transactionId;
        this.branchId = branchId;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.transactionDate = transactionDate;
        this.profit = profit;
        this.status = status;
    }

    // Getters and setters for each field

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Optional: Override toString() to display object details
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", branchId=" + branchId +
                ", productId=" + productId +
                ", quantitySold=" + quantitySold +
                ", transactionDate=" + transactionDate +
                ", profit=" + profit +
                ", status=" + status +
                '}';
    }
}
