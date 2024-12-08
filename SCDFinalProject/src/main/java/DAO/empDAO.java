package SCDFinalProject.src.main.java.DAO;

import SCDFinalProject.src.main.java.Model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class empDAO {
    public List<Employee> getAllEmployees() throws SQLException {
        String query = "SELECT * FROM Employee WHERE position='branch manager';";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("email"),
                        rs.getInt("branch_id"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getDouble("salary"),
                        rs.getDate("joining_date"),
                        rs.getString("status")
                ));
            }
        }

        return employees;
    }
}
