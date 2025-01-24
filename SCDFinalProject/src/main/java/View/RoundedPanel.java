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
    private String text;
    // Define the default color
    private static final Color DEFAULT_COLOR = new Color(35, 42, 67);
    private static final Color DEFAULT_TEXT_COLOR = Color.WHITE; // Default text color (white)
    private static final Color HOVER_TEXT_COLOR = Color.WHITE; // Text color on hover (white)
    private static final Color PRESSED_TEXT_COLOR = Color.WHITE; // Text color on press (white)

    public RoundedButton(String text, int radius) {
        super(text);
        this.cornerRadius = radius;
        this.text=text;
    if(text.equalsIgnoreCase("Print")||text.equalsIgnoreCase("Add To Cart")||text.equalsIgnoreCase("+"))
    {

    // Set colors based on button text
    this.hoverColor = new Color(28, 165, 115); // Hover color for buttons
    this.pressedColor = new Color(24, 140, 100); // Pressed color for buttons
    setForeground(Color.WHITE); // Default text color is white
        setFont(new Font("Century Gothic", Font.BOLD, 14)); // Use Font.BOLD to make text bold
    }
    else
    {
        // Set colors based on button text
    this.hoverColor = new Color(65, 72, 97); // Hover color for buttons
    this.pressedColor = new Color(85, 92, 117); // Pressed color for buttons
    setForeground(DEFAULT_TEXT_COLOR); // Default text color is white
    setBackground(DEFAULT_COLOR); // Set the default background color
        setFont(new Font("Century Gothic", Font.BOLD, 14)); // Use Font.BOLD to make text bold
    }
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Pointer cursor on hover
        setPreferredSize(new Dimension(120, 40)); // Adjust button size as needed

        // Add MouseListener to handle mouse enter and leave events
        addMouseListener(new java.awt.event.MouseAdapter() {



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
            if (!getText().equals("-")) {
                setForeground(PRESSED_TEXT_COLOR); // Text color on press (only for non-minus buttons)
            }
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor); // Use hover color
            if (!getText().equals("-")) {
                setForeground(HOVER_TEXT_COLOR); // Text color on hover (only for non-minus buttons)
            }
        } else {
            if (text.equalsIgnoreCase("Print") ||text.equalsIgnoreCase("Add To Cart")||text.equalsIgnoreCase("+")) {
                g2.setColor(new Color(34, 195, 135)); // Default background color

            } else {
                g2.setColor(DEFAULT_COLOR); // Default background color
                setForeground(DEFAULT_TEXT_COLOR); // Default text color
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
