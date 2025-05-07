package ui.user;

import database.DBconnection;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    // Content settings
    private static final String TITLE_PREFIX = "Account Details for: ";
    private static final String BACK_BUTTON_TEXT = "Back";
    private static final String NAME_LABEL_PREFIX = "Name: ";
    private static final String USERNAME_LABEL_PREFIX = "Username: ";
    private static final String ADDRESS_LABEL_PREFIX = "Address: ";
    private static final String EMAIL_LABEL_PREFIX = "Email: ";

    // Spacing settings
    private static final int FIELD_SPACING = 15;
    private static final int SECTION_SPACING = 25;

    /**
     * Displays the account details page for the specified user.
     *
     * @param username The username of the logged-in user
     */
    public static void goToAccount(String username) {
        // Create frame for Account details page
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the current window
        frame.setLayout(new BorderLayout());
        UIConfig.styleFrame(frame);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        UIConfig.stylePanel(mainPanel);

        // Title Label
        JLabel titleLabel = new JLabel(TITLE_PREFIX + username);
        UIConfig.styleTitle(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(SECTION_SPACING));

        // Account details container
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.setBackground(UIConfig.PRIMARY_BG);

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
                    // Create styled labels for user details
                    JLabel nameLabel = new JLabel(NAME_LABEL_PREFIX + rs.getString("name"));
                    nameLabel.setFont(UIConfig.SUBTITLE_FONT);
                    nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel usernameLabel = new JLabel(USERNAME_LABEL_PREFIX + rs.getString("username"));
                    usernameLabel.setFont(UIConfig.SUBTITLE_FONT);
                    usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel addressLabel = new JLabel(ADDRESS_LABEL_PREFIX + rs.getString("address"));
                    addressLabel.setFont(UIConfig.SUBTITLE_FONT);
                    addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    // Add labels to panel with spacing
                    detailsPanel.add(nameLabel);
                    detailsPanel.add(Box.createVerticalStrut(FIELD_SPACING));
                    detailsPanel.add(usernameLabel);
                    detailsPanel.add(Box.createVerticalStrut(FIELD_SPACING));
                    detailsPanel.add(addressLabel);

                    // Check if email column exists and has ui.user.a value
                    try {
                        String email = rs.getString("email");
                        if (email != null && !email.isEmpty()) {
                            detailsPanel.add(Box.createVerticalStrut(FIELD_SPACING));
                            JLabel emailLabel = new JLabel(EMAIL_LABEL_PREFIX + email);
                            emailLabel.setFont(UIConfig.SUBTITLE_FONT);
                            emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                            detailsPanel.add(emailLabel);
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

        // Add the details panel to the main panel
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createVerticalStrut(SECTION_SPACING));

        // Back Button
        JButton backButton = new JButton(BACK_BUTTON_TEXT);
        UIConfig.styleButton(backButton);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(150, 40));

        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current window
            new UserHomePage(username).showUserHomePage(); // Go back to User Home Page
        });

        mainPanel.add(backButton);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Show frame
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true);
    }
}