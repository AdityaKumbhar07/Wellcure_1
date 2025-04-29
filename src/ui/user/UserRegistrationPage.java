package ui.user;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import controller.UserController;
import Model.User;
import ui.StartWindow;

public class UserRegistrationPage {



    public static void Registration() {
        // Create JFrame for Registration Page
        JFrame frame = new JFrame("User Registration - WellCure");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Title panel at the top
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30)); // Add spacing

        // Input fields with labels
        // NAME
        JLabel nameLabel = new JLabel("NAME");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameField);
        mainPanel.add(Box.createVerticalStrut(15));

        // USERNAME
        JLabel usernameLabel = new JLabel("USERNAME");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(usernameLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(usernameField);
        mainPanel.add(Box.createVerticalStrut(15));

        // PASSWORD
        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(15));

        // EMAIL (Note: This field is in the UI but not in the database yet)
        JLabel emailLabel = new JLabel("EMAIL");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(emailLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(emailField);
        mainPanel.add(Box.createVerticalStrut(15));

        // ADDRESS
        JLabel addressLabel = new JLabel("ADDRESS");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(addressLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JTextField addressField = new JTextField();
        addressField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        addressField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(addressField);
        mainPanel.add(Box.createVerticalStrut(30));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backButton = new JButton("Back to login");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setBackground(new Color(230, 230, 230));
        backButton.setFocusPainted(false);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setBackground(new Color(230, 230, 230));
        registerButton.setFocusPainted(false);

        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalGlue()); // This pushes the register button to the right
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

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
                // Go back to StartWindow
                new StartWindow();
                frame.dispose(); // Close registration window
            }
        });

        // Make the registration page visible
        frame.setLocationRelativeTo(null);  // Center the window on the screen
        frame.setVisible(true);
    }
}
