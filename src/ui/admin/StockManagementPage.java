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
        public String getType() { return type; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }

        public void setPrice(double price) { this.price = price; }
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
        stockFrame.setSize(600, 400);
        stockFrame.setLayout(new BorderLayout());

        // Panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton viewButton = new JButton("View All Medicines");
        JButton updateButton = new JButton("Update Medicine Stock/Price");

        // Action Listener for View All Medicines Button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display all medicines
                displayStockList(stockFrame);
            }
        });

        // Action Listener for Update Medicine Button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update stock or price logic
                showUpdateMedicineDialog(stockFrame);
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);

        stockFrame.add(buttonPanel, BorderLayout.NORTH);
        stockFrame.setVisible(true);
    }

    // Display Medicine List
    private static void displayStockList(JFrame parentFrame) {
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
        JScrollPane scrollPane = new JScrollPane(medicineTable);
        parentFrame.add(scrollPane, BorderLayout.CENTER);
        parentFrame.revalidate(); // Refresh to display the table
    }

    // Show Update Dialog
    private static void showUpdateMedicineDialog(JFrame parentFrame) {
        JTextField idField = new JTextField(5);
        JTextField priceField = new JTextField(5);
        JTextField stockField = new JTextField(5);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Medicine ID to Update:"));
        panel.add(idField);
        panel.add(new JLabel("Enter New Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Enter New Stock Quantity:"));
        panel.add(stockField);

        int option = JOptionPane.showConfirmDialog(parentFrame, panel, "Update Medicine Details", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());

                Medicine medicine = getMedicineByIdFromDB(id);
                if (medicine != null) {
                    medicine.setPrice(price);
                    medicine.setStock(stock);
                    if (updateMedicineInDB(medicine)) {
                        JOptionPane.showMessageDialog(parentFrame, "Medicine updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "Failed to update medicine.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Medicine not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Database Interaction Methods (Direct JDBC)

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
        String sql = "UPDATE medicines SET price = ?, stock = ? WHERE medicine_id = ?";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setDouble(1, medicine.getPrice());
            pstmt.setInt(2, medicine.getStock());
            pstmt.setInt(3, medicine.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error updating medicine ID " + medicine.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
