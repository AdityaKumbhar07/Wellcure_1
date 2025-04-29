package ui.admin;

import database.DBconnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class OrderRequestPage {

    private static JTable orderTable;
    private static DefaultTableModel tableModel;
    private static int selectedOrderId = -1;

    public static void order() {
        JFrame frame = new JFrame("Order Requests");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Top Bar Panel with dots
        JPanel dotsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel dotsLabel = new JLabel("• • • •");
        dotsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dotsPanel.add(dotsLabel);
        dotsPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        frame.add(dotsPanel, BorderLayout.PAGE_START);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Top Panel with GO BACK button and title centered
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        JButton goBackButton = new JButton("GO BACK");
        goBackButton.setFont(new Font("Arial", Font.BOLD, 14));
        goBackButton.setPreferredSize(new Dimension(120, 40));
        goBackButton.setBackground(new Color(230, 230, 230));
        goBackButton.setFocusPainted(false);

        JLabel titleLabel = new JLabel("ORDER REQUEST", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        topPanel.add(goBackButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Create table model with column names
        String[] columnNames = {"Order No", "Order", "Total Price", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Create JTable with the model
        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(40); // Taller rows for better readability
        orderTable.setFont(new Font("Arial", Font.PLAIN, 14));
        orderTable.setGridColor(new Color(220, 220, 220));
        orderTable.setShowVerticalLines(true);
        orderTable.setShowHorizontalLines(true);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.setSelectionBackground(new Color(230, 240, 250));
        orderTable.setSelectionForeground(Color.BLACK);

        // Style the table header
        JTableHeader header = orderTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);
        header.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40)); // Taller header

        // Add selection listener to track selected order
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedOrderId = (int) tableModel.getValueAt(selectedRow, 0);
                } else {
                    selectedOrderId = -1;
                }
            }
        });

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel below the table
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonsPanel.setBorder(new EmptyBorder(20, 10, 10, 10));

        JButton viewButton = new JButton("View Prescription");
        JButton confirmButton = new JButton("Confirm Order");
        JButton rejectButton = new JButton("Reject Order");

        // Style buttons
        viewButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        rejectButton.setFont(new Font("Arial", Font.BOLD, 14));

        viewButton.setPreferredSize(new Dimension(180, 40));
        confirmButton.setPreferredSize(new Dimension(150, 40));
        rejectButton.setPreferredSize(new Dimension(150, 40));

        // Add button styling
        viewButton.setBackground(new Color(240, 240, 240));
        confirmButton.setBackground(new Color(240, 240, 240));
        rejectButton.setBackground(new Color(240, 240, 240));

        viewButton.setFocusPainted(false);
        confirmButton.setFocusPainted(false);
        rejectButton.setFocusPainted(false);

        buttonsPanel.add(viewButton);
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(rejectButton);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Load order data
        loadOrderData();

        // Show the frame
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);

        // Action for GO BACK button
        goBackButton.addActionListener(e -> {
            frame.dispose();
            AdminPage.admin();
        });

        // Action listeners for buttons
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedOrderId != -1) {
                    viewPrescription(selectedOrderId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select an order first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedOrderId != -1) {
                    if (confirmOrder(selectedOrderId)) {
                        loadOrderData(); // Refresh the table
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select an order first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedOrderId != -1) {
                    if (rejectOrder(selectedOrderId)) {
                        loadOrderData(); // Refresh the table
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select an order first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private static void loadOrderData() {
        // Clear existing data
        tableModel.setRowCount(0);

        Connection con = DBconnection.getConnection();
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Failed to establish database connection.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Query to get order data - simplified to not use order_items table
        String sql = "SELECT o.order_id, o.order_status FROM orders o";

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String orderStatus = rs.getString("order_status");
                // Use a placeholder value for total price since we're not calculating it from order_items
                double totalPrice = 0.0; // Default value

                // You could set different placeholder prices based on order status if desired
                if ("Confirmed".equals(orderStatus)) {
                    totalPrice = 100.0; // Example placeholder for confirmed orders
                } else if ("Pending".equals(orderStatus)) {
                    totalPrice = 75.0; // Example placeholder for pending orders
                } else {
                    totalPrice = 50.0; // Default placeholder for other statuses
                }

                // Add row to table model
                tableModel.addRow(new Object[]{
                    orderId,
                    "Order " + orderId,
                    String.format("$%.2f", totalPrice),
                    orderStatus
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching order data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void viewPrescription(int orderId) {
        Connection con = DBconnection.getConnection();
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Failed to establish database connection.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT p.image_path FROM prescriptions p " +
                "JOIN orders o ON p.prescriptions_id = o.prescription_id " +
                "WHERE o.order_id = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, orderId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String imagePath = rs.getString("image_path");
                    if (imagePath != null && !imagePath.isEmpty()) {
                        // Display the prescription image
                        ImageIcon icon = new ImageIcon(imagePath);

                        // Scale image if it's too large
                        if (icon.getIconWidth() > 800 || icon.getIconHeight() > 600) {
                            Image img = icon.getImage();
                            Image scaledImg = img.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(scaledImg);
                        }

                        JOptionPane.showMessageDialog(null, new JLabel(icon),
                                "Prescription for Order #" + orderId, JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No prescription image found for this order.",
                                "No Image", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No prescription found for this order.",
                            "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving prescription: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean confirmOrder(int orderId) {
        String sql = "UPDATE orders SET order_status = 'Confirmed' WHERE order_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Order #" + orderId + " has been confirmed!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to confirm order #" + orderId + ".",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error while confirming order: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    private static boolean rejectOrder(int orderId) {
        String sql = "UPDATE orders SET order_status = 'Rejected' WHERE order_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Order #" + orderId + " has been rejected!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to reject order #" + orderId + ".",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error while rejecting order: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}