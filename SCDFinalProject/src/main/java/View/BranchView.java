package View;

import Controller.BranchController;
import Model.Branch;
import Service.BranchService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class BranchView extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField tfCity, tfName, tfStatus, tfAddress, tfPhone, tfNoOfEmployees;
    private final JButton btnAdd, btnUpdate;

    public BranchView() {
        setTitle("Branch Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "City", "Name", "Status", "Address", "Phone", "Employees"}, 0);
        table = new JTable(tableModel);

        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        tfCity = new JTextField();
        tfName = new JTextField();
        tfStatus = new JTextField();
        tfAddress = new JTextField();
        tfPhone = new JTextField();
        tfNoOfEmployees = new JTextField();
        formPanel.add(new JLabel("City:"));
        formPanel.add(tfCity);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(tfName);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(tfStatus);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(tfAddress);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(tfPhone);
        formPanel.add(new JLabel("Employees:"));
        formPanel.add(tfNoOfEmployees);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void refreshTable(List<Branch> branches) {
        System.out.println("[DEBUG] Refreshing table. Number of branches: " + branches.size());
        tableModel.setRowCount(0);
        for (Branch branch : branches) {
            System.out.println("[DEBUG] Adding branch to table: " + branch);
            tableModel.addRow(new Object[]{branch.getBranchId(), branch.getCity(), branch.getName(), branch.getStatus(), branch.getAddress(), branch.getPhone(), branch.getNumberOfEmployees()});
        }
    }

    public Branch getBranchFormInput() {
        System.out.println("[DEBUG] Collecting branch form input.");
        int branchId = table.getSelectedRow() >= 0 ? (int) table.getValueAt(table.getSelectedRow(), 0) : 0; // If updating, use selected ID
        Branch branch = new Branch(
                branchId,
                tfCity.getText(),
                tfName.getText(),
                tfStatus.getText(),
                tfAddress.getText(),
                tfPhone.getText(),
                Integer.parseInt(tfNoOfEmployees.getText())
        );
        System.out.println("[DEBUG] Branch form input collected: " + branch);
        return branch;
    }

    public void showError(String message) {
        System.err.println("[ERROR] Displaying error to user: " + message);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void addAddButtonListener(ActionListener listener) {
        btnAdd.addActionListener(listener);
    }

    public void addUpdateButtonListener(ActionListener listener) {
        btnUpdate.addActionListener(listener);
    }


    public static void main(String[] args) {
        BranchService branchService = new BranchService();
        BranchView branchView = new BranchView();
        new BranchController();
    }

}


