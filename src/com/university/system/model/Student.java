package com.university.system.model;

import java.time.LocalDate;

/**
 * Represents a student in the university system.
 * Inherits from Person.
 */
public class Student extends Person {
    private String registrationNumber;
    private String programme;
    private LocalDate enrollmentDate;
    private int currentYear;

    public Student() {
        super();
    }

    public Student(int id, String firstName, String lastName, String email, String phone, String address, boolean isActive,
                   String registrationNumber, String programme, LocalDate enrollmentDate, int currentYear) {
        super(id, firstName, lastName, email, phone, address, isActive);
        this.registrationNumber = registrationNumber;
        this.programme = programme;
        this.enrollmentDate = enrollmentDate;
        this.currentYear = currentYear;
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getProgramme() { return programme; }
    public void setProgramme(String programme) { this.programme = programme; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public int getCurrentYear() { return currentYear; }
    public void setCurrentYear(int currentYear) { this.currentYear = currentYear; }

    @Override
    public String toString() {
        return "Student{" +
                "regNumber='" + registrationNumber + '\'' +
                ", programme='" + programme + '\'' +
                "} " + super.toString();
    }
}
