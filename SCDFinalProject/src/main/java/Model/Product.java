package Model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {
    private int productId;  // Product ID field (mapped to the DB auto-increment column)
    private Branch branch;  // Branch object (instead of just branchId)
    private String name;
    private String category;
    private int quantity;
    private BigDecimal originalPrice;
    private BigDecimal salesPrice;
    private boolean status;
    private static String tax="0.17";
    // Constructor
    public Product(int productId, Branch branch, String name, String category,
                   BigDecimal originalPrice, BigDecimal salesPrice, int quantity, boolean status) {
        this.productId = productId;
        this.branch = branch;
        this.name = name;
        this.category = category;
        this.originalPrice = originalPrice;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // toString() method for debugging/logging
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", branch=" + branch +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", originalPrice=" + originalPrice +
                ", salesPrice=" + salesPrice +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }

    // Calculate Profit method: Sales price minus 17% tax and the original price
    public BigDecimal calculateProfit() {
        BigDecimal taxRate = new BigDecimal(tax);  // 17% tax
        BigDecimal salesAfterTax = salesPrice.subtract(salesPrice.multiply(taxRate)); // Sales price after tax
        BigDecimal profit = salesAfterTax.subtract(originalPrice); // Profit = after-tax sales price - original price

        // Round the result to two decimal places (standard currency rounding)
        profit = profit.setScale(2, RoundingMode.HALF_UP);

        return profit;
    }

}
