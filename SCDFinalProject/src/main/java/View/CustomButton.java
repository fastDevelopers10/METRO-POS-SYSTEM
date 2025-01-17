package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;

public class CustomButton extends JButton {
    private Color defaultColor = new Color(70, 130, 180);
    private Color hoverColor = new Color(100, 149, 237);
    private Color pressedColor = new Color(50, 100, 150);
    private Color logoutColor = new Color(214, 24, 33);

    public CustomButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 14));
        setBackground(defaultColor);
        setForeground(Color.WHITE);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(15, 30, 15, 30));

        setPreferredSize(new Dimension(250, 70));

        if (text.equalsIgnoreCase("Logout")) {
            setBackground(logoutColor);
        }

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if ("Logout".equals(getText())) {
                    setBackground(new Color(252, 5, 10));
                } else {
                    setBackground(hoverColor);
                }
            }

            public void mouseExited(MouseEvent evt) {
                if ("Logout".equals(getText())) {
                    setBackground(logoutColor);
                } else {
                    setBackground(defaultColor);
                }
            }


            public void mouseReleased(MouseEvent evt) {
                if ("Logout".equals(getText())) {
                    setBackground(logoutColor);
                } else {
                    setBackground(defaultColor);
                }
            }
        });
    }

    public void addCustomActionListener(ActionListener listener) {
        this.addActionListener(listener);
    }
}
