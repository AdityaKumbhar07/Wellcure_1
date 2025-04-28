package ui.user;

import database.DBconnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MyOrdersPage {

    public static void viewOrders(String username) {
        JFrame frame = new JFrame("My Orders");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));

        String sql = "SELECT o.order_id, o.order_status, p.image_path FROM orders o " +
                "JOIN prescriptions p ON o.prescription_id = p.prescription_id " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE u.username = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            boolean hasOrders = false;

            while (rs.next()) {
                hasOrders = true;
                int orderId = rs.getInt("order_id");
                String orderStatus = rs.getString("order_status");
                String imagePath = rs.getString("image_path");

                JPanel singleOrder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                singleOrder.add(new JLabel("Order ID: " + orderId));
                singleOrder.add(new JLabel("Status: " + orderStatus));

                JButton viewPrescription = new JButton("View Prescription");
                viewPrescription.addActionListener(e -> {
                    ImageIcon icon = new ImageIcon(imagePath);
                    JOptionPane.showMessageDialog(frame, new JLabel(icon));
                });

                singleOrder.add(viewPrescription);
                ordersPanel.add(singleOrder);
            }

            if (!hasOrders) {
                ordersPanel.add(new JLabel("You have no orders."));
            }

            JScrollPane scrollPane = new JScrollPane(ordersPanel);
            frame.add(scrollPane, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching orders.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }
}
