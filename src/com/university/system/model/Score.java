package com.university.system.model;

/** Represents a student's score in a specific course. */
public class Score {
  private int id;
  private int studentId;
  private String courseCode;
  private double catScore;
  private double examScore;
  private double totalScore; // Stored in DB as generated column
  private String grade;
  private String academicYear;
  private int semester;

  public Score() {}

  public Score(
      int id,
      int studentId,
      String courseCode,
      double catScore,
      double examScore,
      double totalScore,
      String grade,
      String academicYear,
      int semester) {
    this.id = id;
    this.studentId = studentId;
    this.courseCode = courseCode;
    this.catScore = catScore;
    this.examScore = examScore;
    this.totalScore = totalScore;
    this.grade = grade;
    this.academicYear = academicYear;
    this.semester = semester;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getStudentId() {
    return studentId;
  }

  public void setStudentId(int studentId) {
    this.studentId = studentId;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public void setCourseCode(String courseCode) {
    this.courseCode = courseCode;
  }

  public double getCatScore() {
    return catScore;
  }

  public void setCatScore(double catScore) {
    this.catScore = catScore;
  }

  public double getExamScore() {
    return examScore;
  }

  public void setExamScore(double examScore) {
    this.examScore = examScore;
  }

  public double getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(double totalScore) {
    this.totalScore = totalScore;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public String getAcademicYear() {
    return academicYear;
  }

  public void setAcademicYear(String academicYear) {
    this.academicYear = academicYear;
  }

  public int getSemester() {
    return semester;
  }

  public void setSemester(int semester) {
    this.semester = semester;
  }

  @Override
  public String toString() {
    return "Score for " + courseCode + ": " + totalScore + " (Grade: " + grade + ")";
  }
}
