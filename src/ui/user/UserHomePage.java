package ui.user;

import database.DBconnection;
import ui.admin.StockManagementPage;
import ui.util.UIConfig;

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
 * UserHomePage provides the main dashboard for regular users.
 * It offers access to various user functions like account management,
 * prescription uploads, and order history.
 */
public class UserHomePage {
    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "User Home - WellCure";
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 600;

    // Content settings
    private static final String TITLE_TEXT = "Welcome to WellCure";
    private static final String VIEW_ACCOUNT_TEXT = "View Account";
    private static final String UPLOAD_PRESCRIPTION_TEXT = "Upload Prescription";
    private static final String ORDER_HISTORY_TEXT = "Order History";
    private static final String LOGOUT_TEXT = "Logout";
    private static final String HELP_TEXT = "Help";
    private static final String AVAILABLE_MEDICINES_TEXT = "Available Medicines";

    // Spacing settings
    private static final int TITLE_SPACING = 20;
    private static final int BUTTON_SPACING = 15;
    private static final int PANEL_SPACING = 20;

    // Button dimensions
    private static final Dimension BUTTON_SIZE = new Dimension(200, 40);

    // Panel dimensions
    private static final Dimension LEFT_PANEL_SIZE = new Dimension(250, 500);
    private static final Dimension RIGHT_PANEL_SIZE = new Dimension(600, 500);

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
        userHomeFrame.setLayout(new BorderLayout());
        UIConfig.styleFrame(userHomeFrame);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        UIConfig.stylePanel(mainPanel);

        // Title panel at the top
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(UIConfig.PRIMARY_BG);

        JLabel titleLabel = new JLabel(TITLE_TEXT);
        UIConfig.styleTitle(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(TITLE_SPACING));

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Create a split panel for left navigation and right content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        splitPane.setEnabled(false); // Disable resizing
        splitPane.setBorder(null);

        // Left panel for navigation buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        leftPanel.setBackground(UIConfig.PRIMARY_BG);
        leftPanel.setPreferredSize(LEFT_PANEL_SIZE);

        // Create and add buttons
        JButton viewAccountButton = createStyledButton(VIEW_ACCOUNT_TEXT);
        JButton uploadPrescriptionButton = createStyledButton(UPLOAD_PRESCRIPTION_TEXT);
        JButton orderHistoryButton = createStyledButton(ORDER_HISTORY_TEXT);
        JButton logoutButton = createStyledButton(LOGOUT_TEXT);
        JButton helpButton = createStyledButton(HELP_TEXT);

        leftPanel.add(viewAccountButton);
        leftPanel.add(Box.createVerticalStrut(BUTTON_SPACING));
        leftPanel.add(uploadPrescriptionButton);
        leftPanel.add(Box.createVerticalStrut(BUTTON_SPACING));
        leftPanel.add(orderHistoryButton);
        leftPanel.add(Box.createVerticalStrut(BUTTON_SPACING));
        leftPanel.add(logoutButton);
        leftPanel.add(Box.createVerticalStrut(BUTTON_SPACING));
        leftPanel.add(helpButton);
        leftPanel.add(Box.createVerticalGlue()); // Push buttons to the top

        // Right panel for medicine list
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(UIConfig.PRIMARY_BG);
        rightPanel.setPreferredSize(RIGHT_PANEL_SIZE);

        // Medicine list title
        JLabel medicineListTitle = new JLabel(AVAILABLE_MEDICINES_TEXT);
        UIConfig.styleSubtitle(medicineListTitle);
        medicineListTitle.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(medicineListTitle, BorderLayout.NORTH);

        // Medicine table
        JPanel tablePanel = createMedicineTablePanel();
        rightPanel.add(tablePanel, BorderLayout.CENTER);

        // Add panels to split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        // Add split pane to main panel
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Add main panel to frame
        userHomeFrame.add(mainPanel, BorderLayout.CENTER);

        // ==================== Event Handlers ====================

        // View Account Button Action
        viewAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hide User Home page
                userHomeFrame.setVisible(false);

                // Show AccountPage
                AccountPage.goToAccount(username);
            }
        });

        // Upload Prescription Button Action
        uploadPrescriptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hide User Home page
                userHomeFrame.setVisible(false);

                // Show Prescription Upload page and pass the username
                UploadPrescriptionPage.uploadPrescription(username);
            }
        });

        // Order History Button Action
        orderHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hide User Home page
                userHomeFrame.setVisible(false);

                // Show OrderHistoryPage
                OrderPage.showUserOrders(username);
            }
        });

        // Logout Button Action
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logout functionality here, just dispose the frame
                userHomeFrame.dispose();

                // Redirect to login page
                UserLoginPage.login();
            }
        });

        // Help Button Action
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show help or instructions
                JOptionPane.showMessageDialog(userHomeFrame,
                    "For help, contact support@wellcure.com",
                    "Help", JOptionPane.INFORMATION_MESSAGE);
            }
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
     * Creates a panel containing the medicine table.
     *
     * @return A panel with the medicine table
     */
    private JPanel createMedicineTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConfig.PRIMARY_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            UIConfig.ROUNDED_BORDER,
            new EmptyBorder(10, 10, 10, 10)
        ));

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
        UIConfig.styleTable(medicineTable);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(medicineTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UIConfig.PRIMARY_BG);

        // Add note label
        JLabel noteLabel = new JLabel("Note: This list is for information only. To order, please upload a prescription.");
        noteLabel.setFont(UIConfig.SMALL_FONT);
        noteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noteLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(noteLabel, BorderLayout.SOUTH);

        return panel;
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
     * Creates a styled button with consistent appearance for the application.
     *
     * @param text The text to display on the button
     * @return A configured JButton instance
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        UIConfig.styleButton(button);
        button.setMaximumSize(BUTTON_SIZE);
        button.setPreferredSize(BUTTON_SIZE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}