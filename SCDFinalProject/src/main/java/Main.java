import DAO.DBConnection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing database setup...");

        // Initialize the database and tables
        DBConnection.getConnection();

        System.out.println("Database setup completed!");
    }
}
