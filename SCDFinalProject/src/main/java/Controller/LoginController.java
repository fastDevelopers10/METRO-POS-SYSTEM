package Controller;

import Model.Employee;
import Service.EmployeeService;

public class LoginController {
    private final EmployeeService employeeService;

    // Constructor to initialize the service layer
    public LoginController() {
        this.employeeService = new EmployeeService();
    }

    // Validate login credentials
    public boolean validateLogin(String role, String username, String password) {
        Employee employee = employeeService.getEmployeeByUsernameAndRole(username, role);
        return employee != null && employee.getPassword().equals(password);
    }
}
