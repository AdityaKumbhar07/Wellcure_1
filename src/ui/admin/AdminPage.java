package ui.admin;

import ui.StartWindow;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AdminPage provides the main dashboard for administrators.
 * It offers access to various management functions like order requests,
 * stock management, and payment management.
 */
public class AdminPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Admin Panel - WellCure";
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;

    // Content settings
    private static final String TITLE_TEXT = "ADMIN PANEL";
    private static final String ORDER_BUTTON_TEXT = "ORDER REQUESTS";
    private static final String STOCK_BUTTON_TEXT = "MANAGE STOCK";
    private static final String PAYMENT_BUTTON_TEXT = "MANAGE PAYMENT";
    private static final String LOGOUT_BUTTON_TEXT = "Logout";

    // Button positions
    private static final int ORDER_BUTTON_X = 100;
    private static final int ORDER_BUTTON_Y = 50;
    private static final int STOCK_BUTTON_X = 350;
    private static final int STOCK_BUTTON_Y = 50;
    private static final int PAYMENT_BUTTON_X = 225;
    private static final int PAYMENT_BUTTON_Y = 150;

    // Button dimensions
    private static final Dimension ADMIN_BUTTON_SIZE = new Dimension(150, 50);

    /**
     * Initializes and displays the admin dashboard.
     */
    public static void admin() {
        JFrame adminFrame = new JFrame(WINDOW_TITLE);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        adminFrame.setLayout(new BorderLayout());
        UIConfig.styleFrame(adminFrame);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        UIConfig.stylePanel(mainPanel);

        // Top panel with title and logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConfig.PRIMARY_BG);

        // Title panel at the center
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(UIConfig.PRIMARY_BG);
        JLabel titleLabel = new JLabel(TITLE_TEXT);
        UIConfig.styleTitle(titleLabel);
        titlePanel.add(titleLabel);

        // Logout button at the right
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(UIConfig.PRIMARY_BG);
        JButton logoutButton = new JButton(LOGOUT_BUTTON_TEXT);
        UIConfig.styleButton(logoutButton);
        logoutPanel.add(logoutButton);

        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null); // Using absolute positioning for precise layout
        buttonPanel.setBackground(UIConfig.PRIMARY_BG);

        // Create buttons with specific styling
        JButton orderButton = new JButton(ORDER_BUTTON_TEXT);
        UIConfig.styleButton(orderButton);
        orderButton.setPreferredSize(ADMIN_BUTTON_SIZE);
        orderButton.setSize(ADMIN_BUTTON_SIZE);

        JButton stockButton = new JButton(STOCK_BUTTON_TEXT);
        UIConfig.styleButton(stockButton);
        stockButton.setPreferredSize(ADMIN_BUTTON_SIZE);
        stockButton.setSize(ADMIN_BUTTON_SIZE);

        JButton paymentButton = new JButton(PAYMENT_BUTTON_TEXT);
        UIConfig.styleButton(paymentButton);
        paymentButton.setPreferredSize(ADMIN_BUTTON_SIZE);
        paymentButton.setSize(ADMIN_BUTTON_SIZE);

        // Position buttons according to the constants
        orderButton.setLocation(ORDER_BUTTON_X, ORDER_BUTTON_Y);
        stockButton.setLocation(STOCK_BUTTON_X, STOCK_BUTTON_Y);
        paymentButton.setLocation(PAYMENT_BUTTON_X, PAYMENT_BUTTON_Y);

        // Add buttons to the panel
        buttonPanel.add(orderButton);
        buttonPanel.add(stockButton);
        buttonPanel.add(paymentButton);

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to frame
        adminFrame.add(mainPanel);

        // ==================== Event Handlers ====================

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
                StockManagementPage.showStockManagementPage();
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
}