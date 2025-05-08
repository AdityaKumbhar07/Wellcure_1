package ui.admin;

import ui.StartWindow;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * AdminPage serves as the main dashboard for administrators.
 * It provides access to various administrative functions.
 */
public class AdminPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Admin Panel - WellCure";
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 500;

    // Content settings
    private static final String TITLE_TEXT = "ADMIN PANEL";
    private static final String ORDER_REQUESTS_TEXT = "ORDER REQUESTS";
    private static final String MANAGE_STOCK_TEXT = "MANAGE STOCK";
    private static final String MANAGE_PAYMENT_TEXT = "MANAGE PAYMENT";
    private static final String PAYMENT_VERIFICATION_TEXT = "PAYMENT VERIFICATION";
    private static final String LOGOUT_TEXT = "LOGOUT";

    // Button dimensions
    private static final Dimension MAIN_BUTTON_SIZE = new Dimension(200, 80);
    private static final Dimension LOGOUT_BUTTON_SIZE = new Dimension(120, 40);

    // Spacing settings
    private static final int TITLE_SPACING = 30;
    private static final int BUTTON_SPACING = 20;

    /**
     * Displays the admin panel with options for managing the pharmacy.
     */
    public static void admin() {
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLayout(new BorderLayout());
        UIConfig.styleFrame(frame);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        UIConfig.stylePanel(mainPanel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(UIConfig.PRIMARY_BG);

        JLabel titleLabel = new JLabel(TITLE_TEXT);
        UIConfig.styleTitle(titleLabel);
        titlePanel.add(titleLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 2, BUTTON_SPACING, BUTTON_SPACING));
        buttonsPanel.setBackground(UIConfig.PRIMARY_BG);
        buttonsPanel.setBorder(new EmptyBorder(TITLE_SPACING, 0, 0, 0));

        JButton orderRequestsButton = new JButton(ORDER_REQUESTS_TEXT);
        JButton manageStockButton = new JButton(MANAGE_STOCK_TEXT);
        JButton managePaymentButton = new JButton(MANAGE_PAYMENT_TEXT);
        JButton paymentVerificationButton = new JButton(PAYMENT_VERIFICATION_TEXT);

        UIConfig.styleButton(orderRequestsButton);
        UIConfig.styleButton(manageStockButton);
        UIConfig.styleButton(managePaymentButton);
        UIConfig.styleButton(paymentVerificationButton);

        orderRequestsButton.setPreferredSize(MAIN_BUTTON_SIZE);
        manageStockButton.setPreferredSize(MAIN_BUTTON_SIZE);
        managePaymentButton.setPreferredSize(MAIN_BUTTON_SIZE);
        paymentVerificationButton.setPreferredSize(MAIN_BUTTON_SIZE);

        buttonsPanel.add(orderRequestsButton);
        buttonsPanel.add(manageStockButton);
        buttonsPanel.add(managePaymentButton);
        buttonsPanel.add(paymentVerificationButton);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        // Logout button panel
        JPanel logoutPanel = new JPanel();
        logoutPanel.setBackground(UIConfig.PRIMARY_BG);
        logoutPanel.setBorder(new EmptyBorder(BUTTON_SPACING, 0, 0, 0));

        JButton logoutButton = new JButton(LOGOUT_TEXT);
        UIConfig.styleButton(logoutButton);
        logoutButton.setPreferredSize(LOGOUT_BUTTON_SIZE);
        logoutPanel.add(logoutButton);

        mainPanel.add(logoutPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        frame.add(mainPanel);

        // ==================== Event Handlers ====================

        // Order Requests Button Action
        orderRequestsButton.addActionListener(e -> {
            frame.dispose();
            OrderRequestPage.order();
        });

        // Manage Stock Button Action
        manageStockButton.addActionListener(e -> {
            frame.dispose();
            StockManagementPage.showStockManagementPage();
        });

        // Manage Payment Button Action
        managePaymentButton.addActionListener(e -> {
            frame.dispose();
            // Navigate to payment management page
            // PaymentManagementPage.showPaymentManagement();
            JOptionPane.showMessageDialog(null, "Payment Management feature coming soon!", "Under Development", JOptionPane.INFORMATION_MESSAGE);
            admin(); // Return to admin page for now
        });

        // Payment Verification Button Action
        paymentVerificationButton.addActionListener(e -> {
            frame.dispose();
            PaymentVerificationPage.showPaymentVerification();
        });

        // Logout Button Action
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new StartWindow();
        });

        // Display the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}