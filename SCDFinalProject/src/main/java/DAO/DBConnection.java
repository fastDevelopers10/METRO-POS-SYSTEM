package SCDFinalProject.src.main.java.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    private DBConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/METRO-POS-System",
                        "root", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
