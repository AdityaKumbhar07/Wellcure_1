package ui.user;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import controller.UserController;
import Model.User;
import ui.StartWindow;
import ui.util.UIConfig;

/**
 * UserRegistrationPage provides the interface for new users to register.
 * It collects user information such as name, username, password, email, and address.
 */
public class UserRegistrationPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "User Registration - WellCure";
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 650;

    // Colors
    private static final Color HEADER_BG = new Color(44, 43, 43);  // Dark header background
    private static final Color HEADER_FG = Color.WHITE;            // White text for header
    private static final Color FIELD_BG = new Color(245, 245, 245); // Light gray for field backgrounds
    private static final Color LABEL_FG = new Color(70, 70, 70);   // Dark gray for labels
    private static final Color ACCENT_COLOR = new Color(70, 130, 180); // Steel blue accent
    private static final Color BUTTON_BG = new Color(44, 43, 43);  // Dark button background
    private static final Color BUTTON_FG = Color.WHITE;            // White text for buttons

    // Content settings
    private static final String TITLE_TEXT = "User Registration";
    private static final String NAME_LABEL = "Full Name";
    private static final String USERNAME_LABEL = "Username";
    private static final String PASSWORD_LABEL = "Password";
    private static final String EMAIL_LABEL = "Email";
    private static final String ADDRESS_LABEL = "Address";
    private static final String REGISTER_BUTTON_TEXT = "Register";
    private static final String BACK_BUTTON_TEXT = "Back to Login";
    private static final String BACK_ICON_PATH = "outside thigs/U_back.png";  // Path to back icon image

    // Spacing settings
    private static final int FIELD_SPACING = 15;
    private static final int SECTION_SPACING = 25;

    // Field dimensions
    private static final Dimension BUTTON_SIZE = new Dimension(150, 40);

    /**
     * Initializes and displays the user registration page.
     * Redesigned with improved alignment and modern aesthetics.
     */
    public static void Registration() {
        // Create JFrame for Registration Page
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Create a stylish header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Back button in header
        JButton backButton = new JButton();
        try {
            ImageIcon backIcon = new ImageIcon(BACK_ICON_PATH);
            Image img = backIcon.getImage();
            Image resizedImg = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            backButton.setIcon(new ImageIcon(resizedImg));
        } catch (Exception e) {
            backButton.setText("←");
            backButton.setForeground(HEADER_FG);
        }
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);

        // Title with a modern font
        JLabel titleLabel = new JLabel(TITLE_TEXT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(HEADER_FG);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add components to header
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Add header to frame
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        contentPanel.setBackground(Color.WHITE);

        // Create a form panel with GridBagLayout for better alignment
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // GridBagConstraints for positioning components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        // Add form fields
        // NAME field
        addFormField(formPanel, gbc, 0, NAME_LABEL, new JTextField());
        JTextField nameField = (JTextField) ((Container) formPanel.getComponent(1)).getComponent(0);

        // USERNAME field
        addFormField(formPanel, gbc, 2, USERNAME_LABEL, new JTextField());
        JTextField usernameField = (JTextField) ((Container) formPanel.getComponent(3)).getComponent(0);

        // PASSWORD field
        addFormField(formPanel, gbc, 4, PASSWORD_LABEL, new JPasswordField());
        JPasswordField passwordField = (JPasswordField) ((Container) formPanel.getComponent(5)).getComponent(0);

        // EMAIL field
        addFormField(formPanel, gbc, 6, EMAIL_LABEL, new JTextField());
        JTextField emailField = (JTextField) ((Container) formPanel.getComponent(7)).getComponent(0);

        // ADDRESS field
        addFormField(formPanel, gbc, 8, ADDRESS_LABEL, new JTextField());
        JTextField addressField = (JTextField) ((Container) formPanel.getComponent(9)).getComponent(0);

        // Add the form panel to the content panel
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Register button
        JButton registerButton = new JButton(REGISTER_BUTTON_TEXT);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(BUTTON_BG);
        registerButton.setForeground(BUTTON_FG);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(BUTTON_SIZE);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 30, 30), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Add hover effect to register button
        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(ACCENT_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(BUTTON_BG);
            }
        });

        buttonPanel.add(registerButton);
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue()); // Push everything up

        // Add content panel to frame
        frame.add(contentPanel, BorderLayout.CENTER);

        // ==================== Event Handlers ====================

        // Action for Register Button
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String address = addressField.getText();
            String email = emailField.getText();

            // Validate input fields
            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Email validation (basic check)
            if (!email.isEmpty() && !email.contains("@") || !email.contains(".") || !email.contains("com")) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = new User(name, username, password, address, email);
            boolean success = UserController.registervalid(user);

            if (success) {
                JOptionPane.showMessageDialog(frame, "Registration Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();  // Close registration window
                new StartWindow();
            } else {
                JOptionPane.showMessageDialog(frame, "Registration Failed. Username might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action for Back Button
        backButton.addActionListener(e -> {
            // Go back to login page
            UserLoginPage.login();
            frame.dispose(); // Close registration window
        });

        // Make the registration page visible
        frame.setLocationRelativeTo(null);  // Center the window on the screen
        frame.setVisible(true);
    }

    /**
     * Helper method to add a form field with label to the form panel.
     *
     * @param panel The panel to add the field to
     * @param gbc The GridBagConstraints to use for positioning
     * @param row The row number (0-based)
     * @param labelText The text for the label
     * @param field The text field component
     */
    private static void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1.0;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(LABEL_FG);
        panel.add(label, gbc);

        // Field
        gbc.gridy = row + 1;
        gbc.insets = new Insets(2, 5, 10, 5);

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // Create a panel with background for the field
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(FIELD_BG);
        fieldPanel.add(field, BorderLayout.CENTER);

        panel.add(fieldPanel, gbc);

        // Reset insets for next label
        gbc.insets = new Insets(8, 5, 8, 5);
    }
}