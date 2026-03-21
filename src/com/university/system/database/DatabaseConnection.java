package com.university.system.database;

import com.university.system.model.*;
import java.sql.*;
import java.util.*;

/**
 * Database Access Layer - Simple CRUD operations only
 * Returns model objects, no business logic
 */
public class DatabaseConnection {
    
    // TODO: Change database name to match project naming
    private static final String URL = "jdbc:mysql://localhost:3306/student_system_db";
    private static final String USER = "java_app";
    private static final String PASSWORD = "your_password123"; // Update this
    
    private static Connection connection = null;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Database driver loaded");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
        }
    }
    
    // ========== CONNECTION MANAGEMENT ==========
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    // ========== HELPER ==========
    
    private static String combineName(String firstName, String lastName) {
        return (firstName + " " + lastName).trim();
    }
    
    // ========== STUDENT CRUD ==========
    
    public static List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = """
            SELECT p.id, p.first_name, p.last_name, p.email, p.phone,
                   s.registration_number, s.programme
            FROM person p 
            JOIN student s ON p.id = s.id
            ORDER BY p.last_name, p.first_name
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(combineName(rs.getString("first_name"), rs.getString("last_name")));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setStudentId(rs.getString("registration_number"));
                student.setMajor(rs.getString("programme"));
                students.add(student);
            }
        }
        return students;
    }
    
    public static Student getStudentByStudentId(String studentId) throws SQLException {
        String sql = """
            SELECT p.id, p.first_name, p.last_name, p.email, p.phone,
                   s.registration_number, s.programme
            FROM person p 
            JOIN student s ON p.id = s.id
            WHERE s.registration_number = ?
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(combineName(rs.getString("first_name"), rs.getString("last_name")));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setStudentId(rs.getString("registration_number"));
                student.setMajor(rs.getString("programme"));
                return student;
            }
        }
        return null;
    }
    
    // ========== LECTURER CRUD ==========
    
    public static List<Lecturer> getAllLecturers() throws SQLException {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = """
            SELECT p.id, p.first_name, p.last_name, p.email, p.phone,
                   l.staff_number, l.department
            FROM person p 
            JOIN lecturer l ON p.id = l.id
            ORDER BY p.last_name, p.first_name
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Lecturer lecturer = new Lecturer();
                lecturer.setId(rs.getInt("id"));
                lecturer.setName(combineName(rs.getString("first_name"), rs.getString("last_name")));
                lecturer.setEmail(rs.getString("email"));
                lecturer.setPhone(rs.getString("phone"));
                lecturer.setEmployeeId(rs.getString("staff_number"));
                lecturer.setDepartment(rs.getString("department"));
                lecturers.add(lecturer);
            }
        }
        return lecturers;
    }
    
    // ========== BOOK CRUD ==========
    
    public static List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = """
            SELECT b.isbn, b.title, b.author, b.edition, b.version, 
                   b.year_published, b.total_copies,
                   COUNT(br.id) as borrowed_count
            FROM book b
            LEFT JOIN borrow_record br ON b.isbn = br.book_isbn 
                AND br.status = 'borrowed'
            GROUP BY b.isbn
            ORDER BY b.title
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Book book = new Book();
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setEdition(rs.getString("edition"));
                book.setVersion(rs.getString("version"));
                book.setYearPublished(rs.getInt("year_published"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setBorrowedCopies(rs.getInt("borrowed_count"));
                books.add(book);
            }
        }
        return books;
    }
    
    public static List<Book> searchBooks(String searchTerm) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = """
            SELECT b.isbn, b.title, b.author, b.edition, b.version, 
                   b.year_published, b.total_copies,
                   COUNT(br.id) as borrowed_count
            FROM book b
            LEFT JOIN borrow_record br ON b.isbn = br.book_isbn 
                AND br.status = 'borrowed'
            WHERE b.title LIKE ? OR b.author LIKE ?
            GROUP BY b.isbn
            ORDER BY b.title
            LIMIT 20
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = new Book();
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setEdition(rs.getString("edition"));
                book.setVersion(rs.getString("version"));
                book.setYearPublished(rs.getInt("year_published"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setBorrowedCopies(rs.getInt("borrowed_count"));
                books.add(book);
            }
        }
        return books;
    }
    
    public static Book getBookByIsbn(String isbn) throws SQLException {
        String sql = """
            SELECT b.isbn, b.title, b.author, b.edition, b.version, 
                   b.year_published, b.total_copies,
                   COUNT(br.id) as borrowed_count
            FROM book b
            LEFT JOIN borrow_record br ON b.isbn = br.book_isbn 
                AND br.status = 'borrowed'
            WHERE b.isbn = ?
            GROUP BY b.isbn
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Book book = new Book();
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setEdition(rs.getString("edition"));
                book.setVersion(rs.getString("version"));
                book.setYearPublished(rs.getInt("year_published"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setBorrowedCopies(rs.getInt("borrowed_count"));
                return book;
            }
        }
        return null;
    }
    
    // ========== BORROW RECORD CRUD ==========
    
    public static int createBorrowRecord(String isbn, int studentId, int daysToBorrow) throws SQLException {
        String sql = """
            INSERT INTO borrow_record (book_isbn, student_id, borrow_date, due_date, status)
            VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY), 'borrowed')
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, isbn);
            stmt.setInt(2, studentId);
            stmt.setInt(3, daysToBorrow);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
    
    public static void updateBorrowRecordAsReturned(int borrowId, double fine) throws SQLException {
        String sql = """
            UPDATE borrow_record 
            SET return_date = CURDATE(), status = 'returned', fine_amount = ? 
            WHERE id = ?
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setDouble(1, fine);
            stmt.setInt(2, borrowId);
            stmt.executeUpdate();
        }
    }
    
    // ========== SCORE/RESULT CRUD ==========
    
    public static List<Score> getStudentResults(String studentId) throws SQLException {
        List<Score> scores = new ArrayList<>();
        String sql = """
            SELECT sc.id, sc.course_code, c.title AS course_title,
                   sc.cat_score, sc.exam_score, sc.total_score, sc.grade,
                   sc.academic_year, sc.semester
            FROM score sc
            JOIN student s ON sc.student_id = s.id
            JOIN course c ON sc.course_code = c.course_code
            WHERE s.registration_number = ?
            ORDER BY sc.academic_year DESC, sc.semester, c.title
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Score score = new Score();
                score.setId(rs.getInt("id"));
                score.setCourseCode(rs.getString("course_code"));
                score.setCourseTitle(rs.getString("course_title"));
                score.setCatScore(rs.getDouble("cat_score"));
                score.setExamScore(rs.getDouble("exam_score"));
                score.setTotalScore(rs.getDouble("total_score"));
                score.setGrade(rs.getString("grade"));
                score.setAcademicYear(rs.getString("academic_year"));
                score.setSemester(rs.getInt("semester"));
                scores.add(score);
            }
        }
        return scores;
    }
}
