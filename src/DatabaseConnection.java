import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/db_makau_130000";
        String user = "root"; // Replace with your MySQL username
        String password = "DirtCheap1411";

        return DriverManager.getConnection(url, user, password);
    }
}
