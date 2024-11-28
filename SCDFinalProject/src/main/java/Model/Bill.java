package Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private List<Product> products = new ArrayList<>();
    private BigDecimal totalBill = BigDecimal.ZERO;

    // Add a product to the bill with specified quantity
    public void addProduct(Product product, int quantity) {
        product.setNoOfProducts(quantity);
        products.add(product);

        // Multiply the product's sale price by quantity and add to the totalBill
        totalBill = totalBill.add(product.getSalesPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    // Method to reset the bill (clear the list of products and total)
    public void resetBill() {
        products.clear();
        totalBill = BigDecimal.ZERO;
    }

    // Getters
    public BigDecimal getTotalBill() {
        return totalBill;
    }

    public List<Product> getProducts() {
        return products;
    }

}
