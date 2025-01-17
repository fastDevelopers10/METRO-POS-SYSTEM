package View;

import Controller.ProductController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductsFromVendors {

    private final ProductController controller;

    public ProductsFromVendors() {
        this.controller = new ProductController();
    }

    public void displayHierarchy(int branchId) {
        // Fetch data from the controller
        List<Map<String, Object>> data = controller.fetchVendorProductsByBranchforvendor(branchId);

        // Build tree structure
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Branch " + branchId);

        data.stream()
                .collect(Collectors.groupingBy(row -> row.get("vendor_name"))) // Group by vendor
                .forEach((vendor, products) -> {
                    DefaultMutableTreeNode vendorNode = new DefaultMutableTreeNode(vendor);
                    root.add(vendorNode);

                    products.stream()
                            .collect(Collectors.groupingBy(row -> row.get("product_category"))) // Group by category
                            .forEach((category, items) -> {
                                DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
                                vendorNode.add(categoryNode);
                            });
                });

        // Create JTree
        JTree tree = new JTree(new DefaultTreeModel(root));
        tree.setFont(new Font("Century Gothic", Font.PLAIN, 18));

        // Table to display product details
        JTable detailsTable = new JTable();
        detailsTable.setModel(new DefaultTableModel(
                new Object[]{"Vendor", "Category", "Product", "Original Price", "Sales Price", "Cartons", "Items/Carton", "Total", "Date"},
                0
        ));
        detailsTable.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        detailsTable.setRowHeight(20);
        detailsTable.getTableHeader().setFont(new Font("Century Gothic", Font.BOLD, 14));
        detailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Set table to be non-resizable

        // Search Bar
        JTextField searchBar = new JTextField();
        searchBar.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        searchBar.addActionListener(e -> filterTable(detailsTable, searchBar.getText()));

        // Add selection listener to JTree
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode == null) {
                return; // Exit if nothing is selected
            }

            Object userObject = selectedNode.getUserObject();
            if (userObject == null) {
                return; // Exit if the node has no user object
            }

            // Identify the vendor and category
            String selectedCategory = null;
            String selectedVendor = null;

            if (selectedNode.isLeaf()) { // Leaf node is a category
                selectedCategory = userObject.toString();
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                if (parentNode != null) {
                    selectedVendor = parentNode.getUserObject().toString();
                }
            }

            if (selectedCategory != null && selectedVendor != null) {
                // Populate the table based on selected category and vendor
                populateTableForCategoryAndVendor(detailsTable, data, selectedCategory, selectedVendor);
            }
        });

        // Layout components
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        searchPanel.add(searchBar, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(detailsTable), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree), tablePanel);
        splitPane.setDividerLocation(300);

        // Set up JFrame
        JFrame frame = new JFrame("Products from Vendors - Branch " + branchId);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(275, 0, 1020, 800);
        frame.setLayout(new BorderLayout());
        frame.add(splitPane, BorderLayout.CENTER);

        frame.setResizable(true); // Allow user to resize
        frame.setVisible(true);
    }

    private void populateTableForCategoryAndVendor(JTable table, List<Map<String, Object>> data, String selectedCategory, String selectedVendor) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear previous data

        data.stream()
                .filter(row -> row.get("product_category").equals(selectedCategory) &&
                        row.get("vendor_name").equals(selectedVendor))
                .forEach(row -> model.addRow(new Object[]{
                        row.get("vendor_name"),
                        row.get("product_category"),
                        row.get("product_name"),
                        row.get("original_price"),
                        row.get("sales_price"),
                        row.get("cartons_purchased"),
                        row.get("items_per_carton"),
                        row.get("products_purchased"),
                        row.get("purchase_date")
                }));
    }

    private void filterTable(JTable table, String query) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        if (query.trim().isEmpty()) {
            sorter.setRowFilter(null); // Show all rows
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query.trim())); // Case-insensitive filter
        }
    }

    public static void main(String[] args) {
        ProductsFromVendors ui = new ProductsFromVendors();
        ui.displayHierarchy(1);
    }
}
