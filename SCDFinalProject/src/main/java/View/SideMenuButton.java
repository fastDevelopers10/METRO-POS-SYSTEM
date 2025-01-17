package View;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SideMenuButton extends JButton {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final Color ACTIVE_COLOR = new Color(200, 229, 220);
    private static final Color TEXT_COLOR = Color.BLACK;

    public SideMenuButton(String text, String iconPath) {
        this(text, iconPath, 14); // Default to no border
    }

    public SideMenuButton(String text, String iconPath, int fontSize) {
        super(text);
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            setIcon(icon);
        } catch (Exception e) {
            System.out.println("Icon not found for " + text + ": " + e.getMessage());
        }

        setHorizontalAlignment(SwingConstants.LEFT);
        setFont(new Font("Century Gothic", Font.PLAIN, fontSize)); // Set custom font size
        setForeground(TEXT_COLOR);
        setBackground(DEFAULT_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand when hovering
        setPreferredSize(new Dimension(240, 40)); // Adjust width and height
    }
    public void setActive(boolean isActive) {
        setBackground(isActive ? ACTIVE_COLOR : DEFAULT_COLOR);
    }
}