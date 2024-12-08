package DAO;

import Model.Employee;
import View.UpdatePasswordUI;

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
            "SELECT * FROM employee WHERE username = ? AND password = ?";

    private static final String UPDATE_PASSWORD_QUERY =
            "UPDATE employee SET password = ? WHERE username = ?";
    private static final String UPDATE_FIRST_TIME_JOINED_QUERY =
            "UPDATE employee SET first_time_joined = FALSE WHERE employee_id = ?";


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

    // Method to insert a new employee into the database
    public boolean insertEmployee(Employee employee) {
        boolean isSuccess = false;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Assuming you have a Database connection utility class to get the connection
            connection = DBConnection.getConnection();

            String sql = "INSERT INTO employee (name, position, email, branch_id, address, phone_number, salary, joining_date, username, password, status, first_time_joined) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);
            // Set the parameters for the prepared statement
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getPosition());
            statement.setString(3, employee.getEmail());
            statement.setInt(4, employee.getBranchId());
            statement.setString(5, employee.getAddress());
            statement.setString(6, employee.getPhone());
            statement.setBigDecimal(7, employee.getSalary());
            statement.setDate(8, new java.sql.Date(employee.getJoiningDate().getTime()));
            statement.setString(9, employee.getUsername());
            statement.setString(10, employee.getPassword());
            statement.setString(11, employee.getStatus());
            statement.setBoolean(12, employee.isFirstTimeJoined());

            int rowsAffected = statement.executeUpdate();

            // Check if the employee was added successfully
            if (rowsAffected > 0) {
                isSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Clean up the resources
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isSuccess;
    }

    // Authenticate and check first-time login
    public Employee findEmployeeByUsernameAndPass(String username, String password) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_EMPLOYEE_QUERY)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Map the result set to an Employee object
                    Employee employee = mapResultSetToEmployee(resultSet);

                    // Check if this is the first time the employee is logging in
                    if (employee.isFirstTimeJoined()) {
                        System.out.println("Welcome! This is your first login. Please set up your profile.");

                        // Update the first_time_joined flag to false
                        new UpdatePasswordUI();
                    }

                    return employee; // Return authenticated employee
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if authentication fails
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
            if(rowsAffected>0)
            {        PreparedStatement stmt = connection.prepareStatement(UPDATE_FIRST_TIME_JOINED_QUERY);
                        stmt.executeUpdate();
            }
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
    public List<Employee> getAllBranchManagers() throws SQLException {
        String query = "SELECT * FROM Employee WHERE position='branch manager';";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("branch_id"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getBigDecimal("salary"),
                        rs.getDate("joining_date"),
                        rs.getString("status")
                ));
            }
        }

        return employees;
}

}
