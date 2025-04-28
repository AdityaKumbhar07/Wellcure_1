package ui.user;

import java.sql.*;
import database.DBconnection;
import java.util.Scanner;

public class UploadPrescriptionPage {

    public static int getUserIdByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int userId = -1; // Default value if no user is found

        try {
            conn = DBconnection.getConnection();
            String sql = "SELECT user_id FROM users WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("user_id");
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

        return userId; // Return -1 if no user is found
    }

    public static void uploadPrescription(String username) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter full path of your prescription image:");
        String image_path = sc.nextLine();

        // Retrieve the user_id based on the username
        int userId = getUserIdByUsername(username);

        if (userId == -1) {
            System.out.println("User not found! Please check your username.");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBconnection.getConnection();
            String sql = "INSERT INTO prescriptions (user_id, image_path, status) VALUES (?, ?, 'Draft')";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId); // Use the retrieved user_id
            stmt.setString(2, image_path);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Prescription uploaded successfully as Draft!");
            } else {
                System.out.println("Failed to upload prescription.");
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


    public void sendPrescriptionRequest(int prescriptionId){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBconnection.getConnection();
            String sql = "UPDATE prescriptions SET status = 'Pending' WHERE prescriptions_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, prescriptionId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Prescription sent for approval!");
            } else {
                System.out.println("Failed to send prescription.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null) stmt.close();
                if(conn!=null) conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}