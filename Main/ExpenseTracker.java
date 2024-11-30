package Main;

import javax.swing.*;
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

    public static void main(String[] args) {
        ExpenseTracker app = new ExpenseTracker();
        app.showStartingBalanceDialog();  // Prompt for starting balance
        app.createGUI();  // Proceed to GUI after starting balance is set
    }

    private void createGUI() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1));

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

        buttonPanel.add(totalLabel);
        buttonPanel.add(countLabel);

        JButton addButton = new JButton("Add Expense");
        JButton addSavingsButton = new JButton("Add Savings");
        JButton queryButton = new JButton("Query");
        JButton darkLightButton = new JButton("Toggle Dark Mode");
        JButton exitButton = new JButton("Exit");

        addButton.addActionListener(e -> showAddExpenseDialog(totalLabel, countLabel));
        addSavingsButton.addActionListener(e -> showAddSavingsDialog(totalLabel, countLabel));
        queryButton.addActionListener(e -> showQueryDialog());
        darkLightButton.addActionListener(e -> toggleDarkMode(frame));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(addButton);
        buttonPanel.add(addSavingsButton);
        buttonPanel.add(queryButton);
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
                showStartingBalanceDialog();  // Re-prompt the user if the input is invalid
            }
        }
    }

    private void showAddExpenseDialog(JLabel totalLabel, JLabel countLabel) {
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

            totalAmount -= amount;
            totalExpenses += amount;
            expenseCount++;

            expenses.add(new String[]{"Expense", "$" + String.format("%.2f", amount), category});
            updateLabels(totalLabel, countLabel);
            JOptionPane.showMessageDialog(null, "Expense Added: $" + String.format("%.2f", amount));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
        }
    }

    private void showAddSavingsDialog(JLabel totalLabel, JLabel countLabel) {
        String amountStr = JOptionPane.showInputDialog("Enter Savings Amount:");
        if (amountStr == null) {
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            totalAmount += amount;
            totalSavings += amount;
            savingsCount++;

            savings.add(new String[]{"Saving", "$" + String.format("%.2f", amount)});
            updateLabels(totalLabel, countLabel);
            JOptionPane.showMessageDialog(null, "Savings Added: $" + String.format("%.2f", amount));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
        }
    }

    private void updateLabels(JLabel totalLabel, JLabel countLabel) {
        totalLabel.setText("Total Balance: $" + String.format("%.2f", totalAmount));
        countLabel.setText("Expenses: $" + String.format("%.2f", totalExpenses) + 
                           " (" + expenseCount + " entries) | " +
                           "Savings: $" + String.format("%.2f", totalSavings) + 
                           " (" + savingsCount + " entries)");
    }

    private void showQueryDialog() {
        String[][] data = new String[expenses.size() + savings.size()][];
        int rowIndex = 0;

        for (String[] expense : expenses) {
            data[rowIndex++] = expense;
        }

        for (String[] saving : savings) {
            data[rowIndex++] = saving;
        }

        String[] columnNames = {"Type", "Amount", "Category"};

        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(450, 300));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        JOptionPane.showMessageDialog(null, scrollPane, "Expenses and Savings", JOptionPane.INFORMATION_MESSAGE);
    }

    private void toggleDarkMode(JFrame frame) {
        boolean isDarkMode = frame.getContentPane().getBackground().equals(Color.DARK_GRAY);
        Color background = isDarkMode ? Color.WHITE : Color.DARK_GRAY;
        Color foreground = isDarkMode ? Color.BLACK : Color.WHITE;

        updateComponentColors(frame.getContentPane(), background, foreground);
        frame.getContentPane().setBackground(background);
        frame.repaint();
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

