package ui.admin;

import database.DBconnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockManagementPage {

    static class Medicine {
        int id;
        String name;
        String type;
        double price;
        int stock;

        // Constructor used when fetching from DB
        public Medicine(int id, String name, String type, double price, int stock) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.price = price;
            this.stock = stock;
        }

        // Getters and Setters
        public int getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }

        @Override
        public String toString() {
            return String.format("ID: %-4d | Name: %-20s | Type: %-25s | Price: %-8.2f | Stock: %d",
                    id, name, type, price, stock);
        }
    }

    public static void showStockManagementPage(JFrame parentFrame) {
        JFrame stockFrame = new JFrame("Stock Management");
        stockFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        stockFrame.setSize(800, 500);
        stockFrame.setLayout(new BorderLayout());

        // Panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton viewButton = new JButton("View All Medicines");
        JButton addButton = new JButton("Add New Medicine");
        JButton updateButton = new JButton("Update Medicine");
        JButton deleteButton = new JButton("Delete Medicine");

        // Style buttons
        viewButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Action Listener for View All Medicines Button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display all medicines
                displayStockList(stockFrame);
            }
        });

        // Action Listener for Add New Medicine Button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add new medicine
                showAddMedicineDialog(stockFrame);
            }
        });

        // Action Listener for Update Medicine Button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update medicine details
                showUpdateMedicineDialog(stockFrame);
            }
        });

        // Action Listener for Delete Medicine Button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete medicine
                showDeleteMedicineDialog(stockFrame);
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        stockFrame.add(buttonPanel, BorderLayout.NORTH);

        // Add a back button at the bottom
        JButton backButton = new JButton("Back to Admin Panel");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stockFrame.dispose();
                AdminPage.admin();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);
        stockFrame.add(bottomPanel, BorderLayout.SOUTH);

        stockFrame.setLocationRelativeTo(null);
        stockFrame.setVisible(true);
    }

    // Display Medicine List
    private static void displayStockList(JFrame parentFrame) {
        // Clear any existing content in the center
        if (parentFrame.getContentPane().getComponentCount() > 1) {
            Component centerComponent = ((BorderLayout)parentFrame.getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
            if (centerComponent != null) {
                parentFrame.remove(centerComponent);
            }
        }

        List<Medicine> medicines = getAllMedicinesFromDB();

        if (medicines == null || medicines.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No medicines found in stock.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display the medicines in a table
        String[] columns = {"ID", "Name", "Type", "Price", "Stock"};
        String[][] data = new String[medicines.size()][5];

        for (int i = 0; i < medicines.size(); i++) {
            Medicine med = medicines.get(i);
            data[i][0] = String.valueOf(med.getId());
            data[i][1] = med.getName();
            data[i][2] = med.getType();
            data[i][3] = String.valueOf(med.getPrice());
            data[i][4] = String.valueOf(med.getStock());
        }

        JTable medicineTable = new JTable(data, columns);
        medicineTable.setFont(new Font("Arial", Font.PLAIN, 14));
        medicineTable.setRowHeight(25);
        medicineTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(medicineTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        parentFrame.add(scrollPane, BorderLayout.CENTER);
        parentFrame.revalidate(); // Refresh to display the table
    }

    // Show Add Medicine Dialog
    private static void showAddMedicineDialog(JFrame parentFrame) {
        JTextField nameField = new JTextField(20);
        JTextField typeField = new JTextField(20);
        JTextField priceField = new JTextField(10);
        JTextField stockField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Medicine Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Stock Quantity:"));
        panel.add(stockField);

        int option = JOptionPane.showConfirmDialog(parentFrame, panel, "Add New Medicine", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String type = typeField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());

                if (name.isEmpty() || type.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Name and Type cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (price < 0 || stock < 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Price and Stock cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (addMedicineToDB(name, type, price, stock)) {
                    JOptionPane.showMessageDialog(parentFrame, "Medicine added successfully!");
                    displayStockList(parentFrame); // Refresh the list
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Failed to add medicine.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid input. Please enter valid numbers for Price and Stock.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Show Update Medicine Dialog
    private static void showUpdateMedicineDialog(JFrame parentFrame) {
        // First, get the medicine ID to update
        String idInput = JOptionPane.showInputDialog(parentFrame, "Enter Medicine ID to update:", "Update Medicine", JOptionPane.QUESTION_MESSAGE);
        if (idInput == null || idInput.trim().isEmpty()) {
            return; // User cancelled
        }

        try {
            int id = Integer.parseInt(idInput.trim());
            Medicine medicine = getMedicineByIdFromDB(id);

            if (medicine == null) {
                JOptionPane.showMessageDialog(parentFrame, "Medicine not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create fields pre-filled with current values
            JTextField nameField = new JTextField(medicine.getName(), 20);
            JTextField typeField = new JTextField(medicine.getType(), 20);
            JTextField priceField = new JTextField(String.valueOf(medicine.getPrice()), 10);
            JTextField stockField = new JTextField(String.valueOf(medicine.getStock()), 10);

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            panel.add(new JLabel("Medicine Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Type:"));
            panel.add(typeField);
            panel.add(new JLabel("Price:"));
            panel.add(priceField);
            panel.add(new JLabel("Stock Quantity:"));
            panel.add(stockField);

            int option = JOptionPane.showConfirmDialog(parentFrame, panel, "Update Medicine (ID: " + id + ")", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText().trim();
                    String type = typeField.getText().trim();
                    double price = Double.parseDouble(priceField.getText().trim());
                    int stock = Integer.parseInt(stockField.getText().trim());

                    if (name.isEmpty() || type.isEmpty()) {
                        JOptionPane.showMessageDialog(parentFrame, "Name and Type cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (price < 0 || stock < 0) {
                        JOptionPane.showMessageDialog(parentFrame, "Price and Stock cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    medicine.setName(name);
                    medicine.setType(type);
                    medicine.setPrice(price);
                    medicine.setStock(stock);

                    if (updateMedicineInDB(medicine)) {
                        JOptionPane.showMessageDialog(parentFrame, "Medicine updated successfully!");
                        displayStockList(parentFrame); // Refresh the list
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "Failed to update medicine.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid input. Please enter valid numbers for Price and Stock.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Invalid ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Show Delete Medicine Dialog
    private static void showDeleteMedicineDialog(JFrame parentFrame) {
        String idInput = JOptionPane.showInputDialog(parentFrame, "Enter Medicine ID to delete:", "Delete Medicine", JOptionPane.QUESTION_MESSAGE);
        if (idInput == null || idInput.trim().isEmpty()) {
            return; // User cancelled
        }

        try {
            int id = Integer.parseInt(idInput.trim());
            Medicine medicine = getMedicineByIdFromDB(id);

            if (medicine == null) {
                JOptionPane.showMessageDialog(parentFrame, "Medicine not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "Are you sure you want to delete this medicine?\n" + medicine.toString(),
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (deleteMedicineFromDB(id)) {
                    JOptionPane.showMessageDialog(parentFrame, "Medicine deleted successfully!");
                    displayStockList(parentFrame); // Refresh the list
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Failed to delete medicine.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Invalid ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Database Interaction Methods

    private static List<Medicine> getAllMedicinesFromDB() {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT medicine_id, medicine_name, type, price, stock FROM medicines";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (con == null) {
                System.err.println("DB Connection failed.");
                return null;
            }

            while (rs.next()) {
                medicines.add(new Medicine(
                        rs.getInt("medicine_id"),
                        rs.getString("medicine_name"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
            return medicines;

        } catch (SQLException e) {
            System.err.println("SQL Error fetching all medicines: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static Medicine getMedicineByIdFromDB(int id) {
        String sql = "SELECT medicine_id, medicine_name, type, price, stock FROM medicines WHERE medicine_id = ?";
        Medicine medicine = null;

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    medicine = new Medicine(
                            rs.getInt("medicine_id"),
                            rs.getString("medicine_name"),
                            rs.getString("type"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    );
                }
            }
            return medicine;

        } catch (SQLException e) {
            System.err.println("SQL Error fetching medicine by ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static boolean updateMedicineInDB(Medicine medicine) {
        String sql = "UPDATE medicines SET medicine_name = ?, type = ?, price = ?, stock = ? WHERE medicine_id = ?";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, medicine.getName());
            pstmt.setString(2, medicine.getType());
            pstmt.setDouble(3, medicine.getPrice());
            pstmt.setInt(4, medicine.getStock());
            pstmt.setInt(5, medicine.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error updating medicine ID " + medicine.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static boolean addMedicineToDB(String name, String type, double price, int stock) {
        String sql = "INSERT INTO medicines (medicine_name, type, price, stock) VALUES (?, ?, ?, ?)";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, stock);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error adding medicine: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static boolean deleteMedicineFromDB(int id) {
        String sql = "DELETE FROM medicines WHERE medicine_id = ?";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error deleting medicine ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}