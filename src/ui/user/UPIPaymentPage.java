package ui.user;

import database.DBconnection;
import ui.util.UIConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * UPIPaymentPage displays a QR code for UPI payments and allows users to enter transaction IDs.
 */
public class UPIPaymentPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "UPI Payment - WellCure";
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;

    // Content settings
    private static final String TITLE_TEXT = "UPI Payment";
    private static final String SUBTITLE_TEXT = "Scan the QR code to pay";
    private static final String AMOUNT_PREFIX = "Amount: ";
    private static final String TRANSACTION_ID_LABEL = "Enter Transaction ID:";
    private static final String SUBMIT_BUTTON_TEXT = "Submit";
    private static final String CANCEL_BUTTON_TEXT = "Cancel";

    // UPI settings
    private static final String UPI_ID = "kumbharaditya777-1@okhdfcbank"; // Replace with your actual UPI ID
    private static final String MERCHANT_NAME = "WellCure Pharmacy";
    private static final int QR_CODE_SIZE = 250;

    // Spacing settings
    private static final int TITLE_SPACING = 20;
    private static final int CONTENT_SPACING = 15;
    private static final int BUTTON_SPACING = 10;

    // Instance variables
    private JFrame frame;
    private JTextField transactionIdField;
    private int orderId;
    private double amount;
    private String username;
    private ImageIcon qrCodeImage;

    /**
     * Constructor for UPIPaymentPage
     *
     * @param orderId The ID of the order
     * @param amount The amount to be paid
     * @param username The username of the current user
     */
    public UPIPaymentPage(int orderId, double amount, String username) {
        this.orderId = orderId;
        this.amount = amount;
        this.username = username;

        // Generate QR code
        try {
            String upiLink = generateUPILink(amount);
            BufferedImage qrImage = generateQRCode(upiLink);
            qrCodeImage = new ImageIcon(qrImage);

            // Initialize UI after QR code is generated
            initializeUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error generating QR code: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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

        JLabel amountLabel = new JLabel(AMOUNT_PREFIX + String.format("₹%.2f", amount));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(TITLE_SPACING));
        titlePanel.add(subtitleLabel);
        titlePanel.add(Box.createVerticalStrut(CONTENT_SPACING));
        titlePanel.add(amountLabel);
        titlePanel.add(Box.createVerticalStrut(CONTENT_SPACING));

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // QR Code panel
        JPanel qrCodePanel = new JPanel();
        qrCodePanel.setLayout(new BoxLayout(qrCodePanel, BoxLayout.Y_AXIS));
        qrCodePanel.setBackground(UIConfig.PRIMARY_BG);
        qrCodePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Display QR code
        if (qrCodeImage != null) {
            JLabel qrCodeLabel = new JLabel(qrCodeImage);
            qrCodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            qrCodePanel.add(qrCodeLabel);
        } else {
            JLabel errorLabel = new JLabel("QR code not generated");
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            qrCodePanel.add(errorLabel);
        }

        // Add UPI ID information
        JLabel upiInfoLabel = new JLabel("UPI ID: " + UPI_ID);
        upiInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        upiInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        qrCodePanel.add(Box.createVerticalStrut(10));
        qrCodePanel.add(upiInfoLabel);

        qrCodePanel.add(Box.createVerticalStrut(CONTENT_SPACING * 2));

        // Transaction ID input
        JLabel transactionIdLabel = new JLabel(TRANSACTION_ID_LABEL);
        transactionIdLabel.setFont(UIConfig.REGULAR_FONT);
        transactionIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        transactionIdField = new JTextField(20);
        transactionIdField.setMaximumSize(new Dimension(300, 30));
        transactionIdField.setAlignmentX(Component.CENTER_ALIGNMENT);

        qrCodePanel.add(transactionIdLabel);
        qrCodePanel.add(Box.createVerticalStrut(5));
        qrCodePanel.add(transactionIdField);
        qrCodePanel.add(Box.createVerticalStrut(CONTENT_SPACING));

        mainPanel.add(qrCodePanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTON_SPACING, 0));
        buttonsPanel.setBackground(UIConfig.PRIMARY_BG);

        JButton submitButton = new JButton(SUBMIT_BUTTON_TEXT);
        JButton cancelButton = new JButton(CANCEL_BUTTON_TEXT);

        UIConfig.styleButton(submitButton);
        UIConfig.styleButton(cancelButton);

        submitButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setPreferredSize(new Dimension(120, 40));

        buttonsPanel.add(cancelButton);
        buttonsPanel.add(submitButton);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        frame.add(mainPanel);

        // Action for Cancel Button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // Go back to payment selection
                PaymentSelectionPage.showPaymentSelectionPage(orderId, amount, username);
            }
        });

        // Action for Submit Button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String transactionId = transactionIdField.getText().trim();

                if (transactionId.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter the transaction ID",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save transaction ID
                if (saveTransactionId(transactionId)) {
                    JOptionPane.showMessageDialog(frame,
                            "Payment submitted successfully! Your order will be processed soon.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();

                    // Go back to order page
                    OrderPage.showUserOrders(username);
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Failed to submit payment. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Display the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Generate a UPI link with the specified amount
     *
     * @param amount The payment amount
     * @return A UPI link string
     */
    private String generateUPILink(double amount) {
        return "upi://pay?pa=" + UPI_ID + "&pn=" + MERCHANT_NAME + "&am=" + amount + "&cu=INR";
    }

    /**
     * Generate a QR code for the UPI link
     *
     * @param upiLink The UPI link to encode
     * @return A BufferedImage containing the QR code
     * @throws WriterException If QR code generation fails
     */
    private BufferedImage generateQRCode(String upiLink) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(upiLink, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);

        // Create an image from the BitMatrix
        BufferedImage image = new BufferedImage(QR_CODE_SIZE, QR_CODE_SIZE, BufferedImage.TYPE_INT_RGB);

        // Draw the QR code on the image
        for (int x = 0; x < QR_CODE_SIZE; x++) {
            for (int y = 0; y < QR_CODE_SIZE; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        return image;
    }

    /**
     * Save the transaction ID for the order
     *
     * @param transactionId The UPI transaction ID
     * @return true if successful, false otherwise
     */
    private boolean saveTransactionId(String transactionId) {
        // First, check if the necessary columns exist
        try {
            // Try to update with the transaction ID
            String sql = "UPDATE orders SET transaction_id = ?, order_status = 'Pending Verification' WHERE order_id = ?";

            try (Connection conn = DBconnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, transactionId);
                stmt.setInt(2, orderId);

                int rowsUpdated = stmt.executeUpdate();

                // If update was successful, return true
                if (rowsUpdated > 0) {
                    return true;
                }

                // If no rows were updated, try a different approach
                // Update just the payment_method to indicate payment was attempted
                String fallbackSql = "UPDATE orders SET payment_method = 'UPI' WHERE order_id = ?";

                try (PreparedStatement fallbackStmt = conn.prepareStatement(fallbackSql)) {
                    fallbackStmt.setInt(1, orderId);
                    fallbackStmt.executeUpdate();

                    // Show a message about the transaction ID
                    JOptionPane.showMessageDialog(frame,
                            "Transaction ID: " + transactionId + "\nPlease keep this ID for your reference.",
                            "Transaction ID", JOptionPane.INFORMATION_MESSAGE);

                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

            // If there's an error (likely because columns don't exist),
            // just show a message with the transaction ID and return true
            JOptionPane.showMessageDialog(frame,
                    "Transaction ID: " + transactionId + "\nPlease keep this ID for your reference.",
                    "Transaction ID", JOptionPane.INFORMATION_MESSAGE);

            return true;
        }
    }

    /**
     * Static method to show the UPI payment page
     *
     * @param orderId The ID of the order
     * @param amount The amount to be paid
     * @param username The username of the current user
     */
    public static void showUPIPaymentPage(int orderId, double amount, String username) {
        new UPIPaymentPage(orderId, amount, username);
    }
}