package ui.user;

import database.DBconnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PaymentPage {

    public static void showPaymentPage(int prescriptionId, String username) {
        JFrame frame = new JFrame("Payment - WellCure");
        frame.setSize(450, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title
        JLabel titleLabel = new JLabel("Payment Options");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Order details
        JLabel orderLabel = new JLabel("Order for Prescription #" + prescriptionId);
        orderLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        orderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(orderLabel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Payment options
        JLabel paymentOptionsLabel = new JLabel("Select Payment Method:");
        paymentOptionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        paymentOptionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(paymentOptionsLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // COD option
        JRadioButton codOption = new JRadioButton("Cash On Delivery (COD)");
        codOption.setFont(new Font("Arial", Font.PLAIN, 14));
        codOption.setAlignmentX(Component.CENTER_ALIGNMENT);
        codOption.setSelected(true); // Default selection

        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(codOption);

        mainPanel.add(codOption);
        mainPanel.add(Box.createVerticalStrut(30));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setBackground(new Color(230, 230, 230));
        cancelButton.setFocusPainted(false);

        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setPreferredSize(new Dimension(150, 40));
        confirmButton.setBackground(new Color(230, 230, 230));
        confirmButton.setFocusPainted(false);

        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(confirmButton);

        mainPanel.add(buttonPanel);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Action for Cancel Button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Action for Confirm Button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (codOption.isSelected()) {
                    if (updateOrderStatus(prescriptionId, "Pending")) {
                        JOptionPane.showMessageDialog(frame,
                                "Order confirmed with Cash On Delivery payment option!",
                                "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        // Return to user home page
                        new UserHomePage(username);
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Failed to confirm order. Please try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Make the frame visible
        frame.setLocationRelativeTo(null);  // Center the window on the screen
        frame.setVisible(true);
    }

    private static boolean updateOrderStatus(int prescriptionId, String newStatus) {
        String sql = "UPDATE orders SET order_status = ? WHERE prescription_id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, prescriptionId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}