package View;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoadingBar extends JPanel {
    private int progress = 0;
    private Image backgroundImage;
    static JFrame frame;

    public LoadingBar() {
        backgroundImage = new ImageIcon("Splash.png").getImage();

        // Timer to update progress
        Timer timer = new Timer(30, e -> {
            progress += 1; // Increment progress
            if (progress > 100) {
                ((Timer) e.getSource()).stop(); // Stop timer at 100%
                LoginFrame fr = new LoginFrame();
                fr.setVisible(true);
                frame.dispose();
            }
            repaint(); // Redraw the panel
        });
        timer.start(); // Start the timer
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int barWidth = 490;
        int barHeight = 8;
        int arc = 8; // Rounded corners

        g2d.setColor(Color.decode("#D9D9D9"));
        g2d.fillRoundRect(55, 310, barWidth, barHeight, arc, arc);

        g2d.setColor(Color.decode("#A02348"));
        g2d.fillRoundRect(55, 310, progress * barWidth / 100, barHeight, arc, arc);
    }

    public static void main(String[] args) {
        frame = new JFrame();
        frame.setUndecorated(true);

        frame.setShape(new RoundRectangle2D.Double(0, 0, 600, 400, 36, 36)); // Adjust the corner radius as needed

        frame.setBackground(new Color(0, 0, 0, 0));

        LoadingBar panel = new LoadingBar();
        panel.setPreferredSize(new Dimension(600, 400)); // Set size to match the background

        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
