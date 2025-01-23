package View;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class IOSScrollBarUtils {

    // Method to apply the custom scrollbar to a JScrollPane
    public static void applyIOSStyleScrollBar(JScrollPane scrollPane) {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();

        // Apply the custom scrollbar UI to both vertical and horizontal scrollbars
        verticalScrollBar.setUI(new IOSStyleScrollBarUI());
        horizontalScrollBar.setUI(new IOSStyleScrollBarUI());

        //speedss
        verticalScrollBar.setUnitIncrement(26);
        verticalScrollBar.setBlockIncrement(100);

        horizontalScrollBar.setUnitIncrement(26);
        horizontalScrollBar.setBlockIncrement(100);
    }

    static class IOSStyleScrollBarUI extends BasicScrollBarUI {

        //do thin
        @Override
        public Dimension getPreferredSize(JComponent c) {
            return new Dimension(14, 12); // Thinner scrollbar, adjust this as necessary
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            // Create a custom rounded button for the scroll buttons
            JButton decreaseButton = new RoundedButton("", 15);  // 15px radius for rounded corners
            decreaseButton.setBackground(Color.WHITE);  // White background
            decreaseButton.setBorder(BorderFactory.createEmptyBorder());
            return decreaseButton;
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            // Create a custom rounded button for the scroll buttons
            JButton increaseButton = new RoundedButton("", 15);  // 15px radius for rounded corners
            increaseButton.setBackground(Color.WHITE);  // White background
            increaseButton.setBorder(BorderFactory.createEmptyBorder());
            return increaseButton;
        }
//its for arc, thinner corner style
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(35, 42, 67)); // Semi-transparent gray
            g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 400, 400); // Thinner thumb
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Set the track color with thinner proportions
            g2d.setColor(new Color(230, 230, 230)); // Light gray background for the track
            g2d.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 10, 15); // Thinner track
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            // Ensure the scrollbar has a transparent, minimalistic background
            c.setBackground(new Color(255, 255, 255, 0));
        }
    }
}
