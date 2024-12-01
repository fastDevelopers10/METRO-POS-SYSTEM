package View;

import Controller.LoginController;
import DAO.EmployeeDAO;
import Model.Employee;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class Credentials extends JFrame {

    private Image backgroundImage;
    private String role; // Role passed to the class
    JTextField txtUsername;
    JPasswordField txtPassword;

    // Constructor to initialize the Login frame with a role
    public Credentials(String role) {
        this.role = role;

        setTitle(role + " Login"); // Set the title based on the role
        setSize(1350, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load the background image
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("images/login_screen.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load background image.");
        }

        // Custom JPanel for the background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        backgroundPanel.setLayout(null);
        add(backgroundPanel);

        // Username field
         txtUsername = new JTextField();
        styleTextField(txtUsername);

        // Password field
         txtPassword = new JPasswordField();
        styleTextField(txtPassword);

        // Login button
        JButton btnLogin = createButton("Login", Color.WHITE);

        // Exit button
        JButton btnExit = createButton("Exit", Color.BLACK);

        // Set component bounds
        txtUsername.setBounds(453, 257, 437, 35);
        txtPassword.setBounds(462, 344, 437, 35);
        btnLogin.setBounds(456, 415, 250, 40);
        btnExit.setBounds(572, 415, 250, 40);

        // Add components to the panel
        backgroundPanel.add(txtUsername);
        backgroundPanel.add(txtPassword);
        backgroundPanel.add(btnLogin);
        backgroundPanel.add(btnExit);

        // Login button action
        // Inside the action listener for login button
        LoginController loginController = new LoginController();

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (loginController.validateLogin(role, username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                openDashboard(role); // Open appropriate dashboard based on role
                this.dispose(); // Close the login frame after successful login
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Exit button action
        btnExit.addActionListener(e -> System.exit(0));
    }

    // Style JTextField or JPasswordField
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setForeground(Color.BLACK);
        textField.setBackground(new Color(245, 245, 245));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        textField.setCaretColor(Color.BLUE);
    }

    // Create styled buttons
    private JButton createButton(String text, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    // Open the dashboard based on the role
    private void openDashboard(String role) {
        // After login validation, get the Employee object
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee loggedInEmployee = employeeDAO.findEmployeeByUsernameAndRole(txtUsername.getText(), role);

        if (loggedInEmployee == null) {
            JOptionPane.showMessageDialog(this, "Error retrieving employee details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Open the UI based on the role and pass the Employee object
        switch (role.toLowerCase()) {
            case "cashier":
                new CashierUI(loggedInEmployee).setVisible(true);; // Pass the Employee object to CashierUI
                break;
            case "branch manager":
                // You can pass the Employee object here when implementing BranchManagerUI
                // new BranchManagerUI(loggedInEmployee);
                break;
            case "data operator":
                // new DataOperatorUI(loggedInEmployee);
                break;
            case "super admin":
                // new SuperAdminUI(loggedInEmployee);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid role!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        this.dispose(); // Close the login frame after successful login
    }
    // Main method to demonstrate the role-based login
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Example usage
        });
    }
}
