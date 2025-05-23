package ui.util;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * UIConfig provides centralized styling for the entire application.
 * This ensures ui.user.a consistent look and feel across all pages.
 * The styling follows ui.user.a flat, modern black and white theme with rounded edges
 * for buttons, text fields, tables, and panels.
 */
public class UIConfig {
    // ==================== Color Theme ====================

    // Primary colors
    public static final Color PRIMARY_BG = Color.WHITE;
    public static final Color PRIMARY_FG = Color.BLACK;

    // Secondary colors
    public static final Color SECONDARY_BG = new Color(240, 240, 240);
    public static final Color SECONDARY_FG = new Color(50, 50, 50);

    // Accent colors
    public static final Color ACCENT_COLOR = new Color(70, 70, 70);
    public static final Color ACCENT_COLOR_HOVER = new Color(100, 100, 100);

    // Button colors (configurable)
    public static final Color BUTTON_BG = new Color(240, 240, 240);
    public static final Color BUTTON_FG = new Color(50, 50, 50);
    public static final Color BUTTON_BORDER = new Color(200, 200, 200);

    // Table colors
    public static final Color TABLE_HEADER_BG = new Color(240, 240, 240);
    public static final Color TABLE_HEADER_FG = new Color(50, 50, 50);
    public static final Color TABLE_ROW_BG = Color.WHITE;
    public static final Color TABLE_ROW_ALT_BG = new Color(248, 248, 248);
    public static final Color TABLE_SELECTION_BG = new Color(230, 230, 230);
    public static final Color TABLE_SELECTION_FG = Color.BLACK;

    // ==================== Fonts ====================

    public static final String PRIMARY_FONT = "Arial";
    public static final int TITLE_FONT_SIZE = 24;
    public static final int SUBTITLE_FONT_SIZE = 18;
    public static final int REGULAR_FONT_SIZE = 14;
    public static final int SMALL_FONT_SIZE = 12;

    public static final Font TITLE_FONT = new Font(PRIMARY_FONT, Font.BOLD, TITLE_FONT_SIZE);
    public static final Font SUBTITLE_FONT = new Font(PRIMARY_FONT, Font.BOLD, SUBTITLE_FONT_SIZE);
    public static final Font REGULAR_FONT = new Font(PRIMARY_FONT, Font.PLAIN, REGULAR_FONT_SIZE);
    public static final Font BUTTON_FONT = new Font(PRIMARY_FONT, Font.BOLD, REGULAR_FONT_SIZE);
    public static final Font SMALL_FONT = new Font(PRIMARY_FONT, Font.PLAIN, SMALL_FONT_SIZE);

    // ==================== Dimensions ====================

    // Corner radius for rounded components
    public static final int CORNER_RADIUS = 10;

    // Standard padding and spacing
    public static final int PADDING = 10;
    public static final int SPACING = 10;

    // Standard component sizes
    public static final Dimension BUTTON_SIZE = new Dimension(150, 40);
    public static final Dimension LARGE_BUTTON_SIZE = new Dimension(200, 50);
    public static final Dimension SMALL_BUTTON_SIZE = new Dimension(100, 30);
    public static final Dimension TEXT_FIELD_SIZE = new Dimension(250, 30);

    // ==================== Borders ====================

    // Standard padding border
    public static final EmptyBorder PADDING_BORDER = new EmptyBorder(PADDING, PADDING, PADDING, PADDING);

    // Rounded borders for various components
    public static final Border ROUNDED_BORDER = new LineBorder(BUTTON_BORDER, 1, true);

    // ==================== Component Alignment ====================

    // Alignment constants for easy customization
    // Change these values to adjust alignment throughout the application

    // Horizontal alignment options: LEFT, CENTER, RIGHT
    public static final float TITLE_H_ALIGN = Component.CENTER_ALIGNMENT;
    public static final float BUTTON_H_ALIGN = Component.CENTER_ALIGNMENT;
    public static final float LABEL_H_ALIGN = Component.LEFT_ALIGNMENT;
    public static final float FIELD_H_ALIGN = Component.CENTER_ALIGNMENT;

    // Vertical alignment options: TOP, CENTER, BOTTOM
    public static final float COMPONENT_V_ALIGN = Component.TOP_ALIGNMENT;

    // ==================== Styling Methods ====================

    /**
     * Apply standard styling to ui.user.a JButton with rounded corners
     *
     * @param button The button to style
     * @return The styled button
     */
    public static JButton styleButton(JButton button) {
        return styleButton(button, BUTTON_BG, BUTTON_FG);
    }

    /**
     * Apply custom styling to ui.user.a JButton with rounded corners
     *
     * @param button The button to style
     * @param bgColor Background color
     * @param fgColor Foreground color
     * @return The styled button
     */
    public static JButton styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(ROUNDED_BORDER);
        button.setPreferredSize(BUTTON_SIZE);

