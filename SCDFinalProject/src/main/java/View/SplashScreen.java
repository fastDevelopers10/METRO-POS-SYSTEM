package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.Objects;

public class SplashScreen extends JPanel {
    private int progress = 0;
    private Image backgroundImage;
    static JFrame frame;

    public SplashScreen() {

        try {
            // Use class loader to load the resource
            backgroundImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("images/Splash.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load background image.");
        }
        Timer timer = new Timer(30, e -> {
            progress += 1;
            if (progress > 100) {
                ((Timer) e.getSource()).stop();
                LoginOptions fr = new LoginOptions();
                fr.setVisible(true);
                frame.dispose();
            }
            repaint();
        });
        timer.start();
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

        frame.setShape(new RoundRectangle2D.Double(0, 0, 600, 400, 36, 36));

        frame.setBackground(new Color(0, 0, 0, 0));

        SplashScreen panel = new SplashScreen();
        panel.setPreferredSize(new Dimension(602, 401));

        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
