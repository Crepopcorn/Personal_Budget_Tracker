
# Personal Budget Tracker

## Overview
The **Personal Budget Tracker** is a Java-based application designed to help users manage finances. It allows users to track income, expenses, and savings with easy-to-use features like setting budgets, categorizing expenses, and viewing spending trends in a graphical format.

## App Layout
![table_page](https://github.com/Crepopcorn/personal_budget_tracker/blob/main/layout-budget.jpg)

## Features
- **Track Income & Expenses**: Log your daily expenses and income to get a clear view of your financial situation.
- **Budget Setting**: Set budgets to manage how much you want to spend in different categories.
- **Expense Categorization**: Organize your expenses into categories like food, travel, entertainment, etc.
- **Graphical Insights**: Visualize your spending habits with easy-to-read graphs and charts.
- **Data Persistence**: All financial data is saved in a MySQL database, ensuring your data is safe and accessible.

## Tools & Technologies
- **Programming Language**: Java
- **GUI**: AWT (Abstract Window Toolkit)
- **Database**: MySQL

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/crepopcorn/personal-budget-tracker.git
   ```

2. Make sure MySQL is installed and properly configured. Then, create a new database for the app by running the following SQL command in your MySQL interface:
   ```sql
   CREATE DATABASE budget_tracker;
   ```

3. Open the `DatabaseConnection.java` file in your code editor and update it with your MySQL credentials (username and password).

4. After setting up the database, run the following SQL script to create the necessary table for transactions:
   ```sql
   CREATE TABLE transactions (
       id INT AUTO_INCREMENT PRIMARY KEY,
       type VARCHAR(10),  -- 'income' or 'expense'
       category VARCHAR(50),
       amount DECIMAL(10, 2),
       date DATE
   );
   ```

5. Open the project in your preferred IDE (like VS Code or IntelliJ), and run the `Main.java` file to start the app.

## Requirements
- Java 8 or above
- MySQL Server
- MySQL Connector for Java
