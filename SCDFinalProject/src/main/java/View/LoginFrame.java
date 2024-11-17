package SCDFinalProject.src.main.java.View;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("METRO");
        setSize(1350, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel backgroundLabel = new JLabel(new ImageIcon("SCDFinalProject\\src\\main\\resources\\images\\login.png"));
        backgroundLabel.setLayout(null);
        add(backgroundLabel);

        Font buttonFont = new Font("Century Gothic", Font.PLAIN, 22);
        JButton btnSuperAdmin = createButton("Super Admin", buttonFont, Color.WHITE);
        JButton btnBranchManager = createButton("Branch Manager", buttonFont, Color.WHITE);
        JButton btnDataOperator = createButton("Data Operator", buttonFont, Color.WHITE);
        JButton btnCashier = createButton("Cashier", buttonFont, Color.WHITE);
        JButton exitButton = createButton("Exit", buttonFont, Color.BLACK);

        btnSuperAdmin.setBounds(385, 172, 200, 50);
        btnBranchManager.setBounds(700, 172, 250, 50);
        btnDataOperator.setBounds(375, 357, 200, 50);
        btnCashier.setBounds(730, 355, 200, 50);
        exitButton.setBounds(540, 460, 200, 50);

        backgroundLabel.add(btnSuperAdmin);
        backgroundLabel.add(btnBranchManager);
        backgroundLabel.add(btnDataOperator);
        backgroundLabel.add(btnCashier);
        backgroundLabel.add(exitButton);

        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createButton(String text, Font font, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(foreground);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }


}

