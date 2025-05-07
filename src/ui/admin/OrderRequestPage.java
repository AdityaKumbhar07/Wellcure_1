package ui.admin;

import database.DBconnection;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * OrderRequestPage displays all pending orders for admin review.
 * Admins can view prescriptions, confirm orders, or reject orders.
 */
public class OrderRequestPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Order Requests - WellCure";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    // Content settings
    private static final String TITLE_TEXT = "ORDER REQUEST";
    private static final String GO_BACK_TEXT = "GO BACK";
    private static final String VIEW_PRESCRIPTION_TEXT = "View Prescription";
    private static final String CONFIRM_ORDER_TEXT = "Confirm Order";
    private static final String REJECT_ORDER_TEXT = "Reject Order";

    // Table settings
    private static final String[] COLUMN_NAMES = {"Order No", "Order", "Total Price", "Status"};

    // Button dimensions
    private static final Dimension ACTION_BUTTON_SIZE = new Dimension(150, 40);
    private static final Dimension VIEW_BUTTON_SIZE = new Dimension(180, 40);

    // Spacing settings
    private static final int BUTTON_SPACING = 20;

    // Data - Changed from static to instance variables
    private DefaultTableModel tableModel;
    private JTable orderTable;
    private int selectedOrderId = -1;
    private JFrame frame;

    // Static reference to the current instance
    private static OrderRequestPage currentInstance;

    /**
     * Constructor for OrderRequestPage
     */
    public OrderRequestPage() {
        // Store the current instance
        currentInstance = this;

        // Initialize the UI
        initializeUI();
    }

    /**
     * Initializes and displays the order request page.
     */
    public static void order() {
        new OrderRequestPage();
    }

    /**
     * Initialize the user interface
     */
    private void initializeUI() {
        frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLayout(new BorderLayout());
        UIConfig.styleFrame(frame);

        // Top Bar Panel with dots
        JPanel dotsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dotsPanel.setBackground(UIConfig.PRIMARY_BG);
        JLabel dotsLabel = new JLabel("• • • •");
        dotsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dotsPanel.add(dotsLabel);
        dotsPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        frame.add(dotsPanel, BorderLayout.PAGE_START);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        UIConfig.stylePanel(mainPanel);

        // Top Panel with GO BACK button and title centered
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
        topPanel.setBackground(UIConfig.PRIMARY_BG);

        JButton goBackButton = new JButton(GO_BACK_TEXT);
        UIConfig.styleButton(goBackButton);
        goBackButton.setPreferredSize(new Dimension(120, 40));

        JLabel titleLabel = new JLabel(TITLE_TEXT, SwingConstants.CENTER);
        UIConfig.styleTitle(titleLabel);

        topPanel.add(goBackButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Create table model with column names
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Create JTable with the model
        orderTable = new JTable(tableModel);
        UIConfig.styleTable(orderTable);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add selection listener to track selected order
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedOrderId = (int) tableModel.getValueAt(selectedRow, 0);
                } else {
                    selectedOrderId = -1;
                }
            }
        });

        // Add table to ui.user.a scroll pane
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(UIConfig.ROUNDED_BORDER);
        scrollPane.getViewport().setBackground(UIConfig.PRIMARY_BG);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel below the table
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_SPACING, 20));
        buttonsPanel.setBorder(new EmptyBorder(20, 10, 10, 10));
        buttonsPanel.setBackground(UIConfig.PRIMARY_BG);

        JButton viewButton = new JButton(VIEW_PRESCRIPTION_TEXT);
        JButton confirmButton = new JButton(CONFIRM_ORDER_TEXT);
        JButton rejectButton = new JButton(REJECT_ORDER_TEXT);

        // Style buttons
        UIConfig.styleButton(viewButton);
        UIConfig.styleButton(confirmButton);
        UIConfig.styleButton(rejectButton);

        viewButton.setPreferredSize(VIEW_BUTTON_SIZE);
        confirmButton.setPreferredSize(ACTION_BUTTON_SIZE);
        rejectButton.setPreferredSize(ACTION_BUTTON_SIZE);

        buttonsPanel.add(viewButton);
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(rejectButton);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // ==================== Event Handlers ====================

        // Go Back Button Action
        goBackButton.addActionListener(e -> {
            // Set current instance to null before disposing
            currentInstance = null;
            frame.dispose();
            AdminPage.admin();
        });

        // View Prescription Button Action
        viewButton.addActionListener(e -> {
            if (selectedOrderId != -1) {
                viewPrescription(selectedOrderId);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an order first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Confirm Order Button Action
        confirmButton.addActionListener(e -> {
            if (selectedOrderId != -1) {
                // Open the order confirmation page
                OrderConfirmationPage.showOrderConfirmation(selectedOrderId);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an order first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Reject Order Button Action
        rejectButton.addActionListener(e -> {
            if (selectedOrderId != -1) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to reject this order?",
                        "Confirm Rejection", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (rejectOrder(selectedOrderId)) {
                        JOptionPane.showMessageDialog(frame, "Order rejected successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadOrderData();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to reject order.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an order first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Load order data
        loadOrderData();

        // Make the frame visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Load order data from the database.
     */
    private void loadOrderData() {
        // Clear existing data
        tableModel.setRowCount(0);

        try (Connection conn = DBconnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Query to get order data - simplified to not use order_items table
            String sql = "SELECT o.order_id, o.order_status FROM orders o WHERE o.order_status = 'Pending'";


            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    String orderStatus = rs.getString("order_status");
                    // Use ui.user.a placeholder value for total price since we're not calculating it from order_items
                    double totalPrice = 0.0; // Default value

                    // Add row to table model
                    tableModel.addRow(new Object[]{
                            orderId,
                            "Order " + orderId,
                            String.format("$%.2f", totalPrice),
                            orderStatus
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching order data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * View the prescription for ui.user.a specific order.
     *
     * @param orderId The ID of the order
     */
    private void viewPrescription(int orderId) {
        try (Connection conn = DBconnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Query to get prescription image path
            String sql = "SELECT p.image_path FROM prescriptions p " +
                    "JOIN orders o ON p.prescriptions_id = o.prescription_id " +
                    "WHERE o.order_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, orderId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String imagePath = rs.getString("image_path");
                        displayPrescriptionImage(imagePath, orderId);
                    } else {
                        JOptionPane.showMessageDialog(null, "No prescription found for this order.", "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching prescription: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Display the prescription image in ui.user.a new window.
     *
     * @param imagePath The path to the prescription image
     * @param orderId The ID of the order
     */
    private void displayPrescriptionImage(String imagePath, int orderId) {
        JFrame imageFrame = new JFrame("Prescription for Order #" + orderId);
        imageFrame.setSize(900, 800);
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        UIConfig.styleFrame(imageFrame);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        UIConfig.stylePanel(mainPanel);

        // Title
        JLabel titleLabel = new JLabel("Prescription for Order #" + orderId, SwingConstants.CENTER);
        UIConfig.styleTitle(titleLabel);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Image
        JLabel imageLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            // Scale image if it's too large
            if (icon.getIconWidth() > 550 || icon.getIconHeight() > 700) {
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(550, 700, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
            }
            imageLabel.setIcon(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            imageLabel.setText("Error loading image: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(imageLabel);
        scrollPane.setBorder(UIConfig.ROUNDED_BORDER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Close");
        UIConfig.styleButton(closeButton);
        closeButton.addActionListener(e -> imageFrame.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(UIConfig.PRIMARY_BG);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        imageFrame.add(mainPanel);
        imageFrame.setLocationRelativeTo(null);
        imageFrame.setVisible(true);
    }

    /**
     * Reject an order.
     *
     * @param orderId The ID of the order to reject
     * @return true if the order was successfully rejected, false otherwise
     */
    private boolean rejectOrder(int orderId) {
        try (Connection conn = DBconnection.getConnection()) {
            if (conn == null) {
                return false;
            }

            String sql = "UPDATE orders SET order_status = 'Rejected' WHERE order_id = ?";

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
     * Refresh the order data in the table.
     * This method is called after an order is confirmed or rejected.
     */
    public static void refreshOrderData() {
        // Only refresh if there's ui.user.a current instance
        if (currentInstance != null) {
            currentInstance.loadOrderData();
        }
    }
}