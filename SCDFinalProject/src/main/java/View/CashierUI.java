package SCDFinalProject.src.main.java.View;

import SCDFinalProject.src.main.java.Controller.CashierController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class CashierUI {
    private CashierController controller;
    private JFrame frame;
    private JTextField productNameField;
    private JTextField quantityField;

    public CashierUI() {
        controller = new CashierController();
        initialize();
    }

    private void initialize()
    {
        frame = new JFrame("Cashier System");
        frame.setSize(900, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);  // null layout for manual positioning

        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBounds(0, 0, frame.getWidth(), 50);  // Top panel occupies the full width, height is 50
        topPanel.setBackground(Color.GRAY);


        JLabel titleLabel = new JLabel("Cashier System");
        titleLabel.setBounds(200, 10, 200, 30);  // Position the label in the top panel
        topPanel.add(titleLabel);

        // Add the top panel to the frame
        frame.add(topPanel);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setBounds(0, 50, frame.getWidth() / 3, frame.getHeight() - 50);  // 20% width, full height minus top panel

        // Create buttons to be placed 20% below the top of the left panel
        JButton button1 = new JButton("Button 1");
        JButton button2 = new JButton("Button 2");
        JButton button3 = new JButton("Button 3");

        // Position the buttons 20% below the top of the panel
        int verticalOffset = (int) (frame.getHeight() * 0.2);

        button1.setBounds(10, verticalOffset, 120, 30);
        button2.setBounds(10, verticalOffset + 40, 120, 30); //  below the first button
        button3.setBounds(10, verticalOffset + 80, 120, 30); //  below the second button


        leftPanel.add(button1);
        leftPanel.add(button2);
        leftPanel.add(button3);

        frame.add(leftPanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setBounds(frame.getWidth() / 3, 50, frame.getWidth() * 2 / 3, frame.getHeight() - 50); // 80% width, full height minus top panel

        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setBounds(120, 10, 100, 25);
        centerPanel.add(nameLabel);

        productNameField = new JTextField();
        productNameField.setBounds(230, 10, 150, 25);
        centerPanel.add(productNameField);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(120, 50, 100, 25);
        centerPanel.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(230, 50, 150, 25);
        centerPanel.add(quantityField);

        JButton addButton = new JButton("Add Product");
        addButton.setBounds(120, 90, 120, 30);
        centerPanel.add(addButton);

        JButton generateBillButton = new JButton("Generate Bill");
        generateBillButton.setBounds(250, 90, 120, 30);
        centerPanel.add(generateBillButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        generateBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.generateBill();
            }
        });


        frame.add(centerPanel);

        frame.setVisible(true);
    }

    private void addProduct() {
        String productName = productNameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        controller.addProduct(productName, quantity);
    }

    public static void main(String[] args) {
        new CashierUI();
    }
}
