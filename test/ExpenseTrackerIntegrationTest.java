package test;

import Main.ExpenseTracker;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExpenseTrackerIntegrationTest {

    @Test
    public void testAddExpenseAndQuery() {
        // Set up initial conditions
        ExpenseTracker tracker = new ExpenseTracker();
        ExpenseTracker.totalAmount = 500.00; // Setting an initial balance
        ExpenseTracker.expenses.clear(); // Clearing any previous data
        ExpenseTracker.totalExpenses = 0.00;
        ExpenseTracker.expenseCount = 0;

        // Simulate adding an expense
        String amount = "100.00";
        String category = "Groceries";
        String date = "12-01-2024";

        ExpenseTracker.totalAmount -= Double.parseDouble(amount);
        ExpenseTracker.totalExpenses += Double.parseDouble(amount);
        ExpenseTracker.expenseCount++;
        ExpenseTracker.expenses.add(new String[]{"Expense", "$" + amount, category, date});

        // Validate updated totals
        assertEquals(400.00, ExpenseTracker.totalAmount, "Total balance should be updated after adding expense.");
        assertEquals(100.00, ExpenseTracker.totalExpenses, "Total expenses should be updated.");
        assertEquals(1, ExpenseTracker.expenseCount, "Expense count should increment.");

        // Query and validate expense data
        List<String[]> queriedData = ExpenseTracker.expenses;
        assertEquals(1, queriedData.size(), "There should be one expense entry.");
        assertArrayEquals(new String[]{"Expense", "$100.00", "Groceries", "12-01-2024"}, queriedData.get(0), "Expense data should match.");
    }
}

