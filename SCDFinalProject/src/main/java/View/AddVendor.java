package View;

import Controller.VendorController;
import Model.Vendor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AddVendor extends JFrame {
    private JTextField nameField;
    private JTextField phoneField;
    private JCheckBox statusCheckBox;
    private final VendorController vendorController;

    public AddVendor() {
        vendorController = new VendorController();

        // Set up the JFrame
        setTitle("Add Vendor");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon("D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\logo.PNG").getImage());
        setLocationRelativeTo(null); // Center the window
        getContentPane().setBackground(Color.decode("#CCD4E5"));
        // Add components
        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel statusLabel = new JLabel("Status:");
        nameField = new JTextField();
        phoneField = new JTextField();
        statusCheckBox = new JCheckBox("Active");
        JButton addButton = new RoundedButton("Add Vendor",8);

        // Adjust component bounds and add to the frame
        int labelWidth = 100, labelHeight = 30;
        int fieldWidth = 200, fieldHeight = 30;
        int buttonWidth = 220, buttonHeight = 30;
        int spacing = 20;

        int centerX = getWidth() / 2; // Center X of the frame
        int currentY = 100; // Starting Y position

        // Name
        nameLabel.setBounds(centerX - labelWidth - spacing, currentY, labelWidth, labelHeight);
        nameField.setBounds(centerX, currentY, fieldWidth, fieldHeight);
        add(nameLabel);
        add(nameField);

        // Phone
        currentY += labelHeight + spacing;
        phoneLabel.setBounds(centerX - labelWidth - spacing, currentY, labelWidth, labelHeight);
        phoneField.setBounds(centerX, currentY, fieldWidth, fieldHeight);
        add(phoneLabel);
        add(phoneField);

        // Status
        currentY += labelHeight + spacing;
        statusLabel.setBounds(centerX - labelWidth - spacing, currentY, labelWidth, labelHeight);
        statusCheckBox.setBounds(centerX, currentY, fieldWidth, fieldHeight);
        add(statusLabel);
        add(statusCheckBox);

        // Button
        currentY += labelHeight + spacing * 2;
        addButton.setBounds(centerX - buttonWidth / 2, currentY, buttonWidth, buttonHeight);
        addButton.addActionListener(this::addVendorAction);
        add(addButton);

        // Apply font styles to all components
        setComponentFont(new Font("Century Gothic", Font.PLAIN, 18));
    }

    private void addVendorAction(ActionEvent e) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        boolean status = statusCheckBox.isSelected();

        // Validation checks
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and phone cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Vendor> existingVendors = vendorController.fetchAllVendors();
        boolean isDuplicate = existingVendors.stream()
                .anyMatch(v -> v.getName().equalsIgnoreCase(name) || v.getPhone().equals(phone));
        if (isDuplicate) {
            JOptionPane.showMessageDialog(this, "Vendor name or phone already exists.", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add vendor to the database
        Vendor vendor = new Vendor(0, name, phone, status); // ID will be auto-incremented in the DB
        if (vendorController.addVendor(vendor)) {
            JOptionPane.showMessageDialog(this, "Vendor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the window after successful addition
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add vendor. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setComponentFont(Font font) {
        for (Component component : getContentPane().getComponents()) {
            component.setFont(font);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddVendor().setVisible(true));
    }
}
