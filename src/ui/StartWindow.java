package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import ui.admin.AdminLoginPage;
import ui.user.UserLoginPage;
import ui.util.UIConfig;

/**
 * StartWindow class serves as the entry point of the Wellcure Pharmacy Management System.
 * It provides options for users to navigate to either the User Login or Admin Login pages.
 *
 * This class is designed to be highly configurable through the use of constants that control
 * various aspects of the UI appearance and behavior.
 */
public class StartWindow {

    // ==================== UI Configuration Constants ====================

    // Window settings
    private static final String WINDOW_TITLE = "Welcome to WellCure";
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 300;

    // Padding settings
    private static final int PADDING_TOP = 20;
    private static final int PADDING_LEFT = 40;
    private static final int PADDING_BOTTOM = 30;
    private static final int PADDING_RIGHT = 40;

    // Exit button settings
    private static final String EXIT_ICON = "⏻";
    private static final Color EXIT_HOVER_COLOR = new Color(249, 0, 0);

    // Title settings
    private static final String TITLE_TEXT = "Wellcure";
    private static final int TITLE_SPACING = 50;  // Space below title

    // Button settings
    private static final String USER_BUTTON_TEXT = "User Login";
    private static final String ADMIN_BUTTON_TEXT = "Admin Login";
    private static final int BUTTON_SPACING = 30;  // Space between buttons

    // Button colors - can be customized or use defaults from UIConfig
    private static final Color USER_BUTTON_BG = UIConfig.BUTTON_BG;
    private static final Color USER_BUTTON_FG = UIConfig.BUTTON_FG;
    private static final Color ADMIN_BUTTON_BG = UIConfig.BUTTON_BG;
    private static final Color ADMIN_BUTTON_FG = UIConfig.BUTTON_FG;

    /**
     * Constructor for the StartWindow class.
     * Initializes and displays the main application window with navigation options.
     */
    public StartWindow() {
        // Create and configure the main application window
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null); // Center the window on screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Apply the UI styling to the frame
        UIConfig.styleFrame(frame);

        // Create main panel with vertical box layout and padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(PADDING_TOP, PADDING_LEFT, PADDING_BOTTOM, PADDING_RIGHT));

        // Apply the UI styling to the panel
        UIConfig.stylePanel(mainPanel);

        // Add exit icon to top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConfig.PRIMARY_BG);

        JLabel powerIcon = new JLabel(EXIT_ICON);
        powerIcon.setFont(UIConfig.SUBTITLE_FONT);
        powerIcon.setForeground(UIConfig.ACCENT_COLOR);
        topPanel.add(powerIcon, BorderLayout.WEST);
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Add application title
        JLabel titleLabel = new JLabel(TITLE_TEXT);
        UIConfig.styleTitle(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(TITLE_SPACING));

        // Create and add User Login button with custom styling
        JButton loginButton = new JButton(USER_BUTTON_TEXT);
        UIConfig.styleButton(loginButton, USER_BUTTON_BG, USER_BUTTON_FG);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(UIConfig.LARGE_BUTTON_SIZE);
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(BUTTON_SPACING));

        // Create and add Admin Login button with custom styling
        JButton adminLoginButton = new JButton(ADMIN_BUTTON_TEXT);
        UIConfig.styleButton(adminLoginButton, ADMIN_BUTTON_BG, ADMIN_BUTTON_FG);
        adminLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminLoginButton.setMaximumSize(UIConfig.LARGE_BUTTON_SIZE);
        mainPanel.add(adminLoginButton);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // ==================== Event Handlers ====================

        // User Login button click handler
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to the user login page
                UserLoginPage.login();
                frame.setVisible(false); // Hide the start window
            }
        });

        // Admin Login button click handler
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to the admin login page
                AdminLoginPage.adminlogin();
                frame.setVisible(false); // Hide the start window
            }
        });

        // Exit icon mouse event handlers
        powerIcon.addMouseListener(new MouseAdapter() {
            // Handle click on exit icon
            @Override
            public void mouseClicked(MouseEvent e) {
                // Show confirmation dialog before exiting
                int response = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to exit?", "Exit Application",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0); // Exit the application if confirmed
                }
            }

            // Change cursor and color when mouse enters exit icon
            @Override
            public void mouseEntered(MouseEvent e) {
                powerIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
                powerIcon.setForeground(EXIT_HOVER_COLOR);
            }

            // Reset color when mouse exits the icon
            @Override
            public void mouseExited(MouseEvent e) {
                powerIcon.setForeground(UIConfig.ACCENT_COLOR);
            }
        });

        // Display the window
        frame.setVisible(true);
    }
}