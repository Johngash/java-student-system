package com.university.system.model;

import java.time.LocalDate;

public class Reservation {

    private Student   student;
    private Book      book;
    private LocalDate reservationDate;
    private boolean   isFulfilled;    // true when student gets the book


    public Reservation(Student student, Book book) {
        this.student         = student;
        this.book            = book;
        this.reservationDate = LocalDate.now();
        this.isFulfilled     = false;
        System.out.println("Reservation created for " + student.getName() +
                           " — Book: " + book.getTitle());
    }

    public Reservation() {}


    public void fulfill() {
        this.isFulfilled = true;
        System.out.println("Reservation fulfilled for " + student.getName() +
                           " — Book: " + book.getTitle() + " is now available.");
    }
    public Student   getStudent()         { return student; }
    public Book      getBook()            { return book; }
    public LocalDate getReservationDate() { return reservationDate; }
    public boolean   isFulfilled()        { return isFulfilled; }

    public void setStudent(Student student)              { this.student = student; }
    public void setBook(Book book)                       { this.book = book; }
    public void setReservationDate(LocalDate date)       { this.reservationDate = date; }
    public void setFulfilled(boolean fulfilled)          { this.isFulfilled = fulfilled; }

    @Override
    public String toString() {
        return student.getName() + " reserved: " + book.getTitle() +
               " on " + reservationDate +
               " | Status: " + (isFulfilled ? "Fulfilled" : "Waiting");
    }
}
