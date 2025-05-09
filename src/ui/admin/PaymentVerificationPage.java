package ui.admin;

import database.DBconnection;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * PaymentVerificationPage allows admins to verify payments and update order status.
 */
public class PaymentVerificationPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Payment Verification - WellCure";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    // Content settings
    private static final String TITLE_TEXT = "PAYMENT VERIFICATION";
    private static final String GO_BACK_TEXT = "GO BACK";
    private static final String VERIFY_PAYMENT_TEXT = "Verify Payment";
    private static final String REJECT_PAYMENT_TEXT = "Reject Payment";
    private static final String BACK_ICON_PATH = "outside thigs/U_back.png";  // Path to back icon image

    // Table settings
    private static final String[] COLUMN_NAMES = {"Order ID", "Payment Method", "Transaction ID", "Amount", "Status"};

    // Button dimensions
    private static final Dimension ACTION_BUTTON_SIZE = new Dimension(150, 40);

    // Spacing settings
    private static final int BUTTON_SPACING = 20;

    // Data
    private DefaultTableModel tableModel;
    private JTable paymentTable;
    private int selectedOrderId = -1;
    private JFrame frame;

    /**
     * Constructor for PaymentVerificationPage
     */
    public PaymentVerificationPage() {
        // Initialize the UI
        initializeUI();
    }

    /**
     * Initializes and displays the payment verification page.
     */
    public static void showPaymentVerification() {
        new PaymentVerificationPage();
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

        JButton goBackButton = new JButton();
        try {
            ImageIcon backIcon = new ImageIcon(BACK_ICON_PATH);
            // Resize the icon to fit the button
            Image img = backIcon.getImage();
            Image resizedImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            goBackButton.setIcon(new ImageIcon(resizedImg));
        } catch (Exception e) {
            // Fallback to text if image can't be loaded
            goBackButton.setText(GO_BACK_TEXT);
            System.out.println("Error loading back icon: " + e.getMessage());
        }
        UIConfig.styleButton(goBackButton);
        goBackButton.setPreferredSize(new Dimension(50, 40));
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(UIConfig.PRIMARY_BG);
        backButtonPanel.add(goBackButton);
        topPanel.add(backButtonPanel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel(TITLE_TEXT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIConfig.PRIMARY_BG);

        // Create table model and table
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        paymentTable = new JTable(tableModel);
        paymentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paymentTable.getTableHeader().setReorderingAllowed(false);
        paymentTable.getTableHeader().setResizingAllowed(true);

        // Add selection listener to table
        paymentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = paymentTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedOrderId = (int) tableModel.getValueAt(selectedRow, 0);
                } else {
                    selectedOrderId = -1;
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTON_SPACING, 0));
        buttonsPanel.setBackground(UIConfig.PRIMARY_BG);
        buttonsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton verifyButton = new JButton(VERIFY_PAYMENT_TEXT);
        JButton rejectButton = new JButton(REJECT_PAYMENT_TEXT);

        UIConfig.styleButton(verifyButton);
        UIConfig.styleButton(rejectButton);

        verifyButton.setPreferredSize(ACTION_BUTTON_SIZE);
        rejectButton.setPreferredSize(ACTION_BUTTON_SIZE);

        buttonsPanel.add(verifyButton);
        buttonsPanel.add(rejectButton);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // ==================== Event Handlers ====================

        // Go Back Button Action
        goBackButton.addActionListener(e -> {
            frame.dispose();
            AdminPage.admin();
        });

        // Verify Payment Button Action
        verifyButton.addActionListener(e -> {
            if (selectedOrderId != -1) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to verify this payment?",
                        "Confirm Verification", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (verifyPayment(selectedOrderId)) {
                        JOptionPane.showMessageDialog(frame, "Payment verified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadPaymentData();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to verify payment.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a payment to verify.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Reject Payment Button Action
        rejectButton.addActionListener(e -> {
            if (selectedOrderId != -1) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to reject this payment?",
                        "Confirm Rejection", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (rejectPayment(selectedOrderId)) {
                        JOptionPane.showMessageDialog(frame, "Payment rejected successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadPaymentData();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to reject payment.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a payment to reject.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Load payment data
        loadPaymentData();

        // Make the frame visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Load payment data from the database.
     */
    private void loadPaymentData() {
        // Clear existing data
        tableModel.setRowCount(0);

        try (Connection conn = DBconnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Query to get payment data
            String sql = "SELECT o.order_id, o.order_mode, o.transaction_id, o.total_price, o.order_status " +
                    "FROM orders o WHERE o.order_mode IS NOT NULL AND o.order_status = 'Pending Verification'";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    String paymentMethod = rs.getString("order_mode");
                    String transactionId = rs.getString("transaction_id");
                    double totalPrice = rs.getDouble("total_price");
                    String paymentStatus = rs.getString("order_status");

                    // Add row to table model
                    tableModel.addRow(new Object[]{
                            orderId,
                            paymentMethod,
                            transactionId,
                            String.format("₹%.2f", totalPrice),
                            paymentStatus
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching payment data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Verify a payment and update the order status.
     *
     * @param orderId The ID of the order to verify
     * @return true if the payment was successfully verified, false otherwise
     */
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
     * Reject a payment.
     *
     * @param orderId The ID of the order to reject
     * @return true if the payment was successfully rejected, false otherwise
     */
    private boolean rejectPayment(int orderId) {
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
}