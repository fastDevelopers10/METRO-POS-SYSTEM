package View;

import Controller.*;
import Model.Branch;
import Model.Employee;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

public class SuperAdminUI extends JFrame {

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    private static final Font TITLE_FONT = new Font("Century Gothic", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Century Gothic", Font.BOLD, 16);
    private static final Font DATA_FONT = new Font("Century Gothic", Font.PLAIN, 26);
    private static final Font BUTTON_FONT = new Font("Century Gothic", Font.PLAIN, 16);
    private static final Font BASIC_FONT = new Font("Century Gothic", Font.PLAIN, 14);
    private static final Color FONT_COLOR = Color.BLACK;
    private JLabel lblBackground;
    private BranchController branchController;
    private DefaultTableModel tableModel,branchtableModel;
    private JTable table,employeeTable;
    private EmployeeController controller;
    private JComboBox<String> statusDropdown,bmstatusDropdown;
    private JPanel profitPanel ;
    public SuperAdminUI() throws SQLException {
        setupFrame();
        setIconImage(loadIcon("images/icons/logo.PNG"));

        this.branchController=new BranchController();
        this.profitPanel = new ProfitPanel();

        mainContentPanel = new JPanel(new CardLayout());
        mainContentPanel.setBounds(265, 0, 1090, 710);
        mainContentPanel.setBackground(new Color(0, 0, 0, 0));
        mainContentPanel.setOpaque(false);
        add(createBackgroundLabel(mainContentPanel));

        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(createBranchPanel(), "Branches");
        mainContentPanel.add(createBranchManagerPanel(), "Branch Managers");
        mainContentPanel.add(createProfitPanel(), "Profit Report");
        mainContentPanel.add(createSalesPanel(), "Sales Report");

        cardLayout = (CardLayout) mainContentPanel.getLayout(); // Get layout
        cardLayout.show(mainContentPanel, "Dashboard"); // Show default panel

        setVisible(true);
    }
    private JPanel createProfitPanel() throws SQLException {
        JPanel panel = new JPanel(null);
        panel.setBounds(230, 0, 1090, 710);
        JLabel lblProfit = new JLabel("Profit Report");
        lblProfit.setFont(TITLE_FONT);
        lblProfit.setBounds(50,18,300,60);
        panel.add(lblProfit);
        profitPanel= new ProfitPanel();
        profitPanel.setBounds(170,85,650,450);
        panel.setBackground(Color.decode("#F9F9F9"));
        panel.add(profitPanel);
        return panel;
    }
    private JPanel createSalesPanel() throws SQLException {
        JPanel panel = new JPanel(null);
        panel.setBounds(230, 0, 1090, 710);
        JLabel lblSales = new JLabel("Sales Report");
        lblSales.setFont(TITLE_FONT);
        lblSales.setBounds(50,18,300,60);
        panel.add(lblSales);
        SalesPanel salesPanel= new SalesPanel();
        salesPanel.setBounds(170,85,650,450);
        panel.setBackground(Color.decode("#F9F9F9"));
        panel.add(salesPanel);

        return panel;
    }

    private void setupFrame() {
        setTitle("Super Admin UI");
        setSize(1320, 710);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
    }

    private JLabel createBackgroundLabel(JPanel contentPanel) {
        lblBackground = new JLabel();
        lblBackground.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/side menu.png")));
        lblBackground.setBounds(0, 0, 1320, 710);
        createSidebarMenu(lblBackground);
        lblBackground.add(contentPanel);
        return lblBackground;
    }

    private Image loadIcon(String path) {
        URL iconURL = getClass().getClassLoader().getResource(path);
        if (iconURL != null) {
            return new ImageIcon(iconURL).getImage();
        } else {
            System.err.println("Error: Unable to load frame icon image.");
            return null;
        }
    }

    private void createSidebarMenu(JLabel lblBackground) {
        JPanel sideMenuPanel = new JPanel();
        sideMenuPanel.setLayout(null);
        sideMenuPanel.setOpaque(false);

        addLabelToSidebar(lblBackground, "Position: Super Admin", 97, 142, 250, 30, BASIC_FONT);
        String username = SuperAdminLoginController.getUsername();
        addLabelToSidebar(lblBackground, username, 115, 118, 220, 40, LABEL_FONT);
        addLabelToSidebar(lblBackground, "Menu", 115, 215, 100, 20, new Font("DM Sans", Font.PLAIN, 14));

        int menuYPosition = 250;
        int menuWidth = 230;
        sideMenuPanel.setBounds(14, menuYPosition, menuWidth, getHeight() - menuYPosition);

        String[][] menuItems = {
                {"Dashboard", "images/icons/dash_icon.png"},
                {"Branch Managers", "images/icons/employees.png"},
                {"Branches", "images/icons/branches.png"},
                {"Change Password", "images/icons/pass_icon.png"},
                {"Logout", "images/icons/logout.png"}
        };

        int buttonYPosition = 4; // Starting position for buttons
        int buttonHeight = 50;   // Height for each button

        final SideMenuButton[] activeButton = {null}; // Track the currently active button

        for (String[] menuItem : menuItems) {
            String text = menuItem[0];
            String iconPath = menuItem[1];

            SideMenuButton button = new SideMenuButton(text, iconPath);
            button.setBounds(0, buttonYPosition, menuWidth, buttonHeight);

            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);

            button.addActionListener(e -> {
                if (activeButton[0] != null) {
                    activeButton[0].setBackground(Color.WHITE); // Default background
                }
                button.setBackground(new Color(200, 229, 220));
                activeButton[0] = button; // Update active button

                try {
                    handleMenuAction(text);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            sideMenuPanel.add(button);
            buttonYPosition += buttonHeight ; // Add space between buttons
        }
        lblBackground.add(sideMenuPanel);
    }

    private void handleMenuAction(String menuItem) throws SQLException {
        switch (menuItem) {
            case "Dashboard":
                cardLayout.show(mainContentPanel, "Dashboard");
                break;
            case "Branch Managers":
                cardLayout.show(mainContentPanel, "Branch Managers");
                break;
            case "Branches":
                cardLayout.show(mainContentPanel, "Branches");
                break;
            case "Change Password":
                new UpdatePasswordUI(new Employee());
                this.dispose();
                break;
            case "Logout":
                logoutAction();
                break;
            default:
                System.out.println("Unknown action: " + menuItem);
                break;
        }
    }

    private void logoutAction() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginOptions().setVisible(true);
        }
    }

    private JPanel createDashboardPanel() throws SQLException {
        JPanel panelDashboard = new JPanel(null);
        panelDashboard.setBounds(230, 0, 1090, 710);
        JLabel lblDashboard=new JLabel();
        lblDashboard.setBounds(0, 0, 1090, 710);
        lblDashboard.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/sa.png")));
        addTitle(lblDashboard);
        panelDashboard.add(lblDashboard);
        addDashboardData(lblDashboard);

        return panelDashboard;
    }

    private JPanel createBranchPanel() throws SQLException {
        JPanel panelBranch = new JPanel(null);
        panelBranch.setBounds(230, 0, 1090, 710);
        panelBranch.setBackground(Color.decode("#F9F9F9"));
        addTitle(panelBranch,2);
        addBranchData(panelBranch);

        return panelBranch;
    }

    private JPanel createBranchManagerPanel() throws SQLException {
        JPanel panelBranchManager = new JPanel(null);
        panelBranchManager.setBounds(230, 0, 1090, 710);
        addTitle(panelBranchManager,3);
        panelBranchManager.setBackground(Color.decode("#F9F9F9"));
        addBranchManagerData(panelBranchManager);

        return panelBranchManager;
    }

    private void addTitle(JPanel panel,int val) {
        String text="";
        if(val==2)
            text="Branches";
        else if(val==3)
            text="Branch Managers";

        JLabel lblTitle = new JLabel(text);
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setForeground(FONT_COLOR);
        lblTitle.setBounds(60, 18, 300, 60);
        panel.add(lblTitle);
    }

    private void addTitle(JLabel panel) {
        JLabel lblTitle = new JLabel("Dashboard");
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setForeground(FONT_COLOR);
        lblTitle.setBounds(50, 18, 300, 60);
        panel.add(lblTitle);
    }

    private void addDashboardData(JLabel panel) throws SQLException {
        ProductController controller = new ProductController();


        addLabel(panel, "Total Products", 55, 140, 200, 20, LABEL_FONT);
        addLabel(panel, String.valueOf(controller.getProductRowCount()), 55, 190, 200, 24, DATA_FONT);

        addLabel(panel, "Remaining Products", 55, 300, 200, 20, LABEL_FONT);
        addLabel(panel, String.valueOf(controller.getTotalProductsSum()), 55, 340, 200, 24, DATA_FONT);

        addLabel(panel, "Profit", 300, 300, 200, 20, LABEL_FONT);
        addLabel(panel, String.valueOf(TransactionController.getOverallProfit()), 300, 340, 200, 24, DATA_FONT);

        JLabel lblSalesVal = addLabel(panel, "Loading...", 300, 190, 200, 24, DATA_FONT);
        addLabel(panel, "Sales", 300, 140, 200, 20, LABEL_FONT);
        loadSalesValue(lblSalesVal);

        addDashboardButtons(panel);
    }

    private void addDashboardButtons(JLabel panel) {
        JButton btnProfitReport = createButton("Profit Report", 5, 80, 200, 30);
        btnProfitReport.addActionListener(e -> cardLayout.show(mainContentPanel, "Profit Report"));
        panel.add(btnProfitReport);

        JButton btnSalesReport = createButton("Sales Report", 155, 80, 200, 30);
        btnSalesReport.addActionListener(e-> cardLayout.show(mainContentPanel, "Sales Report"));
        panel.add(btnSalesReport);
    }

    private void loadSalesValue(JLabel lblSalesVal) {
        SwingWorker<BigDecimal, Void> worker = new SwingWorker<>() {
            @Override
            protected BigDecimal doInBackground() throws Exception {
                return new TransactionController().getTotalSales();
            }

            @Override
            protected void done() {
                try {
                    lblSalesVal.setText(get().toBigInteger().toString());
                } catch (Exception e) {
                    lblSalesVal.setText("Error");
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private JLabel addLabel(JLabel panel, String text, int x, int y, int width, int height, Font font) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        label.setForeground(FONT_COLOR);
        panel.add(label);
        return label;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        styleButton(button);
        return button;
    }

    private void addLabelToSidebar(JLabel lblBackground, String text, int x, int y, int width, int height, Font font) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        label.setForeground(FONT_COLOR);
        lblBackground.add(label);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(FONT_COLOR);
        button.setFont(BUTTON_FONT);
    }
    private void addBranchManagerData(JPanel panel) throws SQLException {
        String[] statusOptions = {"All Branch Managers", "Active", "Inactive"};
        bmstatusDropdown = new JComboBox<>(statusOptions);
        bmstatusDropdown.setBackground(Color.decode("#e6e6e7"));
        bmstatusDropdown.setForeground(Color.BLACK);
        bmstatusDropdown.setFont(new Font("Century Gothic", Font.BOLD, 16));
        bmstatusDropdown.setSelectedIndex(0); // Default to "All Branch Managers"
        bmstatusDropdown.setBounds(770, 65, 200, 40);  // Position dropdown
        bmstatusDropdown.addActionListener(e -> bmUpdateTableBasedOnStatus());
        panel.add(bmstatusDropdown);

        // Button Panel with Add and Update buttons
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
        buttonPanel.setBounds(80, 520, 860, 60);
        buttonPanel.setOpaque(false);

        panel.add(buttonPanel);

        // Add button action
        addButton.addActionListener(e -> {
            String[] positions = {"Branch Manager"};
            BranchController bc = new BranchController();
            List<Integer> branchIds = bc.getAllBranchIds();
            EmployeeForm form = new EmployeeForm(positions, branchIds);
            form.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    List<Employee> branchManagers= null;
                    try {
                        branchManagers = controller.getEmployees();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    bmrefreshTable(branchManagers,(String) bmstatusDropdown.getSelectedItem());
                }
            });
        });

        // Update button action
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

            BranchController bc = new BranchController();
            List<Integer> branchIds = bc.getAllBranchIds();
            JComboBox<Integer> branchIdDropdown = new JComboBox<>(branchIds.toArray(new Integer[0]));
            branchIdDropdown.setSelectedItem(Integer.parseInt(branchId)); // Set current branch ID

            JTextField nameField = new JTextField(name);
            JTextField emailField = new JTextField(email);
            JTextField addressField = new JTextField(address);
            JTextField phoneField = new JTextField(phone);
            JTextField salaryField = new JTextField(salary);
            JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Active", "Inactive"});
            statusDropdown.setSelectedItem(status);

            phoneField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    restrictPhoneNumberLength(phoneField);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    restrictPhoneNumberLength(phoneField);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    restrictPhoneNumberLength(phoneField);
                }
            });

            JPanel p = new JPanel(new GridLayout(0, 2, 10, 10));
            p.add(new JLabel("Name:"));
            p.add(nameField);
            p.add(new JLabel("Email:"));
            p.add(emailField);
            p.add(new JLabel("Branch ID:"));
            p.add(branchIdDropdown);
            p.add(new JLabel("Address:"));
            p.add(addressField);
            p.add(new JLabel("Phone:"));
            p.add(phoneField);
            p.add(new JLabel("Salary:"));
            p.add(salaryField);
            p.add(new JLabel("Status:"));
            p.add(statusDropdown);

            int result = JOptionPane.showConfirmDialog(this, p, "Update Branch Manager", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String Name = nameField.getText();
                    String Email = emailField.getText();
                    String Address = addressField.getText();
                    String Phone = phoneField.getText().trim(); // Trim spaces

                    // Validate email format
                    if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                        JOptionPane.showMessageDialog(this, "Invalid email address. Please enter a valid email.");
                        return;
                    }

                    // Validate phone number length
                    if (Phone.length() != 11 || !Phone.matches("\\d+")) {
                        JOptionPane.showMessageDialog(this, "Phone number must be exactly 11 digits.");
                        return;
                    }

                    BigDecimal Salary = new BigDecimal(salaryField.getText());
                    Employee updatedEmployee = new Employee();
                    updatedEmployee.setEmployeeId(Integer.parseInt(id));
                    updatedEmployee.setName(Name);
                    updatedEmployee.setEmail(Email);
                    updatedEmployee.setBranchId((Integer) branchIdDropdown.getSelectedItem());
                    updatedEmployee.setAddress(Address);
                    updatedEmployee.setPhone(Phone);
                    updatedEmployee.setSalary(Salary);
                    updatedEmployee.setStatus((String) statusDropdown.getSelectedItem());

                    EmployeeController empController = new EmployeeController();
                    empController.updateBranchManager(updatedEmployee);

                    List<Employee> branchManagers=controller.getEmployees();
                    bmrefreshTable(branchManagers,(String) bmstatusDropdown.getSelectedItem());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // Table Setup
        String[] columns = {"ID", "Name", "Email", "Branch ID", "Address", "Phone Number", "Salary", "Joining Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);

        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing all cells
            }
        };

        employeeTable.setModel(tableModel);
        employeeTable.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        employeeTable.setRowHeight(25);
        employeeTable.setBackground(Color.WHITE);
        employeeTable.setGridColor(Color.LIGHT_GRAY);

        JTableHeader tableHeader = employeeTable.getTableHeader();
        tableHeader.setFont(new Font("Century Gothic", Font.BOLD, 16));
        tableHeader.setBackground(Color.decode("#e6e6e7"));
        tableHeader.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBounds(70, 125, 900, 380);
        panel.add(scrollPane);

        controller = new EmployeeController();
        bmUpdateTableBasedOnStatus();
    }
    private void addBranchData(JPanel panel)  {

        String[] statusOptions = {"All Branches", "Active", "Closed"};
        statusDropdown = new JComboBox<>(statusOptions);
        statusDropdown.setBackground(Color.decode("#e6e6e7"));
        statusDropdown.setForeground(Color.BLACK);
        statusDropdown.setFont(new Font("Century Gothic", Font.BOLD, 16));
        statusDropdown.setSelectedIndex(0); // Default to "All Branches"
        statusDropdown.setBounds(770, 65, 200, 40);  // Position dropdown
        statusDropdown.addActionListener(e -> updateBranchTableBasedOnStatus());
        panel.add(statusDropdown);

        String[] columnNames = {"Branch ID", "City", "Name", "Status", "Address", "Phone", "Employees"};
        branchtableModel = new DefaultTableModel(columnNames, 0);
        refreshTable("All Branches");
        refreshTable("All Branches");

        table = new JTable(branchtableModel);
        table.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Century Gothic", Font.BOLD, 16));
        tableHeader.setBackground(Color.decode("#e6e6e7"));
        tableHeader.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(70, 125, 900, 380);  // Position table on the right side with specified bounds
        panel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Branch");
        addButton.setBackground(Color.WHITE);
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Century Gothic", Font.PLAIN, 20));

        JButton updateButton = new JButton("Update Branch");
        updateButton.setBackground(Color.WHITE);
        updateButton.setForeground(Color.BLACK);
        updateButton.setFont(new Font("Century Gothic", Font.PLAIN, 20));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.setBounds(70, 520, 860, 90);
        buttonPanel.setOpaque(false);
        panel.add(buttonPanel);

        addButton.addActionListener(e -> showAddBranchDialog());
        updateButton.addActionListener(e -> showUpdateBranchDialog());

    }

    private void refreshTable(List<Branch> branches) {
        branchtableModel.setRowCount(0); // Clear the existing rows

        for (Branch branch : branches) {
            branchtableModel.addRow(new Object[]{
                    branch.getBranchId(),
                    branch.getCity(),
                    branch.getName(),
                    branch.getStatus(),
                    branch.getAddress(),
                    branch.getPhone(),
                    branch.getNumberOfEmployees()
            });
        }
    }


    private void refreshTable(String statusFilter) {
        branchtableModel.setRowCount(0);
        List<Branch> branches;

        if ("Active".equalsIgnoreCase(statusFilter)) {
            branches = branchController.fetchActiveBranches();
        } else if ("Closed".equalsIgnoreCase(statusFilter)) {
            branches = branchController.fetchClosedBranches();
        } else {
            branches = branchController.fetchAllBranches();
        }

        for (Branch branch : branches) {
            Object[] rowData = {
                    branch.getBranchId(),
                    branch.getCity(),
                    branch.getName(),
                    branch.getStatus(),
                    branch.getAddress(),
                    branch.getPhone(),
                    branch.getNumberOfEmployees()
            };
            branchtableModel.addRow(rowData);
        }
    }
    private void updateBranchTableBasedOnStatus() {
        String selectedStatus = (String) statusDropdown.getSelectedItem();

        List<Branch> branches=null;

        if (selectedStatus.equals("All Branches")) {
            branches = branchController.fetchAllBranches();
        } else if(selectedStatus.equals("Active")){
            branches = branchController.fetchActiveBranches();
        }
        else if(selectedStatus.equals("Closed")){
            branches = branchController.fetchClosedBranches();
        }

        refreshTable(branches); // Ensure this method updates the table correctly
    }
    private void showAddBranchDialog() {
        JTextField cityField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();

        JComboBox<String> statusDdown = new JComboBox<>(new String[]{"Active", "Closed"});
        statusDdown.setSelectedIndex(0);

        JTextField employeesField = new JTextField("0");
        employeesField.setEditable(false);

        Object[] fields = {
                "City:", cityField,
                "Name:", nameField,
                "Status:", statusDdown,
                "Address:", addressField,
                "Phone:", phoneField,
                "Employees (default: 0):", employeesField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Add Branch", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Branch branch = new Branch(
                        0,
                        cityField.getText(),
                        nameField.getText(),
                        (String) statusDdown.getSelectedItem(),
                        addressField.getText(),
                        phoneField.getText(),
                        Integer.parseInt(employeesField.getText())
                );

                if (branchController.addBranch(branch)) {
                    JOptionPane.showMessageDialog(null, "Branch added successfully!");
                    refreshTable((String) statusDropdown.getSelectedItem());
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add branch!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please check your entries.");
            }
        }
    }

    private void showUpdateBranchDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to update.");
            return;
        }

        int branchId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        String city = table.getValueAt(selectedRow, 1).toString();
        String name = table.getValueAt(selectedRow, 2).toString();
        String status = table.getValueAt(selectedRow, 3).toString();
        String address = table.getValueAt(selectedRow, 4).toString();
        String phone = table.getValueAt(selectedRow, 5).toString();
        int employees = Integer.parseInt(table.getValueAt(selectedRow, 6).toString());

        JTextField idField = new JTextField(String.valueOf(branchId));
        idField.setEditable(false);

        JTextField cityField = new JTextField(city);
        JTextField nameField = new JTextField(name);

        JComboBox<String> statusDdown = new JComboBox<>(new String[]{"Active", "Closed"});
        statusDdown.setSelectedItem(status);

        JTextField addressField = new JTextField(address);
        JTextField phoneField = new JTextField(phone);
        JTextField employeesField = new JTextField(String.valueOf(employees));
        employeesField.setEditable(false);

        Object[] fields = {
                "Branch ID :", idField,
                "City:", cityField,
                "Name:", nameField,
                "Status:", statusDdown,
                "Address:", addressField,
                "Phone:", phoneField,
                "Employees :", employeesField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Update Branch", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Branch branch = new Branch(
                        branchId,
                        cityField.getText(),
                        nameField.getText(),
                        (String) statusDdown.getSelectedItem(),
                        addressField.getText(),
                        phoneField.getText(),
                        employees
                );

                if (branchController.updateBranch(branch)) {
                    JOptionPane.showMessageDialog(null, "Branch updated successfully!");
                    refreshTable((String) statusDropdown.getSelectedItem());
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update branch!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please check your entries.");
            }
        }
    }

    private void bmrefreshTable(List<Employee> branchManagers, String selectedItem) {
        tableModel.setRowCount(0); // Clear existing rows
        for (Employee emp : branchManagers) {
            tableModel.addRow(new Object[]{
                    emp.getEmployeeId(),
                    emp.getName(),
                    emp.getEmail(),
                    emp.getBranchId(),
                    emp.getAddress(),
                    emp.getPhone(),
                    emp.getSalary(),
                    emp.getJoiningDate(),
                    emp.getStatus()
            });
        }
        String selectedStatus = (String) bmstatusDropdown.getSelectedItem();
        if ("All Branch Managers".equals(selectedStatus)) {
            controller.populateTable(tableModel); // Populate all branch managers
        } else if ("Active".equals(selectedStatus) || "Inactive".equals(selectedStatus)) {
            controller.populateTableByStatus(tableModel,selectedStatus.toLowerCase());
        }
    }

    private void bmUpdateTableBasedOnStatus() {
        String status = (String) bmstatusDropdown.getSelectedItem();
        if (status.equals("All Branch Managers")) {
            controller.populateTable(tableModel);
        } else {
            controller.populateTableByStatus(tableModel, status);
        }
    }

    private void restrictPhoneNumberLength(JTextField phoneField) {
        String text = phoneField.getText();
        if (text.length() > 11) {
            phoneField.setText(text.substring(0, 11));
        }
    }
}