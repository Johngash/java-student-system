package com.university.system.controller;

import com.university.system.database.DatabaseConnection;
import com.university.system.model.Lecturer;

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
}
