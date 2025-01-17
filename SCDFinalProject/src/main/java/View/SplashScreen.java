package View;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.Objects;

public class SplashScreen extends JPanel {
    private int progress = 0;
    static JFrame frame;


    // Variables to customize the heading font size and color
    private static int headingFontSize = 36; // Default font size
    private static Color headingColor =Color.decode("#052A76"); // Default font color

    public SplashScreen() {
        // Timer for progress bar animation
        Timer timer = new Timer(30, e -> {
            progress += 1;
            if (progress > 100) {
                ((Timer) e.getSource()).stop();
                NewLoginFrame fr = new NewLoginFrame();
                fr.setVisible(true);
                frame.dispose();
            }
            repaint();
        });
        timer.start();
        try {
            ImageIcon icon = new ImageIcon(
                    Objects.requireNonNull(getClass().getClassLoader().getResource("images/icons/logo.PNG"))
            );
            frame.setIconImage(icon.getImage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load icon image.");
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the progress bar over the video
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int barWidth = 490;
        int barHeight = 8;
        int arc = 8; // Rounded corners

        g2d.setColor(Color.decode("#D9D9D9"));
        g2d.fillRoundRect(55, 330, barWidth, barHeight, arc, arc);

        g2d.setColor(Color.decode("#052A76"));
        g2d.fillRoundRect(55, 330, progress * barWidth / 100, barHeight, arc, arc);
    }

    public static void setHeadingAttributes(int fontSize, Color color) {
        headingFontSize = fontSize;
        headingColor = color;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame();
            frame.setUndecorated(true); // No window borders
            frame.setShape(new RoundRectangle2D.Double(0, 0, 600, 400, 36, 36)); // Rounded corners
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null); // Center the window

            JLayeredPane layeredPane = new JLayeredPane();
            frame.setContentPane(layeredPane);

            // Create and add JFXPanel for JavaFX media content (bottom layer)
            JFXPanel jfxPanel = new JFXPanel();
            jfxPanel.setBackground(Color.WHITE);
            jfxPanel.setBounds(100, 0, 600, 400); // Set bounds to fill the entire frame
            layeredPane.add(jfxPanel, JLayeredPane.DEFAULT_LAYER);

            // Create and add the splash screen panel (top layer)
            SplashScreen panel = new SplashScreen();
            panel.setBackground(Color.WHITE);
            panel.setBounds(0, 0, 600, 400); // Set bounds to fill the entire frame
            panel.setOpaque(false); // Make splash screen transparent so video is visible
            layeredPane.add(panel, JLayeredPane.PALETTE_LAYER); // Add on top of JFXPanel

            // Add heading "Metro" above the video
            JLabel headingLabel = new JLabel("Metro", JLabel.CENTER);
            headingLabel.setFont(new Font("Century Gothic", Font.BOLD, headingFontSize)); // Set font and size
            headingLabel.setForeground(headingColor); // Set the color for the heading
            headingLabel.setBounds(0, 20, 600, 50); // Position the heading at the top
            layeredPane.add(headingLabel, JLayeredPane.MODAL_LAYER); // Add it above other components

            // Initialize JavaFX components (media player) inside invokeLater to ensure correct thread usage
            SwingUtilities.invokeLater(() -> {
                try {
                    URL videoURL = SplashScreen.class.getClassLoader().getResource("images/loading.mp4"); // Relative path to MP4
                    if (videoURL != null) {
                        Media media = new Media(videoURL.toExternalForm());
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        MediaView mediaView = new MediaView(mediaPlayer);

                        Group root = new Group(mediaView);
                        Scene scene = new Scene(root, 600, 400);
                        mediaView.setFitWidth(600);
                        mediaView.setFitHeight(400);

                        jfxPanel.setScene(scene); // Set the scene for JFXPanel

                        // Loop the video
                        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
                        mediaPlayer.play(); // Start playing the video
                    } else {
                        System.out.println("Video file not found!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Set the background color of the frame
            frame.setBackground(Color.WHITE);

            // Show the frame
            frame.setVisible(true);
        });
    }
}
