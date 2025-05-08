package ui.user;

import database.DBconnection;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * PaymentSelectionPage allows users to select a payment method for their order.
 * Users can choose between Cash on Delivery (COD) and UPI payment options.
 */
public class PaymentSelectionPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Select Payment Method - WellCure";
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 400;

    // Content settings
    private static final String TITLE_TEXT = "Select Payment Method";
    private static final String SUBTITLE_TEXT = "Please select your preferred payment method:";
    private static final String COD_OPTION_TEXT = "Cash on Delivery (COD)";
    private static final String UPI_OPTION_TEXT = "UPI Payment";
    private static final String TOTAL_AMOUNT_PREFIX = "Total Amount: ";
    private static final String CONFIRM_BUTTON_TEXT = "Confirm";
    private static final String BACK_BUTTON_TEXT = "Back";

    // Spacing settings
    private static final int TITLE_SPACING = 20;
    private static final int CONTENT_SPACING = 15;
    private static final int BUTTON_SPACING = 10;

    // Instance variables
    private JFrame frame;
    private JRadioButton codOption;
    private JRadioButton upiOption;
    private int orderId;
    private double totalAmount;
    private String username;

    /**
     * Constructor for PaymentSelectionPage
     *
     * @param orderId The ID of the order
     * @param totalAmount The total amount to be paid
     * @param username The username of the current user
     */
    public PaymentSelectionPage(int orderId, double totalAmount, String username) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.username = username;

        // Initialize UI
        initializeUI();
    }

    /**
     * Initialize the user interface
     */
    private void initializeUI() {
        // Create main frame
        frame = new JFrame(WINDOW_TITLE);
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

        JLabel subtitleLabel = new JLabel(SUBTITLE_TEXT);
        subtitleLabel.setFont(UIConfig.SUBTITLE_FONT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(TITLE_SPACING));
        titlePanel.add(subtitleLabel);
        titlePanel.add(Box.createVerticalStrut(CONTENT_SPACING));

        // Total amount label
        JLabel totalAmountLabel = new JLabel(TOTAL_AMOUNT_PREFIX + String.format("₹%.2f", totalAmount));
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(totalAmountLabel);
        titlePanel.add(Box.createVerticalStrut(CONTENT_SPACING * 2));

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Payment options panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(UIConfig.PRIMARY_BG);
        optionsPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        // Radio buttons for payment options
        ButtonGroup paymentOptions = new ButtonGroup();

        // COD option
        codOption = new JRadioButton(COD_OPTION_TEXT);
        codOption.setFont(UIConfig.REGULAR_FONT);
        codOption.setBackground(UIConfig.PRIMARY_BG);
        codOption.setFocusPainted(false);
        codOption.setSelected(true); // Default selection

        // UPI option
        upiOption = new JRadioButton(UPI_OPTION_TEXT);
        upiOption.setFont(UIConfig.REGULAR_FONT);
        upiOption.setBackground(UIConfig.PRIMARY_BG);
        upiOption.setFocusPainted(false);

        // Add to button group
        paymentOptions.add(codOption);
        paymentOptions.add(upiOption);

        // Add to panel with spacing
        optionsPanel.add(codOption);
        optionsPanel.add(Box.createVerticalStrut(CONTENT_SPACING));
        optionsPanel.add(upiOption);

        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTON_SPACING, 0));
        buttonsPanel.setBackground(UIConfig.PRIMARY_BG);
        buttonsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton confirmButton = new JButton(CONFIRM_BUTTON_TEXT);
        JButton backButton = new JButton(BACK_BUTTON_TEXT);

        UIConfig.styleButton(confirmButton);
        UIConfig.styleButton(backButton);

        confirmButton.setPreferredSize(new Dimension(120, 40));
        backButton.setPreferredSize(new Dimension(120, 40));

        buttonsPanel.add(backButton);
        buttonsPanel.add(confirmButton);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        frame.add(mainPanel);

        // Action for Back Button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                OrderPage.showUserOrders(username);
            }
        });

        // Action for Confirm Button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (codOption.isSelected()) {
                    // COD selected
                    updateOrderPaymentMethod("COD");
                    JOptionPane.showMessageDialog(frame,
                            "Payment method set to Cash On Delivery. Your order will be processed soon.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();

                    String sql = "UPDATE orders SET order_status = 'Placed' WHERE prescription_id = ?";

                    verifyPayment(orderId);

                    // Go back to order page
                    OrderPage.showUserOrders(username);

                } else if (upiOption.isSelected()) {
                    // UPI selected
                    updateOrderPaymentMethod("UPI");

                    // Generate and show UPI QR code
                     UPIPaymentPage.showUPIPaymentPage(orderId, totalAmount, username);
                    frame.dispose();
                }
            }
        });

        // Display the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean verifyPayment(int orderId) {
        try (Connection conn = DBconnection.getConnection()) {
            if (conn == null) {
                return false;
            }

            String sql = "UPDATE orders SET order_status = 'Placed' WHERE order_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, orderId);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update the payment method for the order in the database
     *
     * @param paymentMethod The selected payment method (COD or UPI)
     */
    private void updateOrderPaymentMethod(String paymentMethod) {
        String sql = "UPDATE orders SET order_mode = ? WHERE order_id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paymentMethod);
            stmt.setInt(2, orderId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "Error updating payment method: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Static method to show the payment selection page
     *
     * @param orderId The ID of the order
     * @param totalAmount The total amount to be paid
     * @param username The username of the current user
     */
    public static void showPaymentSelectionPage(int orderId, double totalAmount, String username) {
        new PaymentSelectionPage(orderId, totalAmount, username);
    }
}