package View;

import DAO.EmployeeDAO;
import Model.Employee;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;

public class EmployeeForm extends JFrame {
    private JTextField nameField, emailField, addressField, phoneField, salaryField, usernameField;
    private JComboBox<String> positionComboBox;
    private JComboBox<Integer> branchComboBox;
    private JButton submitButton, cancelButton;
    private int branchid;
    public EmployeeForm(String []roles, int branchid) {
        this.branchid=branchid;

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

    private void submitForm() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            BigDecimal salary = new BigDecimal(salaryField.getText());
            String position = (String) positionComboBox.getSelectedItem();
            int branchId = branchid;

            Date joiningDate = new Date(System.currentTimeMillis());

            // Create an Employee object
            Employee employee = new Employee(name, email,position, branchId, address, phone, salary, joiningDate,"Active");

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


}
