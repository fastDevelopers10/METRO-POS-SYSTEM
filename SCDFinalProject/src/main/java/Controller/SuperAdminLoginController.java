package Controller;

import DAO.SuperAdminDAO;
import View.LoginOptions;
import View.SuperAdminLogin;
import View.SuperAdminUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SuperAdminLoginController {

    private static SuperAdminDAO admin;
    private SuperAdminLogin loginView;

    public SuperAdminLoginController() {
        this.loginView = new SuperAdminLogin();
        this.admin = new SuperAdminDAO();

        loginView.addLoginListener(e ->
        {
            try {
                handleLogin();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        loginView.addExitListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginOptions fr = new LoginOptions();
                fr.setVisible(true);
            }
        });

    }

    public static String getUsername() {
        return admin.getUsername();
    }
    public static String getPassword(){return admin.getPassword();}


    private void handleLogin() throws SQLException {
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
            JOptionPane.showMessageDialog(loginView, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}