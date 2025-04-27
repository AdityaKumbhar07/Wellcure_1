package ui.user;

import database.DBconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountPage {

    public static void goToAccount(String username) {

        System.out.println("Fetching Account Details for: " + username);

        String sql = "SELECT name, username, address FROM users WHERE username=?"; // Select specific columns

        // Use try-with-resources for automatic closing
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                System.err.println("Failed to get database connection.");
                System.out.println("Unable to load details - Database connection error.");
                return; // Exit if no connection
            }

            ps.setString(1, username); // Set the username parameter

            try (ResultSet rs = ps.executeQuery()) { // Also manage ResultSet with try-with-resources

                // *** FIX: Check if a row exists and move cursor ***
                if (rs.next()) {
                    System.out.println("--- Account Details ---");
                    // Now it's safe to get data because rs.next() was true
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Username: " + rs.getString("username")); // Corrected label
                    System.out.println("Address: " + rs.getString("address"));   // Corrected label
                } else {
                    // Handle the case where the username was not found
                    System.out.println("No account details found for username: " + username);
                }

            } // ResultSet rs is automatically closed here

        } catch (SQLException e) {
            System.err.println("SQL Error fetching account details: " + e.getMessage());
            e.printStackTrace(); // Log the full error for debugging
            System.out.println("Unable to load details due to a database error.");
        } catch (Exception e) {
            // Catch any other unexpected errors
            System.err.println("Unexpected error fetching account details: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Unable to load details due to an unexpected error.");
        }
        // Connection con and PreparedStatement ps are automatically closed here
    }
}