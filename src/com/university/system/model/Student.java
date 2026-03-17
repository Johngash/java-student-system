package com.university.system.model;

public class Student extends Person {
    private String studentId;
    private String major;

    public Student() {
        super();
    }

    public Student(int id, String name, String email, String phone, String studentId, String major) {
        super(id, name, email, phone);
        this.studentId = studentId;
        this.major = major;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", major='" + major + '\'' +
                "} " + super.toString();
    }
}
