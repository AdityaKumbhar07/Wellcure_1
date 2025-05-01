package ui.admin;

import database.DBconnection;
import ui.util.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StockManagementPage provides an interface for administrators to manage medicine inventory.
 * It allows viewing, adding, updating, and deleting medicines in the database.
 */
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

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Stock Management - WellCure";
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 600;

    // Content settings
    private static final String VIEW_BUTTON_TEXT = "View All Medicines";
    private static final String ADD_BUTTON_TEXT = "Add New Medicine";
    private static final String UPDATE_BUTTON_TEXT = "Update Medicine";
    private static final String DELETE_BUTTON_TEXT = "Delete Medicine";
    private static final String BACK_BUTTON_TEXT = "Back to Admin Panel";

    // Button dimensions
    private static final Dimension ACTION_BUTTON_SIZE = new Dimension(180, 40);

    // Spacing settings
    private static final int BUTTON_SPACING = 20;

    /**
     * Displays the stock management page with options to view, add, update, and delete medicines.
     */
    public static void showStockManagementPage() {
        JFrame stockFrame = new JFrame(WINDOW_TITLE);
        stockFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        stockFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        stockFrame.setLayout(new BorderLayout());
        UIConfig.styleFrame(stockFrame);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        UIConfig.stylePanel(mainPanel);

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(UIConfig.PRIMARY_BG);

        JLabel titleLabel = new JLabel("Medicine Inventory Management");
        UIConfig.styleTitle(titleLabel);
        titlePanel.add(titleLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTON_SPACING, 20));
        buttonPanel.setBackground(UIConfig.PRIMARY_BG);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Create styled buttons
        JButton viewButton = createStyledButton(VIEW_BUTTON_TEXT);
        JButton addButton = createStyledButton(ADD_BUTTON_TEXT);
        JButton updateButton = createStyledButton(UPDATE_BUTTON_TEXT);
        JButton deleteButton = createStyledButton(DELETE_BUTTON_TEXT);

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

        // Add a content panel for displaying data
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIConfig.PRIMARY_BG);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            UIConfig.ROUNDED_BORDER,
            new EmptyBorder(10, 10, 10, 10)
        ));

        // Add a placeholder message
        JLabel placeholderLabel = new JLabel("Select an option above to manage medicines");
        placeholderLabel.setFont(UIConfig.SUBTITLE_FONT);
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(placeholderLabel, BorderLayout.CENTER);

        // Add panels to main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add a back button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(UIConfig.PRIMARY_BG);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton backButton = createStyledButton(BACK_BUTTON_TEXT);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stockFrame.dispose();
                AdminPage.admin();
            }
        });

        bottomPanel.add(backButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        stockFrame.add(mainPanel, BorderLayout.CENTER);

        // Display the frame
        stockFrame.setLocationRelativeTo(null);
        stockFrame.setVisible(true);
    }

    /**
     * Creates a styled button with consistent appearance.
     *
     * @param text The text to display on the button
     * @return A configured JButton instance
     */
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        UIConfig.styleButton(button);
        button.setPreferredSize(ACTION_BUTTON_SIZE);
        return button;
    }

    /**
     * Displays a dialog for adding a new medicine to the inventory.
     *
     * @param parentFrame The parent frame for the dialog
     */
    private static void showAddMedicineDialog(JFrame parentFrame) {
        // Create styled text fields
        JTextField nameField = new JTextField(20);
        UIConfig.styleTextField(nameField);

        JTextField typeField = new JTextField(20);
        UIConfig.styleTextField(typeField);

        JTextField priceField = new JTextField(10);
        UIConfig.styleTextField(priceField);

        JTextField stockField = new JTextField(10);
        UIConfig.styleTextField(stockField);

        // Create panel with styled labels
        JPanel panel = new JPanel(new GridLayout(0, 2, 15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(UIConfig.PRIMARY_BG);

        JLabel nameLabel = new JLabel("Medicine Name:");
        nameLabel.setFont(UIConfig.REGULAR_FONT);
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(UIConfig.REGULAR_FONT);
        panel.add(typeLabel);
        panel.add(typeField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(UIConfig.REGULAR_FONT);
        panel.add(priceLabel);
        panel.add(priceField);

        JLabel stockLabel = new JLabel("Stock Quantity:");
        stockLabel.setFont(UIConfig.REGULAR_FONT);
        panel.add(stockLabel);
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

    /**
     * Displays a dialog for updating an existing medicine in the inventory.
     *
     * @param parentFrame The parent frame for the dialog
     */
    private static void showUpdateMedicineDialog(JFrame parentFrame) {
        // Create a styled input dialog for medicine ID
        JTextField idField = new JTextField(10);
        UIConfig.styleTextField(idField);

        JPanel idPanel = new JPanel(new BorderLayout(10, 10));
        idPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        idPanel.setBackground(UIConfig.PRIMARY_BG);

        JLabel idLabel = new JLabel("Enter Medicine ID to update:");
        idLabel.setFont(UIConfig.SUBTITLE_FONT);
        idPanel.add(idLabel, BorderLayout.NORTH);
        idPanel.add(idField, BorderLayout.CENTER);

        int idOption = JOptionPane.showConfirmDialog(parentFrame, idPanel, "Update Medicine", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (idOption != JOptionPane.OK_OPTION || idField.getText().trim().isEmpty()) {
            return; // User cancelled or empty input
        }

        try {
            int id = Integer.parseInt(idField.getText().trim());
            Medicine medicine = getMedicineByIdFromDB(id);

            if (medicine == null) {
                JOptionPane.showMessageDialog(parentFrame, "Medicine not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create styled fields pre-filled with current values
            JTextField nameField = new JTextField(medicine.getName(), 20);
            UIConfig.styleTextField(nameField);

            JTextField typeField = new JTextField(medicine.getType(), 20);
            UIConfig.styleTextField(typeField);

            JTextField priceField = new JTextField(String.valueOf(medicine.getPrice()), 10);
            UIConfig.styleTextField(priceField);

            JTextField stockField = new JTextField(String.valueOf(medicine.getStock()), 10);
            UIConfig.styleTextField(stockField);

            // Create panel with styled labels
            JPanel panel = new JPanel(new GridLayout(0, 2, 15, 15));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            panel.setBackground(UIConfig.PRIMARY_BG);

            JLabel titleLabel = new JLabel("Update Medicine (ID: " + id + ")");
            titleLabel.setFont(UIConfig.SUBTITLE_FONT);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(titleLabel);
            panel.add(new JLabel()); // Empty label for grid alignment

            JLabel nameLabel = new JLabel("Medicine Name:");
            nameLabel.setFont(UIConfig.REGULAR_FONT);
            panel.add(nameLabel);
            panel.add(nameField);

            JLabel typeLabel = new JLabel("Type:");
            typeLabel.setFont(UIConfig.REGULAR_FONT);
            panel.add(typeLabel);
            panel.add(typeField);

            JLabel priceLabel = new JLabel("Price:");
            priceLabel.setFont(UIConfig.REGULAR_FONT);
            panel.add(priceLabel);
            panel.add(priceField);

            JLabel stockLabel = new JLabel("Stock Quantity:");
            stockLabel.setFont(UIConfig.REGULAR_FONT);
            panel.add(stockLabel);
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

    /**
     * Displays a dialog for deleting a medicine from the inventory.
     *
     * @param parentFrame The parent frame for the dialog
     */
    private static void showDeleteMedicineDialog(JFrame parentFrame) {
        // Create a styled input dialog for medicine ID
        JTextField idField = new JTextField(10);
        UIConfig.styleTextField(idField);

        JPanel idPanel = new JPanel(new BorderLayout(10, 10));
        idPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        idPanel.setBackground(UIConfig.PRIMARY_BG);

        JLabel idLabel = new JLabel("Enter Medicine ID to delete:");
        idLabel.setFont(UIConfig.SUBTITLE_FONT);
        idPanel.add(idLabel, BorderLayout.NORTH);
        idPanel.add(idField, BorderLayout.CENTER);

        int idOption = JOptionPane.showConfirmDialog(parentFrame, idPanel, "Delete Medicine", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (idOption != JOptionPane.OK_OPTION || idField.getText().trim().isEmpty()) {
            return; // User cancelled or empty input
        }

        try {
            int id = Integer.parseInt(idField.getText().trim());
            Medicine medicine = getMedicineByIdFromDB(id);

            if (medicine == null) {
                JOptionPane.showMessageDialog(parentFrame, "Medicine not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create a styled confirmation dialog
            JPanel confirmPanel = new JPanel();
            confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
            confirmPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            confirmPanel.setBackground(UIConfig.PRIMARY_BG);

            JLabel confirmLabel = new JLabel("Are you sure you want to delete this medicine?");
            confirmLabel.setFont(UIConfig.SUBTITLE_FONT);
            confirmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel idLabel2 = new JLabel("ID: " + medicine.getId());
            idLabel2.setFont(UIConfig.REGULAR_FONT);
            idLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel nameLabel = new JLabel("Name: " + medicine.getName());
            nameLabel.setFont(UIConfig.REGULAR_FONT);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel typeLabel = new JLabel("Type: " + medicine.getType());
            typeLabel.setFont(UIConfig.REGULAR_FONT);
            typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel priceLabel = new JLabel(String.format("Price: $%.2f", medicine.getPrice()));
            priceLabel.setFont(UIConfig.REGULAR_FONT);
            priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel stockLabel = new JLabel("Stock: " + medicine.getStock());
            stockLabel.setFont(UIConfig.REGULAR_FONT);
            stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            confirmPanel.add(confirmLabel);
            confirmPanel.add(Box.createVerticalStrut(15));
            confirmPanel.add(idLabel2);
            confirmPanel.add(Box.createVerticalStrut(5));
            confirmPanel.add(nameLabel);
            confirmPanel.add(Box.createVerticalStrut(5));
            confirmPanel.add(typeLabel);
            confirmPanel.add(Box.createVerticalStrut(5));
            confirmPanel.add(priceLabel);
            confirmPanel.add(Box.createVerticalStrut(5));
            confirmPanel.add(stockLabel);

            int confirm = JOptionPane.showConfirmDialog(parentFrame, confirmPanel,
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

    /**
     * Displays a list of all medicines in the inventory.
     *
     * @param parentFrame The parent frame to display the list in
     */
    /**
     * Displays a list of all medicines in the inventory.
     *
     * @param parentFrame The parent frame to display the list in
     */
    /**
     * Displays a list of all medicines in the inventory.
     *
     * @param parentFrame The parent frame to display the list in
     */
    private static void displayStockList(JFrame parentFrame) {
        // Get the main panel (first component of the content pane)
        Container contentPane = parentFrame.getContentPane();
        if (contentPane.getComponentCount() == 0) {
            System.out.println("No components in content pane");
            return;
        }

        JPanel mainPanel = (JPanel) contentPane.getComponent(0);

        // Find the content panel by its border
        Component[] components = mainPanel.getComponents();
        JPanel contentPanel = null;

        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getBorder() != null &&
                        mainPanel.getLayout() instanceof BorderLayout &&
                        ((BorderLayout)mainPanel.getLayout()).getConstraints(panel) == BorderLayout.CENTER) {
                    contentPanel = panel;
                    break;
                }
            }
        }

        // If content panel not found, try to find it by position
        if (contentPanel == null) {
            for (Component comp : components) {
                if (comp instanceof JPanel && comp != mainPanel.getComponent(0) && comp != mainPanel.getComponent(components.length - 1)) {
                    contentPanel = (JPanel) comp;
                    break;
                }
            }
        }

        // If still not found, create a new one
        if (contentPanel == null) {
            System.out.println("Content panel not found, creating a new one");
            contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createCompoundBorder(
                    UIConfig.ROUNDED_BORDER,
                    new EmptyBorder(10, 10, 10, 10)
            ));
            contentPanel.setBackground(UIConfig.PRIMARY_BG);

            // Remove any existing center component
            for (Component comp : components) {
                if (mainPanel.getLayout() instanceof BorderLayout &&
                        ((BorderLayout)mainPanel.getLayout()).getConstraints(comp) == BorderLayout.CENTER) {
                    mainPanel.remove(comp);
                    break;
                }
            }

            mainPanel.add(contentPanel, BorderLayout.CENTER);
        }

        // Clear the content panel
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        // Fetch medicines from database
        List<Medicine> medicines = getAllMedicinesFromDB();

        if (medicines == null || medicines.isEmpty()) {
            JLabel noDataLabel = new JLabel("No medicines found in stock.");
            noDataLabel.setFont(UIConfig.SUBTITLE_FONT);
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            // Display the medicines in a table
            String[] columns = {"ID", "Name", "Type", "Price", "Stock"};
            String[][] data = new String[medicines.size()][5];

            for (int i = 0; i < medicines.size(); i++) {
                Medicine med = medicines.get(i);
                data[i][0] = String.valueOf(med.getId());
                data[i][1] = med.getName();
                data[i][2] = med.getType();
                data[i][3] = String.format("$%.2f", med.getPrice());
                data[i][4] = String.valueOf(med.getStock());
            }

            JTable medicineTable = new JTable(data, columns);
            UIConfig.styleTable(medicineTable);

            JScrollPane scrollPane = new JScrollPane(medicineTable);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(UIConfig.PRIMARY_BG);

            // Add a title for the table
            JLabel tableTitle = new JLabel("Medicine Inventory");
            tableTitle.setFont(UIConfig.SUBTITLE_FONT);
            tableTitle.setHorizontalAlignment(SwingConstants.CENTER);
            tableTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

            contentPanel.add(tableTitle, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        }

        // Refresh the UI
        contentPanel.revalidate();
        contentPanel.repaint();
        mainPanel.revalidate();
        mainPanel.repaint();
        parentFrame.revalidate();
        parentFrame.repaint();
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