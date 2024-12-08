import DAO.ProductDAO;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class InternetCheckTask {

    private ProductDAO productDAO;

    public InternetCheckTask() {
        productDAO = new ProductDAO();
    }

    // Start the internet check task that runs in the background
    public void startInternetCheckTask() {
        Timer timer = new Timer();

        // Check every 60 seconds (60000 ms)
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndProcessPendingUpdates();
            }
        }, 0, 60000); // Initial delay of 0ms, repeated every 60 seconds
    }

    // Check if the internet is available
    private boolean isInternetAvailable() {
        // Use the ProductDAO's method to check for the database connection
        return productDAO.isDatabaseConnected();
    }

    // Check internet availability and process saved updates if internet is available
    private void checkAndProcessPendingUpdates() {
        if (isInternetAvailable()) {
            System.out.println("Internet is back. Processing saved updates...");
            productDAO.processPendingUpdates(); // Process saved updates from the file
        } else {
            System.out.println("No internet connection.");
        }
    }
}
