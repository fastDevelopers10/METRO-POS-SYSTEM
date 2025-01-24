package View;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private final int cornerRadius;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill the background with the rounded rectangle
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw the border with specified color and thickness
        g2.setColor(new Color(35, 42, 67)); // Border color
        g2.setStroke(new BasicStroke(2));   // Border thickness
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);

        super.paintComponent(g2);
    }
}


class RoundedButton extends JButton {
    public final int cornerRadius;
    public Color hoverColor;
    public Color pressedColor; // Color for the pressed state

    // Define the default color
    private static final Color DEFAULT_COLOR = new Color(35, 42, 67);
    private static final Color DEFAULT_TEXT_COLOR = Color.WHITE; // Default text color (white)
    private static final Color HOVER_TEXT_COLOR = Color.WHITE; // Text color on hover (white)
    private static final Color PRESSED_TEXT_COLOR = Color.WHITE; // Text color on press (white)

    public RoundedButton(String text, int radius) {
        super(text);
        this.cornerRadius = radius;

        // Set colors based on button text
        this.hoverColor = new Color(65, 72, 97); // Hover color for buttons
        this.pressedColor = new Color(85, 92, 117); // Pressed color for buttons
        setForeground(DEFAULT_TEXT_COLOR); // Default text color is white

        setBackground(DEFAULT_COLOR); // Set the default background color
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Pointer cursor on hover
        setPreferredSize(new Dimension(120, 40)); // Adjust button size as needed

        // Add MouseListener to handle mouse enter and leave events
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setForeground(HOVER_TEXT_COLOR); // Change text color on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setForeground(DEFAULT_TEXT_COLOR); // Revert to default color
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clip the graphics area to ensure the paint doesn't stretch outside the rounded shape
        g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Set color based on the button state (pressed, rollover, or default)
        if (getModel().isPressed()) {
            g2.setColor(pressedColor); // Set pressed color
            setForeground(PRESSED_TEXT_COLOR); // Text color on press
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor); // Use hover color
            setForeground(HOVER_TEXT_COLOR); // Text color on hover
        } else {
            g2.setColor(DEFAULT_COLOR); // Default background color
            setForeground(DEFAULT_TEXT_COLOR); // Default text color
        }

        // Fill the rounded rectangle
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(g2); // Draw the button text
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the rounded border
        g2.setColor(Color.WHITE); // Border color
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
    }
}