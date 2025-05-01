package ui.user;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private static final int WINDOW_HEIGHT = 600;

    // Content settings
    private static final String TITLE_TEXT = "Register";
    private static final String NAME_LABEL = "NAME";
    private static final String USERNAME_LABEL = "USERNAME";
    private static final String PASSWORD_LABEL = "PASSWORD";
    private static final String EMAIL_LABEL = "EMAIL";
    private static final String ADDRESS_LABEL = "ADDRESS";
    private static final String REGISTER_BUTTON_TEXT = "Register";
    private static final String BACK_BUTTON_TEXT = "Back to login";

    // Spacing settings
    private static final int TITLE_SPACING = 30;
    private static final int FIELD_LABEL_SPACING = 5;
    private static final int FIELD_SPACING = 15;
    private static final int BUTTON_SPACING = 30;

    // Field dimensions
    private static final Dimension FIELD_SIZE = new Dimension(Integer.MAX_VALUE, 30);

    /**
     * Initializes and displays the user registration page.
     */
    public static void Registration() {
        // Create JFrame for Registration Page
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        UIConfig.styleFrame(frame);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        UIConfig.stylePanel(mainPanel);

        // Title
        JLabel titleLabel = new JLabel(TITLE_TEXT);
        UIConfig.styleTitle(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(TITLE_SPACING));

        // NAME field
        JLabel nameLabel = new JLabel(NAME_LABEL);
        nameLabel.setFont(UIConfig.REGULAR_FONT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameLabel);
        mainPanel.add(Box.createVerticalStrut(FIELD_LABEL_SPACING));

        JTextField nameField = new JTextField();
        UIConfig.styleTextField(nameField);
        nameField.setMaximumSize(FIELD_SIZE);
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameField);
        mainPanel.add(Box.createVerticalStrut(FIELD_SPACING));

        // USERNAME field
        JLabel usernameLabel = new JLabel(USERNAME_LABEL);
        usernameLabel.setFont(UIConfig.REGULAR_FONT);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(usernameLabel);
        mainPanel.add(Box.createVerticalStrut(FIELD_LABEL_SPACING));

        JTextField usernameField = new JTextField();
        UIConfig.styleTextField(usernameField);
        usernameField.setMaximumSize(FIELD_SIZE);
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(usernameField);
        mainPanel.add(Box.createVerticalStrut(FIELD_SPACING));

        // PASSWORD field
        JLabel passwordLabel = new JLabel(PASSWORD_LABEL);
        passwordLabel.setFont(UIConfig.REGULAR_FONT);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createVerticalStrut(FIELD_LABEL_SPACING));

        JPasswordField passwordField = new JPasswordField();
        UIConfig.styleTextField(passwordField);
        passwordField.setMaximumSize(FIELD_SIZE);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(FIELD_SPACING));

        // EMAIL field
        JLabel emailLabel = new JLabel(EMAIL_LABEL);
        emailLabel.setFont(UIConfig.REGULAR_FONT);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(emailLabel);
        mainPanel.add(Box.createVerticalStrut(FIELD_LABEL_SPACING));

        JTextField emailField = new JTextField();
        UIConfig.styleTextField(emailField);
        emailField.setMaximumSize(FIELD_SIZE);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(emailField);
        mainPanel.add(Box.createVerticalStrut(FIELD_SPACING));

        // ADDRESS field
        JLabel addressLabel = new JLabel(ADDRESS_LABEL);
        addressLabel.setFont(UIConfig.REGULAR_FONT);
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(addressLabel);
        mainPanel.add(Box.createVerticalStrut(FIELD_LABEL_SPACING));

        JTextField addressField = new JTextField();
        UIConfig.styleTextField(addressField);
        addressField.setMaximumSize(FIELD_SIZE);
        addressField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(addressField);
        mainPanel.add(Box.createVerticalStrut(BUTTON_SPACING));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setBackground(UIConfig.PRIMARY_BG);

        JButton backButton = new JButton(BACK_BUTTON_TEXT);
        UIConfig.styleButton(backButton);

        JButton registerButton = new JButton(REGISTER_BUTTON_TEXT);
        UIConfig.styleButton(registerButton);

        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalGlue()); // This pushes the register button to the right
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // ==================== Event Handlers ====================

        // Action for Register Button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String address = addressField.getText();
                String email = emailField.getText(); // Get email (though not saved to DB yet)

                // Validate input fields
                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Email validation (basic check)
                if (!email.isEmpty() && !email.contains("@")) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                User user = new User(name, username, password, address);
                boolean success = UserController.registervalid(user);

                if (success) {
                    JOptionPane.showMessageDialog(frame, "Registration Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();  // Close registration window
                    new StartWindow();
                } else {
                    JOptionPane.showMessageDialog(frame, "Registration Failed. Username might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Back Button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to login page
                UserLoginPage.login();
                frame.dispose(); // Close registration window
            }
        });

        // Make the registration page visible
        frame.setLocationRelativeTo(null);  // Center the window on the screen
        frame.setVisible(true);
    }
}