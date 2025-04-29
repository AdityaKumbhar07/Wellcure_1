package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import ui.admin.AdminLoginPage;
import ui.admin.AdminPage;
import ui.user.UserLoginPage;
import ui.user.UserRegistrationPage;

public class StartWindow {

    public StartWindow() {
        // JFrame for the Start Window
        JFrame frame = new JFrame("Welcome to WellCure");
        frame.setSize(450, 400);
        frame.setLocationRelativeTo(null); // This centers the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 30, 40));

        // Power icon at top left - more stylish
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel powerIcon = new JLabel("⏻");
        powerIcon.setFont(new Font("Arial", Font.BOLD, 24));
        powerIcon.setForeground(new Color(70, 70, 70));
        topPanel.add(powerIcon, BorderLayout.WEST);
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Title
        JLabel titleLabel = new JLabel("Wellcure");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(50));

        // User Login button
        JButton loginButton = new JButton("User Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(250, 50));
        loginButton.setBackground(new Color(248, 248, 248));
        loginButton.setFocusPainted(false);
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(30));

        // Admin Login button
        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.setFont(new Font("Arial", Font.BOLD, 16));
        adminLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminLoginButton.setMaximumSize(new Dimension(250, 50));
        adminLoginButton.setBackground(new Color(230, 230, 230));
        adminLoginButton.setFocusPainted(false);
        mainPanel.add(adminLoginButton);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Login Button Action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to login page
                UserLoginPage.login();
                frame.setVisible(false); // Close the start window
            }
        });

        // Admin Login Button Action
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to admin login page
                AdminLoginPage.adminlogin();
                frame.setVisible(false); // Close the start window
            }
        });

        // Power icon action (exit application) - more stylish
        powerIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to exit?", "Exit Application",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0); // Close the application
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                powerIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
                powerIcon.setForeground(new Color(249, 0, 0)); // Change color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                powerIcon.setForeground(new Color(70, 70, 70)); // Reset color
            }
        });

        // Make the start window visible
        frame.setVisible(true);
    }
}