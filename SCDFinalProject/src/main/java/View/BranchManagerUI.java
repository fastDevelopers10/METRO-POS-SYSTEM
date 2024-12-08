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
    private static Employee employee;
    private JPanel employeePanel;  // Panel for adding employee buttons
    private static int menuYPosition = 220; // Y position for the menu
    private static int menuWidth = 157;
    private static JPanel backgroundPanel;
    private EmployeeController employeeController;
    // Constructor to initialize UI with the logged-in Employee
    public BranchManagerUI(Employee loggedInEmployee) {
        this.employee = loggedInEmployee; // Assign the received employee object
        this.employeeController = new EmployeeController();
        setTitle("Branch Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Get screen size and set JFrame to this
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the window if undecorated
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
        int branchId = employee.getBranchId(); // Assuming 'employee' is the logged-in employee
        List<Employee> employeeList = employeeController.getEmployeesByBranch(branchId); // Fetch employees from the DB
        System.out.println("Number of employees: " + employeeList.size());

        // Convert the list of employees to a 2D Object array for the JTable
        String[] columnNames = {"employee_id", "name", "position", "email", "branch_id", "address", "phone_number", "salary", "joining_date", "username", "password", "status", "first_time_joined"};
        Object[][] rowData = new Object[employeeList.size()][columnNames.length];
        for (int i = 0; i < employeeList.size(); i++) {
            Employee emp = employeeList.get(i);
            rowData[i][0] = emp.getEmployeeId();
            rowData[i][1] = emp.getName();
            rowData[i][2] = emp.getPosition();
            rowData[i][3] = emp.getEmail();
            rowData[i][4] = emp.getBranchId();
            rowData[i][5] = emp.getAddress();
            rowData[i][6] = emp.getPhone();
            rowData[i][7] = emp.getSalary();
            rowData[i][8] = emp.getJoiningDate();
            rowData[i][9] = emp.getUsername();
            rowData[i][10] = emp.getPassword();
            rowData[i][11] = emp.getStatus();
            rowData[i][12] = emp.getJoiningDate();
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
        tableScrollPane.setBounds(0, 100, getWidth() - menuWidth - 40, 420); // Set bounds for the scroll pane

        // Add the JScrollPane to the employee panel
        employeePanel.add(tableScrollPane);

        // Create and set the label for the employee section
        JLabel employeeLabel = new JLabel("Your Employees", JLabel.CENTER);
        employeeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        employeeLabel.setForeground(new Color(70, 130, 180));

        // Set bounds for the label
        employeeLabel.setBounds(0, 25, getWidth() - menuWidth - 30, 100);

        // Add the label to the employee panel
        employeePanel.add(employeeLabel);

        // Create and add the "Add Cashier" button
        RoundedButton addCashierButton = new RoundedButton("Add Cashier", 20);
        addCashierButton.setBounds(570, 570, 280, 50);
        addCashierButton.setBackground(new Color(0, 102, 204));
        addCashierButton.setForeground(Color.WHITE);
        addCashierButton.setFont(new Font("Arial", Font.BOLD, 16));

        addCashierButton.addActionListener(e -> {
            // Add Cashier Logic
            String [] positions={"Cashier","Data Operator"};
            EmployeeForm form= new EmployeeForm(positions,employee.getBranchId());
            Employee newEmp = new Employee();
            JOptionPane.showMessageDialog(this, "Cashier Added Successfully");
        });

        // Add Cashier button to the employee panel
        employeePanel.add(addCashierButton);

        // Create and add the "Add Data Operator" button
        RoundedButton addDataOpButton = new RoundedButton("Add Data Operator", 20);
        addDataOpButton.setBounds(870, 570, 280, 50);
        addDataOpButton.setBackground(new Color(0, 102, 204));
        addDataOpButton.setForeground(Color.WHITE);
        addDataOpButton.setFont(new Font("Arial", Font.BOLD, 16));

//        addDataOpButton.addActionListener(e -> {
//            // Add Data Operator Logic
//            Employee newDataOp = new Employee();
//            newDataOp.setPosition("Data Operator");
//            employeeController.insertEmployee(newDataOp); // Insert new Data Operator employee
//            JOptionPane.showMessageDialog(this, "Data Operator Added Successfully");
//        });

        // Add Data Operator button to the employee panel
        employeePanel.add(addDataOpButton);

        // Add the employee panel to the background panel
        backgroundPanel.add(employeePanel);

        // Refresh the frame to show updated employee panel
        revalidate();
        repaint();
    }

    // Method for handling logout action
    private void logoutAction() {
        // Close current frame and show the login screen again
        dispose();
        new LoginOptions().setVisible(true);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        // Simulating the logged-in employee with branchId


        // Launch Branch Manager UI
        SwingUtilities.invokeLater(() -> {
            new BranchManagerUI(employee).setVisible(true);
        });
    }
}
