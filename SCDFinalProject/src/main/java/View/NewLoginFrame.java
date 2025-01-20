package View;

import Controller.LoginController;
import Controller.SuperAdminLoginController;
import Model.Employee;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class NewLoginFrame extends JFrame {

    private String role; // Role selected by the user
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private Employee loggedInEmployee;
    private JButton btnLogin;
    private SuperAdminLoginController saloginController;


    public NewLoginFrame() {
        setTitle("Login");
        setSize(1320, 710);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            ImageIcon icon = new ImageIcon(
                    Objects.requireNonNull(getClass().getClassLoader().getResource("images/icons/logo.PNG"))
            );
            setIconImage(icon.getImage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load icon image.");
        }


        // Remove GridLayout, use null layout for absolute positioning
        JPanel mainPanel = new JPanel(null);
        add(mainPanel);

        // Left Panel with padding and border only on the right wall
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, getWidth() / 2, getHeight());
        leftPanel.setBackground(Color.WHITE);

        // Set padding (EmptyBorder) and right side border
        int padding = 20; // You can change this to your desired padding value
        int borderWidth = 15; // Border width for the right side
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(-10, -10, -10, 0), // Padding on all sides except the right
                BorderFactory.createLineBorder(new Color(5, 42, 117), borderWidth) // Line border on the right side
        ));

        mainPanel.add(leftPanel);

        JLabel heading = new JLabel("WELCOME TO METRO!", SwingConstants.CENTER);
        heading.setFont(new Font("Century Gothic", Font.BOLD, 34));
        heading.setForeground(Color.decode("#052A76"));
        heading.setBounds(110, 100, 400, 70);
        leftPanel.add(heading);

// Add another label for shopping
        JLabel shoppingLabel = new JLabel("Shop your favorite items!", SwingConstants.CENTER);
        shoppingLabel.setFont(new Font("Century Gothic", Font.PLAIN, 20)); // Adjust the font size as needed
        shoppingLabel.setForeground(Color.decode("#052A76")); // You can use the same color or change it
        shoppingLabel.setBounds(110, 180, 400, 40); // Adjust the position and size based on your layout
        leftPanel.add(shoppingLabel);


        // Embed JavaFX Video Player
        JFXPanel jfxPanel = new JFXPanel();
        jfxPanel.setBounds(100, 150, 500, 400); // Adjust dimensions as needed
        leftPanel.add(jfxPanel);

        SwingUtilities.invokeLater(() -> {
            try {
                URL videoURL = getClass().getClassLoader().getResource("images/Logingif.mp4"); // Relative path to MP4
                if (videoURL != null) {
                    Media media = new Media(videoURL.toExternalForm());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    MediaView mediaView = new MediaView(mediaPlayer);

                    javafx.scene.Group root = new javafx.scene.Group(mediaView);
                    Scene scene = new Scene(root, 600, 400);
                    mediaView.setFitWidth(600);
                    mediaView.setFitHeight(400);

                    jfxPanel.setScene(scene);

                    // Loop the video
                    mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(javafx.util.Duration.ZERO));
                    mediaPlayer.play(); // Start playing the video
                } else {
                    System.out.println("Video file not found!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Right panel: Black background with login elements
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.white);
        //255, 245, 238
        //255, 255, 240
        //248, 248, 255 ghost white
        //250, 240, 230

        int BW = 15; // You can change this to your desired width

        rightPanel.setBounds(getWidth() / 2, 0, getWidth() / 2, getHeight());
        mainPanel.add(rightPanel);


        // Heading
        JLabel lblHeading = new JLabel("LETS GET STARTED", SwingConstants.CENTER);
        lblHeading.setFont(new Font("Century Gothic", Font.BOLD, 34));
        lblHeading.setForeground(Color.decode("#052A76"));
        lblHeading.setBounds(150, 70, 300, 50);
        rightPanel.add(lblHeading);

        // Role label and dropdown
        JLabel lblRole = new JLabel("User Type:");
        lblRole.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblRole.setForeground(Color.BLACK);
        lblRole.setBounds(100, 150, 100, 30);  // Positioned above the dropdown
        rightPanel.add(lblRole);

        JComboBox<String> roleDropdown = new JComboBox<>(new String[]{"Branch Manager", "Cashier", "Data Operator", "Super Admin"});
        roleDropdown.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        roleDropdown.setBounds(100, 180, 400, 40);
        rightPanel.add(roleDropdown);

        // Username label and field
        JLabel lblUsername = new JLabel("Enter Username:");
        lblUsername.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setBounds(100, 240, 150, 30);  // Positioned above the username field
        rightPanel.add(lblUsername);

        txtUsername = new JTextField();
        styleTextField(txtUsername);
        txtUsername.setBounds(100, 270, 400, 40);  // Below the username label
        rightPanel.add(txtUsername);

        JLabel lblPassword = new JLabel("Enter Password:");
        lblPassword.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBounds(100, 340, 150, 30);  // Positioned above the password field
        rightPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        txtPassword.setBounds(100, 370, 400, 40);  // Below the password label
        rightPanel.add(txtPassword);

        btnLogin = createButton("Login");
        btnLogin.setBounds(100, 470, 180, 50);
        rightPanel.add(btnLogin);

        JButton btnExit = createButton("Exit");
        btnExit.setBounds(320, 470, 180, 50);
        rightPanel.add(btnExit);

        LoginController loginController = new LoginController();
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            role = (String) roleDropdown.getSelectedItem();
            saloginController = new SuperAdminLoginController();

            if ("Super Admin".equalsIgnoreCase(role)) {
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                    try {
                        boolean login = saloginController.handleLogin(username, password);
                        if(login) {
                            new SuperAdminUI();
                            dispose();
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this, "Username or password is incorrect. " ,
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error opening Super Admin: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
            }

                else {
                loggedInEmployee = loginController.validateLogin(username, password);
                boolean isValid = loggedInEmployee != null && loggedInEmployee.getPassword().equals(password);

                if (isValid) {
                    JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    openDashboard(role); // Open appropriate dashboard based on role
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        textField.setCaretColor(Color.BLACK);
    }

    private JButton createButton(String text) {
        JButton button = new RoundedButton(text, 8);
        button.setFont(new Font("Century Gothic", Font.BOLD, 20));
        button.setForeground(Color.black);

         button.setBackground(Color.WHITE);

        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
    }

    // Open the dashboard based on the role
    private void openDashboard(String role) {
        if (loggedInEmployee == null) {
            JOptionPane.showMessageDialog(this, "Error retrieving employee details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (role.toLowerCase()) {
            case "cashier":
                new CashierUI(loggedInEmployee).setVisible(true);
                break;
            case "branch manager":
                new BranchManagerUI(loggedInEmployee).setVisible(true);
                break;
            case "data operator":
                new DataOperatorUI(loggedInEmployee).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid role!", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NewLoginFrame frame = new NewLoginFrame();
            frame.setVisible(true);
        });
    }
}
