package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpenseTracker {

    private static final String DB_URL = "jdbc:sqlite:expenses.db";
    private static double totalAmount = 0.00;

    public static void main(String[] args) {
        ExpenseTracker app = new ExpenseTracker();
        app.initializeDatabase();
        app.createGUI();
    }

    private void initializeDatabase() {
    }

    private void createGUI() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1));

        JLabel totalLabel = new JLabel("Total: $" + String.format("%.2f", totalAmount));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Font largeFont = new Font("Arial", Font.BOLD, 30);
        totalLabel.setFont(largeFont);
        buttonPanel.add(totalLabel);

        JButton addButton = new JButton("Add Expense");
        JButton queryButton = new JButton("Query Expenses");
        JButton darkLightButton = new JButton("Toggle Dark Mode");
        JButton exitButton = new JButton("Exit");

        addButton.addActionListener(e -> showAddExpenseDialog(totalLabel));
        queryButton.addActionListener(e -> showQueryExpensesDialog());
        darkLightButton.addActionListener(e -> toggleDarkMode(frame));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(addButton);
        buttonPanel.add(queryButton);
        buttonPanel.add(darkLightButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel);
        frame.setVisible(true);
    }

    private void showAddExpenseDialog(JLabel totalLabel) {
        String date = JOptionPane.showInputDialog("Enter Date (YYYY-MM-DD):");
        if (date == null) {
            return;
        }

        String amountStr = JOptionPane.showInputDialog("Enter Amount:");
        if (amountStr == null) {
            return;
        }

        String category = JOptionPane.showInputDialog("Enter Category:");
        if (category == null) {
            return;
        }

        String tag = JOptionPane.showInputDialog("Enter Tag:");
        if (tag == null) {
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            totalAmount += amount;
            totalLabel.setText("Total: $" + String.format("%.2f", totalAmount));
            JOptionPane.showMessageDialog(null, "Expense Added: " + amount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
        }
    }

    private void showQueryExpensesDialog() {
        int result = JOptionPane.showConfirmDialog(null, "Would you like to view the list of expenses?", 
                                                   "Query Expenses", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
            return;
        }

        JOptionPane.showMessageDialog(null, "Query Expenses Dialog!");
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
