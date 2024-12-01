package Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTrackerTest {

    private ExpenseTracker expenseTracker;
    private JLabel totalLabel;
    private JLabel countLabel;

    @BeforeEach
    public void setUp() {
        expenseTracker = new ExpenseTracker();
        totalLabel = Mockito.mock(JLabel.class);
        countLabel = Mockito.mock(JLabel.class);
        expenseTracker.showStartingBalanceDialog(); // Set a starting balance for tests
    }

    @Test
    public void testAddExpense() {
        // Mock the dialog inputs for expense addition
        Mockito.when(JOptionPane.showInputDialog(Mockito.anyString())).thenReturn("100.00", "Food", "12-01-2024");

        expenseTracker.showAddExpenseDialog(totalLabel, countLabel);

        // Verify that the total balance, expenses, and the count label are updated
        assertEquals(900.00, expenseTracker.getTotalAmount()); // Assuming starting balance was 1000.00
        assertEquals(100.00, expenseTracker.getTotalExpenses());
        assertEquals(1, expenseTracker.getExpenseCount());

        Mockito.verify(totalLabel).setText("Total Balance: $900.00");
        Mockito.verify(countLabel).setText(Mockito.contains("Expenses: $100.00"));
    }

    @Test
    public void testAddSavings() {
        // Mock the dialog inputs for savings addition
        Mockito.when(JOptionPane.showInputDialog(Mockito.anyString())).thenReturn("50.00", "Emergency", "12-01-2024");

        expenseTracker.showAddSavingsDialog(totalLabel, countLabel);

        // Verify that the total balance, savings, and the count label are updated
        assertEquals(1050.00, expenseTracker.getTotalAmount()); // Assuming starting balance was 1000.00
        assertEquals(50.00, expenseTracker.getTotalSavings());
        assertEquals(1, expenseTracker.getSavingsCount());

        Mockito.verify(totalLabel).setText("Total Balance: $1050.00");
        Mockito.verify(countLabel).setText(Mockito.contains("Savings: $50.00"));
    }

    @Test
    public void testQuery() {
        // Add some expenses and savings
        Mockito.when(JOptionPane.showInputDialog(Mockito.anyString())).thenReturn("200.00", "Travel", "12-01-2024");
        expenseTracker.showAddExpenseDialog(totalLabel, countLabel);
        Mockito.when(JOptionPane.showInputDialog(Mockito.anyString())).thenReturn("150.00", "Emergency Fund", "12-01-2024");
        expenseTracker.showAddSavingsDialog(totalLabel, countLabel);

        // Query the data
        expenseTracker.showQueryDialog();

        // Verify that the query shows the correct number of expenses and savings
        List<String[]> allData = expenseTracker.getAllData();  // Assuming a method to get all data for testing
        assertEquals(2, allData.size());
        assertEquals("Expense", allData.get(0)[0]);
        assertEquals("Saving", allData.get(1)[0]);
    }

    @Test
    public void testDarkModeToggle() {
        JFrame frame = Mockito.mock(JFrame.class);
        expenseTracker.toggleDarkMode(frame);
        
        assertTrue(expenseTracker.isDarkMode());
        
        // Toggle again
        expenseTracker.toggleDarkMode(frame);
        assertFalse(expenseTracker.isDarkMode());
    }

    @Test
    public void testInvalidExpenseInput() {
        // Simulate invalid user input for expense
        Mockito.when(JOptionPane.showInputDialog(Mockito.anyString())).thenReturn("invalid", "Food", "12-01-2024");

        expenseTracker.showAddExpenseDialog(totalLabel, countLabel);

        // Verify that the total balance and expenses remain unchanged due to invalid input
        assertEquals(1000.00, expenseTracker.getTotalAmount()); // Assuming starting balance was 1000.00
        assertEquals(0.00, expenseTracker.getTotalExpenses());
    }

    @Test
    public void testInvalidDateFormat() {
        // Simulate an invalid date input
        Mockito.when(JOptionPane.showInputDialog(Mockito.anyString())).thenReturn("50.00", "Emergency", "invalid-date");

        expenseTracker.showAddSavingsDialog(totalLabel, countLabel);

        // Verify that the savings are not added due to invalid date format
        assertEquals(1000.00, expenseTracker.getTotalAmount()); // Assuming starting balance was 1000.00
        assertEquals(0.00, expenseTracker.getTotalSavings());
    }
}
