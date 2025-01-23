package View;

import Controller.CashierController;
import Controller.ProductController;
import Controller.TransactionController;
import DAO.ProductDAO;
import Model.*;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;


public class CashierUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel backgroundPanel;
    private static final String TAX_PERCENTAGE = "17";
    private BufferedImage backgroundImage;
    private JButton activeButton = null;
    private JPanel productPanel; // Panel to display products
    private JPanel billItemsPanel; // Panel to display bill items
    private JScrollPane billScrollPane; // Scroll pane for bill items
    private JLabel subtotalLabel, taxLabel, totalBillLabel; // Labels for subtotal, tax, and total
    private Bill bill; // Use Bill instead of a Map for cart    private final double TAX_PERCENTAGE = 8.5; // Tax percentage
    private JPanel horizontalScrollPanel = new JPanel();
    private JButton activeCategoryButton = new JButton(); // Tracks the currently active category button
    int menuYPosition = 280; // Set this closer to 0 for moving the panel higher
    int menuWidth = 185;
    JPanel startSalesPanel;
    JPanel viewBillsPanel;
    JPanel mainPanel;

    private ProductDAO productDAO;
    private Employee employee;
    CashierController cashierController;
    List<String> categories;
    TransactionController transactionController = new TransactionController();

    // Constructor to initialize the UI
    public CashierUI(Employee loggedInEmployee) {
        this.employee = loggedInEmployee;
        this.cashierController = new CashierController();
        this.productDAO = new ProductDAO();
        this.categories = productDAO.getUniqueCategories(employee.getBranchId());
        bill = new Bill();
        setTitle("Cashier Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 710);
        setResizable(false);
        setIconImage(loadIcon("images/icons/logo.PNG"));

        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("images/Cashier.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load background image.");
        }

        // Background Panel
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Fallback color
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());
        setVisible(true);  // Make sure to set the JFrame visible after all components are added
        add(backgroundPanel);

        // Labels
        JLabel userName = new JLabel("" + employee.getUsername());
        userName.setBounds(82, 99, 200, 22);
        userName.setFont(new Font("Arial", Font.BOLD, 22));
        userName.setForeground(Color.white);


        JLabel branchId = new JLabel("" + employee.getEmployeeId());
        branchId.setBounds(118, 189, 200, 22);
        branchId.setFont(new Font("Arial", Font.PLAIN, 16));
        branchId.setForeground(Color.white);

        JLabel position = new JLabel(employee.getPosition());
        position.setFont(new Font("Century Gothic", Font.ITALIC, 13));
        position.setForeground(Color.white);
        position.setPreferredSize(new Dimension(150, 30));
        position.setBounds(80, 125, 150, 30);

        backgroundPanel.add(position);
        backgroundPanel.add(userName);
        backgroundPanel.add(branchId);

        // Side Menu Panel
        JPanel sideMenuPanel = createSideMenuPanel();
        backgroundPanel.add(sideMenuPanel);

        // CardLayout and Main Panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);  // Main panel holding cards
        mainPanel.setBounds(menuWidth, 0, getWidth() - menuWidth, getHeight());
        backgroundPanel.add(mainPanel);

        // Create panels for CardLayout
        startSalesPanel = createStartSalesPanel();

        viewBillsPanel = new JPanel();
        viewBillsPanel.setBackground(Color.WHITE);

        // Add panels to CardLayout
        mainPanel.add(startSalesPanel, "Start Sales");
        mainPanel.add(viewBillsPanel, "View Bills");

        // Show the first card
        cardLayout.show(mainPanel, "Start Sales");

        // Add the main panel to backgroundPanel

    }

    private JPanel createSideMenuPanel() {
        // Side Menu
        JPanel sideMenuPanel = new JPanel();
        sideMenuPanel.setLayout(null); // Use null layout for manual positioning
        sideMenuPanel.setBackground(Color.WHITE);

// Adjust the bounds of the panel itself (height can adjust based on content)

        sideMenuPanel.setBounds(0, menuYPosition, menuWidth, getHeight() - menuYPosition);
        sideMenuPanel.setOpaque(false);

// Button text and optional icon paths
        String[][] menuItems = {
                {"Start Sale", "images/icons/dash_icon.png"},
                {"View Bills", "icons/bill.png"},
                {"Logout", "images/icons/logout.png"}
        };

        final SideMenuButton[] activeButton = {null}; // Track the currently active button

// Initial Y position for the first button
        int buttonYPosition = 10; // Start higher in the panel

        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i][0];
            String iconPath = menuItems[i][1];

            SideMenuButton button = new SideMenuButton(text, iconPath);
            button.setSize(80,30);
            // Set button bounds manually
            int buttonHeight = 50; // Adjust the height of each button as needed
            button.setBounds(0, buttonYPosition, menuWidth+28, buttonHeight);

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
                        cardLayout.show(mainPanel,"Start Sales");
                        break;

                    case "View Bills":
                        System.out.println("Viewing Bills...");
                        cardLayout.show(mainPanel,"View Bills");

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

        return sideMenuPanel;

