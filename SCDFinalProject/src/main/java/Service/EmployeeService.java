package Service;

import DAO.EmployeeDAO;
import Model.Employee;

public class EmployeeService {
    private final EmployeeDAO employeeDAO;

    // Constructor to initialize the DAO layer
    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }

    // Get employee by username and role
    public Employee getEmployeeByUsernameAndRole(String username, String role) {
        return employeeDAO.findEmployeeByUsernameAndRole(username, role);
    }
}
