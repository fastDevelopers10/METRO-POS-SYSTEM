package Model;

import java.math.BigDecimal;

public class Product {
    private String name;
    private String category;
    private BigDecimal originalPrice;
    private BigDecimal salesPrice;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;  // This method updates the product's quantity
    }

    // Constructor
    public Product(String name, String category, BigDecimal originalPrice, BigDecimal salesPrice, int noOfProducts) {
        this.name = name;
        this.category = category;
        this.originalPrice = originalPrice;
        this.salesPrice = salesPrice;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }



    public BigDecimal getPrice() {
        return originalPrice;
    }
}
