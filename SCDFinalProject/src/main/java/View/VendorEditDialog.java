package View;

import Controller.VendorController;
import Model.Vendor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VendorEditDialog extends JDialog {
    private final VendorController vendorController;
    private final Vendor vendor; // The vendor to be edited
    private JTextField nameField;
    private JTextField phoneField;
    private JComboBox<String> statusComboBox;

    private int dialogWidth = 400;  // Default width
    private int dialogHeight = 300; // Default height
    private int dialogX = 600;      // Default X position
    private int dialogY = 150;      // Default Y position

    // Constructor with frame parent and vendor object
    public VendorEditDialog(Frame parent, Vendor vendor) {
        super(parent, "Edit Vendor", true);
        this.vendorController = new VendorController();
        this.vendor = vendor;

        // Default size and position settings
        setSize(dialogWidth, dialogHeight);
        setLocation(dialogX, dialogY);
        setLayout(null);  // Use absolute positioning

        // Vendor ID is non-editable
        JLabel idLabel = new JLabel("Vendor ID:");
        idLabel.setBounds(30, 30, 100, 25);
        add(idLabel);

        JTextField idField = new JTextField(String.valueOf(vendor.getVendorId()));
        idField.setEditable(false);
        idField.setBounds(140, 30, 200, 25);
        add(idField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 70, 100, 25);
        add(nameLabel);

        nameField = new JTextField(vendor.getName());
        nameField.setBounds(140, 70, 200, 25);
        add(nameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(30, 110, 100, 25);
        add(phoneLabel);

        phoneField = new JTextField(vendor.getPhone());
        phoneField.setBounds(140, 110, 200, 25);
        add(phoneField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(30, 150, 100, 25);
        add(statusLabel);

        statusComboBox = new JComboBox<>(new String[]{"Active", "Inactive"});
        statusComboBox.setSelectedItem(vendor.isStatus() ? "Active" : "Inactive");
        statusComboBox.setBounds(140, 150, 200, 25);
        add(statusComboBox);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(140, 190, 100, 30);
        saveButton.addActionListener(this::saveVendor);
        add(saveButton);

        setVisible(true);
    }

    // Method to allow the user to set the size
    public void setDialogSize(int width, int height) {
        this.dialogWidth = width;
        this.dialogHeight = height;
        setSize(width, height);
    }

    // Method to allow the user to set the position (bounds)
    public void setDialogBounds(int x, int y, int width, int height) {
        this.dialogX = x;
        this.dialogY = y;
        setBounds(x, y, width, height);
    }

    private void saveVendor(ActionEvent e) {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String status = (String) statusComboBox.getSelectedItem();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and phone cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update the vendor object with the new values
        vendor.setName(name);
        vendor.setPhone(phone);
        vendor.setStatus("Active".equals(status));

        // Call the controller to update the vendor in the database
        if (vendorController.modifyVendor(vendor)) {
            JOptionPane.showMessageDialog(this, "Vendor updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the dialog
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update vendor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
