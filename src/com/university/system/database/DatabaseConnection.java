package com.university.system.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/university_library_system";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Update with your actual password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static Connection connection;

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> getAllStudents() throws SQLException {
        String sql = "SELECT p.*, s.registration_number, s.programme, s.current_year " +
                     "FROM person p JOIN student s ON p.id = s.id";
        return executeQuery(sql);
    }

    public static List<Map<String, Object>> getAllBooks() throws SQLException {
        return executeQuery("SELECT * FROM book");
    }

    public static List<Map<String, Object>> getAvailableBooks() throws SQLException {
        return executeQuery("SELECT * FROM book WHERE available_copies > 0");
    }

    public static List<Map<String, Object>> getOverdueBooks() throws SQLException {
        return executeQuery("SELECT * FROM overdue_books");
    }

    public static Map<String, Object> getStudentByRegNumber(String regNumber) throws SQLException {
        String sql = "SELECT p.*, s.registration_number, s.programme " +
                     "FROM person p JOIN student s ON p.id = s.id " +
                     "WHERE s.registration_number = ?";
        List<Map<String, Object>> results = executeQuery(sql, regNumber);
        return results.isEmpty() ? null : results.get(0);
    }

    public static List<Map<String, Object>> getStudentResults(String regNumber) throws SQLException {
        String sql = "SELECT * FROM student_result_slip WHERE registration_number = ?";
        return executeQuery(sql, regNumber);
    }

    public static List<Map<String, Object>> searchBooks(String term) throws SQLException {
        String sql = "SELECT * FROM book WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?";
        String pattern = "%" + term + "%";
        return executeQuery(sql, pattern, pattern, pattern);
    }

    private static List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columns; i++) {
                        row.put(md.getColumnName(i), rs.getObject(i));
                    }
                    list.add(row);
                }
            }
        }
        return list;
    }
}
