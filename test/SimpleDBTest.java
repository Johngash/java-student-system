import java.sql.*;

public class SimpleDBTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/student_system_db";
        String user = "java_app";
        String password = "your_password123"; // Change this to your actual password
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver loaded!");
            
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to student_system_db!");
            
            // Test 1: Count students
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM student");
            if (rs.next()) {
                System.out.println("📊 Number of students: " + rs.getInt(1));
            }
            
            // Test 2: List students
            rs = stmt.executeQuery("SELECT p.first_name, p.last_name, s.registration_number FROM person p JOIN student s ON p.id = s.id");
            System.out.println("\n👥 Students:");
            while (rs.next()) {
                System.out.println("   - " + rs.getString("first_name") + " " + rs.getString("last_name") + 
                                 " (" + rs.getString("registration_number") + ")");
            }
            
            // Test 3: Count books
            rs = stmt.executeQuery("SELECT COUNT(*) FROM book");
            if (rs.next()) {
                System.out.println("\n📚 Number of books: " + rs.getInt(1));
            }
            
            // Test 4: List books
            rs = stmt.executeQuery("SELECT title, author, available_copies FROM book LIMIT 5");
            System.out.println("\n📖 Books:");
            while (rs.next()) {
                System.out.println("   - " + rs.getString("title") + " by " + rs.getString("author") + 
                                 " (Available: " + rs.getInt("available_copies") + ")");
            }
            
            // Test 5: Search for books
            String searchTerm = "java";
            PreparedStatement pstmt = conn.prepareStatement("SELECT title, author FROM book WHERE title LIKE ?");
            pstmt.setString(1, "%" + searchTerm + "%");
            rs = pstmt.executeQuery();
            System.out.println("\n🔍 Search results for '" + searchTerm + "':");
            while (rs.next()) {
                System.out.println("   - " + rs.getString("title") + " by " + rs.getString("author"));
            }
            
            conn.close();
            System.out.println("\n✅ All tests passed!");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
