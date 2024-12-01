package View;

import Controller.CashierController;
import DAO.ProductDAO;
import Model.Bill;
import Model.Cashier;
import Model.Employee;
import Model.Product;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.imageio.ImageIO;

public class CashierUI extends JFrame {


    private static final String TAX_PERCENTAGE = "17";
    private BufferedImage backgroundImage;
    private JButton activeButton = null;
    private JPanel productPanel; // Panel to display products
    private JPanel billItemsPanel; // Panel to display bill items
    private JScrollPane billScrollPane; // Scroll pane for bill items
    private JLabel subtotalLabel, taxLabel, totalBillLabel; // Labels for subtotal, tax, and total
    private Bill cart; // Use Bill instead of a Map for cart    private final double TAX_PERCENTAGE = 8.5; // Tax percentage
    private JPanel horizontalScrollPanel = new JPanel();
    private JButton activeCategoryButton; // Tracks the currently active category button

    private ProductDAO productDAO;
    private Employee employee;
    List<String> categories;


    // Constructor to initialize the UI
    public CashierUI(Employee loggedInEmployee) {
        this.employee=loggedInEmployee;
        this.productDAO = new ProductDAO(); // Initialize with the proper constructor
        this.categories=productDAO.getUniqueCategories(employee.getBranchCode()); // Fetch categories from DAO
        cart = new Bill(); // Correct initialization of cart as Bill        productDAO = new ProductDAO(); // Initialize ProductDAO to fetch products
        setTitle("Cashier Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 710); // Set a default size for the window
        setResizable(false);

        try {
            // Use class loader to load the resource
            backgroundImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("images/Cashier.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load background image.");
        }

        // Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Fallback color if image is not found
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());

        // Side Menu
        JPanel sideMenuPanel = new JPanel();
        sideMenuPanel.setLayout(null); // Use null layout for manual positioning
        sideMenuPanel.setBackground(Color.WHITE);

// Adjust the bounds of the panel itself (height can adjust based on content)
        int menuYPosition = 220; // Set this closer to 0 for moving the panel higher
        int menuWidth = 157;
        sideMenuPanel.setBounds(14, menuYPosition, menuWidth, getHeight() - menuYPosition);
        sideMenuPanel.setOpaque(false);

// Button text and optional icon paths
        String[][] menuItems = {
                {"Start Sale", "images/icons/dash_icon.png"},
                {"View Bills", "icons/view_bills.png"},
                {"Generate Bill", "icons/generate_bill.png"},
                {"Logout", "images/icons/logout.png"}
        };

        final SideMenuButton[] activeButton = {null}; // Track the currently active button

// Initial Y position for the first button
        int buttonYPosition = 10; // Start higher in the panel

        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i][0];
            String iconPath = menuItems[i][1];

            SideMenuButton button = new SideMenuButton(text, iconPath);

            // Set button bounds manually
            int buttonHeight = 50; // Adjust the height of each button as needed
            button.setBounds(0, buttonYPosition, menuWidth, buttonHeight);

            button.addActionListener(e -> {
                // Set the active button's background
                if (activeButton[0] != null) {
                    activeButton[0].setActive(false); // Reset previous button
                }
                button.setActive(true);
                activeButton[0] = button;
                System.out.println("Button clicked: " + button.getText());

                // Perform specific actions based on the button clicked
                switch (button.getText()) {
                    case "Start Sale":
                        System.out.println("Starting Sale...");
//                        startSaleAction();
                        break;

                    case "View Bills":
                        System.out.println("Viewing Bills...");
                  //      viewBillsAction();
                        break;

                    case "Generate Bill":
                        System.out.println("Generating Bill...");
                        try {
                            generateBillAction();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;

                    case "Logout":
                        System.out.println("Logging out...");
                        logoutAction();
                        break;

                    default:
                        System.out.println("Unknown action");
                        break;
                }
            });
            if (i == 0) {
                button.setActive(true);  // Set the "Start Sale" button as active
                activeButton[0] = button;  // Track it as the active button
            }

            // Add button to the panel
            sideMenuPanel.add(button);

            // Update Y position for next button
            buttonYPosition += buttonHeight; // Increase Y position by the height of the button
        }

// Add side menu panel to your background panel
        backgroundPanel.add(sideMenuPanel);


