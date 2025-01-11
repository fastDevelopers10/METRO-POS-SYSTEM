package View;

import Controller.BranchController;
import Controller.EmployeeController;
import Model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class BranchManagerTableUI extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeController controller;
    private JComboBox<String> statusDropdown;

    public BranchManagerTableUI() throws SQLException {
        setTitle("METRO");
        setSize(1320, 710);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Set the frame icon
        URL iconURL = getClass().getClassLoader().getResource("images/icons/logo.PNG");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        } else {
            System.err.println("Error: Unable to load frame icon image.");
        }

        // Background image
        ImageIcon backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("images/Super Admin Dashboard.png"));
        JLabel lblBackground = new JLabel(backgroundImage);
        lblBackground.setBounds(0, 0, 1320, 710);
        add(lblBackground);

        // Header Label
        JLabel header = new JLabel("Branch Managers", SwingConstants.CENTER);
        header.setFont(new Font("Century Gothic", Font.BOLD, 24));
        header.setOpaque(true);
        header.setBackground(Color.decode("#e6e6e7"));
        header.setForeground(Color.BLACK);
        header.setBounds(273, 0, 1000, 50); // Position header
        lblBackground.add(header);

        // Sidebar menu with buttons
        SideBarMenuUtil.createSidebarMenu(lblBackground, this);

        // Status Dropdown
        String[] statusOptions = {"All Branch Managers", "Active", "Inactive"};
        statusDropdown = new JComboBox<>(statusOptions);
        statusDropdown.setBackground(Color.decode("#e6e6e7"));
        statusDropdown.setForeground(Color.BLACK);
        statusDropdown.setFont(new Font("Century Gothic", Font.BOLD, 16));
        statusDropdown.setSelectedIndex(0); // Default to "All Branch Managers"
        statusDropdown.setBounds(1020, 65, 200, 40);  // Position dropdown
        statusDropdown.addActionListener(e -> updateTableBasedOnStatus());
        lblBackground.add(statusDropdown);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Branch Manager");
        addButton.setBackground(Color.WHITE);
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Century Gothic", Font.PLAIN, 18));

        JButton updateButton = new JButton("Update Branch Manager");
        updateButton.setBackground(Color.WHITE);
        updateButton.setForeground(Color.BLACK);
        updateButton.setFont(new Font("Century Gothic", Font.PLAIN, 18));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.setBounds(320, 520, 860, 150);
        buttonPanel.setBackground(new Color(0xf9f9f9));
        lblBackground.add(buttonPanel);

        addButton.addActionListener(e -> {
            String[] positions = {"Branch Manager"};
            BranchController bc = new BranchController();
            List<Integer> branchIds = bc.getAllBranchIds();
            EmployeeForm form = new EmployeeForm(positions, branchIds);

            refreshTable();

        });

        updateButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a branch manager to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = tableModel.getValueAt(selectedRow, 0).toString();
            String name = tableModel.getValueAt(selectedRow, 1).toString();
            String email = tableModel.getValueAt(selectedRow, 2).toString();
            String branchId = tableModel.getValueAt(selectedRow, 3).toString();
            String address = tableModel.getValueAt(selectedRow, 4).toString();
            String phone = tableModel.getValueAt(selectedRow, 5).toString();
            String salary = tableModel.getValueAt(selectedRow, 6).toString();
            String status = tableModel.getValueAt(selectedRow, 8).toString();

            // Initialize the BranchController with an Employee object
            Employee selectedEmployee = null; // Fetch the employee data
            try {
                selectedEmployee = controller.getEmployeeById(Integer.parseInt(id));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            // Fetch branch IDs dynamically
            BranchController bc = new BranchController();
            List<Integer> branchIds = bc.getAllBranchIds();
            JComboBox<Integer> branchIdDropdown = new JComboBox<>(branchIds.toArray(new Integer[0]));
            branchIdDropdown.setSelectedItem(Integer.parseInt(branchId)); // Set current branch ID

            // Create input fields
            JTextField nameField = new JTextField(name);
            JTextField emailField = new JTextField(email);
            JTextField addressField = new JTextField(address);
            JTextField phoneField = new JTextField(phone);
            JTextField salaryField = new JTextField(salary);
            JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Active", "Inactive"});
            statusDropdown.setSelectedItem(status);

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Branch ID:"));
            panel.add(branchIdDropdown);
            panel.add(new JLabel("Address:"));
            panel.add(addressField);
            panel.add(new JLabel("Phone:"));
            panel.add(phoneField);
            panel.add(new JLabel("Salary:"));
            panel.add(salaryField);
            panel.add(new JLabel("Status:"));
            panel.add(statusDropdown);

            int result = JOptionPane.showConfirmDialog(this, panel, "Update Branch Manager", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Create an Employee object with updated data
                    Employee updatedEmployee = new Employee();
                    updatedEmployee.setEmployeeId(Integer.parseInt(id));
                    updatedEmployee.setName(nameField.getText());
                    updatedEmployee.setEmail(emailField.getText());
                    updatedEmployee.setBranchId((Integer) branchIdDropdown.getSelectedItem());
                    updatedEmployee.setAddress(addressField.getText());
                    updatedEmployee.setPhone(phoneField.getText());
                    updatedEmployee.setSalary(new BigDecimal(salaryField.getText()));
                    updatedEmployee.setStatus(statusDropdown.getSelectedItem().toString());

                    // Call the controller to update the employee
                    boolean isUpdated = controller.updateBranchManager(updatedEmployee);

                    if (isUpdated) {
                        JOptionPane.showMessageDialog(this, "Branch manager updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        updateTableBasedOnStatus();

                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update branch manager.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        String[] columns = {"ID", "Name", "Email", "Branch ID", "Address", "Phone Number", "Salary", "Joining Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);

        // Set table style
        employeeTable.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        employeeTable.setRowHeight(25);
        employeeTable.setBackground(Color.WHITE);
        employeeTable.setGridColor(Color.LIGHT_GRAY);

        JTableHeader tableHeader = employeeTable.getTableHeader();
        tableHeader.setFont(new Font("Century Gothic", Font.BOLD, 16));
        tableHeader.setBackground(Color.decode("#e6e6e7"));
        tableHeader.setForeground(Color.BLACK);

        // ScrollPane for the table
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBounds(320, 125, 900, 380);  // Position table on the right side with specified bounds
        lblBackground.add(scrollPane);

        // Populate table with data
        controller = new EmployeeController(this);
        controller.populateTable(); // Populate initially with all branch managers

        setVisible(true);
    }

    // Method to refresh the table by reloading data from the database
    void refreshTable() {
        controller.populateTable();  // Reload all branch managers without any status filtering
    }



    // Method to update the table based on selected status
    private void updateTableBasedOnStatus() {
        String selectedStatus = (String) statusDropdown.getSelectedItem();
        if ("All Branch Managers".equals(selectedStatus)) {
            controller.populateTable(); // Populate all branch managers
        } else if ("Active".equals(selectedStatus) || "Inactive".equals(selectedStatus)) {
            controller.populateTableByStatus(selectedStatus.toLowerCase());
        }
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public static void main(String[] args) throws SQLException {
        new BranchManagerTableUI();
    }
}
