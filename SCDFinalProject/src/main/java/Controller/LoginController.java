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
    public Employee validateLogin( String username, String password) {
        Employee employee = employeeService.getEmployeeByUsernameAndPass(username, password);
        return employee ;
    }
}
