package View;

import Controller.EmployeeController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;



public class ProfitView extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeController controller;

    public ProfitView() throws SQLException {
        setTitle("METRO");
        setSize(1320, 710);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Background image
        JLabel lblBackground = new JLabel(new ImageIcon("SCDFinalProject\\src\\main\\resources\\images\\Super Admin Dashboard.png"));
        lblBackground.setBounds(0, 0, 1320, 710);
        add(lblBackground);

       /* String username = SuperAdminLoginController.getUsername();
        JLabel lblUsername = new JLabel(username);
        lblUsername.setBounds(105, 125, 220, 40);
        lblUsername.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        lblUsername.setForeground(Color.BLACK);
        lblBackground.add(lblUsername);
*/
        JLabel lblMenu = new JLabel("Menu");
        lblMenu.setFont(new Font("DM Sans", Font.PLAIN, 14));
        lblMenu.setForeground(Color.BLACK);
        lblMenu.setBounds(115, 215, 100, 20);
        lblBackground.add(lblMenu);

        JLabel lblEmpTitle = new JLabel("Profit Reports");
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
        btnBranchManagers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new BranchManagerTableUI();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();  // Optionally dispose the current window
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
                //new ChangePasswordUI(); // Replace with your Change Password UI class
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
        JPanel PnlProfit = new JPanel();
        PnlProfit.setBounds(273, 30, 1000, 700);
        PnlProfit.add(new ProfitPanel());

        lblBackground.add(PnlProfit);
        setVisible(true);
    }
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Century Gothic", Font.PLAIN, 16));
    }

    public static void main(String[] args) throws SQLException {
        new ProfitView();
    }

}
