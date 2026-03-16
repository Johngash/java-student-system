package com.university.system.model;

ls/**
 * Represents a lecturer in the university system.
 * Inherits from Person.
 */
public class Lecturer extends Person {
    private String employeeId;
    private String department;

    public Lecturer() {
        super();
    }

    public Lecturer(int id, String name, String email, String phone, String employeeId, String department) {
        super(id, name, email, phone);
        this.employeeId = employeeId;
        this.department = department;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                "} " + super.toString();
    }
}
