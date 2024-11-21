package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.io.IOException;

public class DataOperatorUI extends JFrame {
    private BufferedImage backgroundImage;
    private JButton activeButton = null; // To track the active button

    public DataOperatorUI() {
        setTitle("Data Operator Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 710); // Set a default size for the window
        setResizable(false);

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\DOPN.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Fallback color if image is not found
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(null); // Use null layout for manual positioning
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight()); // Ensure background covers the frame

        // Create the side menu panel
        JPanel sideMenuPanel = new JPanel();
        sideMenuPanel.setLayout(new GridBagLayout());
        sideMenuPanel.setBackground(Color.WHITE);
        int menuYPosition = 250; // Default Y position of side menu
        int menuWidth = 240;     // Width of the side menu
        sideMenuPanel.setBounds(10, menuYPosition, menuWidth, getHeight() - menuYPosition);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.NORTH;

        // Button texts and icon paths
        String[] buttonTexts = {"Dashboard", "Change Password", "Add Product", "Add Vendor", "Add Category", "LogOut"};
        String[] iconPaths = {
                "D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\dash_icon.png",
                "D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\pass_icon.png",
                "D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\product.png",
                "D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\inventory.png",
                "D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\category.png",
                "D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\logout.png"
        };

        // Create buttons and add them to the side menu panel
        for (int i = 0; i < buttonTexts.length; i++) {
            SideMenuButton button = new SideMenuButton(buttonTexts[i], iconPaths[i]);

            // Add action listener
            button.addActionListener(e -> {
                if (activeButton != null) {
                    ((SideMenuButton) activeButton).setActive(false);
                }
                button.setActive(true);
                activeButton = button;
                System.out.println("Button clicked: " + button.getText());
            });

            gbc.gridy = i; // Set the row for the button
            gbc.weighty = 0; // Don't let buttons take up any extra vertical space
            sideMenuPanel.add(button, gbc);

            // Set the first button as the active button by default
            if (i == 0) {
                button.setActive(true);
                activeButton = button;
            }
        }

        // Add vertical space after the buttons to push them upward (using a "filler" component)
        gbc.gridy = buttonTexts.length; // Set after all buttons
        gbc.weighty = 1.0; // Give the filler component vertical space
        gbc.fill = GridBagConstraints.VERTICAL; // Allow filler to take vertical space
        sideMenuPanel.add(new JLabel(" "), gbc); // Add an empty label as a filler



        // Create the layered pane for stacking components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        // Add components to the layered pane
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(sideMenuPanel, JLayeredPane.PALETTE_LAYER);


        JPanel transparentPanel = new JPanel();
        transparentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 30));  // or any other layout
        transparentPanel.setBackground(new Color(0, 0, 0, 0)); // Set background to transparent
        transparentPanel.setBounds(300, 220, 950, 140); // Set size and position for the panel
        // Set a border around the panel (LineBorder)

        // Existing Rounded Panels (Customers, NadraDB, Billing)
        RoundedPanel VendorPanel = new RoundedPanel(8);
        VendorPanel.setBounds(0, 70, 300, 70); // Set bounds for the entire panel
        VendorPanel.setBackground(Color.decode("#C8E5DC")); // Set background color for the panel
        VendorPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding inside the panel
        VendorPanel.setLayout(new BorderLayout());

        JLabel VendorLB = new JLabel("Vendors", JLabel.LEFT);
        VendorLB.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        VendorLB.setForeground(Color.decode("#00000")); // Set text color for label
        VendorLB.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        VendorLB.setHorizontalAlignment(JLabel.LEFT);

        RoundedButton VendorBTN = new RoundedButton("View", 8);
        VendorBTN.setPreferredSize(new Dimension(80, 30)); // Adjust size as needed
        VendorBTN.setBackground(Color.WHITE); // Set button background color to white
        VendorBTN.setForeground(Color.decode("#000000")); // Set button text color
        VendorBTN.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        VendorBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        // Add label and button to VendorPanel
        VendorPanel.add(VendorLB, BorderLayout.WEST); // Add label on the left
        VendorPanel.add(VendorBTN, BorderLayout.EAST); // Add button on the right

        // Rounded categoryPanel
        RoundedPanel categoryPanel = new RoundedPanel(8);
        categoryPanel.setBounds(400, 100, 300, 70); // Set bounds for the entire panel
        categoryPanel.setBackground(Color.decode("#CCD4E5")); // Set background color for the panel
        categoryPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding inside the panel
        categoryPanel.setLayout(new BorderLayout());

        JLabel categorylabel = new JLabel("Categories", JLabel.LEFT);
        categorylabel.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        categorylabel.setForeground(Color.decode("#000000")); // Set text color for label
        categorylabel.setHorizontalAlignment(JLabel.LEFT);
        categorylabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        RoundedButton categorybtn = new RoundedButton("view", 8);
        categorybtn.setPreferredSize(new Dimension(80, 30)); // Adjust size as needed
        categorybtn.setBackground(Color.WHITE); // Set button background color to white
        categorybtn.setForeground(Color.decode("#000000")); // Set button text color
        categorybtn.setFont(new Font("Century Gothic", Font.PLAIN, 12));


        categoryPanel.add(categorylabel, BorderLayout.WEST); // Add label on the left
        categoryPanel.add(categorybtn, BorderLayout.EAST);

        // Add an action listener to the update button
        // categorybtn.addActionListener();

        // Rounded BillingPanel
        RoundedPanel productspanel = new RoundedPanel(8);
        productspanel.setBounds(750, 100, 250, 70); // Set bounds for the entire panel
        productspanel.setBackground(Color.decode("#FAE7D5")); // Set background color for the panel
        productspanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding inside the panel
        productspanel.setLayout(new BorderLayout());

        JLabel productslb = new JLabel("Products", JLabel.LEFT);
        productslb.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        productslb.setForeground(Color.decode("#000000")); // Set text color for label
        productslb.setHorizontalAlignment(JLabel.LEFT);
        productslb.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        RoundedButton productsbtn = new RoundedButton("View", 8);
        productsbtn.setPreferredSize(new Dimension(80, 30)); // Adjust size as needed
        productsbtn.setBackground(Color.WHITE); // Set button background color to white
        productsbtn.setForeground(Color.decode("#000000")); // Set button text color
        productsbtn.setFont(new Font("Century Gothic", Font.PLAIN, 12));

        // Fix: add productslb and productsbtn to productspanel
        productspanel.add(productslb, BorderLayout.WEST); // Add label on the left
        productspanel.add(productsbtn, BorderLayout.EAST); // Add button on the right

        // Add an action listener to the productsbtn
//        productsbtn.addActionListener(e -> {
//
//        });

        transparentPanel.add(VendorPanel);
        transparentPanel.add(categoryPanel);
        transparentPanel.add(productspanel);

// Add the transparent panel to the layered pane
        layeredPane.add(transparentPanel, JLayeredPane.PALETTE_LAYER);
// Set the layered pane as the content pane of the frame
        setContentPane(layeredPane);
        // Make the frame visible
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DataOperatorUI::new);
    }
}
