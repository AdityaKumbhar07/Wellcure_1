package ui.admin;

import ui.StartWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage {

    public static void admin() {
        JFrame adminFrame = new JFrame("Admin Panel");
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setSize(600, 400);
        adminFrame.setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top panel with title and logout button
        JPanel topPanel = new JPanel(new BorderLayout());

        // Title panel at the center
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("ADMIN PANEL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Logout button at the right
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(230, 230, 230));
        logoutButton.setFocusPainted(false);
        logoutPanel.add(logoutButton);

        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null); // Using absolute positioning for precise layout

        // Create buttons with specific styling
        JButton orderButton = createStyledButton("ORDER REQUESTS", 150, 50);
        JButton stockButton = createStyledButton("MANAGE STOCK", 150, 50);
        JButton paymentButton = createStyledButton("MANAGE PAYMENT", 150, 50);

        // Position buttons according to the mockup
        orderButton.setBounds(100, 50, 150, 50);
        stockButton.setBounds(350, 50, 150, 50);
        paymentButton.setBounds(225, 150, 150, 50);

        // Add buttons to the panel
        buttonPanel.add(orderButton);
        buttonPanel.add(stockButton);
        buttonPanel.add(paymentButton);

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to frame
        adminFrame.add(mainPanel);

        // Button action listeners
        orderButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();
                OrderRequestPage.order();
            }

        });

        stockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();
                StockManagementPage.showStockManagementPage(adminFrame);
            }
        });

        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(adminFrame, "Payment management is not implemented yet.");
            }
        });

        // Logout button action
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();
                new StartWindow();
            }
        });

        // Center the frame on screen and make it visible
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setVisible(true);
    }

    // Helper method to create consistently styled buttons
    private static JButton createStyledButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(230, 230, 230));
        button.setFocusPainted(false);
        return button;
    }
}