import DAO.DBConnection;
import View.LoginOptions;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing database setup...");

        // Initialize the database and tables
        DBConnection.getConnection();
        // In your main method or application, start the background task
        new Thread(String.valueOf(new InternetCheckTask())).start();
        new LoginOptions().setVisible(true);
        System.out.println("Database setup completed!");
    }
}
