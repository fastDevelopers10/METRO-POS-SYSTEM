package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SideMenuButton extends JButton {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final Color ACTIVE_COLOR = new Color(200, 229, 220);
    private static final Color HOVER_COLOR = new Color(220, 220, 220); // Color when hovered
    private static final Color PRESSED_COLOR = new Color(210, 180, 180); // Color when button is pressed
    private static final Font DEFAULT_FONT = new Font("Century Gothic", Font.PLAIN, 14);
    private static final Color TEXT_COLOR = Color.BLACK;

    private boolean isActive = false;
    private boolean isPressed = false;  // Track if the button is pressed
    private Timer pressTimer;  // Timer to track long press

    public SideMenuButton(String text, String iconPath) {
        super(text);  // Set button text

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
        setPreferredSize(new Dimension(240, 40));

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add mouse listener for hover effect and press functionality
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isPressed) {
                    setBackground(HOVER_COLOR);  // Hover color when not pressed
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isPressed) {
                    setBackground(isActive ? ACTIVE_COLOR : DEFAULT_COLOR);  // Reset color when mouse leaves
                }
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;  // Button is released
                setBackground(isActive ? ACTIVE_COLOR : DEFAULT_COLOR);  // Reset the color
                if (pressTimer != null) {
                    pressTimer.stop();  // Stop the timer if the press is released early
                }
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
