// Adjust the design of the Update Password screen to match the Login screen's design.
package View;

import Model.Employee;
import Controller.SuperAdminLoginController;
import DAO.EmployeeDAO;
import DAO.SuperAdminDAO;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class NewUpdatePassword extends JFrame {

    private JPasswordField txtPreviousPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private Employee loggedInEmployee;

    public NewUpdatePassword(Employee loggedInEmployee) {
        this.loggedInEmployee = loggedInEmployee;

        setTitle("Update Password");
        setIconImage(loadIcon("images/icons/logo.PNG"));
        setSize(1320, 710);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.decode("#F4F4F4")); // Match Login screen background
        add(mainPanel);

        // Left Panel with picture
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, getWidth() / 2, getHeight());
        leftPanel.setBackground(Color.WHITE);

        // Set padding (EmptyBorder) and right side border
        int padding = 20; // Padding value
        int borderWidth = 15; // Border width for the right side
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(-10, -10, -10, 0), // Padding on all sides except the right
                BorderFactory.createLineBorder(new Color(5, 42, 117), borderWidth) // Line border on the right side
        ));

        mainPanel.add(leftPanel);

        JLabel heading = new JLabel("METRO", SwingConstants.CENTER);
        heading.setFont(new Font("Century Gothic", Font.BOLD, 50));
        heading.setForeground(Color.decode("#052A76"));
        heading.setBounds(110, 150, 400, 70);
        leftPanel.add(heading);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(60, 180, 500, 400);
        leftPanel.add(imageLabel);

        try {
            URL imageURL = getClass().getClassLoader().getResource("images/Updateimg.jpg");
            if (imageURL != null) {
                ImageIcon imageIcon = new ImageIcon(imageURL);
                Image scaledImage = imageIcon.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                System.out.println("Error: Image file not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(new Color(35, 42, 67));
        rightPanel.setBounds(getWidth() / 2, 0, getWidth() / 2, getHeight());
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(35, 42, 67), 5)); // Match Login screen design
        mainPanel.add(rightPanel);

        JLabel lblHeading = new JLabel("UPDATE PASSWORD", SwingConstants.CENTER);
        lblHeading.setFont(new Font("Century Gothic", Font.BOLD, 30));
        lblHeading.setForeground(Color.WHITE);
        lblHeading.setBounds(150, 70, 300, 50);
        rightPanel.add(lblHeading);

        JLabel lblPreviousPassword = new JLabel("Enter Previous Password:");
        lblPreviousPassword.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblPreviousPassword.setForeground(Color.WHITE);
        lblPreviousPassword.setBounds(100, 210, 300, 30);
        rightPanel.add(lblPreviousPassword);

        txtPreviousPassword = new JPasswordField();
        styleTextField(txtPreviousPassword);
        txtPreviousPassword.setBounds(100, 250, 400, 40);
        rightPanel.add(txtPreviousPassword);

        JLabel lblNewPassword = new JLabel("Enter New Password:");
        lblNewPassword.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblNewPassword.setForeground(Color.WHITE);
        lblNewPassword.setBounds(100, 310, 300, 30);
        rightPanel.add(lblNewPassword);

        txtNewPassword = new JPasswordField();
        styleTextField(txtNewPassword);
        txtNewPassword.setBounds(100, 350, 400, 40);
        rightPanel.add(txtNewPassword);

        JLabel lblConfirmPassword = new JLabel("Confirm New Password:");
        lblConfirmPassword.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblConfirmPassword.setForeground(Color.WHITE);
        lblConfirmPassword.setBounds(100, 410, 300, 30);
        rightPanel.add(lblConfirmPassword);

        txtConfirmPassword = new JPasswordField();
        styleTextField(txtConfirmPassword);
        txtConfirmPassword.setBounds(100, 450, 400, 40);
        rightPanel.add(txtConfirmPassword);

        JButton btnUpdate = createButton("Update");
        btnUpdate.setBounds(100, 550, 180, 50);
        rightPanel.add(btnUpdate);

        JButton btnExit = createButton("Exit");
        btnExit.setBounds(320, 550, 180, 50);
        rightPanel.add(btnExit);

        btnUpdate.addActionListener(e -> updatePassword());
        btnExit.addActionListener(e -> dispose());
    }

    private void updatePassword() {
        String previousPassword = new String(txtPreviousPassword.getPassword()).trim();
        String newPassword = new String(txtNewPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

        if (previousPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the user is SuperAdmin
        boolean isSuperAdmin = (loggedInEmployee == null || loggedInEmployee.getUsername() == null || loggedInEmployee.getPosition() == null);

        if (isSuperAdmin) {
            String superAdminUsername = SuperAdminLoginController.getUsername(); // Fetch username from SuperAdminLoginController
            SuperAdminDAO superAdminDAO = new SuperAdminDAO();

            if (!superAdminDAO.validateLogin(superAdminUsername, previousPassword)) {
                JOptionPane.showMessageDialog(this, "Previous password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isUpdated = superAdminDAO.updatePassword(superAdminUsername, newPassword);
            if (isUpdated) {
                JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Error updating password!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            // Regular employee password update logic
            if (!previousPassword.equals(loggedInEmployee.getPassword())) {
                JOptionPane.showMessageDialog(this, "Previous password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EmployeeDAO employeeDAO = new EmployeeDAO();
            try {
                boolean isUpdated = employeeDAO.updatePassword(loggedInEmployee.getUsername(), newPassword);
                if (isUpdated) {
                    JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An unexpected error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.decode("#F4F4F4")); // Match Login screen text field color
        textField.setBorder(BorderFactory.createLineBorder(new Color(5, 42, 117), 2)); // Match Login screen text field border
        textField.setCaretColor(Color.BLACK);
    }

    private Image loadIcon(String path) {
        URL iconURL = getClass().getClassLoader().getResource(path);
        if (iconURL != null) {
            return new ImageIcon(iconURL).getImage();
        } else {
            System.err.println("Error: Unable to load frame icon image.");
            return null;
        }
    }

    c

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Employee mockEmployee = new Employee();
            mockEmployee.setUsername("testUser");
            mockEmployee.setPassword("testPassword");

            NewUpdatePassword frame = new NewUpdatePassword(mockEmployee);
            frame.setVisible(true);
        });
    }

    public void setModal(boolean b) {
        b=true;

    }
}
