package com.university.system.model;

/**
 * Represents a course offered by the university.
 */
public class Course {
    private String courseCode;
    private String title;
    private int credits;
    private String description;
    private int lecturerId;

    public Course() {}

    public Course(String courseCode, String title, int credits, String description, int lecturerId) {
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.description = description;
        this.lecturerId = lecturerId;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getLecturerId() { return lecturerId; }
    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }

    @Override
    public String toString() {
        return "[" + courseCode + "] " + title + " (" + credits + " credits)";
    }
}
