package com.university.system.controller;

import com.university.system.database.DatabaseConnection;
import com.university.system.model.Student;
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
}
