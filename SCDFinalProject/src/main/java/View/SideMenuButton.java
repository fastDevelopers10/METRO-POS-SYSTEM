package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SideMenuButton extends JButton {
    private static final Color DEFAULT_COLOR = new Color(35, 42, 67);
    private static final Color ACTIVE_COLOR = new Color(55, 62, 97); // Active color
    private static final Color HOVER_COLOR = new Color(65, 72, 97); // Hover color
    private static final Color PRESSED_COLOR = new Color(95, 105, 130); // Pressed color
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color PRESSED_TEXT_COLOR = Color.BLACK; // Text color when pressed
    private static final int CORNER_RADIUS = 15; // Radius for rounded corners

    public SideMenuButton(String text, String iconPath) {
        this(text, iconPath, 16); // Default font size
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
        setContentAreaFilled(false); // Disable default button painting
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand
        setPreferredSize(new Dimension(300, 40)); // Adjust width and height

        // Add mouse listeners to handle hover and pressed states
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!getBackground().equals(ACTIVE_COLOR)) {
                    setBackground(HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!getBackground().equals(ACTIVE_COLOR)) {
                    setBackground(DEFAULT_COLOR);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(PRESSED_COLOR);
                setForeground(PRESSED_TEXT_COLOR);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!getBackground().equals(ACTIVE_COLOR)) {
                    setBackground(HOVER_COLOR);
                }
                setForeground(TEXT_COLOR);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the rounded rectangle background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

        // Draw the button's text and icon
        super.paintComponent(g2);

        g2.dispose();
    }

    @Override
    public void setContentAreaFilled(boolean filled) {
        // Prevent default behavior to allow custom painting
    }

    public void setActive(boolean isActive) {
        setBackground(isActive ? ACTIVE_COLOR : DEFAULT_COLOR);
    }
}
