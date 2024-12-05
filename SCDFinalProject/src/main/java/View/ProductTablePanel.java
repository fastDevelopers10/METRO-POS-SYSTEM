package View;

import Controller.ProductController;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ProductTablePanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private final ProductController productController;
    private final int branchId;
    private JTextField searchField;

    public ProductTablePanel(int branchId) {
        this.branchId = branchId;
        this.productController = new ProductController();
        setLayout(null); // Manual positioning of components
        initializeComponents();
        loadProducts(""); // Load all products initially
    }

    private void initializeComponents() {
        // Search bar
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        searchLabel.setBounds(50, 20, 60, 30); // Adjusted position
        add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        searchField.setBounds(120, 20, 300, 30); // Adjusted position
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadProducts(searchField.getText());
            }
        });
        add(searchField);

        // Table model with columns
        tableModel = new DefaultTableModel(new String[]{
                "ID", "Name", "Category", "Quantity", "Original Price", "Sales Price", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        };

        productTable = new JTable(tableModel);
        productTable.setRowHeight(30);

        // Add scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(50, 70, 900, 600); // Adjusted position and size
        add(scrollPane);
    }

    private void loadProducts(String searchQuery) {
        tableModel.setRowCount(0); // Clear the table
        List<Product> products = productController.fetchProductsByBranch(branchId, searchQuery);

        for (Product product : products) {
            System.out.println("Product ID: " + product.getId()); // Debugging output
            tableModel.addRow(new Object[]{
                    product.getId(), // Ensure the ID is correctly added
                    product.getName(),
                    product.getCategory(),
                    product.getQuantity(),
                    product.getOriginalPrice(),
                    product.getSalesPrice(),
                    product.isStatus() ? "Active" : "Inactive"
            });
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main application frame
            JFrame frame = new JFrame("Product Table - Branch 1");
            frame.setIconImage(new ImageIcon("D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\logo.PNG").getImage());
            frame.setBounds(275, 0, 1020, 800); // Set bounds on the screen
            frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // Create an instance of ProductTablePanel with branchId = 1
            ProductTablePanel productTable = new ProductTablePanel(1);

            // Add the ProductTable JPanel to the frame
            frame.add(productTable);

            // Make the frame visible
            frame.setVisible(true);
        });
    }
}
