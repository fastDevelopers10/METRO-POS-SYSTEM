package Service;

import DAO.EmployeeDAO;
import Model.Employee;

import java.math.BigDecimal;
import java.sql.Date;

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

    // Method to update the employee password
    public boolean updateEmployeePassword(String employeeId, String newPassword) {
        return employeeDAO.updatePassword(employeeId, newPassword); // Call DAO to update password
    }

    // Method to add a new employee
    public boolean insertEmployee(String employeeId, String username, String email, String password,
                               int branchCode, String address, BigDecimal salary, String phone,
                               String status, Date joiningDate, String employeeType) {
        Employee employee = new Employee(employeeId, username, email, password, branchCode, address, salary, phone, status, joiningDate, employeeType);
        return employeeDAO.insertEmployee(employee); // Call DAO to insert employee
    }

}
