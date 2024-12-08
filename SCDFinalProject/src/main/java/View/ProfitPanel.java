package View;

import Controller.ProfitController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProfitPanel extends JPanel {
    private JComboBox<String> timeRangeDropdown;
    private JTextField startDateField, endDateField;
    private JPanel customRangePanel; // Panel to group custom range components
    private DefaultTableModel tableModel;
    private ProfitController profitController;

    public ProfitPanel() {
        profitController = new ProfitController(); // Controller instance

        // Set panel size, layout, and background color
        setPreferredSize(new Dimension(700, 510));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // Set the background color of the panel to white

        // Initialize components
        JPanel topPanel = new JPanel(new FlowLayout());
        initializeComponents(topPanel);

        // Table for displaying profit data
        tableModel = new DefaultTableModel(new String[]{"Branch ID", "Total Profit"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setBounds(373,100,700,510);

        // Set table font to Century Gothic
        table.setFont(new Font("Century Gothic", Font.PLAIN, 16));

        // Customize the table header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Century Gothic", Font.BOLD, 16));
        tableHeader.setBackground(new Color(0, 123, 255)); // Set header background to a nice blue color
        tableHeader.setForeground(Color.WHITE); // Set header text color to white

        // Align header text to center
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Add components to the panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initializeComponents(JPanel panel) {
        // Dropdown for selecting time range
        String[] timeRanges = {"Today's Profit", "Weekly Profit", "Monthly Profit", "Yearly Profit", "Custom Range"};
        timeRangeDropdown = new JComboBox<>(timeRanges);
        timeRangeDropdown.addActionListener(e -> toggleDateFields());

        // Custom range components
        customRangePanel = new JPanel();
        customRangePanel.setLayout(new BoxLayout(customRangePanel, BoxLayout.Y_AXIS)); // Use BoxLayout to stack components vertically
        customRangePanel.add(new JLabel("Start Date (dd-MM-yyyy):"));
        startDateField = new JTextField(10);
        customRangePanel.add(startDateField);
        customRangePanel.add(Box.createVerticalStrut(10)); // Add some space between the text fields
        customRangePanel.add(new JLabel("End Date (dd-MM-yyyy):"));
        endDateField = new JTextField(10);
        customRangePanel.add(endDateField);

        JButton fetchProfitButton = new JButton("Fetch Profit");
        fetchProfitButton.setBackground(new Color(0, 123, 255));
        fetchProfitButton.setFont(new Font("Century Gothic", Font.BOLD, 16));
        fetchProfitButton.setForeground(Color.WHITE);

        fetchProfitButton.addActionListener(e -> displayProfit());

        // Add components to the panel
        panel.add(new JLabel("Select Time Range:"));
        panel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        panel.setForeground(Color.BLACK);
        panel.add(timeRangeDropdown);
        panel.add(customRangePanel);
        panel.add(fetchProfitButton);

        // Hide the custom range panel initially
        toggleDateFields();
    }


    private void toggleDateFields() {
        boolean showDateFields = "Custom Range".equals(timeRangeDropdown.getSelectedItem());
        customRangePanel.setVisible(showDateFields);

        // Ensure proper layout update
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

                // Fetch profit for custom range
                profit = profitController.fetchProfitForDateRange(
                        new java.sql.Date(startDate.getTime()),
                        new java.sql.Date(endDate.getTime())
                );
            } else {
                // Fetch profit for predefined range
                profit = profitController.fetchProfit(selectedTimeRange);
            }

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


public static void main(String[] args) {
        // Test the panel inside a JFrame
        JFrame frame = new JFrame("Profit Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 510);
        frame.add(new ProfitPanel());
        frame.setVisible(true);
    }
}
