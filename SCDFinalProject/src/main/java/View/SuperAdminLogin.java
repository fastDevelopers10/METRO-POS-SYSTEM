package View;

import Controller.SuperAdminLoginController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class SuperAdminLogin extends JFrame {

    private BufferedImage backgroundImage;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JButton btnExit;

    public SuperAdminLogin() {
        setTitle("Super Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 710);
        setResizable(false);
        setLocationRelativeTo(null);

        URL iconURL = getClass().getClassLoader().getResource("images/icons/logo.PNG");

        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        } else {
            System.err.println("Error: Unable to load frame icon image.");
        }


// Load the background image
        try {
            URL backgroundURL = getClass().getClassLoader().getResource("images/LoginScreen.png");
            if (backgroundURL != null) {
                backgroundImage = ImageIO.read(backgroundURL);
            } else {
                System.err.println("Error: Unable to load background image.");
            }
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };

        panel.setLayout(null);

        // Username field
        tfUsername = new JTextField();
        tfUsername.setOpaque(true);
        tfUsername.setBorder(new LineBorder(Color.BLACK, 1));
        tfUsername.setBounds(470, 240, 350, 30);
        panel.add(tfUsername);

        // Password field
        pfPassword = new JPasswordField();
        pfPassword.setOpaque(true);
        pfPassword.setBorder(new LineBorder(Color.BLACK, 1));
        pfPassword.setBounds(470, 320, 350, 30);
        panel.add(pfPassword);

        // Login button
        btnLogin = new JButton("Login");
        btnLogin.setOpaque(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        btnLogin.setBounds(530, 360, 120, 40);
        panel.add(btnLogin);

        // Exit button
        btnExit = new JButton("Exit");
        btnExit.setOpaque(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setBorderPainted(false);
        btnExit.setForeground(Color.BLACK);
        btnExit.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        btnExit.setBounds(645, 360, 120, 40);
        panel.add(btnExit);

        add(panel);
        setVisible(true);
    }

    // Getters for username and password
    public String getUsername() {
        return tfUsername.getText().trim();
    }

    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);

    }

    public void addExitListener(ActionListener listener) {
        btnExit.addActionListener(listener);
    }
}