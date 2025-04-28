package ui.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserHomePage {
    private JFrame userHomeFrame;

    public UserHomePage(String username) {

        // Create the User Home Frame
        userHomeFrame = new JFrame("User Home");

        // Create Panel for layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1)); // Grid layout with 5 rows and 1 column

        // Create buttons
        JButton viewAccountButton = new JButton("View Account");
        JButton uploadPrescriptionButton = new JButton("Upload Prescription");
        JButton orderHistoryButton = new JButton("Order History");
        JButton logoutButton = new JButton("Logout");
        JButton helpButton = new JButton("Help");

        // Add action listeners for buttons
        viewAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hide User Home page
                userHomeFrame.setVisible(false);

                // Show AccountPage
                AccountPage.goToAccount(username);
            }
        });

        uploadPrescriptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hide User Home page
                userHomeFrame.setVisible(false);

                // Show Prescription Upload page and pass the username
                UploadPrescriptionPage.uploadPrescription(username);
            }
        });

        orderHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hide User Home page
                userHomeFrame.setVisible(false);

                // Show OrderHistoryPage (needs you to implement the order history page)
                OrderPage.showUserOrders(username);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logout functionality here, just dispose the frame
                userHomeFrame.dispose();

                // Redirect to login page (make sure you have a LoginPage class)
                UserLoginPage.login();
            }
        });

        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show help or instructions
                JOptionPane.showMessageDialog(userHomeFrame, "For help, contact support@wellcure.com", "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add buttons to the panel
        panel.add(viewAccountButton);
        panel.add(uploadPrescriptionButton);
        panel.add(orderHistoryButton);
        panel.add(logoutButton);
        panel.add(helpButton);

        // Add panel to the frame
        userHomeFrame.add(panel);

        // Frame settings
        userHomeFrame.setSize(400, 300);
        userHomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userHomeFrame.setVisible(true);
    }

    // Show the User Home page
    public void showUserHomePage() {
        userHomeFrame.setVisible(true);
    }
}
