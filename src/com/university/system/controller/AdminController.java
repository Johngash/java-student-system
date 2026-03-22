package com.university.system.controller;

import com.university.system.database.DatabaseConnection;
import com.university.system.model.Book;
import com.university.system.model.BorrowRecord;
import com.university.system.model.Course;
import com.university.system.model.Lecturer;
import com.university.system.model.Reservation;
import com.university.system.model.Score;
import com.university.system.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminController {

  private final StudentController studentController;
  private final LecturerController lecturerController;

  public AdminController() {
    this.studentController = new StudentController();
    this.lecturerController = new LecturerController();
  }

  // ========== PERSON MANAGEMENT ==========

  // --- Student ---
  public boolean addStudent(Student student) {
    return studentController.addStudent(student);
  }

  public boolean updateStudentDetails(Student student) {
    return studentController.updateStudent(student);
  }

  public boolean deactivateStudentAccount(int personId) {
    return studentController.deactivateStudent(personId);
  }

  public List<Student> viewAllStudents() {
    return studentController.getAllStudents();
  }

  public Student viewSpecificStudentById(int personId) {
    return studentController.getStudentById(personId);
  }

  public Student viewSpecificStudentByRegistrationNumber(String regNumber) {
    return studentController.getStudent(regNumber);
  }

  // --- Lecturer ---
  public boolean addLecturer(Lecturer lecturer) {
    return lecturerController.addLecturer(lecturer);
  }

  public boolean updateLecturerDetails(Lecturer lecturer) {
    return lecturerController.updateLecturer(lecturer);
  }

  public boolean deactivateLecturerAccount(int personId) {
    return lecturerController.deactivateLecturer(personId);
  }

  public List<Lecturer> viewAllLecturers() {
    return lecturerController.getAllLecturers();
  }

  public Lecturer viewSpecificLecturerById(int personId) {
    return lecturerController.getLecturerById(personId);
  }

  public Lecturer viewSpecificLecturerByStaffNumber(String staffNumber) {
    return lecturerController.getLecturerByStaffNumber(staffNumber);
  }

  // ========== COURSE MANAGEMENT ==========

  public boolean addCourse(Course course) {
    String sql =
        "INSERT INTO course (course_code, title, credits, description, lecturer_id) VALUES (?, ?,"
            + " ?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, course.getCourseCode());
      stmt.setString(2, course.getTitle());
      stmt.setInt(3, course.getCredits());
      stmt.setString(4, course.getDescription());
      if (course.getLecturerId() > 0) {
        stmt.setInt(5, course.getLecturerId());
      } else {
        stmt.setNull(5, java.sql.Types.INTEGER);
      }
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateCourse(Course course) {
    String sql =
        "UPDATE course SET title=?, credits=?, description=?, lecturer_id=? WHERE course_code=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, course.getTitle());
      stmt.setInt(2, course.getCredits());
      stmt.setString(3, course.getDescription());
      if (course.getLecturerId() > 0) {
        stmt.setInt(4, course.getLecturerId());
      } else {
        stmt.setNull(4, java.sql.Types.INTEGER);
      }
      stmt.setString(5, course.getCourseCode());
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteCourse(String courseCode) {
    String sql = "DELETE FROM course WHERE course_code=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, courseCode);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<Course> viewAllCourses() {
    List<Course> courses = new ArrayList<>();
    String sql = "SELECT * FROM course";
    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        courses.add(
            new Course(
                rs.getString("course_code"),
                rs.getString("title"),
                rs.getInt("credits"),
                rs.getString("description"),
                rs.getInt("lecturer_id")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return courses;
  }

  public Course viewSpecificCourseByCourseCode(String courseCode) {
    String sql = "SELECT * FROM course WHERE course_code=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, courseCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Course(
              rs.getString("course_code"),
              rs.getString("title"),
              rs.getInt("credits"),
              rs.getString("description"),
              rs.getInt("lecturer_id"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean assignLecturerToCourse(int lecturerId, String courseCode) {
    String sql = "UPDATE course SET lecturer_id=? WHERE course_code=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, lecturerId);
      stmt.setString(2, courseCode);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean unassignLecturerFromCourse(String courseCode) {
    String sql = "UPDATE course SET lecturer_id=NULL WHERE course_code=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, courseCode);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== ENROLLMENT MANAGEMENT ==========

  public boolean enrollStudentInCourse(int studentId, String courseCode) {
    String sql = "INSERT INTO student_course (student_id, course_code) VALUES (?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, studentId);
      stmt.setString(2, courseCode);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean unenrollStudentFromCourse(int studentId, String courseCode) {
    String sql = "DELETE FROM student_course WHERE student_id=? AND course_code=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, studentId);
      stmt.setString(2, courseCode);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<Student> viewAllStudentsInCourse(String courseCode) {
    List<Student> students = new ArrayList<>();
    String sql =
        "SELECT s.*, p.*, s.id as student_id, p.id as person_id FROM student s "
            + "JOIN person p ON s.id = p.id "
            + "JOIN student_course sc ON s.id = sc.student_id "
            + "WHERE sc.course_code = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, courseCode);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          students.add(
              new Student(
                  rs.getInt("student_id"),
                  rs.getString("first_name"),
                  rs.getString("last_name"),
                  rs.getString("email"),
                  rs.getString("phone"),
                  rs.getString("address"),
                  rs.getBoolean("is_active"),
                  rs.getString("registration_number"),
                  rs.getString("programme"),
                  rs.getDate("enrollment_date").toLocalDate(),
                  rs.getInt("current_year")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return students;
  }

  public List<Course> viewAllCoursesForStudent(int studentId) {
    List<Course> courses = new ArrayList<>();
    String sql =
        "SELECT c.* FROM course c "
            + "JOIN student_course sc ON c.course_code = sc.course_code "
            + "WHERE sc.student_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, studentId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          courses.add(
              new Course(
                  rs.getString("course_code"),
                  rs.getString("title"),
                  rs.getInt("credits"),
                  rs.getString("description"),
                  rs.getInt("lecturer_id")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return courses;
  }

  // ========== SCORE MANAGEMENT ==========

  public List<Score> viewScoresForStudent(int studentId) {
    List<Score> scores = new ArrayList<>();
    String sql = "SELECT * FROM score WHERE student_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, studentId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          scores.add(
              new Score(
                  rs.getInt("id"),
                  rs.getInt("student_id"),
                  rs.getString("course_code"),
                  rs.getDouble("cat_score"),
                  rs.getDouble("exam_score"),
                  rs.getString("grade"),
                  rs.getString("academic_year"),
                  rs.getInt("semester")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return scores;
  }

  public List<Score> viewScoresForCourse(String courseCode) {
    List<Score> scores = new ArrayList<>();
    String sql = "SELECT * FROM score WHERE course_code = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, courseCode);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          scores.add(
              new Score(
                  rs.getInt("id"),
                  rs.getInt("student_id"),
                  rs.getString("course_code"),
                  rs.getDouble("cat_score"),
                  rs.getDouble("exam_score"),
                  rs.getString("grade"),
                  rs.getString("academic_year"),
                  rs.getInt("semester")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return scores;
  }

  public List<Score> viewAllScores() {
    List<Score> scores = new ArrayList<>();
    String sql = "SELECT * FROM score";
    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        scores.add(
            new Score(
                rs.getInt("id"),
                rs.getInt("student_id"),
                rs.getString("course_code"),
                rs.getDouble("cat_score"),
                rs.getDouble("exam_score"),
                rs.getString("grade"),
                rs.getString("academic_year"),
                rs.getInt("semester")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return scores;
  }

  public ResultSet generateResultSlip(String registrationNumber) {
    String sql = "SELECT * FROM student_result_slip WHERE registration_number = ?";
    try {
      Connection conn = DatabaseConnection.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, registrationNumber);
      return stmt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // ========== LIBRARY OVERSIGHT ==========

  public boolean addBook(Book book) {
    String sql =
        "INSERT INTO book (isbn, title, edition, version, year_published, publisher, author,"
            + " total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, book.getIsbn());
      stmt.setString(2, book.getTitle());
      stmt.setString(3, book.getEdition());
      stmt.setString(4, book.getVersion());
      stmt.setInt(5, book.getYearPublished());
      stmt.setString(6, book.getPublisher());
      stmt.setString(7, book.getAuthor());
      stmt.setInt(8, book.getTotalCopies());
      stmt.setInt(9, book.getAvailableCopies());
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateBookDetails(Book book) {
    String sql =
        "UPDATE book SET title=?, edition=?, version=?, year_published=?, publisher=?, author=?,"
            + " total_copies=?, available_copies=? WHERE isbn=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, book.getTitle());
      stmt.setString(2, book.getEdition());
      stmt.setString(3, book.getVersion());
      stmt.setInt(4, book.getYearPublished());
      stmt.setString(5, book.getPublisher());
      stmt.setString(6, book.getAuthor());
      stmt.setInt(7, book.getTotalCopies());
      stmt.setInt(8, book.getAvailableCopies());
      stmt.setString(9, book.getIsbn());
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean removeBook(String isbn) {
    String sql = "DELETE FROM book WHERE isbn=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, isbn);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<BorrowRecord> viewAllBorrowRecords() {
    List<BorrowRecord> records = new ArrayList<>();
    String sql = "SELECT * FROM borrow_record";
    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        records.add(
            new BorrowRecord(
                rs.getInt("id"),
                rs.getString("book_isbn"),
                rs.getInt("student_id"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                rs.getString("status"),
                rs.getDouble("fine_amount")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  public List<BorrowRecord> viewStudentBorrowHistory(int studentId) {
    List<BorrowRecord> records = new ArrayList<>();
    String sql = "SELECT * FROM borrow_record WHERE student_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, studentId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          records.add(
              new BorrowRecord(
                  rs.getInt("id"),
                  rs.getString("book_isbn"),
                  rs.getInt("student_id"),
                  rs.getDate("borrow_date").toLocalDate(),
                  rs.getDate("due_date").toLocalDate(),
                  rs.getDate("return_date") != null
                      ? rs.getDate("return_date").toLocalDate()
                      : null,
                  rs.getString("status"),
                  rs.getDouble("fine_amount")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  public List<Map<String, Object>> viewAllOverdueBooks() {
    List<Map<String, Object>> overdueBooks = new ArrayList<>();
    String sql = "SELECT * FROM overdue_books";
    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        // Since overdue_books is a view with joins, we'll just return a map
        // to avoid creating a complex a non-model object
        Map<String, Object> overdueBook = new java.util.HashMap<>();
        overdueBook.put("borrow_id", rs.getInt("borrow_id"));
        overdueBook.put("title", rs.getString("title"));
        overdueBook.put("isbn", rs.getString("isbn"));
        overdueBook.put("first_name", rs.getString("first_name"));
        overdueBook.put("last_name", rs.getString("last_name"));
        overdueBook.put("registration_number", rs.getString("registration_number"));
        overdueBook.put("borrow_date", rs.getDate("borrow_date"));
        overdueBook.put("due_date", rs.getDate("due_date"));
        overdueBook.put("days_overdue", rs.getInt("days_overdue"));
        overdueBooks.add(overdueBook);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return overdueBooks;
  }

  public List<Reservation> viewAllPendingReservations() {
    List<Reservation> reservations = new ArrayList<>();
    String sql = "SELECT * FROM reservation WHERE status = 'pending'";
    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        reservations.add(
            new Reservation(
                rs.getInt("id"),
                rs.getString("book_isbn"),
                rs.getInt("student_id"),
                rs.getDate("reservation_date").toLocalDate(),
                rs.getString("status"),
                rs.getBoolean("notification_sent")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return reservations;
  }

  // ========== REPORTS ==========

  public Map<String, Object> fullStudentReport(int studentId) {
    Map<String, Object> report = new java.util.HashMap<>();
    report.put("profile", studentController.getStudentById(studentId));
    report.put("courses", viewAllCoursesForStudent(studentId));
    report.put("scores", viewScoresForStudent(studentId));
    report.put("library", viewStudentBorrowHistory(studentId));
    return report;
  }

  public Map<String, Object> fullLecturerReport(int lecturerId) {
    Map<String, Object> report = new java.util.HashMap<>();
    report.put("profile", lecturerController.getLecturerById(lecturerId));
    // Get courses taught by lecturer
    List<Course> courses = new ArrayList<>();
    String sql = "SELECT * FROM course WHERE lecturer_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, lecturerId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          courses.add(
              new Course(
                  rs.getString("course_code"),
                  rs.getString("title"),
                  rs.getInt("credits"),
                  rs.getString("description"),
                  rs.getInt("lecturer_id")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    report.put("courses", courses);
    return report;
  }

  public List<Map<String, Object>> overdueBooksReport() {
    return viewAllOverdueBooks();
  }

  public List<Map<String, Object>> courseEnrollmentReport(String courseCode) {
    List<Map<String, Object>> report = new ArrayList<>();
    String sql =
        "SELECT p.first_name, p.last_name, s.registration_number, sc.enrollment_date "
            + "FROM student_course sc "
            + "JOIN student s ON sc.student_id = s.id "
            + "JOIN person p ON s.id = p.id "
            + "WHERE sc.course_code = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, courseCode);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Map<String, Object> row = new java.util.HashMap<>();
          row.put("first_name", rs.getString("first_name"));
          row.put("last_name", rs.getString("last_name"));
          row.put("registration_number", rs.getString("registration_number"));
          row.put("enrollment_date", rs.getDate("enrollment_date"));
          report.add(row);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return report;
  }
}
