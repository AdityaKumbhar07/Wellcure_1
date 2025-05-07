package ui.user;

import database.DBconnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserHomePage provides the main dashboard for regular users.
 * It offers access to various user functions like account management,
 * prescription uploads, and order history, along with ui.user.a display of available medicines.
 * This version uses absolute positioning for precise component placement.
 */
public class UserHomePage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "User Home - WellCure";
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 600;

    // Colors - Customize these to change the color scheme
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color SECONDARY_TEXT_COLOR = new Color(100, 100, 100);
    private static final Color BUTTON_BG = new Color(240, 240, 240);
    private static final Color BUTTON_FG = new Color(50, 50, 50);
    private static final Color PANEL_BORDER = new Color(220, 220, 220);
    private static final Color TABLE_HEADER_BG = new Color(240, 240, 240);
    private static final Color TABLE_HEADER_FG = new Color(50, 50, 50);

    // Fonts - Customize these to change the text appearance
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);

    // Content settings
    private static final String TITLE_TEXT = "Welcome to WellCure";
    private static final String MEDICINES_TITLE = "Available Medicines";
    private static final String VIEW_ACCOUNT_TEXT = "View Account";
    private static final String UPLOAD_PRESCRIPTION_TEXT = "Upload Prescription";
    private static final String ORDER_HISTORY_TEXT = "Order History";
    private static final String LOGOUT_TEXT = "Logout";
    private static final String HELP_TEXT = "Help";
    private static final String NOTE_TEXT = "Note: This list is for information only. To order, please upload ui.user.a prescription.";

    // Component dimensions
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int CORNER_RADIUS = 10;  // Rounded corners for components

    // Component positions - Adjust these to change the layout
    // Left panel (navigation)
    private static final int LEFT_PANEL_X = 20;
    private static final int LEFT_PANEL_Y = 80;
    private static final int LEFT_PANEL_WIDTH = 220;
    private static final int LEFT_PANEL_HEIGHT = 480;
    private static final int NAV_BUTTON_X = 30;
    private static final int FIRST_NAV_BUTTON_Y = 100;
    private static final int NAV_BUTTON_SPACING = 60;

    // Right panel (medicine list)
    private static final int RIGHT_PANEL_X = 260;
    private static final int RIGHT_PANEL_Y = 80;
    private static final int RIGHT_PANEL_WIDTH = 620;
    private static final int RIGHT_PANEL_HEIGHT = 480;
    private static final int MEDICINES_TITLE_Y = 90;
    private static final int TABLE_Y = 130;
    private static final int TABLE_HEIGHT = 380;
    private static final int NOTE_Y = 520;

    private JFrame userHomeFrame;

    /**
     * Medicine class to represent medicine data
     */
    static class Medicine {
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
    }

    /**
     * Constructor for the UserHomePage class.
     *
     * @param username The username of the logged-in user
     */
    public UserHomePage(String username) {
        // Create the User Home Frame
        userHomeFrame = new JFrame(WINDOW_TITLE);
        userHomeFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        userHomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userHomeFrame.getContentPane().setBackground(BG_COLOR);

        // Main panel with absolute positioning (null layout)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null); // Use null layout for absolute positioning
        mainPanel.setBackground(BG_COLOR);

        // Title at the top
        JLabel titleLabel = new JLabel(TITLE_TEXT, JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(0, 20, WINDOW_WIDTH, 40);
        mainPanel.add(titleLabel);

        // Create navigation buttons
        JButton viewAccountButton = createRoundedButton(VIEW_ACCOUNT_TEXT);
        JButton uploadPrescriptionButton = createRoundedButton(UPLOAD_PRESCRIPTION_TEXT);
        JButton orderHistoryButton = createRoundedButton(ORDER_HISTORY_TEXT);
        JButton logoutButton = createRoundedButton(LOGOUT_TEXT);
        JButton helpButton = createRoundedButton(HELP_TEXT);

        // Position navigation buttons
        viewAccountButton.setBounds(NAV_BUTTON_X, FIRST_NAV_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        uploadPrescriptionButton.setBounds(NAV_BUTTON_X, FIRST_NAV_BUTTON_Y + NAV_BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        orderHistoryButton.setBounds(NAV_BUTTON_X, FIRST_NAV_BUTTON_Y + (2 * NAV_BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT);
        logoutButton.setBounds(NAV_BUTTON_X, FIRST_NAV_BUTTON_Y + (3 * NAV_BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT);
        helpButton.setBounds(NAV_BUTTON_X, FIRST_NAV_BUTTON_Y + (4 * NAV_BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT);

        // Add navigation buttons to panel
        mainPanel.add(viewAccountButton);
        mainPanel.add(uploadPrescriptionButton);
        mainPanel.add(orderHistoryButton);
        mainPanel.add(logoutButton);
        mainPanel.add(helpButton);

        // Create medicine list panel with border
        JPanel medicinePanel = createRoundedPanel();
        medicinePanel.setBounds(RIGHT_PANEL_X, RIGHT_PANEL_Y, RIGHT_PANEL_WIDTH, RIGHT_PANEL_HEIGHT);
        medicinePanel.setLayout(null); // Use null layout for absolute positioning

        // Medicine list title
        JLabel medicineListTitle = new JLabel(MEDICINES_TITLE, JLabel.CENTER);
        medicineListTitle.setFont(SUBTITLE_FONT);
        medicineListTitle.setForeground(TEXT_COLOR);
        medicineListTitle.setBounds(0, 10, RIGHT_PANEL_WIDTH, 30);
        medicinePanel.add(medicineListTitle);

        // Create medicine table
        JTable medicineTable = createMedicineTable();
        JScrollPane scrollPane = new JScrollPane(medicineTable);
        scrollPane.setBounds(10, 50, RIGHT_PANEL_WIDTH - 20, TABLE_HEIGHT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        medicinePanel.add(scrollPane);

        // Add note about medicine list
        JLabel noteLabel = new JLabel(NOTE_TEXT, JLabel.CENTER);
        noteLabel.setFont(SMALL_FONT);
        noteLabel.setForeground(SECONDARY_TEXT_COLOR);
        noteLabel.setBounds(10, RIGHT_PANEL_HEIGHT - 30, RIGHT_PANEL_WIDTH - 20, 20);
        medicinePanel.add(noteLabel);

        // Add medicine panel to main panel
        mainPanel.add(medicinePanel);

        // Add main panel to frame
        userHomeFrame.add(mainPanel);

        // ==================== Event Handlers ====================

        // View Account Button Action
        viewAccountButton.addActionListener(e -> {
            // Hide User Home page
            userHomeFrame.setVisible(false);

            // Show AccountPage
            AccountPage.goToAccount(username);
        });

        // Upload Prescription Button Action
        uploadPrescriptionButton.addActionListener(e -> {
            // Hide User Home page
            userHomeFrame.setVisible(false);

            // Show Prescription Upload page and pass the username
            UploadPrescriptionPage.uploadPrescription(username);
        });

        // Order History Button Action
        orderHistoryButton.addActionListener(e -> {
            // Hide User Home page
            userHomeFrame.setVisible(false);

            // Show OrderHistoryPage
            OrderPage.showUserOrders(username);
        });

        // Logout Button Action
        logoutButton.addActionListener(e -> {
            // Logout functionality here, just dispose the frame
            userHomeFrame.dispose();

            // Redirect to login page
            UserLoginPage.login();
        });

        // Help Button Action
        helpButton.addActionListener(e -> {
            // Show help or instructions
            JOptionPane.showMessageDialog(userHomeFrame,
                    "For help, contact support@wellcure.com",
                    "Help", JOptionPane.INFORMATION_MESSAGE);
        });

        // Display the frame
        userHomeFrame.setLocationRelativeTo(null);
        userHomeFrame.setVisible(true);
    }

    /**
     * Show the User Home page.
     * Makes the user home frame visible.
     */
    public void showUserHomePage() {
        userHomeFrame.setVisible(true);
    }

    /**
     * Creates ui.user.a JTable with medicine data.
     *
     * @return A styled JTable with medicine data
     */
    private JTable createMedicineTable() {
        // Create table model with column names
        String[] columns = {"ID", "Name", "Type", "Price", "Stock"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Fetch medicines from database
        List<Medicine> medicines = getAllMedicinesFromDB();

        if (medicines != null && !medicines.isEmpty()) {
            for (Medicine med : medicines) {
                tableModel.addRow(new Object[]{
                        med.getId(),
                        med.getName(),
                        med.getType(),
                        String.format("$%.2f", med.getPrice()),
                        med.getStock()
                });
            }
        }

        // Create and style table
        JTable medicineTable = new JTable(tableModel);
        medicineTable.setFont(REGULAR_FONT);
        medicineTable.setRowHeight(30);
        medicineTable.setGridColor(PANEL_BORDER);
        medicineTable.setShowGrid(true);

        // Style the header
        medicineTable.getTableHeader().setFont(BUTTON_FONT);
        medicineTable.getTableHeader().setBackground(TABLE_HEADER_BG);
        medicineTable.getTableHeader().setForeground(TABLE_HEADER_FG);

        return medicineTable;
    }

    /**
     * Fetches all medicines from the database.
     *
     * @return A list of Medicine objects
     */
    private List<Medicine> getAllMedicinesFromDB() {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT medicine_id, medicine_name, type, price, stock FROM medicines";

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
                medicines.add(medicine);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching medicines: " + e.getMessage());
            e.printStackTrace();
        }

        return medicines;
    }

    /**
     * Creates ui.user.a button with rounded corners.
     *
     * @param text The text to display on the button
     * @return A styled JButton with rounded corners
     */
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Paint rounded background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

                // Paint text
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };

        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_BG);
        button.setForeground(BUTTON_FG);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        return button;
    }

    /**
     * Creates ui.user.a panel with rounded corners.
     *
     * @return A styled JPanel with rounded corners
     */
    private JPanel createRoundedPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Paint rounded background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);

                // Paint border
                g2.setColor(PANEL_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);

                g2.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setBackground(BG_COLOR);

        return panel;
    }
}