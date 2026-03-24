package com.university.system.controller;

import com.university.system.database.DatabaseConnection;
import com.university.system.model.Student;
import com.university.system.model.Course;
import com.university.system.model.Score;
import com.university.system.model.Book;
import com.university.system.model.BorrowRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {

  // =========================
  // CREATE (INSERT)
  // =========================
  public boolean addStudent(Student student) {
    String sqlPerson =
        "INSERT INTO person (first_name, last_name, email, phone, address, person_type) VALUES (?,"
            + " ?, ?, ?, ?, 'student')";
    String sqlStudent =
        "INSERT INTO student (id, registration_number, programme, enrollment_date, current_year)"
            + " VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection()) {
      conn.setAutoCommit(false); // Start transaction

      try (PreparedStatement psPerson =
          conn.prepareStatement(sqlPerson, Statement.RETURN_GENERATED_KEYS)) {
        psPerson.setString(1, student.getFirstName());
        psPerson.setString(2, student.getLastName());
        psPerson.setString(3, student.getEmail());
        psPerson.setString(4, student.getPhone());
        psPerson.setString(5, student.getAddress());

        int affectedRows = psPerson.executeUpdate();
        if (affectedRows == 0) throw new SQLException("Creating person failed.");

        try (ResultSet generatedKeys = psPerson.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            int personId = generatedKeys.getInt(1);
            student.setId(personId);

            try (PreparedStatement psStudent = conn.prepareStatement(sqlStudent)) {
              psStudent.setInt(1, personId);
              psStudent.setString(2, student.getRegistrationNumber());
              psStudent.setString(3, student.getProgramme());
              psStudent.setDate(4, Date.valueOf(student.getEnrollmentDate()));
              psStudent.setInt(5, student.getCurrentYear());
              psStudent.executeUpdate();
            }
          } else {
            throw new SQLException("Creating person failed, no ID obtained.");
          }
        }
        conn.commit();
        return true;
      } catch (SQLException e) {
        conn.rollback();
        e.printStackTrace();
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // =========================
  // READ (GET ONE STUDENT)
  // =========================
  public Student getStudent(String regNumber) {
    String sql =
        "SELECT p.*, s.registration_number, s.programme, s.enrollment_date, s.current_year "
            + "FROM person p JOIN student s ON p.id = s.id "
            + "WHERE s.registration_number = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, regNumber);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Student(
              rs.getInt("id"),
              rs.getString("first_name"),
              rs.getString("last_name"),
              rs.getString("email"),
              rs.getString("phone"),
              rs.getString("address"),
              rs.getBoolean("is_active"),
              rs.getString("registration_number"),
              rs.getString("programme"),
              rs.getDate("enrollment_date").toLocalDate(),
              rs.getInt("current_year"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // =========================
  // READ (GET ALL STUDENTS)
  // =========================
  public List<Student> getAllStudents() {
    List<Student> studentList = new ArrayList<>();
    String sql =
        "SELECT p.*, s.registration_number, s.programme, s.enrollment_date, s.current_year "
            + "FROM person p JOIN student s ON p.id = s.id";

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        studentList.add(
            new Student(
                rs.getInt("id"),
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return studentList;
  }

  // =========================
  // READ (GET ONE STUDENT BY ID)
  // =========================
  public Student getStudentById(int personId) {
    String sql =
        "SELECT p.*, s.registration_number, s.programme, s.enrollment_date, s.current_year "
            + "FROM person p JOIN student s ON p.id = s.id "
            + "WHERE p.id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, personId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Student(
              rs.getInt("id"),
              rs.getString("first_name"),
              rs.getString("last_name"),
              rs.getString("email"),
              rs.getString("phone"),
              rs.getString("address"),
              rs.getBoolean("is_active"),
              rs.getString("registration_number"),
              rs.getString("programme"),
              rs.getDate("enrollment_date").toLocalDate(),
              rs.getInt("current_year"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // =========================
  // UPDATE
  // =========================
  public boolean updateStudent(Student student) {
    String sqlPerson = "UPDATE person SET first_name=?, last_name=?, email=?, phone=?, address=? WHERE id=?";
    String sqlStudent = "UPDATE student SET programme=?, current_year=? WHERE id=?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);
        try (PreparedStatement psPerson = conn.prepareStatement(sqlPerson);
             PreparedStatement psStudent = conn.prepareStatement(sqlStudent)) {

            psPerson.setString(1, student.getFirstName());
            psPerson.setString(2, student.getLastName());
            psPerson.setString(3, student.getEmail());
            psPerson.setString(4, student.getPhone());
            psPerson.setString(5, student.getAddress());
            psPerson.setInt(6, student.getId());
            psPerson.executeUpdate();

            psStudent.setString(1, student.getProgramme());
            psStudent.setInt(2, student.getCurrentYear());
            psStudent.setInt(3, student.getId());
            psStudent.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
  }

    // =========================
  // DEACTIVATE
  // =========================
  public boolean deactivateStudent(int personId) {
      String sql = "UPDATE person SET is_active = 0 WHERE id = ?";
      try (Connection conn = DatabaseConnection.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setInt(1, personId);
          return stmt.executeUpdate() > 0;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }

  // =========================
  // DELETE
  // =========================
  public boolean deleteStudent(int personId) {
    // Due to ON DELETE CASCADE, deleting from person deletes from student too
    String sql = "DELETE FROM person WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, personId);
      return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // =========================
  // PROFILE
  // =========================
  public boolean updateEmail(int personId, String email) {
    String sql = "UPDATE person SET email=? WHERE id=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, email);
      stmt.setInt(2, personId);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updatePhone(int personId, String phone) {
    String sql = "UPDATE person SET phone=? WHERE id=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, phone);
      stmt.setInt(2, personId);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateAddress(int personId, String address) {
    String sql = "UPDATE person SET address=? WHERE id=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, address);
      stmt.setInt(2, personId);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // =========================
  // ACADEMIC
  // =========================
    public List<Course> viewEnrolledCourses(int studentId) {
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

    public List<Score> viewScores(int studentId) {
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
                                    rs.getDouble("total_score"),
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

  public ResultSet viewResultSlip(int studentId) {
    // This method can be improved to return a structured object
    String regNumber = getStudentById(studentId).getRegistrationNumber();
    String sql = "SELECT * FROM student_result_slip WHERE registration_number = ?";
    try {
      Connection conn = DatabaseConnection.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, regNumber);
      return stmt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public double calculateGPA(int studentId) {
    List<Score> scores = viewScores(studentId);
    if (scores.isEmpty()) {
      return 0.0;
    }

    double totalGradePoints = 0;
    for (Score score : scores) {
      totalGradePoints += convertGradeToGPA(score.getGrade());
    }

    return totalGradePoints / scores.size();
  }

  private double convertGradeToGPA(String grade) {
    if (grade == null) return 0.0;
    switch (grade.toUpperCase()) {
      case "A": return 4.0;
      case "B": return 3.0;
      case "C": return 2.0;
      case "D": return 1.0;
      default: return 0.0;
    }
  }

  // =========================
  // LIBRARY
  // =========================
  public List<Book> searchBooks(String query) {
    List<Book> books = new ArrayList<>();
    String sql = "SELECT * FROM book_search WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      String searchQuery = "%" + query + "%";
      stmt.setString(1, searchQuery);
      stmt.setString(2, searchQuery);
      stmt.setString(3, searchQuery);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          books.add(new Book(
              rs.getString("isbn"),
              rs.getString("title"),
              rs.getString("author"),
              rs.getString("publisher"),
              rs.getString("edition"),
              rs.getString("version"),
              rs.getInt("year_published"),
              rs.getInt("total_copies"),
              rs.getInt("available_copies")
          ));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return books;
  }

  public boolean checkBookAvailability(String isbn) {
    String sql = "SELECT available_copies FROM book WHERE isbn = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, isbn);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("available_copies") > 0;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean reserveBook(int studentId, String isbn) {
    if (!checkBookAvailability(isbn)) {
      return false;
    }
    String sql = "INSERT INTO reservation (student_id, book_isbn) VALUES (?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, studentId);
      stmt.setString(2, isbn);
      int updatedRows = stmt.executeUpdate();
      if (updatedRows > 0) {
        String sqlUpdate = "UPDATE book SET available_copies = available_copies - 1 WHERE isbn = ?";
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
          stmtUpdate.setString(1, isbn);
          stmtUpdate.executeUpdate();
        }
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean cancelReservation(int reservationId) {
    String sql = "UPDATE reservation SET status = 'cancelled' WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, reservationId);
      int updatedRows = stmt.executeUpdate();
      if (updatedRows > 0) {
        String sqlSelect = "SELECT book_isbn FROM reservation WHERE id = ?";
        try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
          stmtSelect.setInt(1, reservationId);
          try (ResultSet rs = stmtSelect.executeQuery()) {
            if (rs.next()) {
              String isbn = rs.getString("book_isbn");
              String sqlUpdate = "UPDATE book SET available_copies = available_copies + 1 WHERE isbn = ?";
              try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setString(1, isbn);
                stmtUpdate.executeUpdate();
              }
            }
          }
        }
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public List<BorrowRecord> viewActiveBorrows(int studentId) {
    List<BorrowRecord> records = new ArrayList<>();
    String sql = "SELECT * FROM borrow_record WHERE student_id = ? AND status = 'borrowed'";
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

    public List<BorrowRecord> viewBorrowHistory(int studentId) {
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

  public List<BorrowRecord> viewOverdueBooks(int studentId) {
    List<BorrowRecord> records = new ArrayList<>();
    String sql = "SELECT * FROM borrow_record WHERE student_id = ? AND status = 'overdue'";
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

  public double viewFines(int studentId) {
    String sql = "SELECT SUM(fine_amount) FROM borrow_record WHERE student_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, studentId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getDouble(1);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0.0;
  }
}
