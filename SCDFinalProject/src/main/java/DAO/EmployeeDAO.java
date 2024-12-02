package DAO;

import Model.Employee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeDAO {
    private static final String INSERT_EMPLOYEE_QUERY =
            "INSERT INTO employee (name, position, email, branch_id, address, phone_number, salary, joining_date, username, password, status, first_time_joined) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

    private static final String FIND_EMPLOYEE_QUERY =
            "SELECT * FROM employee WHERE username = ? AND position = ?";

    private static final String UPDATE_PASSWORD_QUERY =
            "UPDATE employee SET password = ? WHERE username = ?";

    // Method to fetch employees by branch ID
    public List<Employee> getEmployeesByBranch(int branchId) throws Exception {
        String query = "SELECT * FROM employee WHERE branch_id = ?";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, branchId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Mapping ResultSet to Employee object
                    Employee employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error fetching employees by branch ID: " + e.getMessage(), e);
        }
        System.out.println("Number of employees: " +employees.size());

        return employees;
    }

    // Insert a new employee into the database
    public boolean insertEmployee(Employee employee) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            // Set all parameters for the employee (except employeeId, as it's auto-generated)
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPosition());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setInt(4, employee.getBranchId());
            preparedStatement.setString(5, employee.getAddress());
            preparedStatement.setString(6, employee.getPhone());
            preparedStatement.setBigDecimal(7, employee.getSalary());
            preparedStatement.setDate(8, new java.sql.Date(employee.getJoiningDate().getTime()));
            preparedStatement.setString(9, employee.getUsername());
            preparedStatement.setString(10, employee.getPassword());
            preparedStatement.setString(11, employee.getStatus());
            preparedStatement.setBoolean(12, employee.isFirstTimeJoined());

            // Execute the query and get the generated keys (auto-generated employee_id)
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insertion was successful and retrieve the generated employee_id
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedEmployeeId = generatedKeys.getInt(1);  // Get the auto-generated ID
                        employee.setEmployeeId(generatedEmployeeId);  // Set the generated ID in the Employee object
                    }
                }
                return true; // Successfully inserted and set the ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Insertion failed
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

    // Method to update employee's password
    public boolean updatePassword(String username, String newPassword) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD_QUERY)) {

            // Set the new password
            preparedStatement.setString(1, newPassword);

            preparedStatement.setString(2, username);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            // Return true if the password was updated successfully
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Employee mapResultSetToEmployee(ResultSet resultSet) throws SQLException {
        // Fetch fields from the ResultSet
        int employeeId = resultSet.getInt("employee_id");
        String username = resultSet.getString("username");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        int branchId = resultSet.getInt("branch_id");
        String address = resultSet.getString("address");
        BigDecimal salary = resultSet.getBigDecimal("salary");
        String phone = resultSet.getString("phone_number");
        String status = resultSet.getString("status");
        Date joiningDate = resultSet.getDate("joining_date");
        String position = resultSet.getString("position");
        boolean firstTimeJoined = resultSet.getBoolean("first_time_joined");

        // Create Employee object
        Employee employee = new Employee(username, name, email, password, branchId, address, salary, phone, status, joiningDate, position);
        employee.setEmployeeId(employeeId);

        return employee;
    }
}
