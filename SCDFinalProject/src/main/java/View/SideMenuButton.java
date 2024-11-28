package View;

import javax.swing.*;
import java.awt.*;

public class SideMenuButton extends JButton {
    // Default properties for the buttons
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final Color ACTIVE_COLOR = new Color(200, 229, 220);
    private static final Font DEFAULT_FONT = new Font("Century Gothic", Font.PLAIN, 14);
    private static final Color TEXT_COLOR = Color.BLACK;

    public SideMenuButton(String text, String iconPath) {
        super(text); // Set button text

        // Set icon
        try {
            setIcon(new ImageIcon(iconPath)); // Load the icon
        } catch (Exception e) {
            System.out.println("Icon not found for " + text + ": " + e.getMessage());
        }

        // Style the button
        setHorizontalAlignment(SwingConstants.LEFT);
        setFont(DEFAULT_FONT);
        setForeground(TEXT_COLOR);
        setBackground(DEFAULT_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);

        // Set preferred size
        setPreferredSize(new Dimension(240, 40)); // Adjust width and height
    }

    // Method to set the active state
    public void setActive(boolean isActive) {
        setBackground(isActive ? ACTIVE_COLOR : DEFAULT_COLOR);
    }
}

