package Controller;

import SCDFinalProject.src.main.java.Model.SuperAdmin;
import SCDFinalProject.src.main.java.View.SuperAdminLogin;
import SCDFinalProject.src.main.java.View.SuperAdminUI;

import javax.swing.*;

public class SuperAdminLoginController {

    private static SuperAdmin admin;
    private SuperAdminLogin loginView;

    public SuperAdminLoginController() {
        this.loginView = new SuperAdminLogin();
        this.admin = new SuperAdmin();

        // Add action listeners for login and exit buttons
        loginView.addLoginListener(e ->
        {
                      handleLogin();

        });
        loginView.addExitListener(e -> System.exit(0));
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
