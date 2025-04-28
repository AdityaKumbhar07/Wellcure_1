package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ui.admin.AdminPage;
import ui.user.UserLoginPage;
import ui.user.UserRegistrationPage;

public class StartWindow {


    public StartWindow() {
        // JFrame for the Start Window
        JFrame frame = new JFrame("Welcome to WellCure");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Center panel with buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));  // 4 buttons in a vertical layout

        // Create buttons
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton adminLoginButton = new JButton("Admin Login");
        JButton exitButton = new JButton("Exit");

        // Add buttons to the panel
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(adminLoginButton);
        panel.add(exitButton);

        // Add panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Login Button Action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to login page
                UserLoginPage.login();
                frame.setVisible(false); // Close the start window
            }
        });

        // Register Button Action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to registration page
                UserRegistrationPage.Registration();
                frame.setVisible(false); // Close the start window
            }
        });

        // Admin Login Button Action
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to admin login page
                AdminPage.admin();
                frame.setVisible(false); // Close the start window
            }
        });

        // Exit Button Action
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Close the application
            }
        });

        // Make the start window visible
        frame.setVisible(true);
    }
}