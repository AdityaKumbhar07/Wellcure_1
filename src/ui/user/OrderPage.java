package ui.user;

import database.DBconnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderPage {

    public static void showUserOrders(String username) {
        JFrame frame = new JFrame("Your Orders");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Your Orders", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));

        List<Order> orders = fetchOrders(username);

        if (orders.isEmpty()) {
            ordersPanel.add(new JLabel("No orders found."));
        } else {
            for (Order order : orders) {
                JPanel orderDetailsPanel = new JPanel(new GridLayout(3, 1));
                orderDetailsPanel.add(new JLabel("Prescription ID: " + order.getPrescriptionId()));
                orderDetailsPanel.add(new JLabel("Status: " + order.getStatus()));

                JButton checkoutButton = new JButton("Checkout");
                if ("Draft".equals(order.getStatus())) {
                    checkoutButton.addActionListener(e -> {
                        // Navigate to payment page instead of directly updating status
                        frame.dispose();
                        PaymentPage.showPaymentPage(order.getPrescriptionId(), username);
                    });
                } else {
                    checkoutButton.setEnabled(false);
                }

                orderDetailsPanel.add(checkoutButton);
                ordersPanel.add(orderDetailsPanel);
            }
        }

        frame.add(new JScrollPane(ordersPanel), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            new UserHomePage(username);
        });
        frame.add(backButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static List<Order> fetchOrders(String username) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT p.prescription_id, o.order_status FROM prescriptions p " +
                "JOIN orders o ON p.prescription_id = o.prescription_id " +
                "JOIN users u ON p.user_id = u.user_id WHERE u.username = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int prescriptionId = rs.getInt("prescription_id");
                    String status = rs.getString("order_status");
                    orders.add(new Order(prescriptionId, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return orders;
    }

    static class Order {
        private final int prescriptionId;
        private final String status;

        public Order(int prescriptionId, String status) {
            this.prescriptionId = prescriptionId;
            this.status = status;
        }

        public int getPrescriptionId() {
            return prescriptionId;
        }

        public String getStatus() {
            return status;
        }
    }
}