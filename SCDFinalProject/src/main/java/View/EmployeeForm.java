package View;

import Model.Employee;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import DAO.EmployeeDAO;

public class EmployeeForm extends JFrame {
    private JTextField nameField, emailField, addressField, phoneField, salaryField, usernameField;
    private JComboBox<String> positionComboBox;
    private JComboBox<Integer> branchComboBox;
    private JButton submitButton, cancelButton;
    private int branchid;
    public EmployeeForm(String []roles, int branchid) throws SQLException {
        this.branchid=branchid;
        setTitle("Add New Employee");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2, 10, 10));

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

        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        panel.add(submitButton);
        panel.add(cancelButton);

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

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));  // Adjusted to accommodate branch field

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

        panel.add(new JLabel("Branch:"));
        branchComboBox = new JComboBox<>();
        for (Integer branchId : branchIds) {
            branchComboBox.addItem(branchId);  // Adding branch IDs directly
        }
        panel.add(branchComboBox);

        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        panel.add(submitButton);
        panel.add(cancelButton);

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

            if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Invalid email address. Please enter a valid email.");
                return;
            }

            if (phone.length() != 11 || !phone.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Phone number must be exactly 11 digits.");
                return;
            }

            BigDecimal salary = new BigDecimal(salaryField.getText());
            String position = (String) positionComboBox.getSelectedItem();
            int branchId = branchid;

            Date joiningDate = new Date(System.currentTimeMillis());

            Employee employee = new Employee(name, email, position, branchId, address, phone, salary, joiningDate, "Active");
             EmployeeDAO empDAO = new EmployeeDAO();
            boolean isInserted = empDAO.insertEmployee(employee);

            if (isInserted) {
                JOptionPane.showMessageDialog(this, "Employee added successfully with default password '123'!");
                dispose();
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

            if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Invalid email address. Please enter a valid email.");
                return;
            }

            if (phone.length() != 11 || !phone.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Phone number must be exactly 11 digits.");
                return;
            }

            BigDecimal salary = new BigDecimal(salaryField.getText());
            String position = (String) positionComboBox.getSelectedItem();
            int branchId = (int) branchComboBox.getSelectedItem();

            Date joiningDate = new Date(System.currentTimeMillis());

            Employee employee = new Employee(name, email, position, branchId, address, phone, salary, joiningDate, "Active");
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
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee. Try again.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}