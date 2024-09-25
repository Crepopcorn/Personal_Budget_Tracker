import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Connection details
    private static final String URL = "jdbc:mysql://localhost:3306/budget_tracker";
    private static final String USER = "root";  // DB username
    private static final String PASSWORD = "Mypwd_3016";  // DB password

    // Get the connection to the DB
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e.getMessage());
        }

        // Return the connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
