import com.university.system.database.DatabaseConnection;
import com.university.system.model.*;
import java.util.List;

public class StandaloneDBTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== Database Component Test ===\n");
            
            // Test connection
            System.out.println("1. Testing connection...");
            DatabaseConnection.getConnection();
            System.out.println("   ✅ Connected to student_system_db\n");
            
            // Test students
            System.out.println("2. Testing getAllStudents()...");
            List<Student> students = DatabaseConnection.getAllStudents();
            System.out.println("   ✅ Found " + students.size() + " students");
            for (Student s : students) {
                System.out.println("      - " + s.getName() + " (" + s.getStudentId() + ")");
            }
            System.out.println();
            
            // Test books
            System.out.println("3. Testing getAllBooks()...");
            List<Book> books = DatabaseConnection.getAllBooks();
            System.out.println("   ✅ Found " + books.size() + " books");
            for (Book b : books) {
                System.out.println("      - " + b.getTitle() + " by " + b.getAuthor());
            }
            System.out.println();
            
            // Test search
            System.out.println("4. Testing searchBooks('java')...");
            List<Book> results = DatabaseConnection.searchBooks("java");
            System.out.println("   ✅ Found " + results.size() + " results");
            
            System.out.println("\n=== All Tests Passed! ===");
            DatabaseConnection.closeConnection();
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
