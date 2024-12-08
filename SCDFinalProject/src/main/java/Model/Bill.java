package Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bill {
    private Map<Product, Integer> cart = new HashMap<>(); // Cart will hold products and quantities
    public BigDecimal totalBill = BigDecimal.ZERO;
    public BigDecimal subtotal = BigDecimal.ZERO;
    public BigDecimal tax = BigDecimal.ZERO;
    private int branchid;
    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.10); // 10% tax

    // Add a product to the cart with specified quantity
    public boolean addProduct(Product product, int quantity) {
        // Check if the product already exists in the cart, if so, update the quantity
        if (cart.containsKey(product)) {
            quantity += cart.get(product); // Increment the quantity if the product already exists
        }
        cart.put(product, quantity);  // Add or update the product in the cart
        updateBill(); // Recalculate bill after adding/updating the product
        return true;
    }

    // Method to remove a product from the cart
    public void removeProduct(Product product) {
        cart.remove(product);  // Remove the product from the cart
        updateBill(); // Recalculate bill after removal
    }

    // Method to reset the bill (clear the cart and total)
    public void resetBill() {
        cart.clear();  // Clear the cart
        totalBill = BigDecimal.ZERO;
        subtotal = BigDecimal.ZERO;
        tax = BigDecimal.ZERO;
    }

    // Update the bill by recalculating the subtotal, tax, and total
    private void updateBill() {
        subtotal = BigDecimal.ZERO;
        tax = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            subtotal = subtotal.add(product.getSalesPrice().multiply(BigDecimal.valueOf(quantity)));  // Add product subtotal
        }

        tax = subtotal.multiply(TAX_RATE);  // Calculate tax
        totalBill = subtotal.add(tax);  // Total = Subtotal + Tax
    }

    // Getters
    public BigDecimal getTotalBill() {
        return totalBill;
    }

    public Map<Product, Integer> getCart() {
        return cart;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    // Get total quantity of all products in the cart
    public int getQuantity() {
        int totalQuantity = 0;
        for (int quantity : cart.values()) {
            totalQuantity += quantity;
        }
        return totalQuantity;
    }

    public Product[] getProducts() {
        // Create a list to store products based on their quantities
        List<Product> productList = new ArrayList<>();

        // Iterate through the cart and add products to the list
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            // Add the product to the list for each quantity
            for (int i = 0; i < quantity; i++) {
                productList.add(product);
            }
        }

        // Convert the list to an array and return it
        return productList.toArray(new Product[0]);
    }
}
