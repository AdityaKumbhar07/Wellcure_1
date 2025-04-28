package ui.user;

import database.DBconnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountPage {

    public static void goToAccount(String username) {
        // Create frame for Account details page
        JFrame frame = new JFrame("Account Details - WellCure");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the current window
        frame.setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Account Details for: " + username, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Account details container
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Fetch user details from DB
        String sql = "SELECT name, username, address FROM users WHERE username=?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                JOptionPane.showMessageDialog(frame, "Database connection error. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ps.setString(1, username); // Set the username parameter

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detailsPanel.add(new JLabel("Name: " + rs.getString("name")));
                    detailsPanel.add(new JLabel("Username: " + rs.getString("username")));
                    detailsPanel.add(new JLabel("Address: " + rs.getString("address")));
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

        // Add the details panel to the frame
        frame.add(detailsPanel, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current window
            new UserHomePage(username).showUserHomePage(); // Go back to User Home Page
        });
        frame.add(backButton, BorderLayout.SOUTH);

        // Show frame
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true);
    }
}
