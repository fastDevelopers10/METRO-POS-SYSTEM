package Model;

import java.math.BigDecimal;

public class Product {
    private Branch branch; // Now using Branch object instead of branchId
    private String name;
    private String category;
    private BigDecimal originalPrice;
    private BigDecimal salesPrice;
    private int quantity;
    private boolean status;

    // Constructor
    public Product(Branch branch, String name, String category, BigDecimal originalPrice, BigDecimal salesPrice, int quantity, boolean status) {
        this.branch = branch;
        this.name = name;
        this.category = category;
        this.originalPrice = originalPrice;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters and Setters
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // toString() Method for debugging and logging
    @Override
    public String toString() {
        return "Product{" +
                "branch=" + branch +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", originalPrice=" + originalPrice +
                ", salesPrice=" + salesPrice +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }
}
