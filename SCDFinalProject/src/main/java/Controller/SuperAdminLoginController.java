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
    private static String username, password;

    public SuperAdminLoginController() {
        this.admin = new SuperAdminDAO();
    }
    public boolean handleLogin(String username, String password) throws SQLException {
        SuperAdminLoginController.username = username;
        SuperAdminLoginController.password = password;
        if (admin.validateLogin(username, password)) {
            return true;
        }
        return false;
    }

    public static String getUsername() {
        return admin.getUsername();
    }

    public static String getPassword() {
        return admin.getPassword();
    }

}