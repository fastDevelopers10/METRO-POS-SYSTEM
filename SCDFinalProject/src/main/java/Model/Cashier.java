package Model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Cashier extends Employee {
    private List<Product> products;  // List of products added to the bill
    private BigDecimal totalBill;    // Total bill amount

    // Constructor to initialize all fields
    public Cashier(String employeeId, String username, String email, String password, int branchCode, String address,
                   BigDecimal salary, String phone, String status, Date joiningDate, String employeeType) {
        // Calling the Employee constructor to initialize inherited fields
        super(employeeId, username, email, password, branchCode, address, salary, phone, status, joiningDate, employeeType);
        this.products = new ArrayList<>();
        this.totalBill = BigDecimal.ZERO;  // Initialize totalBill as BigDecimal.ZERO
    }

    // Add product to the bill
    public void addProductToBill(Product product, int quantity) {
        products.add(product);
        // Multiply product's sales price by quantity and add to totalBill
        totalBill = totalBill.add(product.getSalesPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    // Get the list of products in the bill
    public List<Product> getProducts() {
        return products;
    }

    // Get the total bill amount
    public BigDecimal getTotalBill() {
        return totalBill;
    }


}
