package SCDFinalProject.src.main.java.View;

import SCDFinalProject.src.main.java.Controller.SuperAdminLoginController;
import SCDFinalProject.src.main.java.Model.SuperAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SuperAdminUI extends JFrame {
    private SuperAdmin admin;
    public SuperAdminUI() {
        setTitle("Super Admin UI");
        setSize(1320, 710);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Absolute layout for precise positioning
        setResizable(false);
        admin=new SuperAdmin();
        // Background image
        JLabel lblBackground = new JLabel(new ImageIcon("SCDFinalProject\\src\\main\\resources\\images\\Super Admin Dashboard (1).png"));
        lblBackground.setBounds(0, 0, 1320, 710);
        add(lblBackground);

        // Get username from SuperAdminController
        String username = SuperAdminLoginController.getUsername();
        JLabel lblUsername = new JLabel(username);
        lblUsername.setBounds(105, 125, 180, 40);
        lblUsername.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        lblUsername.setForeground(Color.BLACK);
        lblBackground.add(lblUsername);

        // Labels
        JLabel lblMenu = new JLabel("Menu");
        lblMenu.setFont(new Font("DM Sans", Font.PLAIN, 14));
        lblMenu.setForeground(Color.BLACK);
        lblMenu.setBounds(115, 215, 100, 20);
        lblBackground.add(lblMenu);

        JLabel lblDashboardTitle = new JLabel("Dashboard");
        lblDashboardTitle.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblDashboardTitle.setForeground(Color.BLACK);
        lblDashboardTitle.setBounds(300, 30, 200, 40);
        lblBackground.add(lblDashboardTitle);

        JLabel lblTotalProducts = new JLabel("Total Products");
        lblTotalProducts.setFont(new Font("Century Gothic", Font.BOLD, 15));
        lblTotalProducts.setForeground(Color.BLACK);
        lblTotalProducts.setBounds(310, 140, 200, 20);
        lblBackground.add(lblTotalProducts);

        JLabel lblWeeklySales = new JLabel("Weekly Sales");
        lblWeeklySales.setFont(new Font("Century Gothic", Font.BOLD, 15));
        lblWeeklySales.setForeground(Color.BLACK);
        lblWeeklySales.setBounds(555, 140, 200, 20);
        lblBackground.add(lblWeeklySales);

        JLabel lblRemainingProducts = new JLabel("Remaining Products");
        lblRemainingProducts.setFont(new Font("Century Gothic", Font.BOLD, 15));
        lblRemainingProducts.setForeground(Color.BLACK);
        lblRemainingProducts.setBounds(310, 300, 200, 20);
        lblBackground.add(lblRemainingProducts);

        JLabel lblProfit = new JLabel("Profit");
        lblProfit.setFont(new Font("Century Gothic", Font.BOLD, 15));
        lblProfit.setForeground(Color.BLACK);
        lblProfit.setBounds(555, 300, 200, 20);
        lblBackground.add(lblProfit);

        // Buttons with independent positions
        JButton btnDashboard = new JButton("Dashboard");
        btnDashboard.setBounds(50, 270, 140, 30); // Position of the Dashboard button
        styleButton(btnDashboard);
        lblBackground.add(btnDashboard);

        JButton btnbranchManagers = new JButton("Branch Managers");
        btnbranchManagers.setBounds(50, 320, 140, 30); // Position of the branchManagers button
        styleButton(btnbranchManagers);
        lblBackground.add(btnbranchManagers);

        JButton btnBranches = new JButton("Branches");
        btnBranches.setBounds(50, 370, 120, 30); // Position of the Branches button
        styleButton(btnBranches);
        lblBackground.add(btnBranches);

        JButton btnChangePassword = new JButton("Change Password");
        btnChangePassword.setBounds(45, 420, 200, 30); // Position of the Change Password button
        styleButton(btnChangePassword);
        lblBackground.add(btnChangePassword);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(50, 470, 100, 30); // Position of the Logout button
        styleButton(btnLogout);
        lblBackground.add(btnLogout);

        // ActionListener for Dashboard button
        btnDashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SuperAdminUI();
                dispose();  // Close current window
            }
        });

        // ActionListener for branchManagers button
        btnbranchManagers.addActionListener(new ActionListener() {
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

        // ActionListener for Logout button
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame();  // Exit the application
            }
        });

        setVisible(true); // Make the window visible
    }

    // Method to style the buttons
    private void styleButton(JButton button) {
        button.setFocusPainted(false); // Removes the focus border
        button.setContentAreaFilled(false); // Makes the button look like text
        button.setBorderPainted(false); // Removes the border
        button.setForeground(Color.BLACK); // Text color
        button.setFont(new Font("Century Gothic", Font.PLAIN, 16)); // Font style and size
    }

    public static void main(String[] args) {
        new SuperAdminUI(); // Create and show the Super Admin UI window
    }
}
