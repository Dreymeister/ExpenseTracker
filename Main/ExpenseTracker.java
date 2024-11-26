package Main;
import java.sql.*;
import java.util.Scanner;

public class ExpenseTracker {

    private static final String DB_URL = "jdbc:sqlite:expenses.db";

    public static void main(String[] args) {
        ExpenseTracker app = new ExpenseTracker();
        app.initializeDatabase();
        app.run();
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Expenses (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "date TEXT NOT NULL, " +
                                    "amount REAL NOT NULL, " +
                                    "category TEXT NOT NULL, " +
                                    "tag TEXT)";
            statement.execute(createTableSQL);
            System.out.println("Database initialized.");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Expense");
            System.out.println("2. Query Expenses");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addExpense(scanner);
                case 2 -> queryExpenses(scanner);
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Add an expense
    private void addExpense(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter category: ");
            String category = scanner.nextLine();
            System.out.print("Enter tag: ");
            String tag = scanner.nextLine();

            String insertSQL = "INSERT INTO Expenses (date, amount, category, tag) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, date);
                preparedStatement.setDouble(2, amount);
                preparedStatement.setString(3, category);
                preparedStatement.setString(4, tag);
                preparedStatement.executeUpdate();
                System.out.println("Expense added successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding expense: " + e.getMessage());
        }
    }

    // Query expenses
    private void queryExpenses(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            System.out.print("Enter query criteria (e.g., category, tag, or leave blank for all): ");
            String criteria = scanner.nextLine();

            String querySQL = "SELECT * FROM Expenses";
            if (!criteria.isBlank()) {
                querySQL += " WHERE category = ? OR tag = ?";
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(querySQL)) {
                if (!criteria.isBlank()) {
                    preparedStatement.setString(1, criteria);
                    preparedStatement.setString(2, criteria);
                }

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("Expenses:");
                    while (resultSet.next()) {
                        System.out.printf("ID: %d | Date: %s | Amount: %.2f | Category: %s | Tag: %s%n",
                                resultSet.getInt("id"),
                                resultSet.getString("date"),
                                resultSet.getDouble("amount"),
                                resultSet.getString("category"),
                                resultSet.getString("tag"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error querying expenses: " + e.getMessage());
        }
    }
}

