//package Test;
//
//import DAO.DBConnection;
//import DAO.SuperAdminDAO;
//import org.junit.jupiter.api.*;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class SuperAdminDAOTest {
//
//    private SuperAdminDAO superAdminDAO;
//
//    @BeforeAll
//    void setUpDatabase() throws SQLException {
//        // Assuming DBConnection uses an in-memory H2 database for testing
//        Connection conn = DBConnection.getConnection();
//        String createTableQuery = """
//                CREATE TABLE super_admin (
//                    id INT AUTO_INCREMENT PRIMARY KEY,
//                    username VARCHAR(255) NOT NULL,
//                    password VARCHAR(255) NOT NULL
//                );
//                """;
//
//        String insertDataQuery = """
//                INSERT INTO super_admin (username, password) VALUES
//                ('admin1', 'password1'),
//                ('admin2', 'password2');
//                """;
//
//        try (Statement stmt = conn.createStatement()) {
//            stmt.execute(createTableQuery);
//            stmt.execute(insertDataQuery);
//        }
//    }
//
//    @BeforeEach
//    void setUp() {
//        superAdminDAO = new SuperAdminDAO();
//    }
//
//    @Test
//    void testValidateLogin_Successful() {
//        boolean result = superAdminDAO.validateLogin("admin1", "password1");
//        assertTrue(result, "Login should be successful for valid credentials");
//        assertEquals("admin1", superAdminDAO.getUsername(), "Username should be stored after successful login");
//    }
//
//    @Test
//    void testValidateLogin_Failed() {
//        boolean result = superAdminDAO.validateLogin("admin1", "wrongpassword");
//        assertFalse(result, "Login should fail for invalid credentials");
//        assertNull(superAdminDAO.getUsername(), "Username should not be set for failed login");
//    }
//
//    @Test
//    void testValidateLogin_InvalidUser() {
//        boolean result = superAdminDAO.validateLogin("nonexistent", "password");
//        assertFalse(result, "Login should fail for non-existent user");
//        assertNull(superAdminDAO.getUsername(), "Username should not be set for failed login");
//    }
//
//    @AfterAll
//    void tearDownDatabase() throws SQLException {
//        Connection conn = DBConnection.getConnection();
//        try (Statement stmt = conn.createStatement()) {
//            stmt.execute("DROP TABLE super_admin");
//        }
//    }
//}
