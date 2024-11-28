package View;

import DAO.ProductDAO;
import Model.Product;

import java.math.BigDecimal;
import java.util.List; // Correct import for List
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class CashierUI extends JFrame {
    private BufferedImage backgroundImage;
    private JButton activeButton = null;
    private JPanel productPanel; // Panel to display products
    private JPanel billItemsPanel; // Panel to display bill items
    private JScrollPane billScrollPane; // Scroll pane for bill items
    private JLabel subtotalLabel, taxLabel, totalLabel; // Labels for subtotal, tax, and total
    private Map<String, Integer> cart; // Cart to store products and their quantities
    private final double TAX_PERCENTAGE = 8.5; // Tax percentage

    // Constructor to initialize the UI
    public CashierUI() {
        cart = new HashMap<>(); // Initialize the cart

        setTitle("Cashier Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 710); // Set a default size for the window
        setResizable(false);

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\HP\\Documents\\GitHub\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\Cashier.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
        sideMenuPanel.setLayout(new GridBagLayout());
        sideMenuPanel.setBackground(Color.WHITE);
        int menuYPosition = 40;
        int menuWidth = 157;
        sideMenuPanel.setBounds(10, menuYPosition, menuWidth, getHeight() - menuYPosition);
        sideMenuPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.NORTH;

        // Create buttons dynamically for side menu
        String[] buttonTexts = {"Start Sale", "View Bills", "Generate Bill", "Logout"};
        for (int i = 0; i < buttonTexts.length; i++) {
            JButton button = new JButton(buttonTexts[i]);
            button.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            button.setForeground(Color.BLACK);
            button.setBackground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(menuWidth, 40));
            button.addActionListener(e -> {
                if (activeButton != null) {
                    activeButton.setBackground(Color.WHITE);
                }
                button.setBackground(new Color(200, 229, 220));
                activeButton = button;
                System.out.println("Button clicked: " + button.getText());
            });
            gbc.gridy = i;
            sideMenuPanel.add(button, gbc);
        }

        // Horizontal Scrollable Panel for categories
        JPanel horizontalScrollPanel = new JPanel();
        horizontalScrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Adding Shampoo and Fruit category buttons
        JButton shampooButton = new JButton("Shampoo");
        shampooButton.setPreferredSize(new Dimension(100, 40));
        shampooButton.addActionListener(e -> displayProducts("Shampoo"));

        JButton fruitButton = new JButton("Fruit");
        fruitButton.setPreferredSize(new Dimension(100, 40));
        fruitButton.addActionListener(e -> displayProducts("Fruit"));

        horizontalScrollPanel.add(shampooButton);
        horizontalScrollPanel.add(fruitButton);

        JScrollPane scrollPane = new JScrollPane(horizontalScrollPanel);
        scrollPane.setBounds(200, 20, 900, 60);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // Product Panel - Now displaying products
        productPanel = new JPanel();
        productPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8)); // 4 products per row with 8px horizontal spacing
        productPanel.setBounds(200, 100, 700, 500); // Adjusted size
        productPanel.setBackground(Color.LIGHT_GRAY);
        productPanel.setOpaque(false);

        // Scrollable product panel with only vertical scrollbar
        JScrollPane productScrollPane = new JScrollPane(productPanel);
        productScrollPane.setBounds(200, 100, 700, 500);
        productScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Enable vertical scrollbar

        // Add the product scroll pane to the layered pane
        JPanel billPanel = new JPanel();
        billPanel.setLayout(new BorderLayout());
        billPanel.setBounds(920, 100, 350, 500); // Increased width from 280 to 350
        billPanel.setBackground(Color.WHITE);
        billPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel billTitle = new JLabel("Bill Summary");
        billTitle.setFont(new Font("Century Gothic", Font.BOLD, 16));
        billTitle.setHorizontalAlignment(SwingConstants.CENTER);
        billPanel.add(billTitle, BorderLayout.NORTH);
        billPanel.setOpaque(false);

        // Bill Items Panel with Scrollbar
        billItemsPanel = new JPanel();
        billItemsPanel.setLayout(new BoxLayout(billItemsPanel, BoxLayout.Y_AXIS));
        billItemsPanel.setBackground(Color.WHITE);
        billScrollPane = new JScrollPane(billItemsPanel);
        billScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scroll
        billPanel.add(billScrollPane, BorderLayout.CENTER);

        // Subtotal, Tax, and Total Panel
        JPanel totalsPanel = new JPanel();
        totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));
        totalsPanel.setBackground(Color.WHITE);
        totalsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        subtotalLabel = new JLabel("Subtotal: $0");
        subtotalLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        taxLabel = new JLabel("Tax (" + TAX_PERCENTAGE + "%): $0");
        taxLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        totalLabel = new JLabel("Total: $0");
        totalLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));

        totalsPanel.add(subtotalLabel);
        totalsPanel.add(taxLabel);
        totalsPanel.add(totalLabel);

        billPanel.add(totalsPanel, BorderLayout.SOUTH);

        // Layered Pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(sideMenuPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(scrollPane, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(productPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(billPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(productScrollPane, JLayeredPane.PALETTE_LAYER);

        // Set content pane and display
        setContentPane(layeredPane);
        setVisible(true);
    }

    private void displayProducts(String category) {
        productPanel.removeAll(); // Clear existing products

        // Get products from the database based on the selected category
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getProductsByCategory(category);

        // Add each product to the productPanel
        for (Product product : products) {
            JPanel productBox = new JPanel();
            productBox.setLayout(new BoxLayout(productBox, BoxLayout.Y_AXIS));
            productBox.setPreferredSize(new Dimension(150, 150));
            productBox.setBackground(Color.WHITE);
            productBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            String productName = product.getName();
            BigDecimal price = product.getPrice();

            JLabel nameLabel = new JLabel(productName);
            JLabel priceLabel = new JLabel("Price: $" + price);
            JButton addButton = new JButton("Add");

            addButton.addActionListener(e -> addToBill(product));

            productBox.add(nameLabel);
            productBox.add(priceLabel);
            productBox.add(addButton);

            productPanel.add(productBox);
        }

        productPanel.revalidate();
        productPanel.repaint();
    }


    // Method to add a product to the bill
    private void addToBill(Product product) {
        // Update the cart by adding the product or increasing its quantity
        String productName = product.getName();
        BigDecimal price =  product.getPrice(); // Assuming price is a double, cast to int for simplicity
        cart.put(productName, cart.getOrDefault(productName, 0) + 1);

        // Update bill panel
        JPanel billItemPanel = new JPanel();
        billItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        billItemPanel.setPreferredSize(new Dimension(320, 40));

        // Display product name with quantity
        JLabel itemLabel = new JLabel(productName + " x" + cart.get(productName));
        JLabel priceLabel = new JLabel("$" + price);
        billItemPanel.add(itemLabel);
        billItemPanel.add(priceLabel);

        billItemsPanel.add(billItemPanel); // Add the item to the bill items panel
        updateTotal(); // Update the total (subtotal, tax, and grand total)
    }


    // Method to update the subtotal, tax, and total amounts
    private void updateTotal() {
        double subtotal = 0;
        for (String product : cart.keySet()) {
            int price = 10; // Placeholder price, update as needed
            subtotal += price;
        }

        double tax = subtotal * TAX_PERCENTAGE / 100;
        double total = subtotal + tax;

        subtotalLabel.setText("Subtotal: $" + subtotal);
        taxLabel.setText("Tax (" + TAX_PERCENTAGE + "%): $" + tax);
        totalLabel.setText("Total: $" + total);
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CashierUI::new);
    }
}
