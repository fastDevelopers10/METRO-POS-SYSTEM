package SCDFinalProject.src.main.java.Model;

import java.util.ArrayList;
import java.util.List;

public class Cashier extends Employee {
    private List<Product> products;
    private double totalBill;

    public Cashier(String employeeId, String name, String email, String password) {
        super(employeeId, name, "Cashier", email, password);
        this.products = new ArrayList<>();
        this.totalBill = 0.0;
    }

    public void addProductToBill(Product product) {
        products.add(product);
        totalBill += product.getSalePrice() * product.getQuantity();
    }

    //get the list of products in the bill
    public List<Product> getProducts() {
        return products;
    }

    public double getTotalBill() {
        return totalBill;
    }



    // Method to process the bill (this could involve printing it or any other necessary action)
    public void processBill() {
        System.out.println(getName() + " is processing the bill...");
        System.out.println("Total Bill: $" + totalBill);
    }
}
