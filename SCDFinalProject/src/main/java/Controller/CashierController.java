package Controller;

import Service.CashierService;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.sql.SQLException;

public class CashierController {
    private CashierService cashierService;

    public CashierController() {
        cashierService = new CashierService();
    }

    public void addProduct(String productName, int quantity) {
        if (cashierService.addProductToBill(productName, quantity))
        {
            JOptionPane.showMessageDialog(null, "Product added to bill!");
        } else {
            JOptionPane.showMessageDialog(null, "Product not found or insufficient stock.");
        }
    }

    public void generateBill() throws SQLException {
        BigDecimal totalBill = cashierService.getTotalBill();
        JOptionPane.showMessageDialog(null, "Total Bill: $" + totalBill);
        cashierService.deductStockFromDatabase();
        cashierService.resetBill();
    }
}
