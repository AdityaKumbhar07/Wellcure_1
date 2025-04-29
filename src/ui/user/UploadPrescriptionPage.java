package ui.user;

import database.DBconnection;

import javax.swing.*;
import java.io.File;
import java.sql.*;

public class UploadPrescriptionPage {

    public static void uploadPrescription(String username) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Prescription Image");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getAbsolutePath();

            int userId = getUserIdByUsername(username);
            if (userId == -1) {
                showErrorMessage("User not found!");
                return;
            }

            String sql = "INSERT INTO prescriptions (user_id, image_path, status) VALUES (?, ?, 'Draft')";
            try (Connection conn = DBconnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setString(2, imagePath);
                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        int prescriptionId = rs.getInt(1);
                        createDraftOrder(userId, prescriptionId);
                        showSuccessMessage("Prescription uploaded and draft order created!");

                        // Navigate to the orders page after successful upload
                        OrderPage.showUserOrders(username);
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

    private static int getUserIdByUsername(String username) {
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

    private static void createDraftOrder(int userId, int prescriptionId) {
        String sql = "INSERT INTO orders (user_id, prescription_id, order_status) VALUES (?, ?, 'Draft')";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, prescriptionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Error creating draft order.");
        }
    }

    private static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}