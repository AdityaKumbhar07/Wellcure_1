package ui.admin;

import database.DBconnection;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Desktop;

/**
 * Simple report generator for placed orders
 */
public class ReportGenerator {

    /**
     * Generate a simple Excel report for placed orders using HTML format
     */
    public static void generateOrderReport() {
        try {
            // Create reports directory if it doesn't exist
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdir();
            }

            // Create filename with timestamp
            SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "reports/OrderReport_" + fileNameFormat.format(new Date()) + ".xls";

            // Create HTML file with .xls extension
            FileWriter htmlWriter = new FileWriter(fileName);

            // Start HTML content
            htmlWriter.write("<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel'>");
            htmlWriter.write("<head>");
            htmlWriter.write("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            htmlWriter.write("<title>Order Report</title>");
            htmlWriter.write("<style>");
            htmlWriter.write("table {border-collapse: collapse;}");
            htmlWriter.write("td, th {border: 1px solid black; padding: 5px;}");
            htmlWriter.write("th {background-color: #f2f2f2; font-weight: bold;}");
            htmlWriter.write("</style>");
            htmlWriter.write("</head>");
            htmlWriter.write("<body>");
            htmlWriter.write("<h2>Placed Orders Report</h2>");
            htmlWriter.write("<table>");

            // Write table header
            htmlWriter.write("<tr>");
            htmlWriter.write("<th>Order ID</th>");
            htmlWriter.write("<th>Customer Name</th>");
            htmlWriter.write("<th>Total Amount</th>");
            htmlWriter.write("<th>Payment Method</th>");
            htmlWriter.write("<th>Date</th>");
            htmlWriter.write("</tr>");

            // Get data from database
            Connection conn = DBconnection.getConnection();
            if (conn != null) {
                String sql = "SELECT o.order_id, u.name, o.total_price, o.order_mode, " +
                        "o.order_status, o.transaction_id " +
                        "FROM orders o " +
                        "JOIN users u ON o.user_id = u.user_id " +
                        "WHERE o.order_status = 'Placed' " +
                        "ORDER BY o.order_id";

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String currentDate = dateFormat.format(new Date());

                while (rs.next()) {
                    htmlWriter.write("<tr>");

                    // Order ID
                    htmlWriter.write("<td>" + rs.getInt("order_id") + "</td>");

                    // Customer Name
                    String name = rs.getString("name");
                    htmlWriter.write("<td>" + (name != null ? name : "") + "</td>");

                    // Total Amount
                    htmlWriter.write("<td>₹" + rs.getDouble("total_price") + "</td>");

                    // Payment Method
                    String paymentMethod = rs.getString("order_mode");
                    htmlWriter.write("<td>" + (paymentMethod != null ? paymentMethod : "") + "</td>");

                    // Date (current date as placeholder)
                    htmlWriter.write("<td>" + currentDate + "</td>");

                    htmlWriter.write("</tr>");
                }

                rs.close();
                stmt.close();
                conn.close();
            }

            // End HTML content
            htmlWriter.write("</table>");
            htmlWriter.write("</body>");
            htmlWriter.write("</html>");

            htmlWriter.close();

            // Show success message
            JOptionPane.showMessageDialog(null,
                    "Report generated successfully!\nSaved to: " + fileName,
                    "Report Generated", JOptionPane.INFORMATION_MESSAGE);

            // Try to open the file
            try {
                Desktop.getDesktop().open(new File(fileName));
            } catch (Exception e) {
                System.out.println("Could not open the file automatically: " + e.getMessage());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error generating report: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}