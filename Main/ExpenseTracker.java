package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpenseTracker {

    private static final String DB_URL = "jdbc:sqlite:expenses.db";
    private static double totalAmount = 0.00; // Keep track of the total amount added or removed

    public static void main(String[] args) {
        ExpenseTracker app = new ExpenseTracker();
        app.initializeDatabase();
        app.createGUI();
    }

    // Initialize the database (this is your existing logic)
    private void initializeDatabase() {
        // Initialize database here (e.g., create tables if they don't exist)
        // For now, we'll keep this empty
    }

    // Create the GUI components
    private void createGUI() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);  // Center the window

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1));  // 6 rows for label + 5 buttons

        // Total amount label
        JLabel totalLabel = new JLabel("Total: $" + totalAmount);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Font largeFont = new Font("Arial", Font.BOLD, 30); // Make the font size larger
        totalLabel.setFont(largeFont);  // Set the larger font
        buttonPanel.add(totalLabel);  // Add the total label at the top

        // Create buttons
        JButton addButton = new JButton("Add Expense");
        JButton queryButton = new JButton("Query Expenses");
        JButton darkLightButton = new JButton("Toggle Dark Mode");
        JButton exitButton = new JButton("Exit");

        // Add action listeners to buttons
        addButton.addActionListener(e -> showAddExpenseDialog(totalLabel));
        queryButton.addActionListener(e -> showQueryExpensesDialog());
        darkLightButton.addActionListener(e -> toggleDarkMode(frame));
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(queryButton);
        buttonPanel.add(darkLightButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel);
        frame.setVisible(true);
    }

    // Show dialog for adding expenses
    private void showAddExpenseDialog(JLabel totalLabel) {
        // Input fields for adding an expense
        String date = JOptionPane.showInputDialog("Enter Date (YYYY-MM-DD):");
        if (date == null) {
            return; // If user cancels, return to home menu
        }

        String amountStr = JOptionPane.showInputDialog("Enter Amount:");
        if (amountStr == null) {
            return; // If user cancels, return to home menu
        }

        String category = JOptionPane.showInputDialog("Enter Category:");
        if (category == null) {
            return; // If user cancels, return to home menu
        }

        String tag = JOptionPane.showInputDialog("Enter Tag:");
        if (tag == null) {
            return; // If user cancels, return to home menu
        }

        try {
            double amount = Double.parseDouble(amountStr);
            // Update the totalAmount (you can store this in the database as well)
            totalAmount += amount;
            totalLabel.setText("Total: $" + totalAmount); // Update the total label
            JOptionPane.showMessageDialog(null, "Expense Added: " + amount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
        }
    }

    // Show dialog for querying expenses
    private void showQueryExpensesDialog() {
        // You can query expenses from the database here.
        // For now, we'll show a mock dialog.
        int result = JOptionPane.showConfirmDialog(null, "Would you like to view the list of expenses?", 
                                                   "Query Expenses", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
            return; // If user cancels or closes, return to home menu
        }

        // Placeholder for querying expenses logic
        JOptionPane.showMessageDialog(null, "Query Expenses Dialog!");
    }

    // Toggle between dark and light modes
    private void toggleDarkMode(JFrame frame) {
        Color background = frame.getContentPane().getBackground();
        if (background.equals(Color.WHITE)) {
            frame.getContentPane().setBackground(Color.DARK_GRAY);
            frame.getContentPane().setForeground(Color.WHITE);
        } else {
            frame.getContentPane().setBackground(Color.WHITE);
            frame.getContentPane().setForeground(Color.BLACK);
        }
    }
}
