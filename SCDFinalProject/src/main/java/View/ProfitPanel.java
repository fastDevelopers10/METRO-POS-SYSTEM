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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProfitPanel extends JPanel {
    private JComboBox<String> timeRangeDropdown;
    private JTextField startDateField, endDateField;
    private JPanel customRangePanel; // Panel to group custom range components
    private DefaultTableModel tableModel;
    private TransactionController profitController;
    private JLabel timeRangeLabel; // Label to display time range

    public ProfitPanel() {
        profitController = new TransactionController(); // Controller instance

        // Set panel size, layout, and background color
        setSize(new Dimension(650, 450));
        setLayout(new BorderLayout());
        setBackground(new Color(0xf9f9f9));

        JPanel topPanel = new JPanel(new FlowLayout());
        setBackground(new Color(0xf9f9f9));
        initializeComponents(topPanel);

        // Table for displaying profit data
        tableModel = new DefaultTableModel(new String[]{"Branch ID", "Total Profit"}, 0);
        JTable table = new JTable(tableModel);
        table.setBackground(Color.WHITE);


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(473,120,500,400);
        scrollPane.setBackground(Color.WHITE);
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

        RoundedButton printButton = new RoundedButton("Print",3);
        printButton.addActionListener(e -> {
            try {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int rowCount = model.getRowCount();
                int colCount = model.getColumnCount();

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
                String title = "Profit Report";
                MyPrinter.MyPrinterWithTableData printerWithData = new MyPrinter.MyPrinterWithTableData();
                printerWithData.setTableData(columnHeaders, tableData, title); // Set the table data and title

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(printerWithData); // Set the printable object (MyPrinterWithTableData)

                if (job.printDialog()) {
                    job.print();
                }
            } catch (PrinterException ex) {
                // Handle any printing errors
                JOptionPane.showMessageDialog(null, "Failed to print: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });




        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(printButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeComponents(JPanel panel) {

        String[] timeRanges = {"Today", "Weekly", "Monthly", "Yearly", "Custom Range"};
        timeRangeDropdown = new JComboBox<>(timeRanges);
        timeRangeDropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        RoundedButton fetchProfitButton = new RoundedButton("Fetch Profit",3);

        fetchProfitButton.addActionListener(e -> displayProfit());
        timeRangeLabel = new JLabel("Profit Data for: ");
        timeRangeLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
        timeRangeLabel.setForeground(Color.decode("#eeeeee"));

        JLabel heading=new JLabel("Select Time Range:");
        heading.setFont(new Font("Century Gothic", Font.BOLD, 16));
        panel.add(heading);
        panel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        panel.setForeground(Color.BLACK);
        panel.add(timeRangeDropdown);
        panel.add(customRangePanel);
        panel.add(fetchProfitButton);
        panel.add(timeRangeLabel);

        toggleDateFields();

    }

    private void toggleDateFields() {
        boolean showDateFields = "Custom Range".equals(timeRangeDropdown.getSelectedItem());
        customRangePanel.setVisible(showDateFields);

        customRangePanel.getParent().revalidate();
        customRangePanel.getParent().repaint();
    }

    private void displayProfit() {
        tableModel.setRowCount(0); // Clear previous data
        String selectedTimeRange = (String) timeRangeDropdown.getSelectedItem();

        try {
            Map<Integer, Double> profit;
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

                profit = profitController.fetchProfitForDateRange(
                        new java.sql.Date(startDate.getTime()),
                        new java.sql.Date(endDate.getTime())
                );
            } else {
                profit = profitController.fetchProfit(selectedTimeRange);
            }

            // Update the time range label
            timeRangeLabel.setText("Profit Data for: " + selectedTimeRange);

            if (profit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No profit data found for " + selectedTimeRange, "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                profit.forEach((branchId, totalProfit) ->
                        tableModel.addRow(new Object[]{branchId, totalProfit})
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