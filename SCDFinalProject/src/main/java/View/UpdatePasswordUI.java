package View;

import Model.Employee;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import DAO.EmployeeDAO;

public class UpdatePasswordUI extends JDialog {
    private BufferedImage backgroundImage;
    private String position;
    private String oldPassword;

    // Constructor to initialize the UI with employee details
    public UpdatePasswordUI(Employee employee) {
        // Set window title based on employee position
        setTitle(employee.getPosition() + " Update Password");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);  // Use DISPOSE_ON_CLOSE for dialogs

        // Get screen size and set a reasonable size for the window
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = screenSize.width;  // Set the width to 50% of screen width
        int height = screenSize.height; // Set the height to 50% of screen height
        setSize(width, height);  // Set window size dynamically

        // Optionally set the window position to the top-left corner
        setLocation(0, 0);  // Position the window at the top-left corner of the screen

        // Prevent resizing of the window
        setResizable(false);

        // Load the background image
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("images/UpdatePassword.png")));  // Correct path
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load background image.");
        }

        // Add a custom JPanel for drawing the background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Scale the image to fit the size of the panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null); // Stretch the image to fit the panel
                }
            }
        };
        panel.setLayout(null); // Set layout to null to position components manually

        // Create the current password text field
        JTextField tfCurrentPassword = new JTextField();
        tfCurrentPassword.setOpaque(true);
        tfCurrentPassword.setBorder(new LineBorder(Color.BLACK, 1));
        tfCurrentPassword.setBounds(482, 243, 350, 30); // Position and size (x, y, width, height)
        panel.add(tfCurrentPassword);

        // Create the new password text field
        JTextField tfPassword = new JTextField();
        tfPassword.setOpaque(true);
        tfPassword.setBorder(new LineBorder(Color.BLACK, 1));
        tfPassword.setBounds(482, 307, 350, 30);
        panel.add(tfPassword);

        // Create the confirm password text field
        JTextField tfCFPassword = new JTextField();
        tfCFPassword.setOpaque(true);
        tfCFPassword.setBorder(new LineBorder(Color.BLACK, 1));
        tfCFPassword.setBounds(482, 378, 350, 30); // Position and size (x, y, width, height)
        panel.add(tfCFPassword);

        // Create the "Update" button
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setOpaque(false);
        btnUpdate.setContentAreaFilled(false);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        btnUpdate.setBounds(534, 440, 120, 40);
        panel.add(btnUpdate);

        // Create the "Exit" button
        JButton btnExit = new JButton("Exit");
        btnExit.setOpaque(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setBorderPainted(false);
        btnExit.setForeground(Color.BLACK);
        btnExit.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        btnExit.setBounds(660, 442, 120, 40);
        panel.add(btnExit);

        // Add the custom panel to the frame
        add(panel);

        // Action Listener for "Update" button
        btnUpdate.addActionListener(e -> {
            String currentPassword = employee.getPassword();
            String password = tfPassword.getText().trim();
            String confirmPassword = tfCFPassword.getText().trim();

            // Validate inputs
            if (currentPassword.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if the current password is correct
            if (currentPassword.equals(tfPassword)) {
                JOptionPane.showMessageDialog(this, "Enter a new Password!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if new passwords match
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the password in the database
            EmployeeDAO employeeDAO = new EmployeeDAO();
            boolean isUpdated = employeeDAO.updatePassword(employee.getUsername(), password);
            System.out.println(isUpdated);
            if (isUpdated) {
                JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();  // Close the dialog after successful update
            } else {
                JOptionPane.showMessageDialog(this, "Error updating password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Listener for "Exit" button
        btnExit.addActionListener(e -> {
            dispose();  // Close the dialog
        });

        // Make sure the dialog is modal
        setModal(true);
        setVisible(true);
    }
}
