package View;

import Controller.VendorController;
import Model.Vendor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class AddVendor extends JFrame {
    private JTextField nameField;
    private JTextField phoneField;
    private JCheckBox statusCheckBox;
    private final VendorController vendorController;

    public AddVendor() {
        vendorController = new VendorController();

        setTitle("Add Vendor");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        try {
            ImageIcon icon = new ImageIcon(
                    Objects.requireNonNull(getClass().getClassLoader().getResource("images/icons/logo.PNG"))
            );
            setIconImage(icon.getImage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load icon image.");
        }

        setLocationRelativeTo(null); // Center the window
        getContentPane().setBackground(Color.decode("#CCD4E5"));

        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel statusLabel = new JLabel("Status:");
        nameField = new JTextField();
        phoneField = new JTextField();
        statusCheckBox = new JCheckBox("Active");
        JButton addButton = new RoundedButton("Add Vendor",8);

        int labelWidth = 100, labelHeight = 30;
        int fieldWidth = 200, fieldHeight = 30;
        int buttonWidth = 220, buttonHeight = 30;
        int spacing = 20;

        int centerX = getWidth() / 2;
        int currentY = 100;


        nameLabel.setBounds(centerX - labelWidth - spacing, currentY, labelWidth, labelHeight);
        nameField.setBounds(centerX, currentY, fieldWidth, fieldHeight);
        add(nameLabel);
        add(nameField);

        currentY += labelHeight + spacing;
        phoneLabel.setBounds(centerX - labelWidth - spacing, currentY, labelWidth, labelHeight);
        phoneField.setBounds(centerX, currentY, fieldWidth, fieldHeight);
        add(phoneLabel);
        add(phoneField);

        currentY += labelHeight + spacing;
        statusLabel.setBounds(centerX - labelWidth - spacing, currentY, labelWidth, labelHeight);
        statusCheckBox.setBounds(centerX, currentY, fieldWidth, fieldHeight);
        add(statusLabel);
        add(statusCheckBox);


        currentY += labelHeight + spacing * 2;
        addButton.setBounds(centerX - buttonWidth / 2, currentY, buttonWidth, buttonHeight);
        addButton.addActionListener(this::addVendorAction);
        add(addButton);

        setComponentFont(new Font("Century Gothic", Font.PLAIN, 18));
    }

    private void addVendorAction(ActionEvent e) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        boolean status = statusCheckBox.isSelected();

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
        Vendor vendor = new Vendor(0, name, phone, status);
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

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new AddVendor().setVisible(true));
//    }
}
