import com.university.system.database.DatabaseConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Testing connection...");
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("✅ Connected to student_system_db successfully!");
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
