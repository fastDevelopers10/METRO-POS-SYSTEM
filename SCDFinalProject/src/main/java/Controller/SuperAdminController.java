package Controller;

import Service.EmployeeService;

import java.math.BigDecimal;
import java.sql.Date;

public class SuperAdminController {

    private EmployeeService employeeService;

    public SuperAdminController() {
        employeeService = new EmployeeService(); // Initialize the EmployeeService
    }

    // Method to update employee password
    public boolean updateEmployeePassword(String employeeId, String newPassword) {
        return employeeService.updateEmployeePassword(employeeId, newPassword);
    }

    // Method to add a new employee
    public boolean addEmployee(String employeeId, String username, String email, String password,
                               int branchCode, String address, BigDecimal salary, String phone,
                               String status, Date joiningDate, String employeeType) {
        return employeeService.addEmployee(employeeId, username, email, password, branchCode, address, salary, phone, status, joiningDate, employeeType);
    }
}
