package Service;

import Model.Employee;
import DAO.EmployeeDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class EmployeeService {
    private final EmployeeDAO employeeDAO;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO(); // Initialize the DAO
    }

    public Employee getEmployeeByUsernameAndPass(String username, String pass) {
        return employeeDAO.findEmployeeByUsernameAndPass(username, pass);
    }

    public boolean updateEmployeePassword(String username, String newPassword) {
        return employeeDAO.updatePassword(username, newPassword);
    }

    public boolean updateBranchManager(Employee employee) throws SQLException {
        return employeeDAO.updateBranchManager(employee); // Call DAO to update branch manager
    }

    public boolean insertEmployee(String username, String name, String email, String password,
                                  int branchId, String address, BigDecimal salary, String phone,
                                  String status, Date joiningDate, String position) {
        Employee employee = new Employee(username, name, email, password, branchId, address, salary, phone, status, joiningDate, position);
        return employeeDAO.insertEmployee(employee); // Call DAO to insert employee
    }

    public List<Employee> getEmployeesByBranch(int branchId) throws Exception {
        return employeeDAO.getEmployeesByBranch(branchId); // Fetch employees by branch from DAO
    }

    public List<Employee> getAllBranchManagers() throws SQLException {
        return employeeDAO.getAllBranchManagers(); // Fetch all branch managers
    }
    public List<Employee> getBranchManagersByStatus(String status) {
        try {
            return employeeDAO.getBranchManagersByStatus(status); // Call DAO to fetch branch managers by status
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving branch managers by status: " + status, e);
        }
    }
}