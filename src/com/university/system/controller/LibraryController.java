package com.university.system.controller;

import com.university.system.database.DatabaseConnection;
import com.university.system.model.Book;
import com.university.system.model.BorrowRecord;
import com.university.system.model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryController {

    // =========================
    // BOOK MANAGEMENT
    // =========================

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> searchBooks(String term) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?";
        String pattern = "%" + term + "%";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM book WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =========================
    // BORROWING & RETURNS
    // =========================

    public boolean borrowBook(int studentId, String isbn) {
        String checkSql = "SELECT available_copies FROM book WHERE isbn = ?";
        String insertRecordSql = "INSERT INTO borrow_record (book_isbn, student_id, borrow_date, due_date, status) VALUES (?, ?, ?, ?, 'borrowed')";
        String updateBookSql = "UPDATE book SET available_copies = available_copies - 1 WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertRecordSql);
                 PreparedStatement updateStmt = conn.prepareStatement(updateBookSql)) {

                // 1. Check availability
                checkStmt.setString(1, isbn);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next() || rs.getInt("available_copies") <= 0) {
                        return false; // No copies available
                    }
                }

                // 2. Create borrow record (due in 14 days)
                LocalDate borrowDate = LocalDate.now();
                LocalDate dueDate = borrowDate.plusDays(14);
                insertStmt.setString(1, isbn);
                insertStmt.setInt(2, studentId);
                insertStmt.setDate(3, Date.valueOf(borrowDate));
                insertStmt.setDate(4, Date.valueOf(dueDate));
                insertStmt.executeUpdate();

                // 3. Update book count
                updateStmt.setString(1, isbn);
                updateStmt.executeUpdate();

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

    public boolean returnBook(int borrowRecordId) {
        String getRecordSql = "SELECT * FROM borrow_record WHERE id = ?";
        String updateRecordSql = "UPDATE borrow_record SET return_date = ?, status = 'returned', fine_amount = ? WHERE id = ?";
        String updateBookSql = "UPDATE book SET available_copies = available_copies + 1 WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement getStmt = conn.prepareStatement(getRecordSql);
                 PreparedStatement updateRecStmt = conn.prepareStatement(updateRecordSql);
                 PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql)) {

                // 1. Get record details
                getStmt.setInt(1, borrowRecordId);
                String isbn = null;
                LocalDate dueDate = null;
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (rs.next()) {
                        isbn = rs.getString("book_isbn");
                        dueDate = rs.getDate("due_date").toLocalDate();
                        if ("returned".equals(rs.getString("status"))) return false; // Already returned
                    } else {
                        return false;
                    }
                }

                // 2. Calculate fine (e.g., $0.50 per day)
                LocalDate returnDate = LocalDate.now();
                double fine = 0;
                if (returnDate.isAfter(dueDate)) {
                    long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnDate);
                    fine = daysOverdue * 0.50;
                }

                // 3. Update record
                updateRecStmt.setDate(1, Date.valueOf(returnDate));
                updateRecStmt.setDouble(2, fine);
                updateRecStmt.setInt(3, borrowRecordId);
                updateRecStmt.executeUpdate();

                // 4. Update book count
                updateBookStmt.setString(1, isbn);
                updateBookStmt.executeUpdate();

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
    // RESERVATIONS
    // =========================

    public boolean reserveBook(int studentId, String isbn) {
        String sql = "INSERT INTO reservation (book_isbn, student_id, reservation_date, status) VALUES (?, ?, ?, 'pending')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.setInt(2, studentId);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservation> getStudentReservations(int studentId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getString("book_isbn"),
                        rs.getInt("student_id"),
                        rs.getDate("reservation_date").toLocalDate(),
                        rs.getString("status"),
                        rs.getBoolean("notification_sent")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // =========================
    // HELPERS
    // =========================

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("edition"),
            rs.getString("version"),
            rs.getInt("year_published"),
            rs.getString("publisher"),
            rs.getString("author"),
            rs.getInt("total_copies"),
            rs.getInt("available_copies")
        );
    }
}
