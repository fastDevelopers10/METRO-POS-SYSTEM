package Controller;

import Model.Employee;
import Service.EmployeeService;

import java.util.List;

public class EmployeeController {
    private  EmployeeService employeeService;

    public  EmployeeController() {
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
}
