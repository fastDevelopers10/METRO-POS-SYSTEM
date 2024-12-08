package DAO;

import DAO.EmployeeDAO;
import Model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    private EmployeeDAO employeeDAO;

    @BeforeEach
    void setUp() throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/METRO_POS_SYSTEM", "root", "password");
        employeeDAO = new EmployeeDAO(); // This uses the real database connection
    }

    @Test
    void getEmployeesByBranch() throws Exception {

        int branchId = 1;

        List<Employee> employees = employeeDAO.getEmployeesByBranch(branchId);

        assertNotNull(employees, "Employee list should not be null");
        assertTrue(employees.size() > 0, "There should be at least one employee in the branch");
        assertTrue(employees.stream().allMatch(e -> e.getBranchId() == branchId), "All employees should belong to the branch");
    }

    @Test
    void insertEmployee() throws SQLException {

        Employee employee = new Employee("username123", "John Doe", "john.doe@example.com", "password123",
                1, "123 Street", new BigDecimal("5000.00"), "1234567890", "active", new Date());

        boolean result = employeeDAO.insertEmployee(employee);
        assertTrue(result, "Employee should be inserted successfully");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/METRO_POS_SYSTEM", "root", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employee WHERE username = ?")) {

            stmt.setString(1, "username123");
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Employee should be found in the database");
            assertEquals("John Doe", rs.getString("name"), "Employee name mismatch");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findEmployeeByUsernameAndPass() throws SQLException {

        String username = "existing_user";
        String password = "password123";

        Employee employee = employeeDAO.findEmployeeByUsernameAndPass(username, password);

        assertNotNull(employee, "Employee should be found");
        assertEquals(username, employee.getUsername(), "Username mismatch");
    }

    @Test
    void updatePassword() throws SQLException {

        String username = "existing_user";
        String newPassword = "newpassword123";

        boolean result = employeeDAO.updatePassword(username, newPassword);

        assertTrue(result, "Password should be updated successfully");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/METRO_POS_SYSTEM", "root", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employee WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Employee should be found in the database");
            assertEquals(newPassword, rs.getString("password"), "Password mismatch");
        }
    }

    @Test
    void getAllBranchManagers() throws SQLException {

        List<Employee> managers = employeeDAO.getAllBranchManagers();

        assertNotNull(managers, "Branch managers list should not be null");
        assertTrue(managers.size() > 0, "There should be at least one branch manager");
        assertTrue(managers.stream().allMatch(e -> "branch manager".equalsIgnoreCase(e.getPosition())), "All should be branch managers");
    }
}
