package ui.user;

import database.DBconnection;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderPage displays the user's orders and allows them to checkout draft orders.
 * It retrieves order information from the database and provides options to manage orders.
 */
public class OrderPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Your Orders - WellCure";
    private static final int WINDOW_WIDTH = 550;
    private static final int WINDOW_HEIGHT = 500;

    // Content settings
    private static final String TITLE_TEXT = "Your Orders";
    private static final String NO_ORDERS_TEXT = "No orders found.";
    private static final String PRESCRIPTION_ID_PREFIX = "Prescription ID: ";
    private static final String STATUS_PREFIX = "Status: ";
    private static final String CHECKOUT_BUTTON_TEXT = "Checkout";
    private static final String BACK_BUTTON_TEXT = "Back";

    // Spacing settings
    private static final int TITLE_SPACING = 20;
    private static final int ORDER_SPACING = 15;
    private static final int FIELD_SPACING = 10;

    /**
     * Displays the user's orders and provides options to manage them.
     *
     * @param username The username of the logged-in user
     */
    public static void showUserOrders(String username) {
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        UIConfig.styleFrame(frame);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        UIConfig.stylePanel(mainPanel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(UIConfig.PRIMARY_BG);

        JLabel titleLabel = new JLabel(TITLE_TEXT);
        UIConfig.styleTitle(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(TITLE_SPACING));

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Orders panel
        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBackground(UIConfig.PRIMARY_BG);

        List<Order> orders = fetchOrders(username);

        if (orders.isEmpty()) {
            JLabel noOrdersLabel = new JLabel(NO_ORDERS_TEXT);
            noOrdersLabel.setFont(UIConfig.SUBTITLE_FONT);
            noOrdersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ordersPanel.add(noOrdersLabel);
        } else {
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);

                // Create a panel for each order with a border
                JPanel orderDetailsPanel = new JPanel();
                orderDetailsPanel.setLayout(new BoxLayout(orderDetailsPanel, BoxLayout.Y_AXIS));
                orderDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
                    UIConfig.ROUNDED_BORDER,
                    new EmptyBorder(10, 10, 10, 10)
                ));
                orderDetailsPanel.setBackground(UIConfig.PRIMARY_BG);
                orderDetailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                orderDetailsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

                // Order details
                JLabel prescriptionIdLabel = new JLabel(PRESCRIPTION_ID_PREFIX + order.getPrescriptionId());
                prescriptionIdLabel.setFont(UIConfig.SUBTITLE_FONT);
                prescriptionIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel statusLabel = new JLabel(STATUS_PREFIX + order.getStatus());
                statusLabel.setFont(UIConfig.REGULAR_FONT);
                statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Add details to panel
                orderDetailsPanel.add(prescriptionIdLabel);
                orderDetailsPanel.add(Box.createVerticalStrut(FIELD_SPACING));
                orderDetailsPanel.add(statusLabel);
                orderDetailsPanel.add(Box.createVerticalStrut(FIELD_SPACING));

                // Checkout button
                JButton checkoutButton = new JButton(CHECKOUT_BUTTON_TEXT);
                UIConfig.styleButton(checkoutButton);
                checkoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                checkoutButton.setMaximumSize(new Dimension(150, 40));

                if ("Draft".equals(order.getStatus())) {
                    checkoutButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Navigate to payment page
                            frame.dispose();
                            PaymentPage.showPaymentPage(order.getPrescriptionId(), username);
                        }
                    });
                } else {
                    checkoutButton.setEnabled(false);
                }

                orderDetailsPanel.add(checkoutButton);

                // Add order panel to orders panel
                ordersPanel.add(orderDetailsPanel);

                // Add spacing between orders (except after the last one)
                if (i < orders.size() - 1) {
                    ordersPanel.add(Box.createVerticalStrut(ORDER_SPACING));
                }
            }
        }

        // Add orders panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(UIConfig.PRIMARY_BG);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton backButton = new JButton(BACK_BUTTON_TEXT);
        UIConfig.styleButton(backButton);
        backButton.setPreferredSize(new Dimension(150, 40));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new UserHomePage(username);
            }
        });

        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Display the frame
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