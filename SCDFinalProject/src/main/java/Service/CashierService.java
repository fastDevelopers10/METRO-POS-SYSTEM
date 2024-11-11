package Service;

import DAO.DBConnection;
import DAO.ProductDAO;
import Model.Bill;
import Model.Cashier;
import Model.Product;

import java.sql.Connection;
import java.sql.SQLException;

public class CashierService
{
    private Connection connection;
    private ProductDAO productDAO;
    private Bill bill = new Bill();

    public CashierService()
    {
        this.connection = DBConnection.getConnection();
        this.productDAO = new ProductDAO(connection);
    }

    public boolean addProductToBill(String productName, int quantity)
    {
        Product product = productDAO.getProductByName(productName);
        if (product != null && product.getStock() >= quantity) {
            bill.addProduct(product, quantity);
            return true;
        }
        return false;
    }

    public double getTotalBill() {
        return bill.getTotalBill();
    }
    // Reset the bill after generating it
    public void resetBill() {
        bill.resetBill(); // Reset the bill by calling the reset method on the Bill object
    }
    public void deductStockFromDatabase() {
        for (Product product : bill.getProducts()) {
            productDAO.updateStock(product.getName(), product.getStock());
        }
    }
}
