package ui.admin;

import ui.StartWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage {

    public static void admin() {
        JFrame adminFrame = new JFrame("Admin Page");
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setSize(400, 300);
        adminFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton orderButton = new JButton("View Order Requests");
        JButton stockButton = new JButton("Manage Stock");
        JButton reportButton = new JButton("Generate Report");
        JButton exitButton = new JButton("Exit to Start Window");

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderRequestPage().order();
            }
        });

        stockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StockManagementPage.showStockManagementPage(adminFrame);
            }
        });

        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(adminFrame, "Report generation is not implemented yet.");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();
                new StartWindow();
            }
        });

        panel.add(orderButton);
        panel.add(stockButton);
        panel.add(reportButton);
        panel.add(exitButton);

        adminFrame.add(panel, BorderLayout.CENTER);

        adminFrame.setVisible(true);
    }
}
