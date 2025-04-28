package ui.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Model.User;
import controller.UserController;
import ui.StartWindow;
import ui.user.UserHomePage;

public class UserLoginPage {

    public static void login() {
        // Create the JFrame for the login page
        JFrame frame = new JFrame("User Login - WellCure");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Title panel at the top
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Welcome to WellCure", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        // Login form panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // Username and Password Fields
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        // Buttons
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        // Adding components to the login panel
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(backButton);

        // Center panel where form is placed
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(loginPanel, BorderLayout.CENTER);

        // Adding center panel to the main frame
        frame.add(centerPanel, BorderLayout.CENTER);

        // Action for Login Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                boolean valid = UserController.loginvalid(username, password);

                if(username.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (UserController.loginvalid(username,password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new UserHomePage(username);
                    frame.dispose(); // Close login window
                }
                else if (!UserController.loginvalid(username,password)) JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);

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
