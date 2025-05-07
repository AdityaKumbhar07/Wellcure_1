package ui.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import ui.StartWindow;

/**
 * AdminLoginPage provides the login interface for administrators.
 * It allows administrators to authenticate with their credentials
 * to access the admin panel and management features.
 */
public class AdminLoginPage {

    // Simple UI Configuration - Easy to customize
    private static final String WINDOW_TITLE = "Admin Login - WellCure";
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 450;
    
    // Colors
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color SECONDARY_TEXT_COLOR = new Color(100, 100, 100);
    private static final Color BUTTON_BG = new Color(44, 43, 43);
    private static final Color BUTTON_FG = new Color(255, 255, 255);
    private static final Color FIELD_BORDER = new Color(220, 220, 220);
    private static final Color BACK_COLOR = new Color(70, 70, 70);
    
    // Content
    private static final String TITLE_TEXT = "Admin Login";
    private static final String SUBTITLE_TEXT = "Sign in to continue.";
    private static final String USERNAME_LABEL = "USERNAME";
    private static final String PASSWORD_LABEL = "PASSWORD";
    private static final String LOGIN_BUTTON_TEXT = "Log in";
    private static final String BACK_BUTTON_TEXT = "<--";
    
    // Component positions - Adjust these to change the layout
    private static final int BACK_BUTTON_X = 20;
    private static final int BACK_BUTTON_Y = 20;
    private static final int TITLE_Y = 70;
    private static final int SUBTITLE_Y = 110;
    private static final int USERNAME_LABEL_Y = 160;
    private static final int USERNAME_FIELD_Y = 185;
    private static final int PASSWORD_LABEL_Y = 235;
    private static final int PASSWORD_FIELD_Y = 260;
    private static final int LOGIN_BUTTON_Y = 320;
    
    // Component dimensions
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int CORNER_RADIUS = 10;

    /**
     * Initializes and displays the admin login page.
     */
    public static void adminlogin() {
        // Create the JFrame for the login page
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null); // Use null layout for direct positioning
        frame.getContentPane().setBackground(BG_COLOR);
        
        // Back button at top left
        JButton backButton = new JButton(BACK_BUTTON_TEXT);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(BACK_COLOR);
        backButton.setBackground(BG_COLOR);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBounds(BACK_BUTTON_X, BACK_BUTTON_Y, 30, 30);
        frame.add(backButton);
        
        // Title
        JLabel titleLabel = new JLabel(TITLE_TEXT, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(0, TITLE_Y, WINDOW_WIDTH, 30);
        frame.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel(SUBTITLE_TEXT, JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);
        subtitleLabel.setBounds(0, SUBTITLE_Y, WINDOW_WIDTH, 20);
        frame.add(subtitleLabel);
        
        // Username label
        JLabel usernameLabel = new JLabel(USERNAME_LABEL);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(SECONDARY_TEXT_COLOR);
        usernameLabel.setBounds((WINDOW_WIDTH - FIELD_WIDTH)/2, USERNAME_LABEL_Y, FIELD_WIDTH, 20);
        frame.add(usernameLabel);
        
        // Username field
        JTextField usernameField = createRoundedTextField();
        usernameField.setBounds((WINDOW_WIDTH - FIELD_WIDTH)/2, USERNAME_FIELD_Y, FIELD_WIDTH, FIELD_HEIGHT);
        frame.add(usernameField);
        
        // Password label
        JLabel passwordLabel = new JLabel(PASSWORD_LABEL);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(SECONDARY_TEXT_COLOR);
        passwordLabel.setBounds((WINDOW_WIDTH - FIELD_WIDTH)/2, PASSWORD_LABEL_Y, FIELD_WIDTH, 20);
        frame.add(passwordLabel);
        
        // Password field
        JPasswordField passwordField = createRoundedPasswordField();
        passwordField.setBounds((WINDOW_WIDTH - FIELD_WIDTH)/2, PASSWORD_FIELD_Y, FIELD_WIDTH, FIELD_HEIGHT);
        frame.add(passwordField);
        
        // Login button
        JButton loginButton = createRoundedButton(LOGIN_BUTTON_TEXT);
        loginButton.setBounds((WINDOW_WIDTH - BUTTON_WIDTH)/2, LOGIN_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        frame.add(loginButton);
        
        // Event handlers
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if(username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(frame, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (username.equals("admin") && password.equals("admin123")) {
                JOptionPane.showMessageDialog(frame, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close login window
                AdminPage.admin();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials, try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backButton.addActionListener(e -> {
            // Go back to StartWindow
            new StartWindow();
            frame.dispose(); // Close login window
        });

        // Display the window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Creates ui.user.a text field with rounded corners.
     */
    private static JTextField createRoundedTextField() {
        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS));
                    g2.dispose();
                }
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BORDER);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS));
                g2.dispose();
            }
        };
        
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        return textField;
    }
    
    /**
     * Creates ui.user.a password field with rounded corners.
     */
    private static JPasswordField createRoundedPasswordField() {
        JPasswordField passwordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS));
                    g2.dispose();
                }
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BORDER);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS));
                g2.dispose();
            }
        };
        
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        return passwordField;
    }
    
    /**
     * Creates ui.user.a button with rounded corners.
     */
    private static JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Paint rounded background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                
                // Paint text
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), textX, textY);
                
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(BUTTON_BG);
        button.setForeground(BUTTON_FG);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        
        return button;
    }
}
