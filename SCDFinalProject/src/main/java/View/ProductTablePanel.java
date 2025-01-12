package View;

import Controller.ProductController;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        setLayout(null);
        initializeComponents();
        loadProducts("");
    }

    private void initializeComponents() {
        // Search bar
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        searchLabel.setBounds(50, 20, 60, 30);
        add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        searchField.setBounds(120, 20, 300, 30);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });
        add(searchField);

        // Table model with columns
        tableModel = new DefaultTableModel(new String[]{
                "ID", "Name", "Category", "Quantity", "Original Price", "Sales Price", "Status", "Action"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setRowHeight(30); // Fixed row height
        productTable.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        productTable.getTableHeader().setFont(new Font("Century Gothic", Font.BOLD, 14));
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Set table to be non-resizable

        new ButtonColumn(productTable, new DeleteButtonActionListener(), 7);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(50, 70, 900, 600);
        add(scrollPane);

        // Ensure columns adjust to the table size
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        productTable.getTableHeader().setReorderingAllowed(false); // Disable column reordering
    }

    private void loadProducts(String searchQuery) {
        tableModel.setRowCount(0); // Clear the table
        List<Product> products = productController.fetchProductsByBranchForTable(branchId, searchQuery);

        for (Product product : products) {
            String status = (product.getQuantity() == 0) ? "Inactive" : (product.isStatus() ? "Active" : "Inactive");
            tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getQuantity(),
                    product.getOriginalPrice(),
                    product.getSalesPrice(),
                    status,
                    "Delete"
            });
        }
        filterTable(""); // Apply filter with empty query to reset the table sorting
    }

    // ActionListener for the delete button
    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = productTable.getSelectedRow();
            if (row != -1) {
                int productId = (int) tableModel.getValueAt(row, 0);
                String status = (String) tableModel.getValueAt(row, 6);

                if ("Inactive".equals(status)) {
                    int confirm = JOptionPane.showConfirmDialog(
                            ProductTablePanel.this,
                            "Are you sure you want to delete this product?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean deleted = productController.deleteProduct(productId);
                        if (deleted) {
                            tableModel.removeRow(row); // Remove the row from the table
                            JOptionPane.showMessageDialog(
                                    ProductTablePanel.this,
                                    "Product deleted successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    ProductTablePanel.this,
                                    "Failed to delete the product.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            ProductTablePanel.this,
                            "Only inactive products with quantity 0 can be deleted.",
                            "Invalid Action",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        }
    }

    private void filterTable(String query) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        productTable.setRowSorter(sorter);

        if (query.trim().isEmpty()) {
            sorter.setRowFilter(null); // Show all rows
        } else {
            RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + query.trim());
            sorter.setRowFilter(rowFilter);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Product Table");
            frame.setBounds(275, 0, 1020, 800);
            frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            ProductTablePanel productTable = new ProductTablePanel(1);
            frame.add(productTable);
            frame.setVisible(true);
        });
    }
}