// Add side menu panel to your background panel
    }


    private JPanel createStartSalesPanel() {

        JPanel startSalesPanel = new JPanel();
        startSalesPanel.setLayout(null); // Use your custom layout here

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
        productPanel.setLayout(new GridLayout(0, 3, 16, 16));  // 4 products per row with 20px gap between them
        productPanel.setOpaque(true);  // Make sure productPanel is opaque to display correctly
        productPanel.setBackground(new Color(255, 255, 255));
        // Scrollable product panel with vertical scrollbar
        JScrollPane productScrollPane = new JScrollPane(productPanel);
        productScrollPane.setBounds(200, 123, 670, 500);
        productScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productScrollPane.setBorder(BorderFactory.createEmptyBorder()); // This removes the border

//padding
        productPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Top, Left, Bottom, Right padding

        IOSScrollBarUtils.applyIOSStyleScrollBar(productScrollPane);

        // Bill Panel with enhanced layout
        JPanel billPanel = new JPanel();
        billPanel.setLayout(new BorderLayout());
        billPanel.setBounds(885, 134, 385, 491);
        billPanel.setBackground(new Color(197, 227, 218));
        billPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel billTitle = new JLabel("---------Bill Summary---------");
        billTitle.setFont(new Font("Century Gothic", Font.BOLD, 17));
        billTitle.setHorizontalAlignment(SwingConstants.CENTER);
        billTitle.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));  // Add padding to the title
        billPanel.add(billTitle, BorderLayout.NORTH);
        billPanel.setOpaque(false);

// Bill Items Header Panel
        JPanel billItemsHeaderPanel = new JPanel();
        billItemsHeaderPanel.setLayout(new GridLayout(1, 3)); // 1 row, 3 columns (Item, Qty, Price)
        billItemsHeaderPanel.setBackground(new Color(55, 62, 97)); // Light background color for the header
        billItemsHeaderPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
// Set FlowLayout with left alignment and some horizontal space between components
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 60, 0); // 20px gap between components
        billItemsHeaderPanel.setLayout(flowLayout);

// Fixed Labels for Item, Quantity, and Price
        JLabel itemLabel = new JLabel("Item");
        itemLabel.setForeground(Color.white);
        itemLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));

        JLabel qtyLabel = new JLabel("Price");
        qtyLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
        qtyLabel.setForeground(Color.white);

        JLabel priceLabel = new JLabel("QTY");
        priceLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
        priceLabel.setForeground(Color.white);

// Add labels to the header panel
        billItemsHeaderPanel.add(itemLabel);
        billItemsHeaderPanel.add(qtyLabel);
        billItemsHeaderPanel.add(priceLabel);


// Bill Items Panel - Flexible layout
        billItemsPanel = new JPanel();
        billItemsPanel.setLayout(new BoxLayout(billItemsPanel, BoxLayout.Y_AXIS));  // Stack items vertically
        billItemsPanel.setBackground(Color.WHITE);

        billScrollPane = new JScrollPane(billItemsPanel);
        billScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        billScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        IOSScrollBarUtils.applyIOSStyleScrollBar(billScrollPane);

// Add the header panel to the bill panel before the bill items section
        billPanel.add(billItemsHeaderPanel, BorderLayout.NORTH);
        billPanel.add(billScrollPane, BorderLayout.CENTER);

// Totals Section
        // Totals Section
        JPanel totalsPanel = new JPanel();
        totalsPanel.setLayout(new BorderLayout());
        totalsPanel.setBackground(Color.WHITE);
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

// Sub-panel for labels (aligned to the right)
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
        labelsPanel.setBackground(Color.WHITE);

