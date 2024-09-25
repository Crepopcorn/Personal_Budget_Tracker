import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionManager {

    // Add a new transaction to DB
    public static void addTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (date, description, category, amount, type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Fill in the query with transaction details
            pstmt.setString(1, transaction.getDate());
            pstmt.setString(2, transaction.getDescription());
            pstmt.setString(3, transaction.getCategory());
            pstmt.setDouble(4, transaction.getAmount());
            pstmt.setString(5, transaction.getType());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an existing transaction in DB
    public static void updateTransaction(int id, Transaction transaction) {
        String query = "UPDATE transactions SET date = ?, description = ?, category = ?, amount = ?, type = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Update the fields based on transaction details
            pstmt.setString(1, transaction.getDate());
            pstmt.setString(2, transaction.getDescription());
            pstmt.setString(3, transaction.getCategory());
            pstmt.setDouble(4, transaction.getAmount());
            pstmt.setString(5, transaction.getType());
            pstmt.setInt(6, id);  // Specify which transaction (by ID)
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a transaction from DB
    public static void deleteTransaction(int id) {
        String query = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);  // Specify which transaction (by ID)
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all transactions from DB
    public static ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Loop through the result set and create Transaction objects
            while (rs.next()) {
                int id = rs.getInt("id");
                String date = rs.getString("date");
                String description = rs.getString("description");
                String category = rs.getString("category");
                double amount = rs.getDouble("amount");
                String type = rs.getString("type");

                // Add transaction to list
                transactions.add(new Transaction(id, date, description, category, amount, type));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}
