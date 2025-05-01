package ui.user;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import Model.User;
import controller.UserController;
import ui.StartWindow;
import ui.util.UIConfig;

/**
 * UserLoginPage provides the login interface for regular users.
 * It allows users to authenticate with their username and password,
 * or navigate to the registration page to create a new account.
 */
public class UserLoginPage {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "User Login - WellCure";
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 450;

    // Content settings
    private static final String TITLE_TEXT = "User Login";
    private static final String SUBTITLE_TEXT = "Sign in to continue.";
    private static final String USERNAME_LABEL = "USERNAME";
    private static final String PASSWORD_LABEL = "PASSWORD";
    private static final String LOGIN_BUTTON_TEXT = "Log in";
    private static final String REGISTER_BUTTON_TEXT = "Register";
    private static final String BACK_BUTTON_TEXT = "←";

    // Spacing settings
    private static final int TITLE_SPACING = 10;
    private static final int SUBTITLE_SPACING = 30;
    private static final int FIELD_LABEL_SPACING = 5;
    private static final int FIELD_SPACING = 20;
    private static final int BUTTON_SPACING = 15;

    // Field dimensions
    private static final Dimension FIELD_SIZE = new Dimension(300, 30);

    /**
     * Initializes and displays the user login page.
     */
    public static void login() {
        // Create the JFrame for the login page
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        UIConfig.styleFrame(frame);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        UIConfig.stylePanel(mainPanel);

        // Back button at top left
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConfig.PRIMARY_BG);

        JButton backButton = new JButton(BACK_BUTTON_TEXT);
        backButton.setFont(UIConfig.SUBTITLE_FONT);
        backButton.setForeground(UIConfig.ACCENT_COLOR);
        backButton.setBackground(UIConfig.PRIMARY_BG);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);

        topPanel.add(backButton, BorderLayout.WEST);
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Title and subtitle
        JLabel titleLabel = new JLabel(TITLE_TEXT);
        UIConfig.styleTitle(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(TITLE_SPACING));

        JLabel subtitleLabel = new JLabel(SUBTITLE_TEXT);
        UIConfig.styleSubtitle(subtitleLabel);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(SUBTITLE_SPACING));

        // Username field
        JLabel usernameLabel = new JLabel(USERNAME_LABEL);
        usernameLabel.setFont(UIConfig.REGULAR_FONT);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(usernameLabel);
        mainPanel.add(Box.createVerticalStrut(FIELD_LABEL_SPACING));

        JTextField usernameField = new JTextField();
        UIConfig.styleTextField(usernameField);
        usernameField.setMaximumSize(FIELD_SIZE);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(usernameField);
        mainPanel.add(Box.createVerticalStrut(FIELD_SPACING));

        // Password field
        JLabel passwordLabel = new JLabel(PASSWORD_LABEL);
        passwordLabel.setFont(UIConfig.REGULAR_FONT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createVerticalStrut(FIELD_LABEL_SPACING));

        JPasswordField passwordField = new JPasswordField();
        UIConfig.styleTextField(passwordField);
        passwordField.setMaximumSize(FIELD_SIZE);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(FIELD_SPACING));

        // Login button
        JButton loginButton = new JButton(LOGIN_BUTTON_TEXT);
        UIConfig.styleButton(loginButton);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(UIConfig.BUTTON_SIZE);
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(BUTTON_SPACING));

        // Register button
        JButton registerButton = new JButton(REGISTER_BUTTON_TEXT);
        UIConfig.styleButton(registerButton);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(UIConfig.BUTTON_SIZE);
        mainPanel.add(registerButton);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // ==================== Event Handlers ====================

        // Action for Login Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if(username.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (UserController.loginvalid(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new UserHomePage(username);
                    frame.dispose(); // Close login window
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Register Button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to registration page
                UserRegistrationPage.Registration();
                frame.dispose(); // Close login window
            }
        });

        // Action for Back Button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to StartWindow
                new StartWindow();
                frame.dispose(); // Close login window
            }
        });

        // Make the login page visible
        frame.setLocationRelativeTo(null);  // Center the window on the screen
        frame.setVisible(true);
    }
}