        // Set alignment based on configuration
        button.setAlignmentX(BUTTON_H_ALIGN);
        button.setAlignmentY(COMPONENT_V_ALIGN);

        return button;
    }

    /**
     * Apply standard styling to ui.user.a JTextField with rounded corners
     *
     * @param textField The text field to style
     * @return The styled text field
     */
    public static JTextField styleTextField(JTextField textField) {
        textField.setFont(REGULAR_FONT);
        textField.setBorder(ROUNDED_BORDER);
        textField.setPreferredSize(TEXT_FIELD_SIZE);

        // Set alignment based on configuration
        textField.setAlignmentX(FIELD_H_ALIGN);
        textField.setAlignmentY(COMPONENT_V_ALIGN);

        return textField;
    }

    /**
     * Apply standard styling to ui.user.a JPasswordField with rounded corners
     *
     * @param passwordField The password field to style
     * @return The styled password field
     */
    public static JPasswordField stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(REGULAR_FONT);
        passwordField.setBorder(ROUNDED_BORDER);
        passwordField.setPreferredSize(TEXT_FIELD_SIZE);

        // Set alignment based on configuration
        passwordField.setAlignmentX(FIELD_H_ALIGN);
        passwordField.setAlignmentY(COMPONENT_V_ALIGN);

        return passwordField;
    }

    /**
     * Apply standard styling to ui.user.a JTable with rounded corners
     *
     * @param table The table to style
     * @return The styled table
     */
    public static JTable styleTable(JTable table) {
        table.setFont(REGULAR_FONT);
        table.setRowHeight(30);
        table.setGridColor(BUTTON_BORDER);
        table.setSelectionBackground(TABLE_SELECTION_BG);
        table.setSelectionForeground(TABLE_SELECTION_FG);
        table.setShowGrid(true);
        table.setBorder(ROUNDED_BORDER);

        // Style the header
        JTableHeader header = table.getTableHeader();
        header.setFont(BUTTON_FONT);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FG);
        header.setBorder(ROUNDED_BORDER);

        return table;
    }

    /**
     * Apply standard styling to ui.user.a JPanel
     *
     * @param panel The panel to style
     * @return The styled panel
     */
    public static JPanel stylePanel(JPanel panel) {
        panel.setBackground(PRIMARY_BG);
        panel.setBorder(PADDING_BORDER);
        return panel;
    }

    /**
     * Apply standard styling to ui.user.a JPanel with rounded corners
     *
     * @param panel The panel to style
     * @return The styled panel
     */
    public static JPanel styleRoundedPanel(JPanel panel) {
        panel.setBackground(PRIMARY_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                ROUNDED_BORDER,
                PADDING_BORDER
        ));
        return panel;
    }

    /**
     * Apply standard styling to ui.user.a JFrame
     *
     * @param frame The frame to style
     * @return The styled frame
     */
    public static JFrame styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(PRIMARY_BG);
        return frame;
    }

    /**
     * Apply standard styling to ui.user.a JLabel as ui.user.a title
     *
     * @param label The label to style
     * @return The styled label
     */
    public static JLabel styleTitle(JLabel label) {
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY_FG);

        // Set alignment based on configuration
        label.setAlignmentX(TITLE_H_ALIGN);
        label.setAlignmentY(COMPONENT_V_ALIGN);

        return label;
    }

    /**
     * Apply standard styling to ui.user.a JLabel as ui.user.a subtitle
     *
     * @param label The label to style
     * @return The styled label
     */
    public static JLabel styleSubtitle(JLabel label) {
        label.setFont(SUBTITLE_FONT);
        label.setForeground(SECONDARY_FG);

        // Set alignment based on configuration
        label.setAlignmentX(TITLE_H_ALIGN);
        label.setAlignmentY(COMPONENT_V_ALIGN);

        return label;
    }

    /**
     * Apply standard styling to ui.user.a JLabel
     *
     * @param label The label to style
     * @return The styled label
     */
    public static JLabel styleLabel(JLabel label) {
        label.setFont(REGULAR_FONT);
        label.setForeground(SECONDARY_FG);

        // Set alignment based on configuration
        label.setAlignmentX(LABEL_H_ALIGN);
        label.setAlignmentY(COMPONENT_V_ALIGN);

        return label;
    }

    /**
     * Create ui.user.a scroll pane with rounded corners
     *
     * @param component The component to add to the scroll pane
     * @return The styled scroll pane
     */
    public static JScrollPane createRoundedScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(ROUNDED_BORDER);
        scrollPane.getViewport().setBackground(PRIMARY_BG);
        return scrollPane;
    }
}