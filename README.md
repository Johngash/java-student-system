# Student Library System

A comprehensive university system for managing students, lecturers, courses, and library resources.

## Project Structure

```text
student-library-system/
├── bin/                        # Compiled .class files
├── src/
│   └── com/
│       └── university/
│           └── system/
│               ├── Main.java
│               ├── database/
│               │   ├── DatabaseConnection.java
│               │   └── schema.sql
│               ├── model/
│               │   ├── Person.java
│               │   ├── Student.java
│               │   ├── Lecturer.java
│               │   ├── Course.java
│               │   ├── Score.java
│               │   ├── Book.java
│               │   ├── BorrowRecord.java
│               │   └── Reservation.java
│               ├── controller/
│               │   ├── AdminController.java
│               │   ├── StudentController.java
│               │   ├── LecturerController.java
│               │   └── LibraryController.java
│               └── view/
│                   ├── MainWindow.java
│                   ├── StudentPanel.java
│                   ├── LecturerPanel.java
│                   └── LibraryPanel.java
└── README.md
```

## Features

The system is structured around several controllers, each managing specific functionalities for different user roles and administrative tasks.

### AdminController
Provides comprehensive management and overview capabilities for administrators:
- **Person Management:** Add, update, deactivate, view all, and view specific students/lecturers.
- **Course Management:** Add, update, delete, view all, view specific courses, assign/unassign lecturers.
- **Enrollment Management:** Enroll/unenroll students in courses, view all students in a course, view all courses for a student.
- **Score Management:** View all scores, view scores for a specific course, generate result slips for students.
- **Library Oversight:** Add, update, remove books, view all borrow records, view student borrow history, view all overdue books, view all pending reservations.
- **Reports:** Generate full student reports, full lecturer reports (including students per course), overdue books reports, and course enrollment reports.

### StudentController
Enables students to manage their personal, academic, and library-related activities:
- **Profile:** View own profile, update own email, phone, and address.
- **Academic:** View own enrolled courses, view own scores by course, view own result slip, and calculate GPA.
- **Library:** Search books, check book availability, reserve/cancel own reservation, view own active borrows, view own borrow history, view own overdue books, and view own fines.

### LecturerController
Allows lecturers to manage their profile, courses, and student scores:
- **Profile:** View own profile, update own email, phone, and address.
- **Academic:** View own assigned courses, view all students in a specific course, and view all students across all own courses.
- **Scores:** Add and update scores for students, view scores for a specific course, view scores for a specific student in own course, and generate result slips for students.

## Compilation and Execution

### Prerequisites

- Java Development Kit (JDK) 8 or higher.

### Compiling the Project

To compile all Java source files into the `bin/` directory, run the following command from the root directory. This command uses `find` to locate all `.java` files and then passes them to `javac` with `src` as the source path.

```bash
javac -d bin -sourcepath src $(find src -name "*.java")
```

Alternatively, if your shell supports globbing (like Bash 4+ or Zsh), you can use:

```bash
javac -d bin -sourcepath src src/com/university/system/**/*.java
```

### Running the Application

Once compiled, run the application from the root directory using:

```bash
java -cp bin com.university.system.Main
```
