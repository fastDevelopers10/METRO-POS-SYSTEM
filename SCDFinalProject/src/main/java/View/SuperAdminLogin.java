package SCDFinalProject.src.main.java.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("SCDFinalProject\\src\\main\\resources\\images\\LoginScreen.png"));
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
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
        tfUsername.setBounds(490, 245, 350, 30);
        panel.add(tfUsername);

        // Password field
        pfPassword = new JPasswordField();
        pfPassword.setOpaque(true);
        pfPassword.setBorder(new LineBorder(Color.BLACK, 1));
        pfPassword.setBounds(490, 325, 350, 30);
        panel.add(pfPassword);

        // Login button
        btnLogin = new JButton("Login");
        btnLogin.setOpaque(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        btnLogin.setBounds(540, 400, 120, 40);
        panel.add(btnLogin);

        // Exit button
        btnExit = new JButton("Exit");
        btnExit.setOpaque(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setBorderPainted(false);
        btnExit.setForeground(Color.BLACK);
        btnExit.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        btnExit.setBounds(660, 400, 120, 40);
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

    // Methods to add action listeners
    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);

    }

    public void addExitListener(ActionListener listener) {
        btnExit.addActionListener(listener);
    }
}
