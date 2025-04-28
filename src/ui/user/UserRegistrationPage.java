package ui.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import controller.UserController;
import Model.User;
import ui.StartWindow;

public class UserRegistrationPage {



    public static void Registration() {
        // Create JFrame for Registration Page
        JFrame frame = new JFrame("User Registration - WellCure");
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Title panel at the top
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Welcome to WellCure - Register", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        // Registration form panel
        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new GridLayout(5, 2, 10, 10));

        // Input fields
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JLabel addressLabel = new JLabel("Address:");
        JTextArea addressField = new JTextArea(3, 20);
        JScrollPane addressScroll = new JScrollPane(addressField);

        // Buttons
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        // Adding components to registration panel
        registrationPanel.add(nameLabel);
        registrationPanel.add(nameField);
        registrationPanel.add(usernameLabel);
        registrationPanel.add(usernameField);
        registrationPanel.add(passwordLabel);
        registrationPanel.add(passwordField);
        registrationPanel.add(addressLabel);
        registrationPanel.add(addressScroll);
        registrationPanel.add(registerButton);
        registrationPanel.add(backButton);

        // Center panel where the form is placed
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(registrationPanel, BorderLayout.CENTER);

        // Adding center panel to main frame
        frame.add(centerPanel, BorderLayout.CENTER);

        // Action for Register Button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String address = addressField.getText();

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
