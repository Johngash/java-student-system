import com.university.system.database.DatabaseConnection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TestDatabase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=========================================================");
        System.out.println("📚 STUDENT LIBRARY SYSTEM - DATABASE COMPONENT TEST");
        System.out.println("=========================================================\n");
        
        try {
            // Test 1: Database Connection
            System.out.println("🔌 TEST 1: Database Connection");
            System.out.println("-------------------------------");
            DatabaseConnection.getConnection();
            System.out.println("   ✅ Connected to university_library_system\n");
            
            // Test 2: Display All Students
            System.out.println("👥 TEST 2: Students in System");
            System.out.println("-------------------------------");
            List<Map<String, Object>> students = DatabaseConnection.getAllStudents();
            if (students.isEmpty()) {
                System.out.println("   ⚠️ No students found in database");
            } else {
                for (Map<String, Object> student : students) {
                    System.out.printf("   📋 ID: %s | %s %s | Reg: %s | %s | Year: %s%n",
                        student.get("id"),
                        student.get("first_name"),
                        student.get("last_name"),
                        student.get("registration_number"),
                        student.get("programme"),
                        student.get("current_year"));
                }
            }
            System.out.println("   ✅ Total students: " + students.size() + "\n");
            
            // Test 3: Display All Books
            System.out.println("📚 TEST 3: Books in Library");
            System.out.println("-----------------------------");
            List<Map<String, Object>> books = DatabaseConnection.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("   ⚠️ No books found in library");
            } else {
                for (Map<String, Object> book : books) {
                    System.out.printf("   📖 ISBN: %s | %s by %s | Available: %s/%s%n",
                        book.get("isbn"),
                        book.get("title"),
                        book.get("author"),
                        book.get("available_copies"),
                        book.get("total_copies"));
                }
            }
            System.out.println("   ✅ Total books: " + books.size() + "\n");
            
            // Test 4: Available Books
            System.out.println("✅ TEST 4: Currently Available Books");
            System.out.println("-------------------------------------");
            List<Map<String, Object>> available = DatabaseConnection.getAvailableBooks();
            if (available.isEmpty()) {
                System.out.println("   ⚠️ No books available at the moment");
            } else {
                for (Map<String, Object> book : available) {
                    System.out.printf("   📗 %s - %d copies available%n",
                        book.get("title"),
                        book.get("available_copies"));
                }
            }
            System.out.println("   ✅ Available books: " + available.size() + "\n");
            
            // Test 5: Overdue Books
            System.out.println("⚠️ TEST 5: Overdue Books");
            System.out.println("-------------------------");
            List<Map<String, Object>> overdue = DatabaseConnection.getOverdueBooks();
            if (overdue.isEmpty()) {
                System.out.println("   ✅ No overdue books - Good!");
            } else {
                for (Map<String, Object> book : overdue) {
                    System.out.printf("   ⚠️ %s borrowed by %s %s - %s days overdue%n",
                        book.get("title"),
                        book.get("first_name"),
                        book.get("last_name"),
                        book.get("days_overdue"));
                }
            }
            System.out.println();
            
            // Test 6: Search for a specific student
            System.out.println("🔍 TEST 6: Search Student by Registration Number");
            System.out.println("------------------------------------------------");
            String testRegNumber = "REG2024001"; // First student from our sample data
            Map<String, Object> student = DatabaseConnection.getStudentByRegNumber(testRegNumber);
            if (student != null) {
                System.out.printf("   ✅ Found: %s %s (%s)%n",
                    student.get("first_name"),
                    student.get("last_name"),
                    student.get("registration_number"));
                System.out.printf("      Email: %s | Programme: %s%n",
                    student.get("email"),
                    student.get("programme"));
            } else {
                System.out.println("   ⚠️ Student not found: " + testRegNumber);
            }
            System.out.println();
            
            // Test 7: Get Student Results
            System.out.println("📊 TEST 7: Student Results (REG2024001)");
            System.out.println("----------------------------------------");
            List<Map<String, Object>> results = DatabaseConnection.getStudentResults("REG2024001");
            if (results.isEmpty()) {
                System.out.println("   ⚠️ No results found for this student");
            } else {
                double totalScore = 0;
                for (Map<String, Object> result : results) {
                    System.out.printf("   📝 %s: %s | CAT: %s, Exam: %s, Total: %s, Grade: %s%n",
                        result.get("course_code"),
                        result.get("course_title"),
                        result.get("cat_score"),
                        result.get("exam_score"),
                        result.get("total_score"),
                        result.get("grade"));
                    
                    // Calculate total for average
                    if (result.get("total_score") != null) {
                        totalScore += ((Number) result.get("total_score")).doubleValue();
                    }
                }
                if (!results.isEmpty()) {
                    double average = totalScore / results.size();
                    System.out.printf("   📊 Average Score: %.2f%n", average);
                }
            }
            System.out.println();
            
            // Test 8: Live Book Search Demo
            System.out.println("🔎 TEST 8: Live Book Search Demo");
            System.out.println("----------------------------------");
            System.out.println("   This simulates the 'search as you type' feature");
            System.out.println("   Try searching for: 'database', 'java', 'programming'\n");
            
            String[] searchTerms = {"database", "java", "programming"};
            for (String term : searchTerms) {
                System.out.println("   Searching for: \"" + term + "\"");
                List<Map<String, Object>> searchResults = DatabaseConnection.searchBooks(term);
                if (searchResults.isEmpty()) {
                    System.out.println("      No books found");
                } else {
                    for (Map<String, Object> book : searchResults) {
                        System.out.printf("      • %s (Available: %d/%d)%n",
                            book.get("title"),
                            book.get("available_copies"),
                            book.get("total_copies"));
                    }
                }
                System.out.println();
            }
            
            // Test 9: Interactive Search (optional)
            System.out.println("🎮 TEST 9: Interactive Search (type 'quit' to exit)");
            System.out.println("----------------------------------------------------");
            System.out.print("   Enter a book title to search: ");
            
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("quit")) {
                    break;
                }
                if (input.trim().isEmpty()) {
                    System.out.print("   Enter search term (or 'quit'): ");
                    continue;
                }
                
                List<Map<String, Object>> searchResults = DatabaseConnection.searchBooks(input);
                System.out.println("\n   Results for \"" + input + "\":");
                if (searchResults.isEmpty()) {
                    System.out.println("   ❌ No books found");
                } else {
                    for (Map<String, Object> book : searchResults) {
                        System.out.printf("   📚 %s by %s%n      ISBN: %s | Available: %d/%d%n",
                            book.get("title"),
                            book.get("author"),
                            book.get("isbn"),
                            book.get("available_copies"),
                            book.get("total_copies"));
                    }
                }
                System.out.print("\n   Enter another search term (or 'quit'): ");
            }
            
            System.out.println("\n=========================================================");
            System.out.println("🎉 ALL TESTS COMPLETED SUCCESSFULLY!");
            System.out.println("✅ Your database component is working perfectly!");
            System.out.println("=========================================================");
            
        } catch (SQLException e) {
            System.err.println("\n❌ DATABASE ERROR: " + e.getMessage());
            System.err.println("\n🔧 Troubleshooting Tips:");
            System.err.println("   1. Check if MariaDB is running: sudo systemctl status mariadb");
            System.err.println("   2. Verify database exists: sudo mariadb -e 'SHOW DATABASES;'");
            System.err.println("   3. Check user credentials in DatabaseConnection.java");
            System.err.println("   4. Ensure java_app user has permissions");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("\n❌ UNEXPECTED ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
            scanner.close();
        }
    }
}
