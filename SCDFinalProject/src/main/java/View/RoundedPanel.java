package View;

import javax.swing.*;
import java.awt.*;

// Custom RoundedPanel class
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
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        super.paintComponent(g2);
    }
}


class RoundedButton extends JButton {
    private final int cornerRadius;
    private Color hoverColor;
    private Color pressedColor; // Color for the pressed state
    private boolean isLoggedOut = false; // Flag to track logout/close state

    public RoundedButton(String text, int radius) {
        super(text);
        this.cornerRadius = radius;

        // Set colors based on button text
        if (text.equalsIgnoreCase("Login") || text.equalsIgnoreCase("Exit")) {
            this.hoverColor = new Color(65, 72, 97); // Hover color for Login/Exit
            this.pressedColor = new Color(85, 92, 117); // Pressed color for Login/Exit
            setForeground(Color.WHITE); // Login and Exit should always have white text
        } else {
            this.hoverColor = new Color(65, 72, 97); // Default hover color
            this.pressedColor = new Color(85, 92, 117); // Default pressed color
            setForeground(Color.BLACK); // Default text color (black) for other buttons
        }

        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Pointer cursor on hover
        setPreferredSize(new Dimension(120, 40)); // Adjust button size as needed
    }

    // Set custom hover color
    public void setHoverColor(Color color) {
        this.hoverColor = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clip the graphics area to ensure the paint doesn't stretch outside the rounded shape
        g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Set color based on the button state
        if (getModel().isPressed()) {
            g2.setColor(pressedColor); // Set the pressed color
            if (!getText().equalsIgnoreCase("Login") && !getText().equalsIgnoreCase("Exit")) {
                setForeground(Color.WHITE); // Text color white when pressed for other buttons
            }
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor); // Use the hover color when the mouse hovers over the button
            if (!getText().equalsIgnoreCase("Login") && !getText().equalsIgnoreCase("Exit")) {
                setForeground(Color.WHITE); // Text color white when hovered for other buttons
            }
        } else {
            g2.setColor(getBackground()); // Normal background color
            if (!getText().equalsIgnoreCase("Login") && !getText().equalsIgnoreCase("Exit")) {
                setForeground(Color.BLACK); // Text color black when neither pressed nor hovered for other buttons
            }
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
        g2.setColor(new Color(200, 229, 220)); // Border color
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
    }
}
