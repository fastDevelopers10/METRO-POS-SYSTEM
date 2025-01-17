package View;

import Controller.TransactionController;
import Model.MyPrinter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SalesPanel extends JPanel {
    private JComboBox<String> timeRangeDropdown;
    private JTextField startDateField, endDateField;
    private JPanel customRangePanel; // Panel to group custom range components
    private DefaultTableModel tableModel;
    private TransactionController salesController;
    private JLabel timeRangeLabel; // Label to display time range

    public SalesPanel() {
        salesController = new TransactionController(); // Controller instance

        // Set panel size, layout, and background color
        setPreferredSize(new Dimension(600, 510));
        setLayout(new BorderLayout());
        setBackground(new Color(0xf9f9f9));

        JPanel topPanel = new JPanel(new FlowLayout());
        setBackground(new Color(0xf9f9f9));
        initializeComponents(topPanel);

        // Table for displaying sales data
        tableModel = new DefaultTableModel(new String[]{"Branch ID", "Total Sales"}, 0);
        JTable table = new JTable(tableModel);
        table.setBackground(new Color(0xf9f9f9));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(new Color(0xf9f9f9));
        scrollPane.setBounds(373, 120, 600, 510);

        // Set table font to Century Gothic
        table.setFont(new Font("Century Gothic", Font.PLAIN, 16));

        // Customize the table header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Century Gothic", Font.BOLD, 16));
        tableHeader.setBackground(Color.decode("#e6e6e7")); // Set header background to a nice blue color
        tableHeader.setForeground(Color.BLACK); // Set header text color to white

        // Align header text to center
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Add components to the panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JButton printButton = new JButton("Print Sales Report");
        printButton.setBackground(new Color(200, 229, 220));
        printButton.setFont(new Font("Century Gothic", Font.BOLD, 16));
        printButton.setForeground(Color.BLACK);
        printButton.addActionListener(e -> {
            try {
                // Extract table data
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int rowCount = model.getRowCount();
                int colCount = model.getColumnCount();

                // Prepare data for printing
                String[] columnHeaders = new String[colCount];
                for (int col = 0; col < colCount; col++) {
                    columnHeaders[col] = model.getColumnName(col);  // Get column names for headers
                }

                String[][] tableData = new String[rowCount][colCount];
                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < colCount; col++) {
                        tableData[row][col] = model.getValueAt(row, col).toString(); // Fill table data
                    }
                }

                // Title for the printed document (e.g., "Sales Report")
                String title = "Sales Report"; // You can change this as per your needs

                // Create the inner printer class instance
                MyPrinter.MyPrinterWithTableData printerWithData = new MyPrinter.MyPrinterWithTableData();
                printerWithData.setTableData(columnHeaders, tableData, title); // Set the table data and title

                // Create a PrinterJob object
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(printerWithData); // Set the printable object (MyPrinterWithTableData)

                // Show the print dialog and print if confirmed
                if (job.printDialog()) {
                    job.print(); // Start the print process if the user confirms
                }
            } catch (PrinterException ex) {
                // Handle any printing errors
                JOptionPane.showMessageDialog(null, "Failed to print: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(0xf9f9f9));
        bottomPanel.add(printButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeComponents(JPanel panel) {
        String[] timeRanges = {"Today", "Weekly", "Monthly", "Yearly", "Custom Range"};
        timeRangeDropdown = new JComboBox<>(timeRanges);
        timeRangeDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                component.setFont(new Font("Century Gothic", Font.PLAIN, 14)); // Set font for list items
                return component;
            }
        });
        timeRangeDropdown.addActionListener(e -> toggleDateFields());

        customRangePanel = new JPanel();
        customRangePanel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        customRangePanel.setLayout(new BoxLayout(customRangePanel, BoxLayout.Y_AXIS));
        JLabel startDate=new JLabel("Start Date (dd-MM-yyyy):");
        startDate.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        customRangePanel.add(startDate);
        startDateField = new JTextField(10);
        customRangePanel.add(startDateField);
        customRangePanel.add(Box.createVerticalStrut(10));
        JLabel endDate=new JLabel("End Date (dd-MM-yyyy):");
        endDate.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        customRangePanel.add(endDate);
        endDateField = new JTextField(10);
        customRangePanel.add(endDateField);

        JButton fetchSalesButton = new JButton("Fetch Sales");
        fetchSalesButton.setBackground(new Color(200, 229, 220));
        fetchSalesButton.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        fetchSalesButton.setForeground(Color.BLACK);

        fetchSalesButton.addActionListener(e -> displaySales());

        timeRangeLabel = new JLabel("Sales Data for: ");
        timeRangeLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
        timeRangeLabel.setForeground(Color.decode("#eeeeee"));

        // Add components to the panel
        JLabel heading=new JLabel("Select Time Range:");
        heading.setFont(new Font("Century Gothic", Font.BOLD, 16));
        panel.add(heading);
        panel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        panel.setForeground(Color.BLACK);
        panel.add(timeRangeDropdown);
        panel.add(customRangePanel);
        panel.add(fetchSalesButton);
        panel.add(timeRangeLabel);

        toggleDateFields();
    }

    private void toggleDateFields() {
        boolean showDateFields = "Custom Range".equals(timeRangeDropdown.getSelectedItem());
        customRangePanel.setVisible(showDateFields);

        customRangePanel.getParent().revalidate();
        customRangePanel.getParent().repaint();
    }

    private void displaySales() {
        tableModel.setRowCount(0); // Clear previous data
        String selectedTimeRange = (String) timeRangeDropdown.getSelectedItem();

        try {
            Map<Integer, BigDecimal> sales;
            if ("Custom Range".equals(selectedTimeRange)) {
                String startDateStr = startDateField.getText();
                String endDateStr = endDateField.getText();

                if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter both start and end dates.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Date startDate = parseDate(startDateStr);
                Date endDate = parseDate(endDateStr);

                if (startDate == null || endDate == null) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd-MM-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                sales = salesController.fetchSalesForDateRange(
                        new java.sql.Date(startDate.getTime()),
                        new java.sql.Date(endDate.getTime())
                );
            } else {
                sales = salesController.fetchSales(selectedTimeRange);
            }

            // Update the time range label
            timeRangeLabel.setText("Sales Data for: " + selectedTimeRange);

            if (sales.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No sales data found for " + selectedTimeRange, "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                sales.forEach((branchId, totalSales) ->
                        tableModel.addRow(new Object[]{branchId, totalSales})
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

}