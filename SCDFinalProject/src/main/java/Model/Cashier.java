package Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cashier extends Employee {
    private List<Product> products;
    private BigDecimal totalBill;

    public Cashier(String employeeId, String name, String email, String password) {
        super(employeeId, name, "Cashier", email, password);
        this.products = new ArrayList<>();
        this.totalBill = BigDecimal.ZERO; // Initializing totalBill as BigDecimal.ZERO
    }

    public void addProductToBill(Product product, int quantity) {
        products.add(product);
        // Multiply product's sales price by quantity and add to totalBill
        totalBill = totalBill.add(product.getSalesPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    // Get the list of products in the bill
    public List<Product> getProducts() {
        return products;
    }

    // Get total bill amount
    public BigDecimal getTotalBill() {
        return totalBill;
    }

    // Method to process the bill (this could involve printing it or any other necessary action)
    public void processBill() {
        System.out.println(getName() + " is processing the bill...");
        System.out.println("Total Bill: $" + totalBill);
    }
}
