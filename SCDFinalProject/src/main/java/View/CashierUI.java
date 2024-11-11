package View;

import Controller.CashierController;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CashierUI
{
    private CashierController controller;
    private JFrame frame;
    private JTextField productNameField;
    private JTextField quantityField;

    public CashierUI()
    {
        controller = new CashierController();
        initialize();
    }

    private void initialize()
    {
        frame = new JFrame("Cashier System");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setBounds(10, 10, 100, 25);
        frame.add(nameLabel);

        productNameField = new JTextField();
        productNameField.setBounds(120, 10, 150, 25);
        frame.add(productNameField);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(10, 50, 100, 25);
        frame.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(120, 50, 150, 25);
        frame.add(quantityField);

        JButton addButton = new JButton("Add Product");
        addButton.setBounds(10, 90, 120, 30);
        frame.add(addButton);

        JButton generateBillButton = new JButton("Generate Bill");
        generateBillButton.setBounds(150, 90, 120, 30);
        frame.add(generateBillButton);

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
