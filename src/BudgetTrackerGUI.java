import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BudgetTrackerGUI extends JFrame {

    // Inputs and table stuff
    private final JTextField descriptionField, amountField, dateField, categoryField;
    private final JComboBox<String> typeChoice;
    private final JTable transactionsTable;
    private final DefaultTableModel tableModel;
    private final JLabel totalIncomeLabel, totalExpenseLabel, balanceLabel;
    private int selectedRow = -1;  // Tracks row we're editing
    private int selectedTransactionId = -1; // ID for current transaction

    public BudgetTrackerGUI() {
        // Main layout setup
        setLayout(new BorderLayout());

        // Top panel (input fields)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(15); // Set length same for all
        gbc.gridx = 1;
        inputPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Description:"), gbc);
        descriptionField = new JTextField(15); // Same length
        gbc.gridx = 1;
        inputPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Category:"), gbc);
        categoryField = new JTextField(15); // Same length
        gbc.gridx = 1;
        inputPanel.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Amount:"), gbc);
        amountField = new JTextField(15); // Same length
        gbc.gridx = 1;
        inputPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Type:"), gbc);
        String[] types = {"income", "expense"};
        typeChoice = new JComboBox<>(types);
        gbc.gridx = 1;
        inputPanel.add(typeChoice, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton addButton = new JButton("Add/Update Transaction");
        inputPanel.add(addButton, gbc);

        // Handle adding or updating transaction
        addButton.addActionListener(e -> addOrUpdateTransaction());

        // Delete button
        JButton deleteButton = new JButton("Delete Transaction");
        gbc.gridy = 6;
        inputPanel.add(deleteButton, gbc);
        deleteButton.addActionListener(e -> deleteTransaction());

        add(inputPanel, BorderLayout.NORTH);

        // Transaction table setup
        String[] columnNames = {"ID", "Date", "Description", "Category", "Amount", "Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
        transactionsTable = new JTable(tableModel);
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionsTable.getSelectionModel().addListSelectionListener(e -> loadTransactionForEdit());

        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom row for totals (income, expense, balance)
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalIncomeLabel = new JLabel("Total Income: $0.00");
        totalExpenseLabel = new JLabel("Total Expenses: $0.00");
        balanceLabel = new JLabel("Balance: $0.00");

        summaryPanel.add(totalIncomeLabel);
        summaryPanel.add(totalExpenseLabel);
        summaryPanel.add(balanceLabel);

        add(summaryPanel, BorderLayout.SOUTH);

        setTitle("Personal Budget Tracker");
        setSize(800, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load everything and show totals
        loadAllTransactions();
        updateSummary();
    }

    // Add or update a transaction
    private void addOrUpdateTransaction() {
        try {
            String date = dateField.getText();

            // Check date format (YYYY-MM-DD)
            if (!isValidDate(date)) {
                showError("Invalid date format! Use YYYY-MM-DD.");
                return;
            }

            String description = descriptionField.getText();
            String category = categoryField.getText();
            String amountText = amountField.getText();

            if (amountText.isEmpty()) {
                showError("Amount can't be empty!");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText); // Parse the amount
            } catch (NumberFormatException e) {
                showError("Amount must be a valid number!");
                return;
            }

            String type = (String) typeChoice.getSelectedItem();
            Transaction transaction = new Transaction(date, description, category, amount, type);

            // Edit mode or add new
            if (selectedTransactionId != -1) {
                TransactionManager.updateTransaction(selectedTransactionId, transaction);
                selectedTransactionId = -1;  // Reset
            } else {
                TransactionManager.addTransaction(transaction);
            }

            clearFields();  // Reset input
            loadAllTransactions();  // Refresh table
            updateSummary();  // Update totals

        } catch (Exception e) {
            showError("Error adding/updating transaction: " + e.getMessage());
        }
    }

    // Delete selected transaction
    private void deleteTransaction() {
        if (selectedTransactionId != -1) {
            TransactionManager.deleteTransaction(selectedTransactionId);  // Remove from DB
            clearFields();
            loadAllTransactions();  // Refresh table
            updateSummary();  // Update totals
            selectedTransactionId = -1;  // Reset
        } else {
            showError("Pick a transaction to delete!");
        }
    }

    // Load a transaction for editing (if row clicked)
    private void loadTransactionForEdit() {
        selectedRow = transactionsTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedTransactionId = (int) tableModel.getValueAt(selectedRow, 0); // Get ID
            dateField.setText((String) tableModel.getValueAt(selectedRow, 1));
            descriptionField.setText((String) tableModel.getValueAt(selectedRow, 2));
            categoryField.setText((String) tableModel.getValueAt(selectedRow, 3));
            amountField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            typeChoice.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
        }
    }

    // Load all transactions from DB to table
    private void loadAllTransactions() {
        tableModel.setRowCount(0);  // Clear table
        ArrayList<Transaction> transactions = TransactionManager.getAllTransactions();
        for (Transaction t : transactions) {
            Object[] rowData = {
                t.getId(),
                t.getDate(),
                t.getDescription(),
                t.getCategory(),
                t.getAmount(),
                t.getType()
            };
            tableModel.addRow(rowData);
        }
    }

    // Reset input fields after add/update
    private void clearFields() {
        dateField.setText("");
        descriptionField.setText("");
        categoryField.setText("");
        amountField.setText("");
        typeChoice.setSelectedIndex(0);
        selectedRow = -1;  // Clear selected row
    }

    // Show error messages
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        clearFields();  // Clear after error
    }

    // Check if the date is valid
    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);  // Don't allow bad dates
        try {
            sdf.parse(date);  // Try parsing
            return true;  // Good format
        } catch (ParseException e) {
            return false;  // Bad format
        }
    }

    // Update the income/expense/balance totals
    private void updateSummary() {
        double totalIncome = 0;
        double totalExpense = 0;

        // Loop through table rows to calculate totals
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double amount = (double) tableModel.getValueAt(i, 4);  // Get amount
            String type = (String) tableModel.getValueAt(i, 5);   // Income or expense?

            if ("income".equals(type)) {
                totalIncome += amount;
            } else if ("expense".equals(type)) {
                totalExpense += amount;
            }
        }

        // Calculate balance (income - expenses)
        double balance = totalIncome - totalExpense;

        // Show the totals
        totalIncomeLabel.setText(String.format("Total Income: $%.2f", totalIncome));
        totalExpenseLabel.setText(String.format("Total Expenses: $%.2f", totalExpense));
        balanceLabel.setText(String.format("Balance: $%.2f", balance));
    }

    public static void main(String[] args) {
        new BudgetTrackerGUI();
    }
}
