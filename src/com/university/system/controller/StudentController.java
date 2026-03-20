package com.university.system.controller;

import model.Student;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {

    // =========================
    // CREATE (INSERT)
    // =========================
    public boolean addStudent(String regNumber, String name, String programme) {

        String sql = "INSERT INTO students (reg_number, name, programme) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set values
            stmt.setString(1, regNumber);
            stmt.setString(2, name);
            stmt.setString(3, programme);

            // Execute query
            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error adding student:");
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // READ (GET ONE STUDENT)
    // =========================
    public Student getStudent(String regNumber) {

        String sql = "SELECT * FROM students WHERE reg_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, regNumber);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String programme = rs.getString("programme");

                return new Student(regNumber, name, programme);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving student:");
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // READ (GET ALL STUDENTS)
    // =========================
    public List<Student> getAllStudents() {

        List<Student> studentList = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                String regNumber = rs.getString("reg_number");
                String name = rs.getString("name");
                String programme = rs.getString("programme");

                Student student = new Student(regNumber, name, programme);
                studentList.add(student);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving all students:");
            e.printStackTrace();
        }

        return studentList;
    }

    // =========================
    // UPDATE
    // =========================
    public boolean updateStudent(String regNumber, String name, String programme) {

        String sql = "UPDATE students SET name = ?, programme = ? WHERE reg_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, programme);
            stmt.setString(3, regNumber);

            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error updating student:");
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // DELETE
    // =========================
    public boolean deleteStudent(String regNumber) {

        String sql = "DELETE FROM students WHERE reg_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, regNumber);

            int rowsDeleted = stmt.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting student:");
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // VALIDATION (OPTIONAL BUT IMPORTANT)
    // =========================
    public boolean validateStudent(String regNumber, String name, String programme) {

        if (regNumber == null || regNumber.isEmpty()) {
            System.out.println("Registration number is required");
            return false;
        }

        if (name == null || name.isEmpty()) {
            System.out.println("Name is required");
            return false;
        }

        if (programme == null || programme.isEmpty()) {
            System.out.println("Programme is required");
            return false;
        }

        return true;
    }
}