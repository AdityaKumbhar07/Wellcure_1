package ui.admin;

import database.DBconnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class OrderRequestPage {

    public static void order() {
        JFrame frame = new JFrame("Order Requests");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        refreshOrders(frame);

        frame.setVisible(true);
    }

    private static void refreshOrders(JFrame frame) {
        frame.getContentPane().removeAll();
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));

        Connection con = DBconnection.getConnection();
        if (con == null) {
            JOptionPane.showMessageDialog(frame, "Failed to establish connection.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT orders.order_id, users.name AS username, orders.order_status, prescriptions.status AS status, prescriptions.image_path " +
                "FROM orders " +
                "JOIN users ON orders.user_id = users.user_id " +
                "JOIN prescriptions ON orders.prescription_id = prescriptions.prescription_id";

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;

                int orderId = rs.getInt("order_id");
                String userName = rs.getString("username");
                String orderStatus = rs.getString("order_status");
                String prescriptionStatus = rs.getString("status");
                String imagePath = rs.getString("image_path");

                JPanel orderDetails = new JPanel();
                orderDetails.setLayout(new FlowLayout(FlowLayout.LEFT));

                orderDetails.add(new JLabel("Order ID: " + orderId));
                orderDetails.add(new JLabel("User: " + userName));
                orderDetails.add(new JLabel("Order Status: " + orderStatus));
                orderDetails.add(new JLabel("Prescription Status: " + prescriptionStatus));

                JButton viewPrescriptionButton = new JButton("View Prescription");
                viewPrescriptionButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        showPrescriptionImage(imagePath);
                    }
                });

                JButton confirmButton = new JButton("Confirm");
                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        confirmOrder(orderId);
                        refreshOrders(frame);
                    }
                });

                JButton rejectButton = new JButton("Reject");
                rejectButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        rejectOrder(orderId);
                        refreshOrders(frame);
                    }
                });

                orderDetails.add(viewPrescriptionButton);
                orderDetails.add(confirmButton);
                orderDetails.add(rejectButton);

                orderPanel.add(orderDetails);
            }

            if (!hasResults) {
                orderPanel.add(new JLabel("No order requests found."));
            }

            JScrollPane scrollPane = new JScrollPane(orderPanel);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching order requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showPrescriptionImage(String imagePath) {
        JFrame imgFrame = new JFrame("Prescription Image");
        imgFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            if (img == null) {
                JOptionPane.showMessageDialog(null, "Failed to load image.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Image scaledImage = img.getScaledInstance(500, -1, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaledImage));

            imgFrame.add(new JScrollPane(label));
            imgFrame.pack();
            imgFrame.setLocationRelativeTo(null);
            imgFrame.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void confirmOrder(int orderId) {
        String sql = "UPDATE orders SET order_status = 'Confirmed' WHERE order_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Order confirmed!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to confirm order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error confirming order.");
        }
    }

    private static void rejectOrder(int orderId) {
        String sql = "UPDATE orders SET order_status = 'Rejected' WHERE order_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Order rejected!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to reject order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error rejecting order.");
        }
    }
}