// Labels for totals with updated font and alignment
        subtotalLabel = new JLabel("Subtotal: Rs.0");
        subtotalLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        subtotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        taxLabel = new JLabel("Tax (" + TAX_PERCENTAGE + "%): Rs.0");
        taxLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        taxLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        totalBillLabel = new JLabel("Total: Rs.0");
        totalBillLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
        totalBillLabel.setForeground(Color.BLACK);  // Green for total
        totalBillLabel.setHorizontalAlignment(SwingConstants.RIGHT);

// Add labels to the labelsPanel
        labelsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Small spacing
        labelsPanel.add(subtotalLabel);
        labelsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Small spacing
        labelsPanel.add(taxLabel);
        labelsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Small spacing
        labelsPanel.add(totalBillLabel);

// Button for generating the bill (aligned to the left)
        RoundedButton generateBill = new RoundedButton("Print", 15);
        generateBill.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        generateBill.setAlignmentX(Component.LEFT_ALIGNMENT);
        generateBill.addActionListener(e -> {
            try {
                generateBillAction();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

// Sub-panel for the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Align button to the left
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(generateBill);

// Add components to totalsPanel
        totalsPanel.add(buttonPanel, BorderLayout.WEST); // Button on the left
        totalsPanel.add(labelsPanel, BorderLayout.EAST); // Labels on the right
// Add labels to the totals panel
        totalsPanel.add(subtotalLabel);
        totalsPanel.add(taxLabel);
        totalsPanel.add(totalBillLabel);

        billPanel.add(totalsPanel, BorderLayout.SOUTH);  // Add totals at the bottom
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(scrollPane, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(productScrollPane, JLayeredPane.PALETTE_LAYER);  // Add scroll pane for products
        layeredPane.add(billPanel, JLayeredPane.PALETTE_LAYER);

// After adjusting, revalidate and repaint
        layeredPane.revalidate();
        layeredPane.repaint();


        this.add(layeredPane);

        return startSalesPanel;
    }

    private JPanel createViewBillsPanel() {
        JPanel viewBillsPanel = new JPanel();
        viewBillsPanel.setLayout(new BorderLayout());

        // Example content for "View Bills" panel
        JLabel titleLabel = new JLabel("View Bills Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
        viewBillsPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea billsArea = new JTextArea("Bill details will appear here...");
        billsArea.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(billsArea);
        viewBillsPanel.add(scrollPane, BorderLayout.CENTER);

        return viewBillsPanel;
    }


    public CashierUI() {

    }

    private Image loadIcon(String path) {
        URL iconURL = getClass().getClassLoader().getResource(path);
        if (iconURL != null) {
            return new ImageIcon(iconURL).getImage();
        } else {
            System.err.println("Error: Unable to load frame icon image.");
            return null;
        }
    }

    private void initializeCategoryButtons() {

        final SideMenuButton[] activeCategoryButton = {null};
        for (String category : categories) {
            String iconPath = getCategoryIcon(category); // Fetch appropriate icon for the category

            // Create a category button with the determined icon
            SideMenuButton categoryButton = new SideMenuButton(category, iconPath);

            // Add action listener to handle button clicks
            categoryButton.addActionListener(e -> {
                // Deactivate the previously active button, if any
                if (activeCategoryButton[0] != null) {
                    activeCategoryButton[0].setActive(false); // Deactivate the previously active button
                }

                // Activate the clicked button
                categoryButton.setActive(true);
                activeCategoryButton[0] = categoryButton; // Set this button as active

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
        List<Product> products = productDAO.getProductsByCategory(category, employee.getBranchId());
        // Clear the current products displayed
        productPanel.removeAll();
        productStockLabels.clear(); // Clear previous stock labels

        for (Product product : products) {
            // Create a rounded panel for each product
            RoundedPanel productInfoPanel = new RoundedPanel(15); // Corner radius of 15
            productInfoPanel.setLayout(new BoxLayout(productInfoPanel, BoxLayout.Y_AXIS)); // Stack vertically
            productInfoPanel.setBackground(new Color(35, 42, 67));
            productInfoPanel.setPreferredSize(new Dimension(135, 148)); // Ensure consistent size for the product panel
            productInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Product name label
            JLabel productNameLabel = new JLabel(product.getName());
            productNameLabel.setForeground(Color.white);
            productNameLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            productNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Product price label
            JLabel productPriceLabel = new JLabel("Rs. " + product.getOriginalPrice());
            productPriceLabel.setForeground(Color.WHITE);
            productPriceLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            productPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
            productPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Product stock label
            JLabel productStockLabel = new JLabel("Stock: " + productDAO.getProductQuantityByName(product.getName(), employee.getBranchId()));
            productStockLabel.setFont(new Font("Century Gothic", Font.PLAIN, 12));
            productStockLabel.setForeground(Color.WHITE);
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
            productInfoPanel.add(Box.createVerticalGlue()); // Ensure padding below the button
            // Add the product info panel to the main product panel
            productPanel.add(productInfoPanel);
        }

        // Revalidate and repaint to update the UI
        productPanel.revalidate();
        productPanel.repaint();
    }


    // Method to add a product to the cart
    private void addProductToCart(Product product) {
        bill.addProduct(product, 1);  // Add 1 quantity of the product to the cart
        updateBill();  // Update the UI with the new bill information (for example, total, tax, etc.)
    }

    private void updateBill() {
        // Step 1: Clear existing bill items
        billItemsPanel.removeAll();
        bill.subtotal = BigDecimal.ZERO;

        // Step 2: Loop through the cart and handle each product
        for (Map.Entry<Product, Integer> entry : bill.getCart().entrySet()) {
            Product product = entry.getKey();  // Get the Product object directly
            int quantity = entry.getValue();

            // Step 3: Fetch product price and quantity available in stock
            BigDecimal productPrice = BigDecimal.ZERO;
            int availableQuantity = 0;  // Fetch quantity available in stock
            productPrice = productDAO.getProductPriceByName(product.getName(), employee.getBranchId());  // Get product price from DB
            availableQuantity = productDAO.getProductQuantityByName(product.getName(), employee.getBranchId());  // Get available quantity from DB

            // Step 4: Update subtotal for the bill
            bill.subtotal = bill.subtotal.add(productPrice.multiply(BigDecimal.valueOf(quantity)));

            // Step 5: Create a panel for each bill item (product, price, and quantity controls)
            JPanel billItemPanel = new JPanel();
            billItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 3)); // Reduced spacing between items
            billItemPanel.setBackground(Color.WHITE);

            // Step 6: Add product name label
            JLabel productNameLabel = new JLabel(product.getName());
            productNameLabel.setFont(new Font("Century Gothic", Font.PLAIN, 12));
            productNameLabel.setPreferredSize(new Dimension(140, 30));  // Fixed width for wrapping
            productNameLabel.setMaximumSize(new Dimension(150, 30));  // Prevent overflow
            billItemPanel.add(productNameLabel);

            // Step 7: Add product price label
            JLabel productPriceLabel = new JLabel("Rs." + productPrice);
            productPriceLabel.setFont(new Font("Century Gothic", Font.PLAIN, 12));
            productPriceLabel.setPreferredSize(new Dimension(60, 20));  // Fixed size
            productPriceLabel.setMaximumSize(new Dimension(70, 20));  // Prevent overflow
            billItemPanel.add(productPriceLabel);

            // Step 8: Create the quantity control panel (minus, quantity, and plus buttons)
            JPanel quantityPanel = new JPanel();
            quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3)); // Reduced spacing between buttons
            quantityPanel.setBackground(Color.WHITE);

            // Step 9: Add quantity label
            JLabel quantityLabel = new JLabel(" " + quantity + " ");
            quantityLabel.setFont(new Font("Century Gothic", Font.PLAIN, 12));
            quantityPanel.add(quantityLabel);

            // Step 10: Create and add the minus button (rounded)
            RoundedButton minusButton = new RoundedButton("-", 15);  // 15 is the corner radius for rounded corners
            minusButton.setFont(new Font("Century Gothic", Font.PLAIN, 20));
            minusButton.setBackground(new Color(255, 255, 255));  // Example background color for the button
            minusButton.setPreferredSize(new Dimension(45, 32));  // Smaller size (width x height)
            minusButton.addActionListener(e -> adjustQuantity(product, -1, quantityLabel));
            minusButton.setForeground(Color.BLACK);
            quantityPanel.add(minusButton);

            // Step 11: Create and add the plus button (rounded)
            RoundedButton plusButton = new RoundedButton("+", 15);  // 15 is the corner radius for rounded corners
            plusButton.setFont(new Font("Century Gothic", Font.PLAIN, 20));

// Set the background and text color
            plusButton.setBackground(new Color(35, 42, 67));  // Background color for the button
            plusButton.setForeground(Color.WHITE);  // Text color to white

// Set a smaller preferred size to avoid button cutting
            plusButton.setPreferredSize(new Dimension(50, 32));  // Smaller size (width x height)

// Add action listener to adjust quantity when clicked
            plusButton.addActionListener(e -> adjustQuantity(product, 1, quantityLabel));

// Add the plus button to the quantity panel
            quantityPanel.add(plusButton);


            // Step 12: Add the quantity panel to the bill item panel
            billItemPanel.add(quantityPanel);

            // Step 13: Add the bill item panel to the bill items panel
            billItemsPanel.add(billItemPanel);
        }

        // Step 14: After all items are added, update the bill summary
        updateBillSummary();
    }

    // Adjust the quantity of the product in the cart
    private void adjustQuantity(Product product, int adjustment, JLabel quantityLabel) {
        // Adjust quantity in the cart
        int currentQuantity = bill.getCart().get(product);  // Get current quantity
        int newQuantity = currentQuantity + adjustment;

        // Prevent going below 0
        if (newQuantity >= 0) {
            bill.addProduct(product, newQuantity - currentQuantity);  // Update quantity in cart
        }

        // Update the quantity label
        quantityLabel.setText(" " + newQuantity + " ");  // Update label text with the new quantity

        // Recalculate the bill
        updateBill();  // This will automatically update the subtotal, total, etc.
    }

    // Summary update for the bill (e.g., total, tax, subtotal)
    private void updateBillSummary() {
        // You can display the final total, tax, and subtotal somewhere in the UI
        totalBillLabel.setText("Total: " + bill.getTotalBill().toString());
        subtotalLabel.setText("Subtotal: " + bill.getSubtotal().toString());
        taxLabel.setText("Tax: " + bill.getTax().toString());
    }


    private void updateSubtotalAndTax() {
        // Reset the subtotal, tax, and total in the Bill object (cart is the Bill object)
        bill.subtotal = BigDecimal.ZERO;  // Reset Bill's subtotal
        bill.tax = BigDecimal.ZERO; // Reset Bill's tax amount

        // Loop through each item in the cart
        for (Map.Entry<Product, Integer> entry : bill.getCart().entrySet()) {
            Product product = entry.getKey(); // Product object
            int quantity = entry.getValue();  // Quantity of the product

            try {
                // Calculate the product price from the product object directly
                BigDecimal productPrice = product.getSalesPrice();  // Get product's sales price

                // Add product price * quantity to the Bill's subtotal
                bill.subtotal = bill.subtotal.add(productPrice.multiply(BigDecimal.valueOf(quantity)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Calculate tax based on the Bill's subtotal
        bill.tax = bill.subtotal.multiply(new BigDecimal(String.valueOf(bill.getTax()))).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

        // Calculate the total (subtotal + tax) and set it in the Bill object
        bill.totalBill = bill.subtotal.add(bill.tax);
    }


    private boolean updateStockInDatabase() throws SQLException {
        boolean flag = false;

        flag = cashierController.updateStockInDatabase(bill, employee.getBranchId());
        return flag;
    }


    private void generateBillAction() throws SQLException {
// Create a map to aggregate products by their ID
        Map<Integer, Integer> aggregatedProducts = new HashMap<>();

// Aggregate products by their ID
        for (Product product : bill.getProducts()) {
            // If the product ID already exists, update the quantity
            if (aggregatedProducts.containsKey(product.getId())) {
                aggregatedProducts.put(product.getId(), aggregatedProducts.get(product.getId()) + 1);
                System.out.println("Updated product with ID " + product.getId() + " - New Quantity: " + aggregatedProducts.get(product.getId()));
            } else {
                // If the product ID doesn't exist, initialize its quantity to 1
                aggregatedProducts.put(product.getId(), 1);
                System.out.println("Added new product with ID " + product.getId() + " - Initial Quantity: 1");
            }
        }

// Prepare data for printing
        String title = "                  ______METRO______";
        String[] headers = {"QTY", "PRODUCT", "PRICE"};

// Convert aggregated products map to an array of Products
        List<Product> productList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : aggregatedProducts.entrySet()) {
            Product product = getProductById(entry.getKey()); // Assume this method retrieves a product by its ID
            if (product != null) {
                product.setQuantity(entry.getValue()); // Set the aggregated quantity
                productList.add(product);
            }
        }

// Create data for the table and calculate subtotal
        String[][] data = new String[productList.size()][3];
        BigDecimal subtotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP); // Set scale for accuracy

        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);

            // Ensure quantity is converted to BigDecimal for accurate calculations
            BigDecimal quantity = new BigDecimal(product.getQuantity());
            BigDecimal price = product.getSalesPrice();

            // Multiply price by quantity to calculate the total price for this product
            BigDecimal totalPrice = price.multiply(quantity).setScale(2, RoundingMode.HALF_UP); // Set scale for price

            // Update the data array with product details
            data[i][0] = String.valueOf(product.getQuantity());
            data[i][1] = product.getName();
            data[i][2] = String.valueOf(product.getSalesPrice()); // Display total price for the product

            // Add the total price to the subtotal
            subtotal = subtotal.add(totalPrice);
        }

// Calculate tax and net total
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.17")).setScale(2, RoundingMode.HALF_UP); // 17% tax
        BigDecimal netTotal = subtotal.add(tax).setScale(2, RoundingMode.HALF_UP);

// Add a line above the footer in the table data
        String[][] lineRow = {{"", "________________________", ""}}; // A separator line
        String[][] footerData = {
                {"Subtotal", "", subtotal.toString()},
                {"Tax", "", tax.toString()},
                {"Net Total", "", netTotal.toString()},
        };

// Add the separator line above the footer
        String[][] completeDataWithLine = new String[data.length + lineRow.length + footerData.length][3];
        System.arraycopy(data, 0, completeDataWithLine, 0, data.length);
        System.arraycopy(lineRow, 0, completeDataWithLine, data.length, lineRow.length);
        System.arraycopy(footerData, 0, completeDataWithLine, data.length + lineRow.length, footerData.length);

// Add a separator line before the "Thank You" message
        String[][] lineBeforeThankYou = {{"", "----------------------------", ""}};

// Add the "Thank You" message at the bottom
        String[][] thankYouMessage = {{"", "Thank you for shopping with us!", ""}};

// Merge the separator line and Thank You message
        String[][] finalCompleteData = new String[completeDataWithLine.length + lineBeforeThankYou.length + thankYouMessage.length][3];
        System.arraycopy(completeDataWithLine, 0, finalCompleteData, 0, completeDataWithLine.length);
        System.arraycopy(lineBeforeThankYou, 0, finalCompleteData, completeDataWithLine.length, lineBeforeThankYou.length);
        System.arraycopy(thankYouMessage, 0, finalCompleteData, completeDataWithLine.length + lineBeforeThankYou.length, thankYouMessage.length);

// Print the bill with the updated data
        MyPrinter.MyPrinterWithTableData printer = new MyPrinter.MyPrinterWithTableData();
        printer.setTableData(headers, finalCompleteData, title);

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(printer);

// Print dialog to allow user to cancel if needed
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Printing failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper method to get Product by its ID (you should implement this method based on your existing logic)
    private Product getProductById(int productId) {
        for (Product product : bill.getProducts()) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }


    // Method to check if internet is available (by checking database connectivity)
    private boolean isInternetAvailable() {
        ProductController productController= new ProductController();
       return productController.isInternetAvailable();
    }

    // Method to save the stock update to a file when no internet is available
    private boolean saveUpdateToFile() {
        // Get product details and save them to a file for later synchronization
        String productName = "Sample Product";  // Example, replace with actual product details
        int quantity = 10;  // Example quantity, replace with actual quantity
        int branchId = 1;  // Example branchId, replace with actual branchId

        try (FileWriter writer = new FileWriter("stock_updates.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            String updateData = productName + "," + quantity + "," + branchId + "," + System.currentTimeMillis();
            bufferedWriter.write(updateData);
            bufferedWriter.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving update to file: " + e.getMessage());
            return false;
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
            SwingUtilities.invokeLater(() -> {
                NewLoginFrame frame = new NewLoginFrame();
                frame.setVisible(true); // Make LoginOptions visible
            });
            this.dispose();
        }
    }



//    // Main method to launch the UI
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new CashierUI(loggedInEmployee).setVisible(true);
//        });
//    }
}
