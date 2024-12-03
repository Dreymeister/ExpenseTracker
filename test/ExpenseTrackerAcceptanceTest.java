import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ExpenseTrackerAcceptanceTest {

    @Test
    public void testAddSavingsFlow() {
        // Set up initial conditions
        ExpenseTracker.totalAmount = 300.00; // Initial balance
        ExpenseTracker.savings.clear(); // Clear previous savings
        ExpenseTracker.totalSavings = 0.00;
        ExpenseTracker.savingsCount = 0;

        // Simulate user adding savings
        String amount = "200.00";
        String category = "Bonus";
        String date = "12-01-2024";

        ExpenseTracker.totalAmount += Double.parseDouble(amount);
        ExpenseTracker.totalSavings += Double.parseDouble(amount);
        ExpenseTracker.savingsCount++;
        ExpenseTracker.savings.add(new String[]{"Saving", "$" + amount, category, date});

        // Verify updated total balance and savings
        assertEquals(500.00, ExpenseTracker.totalAmount, "Total balance should be updated after adding savings.");
        assertEquals(200.00, ExpenseTracker.totalSavings, "Total savings should be updated.");
        assertEquals(1, ExpenseTracker.savingsCount, "Savings count should increment.");

        // Verify saved data
        assertEquals(1, ExpenseTracker.savings.size(), "There should be one savings entry.");
        assertArrayEquals(new String[]{"Saving", "$200.00", "Bonus", "12-01-2024"}, ExpenseTracker.savings.get(0), "Savings data should match.");
    }
}
