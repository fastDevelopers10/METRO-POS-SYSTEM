package Service;

import DAO.EmployeeDAO;
import Model.Employee;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class EmployeeService {
    private final EmployeeDAO employeeDAO;

    // Constructor to initialize the DAO layer
    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }

    // Get employee by username and role
    public Employee getEmployeeByUsernameAndRole(String username, String role) {
        return employeeDAO.findEmployeeByUsernameAndPass(username, role);
    }

    // Method to update the employee password
    public boolean updateEmployeePassword(String username, String newPassword) {
        return employeeDAO.updatePassword(username, newPassword); // Call DAO to update password
    }

    // Method to add a new employee
    public boolean insertEmployee( String username, String name, String email, String password,
                               int branchId, String address, BigDecimal salary, String phone,
                               String status, Date joiningDate, String position) {
        Employee employee = new Employee( username, name, email, password, branchId, address, salary, phone, status, joiningDate, position);
        return employeeDAO.insertEmployee(employee); // Call DAO to insert employee
    }
    // Get a list of employees by branch ID
    public List<Employee> getEmployeesByBranch(int branchId) {
        try {
            return employeeDAO.getEmployeesByBranch(branchId); // Call DAO to fetch employees
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving employees for branch ID: " + branchId, e);
        }
    }

    public boolean insertEmployee(Employee employee) {
        EmployeeDAO emp= new EmployeeDAO();
       return emp.insertEmployee(employee);
    }
}
