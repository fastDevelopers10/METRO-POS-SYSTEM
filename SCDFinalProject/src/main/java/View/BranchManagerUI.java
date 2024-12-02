package View;

import DAO.ProductDAO;
import Model.Employee;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class BranchManagerUI extends JFrame {

    private BufferedImage backgroundImage;
    private Employee employee;

    // Constructor to initialize UI with the logged-in Employee
    public BranchManagerUI(Employee loggedInEmployee) {
        this.employee = loggedInEmployee; // Assign the received employee object
        setTitle("Branch Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Get screen size and set JFrame to full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the window
        setResizable(false);

        try {
            // Load the background image
            backgroundImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("images/Cashier.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load background image.");
        }

        // Initialize and set up components
        setupComponents();
    }

    // Method to set up UI components
    private void setupComponents() {
        // Background Panel
        JPanel backgroundPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Fallback color if image is not found
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());

        // Side Menu
        JPanel sideMenuPanel = new JPanel();
        sideMenuPanel.setLayout(null); // Use null layout for manual positioning
        sideMenuPanel.setBackground(Color.WHITE);

        // Adjust the bounds of the panel
        int menuYPosition = 220; // Y position for the menu
        int menuWidth = 157;
        sideMenuPanel.setBounds(14, menuYPosition, menuWidth, getHeight() - menuYPosition);
        sideMenuPanel.setOpaque(false);

        // Button text and optional icon paths
        String[][] menuItems = {
                {"Dashboard", "images/icons/dash_icon.png"},
                {"Employees", "icons/Products.png"},
                {"Reports", "icons/Product.png"},
                {"Logout", "images/icons/Product.png"}
        };

        final SideMenuButton[] activeButton = {null}; // Track the currently active button

        // Initial Y position for the first button
        int buttonYPosition = 10;

        // Create and add menu buttons
        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i][0];
            String iconPath = menuItems[i][1];

            SideMenuButton button = new SideMenuButton(text, iconPath);

            // Set button bounds manually
            int buttonHeight = 50; // Adjust the height of each button as needed
            button.setBounds(0, buttonYPosition, menuWidth, buttonHeight);

            // Add action listener for button actions
            button.addActionListener(e -> {
                // Set the active button's background
                if (activeButton[0] != null) {
                    activeButton[0].setActive(false); // Reset previous button
                }
                button.setActive(true);
                activeButton[0] = button;
                System.out.println("Button clicked: " + button.getText());

                // Perform specific actions based on the button clicked
                switch (button.getText()) {
                    case "Dashboard":
                        System.out.println("Starting Sale...");
                        // startSaleAction();
                        break;

                    case "Employees":
                        System.out.println("Viewing Employees...");
                        // viewBillsAction();
                        break;

                    case "Reports":
                        System.out.println("View Reports...");

                        break;

                    case "Logout":
                        System.out.println("Logging out...");
                        logoutAction();
                        break;

                    default:
                        System.out.println("Unknown action");
                        break;
                }
            });

            // Set the first button as active by default
            if (i == 0) {
                button.setActive(true);
                activeButton[0] = button;
            }

            // Add button to the panel
            sideMenuPanel.add(button);

            // Update Y position for next button
            buttonYPosition += buttonHeight; // Increase Y position by the height of the button
        }

        // Display Branch ID
        JLabel branchIdLabel = new JLabel("" + employee.getBranchCode());
        branchIdLabel.setBounds(118, 145, 200, 22); // Adjust bounds as needed
        branchIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        branchIdLabel.setForeground(Color.BLACK);

        // Add components to the background panel
        backgroundPanel.add(sideMenuPanel);
        backgroundPanel.add(branchIdLabel);

        // Add the background panel to the frame
        add(backgroundPanel);
    }

    // Logout action method
    private void logoutAction() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose(); // Close the current window
            new LoginOptions(); // Navigate to login options
        }
    }


    // Main method for testing (optional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Employee dummyEmployee = new Employee(
                    "E001",                     // employeeId
                    "john_doe",                 // username
                    "john.doe@example.com",     // email
                    "password123",              // password
                    1,                        // branchCode
                    "123 Main St, City, Country", // address
                    new BigDecimal("5000.00"),  // salary
                    "+1234567890",              // phone
                    "active",                   // status
                    new Date(),                 // joiningDate
                    "Cashier"                   // employeeType
            );            new BranchManagerUI(dummyEmployee).setVisible(true);
        });
    }
}
