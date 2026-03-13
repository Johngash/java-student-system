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

## Compilation and Execution

### Prerequisites

- Java Development Kit (JDK) 8 or higher.

### Compiling the Project

To compile all Java source files into the `bin/` directory, run the following command from the root directory:

```bash
javac -d bin src/com/university/system/*.java \
      src/com/university/system/database/*.java \
      src/com/university/system/model/*.java \
      src/com/university/system/controller/*.java \
      src/com/university/system/view/*.java
```

Alternatively, you can use the following command to find and compile all Java files at once:

```bash
find src -name "*.java" | xargs javac -d bin
```

### Running the Application

Once compiled, run the application from the root directory using:

```bash
java -cp bin com.university.system.Main
```
