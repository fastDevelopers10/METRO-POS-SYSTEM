package Controller;

import Service.EmployeeService;
import Model.Employee;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class EmployeeController {
    private static EmployeeService employeeService;

    public EmployeeController() {
        this.employeeService = new EmployeeService(); // Initialize the service
    }

    public List<Employee> getEmployeesByBranch(int branchId) throws Exception {
        return employeeService.getEmployeesByBranch(branchId);
    }

    public boolean updateBranchManager(Employee employee) {
        try {
            boolean isUpdated = employeeService.updateBranchManager(employee); // Call service to update branch manager
            if (isUpdated) {
                JOptionPane.showMessageDialog(null, "Branch manager updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update branch manager!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return isUpdated;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating branch manager: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<Employee> getEmployees() throws SQLException {
        return employeeService.getAllBranchManagers();
    }

    public void populateTable(DefaultTableModel tableModel) {
        try {
            List<Employee> employees = employeeService.getAllBranchManagers();
            tableModel.setRowCount(0);

            for (Employee employee : employees) {
                tableModel.addRow(new Object[]{
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getBranchId(),
                        employee.getAddress(),
                        employee.getPhone(),
                        employee.getSalary(),
                        employee.getJoiningDate(),
                        employee.getStatus()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Populate the table with employees filtered by their status (Active/Inactive)
    public void populateTableByStatus(DefaultTableModel tableModel, String status) {
        // Fetch the list of employees by status (Active/Inactive)

        List<Employee> employees = employeeService.getBranchManagersByStatus(status);

        // Clear the current table model
        tableModel.setRowCount(0);

        // Populate the table with filtered employee data
        for (Employee employee : employees) {
            tableModel.addRow(new Object[]{
                    employee.getEmployeeId(),
                    employee.getName(),
                    employee.getEmail(),
                    employee.getBranchId(),
                    employee.getAddress(),
                    employee.getPhone(),
                    employee.getSalary(),
                    employee.getJoiningDate(),
                    employee.getStatus()
            });
        }
    }
}