package Controller;

import DAO.SuperAdminDAO;
import View.LoginOptions;
import View.SuperAdminLogin;
import View.SuperAdminUI;

import javax.swing.*;

public class SuperAdminLoginController {

    private static SuperAdminDAO admin;
    private SuperAdminLogin loginView;

    public SuperAdminLoginController() {
        this.loginView = new SuperAdminLogin();
        this.admin = new SuperAdminDAO();

        // Add action listeners for login and exit buttons
        loginView.addLoginListener(e ->
        {
            handleLogin();

        });
        loginView.addExitListener(e -> new LoginOptions() );
    }

    public static String getUsername() {
        return admin.getUsername();
    }


    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Username and password cannot be empty!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (admin.validateLogin(username, password)) {

            new SuperAdminUI();
            loginView.dispose();
        } else {
            JOptionPane.showMessageDialog(loginView, "Invalid username or password!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}




