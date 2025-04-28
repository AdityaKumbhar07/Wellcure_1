package ui.user;

import ui.StartWindow;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import database.DBconnection;

public class UserHome {

    public static void UserHome(String username) {
        System.out.println("User Home Page");
        Scanner sc = new Scanner(System.in);


        while (true) {
            System.out.println("\n--- User Home Screen ---");
            System.out.println("1. View Account");
            System.out.println("2. Go Back to Start Screen");
            System.out.println("3. Upload Prescription (Coming Soon!)");
            System.out.println("4. view medicine");
            System.out.println("5. Exit");
            System.out.println("6. View orders");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    // Call account-related methods
                    System.out.println("\nGoing to Account..."+ username);
                    AccountPage.goToAccount(username);
                    break;

                case 2:
                    // Go back to start screen
                    System.out.println("\nGoing back to Start Screen...");
                    new StartWindow();
                    break;
                case 3:
                    // Placeholder for uploading prescription
                    UploadPrescriptionPage.uploadPrescription(username);
                    System.out.println("\nUpload Prescription feature come!");
                    break;
                case 4:
                    // Display medicine list
                    MedicineList.showMedicineList(username);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                case 6:
                    System.out.println("order page send");
                    OrderPage.showUserOrders(username);
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }


    // Inner class to handle medicine list display
    static class MedicineList {
        public static void showMedicineList(String username) {

            ArrayList<Medicine> medicines = null;
            try {
                medicines = getMedicineData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (medicines.isEmpty()) {
                System.out.println("\nNo medicines available.");
            } else {
                System.out.println("\n--- Available Medicines ---");
                for (Medicine medicine : medicines) {
                    System.out.println(medicine.toString());
                }
            }
        }


        private static ArrayList<Medicine> getMedicineData() throws SQLException {
            ArrayList<Medicine> medicines = new ArrayList<>();
            Connection con = DBconnection.getConnection();
            String sql = "SELECT medicine_name, type, price, stock FROM medicines";
            PreparedStatement ps= con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            try {

                while (rs.next()) {
                    String name = rs.getString("medicine_name");
                    String type = rs.getString("type");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    medicines.add(new Medicine(name, type , price ,  stock));
                }
            } catch (SQLException e) {
                System.out.println("Error fetching medicine data: " + e.getMessage());
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    System.out.println("Error closing database resources: " + e.getMessage());
                }
            }
            return medicines;

        }
    }

    // Inner class to represent a medicine
    static class Medicine {
        String name;
        String type;
        double price;
        int stock;
        public Medicine(String name, String type, double price, int stock){this.name = name; this.type = type; this.price = price; this.stock = stock;}
        public String toString(){return "Name: " + name + ", type: " + type + ", Price: $" + price +", Stock: " + stock; }
        }
    }