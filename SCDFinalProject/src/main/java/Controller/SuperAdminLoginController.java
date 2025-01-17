package Controller;

import DAO.SuperAdminDAO;

import java.sql.SQLException;

public class SuperAdminLoginController {

    private static SuperAdminDAO admin;

    public SuperAdminLoginController() {
        this.admin = new SuperAdminDAO();
    }
    public boolean handleLogin(String username, String password) throws SQLException {
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