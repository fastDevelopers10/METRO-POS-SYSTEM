package View;

import Controller.SuperAdminLoginController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class LoginOptions extends JFrame {

    private Image backgroundImage;

    public LoginOptions() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Get screen size and set JFrame to this
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the window if undecorated
        setResizable(false);
        try {
            // Use the class loader to load the image resource
            ImageIcon icon = new ImageIcon(
                    Objects.requireNonNull(getClass().getClassLoader().getResource("images/icons/logo.PNG"))
            );
            setIconImage(icon.getImage());
        } catch (NullPointerException exe) {
            exe.printStackTrace();
            System.err.println("Error: Unable to load frame icon image.");
        }

        // Load background image
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("images/login.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load background image.");
        }

        // Custom background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(null);
        add(backgroundPanel);

        Font buttonFont = new Font("Century Gothic", Font.PLAIN, 22);

        // Create buttons
        JButton btnSuperAdmin = createButton("Super Admin", buttonFont, Color.WHITE);
        JButton btnBranchManager = createButton("Branch Manager", buttonFont, Color.WHITE);
        JButton btnDataOperator = createButton("Data Operator", buttonFont, Color.WHITE);
        JButton btnCashier = createButton("Cashier", buttonFont, Color.WHITE);
        JButton exitButton = createButton("Exit", buttonFont, Color.BLACK);

        // Set button positions
        btnSuperAdmin.setBounds(402, 180, 200, 50);
        btnBranchManager.setBounds(708, 180, 250, 50);
        btnDataOperator.setBounds(402, 355, 200, 50);
        btnCashier.setBounds(730, 353, 200, 50);
        exitButton.setBounds(533, 452, 200, 50);

        // Add buttons to the background panel
        backgroundPanel.add(btnSuperAdmin);
        backgroundPanel.add(btnBranchManager);
        backgroundPanel.add(btnDataOperator);
        backgroundPanel.add(btnCashier);
        backgroundPanel.add(exitButton);

        // Action listeners
        btnSuperAdmin.addActionListener(e -> {
            try {
                new SuperAdminLoginController(); // Opens the login view and attaches functionality
                dispose(); // Close the current frame
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error opening Super Admin: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBranchManager.addActionListener(e -> OpenCredentials("Branch Manager"));
        btnDataOperator.addActionListener(e -> OpenCredentials("Data Operator"));
        btnCashier.addActionListener(e -> OpenCredentials("Cashier"));
        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createButton(String text, Font font, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(foreground);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    private void OpenCredentials(String role) {
        System.out.println(role+" Creds opened");
        new Credentials(role).setVisible(true);
        this.dispose(); // Close the current window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginOptions frame = new LoginOptions();
            frame.setVisible(true);
        });
    }
}
