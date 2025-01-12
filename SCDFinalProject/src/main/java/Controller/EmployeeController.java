package Controller;

import Service.EmployeeService;
import Model.Employee;
import View.BranchManagerTableUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class EmployeeController {
    private EmployeeService employeeService;
    private BranchManagerTableUI ui;

    public  EmployeeController() {
        this.employeeService = new EmployeeService();
    }

    public EmployeeController(BranchManagerTableUI ui) throws SQLException {
        this.ui = ui;
        this.employeeService = new EmployeeService();
    }

    public List<Employee> getEmployeesByBranch(int branchId) {
        return employeeService.getEmployeesByBranch(branchId);
    }
    // Method to add a new employee to the database
    public void insertEmployee(Employee employee) {
        // Assuming EmployeeService has a method to insert an employee into the database
        if (employee != null) {
            // Call the service method to insert the employee into the database
            boolean isAdded = employeeService.insertEmployee(employee);

            if (isAdded) {
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Failed to add employee.");
            }
        }
    }

    public void populateTable() {

        employeeService.populateBranchManagerTable(ui.getTableModel());
    }

    /*@Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(ui, "Button clicked!");
    }*/
}
