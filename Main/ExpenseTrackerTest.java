import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseTrackerTest {

    private ExpenseTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new ExpenseTracker();
        tracker.showStartingBalanceDialog();  // simulate starting balance input
    }

    @Test
    void testAddExpense() {
        JLabel totalLabel = new JLabel();
        JLabel countLabel = new JLabel();

        tracker.showAddExpenseDialog(totalLabel, countLabel);  // simulate adding expense

        // Check if the total balance is correctly updated
        assertTrue(totalLabel.getText().contains("-10.00"));  // Assume the expense added is $10.00
        assertTrue(countLabel.getText().contains("Expenses: $10.00"));
    }

    @Test
    void testAddSavings() {
        JLabel totalLabel = new JLabel();
        JLabel countLabel = new JLabel();

        tracker.showAddSavingsDialog(totalLabel, countLabel);  // simulate adding savings

        
        assertTrue(totalLabel.getText().contains("10.00"));  
        assertTrue(countLabel.getText().contains("Savings: $10.00"));
    }

    @Test
    void testToggleDarkMode() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tracker.toggleDarkMode(frame);

        // Check if dark mode was applied
        assertEquals(Color.DARK_GRAY, frame.getContentPane().getBackground());
        assertEquals(Color.WHITE, frame.getContentPane().getForeground());
    }
}
