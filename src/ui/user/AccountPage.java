package ui.user;

import database.DBconnection;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AccountPage displays the user's account information.
 * It retrieves and shows details like name, username, and address from the database.
 */
public class AccountPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Account Details - WellCure";
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 400;

    // Colors
    private static final Color HEADER_BG = new Color(44, 43, 43);  // Dark header background
    private static final Color HEADER_FG = Color.WHITE;            // White text for header
    private static final Color FIELD_BG = new Color(245, 245, 245); // Light gray for field backgrounds
    private static final Color LABEL_FG = new Color(70, 70, 70);   // Dark gray for labels
    private static final Color VALUE_FG = new Color(30, 30, 30);   // Darker for values
    private static final Color ACCENT_COLOR = new Color(70, 130, 180); // Steel blue accent

    // Content settings
    private static final String TITLE_TEXT = "Account Details";
    private static final String BACK_BUTTON_TEXT = "Back";
    private static final String BACK_ICON_PATH = "outside thigs/U_back.png";  // Path to back icon image
    private static final String NAME_LABEL = "Name";
    private static final String USERNAME_LABEL = "Username";
    private static final String ADDRESS_LABEL = "Address";
    private static final String EMAIL_LABEL = "Email";

    // Spacing settings
    private static final int FIELD_SPACING = 15;
    private static final int SECTION_SPACING = 20;
    private static final int LABEL_WIDTH = 100;
    private static final int VALUE_WIDTH = 300;

    /**
     * Displays the account details page for the specified user.
     * Redesigned with improved alignment and modern aesthetics.
     *
     * @param username The username of the logged-in user
     */
    public static void goToAccount(String username) {
        // Create frame for Account details page
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the current window
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Create a stylish header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Back button in header
        JButton backButton = new JButton();
        try {
            ImageIcon backIcon = new ImageIcon(BACK_ICON_PATH);
            Image img = backIcon.getImage();
            Image resizedImg = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            backButton.setIcon(new ImageIcon(resizedImg));
        } catch (Exception e) {
            backButton.setText("←");
            backButton.setForeground(HEADER_FG);
        }
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);

        // Title with username
        JLabel titleLabel = new JLabel(TITLE_TEXT + ": " + username);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(HEADER_FG);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add components to header
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Add header to frame
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        contentPanel.setBackground(Color.WHITE);

        // Create a panel for user details with a grid layout for better alignment
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // GridBagConstraints for positioning components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Fetch user details from DB
        String sql = "SELECT name, username, address, email FROM users WHERE username=?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                JOptionPane.showMessageDialog(frame, "Database connection error. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ps.setString(1, username); // Set the username parameter

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Row 1: Name
                    addFieldRow(detailsPanel, gbc, 0, NAME_LABEL, rs.getString("name"));

                    // Row 2: Username
                    addFieldRow(detailsPanel, gbc, 1, USERNAME_LABEL, rs.getString("username"));

                    // Row 3: Address
                    addFieldRow(detailsPanel, gbc, 2, ADDRESS_LABEL, rs.getString("address"));

                    // Row 4: Email (if exists)
                    try {
                        String email = rs.getString("email");
                        if (email != null && !email.isEmpty()) {
                            addFieldRow(detailsPanel, gbc, 3, EMAIL_LABEL, email);
                        }
                    } catch (SQLException ex) {
                        // Email column might not exist yet, ignore this exception
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "No account details found for username: " + username, "No Data", JOptionPane.WARNING_MESSAGE);
                    frame.dispose();
                    return;
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "SQL Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Log for debugging purposes
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Unexpected error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Add the details panel to the content panel with some vertical spacing
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(detailsPanel);
        contentPanel.add(Box.createVerticalGlue()); // Push everything up

        // Add content panel to frame
        frame.add(contentPanel, BorderLayout.CENTER);

        // Back button action
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current window
            new UserHomePage(username).showUserHomePage(); // Go back to User Home Page
        });

        // Show frame
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true);
    }

    /**
     * Helper method to add a field row with label and value to the details panel.
     *
     * @param panel The panel to add the row to
     * @param gbc The GridBagConstraints to use for positioning
     * @param row The row number (0-based)
     * @param labelText The text for the label
     * @param valueText The text for the value
     */
    private static void addFieldRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, String valueText) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.NONE;

        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(LABEL_FG);
        panel.add(label, gbc);

        // Value
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel value = new JLabel(valueText);
        value.setFont(new Font("Arial", Font.PLAIN, 14));
        value.setForeground(VALUE_FG);

        // Create a panel with background for the value
        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setBackground(FIELD_BG);
        valuePanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        valuePanel.add(value, BorderLayout.CENTER);

        panel.add(valuePanel, gbc);
    }
}