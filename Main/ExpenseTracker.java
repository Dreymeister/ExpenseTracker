package Main;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

    private static boolean isDarkMode = false;

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
        buttonPanel.setLayout(new GridLayout(8, 1));

        JLabel titleLabel = new JLabel("<html><u>Expense Tracker</u></html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel totalLabel = new JLabel("Total Balance: $" + String.format("%.2f", totalAmount));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 25));

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
                showStartingBalanceDialog();
            }
        }
    }

    private void showAddExpenseDialog(JLabel totalLabel, JLabel countLabel) {
        if (isDarkMode) {
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
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
        }
    }

    private void showAddSavingsDialog(JLabel totalLabel, JLabel countLabel) {
        if (isDarkMode) {
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

    @SuppressWarnings("unused")
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

        if (isDarkMode) {
            table.setBackground(Color.DARK_GRAY);
            table.setForeground(Color.WHITE);
            table.getTableHeader().setBackground(Color.BLACK);
            table.getTableHeader().setForeground(Color.WHITE);
        }

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

        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(searchPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> applySearchHighlight(searchField, table, data, columnNames));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applySearchHighlight(searchField, table, data, columnNames);
            }
        });

        JOptionPane.showMessageDialog(null, containerPanel, "Expenses and Savings", JOptionPane.INFORMATION_MESSAGE);
    }

    private void applySearchHighlight(JTextField searchField, JTable table, String[][] data, String[] columnNames) {
        String query = searchField.getText().toLowerCase();
    
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        table.setModel(tableModel);
    
        if (!query.isEmpty()) {
            List<String[]> filteredData = new ArrayList<>();
            for (String[] row : data) {
                for (String cell : row) {
                    if (cell.toLowerCase().contains(query)) {
                        filteredData.add(row);
                        break;
                    }
                }
            }
    
            String[][] filteredArray = new String[filteredData.size()][4];
            filteredData.toArray(filteredArray);
            table.setModel(new DefaultTableModel(filteredArray, columnNames));
        }
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
    
        table.repaint();
    }
    

    private void toggleDarkMode(JFrame frame) {
        isDarkMode = !isDarkMode;

        Color backgroundColor = isDarkMode ? Color.DARK_GRAY : null;
        Color textColor = isDarkMode ? Color.WHITE : Color.BLACK;

        frame.getContentPane().setBackground(backgroundColor);
        updateComponentColors(frame.getContentPane(), backgroundColor, textColor);
        applyDarkModeToDialogs();
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void updateComponentColors(Container container, Color backgroundColor, Color textColor) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(backgroundColor);
                updateComponentColors((JPanel) comp, backgroundColor, textColor);
            } else if (comp instanceof JButton || comp instanceof JLabel) {
                comp.setBackground(backgroundColor);
                comp.setForeground(textColor);
            } else if (comp instanceof JTable) {
                JTable table = (JTable) comp;
                table.setBackground(backgroundColor);
                table.setForeground(textColor);
                table.getTableHeader().setBackground(backgroundColor);
                table.getTableHeader().setForeground(textColor);
            }
        }
    }

    private void applyDarkModeToDialogs() {
        if (isDarkMode) {
            UIManager.put("OptionPane.background", Color.DARK_GRAY);
            UIManager.put("Panel.background", Color.DARK_GRAY);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("TextField.background", Color.DARK_GRAY);
            UIManager.put("TextField.foreground", Color.WHITE);
        } else {
            UIManager.put("OptionPane.background", null);
            UIManager.put("Panel.background", null);
            UIManager.put("OptionPane.messageForeground", null);
            UIManager.put("TextField.background", null);
            UIManager.put("TextField.foreground", null);
        }
    }
}
