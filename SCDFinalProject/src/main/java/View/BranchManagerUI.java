package View;

import Controller.EmployeeController;
import Model.Employee;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.List;

public class BranchManagerUI extends JFrame {

    private BufferedImage backgroundImage;
    private Employee employee;
    private JPanel employeePanel;  // Panel for adding employee buttons
    private static    int menuYPosition = 220; // Y position for the menu
    private static     int menuWidth = 157;
    private static JPanel backgroundPanel;
    private EmployeeController employeeController;
    // Constructor to initialize UI with the logged-in Employee
    public BranchManagerUI(Employee loggedInEmployee) {
        this.employee = loggedInEmployee; // Assign the received employee object
        this.employeeController=new EmployeeController();
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
         backgroundPanel = new JPanel() {

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
                        showEmployeePanel();
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
        JLabel branchIdLabel = new JLabel("" + employee.getBranchId());
        branchIdLabel.setBounds(118, 145, 200, 22); // Adjust bounds as needed
        branchIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        branchIdLabel.setForeground(Color.BLACK);

        // Add components to the background panel
        backgroundPanel.add(sideMenuPanel);
        backgroundPanel.add(branchIdLabel);

        // Add the background panel to the frame
        add(backgroundPanel);
    }

    // Method to show the employee panel on the right side
    private void showEmployeePanel() {
        // Remove any existing employee panel
        if (employeePanel != null) {
            remove(employeePanel);
        }

        // Create a new employee panel
        employeePanel = new JPanel();
        employeePanel.setLayout(null); // Use null layout for absolute positioning
        employeePanel.setBounds(menuWidth + 26, 0, getWidth() - menuWidth, getHeight()); // Set bounds of the panel

        // Fetch and display all employees based on the branchId
        int branchId =employee.getBranchId(); // Assuming 'employee' is the logged-in employee
        List<Employee> employeeList = employeeController.getEmployeesByBranch(branchId); // Fetch employees from the DB

        // Convert the list of employees to a 2D Object array for the JTable
        String[] columnNames = {"Employee ID", "Name", "Role", "Email", "Phone", "Salary"};
        Object[][] rowData = new Object[employeeList.size()][columnNames.length];
        for (int i = 0; i < employeeList.size(); i++) {
            Employee emp = employeeList.get(i);
            rowData[i][0] = emp.getEmployeeId();
            rowData[i][1] = emp.getUsername();
            rowData[i][2] = emp.getPosition();
            rowData[i][3] = emp.getEmail();
            rowData[i][4] = emp.getPhone();
            rowData[i][5] = emp.getSalary();
        }

        // Create a JTable to display the employees
        JTable employeeTable = new JTable(rowData, columnNames);
        employeeTable.setFillsViewportHeight(true);
        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        employeeTable.setRowHeight(25);

        // Style the JTable
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        employeeTable.getTableHeader().setBackground(new Color(70, 130, 180));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));

        // Wrap the JTable in a JScrollPane
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);
        tableScrollPane.setBounds(50, 50, getWidth() - menuWidth - 100, 400); // Set bounds for the scroll pane

        // Add the JScrollPane to the employee panel
        employeePanel.add(tableScrollPane);

        // Create and add the "Add Cashier" button (use RoundedButton)
        RoundedButton addCashierButton = new RoundedButton("Add Cashier", 20); // Set corner radius for rounded corners
        addCashierButton.setBounds(615, 600, 280, 50); // Position and size of the button
        addCashierButton.setBackground(new Color(0, 102, 204)); // Set button color (example blue)
        addCashierButton.setForeground(Color.WHITE); // Set text color (white)
        addCashierButton.addActionListener(e -> {
            System.out.println("Add Cashier clicked");
            // Implement Add Cashier functionality here
        });
        employeePanel.add(addCashierButton);

        // Create and add the "Add Data Operator" button (use RoundedButton)
        RoundedButton addDataOperatorButton = new RoundedButton("Add Data Operator", 20); // Set corner radius for rounded corners
        addDataOperatorButton.setBounds(320, 600, 280, 50); // Position and size of the button
        addDataOperatorButton.setBackground(new Color(73, 174, 66)); // Set button color (example green)
        addDataOperatorButton.setForeground(Color.WHITE); // Set text color (white)
        addDataOperatorButton.addActionListener(e -> {
            System.out.println("Add Data Operator clicked");
            // Implement Add Data Operator functionality here
        });
        employeePanel.add(addDataOperatorButton);

        // Add the employee panel to the main background panel
        backgroundPanel.add(employeePanel); // Ensure it's added to the backgroundPanel

        // Revalidate and repaint to update the UI
        revalidate();
        repaint();
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
        SwingUtilities.invokeLater(() -> new BranchManagerUI(new Employee("anas1","anas","anas@gmail.com","1",1,"lhr",new BigDecimal(22222),"12345",true,new Date(),"Branch Manager"))) ;
        }

    }

