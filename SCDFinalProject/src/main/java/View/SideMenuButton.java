package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SideMenuButton extends JButton {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final Color ACTIVE_COLOR = new Color(200, 229, 220);
    private static final Color HOVER_COLOR = new Color(220, 220, 220); // Color when hovered
    private static final Font DEFAULT_FONT = new Font("Century Gothic", Font.PLAIN, 14);
    private static final Color TEXT_COLOR = Color.BLACK;

    // Track whether the button is active
    private boolean isActive = false;

    public SideMenuButton(String text, String iconPath) {
        super(text); // Set button text

        // Set icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            setIcon(icon);
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

        // Set cursor to hand on hover
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add mouse listener for hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_COLOR); // Change color when hovered
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(isActive ? ACTIVE_COLOR : DEFAULT_COLOR); // Reset color when mouse leaves
            }
        });
    }

    // Set the button as active
    public void setActive(boolean isActive) {
        this.isActive = isActive;  // Update the active state
        setBackground(isActive ? ACTIVE_COLOR : DEFAULT_COLOR);
    }

    // Get the current active state of the button
    public boolean isActive() {
        return isActive;
    }
}