        // Create a label for categories
        JLabel categoryLabel = new JLabel("Product Categories");
        categoryLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Set the bounds of the label (optional if you want to control the size and positioning)
        categoryLabel.setBounds(200, 10, 200, 30);  // Adjust the position as needed

        // Horizontal Scrollable Panel for categories
        horizontalScrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
horizontalScrollPanel.setBackground(new Color(247, 247, 247, 255));

        JScrollPane scrollPane = new JScrollPane(horizontalScrollPanel);
        scrollPane.setBounds(200, 40, 1060, 85);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // This removes the border

        IOSScrollBarUtils.applyIOSStyleScrollBar(scrollPane);

        // Initialize category buttons dynamically
        initializeCategoryButtons();
        backgroundPanel.add(scrollPane);
        // Add the label and the scroll pane to the background panel
        backgroundPanel.setLayout(null);  // Ensure the background panel uses null layout for manual positioning
        backgroundPanel.add(categoryLabel);

        // Product Panel - Using GridLayout for 4 products per row
        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 3, 20, 20));  // 4 products per row with 20px gap between them
        productPanel.setBounds(200, 180, 700, 500);  // Increase the 'y' position to move it down        productPanel.setBackground(Color.LIGHT_GRAY);
        productPanel.setOpaque(true);  // Make sure productPanel is opaque to display correctly
        productPanel.setBackground(new Color(247, 247, 247));
        // Scrollable product panel with vertical scrollbar
        JScrollPane productScrollPane = new JScrollPane(productPanel);
        productScrollPane.setBounds(200, 123, 700, 500);
        productScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productScrollPane.setBorder(BorderFactory.createEmptyBorder()); // This removes the border

//padding
        productPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Top, Left, Bottom, Right padding

        IOSScrollBarUtils.applyIOSStyleScrollBar(productScrollPane);

        // Bill Panel with enhanced layout
        JPanel billPanel = new JPanel();
        billPanel.setLayout(new BorderLayout());
        billPanel.setBounds(905, 135, 360, 490);
        billPanel.setBackground(new Color(197, 227, 218));
        billPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel billTitle = new JLabel("---------Bill Summary---------");
        billTitle.setFont(new Font("Century Gothic", Font.BOLD, 17));
        billTitle.setHorizontalAlignment(SwingConstants.CENTER);
        billTitle.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));  // Add padding to the title
        billPanel.add(billTitle, BorderLayout.NORTH);
        billPanel.setOpaque(false);

        // Bill Items Panel - Flexible layout
        billItemsPanel = new JPanel();
        billItemsPanel.setLayout(new BoxLayout(billItemsPanel, BoxLayout.Y_AXIS));  // Stack items vertically
        billItemsPanel.setBackground(Color.WHITE);

        billScrollPane = new JScrollPane(billItemsPanel);
        billScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        billScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        IOSScrollBarUtils.applyIOSStyleScrollBar(billScrollPane);

        billPanel.add(billScrollPane, BorderLayout.CENTER);

        // Totals Section
        JPanel totalsPanel = new JPanel();
        totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));
        totalsPanel.setBackground(Color.WHITE);
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        subtotalLabel = new JLabel("Subtotal: Rs.0");
        subtotalLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        taxLabel = new JLabel("Tax (" + TAX_PERCENTAGE + "%): Rs.0");
        taxLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        totalBillLabel = new JLabel("Total: Rs.0");
        totalBillLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
        totalBillLabel.setForeground(new Color(0, 128, 0));  // Green for total

        totalsPanel.add(subtotalLabel);
        totalsPanel.add(taxLabel);
        totalsPanel.add(totalBillLabel);

        billPanel.add(totalsPanel, BorderLayout.SOUTH);



