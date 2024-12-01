package DAO;

import Model.Employee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;

public class EmployeeDAO {
    private static final String INSERT_EMPLOYEE_QUERY =
            "INSERT INTO employees (employee_id, username, email, password, branch_code, address, salary, phone, status, joining_date, employee_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_EMPLOYEE_QUERY =
            "SELECT * FROM employees WHERE username = ? AND employee_type = ?";

    // Insert a new employee into the database
    public boolean insertEmployee(Employee employee) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_QUERY)) {

            preparedStatement.setString(1, employee.getEmployeeId());
            preparedStatement.setString(2, employee.getUsername());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setString(4, employee.getPassword());
            preparedStatement.setString(5, employee.getBranchCode());
            preparedStatement.setString(6, employee.getAddress());
            preparedStatement.setBigDecimal(7, employee.getSalary());
            preparedStatement.setString(8, employee.getPhone());
            preparedStatement.setString(9, employee.getStatus());
            preparedStatement.setDate(10, new java.sql.Date(employee.getJoiningDate().getTime()));
            preparedStatement.setString(11, employee.getEmployeeType());

            int rowsAffected = preparedStatement.executeUpdate();

            // Return true if the insertion was successful
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Find an employee by username and role
    public Employee findEmployeeByUsernameAndRole(String username, String role) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_EMPLOYEE_QUERY)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, role);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEmployee(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Map the ResultSet row to an Employee object
    private Employee mapResultSetToEmployee(ResultSet resultSet) throws SQLException {
        String employeeId = resultSet.getString("employee_id");
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String branchCode = resultSet.getString("branch_code");
        String address = resultSet.getString("address");
        BigDecimal salary = resultSet.getBigDecimal("salary");
        String phone = resultSet.getString("phone");
        String status = resultSet.getString("status");
        Date joiningDate = resultSet.getDate("joining_date");
        String employeeType = resultSet.getString("employee_type");

        return new Employee(employeeId, username, email, password, branchCode, address, salary, phone, status, joiningDate, employeeType);
    }
}
