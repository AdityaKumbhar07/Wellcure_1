package ui.user;

import database.DBconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class OrderPage {

    public static void showUserOrders(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBconnection.getConnection();
            String sql = "SELECT p.prescription_id, p.image_path, p.status\n" +
                    "FROM prescriptions p\n" +
                    "JOIN users u ON p.user_id = u.user_id\n" +
                    "WHERE u.username = ?\n";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);  // Get user orders by username
            rs = stmt.executeQuery();

            // Display orders
            System.out.println("Your Orders:");
            while (rs.next()) {
                int prescriptionId = rs.getInt("prescription_id");
                String imagePath = rs.getString("image_path");
                String status = rs.getString("status");

                System.out.println("Prescription ID: " + prescriptionId + " | Status: " + status);
                System.out.println("Image Path: " + imagePath);
                System.out.println("-----");

                // Allow the user to send request for 'Draft' orders
                if (status.equalsIgnoreCase("Pending")) {
                    System.out.println("Send request to Admin? (y/n)");
                    Scanner sc = new Scanner(System.in);
                    String response = sc.nextLine();
                    if (response.equalsIgnoreCase("y")) {
                        sendPrescriptionRequest(username,prescriptionId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void sendPrescriptionRequest(String username, int prescription_id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBconnection.getConnection();

            // Insert a new order with status "Pending"
            String sql = "INSERT INTO orders (username, prescription_id, order_status) VALUES (?, ?, 'Requested')";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);  // Pass user ID
            stmt.setInt(2, prescription_id);  // Pass prescription ID

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Order sent to admin successfully!");
            } else {
                System.out.println("Failed to send the order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
