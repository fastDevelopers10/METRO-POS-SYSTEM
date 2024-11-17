
package SCDFinalProject.src.main.java.Model;

public class Product
{
    private String name;
    private String category;
    private double originalPrice;
    private double salePrice;
    private double pricePerUnit;
    private double pricePerCarton;
    private int quantity;  // Quantity being purchased in the current transaction
    private int stock;     // Available stock in the inventory

    // Constructor
    public Product(String name, String category, double originalPrice, double salePrice, double pricePerUnit, double pricePerCarton, int quantity, int stock) {
        this.name = name;
        this.category = category;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.pricePerUnit = pricePerUnit;
        this.pricePerCarton = pricePerCarton;
        this.quantity = quantity;
        this.stock = stock;
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getOriginalPrice() { return originalPrice; }
    public double getSalePrice() { return salePrice; }
    public double getPricePerUnit() { return pricePerUnit; }
    public double getPricePerCarton() { return pricePerCarton; }
    public int getQuantity() { return quantity; }
    public int getStock() { return stock; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setStock(int stock) { this.stock = stock; }

    //update stock after a sale
    public boolean reduceStock(int amount)
    {
        if (stock >= amount)
        {
            stock -= amount;
            return true;  // Stock successfully reduced
        }
        return false;  // Not enough stock
    }
}
