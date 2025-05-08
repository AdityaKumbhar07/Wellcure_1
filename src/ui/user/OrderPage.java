package ui.user;

import database.DBconnection;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderPage displays the user's orders and allows them to check out draft orders.
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
    private static final String PRICE_PREFIX = "Total Price: ";
    private static final String CHECKOUT_BUTTON_TEXT = "Checkout";
    private static final String MAKE_PAYMENT_BUTTON_TEXT = "Make Payment";
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

                // Add price label with conditional display
                String priceText;
                if ("Confirmed".equals(order.getStatus())) {
                    priceText = String.format("₹%.2f", order.getTotalPrice());
                } else {
                    priceText = "₹0.00";
                }
                JLabel priceLabel = new JLabel(PRICE_PREFIX + priceText);
                priceLabel.setFont(UIConfig.REGULAR_FONT);
                priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Add details to panel
                orderDetailsPanel.add(prescriptionIdLabel);
                orderDetailsPanel.add(Box.createVerticalStrut(FIELD_SPACING));
                orderDetailsPanel.add(statusLabel);
                orderDetailsPanel.add(Box.createVerticalStrut(FIELD_SPACING));
                orderDetailsPanel.add(priceLabel);
                orderDetailsPanel.add(Box.createVerticalStrut(FIELD_SPACING));

                // Button panel for order actions
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
                buttonPanel.setBackground(UIConfig.PRIMARY_BG);
                buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

                // Add appropriate buttons based on order status
                if ("Draft".equals(order.getStatus())) {
                    // Checkout button for draft orders
                    JButton checkoutButton = new JButton(CHECKOUT_BUTTON_TEXT);
                    UIConfig.styleButton(checkoutButton);
                    checkoutButton.setPreferredSize(new Dimension(150, 40));

                    checkoutButton.addActionListener(e -> {
                        // Get order ID
                        int orderId = getOrderIdFromPrescription(order.getPrescriptionId());
                        if (orderId > 0) {
                            // Navigate to payment selection page
                            frame.dispose();
                            // Use 0.0 for draft orders
                            PaymentPage.showPaymentPage(order.getPrescriptionId(), username);
//                            PaymentSelectionPage.showPaymentSelectionPage(orderId, 0.0, username);
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Error retrieving order information.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    buttonPanel.add(checkoutButton);
                } else if ("Confirmed".equals(order.getStatus())) {
                    // Make Payment button for confirmed orders
                    JButton makePaymentButton = new JButton(MAKE_PAYMENT_BUTTON_TEXT);
                    UIConfig.styleButton(makePaymentButton);
                    makePaymentButton.setPreferredSize(new Dimension(150, 40));

                    makePaymentButton.addActionListener(e -> {
                        // Get order ID
                        int orderId = getOrderIdFromPrescription(order.getPrescriptionId());
                        if (orderId > 0) {
                            // Navigate to payment selection page
                            frame.dispose();
                            // Use the actual price for confirmed orders
                            PaymentSelectionPage.showPaymentSelectionPage(orderId, order.getTotalPrice(), username);
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Error retrieving order information.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    buttonPanel.add(makePaymentButton);
                } else {
                    // For other statuses (Pending, Rejected, etc.), no action buttons
                    JLabel statusInfoLabel = new JLabel("No actions available");
                    statusInfoLabel.setFont(UIConfig.REGULAR_FONT);
                    statusInfoLabel.setForeground(Color.GRAY);
                    buttonPanel.add(statusInfoLabel);
                }

                orderDetailsPanel.add(buttonPanel);

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
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.setBackground(UIConfig.PRIMARY_BG);
        backButtonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton backButton = new JButton(BACK_BUTTON_TEXT);
        UIConfig.styleButton(backButton);
        backButton.setPreferredSize(new Dimension(150, 40));

        backButton.addActionListener(e -> {
            frame.dispose();
            new UserHomePage(username);
        });

        backButtonPanel.add(backButton);
        mainPanel.add(backButtonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Display the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static List<Order> fetchOrders(String username) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT p.prescription_id, o.order_status, o.total_price FROM prescriptions p " +
                "JOIN orders o ON p.prescription_id = o.prescription_id " +
                "JOIN users u ON p.user_id = u.user_id WHERE u.username = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int prescriptionId = rs.getInt("prescription_id");
                    String status = rs.getString("order_status");
                    double totalPrice = rs.getDouble("total_price");
                    orders.add(new Order(prescriptionId, status, totalPrice));
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
        private final double totalPrice;

        public Order(int prescriptionId, String status, double totalPrice) {
            this.prescriptionId = prescriptionId;
            this.status = status;
            this.totalPrice = totalPrice;
        }

        public int getPrescriptionId() {
            return prescriptionId;
        }

        public String getStatus() {
            return status;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }

    /**
     * Get the order ID associated with a prescription ID
     *
     * @param prescriptionId The prescription ID
     * @return The order ID, or -1 if not found
     */
    private static int getOrderIdFromPrescription(int prescriptionId) {
        String sql = "SELECT order_id FROM orders WHERE prescription_id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prescriptionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("order_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}