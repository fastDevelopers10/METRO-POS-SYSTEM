package Model;

import java.math.BigDecimal;

public class Product {
    private String name;
    private String category;
    private BigDecimal originalPrice;
    private BigDecimal salesPrice;
    private int noOfProducts;

    // Constructor
    public Product(String name, String category, BigDecimal originalPrice, BigDecimal salesPrice, int noOfProducts) {
        this.name = name;
        this.category = category;
        this.originalPrice = originalPrice;
        this.salesPrice = salesPrice;
        this.noOfProducts = noOfProducts;
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

    public int getNoOfProducts() {
        return noOfProducts;
    }

    // Setter for noOfProducts (note the correct naming convention)
    public void setNoOfProducts(int noOfProducts) {
        this.noOfProducts = noOfProducts;
    }

    public BigDecimal getPrice() {
        return originalPrice;

    }
}
