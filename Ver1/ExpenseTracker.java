package Ver1;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
        app.showStartingBalanceDialog();
        app.createGUI();
    }

    @SuppressWarnings("unused")
    private void createGUI() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1));

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
        buttonPanel.add(totalLabel);
        buttonPanel.add(countLabel);

        JButton addButton = new JButton("Add Expense");
        JButton addSavingsButton = new JButton("Add Savings");
        JButton queryButton = new JButton("Query");
        JButton exitButton = new JButton("Exit");

        addButton.addActionListener(e -> showAddExpenseDialog(totalLabel, countLabel));
        addSavingsButton.addActionListener(e -> showAddSavingsDialog(totalLabel, countLabel));
        queryButton.addActionListener(e -> showQueryDialog());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(addButton);
        buttonPanel.add(addSavingsButton);
        buttonPanel.add(queryButton);
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

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        table.setModel(tableModel);

        table.setPreferredScrollableViewportSize(new Dimension(450, 300));
        table.setFillsViewportHeight(true);

        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ("Expense".equals(value)) {
                    c.setForeground(Color.RED);
                } else if ("Saving".equals(value)) {
                    c.setForeground(Color.GREEN);
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(null, containerPanel, "Expenses and Savings", JOptionPane.INFORMATION_MESSAGE);
    }
}
