package ui.admin;

import database.DBconnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderConfirmationPage allows admins to review prescriptions, select medicines,
 * specify quantities, and confirm orders while automatically updating inventory.
 */
public class OrderConfirmationPage {

    // UI Components
    private JFrame frame;
    private JLabel prescriptionImageLabel;
    private JComboBox<Medicine> medicineComboBox;
    private JTextField quantityField;
    private JTable selectedMedicinesTable;
    private DefaultTableModel tableModel;
    private JLabel totalPriceLabel;
    private JButton addButton;
    private JButton removeButton;
    private JButton confirmButton;
    private JButton cancelButton;

    // Data
    private int orderId;
    private String prescriptionImagePath;
    private List<Medicine> availableMedicines;
    private List<OrderItem> selectedItems;
    private double totalPrice;

    /**
     * Class to represent a medicine from the database
     */
    private static class Medicine {
        private int id;
        private String name;
        private String type;
        private double price;
        private int stock;

        public Medicine(int id, String name, String type, double price, int stock) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.price = price;
            this.stock = stock;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }

        @Override
        public String toString() {
            return name + " (" + type + ") - $" + price + " - Stock: " + stock;
        }
    }

    /**
     * Class to represent an item in the order
     */
    private static class OrderItem {
        private Medicine medicine;
        private int quantity;
        private double totalPrice;

        public OrderItem(Medicine medicine, int quantity) {
            this.medicine = medicine;
            this.quantity = quantity;
            this.totalPrice = medicine.getPrice() * quantity;
        }

        public Medicine getMedicine() { return medicine; }
        public int getQuantity() { return quantity; }
        public double getTotalPrice() { return totalPrice; }
    }

    /**
     * Constructor to initialize the order confirmation page
     *
     * @param orderId The ID of the order being confirmed
     */
    public OrderConfirmationPage(int orderId) {
        this.orderId = orderId;
        this.selectedItems = new ArrayList<>();
        this.totalPrice = 0.0;

        // Fetch prescription image path and available medicines
        fetchPrescriptionImagePath();
        fetchAvailableMedicines();

        // Initialize and show the UI
        initializeUI();
    }

    /**
     * Fetch the prescription image path for the given order
     */
    private void fetchPrescriptionImagePath() {
        String sql = "SELECT p.image_path FROM prescriptions p " +
                "JOIN orders o ON p.prescription_id = o.prescription_id " +
                "WHERE o.order_id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    prescriptionImagePath = rs.getString("image_path");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No prescription found for this order.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error fetching prescription: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Fetch all available medicines from the database
     */
    private void fetchAvailableMedicines() {
        availableMedicines = new ArrayList<>();
        String sql = "SELECT medicine_id, medicine_name, type, price, stock FROM medicines WHERE stock > 0";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicine medicine = new Medicine(
                        rs.getInt("medicine_id"),
                        rs.getString("medicine_name"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                availableMedicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error fetching medicines: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Initialize the user interface
     */
    private void initializeUI() {
        // Create main frame
        frame = new JFrame("Order Confirmation - Order #" + orderId);
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top panel with title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Confirm Order #" + orderId);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel);

        // Left panel for prescription image
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        leftPanel.setPreferredSize(new Dimension(400, 500));

        JLabel prescriptionLabel = new JLabel("Prescription");
        prescriptionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(prescriptionLabel, BorderLayout.NORTH);

        // Load and display prescription image
        prescriptionImageLabel = new JLabel();
        if (prescriptionImagePath != null && !prescriptionImagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(prescriptionImagePath);
            // Scale image if it's too large
            if (icon.getIconWidth() > 350 || icon.getIconHeight() > 400) {
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(350, 400, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
            }
            prescriptionImageLabel.setIcon(icon);
        } else {
            prescriptionImageLabel.setText("No prescription image available");
        }

        JScrollPane imageScrollPane = new JScrollPane(prescriptionImageLabel);
        leftPanel.add(imageScrollPane, BorderLayout.CENTER);

        // Right panel for medicine selection and order details
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Medicine selection panel
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Select Medicines"));

        JLabel medicineLabel = new JLabel("Medicine:");
        medicineComboBox = new JComboBox<>();
        for (Medicine medicine : availableMedicines) {
            medicineComboBox.addItem(medicine);
        }

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField("1");

        addButton = new JButton("Add to Order");
        removeButton = new JButton("Remove Selected");

        selectionPanel.add(medicineLabel);
        selectionPanel.add(medicineComboBox);
        selectionPanel.add(quantityLabel);
        selectionPanel.add(quantityField);
        selectionPanel.add(addButton);
        selectionPanel.add(removeButton);

        // Table for selected medicines
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Selected Medicines"));

        String[] columnNames = {"Medicine", "Type", "Price", "Quantity", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0);
        selectedMedicinesTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(selectedMedicinesTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 200));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Total price panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPriceLabel = new JLabel("Total Price: $0.00");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalPriceLabel);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        confirmButton = new JButton("Confirm Order");
        cancelButton = new JButton("Cancel");

        confirmButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setPreferredSize(new Dimension(150, 40));

        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);

        // Add components to right panel
        rightPanel.add(selectionPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(tablePanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(totalPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(buttonsPanel);

        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Add main panel to frame
        frame.add(mainPanel);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMedicineToOrder();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeMedicineFromOrder();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmOrder();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                OrderRequestPage.order();
            }
        });

        // Display the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Add a medicine to the order
     */
    private void addMedicineToOrder() {
        Medicine selectedMedicine = (Medicine) medicineComboBox.getSelectedItem();
        if (selectedMedicine == null) {
            JOptionPane.showMessageDialog(frame,
                    "Please select a medicine.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) {
                throw new NumberFormatException("Quantity must be positive");
            }
            if (quantity > selectedMedicine.getStock()) {
                JOptionPane.showMessageDialog(frame,
                        "Not enough stock available. Maximum available: " + selectedMedicine.getStock(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter a valid quantity.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create new order item
        OrderItem item = new OrderItem(selectedMedicine, quantity);
        selectedItems.add(item);

        // Add to table
        tableModel.addRow(new Object[]{
                selectedMedicine.getName(),
                selectedMedicine.getType(),
                String.format("$%.2f", selectedMedicine.getPrice()),
                quantity,
                String.format("$%.2f", item.getTotalPrice())
        });

        // Update total price
        updateTotalPrice();

        // Reset quantity field
        quantityField.setText("1");
    }

    /**
     * Remove a medicine from the order
     */
    private void removeMedicineFromOrder() {
        int selectedRow = selectedMedicinesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame,
                    "Please select a medicine to remove.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Remove from list and table
        selectedItems.remove(selectedRow);
        tableModel.removeRow(selectedRow);

        // Update total price
        updateTotalPrice();
    }

    /**
     * Update the total price display
     */
    private void updateTotalPrice() {
        totalPrice = 0.0;
        for (OrderItem item : selectedItems) {
            totalPrice += item.getTotalPrice();
        }
        totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
    }

    /**
     * Confirm the order and update the database
     */
    private void confirmOrder() {
        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Please add at least one medicine to the order.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm with user
        int response = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to confirm this order?\nTotal Price: $" + String.format("%.2f", totalPrice),
                "Confirm Order", JOptionPane.YES_NO_OPTION);

        if (response != JOptionPane.YES_OPTION) {
            return;
        }

        // Start database transaction
        Connection conn = null;
        try {
            conn = DBconnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Update order status to Confirmed
            String updateOrderSql = "UPDATE orders SET order_status = 'Confirmed', total_price = ? WHERE order_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateOrderSql)) {
                stmt.setDouble(1,totalPrice);
                stmt.setInt(2, orderId);
                stmt.executeUpdate();
            }

            // 2. Add order items and update stock
//            String insertItemSql = "INSERT INTO order_items (order_id, medicine_id, medicine_price, quantity) VALUES (?, ?, ?, ?)";
            String updateStockSql = "UPDATE medicines SET stock = stock - ? WHERE medicine_id = ?";

            for (OrderItem item : selectedItems) {
                // Insert order item
//                try (PreparedStatement stmt = conn.prepareStatement(insertItemSql)) {
//                    stmt.setInt(1, orderId);
//                    stmt.setInt(2, item.getMedicine().getId());
//                    stmt.setDouble(3, item.getMedicine().getPrice());
//                    stmt.setInt(4, item.getQuantity());
//                    stmt.executeUpdate();
//                }

                // Update stock
                try (PreparedStatement stmt = conn.prepareStatement(updateStockSql)) {
                    stmt.setInt(1, item.getQuantity());
                    stmt.setInt(2, item.getMedicine().getId());
                    stmt.executeUpdate();
                }
            }

            // Commit transaction
            conn.commit();

            JOptionPane.showMessageDialog(frame,
                    "Order #" + orderId + " has been confirmed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Close the window
            frame.dispose();

            // Refresh the order request page
//            OrderRequestPage.refreshOrderData();

        } catch (SQLException e) {
            // Rollback transaction on error
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "Error confirming order: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Reset auto-commit
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Static method to open the order confirmation page
     *
     * @param orderId The ID of the order to confirm
     */
    public static void showOrderConfirmation(int orderId) {
        new OrderConfirmationPage(orderId);
    }
}