// Adding components to the layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(sideMenuPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(scrollPane, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(productScrollPane, JLayeredPane.PALETTE_LAYER);  // Add scroll pane for products
        layeredPane.add(billPanel, JLayeredPane.PALETTE_LAYER);

// After adjusting, revalidate and repaint
        layeredPane.revalidate();
        layeredPane.repaint();


        this.add(layeredPane);
    }

    private void initializeCategoryButtons()
    {
        for (String category : categories) {
            String iconPath = getCategoryIcon(category); // Fetch appropriate icon for the category

            // Create a category button with the determined icon
            SideMenuButton categoryButton = new SideMenuButton(category, iconPath);

            // Add action listener to handle button clicks
            categoryButton.addActionListener(e -> {
                // Deactivate the previously active button, if any
                if (categoryButton != null) {
                    categoryButton.setActive(false); // Deactivate previous button
                }

                // Activate the clicked button
                categoryButton.setActive(true);
                activeCategoryButton = categoryButton; // Set this button as active

                // Log and load products for the selected category
                System.out.println("Category clicked: " + category);
              loadProductsByCategory(category); // Load products based on selected category
            });

            // Add the category button to the horizontal scroll panel
            horizontalScrollPanel.add(categoryButton);
        }

        // Refresh the panel to display the newly added buttons
        horizontalScrollPanel.revalidate();
        horizontalScrollPanel.repaint();
    }

    // Helper method to determine the correct icon path based on category
    private String getCategoryIcon(String category) {
        String iconPath;

        // Determine the icon based on the category
        switch (category.toLowerCase()) {
            case "shampoo":
                iconPath = "images/icons/washingprod_icon.png"; // Path to Shampoo icon
                break;
            case "food":
                iconPath = "images/icons/food_icon.png"; // Path to Food icon
                break;
            case "fruit":
                iconPath = "images/icons/food_icon.png"; // Path to Fruit icon
                break;
            default:
                iconPath = "images/icons/Product.png"; // Default icon for other categories
                break;
        }

        return iconPath;
    }

    private Map<Product, JLabel> productStockLabels = new HashMap<>();

    private void loadProductsByCategory(String category) {
        // Fetch products based on the selected category from the database
        List<Product> products = productDAO.getProductsByCategory(category,employee.getBranchCode());

        // Clear the current products displayed
        productPanel.removeAll();
        productStockLabels.clear(); // Clear previous stock labels

        for (Product product : products) {
            // Create a rounded panel for each product
            RoundedPanel productInfoPanel = new RoundedPanel(15); // Corner radius of 15
            productInfoPanel.setLayout(new BoxLayout(productInfoPanel, BoxLayout.Y_AXIS)); // Stack vertically
            productInfoPanel.setBackground(new Color(198, 227, 218));
            productInfoPanel.setPreferredSize(new Dimension(200, 200)); // Ensure consistent size for the product panel
            productInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Product name label
            JLabel productNameLabel = new JLabel(product.getName());
            productNameLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            productNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Product price label
            JLabel productPriceLabel = new JLabel("Rs. " + product.getOriginalPrice());
            productPriceLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            productPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
            productPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Product stock label
            JLabel productStockLabel = new JLabel("Stock: " + productDAO.getProductQuantityByName(product.getName(), employee.getBranchCode()));
            productStockLabel.setFont(new Font("Century Gothic", Font.PLAIN, 12));
            productStockLabel.setHorizontalAlignment(SwingConstants.CENTER);
            productStockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Store the stock label for later updates
            productStockLabels.put(product, productStockLabel);

            // Add labels to the product info panel
            productInfoPanel.add(Box.createVerticalGlue()); // Add space above
            productInfoPanel.add(productNameLabel);
            productInfoPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small spacing
            productInfoPanel.add(productPriceLabel);
            productInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            productInfoPanel.add(productStockLabel);
            productInfoPanel.add(Box.createVerticalGlue()); // Add space below

            // Button for adding the product to the cart
            RoundedButton addProductButton = new RoundedButton("Add to Cart", 15); // Corner radius of 15
            addProductButton.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            addProductButton.setBackground(Color.WHITE);
            addProductButton.setForeground(Color.BLACK);
            addProductButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            addProductButton.addActionListener(e -> {
                System.out.println("Product clicked: " + product.getName());
                addProductToCart(product);  // Add the product to the cart when clicked
            });

            // Add the button to the product info panel
            productInfoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space before the button
            productInfoPanel.add(addProductButton);

            // Add the product info panel to the main product panel
            productPanel.add(productInfoPanel);
        }

        // Revalidate and repaint to update the UI
        productPanel.revalidate();
        productPanel.repaint();
    }


    // Method to add a product to the cart
    private void addProductToCart(Product product) {
        cart.addProduct(product, 1);  // Add 1 quantity of the product to the cart
        updateBill();  // Update the UI with the new bill information (for example, total, tax, etc.)
    }

    private void updateBill() {
        // Clear existing bill items
        billItemsPanel.removeAll();

        cart.subtotal = BigDecimal.ZERO;

        // Use the cart from the Bill object (which is a Map<Product, Integer>)
        for (Map.Entry<Product, Integer> entry : cart.getCart().entrySet()) {
            Product product = entry.getKey();  // Get the Product object directly
            int quantity = entry.getValue();

            // Handle possible SQLException when fetching product price and quantity
            BigDecimal productPrice = BigDecimal.ZERO;
            int availableQuantity = 0;  // Fetch quantity available in stock

            productPrice = productDAO.getProductPriceByName(product.getName(), employee.getBranchCode());  // Get product price from DB (using product.getName())
            availableQuantity = productDAO.getProductQuantityByName(product.getName(), employee.getBranchCode());  // Get available quantity from DB (using product.getName())

            // Update subtotal
            cart.subtotal = cart.subtotal.add(productPrice.multiply(BigDecimal.valueOf(quantity)));

            // Panel for each bill item (including product name, price, and quantity control buttons)
            JPanel billItemPanel = new JPanel();
            billItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Reduced spacing between items
            billItemPanel.setBackground(Color.WHITE);

            // Product name label (adjust size dynamically based on the longest name)
            JLabel productNameLabel = new JLabel(product.getName());
            productNameLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            productNameLabel.setPreferredSize(new Dimension(60, 30)); // Fixed width for wrapping
            productNameLabel.setMaximumSize(new Dimension(60, 30)); // Prevent overflow
            billItemPanel.add(productNameLabel);

            // Product price label
            JLabel productPriceLabel = new JLabel("$" + productPrice);
            productPriceLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            productPriceLabel.setPreferredSize(new Dimension(60, 30));
            productPriceLabel.setMaximumSize(new Dimension(60, 30)); // Prevent overflow
            billItemPanel.add(productPriceLabel);

            // Quantity control buttons
            JPanel quantityPanel = new JPanel();
            quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Reduced spacing between buttons
            quantityPanel.setBackground(Color.WHITE);

            // Quantity Label
            JLabel quantityLabel = new JLabel(" " + quantity + " ");
            quantityLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            quantityPanel.add(quantityLabel);

            // Minus Button (Rounded)
            RoundedButton minusButton = new RoundedButton("-", 15);  // 15 is the corner radius for rounded corners
            minusButton.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            minusButton.setBackground(new Color(253, 0, 0));  // Example background color for the button
            minusButton.addActionListener(e -> adjustQuantity(product, -1, quantityLabel));
            quantityPanel.add(minusButton);

            // Plus Button (Rounded)
            RoundedButton plusButton = new RoundedButton("+", 15);  // 15 is the corner radius for rounded corners
            plusButton.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            plusButton.setBackground(new Color(0, 253, 42));  // Example background color for the button
            plusButton.addActionListener(e -> adjustQuantity(product, 1, quantityLabel));
            quantityPanel.add(plusButton);

            billItemPanel.add(quantityPanel);

            // Add the item to the bill items panel
            billItemsPanel.add(billItemPanel);
        }

        // Update the bill after all items
        updateBillSummary();
    }

    // Adjust the quantity of the product in the cart
    private void adjustQuantity(Product product, int adjustment, JLabel quantityLabel) {
        // Adjust quantity in the cart
        int currentQuantity = cart.getCart().get(product);  // Get current quantity
        int newQuantity = currentQuantity + adjustment;

        // Prevent going below 0
        if (newQuantity >= 0) {
            cart.addProduct(product, newQuantity - currentQuantity);  // Update quantity in cart
        }

        // Update the quantity label
        quantityLabel.setText(" " + newQuantity + " ");  // Update label text with the new quantity

        // Recalculate the bill
        updateBill();  // This will automatically update the subtotal, total, etc.
    }

    // Summary update for the bill (e.g., total, tax, subtotal)
    private void updateBillSummary() {
        // You can display the final total, tax, and subtotal somewhere in the UI
        totalBillLabel.setText("Total: " + cart.getTotalBill().toString());
        subtotalLabel.setText("Subtotal: " + cart.getSubtotal().toString());
        taxLabel.setText("Tax: " + cart.getTax().toString());
    }



    private void updateSubtotalAndTax() {
        // Reset the subtotal, tax, and total in the Bill object (cart is the Bill object)
        cart.subtotal = BigDecimal.ZERO;  // Reset Bill's subtotal
        cart.tax = BigDecimal.ZERO; // Reset Bill's tax amount

        // Loop through each item in the cart
        for (Map.Entry<Product, Integer> entry : cart.getCart().entrySet()) {
            Product product = entry.getKey(); // Product object
            int quantity = entry.getValue();  // Quantity of the product

            try {
                // Calculate the product price from the product object directly
                BigDecimal productPrice = product.getSalesPrice();  // Get product's sales price

                // Add product price * quantity to the Bill's subtotal
                cart.subtotal = cart.subtotal.add(productPrice.multiply(BigDecimal.valueOf(quantity)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Calculate tax based on the Bill's subtotal
        cart.tax = cart.subtotal.multiply(new BigDecimal(String.valueOf(cart.getTax()))).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

        // Calculate the total (subtotal + tax) and set it in the Bill object
        cart.totalBill = cart.subtotal.add(cart.tax);
    }



    // Example methods for each action:
    private void startSaleAction() {
        // Start sale logic here
        System.out.println("Sale started");
    }

    private void viewBillsAction() {
        // Logic to view bills here
        System.out.println("Bills displayed");
    }

    private boolean updateStockInDatabase() throws SQLException {
        boolean flag=false;
        CashierController cashierController=new CashierController();
      flag=  cashierController.updateStockInDatabase(cart,employee.getBranchCode());
      return flag;
    }

    private void generateBillAction() throws SQLException {
        boolean flag=false;
//        printBill();

       flag=  updateStockInDatabase();
        if (flag) {
            JOptionPane.showMessageDialog(null,
                    "Stock successfully updated for all products!",
                    "Stock Update Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {


            loadProductsByCategory(activeCategoryButton.getText()); // Repaint the panel to reflect changes
            cart.resetBill();
            billItemsPanel.removeAll();
            // Reset bill summary labels
            subtotalLabel.setText("Subtotal: Rs.0");
            taxLabel.setText("Tax (" + TAX_PERCENTAGE + "%): Rs.0");
            totalBillLabel.setText("Total: Rs.0");

            // Refresh the UI
            billItemsPanel.revalidate();
            billItemsPanel.repaint();
        }

    }


    private void logoutAction() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Dispose of the current frame
            this.dispose();

            // Open the login options
            new LoginOptions().setVisible(true);
        }
    }



//    // Main method to launch the UI
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new CashierUI(loggedInEmployee).setVisible(true);
//        });
//    }
}
