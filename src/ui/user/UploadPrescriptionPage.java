package ui.user;

import database.DBconnection;

import javax.swing.*;
import java.io.File;
import java.sql.*;

public class UploadPrescriptionPage {

    public static int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Error fetching user ID.");
        }
        return -1; // Default value if no user is found
    }

    public static void uploadPrescription(String username) {
        // Open file chooser for the user to select a prescription image
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Prescription Image");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getAbsolutePath();

            // Retrieve the user_id based on the username
            int userId = getUserIdByUsername(username);
            if (userId == -1) {
                showErrorMessage("User not found! Please check your username.");
                return;
            }

            // Save the file path in the database (prescriptions table) and create a draft order
            String sql = "INSERT INTO prescriptions (user_id, image_path, status) VALUES (?, ?, 'Draft')";
            try (Connection conn = DBconnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setString(2, imagePath);
                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    // Get the prescription_id of the newly inserted prescription
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        int prescriptionId = rs.getInt(1);
                        createDraftOrder(userId, prescriptionId);
                        showSuccessMessage("Prescription uploaded and draft order created!");
                        new UserHomePage(username);
                    }
                } else {
                    showErrorMessage("Failed to upload prescription.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorMessage("Database error occurred while uploading the prescription.");
            }
        } else {
            showErrorMessage("No file selected!");
        }
    }

    private static void createDraftOrder(int userId, int prescriptionId) {
        String sql = "INSERT INTO orders (user_id, prescription_id, order_status) VALUES (?, ?, 'Draft')";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, prescriptionId);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showSuccessMessage("Prescription uploaded and draft order created!");
            } else {
                showErrorMessage("Failed to create draft order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Error creating draft order.");
        }
    }

    // Utility method to show error message
    private static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Utility method to show success message
    private static void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
