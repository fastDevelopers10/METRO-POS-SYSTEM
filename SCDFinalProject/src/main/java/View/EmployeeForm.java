package View;

import DAO.EmployeeDAO;
import Model.Employee;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;

public class EmployeeForm extends JFrame {
    private JTextField nameField, emailField, addressField, phoneField, salaryField, usernameField;
    private JComboBox<String> positionComboBox;
    private JComboBox<Integer> branchComboBox;
    private JButton submitButton, cancelButton;
    private int branchid;
    String selectedBranch;
    private BranchManagerTableUI bmUI;
    public EmployeeForm(String []roles, int branchid) throws SQLException {
        this.branchid=branchid;
        this.bmUI=new BranchManagerTableUI();
        setTitle("Add New Employee");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel to hold form components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2, 10, 10));

        // Add fields
        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Salary:"));
        salaryField = new JTextField();
        panel.add(salaryField);

        panel.add(new JLabel("Position:"));
        positionComboBox = new JComboBox<>(roles);
        panel.add(positionComboBox);



        // Buttons
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        panel.add(submitButton);
        panel.add(cancelButton);

        // Add action listeners
        submitButton.addActionListener(e -> submitForm());
        cancelButton.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }
     public EmployeeForm(String[] roles, List<Integer> branchIds) {
        setTitle("Add New Employee");
        setSize(400, 500);  // Increase size to accommodate branch combo box
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel to hold form components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));  // Adjusted to accommodate branch field

        // Add fields
        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Salary:"));
        salaryField = new JTextField();
        panel.add(salaryField);

        panel.add(new JLabel("Position:"));
        positionComboBox = new JComboBox<>(roles);
        panel.add(positionComboBox);

        // Add Branch selection field
        panel.add(new JLabel("Branch:"));
        branchComboBox = new JComboBox<>();
        // Populate branch combo box with branch IDs
        for (Integer branchId : branchIds) {
            branchComboBox.addItem(branchId);  // Adding branch IDs directly
        }
        panel.add(branchComboBox);

        // Buttons
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        panel.add(submitButton);
        panel.add(cancelButton);

        // Add action listeners
        submitButton.addActionListener(e -> submitFormSA());
        cancelButton.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }


    private void submitForm() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();

            // Validate email format
            if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Invalid email address. Please enter a valid email.");
                return; // Exit the method if validation fails
            }

            // Validate phone number length
            if (phone.length() != 11 || !phone.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Phone number must be exactly 11 digits.");
                return; // Exit the method if validation fails
            }

            BigDecimal salary = new BigDecimal(salaryField.getText());
            String position = (String) positionComboBox.getSelectedItem();
            int branchId = branchid;

            Date joiningDate = new Date(System.currentTimeMillis());

            // Create an Employee object
            Employee employee = new Employee(name, email, position, branchId, address, phone, salary, joiningDate, "Active");

            // Call DAO to insert employee
            EmployeeDAO empDAO = new EmployeeDAO();
            boolean isInserted = empDAO.insertEmployee(employee);

            if (isInserted) {
                JOptionPane.showMessageDialog(this, "Employee added successfully with default password '123'!");
                dispose(); // Close the form
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee. Try again.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void submitFormSA() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();

            // Validate email format
            if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Invalid email address. Please enter a valid email.");
                return; // Exit the method if validation fails
            }

            // Validate phone number length
            if (phone.length() != 11 || !phone.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Phone number must be exactly 11 digits.");
                return; // Exit the method if validation fails
            }

            BigDecimal salary = new BigDecimal(salaryField.getText());
            String position = (String) positionComboBox.getSelectedItem();
            int branchId = (int) branchComboBox.getSelectedItem();

            Date joiningDate = new Date(System.currentTimeMillis());

            // Create an Employee object
            Employee employee = new Employee(name, email, position, branchId, address, phone, salary, joiningDate, "Active");

            // Call DAO to insert employee
            EmployeeDAO empDAO = new EmployeeDAO();
            boolean isInserted = empDAO.insertEmployee(employee);

            if (!isInserted) {
                if ("Branch Manager".equalsIgnoreCase(position)) {
                    JOptionPane.showMessageDialog(this, "Error: A Branch Manager already exists for this branch.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add employee. Try again.");
                }
            }

            if (isInserted) {
                JOptionPane.showMessageDialog(this, "Employee added successfully with default password '123'!");
                dispose();
//                bmUI.refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee. Try again.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
