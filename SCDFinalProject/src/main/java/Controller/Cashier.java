import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cashier
{
    private List<Product> products;
    private double totalBill;
    private Connection connection;

    public Cashier()
    {
        products = new ArrayList<>();
        totalBill = 0.0;
        connectToDatabase();
    }

    private void connectToDatabase()
    {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDatabase", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //add product to the bill
    public void addProduct(String productName, int quantity) {
        double price = fetchPriceFromDatabase(productName);
        if (price >= 0) {  // if price is valid
            Product product = new Product(productName, price, quantity);
            products.add(product);
            totalBill += price * quantity;
        } else {
            System.out.println("Product not found or price not available in database.");
        }
    }

    //  to fetch product price from the database
    private double fetchPriceFromDatabase(String productName) {
        String query = "SELECT price FROM products WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  //  product not found or error occurred
    }

    //generate the bill
    public void generateBill()
    {
        System.out.println("=== Bill Summary ===");
        for (Product product : products) {
            System.out.printf("%s x %d = $%.2f%n", product.getName(), product.getQuantity(), product.getPrice() * product.getQuantity());
        }
        System.out.printf("Total Bill: $%.2f%n", totalBill);
        System.out.println("=====================");

        deductStockFromDatabase();
    }

    // Method to deduct stock from the database
    private void deductStockFromDatabase() {
        String updateStockQuery = "UPDATE products SET stock = stock - ? WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateStockQuery)) {
            for (Product product : products) {
                statement.setInt(1, product.getQuantity());
                statement.setString(2, product.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateSalesReport()
    {
        System.out.println("=== Sales Report ===");
        double totalSales = 0.0;
        for (Product product : products) {
            totalSales += product.getPrice() * product.getQuantity();
            System.out.printf("Product: %s, Quantity Sold: %d, Revenue: $%.2f%n", product.getName(), product.getQuantity(), product.getPrice() * product.getQuantity());
        }
        System.out.printf("Total Sales: $%.2f%n", totalSales);
        System.out.println("=====================");
    }


}
