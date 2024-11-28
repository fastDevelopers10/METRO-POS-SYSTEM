package View;

import javax.swing.*;
import java.awt.*;

public class ScrollablePanel extends JScrollPane {

    // Constructor to create a scrollable panel with buttons
    public ScrollablePanel(int numberOfButtons) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);

        // Create buttons dynamically based on the specified number
        for (int i = 0; i < numberOfButtons; i++) {
            JButton button = new JButton("Button " + (i + 1));
            button.setFont(new Font("Century Gothic", Font.PLAIN, 12));
            button.setPreferredSize(new Dimension(150, 40));
            button.addActionListener(e -> System.out.println("Slider button clicked: " + ((JButton) e.getSource()).getText()));
            panel.add(button);
        }

        // Create the JScrollPane with the panel
        setViewportView(panel);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }
}
