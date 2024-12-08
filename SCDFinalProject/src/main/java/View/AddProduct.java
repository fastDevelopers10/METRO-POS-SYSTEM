package View;

import Controller.ProductController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class AddProduct extends JFrame {
    private JComboBox<String> vendorDropdown;
    private JComboBox<String> productNameDropdown;
    private JComboBox<String> productCategoryDropdown;
    private JTextField cartonsField;
    private JTextField itemsPerCartonField;
    private JTextField originalPriceField;
    private JTextField salesPriceField;
    private JTextField newCategoryField;
    private JTextField newProductField;
    private final int branchId;

    public AddProduct(int branchId) {
        this.branchId = branchId;
        setTitle("Add Product");

        try {
            ImageIcon icon = new ImageIcon(
                    Objects.requireNonNull(getClass().getClassLoader().getResource("images/icons/logo.PNG"))
            );
            setIconImage(icon.getImage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load icon image.");
        }

        setLayout(null);
        getContentPane().setBackground(Color.decode("#CCD4E5"));
        setBounds(300, 200, 800, 650); // Increased frame height by 50
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add heading
        JLabel headingLabel = new JLabel("Add or Update Stock", JLabel.CENTER);
        headingLabel.setFont(new Font("Century Gothic", Font.BOLD, 22));
        headingLabel.setBounds(50, 20, 700, 30); // Positioned at the top and centered
        headingLabel.setForeground(Color.BLACK);

        // Initialize components
        JLabel vendorLabel = new JLabel("Select Vendor:");
        vendorDropdown = new JComboBox<>();

        JLabel productNameLabel = new JLabel("Select Product:");
        productNameDropdown = new JComboBox<>();

        JLabel productCategoryLabel = new JLabel("Select Category:");
        productCategoryDropdown = new JComboBox<>();

        JLabel cartonsLabel = new JLabel("Cartons Purchased:");
        cartonsField = new JTextField();

        JLabel itemsPerCartonLabel = new JLabel("Items Per Carton:");
        itemsPerCartonField = new JTextField();

        JLabel originalPriceLabel = new JLabel("Original Price:");
        originalPriceField = new JTextField();

        JLabel salesPriceLabel = new JLabel("Sales Price:");
        salesPriceField = new JTextField();

        JLabel newCategoryLabel = new JLabel("New Category:");
        newCategoryField = new JTextField();

        JLabel newProductLabel = new JLabel("New Product:");
        newProductField = new JTextField();

        JButton submitButton = new RoundedButton("Submit",8);

        // Set font for all components
        Font font = new Font("Century Gothic", Font.PLAIN, 16);
        vendorLabel.setFont(font);
        productNameLabel.setFont(font);
        productCategoryLabel.setFont(font);
        cartonsLabel.setFont(font);
        itemsPerCartonLabel.setFont(font);
        originalPriceLabel.setFont(font);
        salesPriceLabel.setFont(font);
        newCategoryLabel.setFont(font);
        newProductLabel.setFont(font);
        submitButton.setFont(font);

        vendorDropdown.setFont(font);
        productNameDropdown.setFont(font);
        productCategoryDropdown.setFont(font);
        cartonsField.setFont(font);
        itemsPerCartonField.setFont(font);
        originalPriceField.setFont(font);
        salesPriceField.setFont(font);
        newCategoryField.setFont(font);
        newProductField.setFont(font);

        // Adjust bounds for centralization
        int baseX = 350; // Centralize horizontally
        int labelWidth = 180;
        int fieldWidth = 300;
        int height = 30;
        int verticalSpacing = 50;

        vendorLabel.setBounds(baseX - labelWidth, 80, labelWidth, height);
        vendorDropdown.setBounds(baseX, 80, fieldWidth, height);

        productNameLabel.setBounds(baseX - labelWidth, 80 + verticalSpacing, labelWidth, height);
        productNameDropdown.setBounds(baseX, 80 + verticalSpacing, fieldWidth, height);

        productCategoryLabel.setBounds(baseX - labelWidth, 80 + 2 * verticalSpacing, labelWidth, height);
        productCategoryDropdown.setBounds(baseX, 80 + 2 * verticalSpacing, fieldWidth, height);

        newCategoryLabel.setBounds(baseX - labelWidth, 80 + 3 * verticalSpacing, labelWidth, height);
        newCategoryField.setBounds(baseX, 80 + 3 * verticalSpacing, fieldWidth, height);

        newProductLabel.setBounds(baseX - labelWidth, 80 + 4 * verticalSpacing, labelWidth, height);
        newProductField.setBounds(baseX, 80 + 4 * verticalSpacing, fieldWidth, height);

        cartonsLabel.setBounds(baseX - labelWidth, 80 + 5 * verticalSpacing, labelWidth, height);
        cartonsField.setBounds(baseX, 80 + 5 * verticalSpacing, fieldWidth, height);

        itemsPerCartonLabel.setBounds(baseX - labelWidth, 80 + 6 * verticalSpacing, labelWidth, height);
        itemsPerCartonField.setBounds(baseX, 80 + 6 * verticalSpacing, fieldWidth, height);

        originalPriceLabel.setBounds(baseX - labelWidth, 80 + 7 * verticalSpacing, labelWidth, height);
        originalPriceField.setBounds(baseX, 80 + 7 * verticalSpacing, fieldWidth, height);

        salesPriceLabel.setBounds(baseX - labelWidth, 80 + 8 * verticalSpacing, labelWidth, height);
        salesPriceField.setBounds(baseX, 80 + 8 * verticalSpacing, fieldWidth, height);

        submitButton.setBounds(baseX + 100, 80 + 9 * verticalSpacing, 100, 40);

        // Add components to frame
        add(headingLabel);
        add(vendorLabel);
        add(vendorDropdown);
        add(productNameLabel);
        add(productNameDropdown);
        add(productCategoryLabel);
        add(productCategoryDropdown);
        add(newCategoryLabel);
        add(newCategoryField);
        add(newProductLabel);
        add(newProductField);
        add(cartonsLabel);
        add(cartonsField);
        add(itemsPerCartonLabel);
        add(itemsPerCartonField);
        add(originalPriceLabel);
        add(originalPriceField);
        add(salesPriceLabel);
        add(salesPriceField);
        add(submitButton);

        loadVendors();
        loadProductsAndCategories();

        submitButton.addActionListener(e -> handleSubmit());

        newCategoryField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                toggleCategoryDropdown();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                toggleCategoryDropdown();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                toggleCategoryDropdown();
            }
        });

        newProductField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                toggleProductDropdown();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                toggleProductDropdown();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                toggleProductDropdown();
            }
        });
    }

    private void toggleCategoryDropdown() {
        if (!newCategoryField.getText().trim().isEmpty()) {
            productCategoryDropdown.setEnabled(false);  // Disable category dropdown
        } else {
            productCategoryDropdown.setEnabled(true);   // Enable category dropdown
        }
    }

    private void toggleProductDropdown() {
        if (!newProductField.getText().trim().isEmpty()) {
            productNameDropdown.setEnabled(false);  // Disable product dropdown
        } else {
            productNameDropdown.setEnabled(true);   // Enable product dropdown
        }
    }

    private void loadVendors() {
        ProductController controller = new ProductController();
        List<String> vendors = controller.getVendors();
        vendorDropdown.addItem("Select Vendor");
        for (String vendor : vendors) {
            vendorDropdown.addItem(vendor);
        }
    }

    private void loadProductsAndCategories() {
        loadProducts();
        loadCategories();
    }

    private void loadProducts() {
        ProductController controller = new ProductController();
        List<String> products = controller.getProductsByBranch(branchId);
        productNameDropdown.addItem("Select Product");
        for (String product : products) {
            productNameDropdown.addItem(product);
        }
    }

    private void loadCategories() {
        ProductController controller = new ProductController();
        List<String> categories = controller.getCategories(branchId);
        productCategoryDropdown.addItem("Select Category");
        for (String category : categories) {
            productCategoryDropdown.addItem(category);
        }
    }

    private void handleSubmit() {
        String selectedVendor = (String) vendorDropdown.getSelectedItem();
        String selectedProduct = (String) productNameDropdown.getSelectedItem();
        String selectedCategory = (String) productCategoryDropdown.getSelectedItem();
        String newCategory = newCategoryField.getText().trim();
        String newProduct = newProductField.getText().trim();

        // Determine final product name and category
        String finalProductName = newProduct.isEmpty() ? selectedProduct : newProduct;
        String finalCategory = newCategory.isEmpty() ? selectedCategory : newCategory;

        // Input validation
        if (selectedVendor == null || selectedVendor.equals("Select Vendor") ||
                finalProductName == null || finalProductName.equals("Select Product") ||
                finalCategory == null || finalCategory.equals("Select Category")) {
            JOptionPane.showMessageDialog(this, "Please select valid options from dropdowns.");
            return;
        }

        try {
            int cartons = Integer.parseInt(cartonsField.getText());
            int itemsPerCarton = Integer.parseInt(itemsPerCartonField.getText());
            double originalPrice = Double.parseDouble(originalPriceField.getText());
            double salesPrice = Double.parseDouble(salesPriceField.getText());

            // Generate the current date for purchaseDate
            java.sql.Date purchaseDate = new java.sql.Date(System.currentTimeMillis());

            ProductController controller = new ProductController();
            boolean success = controller.addOrUpdateProductAction(
                    branchId,
                    selectedVendor,
                    finalProductName.trim(),
                    finalCategory.trim(),
                    cartons,
                    itemsPerCarton,
                    originalPrice,
                    salesPrice,
                    purchaseDate
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "Product added successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
        }
    }
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new AddProduct(1).setVisible(true));
//    }
}
