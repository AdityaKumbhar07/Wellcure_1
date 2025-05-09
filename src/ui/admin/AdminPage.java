package ui.admin;

import ui.StartWindow;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * AdminPage serves as the main dashboard for administrators.
 * It provides access to various administrative functions.
 * Redesigned with a modern, clean aesthetic.
 */
public class AdminPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Admin Panel - WellCure";
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 400;

    // Colors
    private static final Color HEADER_BG = new Color(44, 43, 43);  // Dark header background
    private static final Color HEADER_FG = Color.WHITE;            // White text for header
    private static final Color ACCENT_COLOR = new Color(70, 130, 180); // Steel blue accent

    // Content settings
    private static final String TITLE_TEXT = "ADMIN PANEL";
    private static final String ORDER_REQUESTS_TEXT = "Order Requests";
    private static final String MANAGE_STOCK_TEXT = "Manage Stock";
    private static final String MANAGE_PAYMENT_TEXT = "Manage Payment";
    private static final String PAYMENT_VERIFICATION_TEXT = "Payment Verification";
    private static final String LOGOUT_ICON_PATH = "outside thigs/back_button.png";  // Path to logout icon image
    // Try different variations of the logo path
    private static final String[] APP_LOGO_PATHS = {
        "outside thigs/App_logo.png",
        "outside thigs/App logo.png",
        "outside thigs/logo.png",
        "outside thigs/AppLogo.png",
        "./outside thigs/App_logo.png",
        "../outside thigs/App_logo.png",
        "D:/College/Java/Mini Project (self)/Wellcure/outside thigs/App_logo.png"
    };

    // Button dimensions
    private static final Dimension MAIN_BUTTON_SIZE = new Dimension(200, 50);
    private static final Dimension LOGOUT_BUTTON_SIZE = new Dimension(40, 40);

    // Spacing settings
    private static final int TITLE_SPACING = 20;
    private static final int BUTTON_SPACING = 15;

    /**
     * Displays the admin panel with options for managing the pharmacy.
     * Redesigned with a modern, clean aesthetic.
     */
    public static void admin() {
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Create a stylish header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Back button in header
        JButton logoutButton = new JButton();
        try {
            ImageIcon logoutIcon = new ImageIcon(LOGOUT_ICON_PATH);
            Image img = logoutIcon.getImage();
            Image resizedImg = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            logoutButton.setIcon(new ImageIcon(resizedImg));
        } catch (Exception e) {
            logoutButton.setText("←");
            logoutButton.setForeground(HEADER_FG);
        }
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(LOGOUT_BUTTON_SIZE);

        // Create center panel for logo and title
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setBackground(HEADER_BG);

        // App logo
        JLabel logoLabel = new JLabel();
        boolean logoLoaded = false;

        // Try all possible paths
        for (String path : APP_LOGO_PATHS) {
            try {
                System.out.println("Trying to load logo from: " + path);
                ImageIcon logoIcon = new ImageIcon(path);

                // Check if image loaded properly
                if (logoIcon.getIconWidth() > 0) {
                    // Resize the icon to fit nicely
                    Image img = logoIcon.getImage();
                    Image resizedImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                    logoLabel.setIcon(new ImageIcon(resizedImg));
                    System.out.println("Logo loaded successfully from: " + path);
                    logoLoaded = true;
                    break;
                }

                // Try with File approach
                if (!logoLoaded) {
                    try {
                        java.io.File imgFile = new java.io.File(path);
                        if (imgFile.exists()) {
                            System.out.println("File exists at: " + imgFile.getAbsolutePath());
                            logoIcon = new ImageIcon(imgFile.getAbsolutePath());
                            if (logoIcon.getIconWidth() > 0) {
                                Image img = logoIcon.getImage();
                                Image resizedImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                                logoLabel.setIcon(new ImageIcon(resizedImg));
                                System.out.println("Logo loaded successfully via File from: " + path);
                                logoLoaded = true;
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Error with file approach: " + ex.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error trying path " + path + ": " + e.getMessage());
            }
        }

        // If logo couldn't be loaded, create a simple text-based logo
        if (!logoLoaded) {
            System.out.println("Could not load logo from any path, creating text logo");
            JLabel textLogo = new JLabel("WC");
            textLogo.setFont(new Font("Arial", Font.BOLD, 16));
            textLogo.setForeground(new Color(70, 130, 180)); // Steel blue color
            textLogo.setBackground(new Color(240, 240, 240));
            textLogo.setOpaque(true);
            textLogo.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            textLogo.setPreferredSize(new Dimension(30, 30));
            logoLabel = textLogo;
        }

        // Title with a modern font
        JLabel titleLabel = new JLabel(TITLE_TEXT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(HEADER_FG);

        // Add logo and title to center panel
        centerPanel.add(logoLabel);
        centerPanel.add(titleLabel);

        // Add components to header
        headerPanel.add(logoutButton, BorderLayout.WEST);
        headerPanel.add(centerPanel, BorderLayout.CENTER);

        // Add header to frame
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main content panel with padding
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a GridBagConstraints for positioning components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(BUTTON_SPACING, BUTTON_SPACING, BUTTON_SPACING, BUTTON_SPACING);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Create stylish buttons with custom styling
        JButton orderRequestsButton = createStylishButton(ORDER_REQUESTS_TEXT);
        JButton manageStockButton = createStylishButton(MANAGE_STOCK_TEXT);
        JButton managePaymentButton = createStylishButton(MANAGE_PAYMENT_TEXT);
        JButton paymentVerificationButton = createStylishButton(PAYMENT_VERIFICATION_TEXT);

        // Position buttons in a 2x2 grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(orderRequestsButton, gbc);

        gbc.gridx = 1;
        contentPanel.add(manageStockButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(managePaymentButton, gbc);

        gbc.gridx = 1;
        contentPanel.add(paymentVerificationButton, gbc);

        // Add content panel to frame
        frame.add(contentPanel, BorderLayout.CENTER);

        // Add a subtle footer with version info
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(245, 245, 245));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel versionLabel = new JLabel("WellCure Pharmacy Management System v1.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(120, 120, 120));
        footerPanel.add(versionLabel);

        frame.add(footerPanel, BorderLayout.SOUTH);

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
            ReportGenerator.generateOrderReport();

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

    /**
     * Creates a stylish button with modern aesthetics.
     *
     * @param text The text to display on the button
     * @return A styled JButton
     */
    private static JButton createStylishButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(50, 50, 50));
        button.setFocusPainted(false);
        button.setPreferredSize(MAIN_BUTTON_SIZE);

        // Add a border with rounded corners
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 245, 245));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        return button;
    }
}