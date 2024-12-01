# **Personal Budget and Expense Tracker**

## **Overview**
The **Personal Budget and Expense Tracker** is a simple command-line Java application designed to help users manage their finances. It allows users to log expenses, categorize them, and query their spending using a SQLite database.

---

## **Features**
1. **Add Expenses**:  
   Log your expenses with details such as date, amount, category, and tag.  
   Example: `Amount: $15.75 | Category: Groceries | Tag: FOOD | Date: 2024-10-29`

2. **Query Expenses**:  
   Search for expenses based on category, tag, or view all expenses.

3. **SQLite Integration**:  
   All data is stored in a local SQLite database for persistence.

4. **Simple CLI Interface**:  
   A user-friendly command-line interface for ease of use.

---

## **Prerequisites**
1. **Java 8+**: Ensure Java is installed on your system.  
   Check with: `java -version`

2. **SQLite JDBC Driver**: Download and include the SQLite JDBC driver in your project classpath. (If needed to test application)  
   [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc)

---

## **Setup Instructions**
1. Clone or download the project files.  
2. Compile the Java file:  
   javac ExpenseTracker.java
3. Use command java -cp ".;sqlite-jdbc-3.47.1.0.jar" ExpenseTracker.java to initialize the database.
4. Use command ExpenseTracker.java to begin application.

