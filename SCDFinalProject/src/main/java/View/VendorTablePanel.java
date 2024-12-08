package View;

import Controller.VendorController;
import Model.Vendor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VendorTablePanel extends JPanel {
    JTable vendorTable;
    private DefaultTableModel tableModel;
    private final VendorController vendorController;

    public VendorTablePanel() {
        this.vendorController = new VendorController();
        setLayout(null); // Disable layout manager to use manual positioning
        initializeTable();
        loadVendors();
    }

    // Initialize the table with columns
    private void initializeTable() {
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Status", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // ID column is not editable
            }
        };

        vendorTable = new JTable(tableModel);
        vendorTable.setRowHeight(30);

        // Set up the Action button column
        ButtonColumn buttonColumn = new ButtonColumn(vendorTable, this::updateVendor, 4);
    }

    // Load vendors from the database into the table
    private void loadVendors() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Vendor> vendors = vendorController.fetchAllVendors();

        for (Vendor vendor : vendors) {
            tableModel.addRow(new Object[]{
                    vendor.getVendorId(),
                    vendor.getName(),
                    vendor.getPhone(),
                    vendor.isStatus() ? "Active" : "Inactive",
                    "Update" // This is just a placeholder for the button
            });
        }
    }

    private void updateVendor(ActionEvent e) {
        // Find the row that was clicked
        int rowIndex = vendorTable.getSelectedRow();

        if (rowIndex != -1) {
            int vendorId = (int) tableModel.getValueAt(rowIndex, 0);

            // Fetch the vendor object by its ID
            Vendor vendor = vendorController.fetchAllVendors().stream()
                    .filter(v -> v.getVendorId() == vendorId)
                    .findFirst()
                    .orElse(null);

            // If vendor is found, open the edit dialog
            if (vendor != null) {
                // Get the parent window (it may be a JFrame or JDialog)
                Window parentWindow = SwingUtilities.getWindowAncestor(this);

                if (parentWindow instanceof Frame) {
                    // Cast to Frame if it's an instance of Frame (like JFrame)
                    new VendorEditDialog((Frame) parentWindow, vendor);
                } else {
                    // In case the parent is not a Frame, we need to find a JFrame or something that is a Frame
                    Frame parentFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);
                    if (parentFrame != null) {
                        new VendorEditDialog(parentFrame, vendor); // Pass the parent Frame
                    } else {
                        JOptionPane.showMessageDialog(this, "Unable to find a valid parent Frame.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                loadVendors(); // Reload the vendors after update
            }
        }
    }

    // Method to set the size and bounds of the table manually
    public void setTableBounds(int x, int y, int width, int height) {
        vendorTable.setBounds(x, y, width, height); // Set bounds of the table
    }

//    // Main method to launch the dialog with the vendor table
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            // Create the dialog
//            JDialog dialog = new JDialog((Frame) null, "Vendor Management", true);
//
//            // Manually set position and size for the dialog window
//            dialog.setBounds(275, 0, 1020, 800); // Position it 200px from top left and size it 800x500px
//            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//
//            // Create the panel
//            VendorTablePanel panel = new VendorTablePanel();
//            dialog.add(panel);
//
//            // Set bounds manually for the table
//            panel.setTableBounds(50, 50, 700, 350); // Set table bounds manually (x, y, width, height)
//
//            // Create a JScrollPane and manually set bounds for it as well
//            JScrollPane scrollPane = new JScrollPane(panel.vendorTable);
//            scrollPane.setBounds(50, 50, 700, 350); // Set bounds for JScrollPane manually
//            dialog.add(scrollPane);  // Add scroll pane to dialog
//
//            dialog.setVisible(true); // Show the dialog
//        });
//    }
}
