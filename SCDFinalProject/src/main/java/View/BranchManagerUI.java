package View;

import Controller.EmployeeController;
import Controller.ProductController;
import Controller.TransactionController;
import DAO.ProductDAO;
import Model.Employee;
import Model.MyPrinter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;


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
                {"Profit History", "images/icons/Product.png"},
                {"Sales History", "images/icons/Product.png"},
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

                    case "Profit History":
                        showProfitsHistoryDialog();
                        break;
                    case "Sales History":
                        showSalesHistory();
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

        JLabel employeeName = new JLabel("" + employee.getUsername());
        employeeName.setBounds(82, 86, 200, 22); // Adjust bounds as needed
        employeeName.setFont(new Font("Arial", Font.BOLD, 16));
        employeeName.setForeground(Color.BLACK);

        // Display Branch ID
        JLabel branchIdLabel = new JLabel("" + employee.getBranchId());
        branchIdLabel.setBounds(118, 144, 200, 22); // Adjust bounds as needed
        branchIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        branchIdLabel.setForeground(Color.BLACK);

        // Add components to the background panel
        backgroundPanel.add(sideMenuPanel);
        backgroundPanel.add(branchIdLabel);
        backgroundPanel.add(employeeName);


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
    private RoundedPanel createChartWithZoomButton() {
        // Create a panel for the chart
        RoundedPanel panel = new RoundedPanel(3);
        panel.setBackground(Color.white);
        panel.setLayout(new BorderLayout());  // BorderLayout will manage the position of components
        panel.setPreferredSize(new Dimension(500, 600));  // Set the size of the panel

        // Create the chart
        JFreeChart chart = createProfitChart();

        // Add the chart to the panel in the center of the BorderLayout
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel, BorderLayout.CENTER);

        // Create the zoom button
        RoundedButton zoomButton = new RoundedButton("Zoom In", 20);
        zoomButton.addActionListener(e -> openZoomedChartFrame(chart));

        // Set a fixed size for the button and center it within the panel
        zoomButton.setPreferredSize(new Dimension(120, 40));  // You can adjust the size if needed

        // Create a panel for the button and center it horizontally
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3));  // Center with padding
        buttonPanel.setOpaque(false);  // Make the button panel transparent
        buttonPanel.add(zoomButton);   // Add the zoom button to the button panel

        // Add the button panel to the bottom (SOUTH) of the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


    private void openZoomedChartFrame(JFreeChart chart) {
        // Create a semi-transparent black frame
        JFrame zoomFrame = new JFrame();
        zoomFrame.setUndecorated(true);
        zoomFrame.setSize(1920, 1080);
        zoomFrame.setLayout(null); // Use null layout for precise placement
        zoomFrame.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
        zoomFrame.setLocationRelativeTo(null);

        // Create a rounded panel to hold the chart
        RoundedPanel chartPanel = new RoundedPanel(18); // Rounded corners with radius 4
        chartPanel.setSize(1000, 550); // Fixed size for the chart panel
        chartPanel.setLocation((zoomFrame.getX() + 120),
                (zoomFrame.getHeight() - chartPanel.getHeight()) / 3 - 120); // Centering
        chartPanel.setOpaque(true);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setLayout(new BorderLayout());

        // Add the chart to the rounded panel
        ChartPanel largerChartPanel = new ChartPanel(chart);
        largerChartPanel.setPreferredSize(new Dimension(800, 400)); // Ensure chart fits panel
        chartPanel.add(largerChartPanel, BorderLayout.CENTER);

        // Create a button panel with "Print" and "Close" buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false); // Transparent background
        buttonPanel.setBounds(
                ( chartPanel.getX()) +5, // Align with chart horizontally
                chartPanel.getY() + chartPanel.getHeight() + 10,    // Position below the chart
                chartPanel.getWidth(),
                50
        );

        // Add a print button
        RoundedButton printButton = new RoundedButton("Print", 20);
        printButton.addActionListener(printEvent -> {
            try {
                // Create a string representation of the chart data
                String chartData = "Chart Data: \n"; // Replace this with actual chart data extraction logic
                chartData += "This is a sample representation of the chart's content.";

                // Use MyPrinter to print the chart data
                MyPrinter printer = new MyPrinter();
                printer.setData(chartData);

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(printer);

                if (job.printDialog()) {
                    job.print();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(zoomFrame, "Error printing chart: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(printButton);

        // Add a close button
        RoundedButton closeButton = new RoundedButton("Close", 20);
        closeButton.addActionListener(closeEvent -> zoomFrame.dispose());
        buttonPanel.add(closeButton);

        // Add components to the frame
        zoomFrame.add(chartPanel);
        zoomFrame.add(buttonPanel);

        // Display the frame
        zoomFrame.setVisible(true);
    }


    // Method to create the main panel with pie chart and zoom button
   private RoundedPanel createPieChartWithZoomButton() {
        // Create a panel for the pie chart
        RoundedPanel panel = new RoundedPanel(23);
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 600));

        // Create the pie chart (pass branchId to the createPieChart method)
        JFreeChart pieChart = createPieChart(1);  // Assuming branchId = 1 for testing

        // Add the pie chart to the panel
        ChartPanel chartPanel = new ChartPanel(pieChart);
        panel.add(chartPanel, BorderLayout.CENTER);

        // Create a wrapper panel for the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3)); // Center alignment with padding
        buttonPanel.setOpaque(false); // Ensure the wrapper panel is transparent
        // Create the zoom button
       Map<String, Integer> categorySales = productController.getCategorySales(employee.getBranchId());  // Simulated method

       RoundedButton zoomButton = new RoundedButton("Zoom In", 20);
        zoomButton.addActionListener(e -> openZoomedPieChartFrame(pieChart,categorySales));

        // Optional: Set a fixed size for the button to ensure consistent appearance
        zoomButton.setPreferredSize(new Dimension(120, 40)); // Adjust size if necessary

        buttonPanel.add(zoomButton);

        // Add the wrapper panel to the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }




    // Method to open a zoomed pie chart in a new frame
    private void openZoomedPieChartFrame(JFreeChart pieChart, Map<String, Integer> categorySalesData) {
        // Create a new frame for the zoomed pie chart
        JFrame zoomFrame = new JFrame("Zoomed Pie Chart");
        zoomFrame.setUndecorated(true);
        zoomFrame.setSize(1920, 1080);
        zoomFrame.setLayout(null);  // Use null layout for precise placement
        zoomFrame.setBackground(new Color(0, 0, 0, 150));  // Semi-transparent black background
        zoomFrame.setLocationRelativeTo(null);  // Center on screen

        // Create a rounded panel to hold the pie chart
        RoundedPanel chartPanel = new RoundedPanel(18);  // Rounded corners with radius 18
        chartPanel.setSize(1000, 550);  // Set the size of the panel
        chartPanel.setLocation(
                (zoomFrame.getX() + 120),
                (zoomFrame.getHeight() - chartPanel.getHeight()) / 3 - 120  // Position vertically with some padding
        );
        chartPanel.setOpaque(true);
        chartPanel.setBackground(Color.WHITE);  // Set background color to white
        chartPanel.setLayout(new BorderLayout());

        // Add the pie chart to the panel
        ChartPanel largerChartPanel = new ChartPanel(pieChart);
        largerChartPanel.setPreferredSize(new Dimension(800, 400));  // Set preferred size for the chart
        chartPanel.add(largerChartPanel, BorderLayout.CENTER);



        // Add the pie chart and sales data panel to the zoom frame
        zoomFrame.add(chartPanel);
        // Create a button panel with "Print" and "Close" buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false); // Transparent background
        buttonPanel.setBounds(
                (chartPanel.getX()) + 5, // Align with chart horizontally
                chartPanel.getY() + chartPanel.getHeight() + 10,    // Position below the chart
                chartPanel.getWidth(),
                50
        );

        // Add a print button
        RoundedButton printButton = new RoundedButton("Print", 20);
        printButton.addActionListener(printEvent -> {
            try {
                // Create a string representation of the chart data
                String chartData = "Chart Data: \n"; // Replace this with actual chart data extraction logic
                chartData += "This is a sample representation of the chart's content.";

                // Use MyPrinter to print the chart data
                MyPrinter printer = new MyPrinter();
                printer.setData(chartData);

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(printer);

                if (job.printDialog()) {
                    job.print();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(zoomFrame, "Error printing chart: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(printButton);

        // Add a close button
        RoundedButton closeButton = new RoundedButton("Close", 20);
        closeButton.addActionListener(closeEvent -> zoomFrame.dispose());
        buttonPanel.add(closeButton);

        // Add components to the frame
        zoomFrame.add(buttonPanel);

        // Display the zoom frame
        zoomFrame.setVisible(true);
    }

    // Method to create the pie chart with dynamic colors
    public JFreeChart createPieChart(int branchId) {
        // Retrieve the category sales data
        Map<String, Integer> categorySales = productController.getCategorySales(branchId);  // Simulated method
        System.out.println(categorySales.entrySet());

        // Create a dataset for the pie chart
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Populate the dataset with data from the category sales map
        for (Map.Entry<String, Integer> categoryEntry : categorySales.entrySet()) {
            // Use category name as the label and the total sales count as the value
            dataset.setValue(categoryEntry.getKey(), categoryEntry.getValue());
            System.out.println("Category: " + categoryEntry.getKey() + " Total Sales: " + categoryEntry.getValue());
        }

        // Create a 3D Pie Chart
        JFreeChart pieChart = ChartFactory.createPieChart3D(
                "Sales by Product Category",  // Chart title
                dataset,                     // Dataset
                true,                        // Include legend
                true,                        // Tooltips enabled
                false                        // URLs disabled
        );

        // Cast the plot to PiePlot3D for customization
        PiePlot3D plot = (PiePlot3D) pieChart.getPlot();

        // Dynamically assign colors to slices (optional)
        assignDynamicSliceColors(plot, categorySales);

        // Adjust the section outline stroke (border thickness)
        plot.setSectionOutlineStroke(new BasicStroke(2.0f));

        // Customize shadow (simulates depth)
        plot.setShadowXOffset(3);  // Horizontal shadow
        plot.setShadowYOffset(3);  // Vertical shadow
        plot.setShadowPaint(Color.GRAY);  // Shadow color

        // Set custom label generator to include both category and percentage
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})",
                NumberFormat.getNumberInstance(),
                NumberFormat.getPercentInstance()));  // {0}: category, {1}: value, {2}: percentage

        return pieChart;
    }

    // Method to dynamically assign colors to pie chart slices
    private void assignDynamicSliceColors(PiePlot3D plot, Map<String, Integer> categorySales) {
        Random random = new Random();
        for (Map.Entry<String, Integer> categoryEntry : categorySales.entrySet()) {
            // Generate a random color for each category
            Color randomColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
            plot.setSectionPaint(categoryEntry.getKey(), randomColor);  // Set color for each slice (category)
        }
    }

    private JFreeChart createProfitChart() {
        // Get the current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Create a TimeSeries object to hold profits over time
        TimeSeries profitSeries = new TimeSeries("Profit");

        // Variable to track the maximum profit
        double maxProfit = 0;

        // Fetch profit data for each year from current year to 9 years ago
        for (int i = 0; i < 10; i++) {
            int year = currentYear - i; // Calculate the year
            double profit = transactionController.fetchProfitForYear(year); // Fetch profit for that year

            // Update maximum profit
            if (profit > maxProfit) {
                maxProfit = profit;
            }

            // Add the data to the TimeSeries
            profitSeries.addOrUpdate(new org.jfree.data.time.Year(year), profit);
        }

        // Create a dataset for the chart
        TimeSeriesCollection dataset = new TimeSeriesCollection(profitSeries);

        // Create the chart using the dataset
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Profit Over Time",   // Title
                "Year",               // X-axis Label
                "Profit (Rs.)",         // Y-axis Label
                dataset,              // Dataset
                false,                // Include legend
                true,                 // Tooltips
                false                 // URLs
        );

        // Customize chart appearance
        chart.setBackgroundPaint(Color.white);

        // Get the plot object for further customization (like setting line color)
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);  // Allow horizontal panning
        plot.setRangePannable(true);   // Allow vertical panning
        plot.setBackgroundPaint(new Color(200, 229, 220));

        // Dynamically set the Y-axis range based on the maximum profit
        double upperBound = Math.ceil(maxProfit * 1.1); // Add 10% buffer to the maximum value
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, upperBound); // Set the range from 0 to the calculated upper bound

        // Customize the line style (if needed)
        plot.getRenderer().setSeriesPaint(0, Color.BLUE); // Set line color to blue

        // Return the chart
        return chart;
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
        salesLabel.setForeground(new Color(67, 166, 67, 255)); // Set text color

        salesLabel.setBounds((reportsPanel.getWidth() - salesLabel.getPreferredSize().width) / 2 +170,
                (reportsPanel.getHeight() ) / 3 +110 ,
                salesLabel.getPreferredSize().width+20,
                salesLabel.getPreferredSize().height);

        // Add the label to the reports panel
        reportsPanel.add(salesLabel);

        // Create a label to display the total sales
        String profitText = "Rs. " + netProfit;
        JLabel profitLabel = new JLabel(profitText);
        profitLabel.setFont(new Font("Arial", Font.BOLD, 22));
        profitLabel.setForeground(new Color(253, 193, 0, 255)); // Set text color

        profitLabel.setBounds((reportsPanel.getWidth() - salesLabel.getPreferredSize().width) / 2 +172,
                (reportsPanel.getHeight() ) / 2 +182,
                profitLabel.getPreferredSize().width +10,
                profitLabel.getPreferredSize().height+10);

        // Add the label to the reports panel
        reportsPanel.add(profitLabel);

        int totalStock = 0;
        totalStock = productController.getStockByBranch(employee.getBranchId());

        // Create a label to display the total sales
        String stockText = totalStock +" items";
        JLabel stockLabel = new JLabel(stockText);
        stockLabel.setFont(new Font("Arial", Font.BOLD, 22));
        stockLabel.setForeground(new Color(241, 21, 61, 226)); // Set text color

        stockLabel.setBounds((reportsPanel.getWidth() - salesLabel.getPreferredSize().width) / 2 +170,
                (reportsPanel.getHeight()/2 -230 ),
                stockLabel.getPreferredSize().width+20,
                stockLabel.getPreferredSize().height);

        // Add the label to the reports panel
        reportsPanel.add(stockLabel);


        // Create and add the Profit Chart Panel
        RoundedPanel profitChartPanel = createChartWithZoomButton();
        profitChartPanel.setBounds(menuWidth -80,(reportsPanel.getHeight() ) / 2 +12 ,455, 235); // Width and height of the button
        // Set the size and position as needed
        reportsPanel.add(profitChartPanel);

        RoundedPanel pieChartPanel = createPieChartWithZoomButton();
        pieChartPanel.setBounds(menuWidth -80,(reportsPanel.getHeight() )/2 -300 ,455, 235); // Width and height of the button

        // Add the chart panel to the JFrame
        reportsPanel.add(pieChartPanel);
        // Add the reports panel to the background panel
        backgroundPanel.add(reportsPanel);

        revalidate();
        repaint();

    }


    public void showSalesHistory() {
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
                    // Fetch average sales data based on the selected year and period
                    Map<Integer, Double> productSales = transactionController.getAverageSales(employee.getBranchId(), selectedPeriod.toLowerCase(), year);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                    // Prepare the sales message to be displayed
                    StringBuilder salesMessage = new StringBuilder("<html><h3>Average Sales for Products:</h3>");
                    for (Map.Entry<Integer, Double> entry : productSales.entrySet()) {
                        salesMessage.append(String.format("<p>Product ID: %d, Average Sales: %.2f</p>", entry.getKey(), entry.getValue()));
                    }
                    salesMessage.append("</html>");

                    JLabel salesLabel = new JLabel(salesMessage.toString());

                    // Wrap the label inside a JScrollPane for scrolling
                    JScrollPane scrollPane = new JScrollPane(salesLabel);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.setPreferredSize(new Dimension(400, 200));  // Adjust size if needed

                    panel.add(scrollPane);

                    // Print button functionality for the sales data
                    RoundedButton printButton = new RoundedButton("Print", 20);
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

                    // Display the panel with sales data
                    JOptionPane.showMessageDialog(null, panel, "Product-wise Average Sales", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error fetching average sales: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }



    private void showProfitsHistoryDialog() {
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

                    // Add a scrollable area for displaying results
                    StringBuilder profitMessage = new StringBuilder("<html><h3>Average Profits for Products:</h3>");
                    for (Map.Entry<Integer, Double> entry : productProfits.entrySet()) {
                        profitMessage.append(String.format("<p>Product ID: %d, Average Profit: %.2f</p>", entry.getKey(), entry.getValue()));
                    }
                    profitMessage.append("</html>");

                    JLabel profitLabel = new JLabel(profitMessage.toString());

                    JScrollPane scrollPane = new JScrollPane(profitLabel);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.setPreferredSize(new Dimension(400, 200));

                    panel.add(scrollPane);

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
