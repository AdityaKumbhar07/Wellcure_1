package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import ui.admin.AdminLoginPage;
import ui.user.UserLoginPage;

/**
 * StartWindow class serves as the entry point of the Wellcure Pharmacy Management System.
 * It provides options for users to navigate to either the User Login or Admin Login pages.
 */
public class StartWindow {

    // Simple UI Configuration - Easy to customize
    private static final String WINDOW_TITLE = "Welcome to WellCure";
    private static final int WINDOW_WIDTH = 365;
    private static final int WINDOW_HEIGHT = 350;

    // Colors
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color BUTTON_BG = new Color(44, 43, 43);
    private static final Color BUTTON_FG = new Color(255, 255, 255);
    private static final Color EXIT_COLOR = new Color(70, 70, 70);
    private static final Color EXIT_HOVER_COLOR = Color.RED;

    // Content
    private static final String TITLE_TEXT = "Wellcure";
    private static final String USER_BUTTON_TEXT = "User Login";
    private static final String ADMIN_BUTTON_TEXT = "Admin Login";
    private static final String EXIT_ICON_PATH = "outside thigs/back_button.png";  // Path to exit icon image

    /**
     * Constructor for the StartWindow class.
     */
    public StartWindow() {
        // Create the main window
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null); // Use null layout for direct positioning
        frame.getContentPane().setBackground(BG_COLOR);

        // Exit icon at top left
        JLabel exitLabel = new JLabel();
        try {
            ImageIcon exitIcon = new ImageIcon(EXIT_ICON_PATH);
            // Resize the icon to fit the label
            Image img = exitIcon.getImage();
            Image resizedImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            exitLabel.setIcon(new ImageIcon(resizedImg));
        } catch (Exception e) {
            // Fallback to text if image can't be loaded
            exitLabel.setText("X");
            exitLabel.setFont(new Font("Arial", Font.BOLD, 20));
            exitLabel.setForeground(EXIT_COLOR);
            System.out.println("Error loading exit icon: " + e.getMessage());
        }
        exitLabel.setBounds(10, 10, 50, 50); // x, y, width, height
        frame.add(exitLabel);

        // Title - positioned directly where you want it
        JLabel titleLabel = new JLabel(TITLE_TEXT, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(0, 60, WINDOW_WIDTH, 40); // Positioned higher up
        frame.add(titleLabel);

        // User Login button
        JButton userButton = new JButton(USER_BUTTON_TEXT);
        styleButton(userButton);
        userButton.setBounds((WINDOW_WIDTH - 250)/2, 150, 250, 50); // Centered horizontally
        frame.add(userButton);

        // Admin Login button
        JButton adminButton = new JButton(ADMIN_BUTTON_TEXT);
        styleButton(adminButton);
        adminButton.setBounds((WINDOW_WIDTH - 250)/2, 220, 250, 50); // Centered horizontally
        frame.add(adminButton);

        // Event handlers
        userButton.addActionListener(e -> {
            UserLoginPage.login();
            frame.setVisible(false);
        });

        adminButton.addActionListener(e -> {
            AdminLoginPage.adminlogin();
            frame.setVisible(false);
        });

        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to exit?", "Exit Application",
                    JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exitLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                exitLabel.setForeground(EXIT_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitLabel.setForeground(EXIT_COLOR);
            }
        });

        // Display the window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Apply styling to ui.user.a button
     *
     * @param button The button to style
     */
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(BUTTON_BG);
        button.setForeground(BUTTON_FG);
        button.setFocusPainted(false);

        // Add rounded corners
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
}