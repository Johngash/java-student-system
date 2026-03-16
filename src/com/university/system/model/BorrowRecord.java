package com.university.system.model;

import java.time.LocalDate;

/**
 * BorrowRecord tracks WHEN a student borrowed a book and WHEN they returned it.
 * This is needed to know overdue books.
 * Loan period = 14 days by default.
 */
public class BorrowRecord {

    private static final int LOAN_DAYS = 14; 

    private Student   student;
    private Book      book;
    private LocalDate borrowDate;   
    private LocalDate dueDate;      
    private LocalDate returnDate;  
    private boolean   isReturned;

    public BorrowRecord(Student student, Book book) {
        this.student    = student;
        this.book       = book;
        this.borrowDate = LocalDate.now();           
        this.dueDate    = borrowDate.plusDays(LOAN_DAYS); 
        this.isReturned = false;
        this.returnDate = null;
    }

    public BorrowRecord() {}

    
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.isReturned = true;
        book.returnCopy(); 
        System.out.println(student.getName() + " returned: " + book.getTitle());
    }

    public boolean isOverdue() {
        if (isReturned) return false;
        return LocalDate.now().isAfter(dueDate);
    }

    public long daysOverdue() {
        if (!isOverdue()) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }


    public Student   getStudent()    { return student; }
    public Book      getBook()       { return book; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate()    { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean   isReturned()    { return isReturned; }

    
    public void setStudent(Student student)      { this.student = student; }
    public void setBook(Book book)               { this.book = book; }
    public void setBorrowDate(LocalDate d)       { this.borrowDate = d; }
    public void setDueDate(LocalDate d)          { this.dueDate = d; }
    public void setReturnDate(LocalDate d)       { this.returnDate = d; }
    public void setReturned(boolean returned)    { this.isReturned = returned; }

    @Override
    public String toString() {
        String status = isReturned ? "Returned on " + returnDate
                                   : (isOverdue() ? "OVERDUE by " + daysOverdue() + " days"
                                                  : "Due: " + dueDate);
        return student.getName() + " | " + book.getTitle() +
               " | Borrowed: " + borrowDate + " | " + status;
    }
}
