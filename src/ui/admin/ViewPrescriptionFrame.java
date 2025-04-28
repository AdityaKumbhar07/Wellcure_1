package ui.admin;

import javax.swing.*;
import java.awt.*;

public class ViewPrescriptionFrame {

    public static void open(String prescriptionPath) {
        JFrame frame = new JFrame("View Prescription");

        frame.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon(prescriptionPath);
        JLabel imageLabel = new JLabel(imageIcon);
        JScrollPane scrollPane = new JScrollPane(imageLabel);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
