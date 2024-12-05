package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.io.IOException;

public class DataOperatorUI extends JFrame {
    private BufferedImage backgroundImage;

    private SideMenuButton activebtn = null;
    private JPanel sideMenuPanel;

    public DataOperatorUI() {
        setTitle("Data Operator Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 710); // Set a default size for the window
        // Set icon

        setIconImage(new ImageIcon("D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\logo.PNG").getImage());

        setResizable(false);


        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\DOP.png"));
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
        sideMenuPanel = new JPanel();
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
                "\\images\\icons\\dash_icon.png",
                "\\images\\icons\\pass_icon.png",
                "\\images\\icons\\product.png",
                "\\images\\icons\\inventory.png",
                "\\images\\icons\\category.png",
                "\\images\\icons\\logout.png"
        };

        for (int i = 0; i < buttonTexts.length; i++) {
            SideMenuButton button = new SideMenuButton(buttonTexts[i], iconPaths[i]);

            // Add action listener specific to each button
            switch (i) {
                case 0: // Dashboard button
                    button.addActionListener(e -> {
                        handleButtonClick(button);
                        System.out.println("Dashboard clicked");
                        // Add your logic for Dashboard button here
                    });
                    break;
                case 1: // Change Password button
                    button.addActionListener(e -> {
                        handleButtonClick(button);
                        System.out.println("Change Password clicked");
                        // Add your logic for Change Password button here
                    });
                    break;
                case 2: // Add Product button
                    button.addActionListener(e -> {
                        handleButtonClick(button);
                        System.out.println("Add Product clicked");
                        AddProduct addProductWindow = new AddProduct(1);
                        addProductWindow.setVisible(true);

                        // Add WindowListener to reset active button when the window is closed
                        addProductWindow.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                // Reset active button to Dashboard when the window is closed
                                addProductWindow.dispose();
                                resetActiveButtonToDashboard();
                            }
                        });
                    });
                    break;
                case 3: // Add Vendor button
                    button.addActionListener(e -> {
                        handleButtonClick(button);  // Set the clicked button as active
                        AddVendor addVendorWindow = new AddVendor();
                        addVendorWindow.setVisible(true);

                        // Add WindowListener to reset active button when the window is closed
                        addVendorWindow.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                // Reset active button to Dashboard when the window is closed
                                addVendorWindow.dispose();
                                resetActiveButtonToDashboard();

                            }
                        });
                    });
                    break;

                case 4: // Add Category button
                    button.addActionListener(e -> {
                        handleButtonClick(button);
                        System.out.println("Add Category clicked");
                        // Add your logic for Add Category button here
                    });
                    break;
                case 5: // LogOut button
                    button.addActionListener(e -> {
                        handleButtonClick(button);
                        System.out.println("LogOut clicked");
                        // Add your logic for LogOut button here
                    });
                    break;
            }

            gbc.gridy = i; // Set the row for the button
            gbc.weighty = 0; // Don't let buttons take up any extra vertical space
            sideMenuPanel.add(button, gbc);

            // Set the first button as the active button by default
            if (i == 0) {
                button.setActive(true);
                activebtn = button;
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
                JDialog dialog = new JDialog((Frame) null, "Vendors", true);
                dialog.setIconImage(new ImageIcon("D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\logo.PNG").getImage());
                // Manually set position and size for the dialog window
                dialog.setBounds(275, 0, 1020, 800); // Position it 200px from top left and size it 800x500px
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Create the panel
                VendorTablePanel panel = new VendorTablePanel();
                dialog.add(panel);

                // Set bounds manually for the table
                panel.setTableBounds(50, 50, 700, 350); // Set table bounds manually (x, y, width, height)

                // Create a JScrollPane and manually set bounds for it as well
                JScrollPane scrollPane = new JScrollPane(panel.vendorTable);
                scrollPane.setBounds(50, 50, 700, 350); // Set bounds for JScrollPane manually
                dialog.add(scrollPane);  // Add scroll pane to dialog

                dialog.setVisible(true); // Show the dialog

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

         ////Add an action listener to the productsbtn
        productsbtn.addActionListener(e -> {
            JFrame frame = new JFrame("Product Table - Branch 1");
            frame.setIconImage(new ImageIcon("D:\\Users\\Alien\\OneDrive\\Documents\\GitHubProject\\METRO-POS-SYSTEM\\SCDFinalProject\\src\\main\\resources\\images\\icons\\logo.PNG").getImage());
            // Manually set position and size for the dialog window
            frame.setBounds(275, 0, 1020, 800); // Position it 200px from top left and size it 800x500px
            frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // Create an instance of ProductTable with branchId = 1
            ProductTablePanel productTable = new ProductTablePanel(1);

            // Add the ProductTable JPanel to the frame
            frame.add(productTable);

            // Make the frame visible
            frame.setVisible(true);
        });

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
    private void handleButtonClick(SideMenuButton button) {
        // Deactivate the previously active button if it exists
        if (activebtn != null) {
            activebtn.setActive(false);
        }

        // Set the clicked button as the active button
        button.setActive(true);
        activebtn = button;
    }

    private void resetActiveButtonToDashboard() {
        if (activebtn != null) {
            activebtn.setActive(false); // Deactivate the current active button
        }

        // Assume the first button (Dashboard) is the default
        Component[] components = sideMenuPanel.getComponents();
        if (components.length > 0 && components[0] instanceof SideMenuButton) {
            SideMenuButton dashboardButton = (SideMenuButton) components[0];
            dashboardButton.setActive(true); // Activate the default button
            activebtn = dashboardButton; // Update the activeButton reference
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DataOperatorUI::new);
    }
}
