package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DataOperatorLogin extends JFrame {
    private BufferedImage backgroundImage;

    public DataOperatorLogin() {
        setTitle("Data Operator Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 710); // Set a default size for the window
        setResizable(false);
        setLocationRelativeTo(null); // Center the window on screen

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\LoginScreen.png"));
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }

        // Add a custom JPanel for drawing the background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null); // Draw the background image
                }
            }
        };

        panel.setLayout(null); // Set layout to null to position components manually

        // Create the first text field (username)
        JTextField tfUsername = new JTextField();
        tfUsername.setOpaque(true); // Make background visible (transparent is false)
        tfUsername.setBorder(new LineBorder(Color.BLACK, 1)); // Add a black border
        tfUsername.setBounds(490, 245, 350, 30); // Position and size (x, y, width, height)
        panel.add(tfUsername); // Add to the panel

        // Create the second text field (password)
        JTextField tfPassword = new JTextField();
        tfPassword.setOpaque(true); // Make background visible (transparent is false)
        tfPassword.setBorder(new LineBorder(Color.BLACK, 1)); // Add a black border
        tfPassword.setBounds(490, 325, 350, 30); // Position and size (x, y, width, height)
        panel.add(tfPassword); // Add to the panel

        // Create the "Login" button
        JButton btnLogin = new JButton("Login");
        btnLogin.setOpaque(false); // Make background invisible
        btnLogin.setContentAreaFilled(false); // Disable default button background rendering
        btnLogin.setBorderPainted(false); // Make the border invisible
        btnLogin.setForeground(Color.WHITE); // Set text color to yellow
        btnLogin.setFont(new Font("Century Gothic", Font.PLAIN, 16)); // Optional: Set custom font
        btnLogin.setBounds(540, 400, 120, 40); // Position and size (x, y, width, height)
        panel.add(btnLogin); // Add to the panel

        // Create the "Exit" button
        JButton btnExit = new JButton("Exit");
        btnExit.setOpaque(false); // Make background invisible
        btnExit.setContentAreaFilled(false); // Disable default button background rendering
        btnExit.setBorderPainted(false); // Make the border invisible
        btnExit.setForeground(Color.BLACK); // Set text color to yellow
        btnExit.setFont(new Font("Century Gothic", Font.PLAIN, 16)); // Optional: Set custom font
        btnExit.setBounds(660, 400, 120, 40); // Position and size (x, y, width, height)
        panel.add(btnExit); // Add to the panel

        // Add the custom panel to the frame
        add(panel);

        // Make sure the frame is visible
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataOperatorLogin()); // Initialize the frame on the event dispatch thread
    }
}
