package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

import Controller.EmpCtrlr;
import Controller.SuperAdminLoginController;

public class BranchManagerTableUI extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmpCtrlr controller;

    public BranchManagerTableUI() throws SQLException {
        setTitle("METRO");
        setSize(1320, 710);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Background image
        JLabel lblBackground = new JLabel(new ImageIcon("SCDFinalProject\\src\\main\\resources\\images\\Super Admin Dashboard.png"));
        lblBackground.setBounds(0, 0, 1320, 710);
        add(lblBackground);

        String username = SuperAdminLoginController.getUsername();
        JLabel lblUsername = new JLabel(username);
        lblUsername.setBounds(105, 125, 220, 40);
        lblUsername.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        lblUsername.setForeground(Color.BLACK);
        lblBackground.add(lblUsername);

        JLabel lblMenu = new JLabel("Menu");
        lblMenu.setFont(new Font("DM Sans", Font.PLAIN, 14));
        lblMenu.setForeground(Color.BLACK);
        lblMenu.setBounds(115, 215, 100, 20);
        lblBackground.add(lblMenu);

        JLabel lblEmpTitle = new JLabel("Super Admin");
        lblEmpTitle.setFont(new Font("Century Gothic", Font.BOLD, 25));
        lblEmpTitle.setForeground(Color.BLACK);
        lblEmpTitle.setBounds(300, 10, 200, 40);
        lblBackground.add(lblEmpTitle);

        // Dashboard button
        JButton btnDashboard = new JButton("Dashboard");
        btnDashboard.setBounds(50, 270, 140, 30);
        styleButton(btnDashboard);
        lblBackground.add(btnDashboard);
        btnDashboard.addActionListener(e -> {
            dispose();
            try {
                new SuperAdminUI(); // Replace with your Dashboard UI class
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Branch Managers button
        JButton btnBranchManagers = new JButton("Branch Managers");
        btnBranchManagers.setBounds(50, 320, 180, 30);
        styleButton(btnBranchManagers);
        lblBackground.add(btnBranchManagers);
        btnBranchManagers.addActionListener(e -> {
            dispose();
            try {
                BranchManagerTableUI branchManagerUI = new BranchManagerTableUI();
                branchManagerUI.controller.populateTable(); // Explicitly call to ensure population
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Branches button
        JButton btnBranches = new JButton("Branches");
        btnBranches.setBounds(50, 370, 120, 30);
        styleButton(btnBranches);
        lblBackground.add(btnBranches);
        btnBranches.addActionListener(e -> {
            dispose();
            try {
                //new BranchesUI(); // Replace with your Branches UI class
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Change Password button
        JButton btnChangePassword = new JButton("Change Password");
        btnChangePassword.setBounds(45, 420, 200, 30);
        styleButton(btnChangePassword);
        lblBackground.add(btnChangePassword);
        btnChangePassword.addActionListener(e -> {
            dispose();
            try {
                new UpdatePasswordUI(); // Replace with your Change Password UI class
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Logout button
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(50, 470, 100, 30);
        styleButton(btnLogout);
        lblBackground.add(btnLogout);
        btnLogout.addActionListener(e -> {
            dispose();
            try {
                new LoginOptions(); // Replace with your Login UI class
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add Employee button
        JButton addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.setBounds(650, 580, 200, 50);
        addEmployeeButton.setForeground(Color.BLACK);
        addEmployeeButton.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        addEmployeeButton.setBackground(new Color(0xf9f9f9));
        lblBackground.add(addEmployeeButton);
        addEmployeeButton.addActionListener(e -> {
            try {
               // new AddEmployeeUI(); // Replace with your Add Employee UI class
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        controller = new EmpCtrlr(this);

        String[] columns = {"ID", "Name", "Position", "Email", "Branch ID", "Address",
                "Phone Number", "Salary", "Joining Date", "Username", "Status", "Update"};
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBounds(273, 60, 1000, 510);
        lblBackground.add(scrollPane);
        tableModel.setRowCount(0); // Clears the table before repopulating

        controller.populateTable();
        setVisible(true);
    }

    public JTable getEmployeeTable() {
        return employeeTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Century Gothic", Font.PLAIN, 16));
    }

    public static void main(String[] args) throws SQLException {
        new BranchManagerTableUI();
    }
}
