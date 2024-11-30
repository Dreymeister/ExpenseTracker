package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseTracker {

    private static double totalAmount = 0.00;
    private static double totalExpenses = 0.00;
    private static double totalSavings = 0.00;
    private static int expenseCount = 0;
    private static int savingsCount = 0;

    private static List<String[]> expenses = new ArrayList<>();
    private static List<String[]> savings = new ArrayList<>();

    private static Color buttonBackgroundColor; // Button Fixes Here
    private static Color buttonTextColor;
    private static boolean isDarkMode = false;

    public static void main(String[] args) {
        ExpenseTracker app = new ExpenseTracker();
        app.showStartingBalanceDialog();
        app.createGUI();
    }

    private void createGUI() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 1));

        JLabel titleLabel = new JLabel("<html><u>Expense Tracker</u></html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel totalLabel = new JLabel("Total Balance: $" + String.format("%.2f", totalAmount));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Font largeFont = new Font("Arial", Font.BOLD, 25);
        totalLabel.setFont(largeFont);

        JLabel countLabel = new JLabel("Expenses: $" + String.format("%.2f", totalExpenses) +
                " (" + expenseCount + " entries) | " +
                "Savings: $" + String.format("%.2f", totalSavings) +
                " (" + savingsCount + " entries)");
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        buttonPanel.add(titleLabel);
        buttonPanel.add(totalLabel); // Fixed Count Labels
        buttonPanel.add(countLabel);

        JButton addButton = new JButton("Add Expense");
        JButton addSavingsButton = new JButton("Add Savings");
        JButton queryButton = new JButton("Query");
        JButton darkLightButton = new JButton("Toggle Dark Mode");
        JButton exitButton = new JButton("Exit");

        // Save the button colors on startup
        buttonBackgroundColor = addButton.getBackground();
        buttonTextColor = addButton.getForeground();

        addButton.addActionListener(e -> showAddExpenseDialog(totalLabel, countLabel));
        addSavingsButton.addActionListener(e -> showAddSavingsDialog(totalLabel, countLabel));
        queryButton.addActionListener(e -> showQueryDialog());
        darkLightButton.addActionListener(e -> toggleDarkMode(frame));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(addButton);
        buttonPanel.add(addSavingsButton);
        buttonPanel.add(queryButton); // added button formatting
        buttonPanel.add(darkLightButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel);
        frame.setVisible(true);
    }

    private void showStartingBalanceDialog() {
        String startingBalanceStr = JOptionPane.showInputDialog("Enter your starting balance:");
        if (startingBalanceStr != null) {
            try {
                totalAmount = Double.parseDouble(startingBalanceStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid starting balance. Please enter a valid number.");
                showStartingBalanceDialog();
            }
        }
    }

    private void showAddExpenseDialog(JLabel totalLabel, JLabel countLabel) {
        if (isDarkMode) {
            // Apply dark mode to input dialogs
            applyDarkModeToDialogs();
        }

        String amountStr = JOptionPane.showInputDialog("Enter Expense Amount:");
        if (amountStr == null) {
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            String category = JOptionPane.showInputDialog("Enter Category:");
            if (category == null) {
                return;
            }

            String date = JOptionPane.showInputDialog("Enter Date (MM-DD-YYYY):");
            if (date == null || !date.matches("\\d{2}-\\d{2}-\\d{4}")) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please enter in MM-DD-YYYY format.");
                return;
            }

            totalAmount -= amount;
            totalExpenses += amount;
            expenseCount++;

            expenses.add(new String[]{"Expense", "$" + String.format("%.2f", amount), category, date});
            updateLabels(totalLabel, countLabel);
            JOptionPane.showMessageDialog(null, "Expense Added: $" + String.format("%.2f", amount));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number."); // Fixed Colors
        }
    }

    private void showAddSavingsDialog(JLabel totalLabel, JLabel countLabel) {
        if (isDarkMode) {
            // Apply dark mode to input dialogs
            applyDarkModeToDialogs();
        }

        String amountStr = JOptionPane.showInputDialog("Enter Savings Amount:");
        if (amountStr == null) {
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            String category = JOptionPane.showInputDialog("Enter Category:");
            if (category == null) {
                return;
            }

            String date = JOptionPane.showInputDialog("Enter Date (MM-DD-YYYY):");
            if (date == null || !date.matches("\\d{2}-\\d{2}-\\d{4}")) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please enter in MM-DD-YYYY format.");
                return;
            }

            totalAmount += amount;
            totalSavings += amount;
            savingsCount++;

            savings.add(new String[]{"Saving", "$" + String.format("%.2f", amount), category, date});
            updateLabels(totalLabel, countLabel);
            JOptionPane.showMessageDialog(null, "Savings Added: $" + String.format("%.2f", amount));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
        }
    }

    private void updateLabels(JLabel totalLabel, JLabel countLabel) {
        totalLabel.setText("Total Balance: $" + String.format("%.2f", totalAmount));

        String expenseText = "Expenses: <font color='red'>$" + String.format("%.2f", totalExpenses) + "</font>" +
                " (" + expenseCount + " entries)";
        String savingsText = "Savings: <font color='green'>$" + String.format("%.2f", totalSavings) + "</font>" +
                " (" + savingsCount + " entries)";

        countLabel.setText("<html>" + expenseText + " | " + savingsText + "</html>");
    }

    private void showQueryDialog() {
        String[][] data = new String[expenses.size() + savings.size()][4];
        int rowIndex = 0;

        for (String[] expense : expenses) {
            data[rowIndex++] = expense;
        }

        for (String[] saving : savings) {
            data[rowIndex++] = saving;
        }

        String[] columnNames = {"Type", "Amount", "Category", "Date"};

        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(450, 300)); // Finalized Viewports
        table.setFillsViewportHeight(true);

        if (isDarkMode) {
            table.setBackground(Color.DARK_GRAY);
            table.setForeground(Color.WHITE);
            table.getTableHeader().setBackground(Color.BLACK);
            table.getTableHeader().setForeground(Color.WHITE);
        }

        // Set the custom renderer for the "Type" column
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Apply red color for "Expense" and green for "Saving"
                if (value != null) {
                    if (value.equals("Expense")) {
                        component.setForeground(Color.RED);
                    } else if (value.equals("Saving")) {
                        component.setForeground(Color.GREEN);
                    } else {
                        component.setForeground(Color.WHITE);  // Default color for other rows
                    }
                }

                return component;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        JOptionPane.showMessageDialog(null, scrollPane, "Expenses and Savings", JOptionPane.INFORMATION_MESSAGE);
    }

    private void toggleDarkMode(JFrame frame) {
        isDarkMode = !isDarkMode;
        Color background = isDarkMode ? Color.DARK_GRAY : Color.WHITE;
        Color foreground = isDarkMode ? Color.WHITE : Color.BLACK;

        updateComponentColors(frame.getContentPane(), background, foreground);
        frame.getContentPane().setBackground(background);
        frame.repaint();

        // Ensure buttons keep their original colors
        for (Component comp : frame.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(buttonBackgroundColor); // Preserve original button color
                button.setForeground(buttonTextColor); // Preserve original button text color
            }
        }

        if (!isDarkMode) {
            resetUIManagerForLightMode();  // Reset UI Manager settings for light mode
        }

        // Update the query dialog and input fields
        if (isDarkMode) {
            applyDarkModeToDialogs();
        }
    }

    private void resetUIManagerForLightMode() {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", Color.BLACK);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("Button.background", Color.LIGHT_GRAY);
        UIManager.put("Button.foreground", Color.BLACK);
    }

    private void applyDarkModeToDialogs() {
        UIManager.put("OptionPane.background", Color.DARK_GRAY);
        UIManager.put("Panel.background", Color.DARK_GRAY);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("TextField.background", Color.GRAY);
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("Button.background", Color.DARK_GRAY);
        UIManager.put("Button.foreground", Color.WHITE);
    }

    private void updateComponentColors(Component component, Color background, Color foreground) {
        component.setBackground(background);
        component.setForeground(foreground);

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                updateComponentColors(child, background, foreground);
            }
        }
    }
}
