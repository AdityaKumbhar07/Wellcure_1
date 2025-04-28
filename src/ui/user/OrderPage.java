package ui.user;

import database.DBconnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderPage {

    public static void showUserOrders(String username) {
        JFrame frame = new JFrame("Your Orders");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Your Orders", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Panel to display order details
        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));

        // Fetch orders from DB
        List<Order> orders = fetchOrders(username);

        if (orders.isEmpty()) {
            ordersPanel.add(new JLabel("No orders found."));
        } else {
            for (Order order : orders) {
                JPanel orderDetailsPanel = new JPanel(new GridLayout(2, 1));
                orderDetailsPanel.add(new JLabel("Prescription ID: " + order.getPrescriptionId()));
                orderDetailsPanel.add(new JLabel("Status: " + order.getStatus()));

                JButton requestButton = new JButton("Send Request to Admin");
                if (order.getStatus().equalsIgnoreCase("Pending")) {
                    requestButton.addActionListener(e -> sendPrescriptionRequest(username, order.getPrescriptionId(), frame));
                } else {
                    requestButton.setEnabled(false); // Disable for non-pending orders
                }

                orderDetailsPanel.add(requestButton);
                ordersPanel.add(orderDetailsPanel);
            }
        }

        frame.add(new JScrollPane(ordersPanel), BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.dispose()); // Close the current window
        backButton.addActionListener(e -> new UserHomePage(username).showUserHomePage()); // Go back to User Home Page
        frame.add(backButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }

    private static List<Order> fetchOrders(String username) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT p.prescription_id, p.status FROM prescriptions p " +
                "JOIN users u ON p.user_id = u.user_id WHERE u.username = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int prescriptionId = rs.getInt("prescription_id");
                    String status = rs.getString("status");
                    orders.add(new Order(prescriptionId, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return orders;
    }

    private static void sendPrescriptionRequest(String username, int prescriptionId, JFrame frame) {
        String sql = "INSERT INTO orders (username, prescription_id, order_status) VALUES (?, ?, 'Requested')";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setInt(2, prescriptionId);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Order sent to admin successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close the order page
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to send the order.", "Failure", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error sending order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Order class to hold order data
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
