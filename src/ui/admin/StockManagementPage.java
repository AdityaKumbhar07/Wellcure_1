package ui.admin; // Or wherever this page belongs

import database.DBconnection; // Assuming you have this for DB connection

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StockManagementPage {

    // Inner class to represent Medicine data, kept simple
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

        // Getters (needed for display)
        public int getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }

        // Setters (needed for update)
        public void setPrice(double price) { this.price = price; }
        public void setStock(int stock) { this.stock = stock; }
        // Setters for name/type might be needed if you allow updating those too

        @Override
        public String toString() {
            // Simple string representation for display
            return String.format("ID: %-4d | Name: %-20s | Type: %-25s | Price: %-8.2f | Stock: %d",
                    id, name, type, price, stock);
        }
    }

    // --- Main Menu for Stock Management ---
    public static void showStockMenu() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n--- Stock Management ---");
                System.out.println("1. View All Medicine Stock");
                System.out.println("2. Update Medicine Stock/Price");
                System.out.println("3. Go Back");
                System.out.print("Enter your choice: ");

                int choice = -1;
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume the invalid input
                    continue;
                } finally {
                    if (scanner.hasNextLine()) scanner.nextLine(); // Consume newline always
                }


                switch (choice) {
                    case 1:
                        viewStock();
                        break;
                    case 2:
                        updateStock(scanner);
                        break;
                    case 3:
                        System.out.println("Returning to previous menu...");
                        StockManagementPage.showStockMenu();// Exit this menu
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred in the stock menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- View Stock Functionality ---
    private static void viewStock() {
        List<Medicine> medicines = getAllMedicinesFromDB();

        if (medicines == null) {
            System.out.println("Error retrieving medicine list from database.");
        } else if (medicines.isEmpty()) {
            System.out.println("No medicines found in stock.");
        } else {
            System.out.println("\n--- Current Medicine Stock ---");
            System.out.println("-----------------------------------------------------------------------------------------");
            for (Medicine med : medicines) {
                System.out.println(med.toString());
            }
            System.out.println("-----------------------------------------------------------------------------------------");
        }
    }

    // --- Update Stock Functionality ---
    private static void updateStock(Scanner scanner) {
        System.out.println("\n--- Update Medicine Stock/Price ---");
        viewStock(); // Show current stock first for reference

        System.out.print("Enter the ID of the medicine to update: ");
        int idToUpdate = -1;
        try {
            idToUpdate = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid ID. Please enter a number.");
            scanner.next(); // Consume invalid input
            if (scanner.hasNextLine()) scanner.nextLine(); // Consume newline
            return; // Go back to menu
        } finally {
            if (scanner.hasNextLine()) scanner.nextLine(); // Consume newline always
        }


        Medicine medicineToUpdate = getMedicineByIdFromDB(idToUpdate);

        if (medicineToUpdate == null) {
            System.out.println("Medicine with ID " + idToUpdate + " not found.");
            return;
        }

        System.out.println("Selected: " + medicineToUpdate.toString());

        // Update Price
        System.out.print("Enter new price (or press Enter to keep current: " + medicineToUpdate.getPrice() + "): ");
        String newPriceStr = scanner.nextLine().trim();
        double newPrice = medicineToUpdate.getPrice(); // Default to current
        if (!newPriceStr.isEmpty()) {
            try {
                newPrice = Double.parseDouble(newPriceStr);
                if (newPrice < 0) {
                    System.out.println("Price cannot be negative. Keeping current price.");
                    newPrice = medicineToUpdate.getPrice();
                } else {
                    medicineToUpdate.setPrice(newPrice);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format. Keeping current price.");
                newPrice = medicineToUpdate.getPrice(); // Revert if invalid
            }
        }


        // Update Stock
        System.out.print("Enter new stock quantity (or press Enter to keep current: " + medicineToUpdate.getStock() + "): ");
        String newStockStr = scanner.nextLine().trim();
        int newStock = medicineToUpdate.getStock(); // Default to current
        if (!newStockStr.isEmpty()) {
            try {
                newStock = Integer.parseInt(newStockStr);
                if (newStock < 0) {
                    System.out.println("Stock cannot be negative. Keeping current stock.");
                    newStock = medicineToUpdate.getStock();
                } else {
                    medicineToUpdate.setStock(newStock);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid stock format. Keeping current stock.");
                newStock = medicineToUpdate.getStock(); // Revert if invalid
            }
        }


        // Perform the database update
        if (updateMedicineInDB(medicineToUpdate)) {
            System.out.println("Medicine ID " + medicineToUpdate.getId() + " updated successfully.");
        } else {
            System.out.println("Failed to update medicine ID " + medicineToUpdate.getId() + ".");
        }
    }

    // --- Database Interaction Methods (Direct JDBC) ---

    private static List<Medicine> getAllMedicinesFromDB() {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT medicine_id, medicine_name, type, price, stock FROM medicines ";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (con == null) {
                System.err.println("DB Connection failed in getAllMedicinesFromDB.");
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
            return null; // Indicate error
        }
    }

    private static Medicine getMedicineByIdFromDB(int id) {
        // Corrected SQL query using medicine_id in WHERE clause
        String sql = "SELECT medicine_id, medicine_name, type, price, stock FROM medicines WHERE medicine_id = ?";
        Medicine medicine = null;

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            if (con == null) {
                System.err.println("DB Connection failed in getMedicineByIdFromDB.");
                return null;
            }

            pstmt.setInt(1, id); // Set the ID parameter
            try (ResultSet rs = pstmt.executeQuery()) { // Line 235 where error occurred
                if (rs.next()) {
                    // Retrieve data using the correct column names
                    medicine = new Medicine(
                            rs.getInt("medicine_id"),
                            rs.getString("medicine_name"),
                            rs.getString("type"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    );
                }
            }
            return medicine; // Will be null if not found

        } catch (SQLException e) {
            // Error message now makes sense - 'id' was unknown
            System.err.println("SQL Error fetching medicine by ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null; // Indicate error
        }
    }

    private static boolean updateMedicineInDB(Medicine medicine) {
        // Corrected SQL query using medicine_id in WHERE clause
        String sql = "UPDATE medicines SET price = ?, stock = ? WHERE medicine_id = ?";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            if (con == null) {
                System.err.println("DB Connection failed in updateMedicineInDB.");
                return false;
            }

            pstmt.setDouble(1, medicine.getPrice());
            pstmt.setInt(2, medicine.getStock());
            // Use the ID from the Medicine object, which corresponds to medicine_id
            pstmt.setInt(3, medicine.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was updated

        } catch (SQLException e) {
            System.err.println("SQL Error updating medicine ID " + medicine.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return false; // Indicate error
        }
    }

    // --- Main method for testing this page directly (optional) ---
//    public static void main(String[] args) {
//        // This allows you to run just this page for testing
//        System.out.println("Testing Stock Management Page...");
//        showStockMenu();
//        System.out.println("Exiting Stock Management Test.");
//    }
}