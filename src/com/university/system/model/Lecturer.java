package com.university.system.model;

import java.time.LocalDate;

/**
 * Represents a lecturer in the university system.
 * Inherits from Person.
 */
public class Lecturer extends Person {
    private String staffNumber;
    private String department;
    private LocalDate hireDate;
    private String specialization;

    public Lecturer() {
        super();
    }

    public Lecturer(int id, String firstName, String lastName, String email, String phone, String address, boolean isActive,
                    String staffNumber, String department, LocalDate hireDate, String specialization) {
        super(id, firstName, lastName, email, phone, address, isActive);
        this.staffNumber = staffNumber;
        this.department = department;
        this.hireDate = hireDate;
        this.specialization = specialization;
    }

    public String getStaffNumber() { return staffNumber; }
    public void setStaffNumber(String staffNumber) { this.staffNumber = staffNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    @Override
    public String toString() {
        return "Lecturer{" +
                "staffNumber='" + staffNumber + '\'' +
                ", department='" + department + '\'' +
                ", specialization='" + specialization + '\'' +
                "} " + super.toString();
    }
}
