package Model;

import java.util.ArrayList;
import java.util.List;

public class Bill
{
    private List<Product> products = new ArrayList<>();
    private double totalBill = 0.0;

    public void addProduct(Product product, int quantity) {
        product.setStock(quantity);
        products.add(product);
        totalBill += product.getSalePrice() * quantity;
    }
    // Method to reset the bill (clear the list of products and total)
    public void resetBill() {
        products.clear();
        totalBill = 0.0;
    }

    public double getTotalBill() {
        return totalBill;
    }

    // Getters
    public List<Product> getProducts() { return products; }
}
