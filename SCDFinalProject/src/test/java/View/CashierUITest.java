//package View;
//
//import static org.junit.Assert.*;
//
//import DAO.ProductDAO;
//import Model.Branch;
//import Model.Product;
//import org.junit.Before;
//import org.junit.Test;
//import Model.Employee;
//import javax.swing.*;
//import java.awt.*;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CashierUITest {
//
//    private CashierUI cashierUI;
//    private ProductDAO productDAO;
//    public Employee employee;
//    private List<Product> testProducts;
//
//    @Before
//    public void setup() {
//        // Initialize CashierUI and mock necessary dependencies
//        cashierUI = new CashierUI(); // Initialize your actual UI class
//
//        // Create mock products
//        testProducts = new ArrayList<>();
//        testProducts.add(new Product(1, new Branch(), "Laptop","Electronics", BigDecimal.valueOf(90),BigDecimal.valueOf(500),600,true)); // (name, price, stock)
//
//
//        // Mock the ProductDAO behavior by overriding getProductsByCategory
//        productDAO = new ProductDAO() {
//            @Override
//            public List<Product> getProductsByCategory(String category, int branchId) {
//                return testProducts; // Return our test products
//            }
//
//            @Override
//            public int getProductQuantityByName(String name, int branchId) {
//                // Return stock based on the product name
//                return name.equals("Product1") ? 10 : 5;
//            }
//        };
//
//        // Create an employee mock to get branch ID
//        employee = new Employee() {
//            @Override
//            public int getBranchId() {
//                return 1; // Example branch ID
//            }
//        };
//
//        // Set the mock ProductDAO and Employee into CashierUI
//        cashierUI.setProductDAO(productDAO);
//        cashierUI.setEmployee(employee);
//    }
//
//    @Test
//    public void testLoadProductsByCategory() {
//        // Simulate loading products by category
//        cashierUI.loadProductsByCategory("Electronics");
//
//        // Verify that the correct number of product panels are created
//        JPanel productPanel = cashierUI.getProductPanel();
//        assertEquals("Product panels not correctly loaded", 2, productPanel.getComponentCount());
//
//        // Verify that stock labels are set correctly
//        for (Product product : testProducts) {
//            JLabel stockLabel = cashierUI.getStockLabelForProduct(product);
//            assertNotNull("Stock label is null for " + product.getName(), stockLabel);
//            assertEquals("Stock label text is incorrect", "Stock: " + productDAO.getProductQuantityByName(product.getName(), 1), stockLabel.getText());
//        }
//    }
//
//    @Test
//    public void testAddProductToCart() {
//        // Simulate loading products by category
//        cashierUI.loadProductsByCategory("Electronics");
//
//        // Find the 'Add to Cart' button for the first product
//        RoundedButton addButton = cashierUI.getAddProductButtonForProduct(testProducts.get(0));
//        addButton.doClick();
//
//        // Verify that the 'Add to Cart' action was triggered for the first product
//        // You can replace this with actual logic for cart interaction
//        // Assuming the product is added to some cart, check if it's updated:
//        assertTrue("Product not added to cart", cashierUI.isProductAddedToCart(testProducts.get(0)));
//    }
//
//    @Test
//    public void testUIRevalidateAndRepaint() {
//        // Capture the initial state of the product panel
//        JPanel productPanel = cashierUI.getProductPanel();
//        int initialComponentCount = productPanel.getComponentCount();
//
//        // Load products by category and check if the panel is updated
//        cashierUI.loadProductsByCategory("Electronics");
//
//        // Ensure the number of components (product panels) has increased
//        assertTrue("Product panel did not update", productPanel.getComponentCount() > initialComponentCount);
//
//        // Ensure revalidate and repaint were called (you may need to track these calls)
//        // In a real test case, you might mock revalidate and repaint to check if they're called
//    }
//}
