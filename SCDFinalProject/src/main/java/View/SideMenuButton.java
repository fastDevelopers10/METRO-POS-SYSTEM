package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SideMenuButton extends JButton {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final Color ACTIVE_COLOR = new Color(200, 229, 220); // This stays as your active color
    private static final Color HOVER_COLOR = new Color(170, 199, 189); // Darker hover color
    private static final Color PRESSED_COLOR = new Color(140, 170, 160); // Even darker pressed color
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

        // Add mouse listeners to handle hover and pressed states
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!getBackground().equals(ACTIVE_COLOR)) {
                    setBackground(HOVER_COLOR); // Change to hover color when mouse enters, except if active
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!getBackground().equals(ACTIVE_COLOR)) {
                    setBackground(DEFAULT_COLOR); // Revert to default color when mouse exits, except if active
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(PRESSED_COLOR); // Change to pressed color when button is pressed
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!getBackground().equals(ACTIVE_COLOR)) {
                    setBackground(HOVER_COLOR); // Change back to hover color after press, except if active
                }
            }
        });
    }

    public void setActive(boolean isActive) {
        setBackground(isActive ? ACTIVE_COLOR : DEFAULT_COLOR);
    }
}
