package com.university.system.controller;

import com.university.system.database.DatabaseConnection;
import com.university.system.model.Lecturer;
import com.university.system.model.Student;
import com.university.system.model.Course;
import com.university.system.model.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerController {

    // =========================
    // CREATE (INSERT)
    // =========================
    public boolean addLecturer(Lecturer lecturer) {
        String sqlPerson = "INSERT INTO person (first_name, last_name, email, phone, address, person_type) VALUES (?, ?, ?, ?, ?, 'lecturer')";
        String sqlLecturer = "INSERT INTO lecturer (id, staff_number, department, hire_date, specialization) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement psPerson = conn.prepareStatement(sqlPerson, Statement.RETURN_GENERATED_KEYS)) {
                psPerson.setString(1, lecturer.getFirstName());
                psPerson.setString(2, lecturer.getLastName());
                psPerson.setString(3, lecturer.getEmail());
                psPerson.setString(4, lecturer.getPhone());
                psPerson.setString(5, lecturer.getAddress());

                int affectedRows = psPerson.executeUpdate();
                if (affectedRows == 0) throw new SQLException("Creating person failed.");

                try (ResultSet generatedKeys = psPerson.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int personId = generatedKeys.getInt(1);
                        lecturer.setId(personId);

                        try (PreparedStatement psLecturer = conn.prepareStatement(sqlLecturer)) {
                            psLecturer.setInt(1, personId);
                            psLecturer.setString(2, lecturer.getStaffNumber());
                            psLecturer.setString(3, lecturer.getDepartment());
                            psLecturer.setDate(4, Date.valueOf(lecturer.getHireDate()));
                            psLecturer.setString(5, lecturer.getSpecialization());
                            psLecturer.executeUpdate();
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
    // READ (GET ONE LECTURER BY STAFF NUMBER)
    // =========================
    public Lecturer getLecturerByStaffNumber(String staffNumber) {
        String sql = "SELECT p.*, l.staff_number, l.department, l.hire_date, l.specialization " +
                     "FROM person p JOIN lecturer l ON p.id = l.id " +
                     "WHERE l.staff_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staffNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Lecturer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("is_active"),
                        rs.getString("staff_number"),
                        rs.getString("department"),
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getString("specialization")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // =========================
    // READ (GET ONE LECTURER BY ID)
    // =========================
    public Lecturer getLecturerById(int personId) {
        String sql = "SELECT p.*, l.staff_number, l.department, l.hire_date, l.specialization " +
                     "FROM person p JOIN lecturer l ON p.id = l.id " +
                     "WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Lecturer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("is_active"),
                        rs.getString("staff_number"),
                        rs.getString("department"),
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getString("specialization")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =========================
    // READ (GET ALL LECTURERS)
    // =========================
    public List<Lecturer> getAllLecturers() {
        List<Lecturer> lecturerList = new ArrayList<>();
        String sql = "SELECT p.*, l.staff_number, l.department, l.hire_date, l.specialization " +
                     "FROM person p JOIN lecturer l ON p.id = l.id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lecturerList.add(new Lecturer(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getBoolean("is_active"),
                    rs.getString("staff_number"),
                    rs.getString("department"),
                    rs.getDate("hire_date").toLocalDate(),
                    rs.getString("specialization")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturerList;
    }

    // =========================
    // UPDATE
    // =========================
    public boolean updateLecturer(Lecturer lecturer) {
        String sqlPerson = "UPDATE person SET first_name=?, last_name=?, email=?, phone=?, address=? WHERE id=?";
        String sqlLecturer = "UPDATE lecturer SET department=?, specialization=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psPerson = conn.prepareStatement(sqlPerson);
                 PreparedStatement psLecturer = conn.prepareStatement(sqlLecturer)) {

                psPerson.setString(1, lecturer.getFirstName());
                psPerson.setString(2, lecturer.getLastName());
                psPerson.setString(3, lecturer.getEmail());
                psPerson.setString(4, lecturer.getPhone());
                psPerson.setString(5, lecturer.getAddress());
                psPerson.setInt(6, lecturer.getId());
                psPerson.executeUpdate();

                psLecturer.setString(1, lecturer.getDepartment());
                psLecturer.setString(2, lecturer.getSpecialization());
                psLecturer.setInt(3, lecturer.getId());
                psLecturer.executeUpdate();

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
    public boolean deactivateLecturer(int personId) {
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
    public boolean deleteLecturer(int personId) {
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
    public List<Course> viewAssignedCourses(int lecturerId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE lecturer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lecturerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(new Course(
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

    public List<Student> viewAllStudentsAcrossCourses(int lecturerId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT DISTINCT s.*, p.*, s.id as student_id, p.id as person_id FROM student s " +
                "JOIN person p ON s.id = p.id " +
                "JOIN student_course sc ON s.id = sc.student_id " +
                "JOIN course c ON sc.course_code = c.course_code " +
                "WHERE c.lecturer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lecturerId);
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

    // =========================
    // SCORES
    // =========================
    public boolean addScore(Score score) {
        String sql = "INSERT INTO score (student_id, course_code, cat_score, exam_score, grade, academic_year, semester) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, score.getStudentId());
            stmt.setString(2, score.getCourseCode());
            stmt.setDouble(3, score.getCatScore());
            stmt.setDouble(4, score.getExamScore());
            stmt.setString(5, score.getGrade());
            stmt.setString(6, score.getAcademicYear());
            stmt.setInt(7, score.getSemester());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateScore(Score score) {
        String sql = "UPDATE score SET cat_score=?, exam_score=?, grade=?, academic_year=?, semester=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, score.getCatScore());
            stmt.setDouble(2, score.getExamScore());
            stmt.setString(3, score.getGrade());
            stmt.setString(4, score.getAcademicYear());
            stmt.setInt(5, score.getSemester());
            stmt.setInt(6, score.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

    public List<Score> viewScoresForStudentInCourse(int studentId, String courseCode) {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT * FROM score WHERE student_id = ? AND course_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setString(2, courseCode);
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
}
