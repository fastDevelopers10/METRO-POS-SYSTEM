package View;

import Controller.EmployeeController;
import Controller.ProductController;
import Controller.TransactionController;
import Model.Employee;
import Model.MyPrinter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;



public class BranchManagerUI extends JFrame {
   private BufferedImage reportsImage;
   private TransactionController transactionController;
    private BufferedImage backgroundImage;
    private static Employee employee;
    private JPanel employeePanel;  // Panel for adding employee buttons
    JPanel reportsPanel;
    private static int menuYPosition = 220; // Y position for the menu
    private static int menuWidth = 157;
    private static JPanel backgroundPanel;
    private EmployeeController employeeController;
    private ProductController productController=new ProductController();
    // Constructor to initialize UI with the logged-in Employee
    public BranchManagerUI(Employee loggedInEmployee) {
        this.employee = loggedInEmployee; // Assign the received employee object
        this.employeeController = new EmployeeController();
        this.transactionController=new TransactionController();

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
        try {
            showReportsPanel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
                {"Employees", "icons/Product.png"},
                {"Stocks Left","images/icons/Product.png"},
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
                        System.out.println("View Dash...");

                        if(employeePanel!=null)
                        {backgroundPanel.remove(employeePanel); }// Remove employee panel
                        try {
                            showReportsPanel();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;

                    case "Employees":

                    System.out.println("Viewing Employees...");
                    if(reportsPanel!=null)
                    {backgroundPanel.remove(reportsPanel); }// Remove employee panel
                    showEmployeePanel();
                    break;
                    case "Stocks Left":
                        System.out.println("Checking rem stock...");
                        viewStocksLeft();
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
    public void viewStocksLeft()
    {
        JFrame frame = new JFrame("Product Stock Table");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        // Create a table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Total Products");

        // Fetch the product data from DAO
        List<Object[]> productList = productController.getProductIdAndQuantities();

        // Add data to the table model
        for (Object[] productData : productList) {
            tableModel.addRow(productData);
        }

        // Create the JTable with the data model
        JTable table = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }
    // Method to show the employee panel on the right side
    public void showEmployeePanel() {
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
        RoundedButton addCashierButton = new RoundedButton("Add Employee", 20);
        addCashierButton.setBounds(employeePanel.getWidth()/3, 570, 280, 50);
        addCashierButton.setBackground(new Color(0, 102, 204));
        addCashierButton.setForeground(Color.WHITE);
        addCashierButton.setFont(new Font("Arial", Font.BOLD, 16));

        addCashierButton.addActionListener(e -> {
            // Add Cashier Logic
            String [] positions={"Cashier","Data Operator"};
            try {
                EmployeeForm form= new EmployeeForm(positions,employee.getBranchId());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });

        // Add Cashier button to the employee panel
        employeePanel.add(addCashierButton);

//        // Create and add the "Add Data Operator" button
//        RoundedButton addDataOpButton = new RoundedButton("Add Data Operator", 20);
//        addDataOpButton.setBounds(870, 570, 280, 50);
//        addDataOpButton.setBackground(new Color(0, 102, 204));
//        addDataOpButton.setForeground(Color.WHITE);
//        addDataOpButton.setFont(new Font("Arial", Font.BOLD, 16));

//        addDataOpButton.addActionListener(e -> {
//            // Add Data Operator Logic
//            Employee newDataOp = new Employee();
//            newDataOp.setPosition("Data Operator");
//            employeeController.insertEmployee(newDataOp); // Insert new Data Operator employee
//            JOptionPane.showMessageDialog(this, "Data Operator Added Successfully");
//        });

//        // Add Data Operator button to the employee panel
//        employeePanel.add(addDataOpButton);

        // Add the employee panel to the background panel
        backgroundPanel.add(employeePanel);

        // Refresh the frame to show updated employee panel
        revalidate();
        repaint();
    }
    private void showReportsPanel() throws SQLException {
        // Remove any existing reports panel if it exists
        if (reportsPanel != null) {
            backgroundPanel.remove(reportsPanel);
        }

        // Create a new reports panel
        reportsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (reportsImage != null) {
                    g.drawImage(reportsImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Fallback color if image is not found
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        reportsPanel.setLayout(null); // You can adjust the layout as needed
        reportsPanel.setBounds(menuWidth + 26, 0, getWidth() - menuWidth, getHeight()); // Set bounds of the panel

        // Load the image for the reports panel
        try {
            reportsImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("images/BM_Report.png")));  // Path to your report image
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load reports background image.");
        }

        // Get the total sales for the current year for the branch (example branchId: 1)
        double netProfit = 0;
        try {
            netProfit = transactionController.getNetProfitForCurrentYearBM(employee.getBranchId()); // Pass your actual branchId here
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a label to display the total sales
        String profitText = "Rs. " + netProfit;
        JLabel profitLabel = new JLabel(profitText);
        profitLabel.setFont(new Font("Arial", Font.BOLD, 20));
        profitLabel.setForeground(Color.BLACK); // Set text color

        profitLabel.setBounds((reportsPanel.getWidth() - profitLabel.getPreferredSize().width) / 2 +45,
                (reportsPanel.getHeight() ) / 2 +64,
                profitLabel.getPreferredSize().width + 10,
                profitLabel.getPreferredSize().height);

        // Add the label to the reports panel
        reportsPanel.add(profitLabel);
        // Get the total sales for the current year for the branch (example branchId: 1)
        int totalSales = 0;
        try {
            totalSales = transactionController.getNetProfitForCurrentYearBM(employee.getBranchId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a label to display the total sales
        String salesText = totalSales +" items sold";
        JLabel salesLabel = new JLabel(salesText);
        salesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        salesLabel.setForeground(Color.BLACK); // Set text color

        salesLabel.setBounds((reportsPanel.getWidth() - salesLabel.getPreferredSize().width) / 2 +70,
                (reportsPanel.getHeight() ) / 3 -28 ,
                salesLabel.getPreferredSize().width+20,
                salesLabel.getPreferredSize().height);

        // Add the label to the reports panel
        reportsPanel.add(salesLabel);

        int totalStock = 0;
        totalStock = productController.getStockByBranch(employee.getBranchId());

        // Create a label to display the total sales
        String stockText = totalStock +" stocked";
        JLabel stockLabel = new JLabel(stockText);
        stockLabel.setFont(new Font("Arial", Font.BOLD, 20));
        stockLabel.setForeground(Color.BLACK); // Set text color

        stockLabel.setBounds((reportsPanel.getWidth() - stockLabel.getPreferredSize().width) / 2 -4*100 +5,
                (reportsPanel.getHeight() ) / 3 -28 ,
                stockLabel.getPreferredSize().width+20,
                stockLabel.getPreferredSize().height);

        // Add the label to the reports panel
        reportsPanel.add(stockLabel);


//        RoundedButton remStock = new RoundedButton("Remaining Stocks",25);  // Set button text
//        remStock.setBounds(menuWidth + 14, // Positioned on the right with some padding
//                (reportsPanel.getHeight() ) / 2, // Vertically centered
//                270, 100); // Width and height of the button
//
//// Load and set the icon for the button (optional, if you still want to include the icon)
//        try {
//            ImageIcon buttonIcon = new ImageIcon(Objects.requireNonNull(
//                    getClass().getClassLoader().getResource("images/icons/graph.png") // Path to your button icon
//            ));
//            // Scale the icon to fit the button size
//            Image scaledImage = buttonIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
//            remStock.setIcon(new ImageIcon(scaledImage));
//
//            remStock.setContentAreaFilled(false); // Remove button background to focus on the icon
//            remStock.setBorderPainted(false); // Remove button border
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Error: Unable to load button icon.");
//        }

//// Set the font and color for the text
//        remStock.setFont(new Font("Arial", Font.BOLD, 16)); // Customize font size and style
//        remStock.setForeground(Color.BLACK); // Set text color to black
//
//// Align the text and icon
//        remStock.setHorizontalTextPosition(SwingConstants.RIGHT);  // Align text to the right of the icon
//        remStock.setVerticalTextPosition(SwingConstants.CENTER); // Vertically center the text with the icon

//// Add an action listener to the button
//        remStock.addActionListener(e -> {
//            JFrame frame = new JFrame("Product Stock Table");
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            frame.setSize(600, 400);
//
//            // Create a table model
//            DefaultTableModel tableModel = new DefaultTableModel();
//            tableModel.addColumn("Product ID");
//            tableModel.addColumn("Total Products");
//
//            // Fetch the product data from DAO
//            List<Object[]> productList = productController.getProductIdAndQuantities();
//
//            // Add data to the table model
//            for (Object[] productData : productList) {
//                tableModel.addRow(productData);
//            }
//
//            // Create the JTable with the data model
//            JTable table = new JTable(tableModel);
//
//            // Add the table to a scroll pane
//            JScrollPane scrollPane = new JScrollPane(table);
//            frame.add(scrollPane, BorderLayout.CENTER);
//
//            // Make the frame visible
//            frame.setVisible(true);
//        });
//
//// Add the button to the reports panel
//        reportsPanel.add(remStock);
// "Profits History" Button
        RoundedButton profitsHistoryButton = new RoundedButton("Profits History", 20);
        profitsHistoryButton.setBounds(menuWidth + 205, reportsPanel.getHeight() - 120, 140, 40);
        profitsHistoryButton.setFont(new Font("Arial", Font.BOLD, 14));
        profitsHistoryButton.setBackground(Color.LIGHT_GRAY);
        profitsHistoryButton.setForeground(Color.BLACK);

        profitsHistoryButton.addActionListener(e -> {
            // Create dropdown menu for time period selection
            String[] options = {"Monthly", "Weekly", "Yearly"};
            JComboBox<String> timePeriodComboBox = new JComboBox<>(options);

            // Display the dropdown menu for time period selection
            int timePeriodSelection = JOptionPane.showOptionDialog(
                    null,
                    timePeriodComboBox,
                    "Select Time Period",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null
            );

            if (timePeriodSelection != JOptionPane.CLOSED_OPTION) {
                String selectedPeriod = (String) timePeriodComboBox.getSelectedItem();
                String selectedYear = null;

                // Ask for year based on period selection
                if (selectedPeriod != null) {
                    // Year selection
                    String[] years = {"2020", "2021", "2022", "2023", "2024"};
                    JComboBox<String> yearComboBox = new JComboBox<>(years);
                    int yearSelection = JOptionPane.showOptionDialog(
                            null,
                            yearComboBox,
                            "Select Year",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null
                    );
                    if (yearSelection != JOptionPane.CLOSED_OPTION) {
                        selectedYear = (String) yearComboBox.getSelectedItem();
                    }

                    int year = Integer.parseInt(selectedYear);

                    try {
                        // Fetch profit data based on the selected year and period
                        Map<Integer, Double> productProfits = transactionController.getAverageProfits(employee.getBranchId(), selectedPeriod.toLowerCase(), year);

                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                        // Add a label displaying the results
                        StringBuilder profitMessage = new StringBuilder("<html><h3>Average Profits for Products:</h3>");
                        for (Map.Entry<Integer, Double> entry : productProfits.entrySet()) {
                            profitMessage.append(String.format("<p>Product ID: %d, Average Profit: %.2f</p>", entry.getKey(), entry.getValue()));
                        }
                        profitMessage.append("</html>");

                        JLabel profitLabel2 = new JLabel(profitMessage.toString());
                        panel.add(profitLabel2);

                        // Print button functionality for the profit data
                        RoundedButton printButton = new RoundedButton("Print", 20);
                        printButton.addActionListener(printEvent -> {
                            try {
                                MyPrinter printer = new MyPrinter();
                                printer.setData(profitMessage.toString()); // Update method for profit data

                                PrinterJob job = PrinterJob.getPrinterJob();
                                job.setPrintable(printer);

                                if (job.printDialog()) {
                                    job.print();
                                }

                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Error printing the data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                        panel.add(printButton);

                        // Display the panel with profit data
                        JOptionPane.showMessageDialog(null, panel, "Product-wise Average Profits", JOptionPane.INFORMATION_MESSAGE);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error fetching average profits: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

// "Sales History" Button
        RoundedButton salesHistoryButton = new RoundedButton("Sales History", 20);
        salesHistoryButton.setBounds(menuWidth + 355, reportsPanel.getHeight() - 120, 140, 40);
        salesHistoryButton.setFont(new Font("Arial", Font.BOLD, 14));
        salesHistoryButton.setBackground(Color.LIGHT_GRAY);
        salesHistoryButton.setForeground(Color.BLACK);

        salesHistoryButton.addActionListener(e -> {
            // Create dropdown menu for time period selection
            String[] options = {"Monthly", "Weekly", "Yearly"};
            JComboBox<String> timePeriodComboBox = new JComboBox<>(options);

            // Display the dropdown menu for time period selection
            int timePeriodSelection = JOptionPane.showOptionDialog(
                    null,
                    timePeriodComboBox,
                    "Select Time Period",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null
            );

            if (timePeriodSelection != JOptionPane.CLOSED_OPTION) {
                String selectedPeriod = (String) timePeriodComboBox.getSelectedItem();
                String selectedYear = null;

                // Ask for year based on period selection
                if (selectedPeriod != null) {
                    // Year selection
                    String[] years = {"2020", "2021", "2022", "2023", "2024"};
                    JComboBox<String> yearComboBox = new JComboBox<>(years);
                    int yearSelection = JOptionPane.showOptionDialog(
                            null,
                            yearComboBox,
                            "Select Year",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null
                    );
                    if (yearSelection != JOptionPane.CLOSED_OPTION) {
                        selectedYear = (String) yearComboBox.getSelectedItem();
                    }

                    int year = Integer.parseInt(selectedYear);

                    try {
                        Map<Integer, Double> productSales = transactionController.getAverageSales(employee.getBranchId(), selectedPeriod.toLowerCase(), year);

                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                        // Add a label displaying the results
                        StringBuilder salesMessage = new StringBuilder("<html><h3>Average Sales for Products:</h3>");
                        for (Map.Entry<Integer, Double> entry : productSales.entrySet()) {
                            salesMessage.append(String.format("<p>Product ID: %d, Average Sales: %.2f</p>", entry.getKey(), entry.getValue()));
                        }
                        salesMessage.append("</html>");

                        JLabel salesLabel2 = new JLabel(salesMessage.toString());
                        panel.add(salesLabel2);

                        RoundedButton printButton = new RoundedButton("Print",20);
                        printButton.addActionListener(printEvent -> {
                            try {
                                MyPrinter printer = new MyPrinter();
                                printer.setData(salesMessage.toString());

                                PrinterJob job = PrinterJob.getPrinterJob();
                                job.setPrintable(printer);

                                if (job.printDialog()) {
                                    job.print();
                                }

                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Error printing the data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                        panel.add(printButton);

                        JOptionPane.showMessageDialog(null, panel, "Product-wise Average Sales", JOptionPane.INFORMATION_MESSAGE);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error fetching average sales: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Add both buttons to the reports panel
        reportsPanel.add(profitsHistoryButton);
        reportsPanel.add(salesHistoryButton);

        // Add the reports panel to the background panel
        backgroundPanel.add(reportsPanel);

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
