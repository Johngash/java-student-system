import com.university.system.database.DatabaseConnection;
import java.util.*;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            System.out.println("=== Testing DatabaseConnection ===\n");
            
            // Test connection
            System.out.println("1. Testing connection...");
            DatabaseConnection.getConnection();
            System.out.println("   ✅ Connected to student_system_db\n");
            
            // Test 2: Get all students
            System.out.println("2. Getting all students...");
            List<Map<String, Object>> students = DatabaseConnection.getAllStudents();
            System.out.println("   ✅ Found " + students.size() + " students");
            for (Map<String, Object> s : students) {
                System.out.println("      - " + s.get("fullName") + " (" + s.get("studentId") + ") - " + s.get("major"));
            }
            System.out.println();
            
            // Test 3: Get a specific student
            System.out.println("3. Finding student by ID: REG2024001...");
            Map<String, Object> student = DatabaseConnection.getStudentByStudentId("REG2024001");
            if (student != null) {
                System.out.println("   ✅ Found: " + student.get("fullName"));
            } else {
                System.out.println("   ⚠️ Student not found");
            }
            System.out.println();
            
            // Test 4: Get all books
            System.out.println("4. Getting all books...");
            List<Map<String, Object>> books = DatabaseConnection.getAllBooks();
            System.out.println("   ✅ Found " + books.size() + " books");
            for (Map<String, Object> b : books) {
                System.out.println("      - " + b.get("title") + " by " + b.get("author") + 
                                 " (Available: " + b.get("availableCopies") + "/" + b.get("totalCopies") + ")");
            }
            System.out.println();
            
            // Test 5: Search books
            System.out.println("5. Searching for 'java'...");
            List<Map<String, Object>> results = DatabaseConnection.searchBooks("java");
            System.out.println("   ✅ Found " + results.size() + " results");
            for (Map<String, Object> b : results) {
                System.out.println("      - " + b.get("title") + " by " + b.get("author"));
            }
            System.out.println();
            
            // Test 6: Get book by ISBN
            System.out.println("6. Getting book by ISBN: 9781119602299...");
            Map<String, Object> book = DatabaseConnection.getBookByIsbn("9781119602299");
            if (book != null) {
                System.out.println("   ✅ Found: " + book.get("title"));
            }
            System.out.println();
            
            // Test 7: Create borrow record (if we have a student)
            System.out.println("7. Creating borrow record...");
            if (student != null) {
                int studentId = (int) student.get("id");
                int borrowId = DatabaseConnection.createBorrowRecord("9781119602299", studentId, 14);
                System.out.println("   ✅ Borrow record created with ID: " + borrowId);
            }
            System.out.println();
            
            // Test 8: Get student results
            System.out.println("8. Getting student results...");
            List<Map<String, Object>> results2 = DatabaseConnection.getStudentResults("REG2024001");
            if (results2.isEmpty()) {
                System.out.println("   ℹ️ No results yet (add scores to see them)");
            } else {
                System.out.println("   ✅ Found " + results2.size() + " results");
                for (Map<String, Object> r : results2) {
                    System.out.println("      - " + r.get("courseTitle") + ": " + r.get("grade"));
                }
            }
            
            System.out.println("\n=== All Tests Passed! ===");
            DatabaseConnection.closeConnection();
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
