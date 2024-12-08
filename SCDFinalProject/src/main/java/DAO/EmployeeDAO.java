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
            "UPDATE employee SET first_time_joined = FALSE WHERE username = ?";


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

    public boolean insertEmployee(Employee employee) {
        boolean isSuccess = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            // Assuming you have a Database connection utility class to get the connection
            connection = DBConnection.getConnection();

            // Insert the employee without the username (we'll generate it later)
            String sql = "INSERT INTO employee (name, position, email, branch_id, address, phone_number, salary, joining_date, status, first_time_joined) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // To retrieve generated keys
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getPosition());
            statement.setString(3, employee.getEmail());
            statement.setInt(4, employee.getBranchId());
            statement.setString(5, employee.getAddress());
            statement.setString(6, employee.getPhone());
            statement.setBigDecimal(7, employee.getSalary());
            statement.setDate(8, new java.sql.Date(employee.getJoiningDate().getTime()));
            statement.setString(9, employee.getStatus());
            statement.setBoolean(10, true);

            int rowsAffected = statement.executeUpdate();

            // Check if the employee was inserted successfully
            if (rowsAffected > 0) {
                // Retrieve the generated employee ID (assuming auto-increment for employeeId)
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int employeeId = generatedKeys.getInt(1); // Get the generated employee ID

                    // Dynamically set the username by concatenating "name" with the generated employee ID
                    String username = employee.getName() + employeeId;

                    // Now, update the employee record with the generated username
                    String updateSql = "UPDATE employee SET username = ? WHERE employee_id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                        updateStatement.setString(1, username);
                        updateStatement.setInt(2, employeeId);
                        int updateRows = updateStatement.executeUpdate();

                        // Check if the update was successful
                        if (updateRows > 0) {
                            isSuccess = true;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Clean up the resources
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
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
            System.out.println("hello fmr dao " + username + password);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Map the result set to an Employee object
                    Employee employee = mapResultSetToEmployee(resultSet);

                    // Check if this is the first time the employee is logging in (first_time_joined == true)
                    boolean firstTimeLogin = resultSet.getBoolean("first_time_joined");

                    if (firstTimeLogin) {
                        System.out.println("Welcome! This is your first login. Please set up your profile.");

                        // Show the profile setup UI (password update page)
                        UpdatePasswordUI updatePasswordUI = new UpdatePasswordUI(employee);
                        updatePasswordUI.setModal(true);  // Make the UI modal


                        // After profile setup, update the 'first_time_joined' field to false
                        updateFirstTimeLoginFlag(employee.getEmployeeId()); // Update the flag in the database

                        // Optionally, you can confirm password update or any additional logic here
                    }
                    System.out.println(employee.getPassword());
                    return employee; // Return authenticated employee after UI interaction
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if authentication fails
    }


    // Method to update the 'first_time_joined' flag to false after profile setup
    private void updateFirstTimeLoginFlag(int employeeId) {
        String updateQuery = "UPDATE employee SET first_time_joined = false WHERE employee_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, employeeId); // Set the employee's ID

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee profile has been set up. First-time login flag updated to false.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean updatePassword(String username,String newPassword) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement stmt = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false); // Start a transaction

            // First update the password
            preparedStatement = connection.prepareStatement(UPDATE_PASSWORD_QUERY);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected);
            // If the password update is successful, update the 'first_time_joined' field
            if (rowsAffected > 0) {
                System.out.println("status");
                stmt = connection.prepareStatement(UPDATE_FIRST_TIME_JOINED_QUERY);
                stmt.setString(1, username);
                stmt.executeUpdate();
            }

            // Commit the transaction if both updates were successful
            connection.commit();

            return rowsAffected > 0;
        } catch (SQLException e) {
            // Rollback in case of error
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit to true
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
