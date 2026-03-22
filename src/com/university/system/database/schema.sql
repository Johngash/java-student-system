-- University Library System Database Schema

-- Table: person
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `person_type` enum('student','lecturer') NOT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: lecturer
CREATE TABLE `lecturer` (
  `id` int(11) NOT NULL,
  `staff_number` varchar(20) NOT NULL,
  `department` varchar(100) DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `specialization` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `staff_number` (`staff_number`),
  CONSTRAINT `lecturer_ibfk_1` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: student
CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `registration_number` varchar(20) NOT NULL,
  `programme` varchar(100) NOT NULL,
  `enrollment_date` date DEFAULT NULL,
  `current_year` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `registration_number` (`registration_number`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: book
CREATE TABLE `book` (
  `isbn` varchar(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `edition` varchar(50) DEFAULT NULL,
  `version` varchar(50) DEFAULT NULL,
  `year_published` int(11) DEFAULT NULL,
  `publisher` varchar(100) DEFAULT NULL,
  `author` varchar(200) DEFAULT NULL,
  `total_copies` int(11) DEFAULT 1,
  `available_copies` int(11) DEFAULT 1,
  PRIMARY KEY (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: course
CREATE TABLE `course` (
  `course_code` varchar(20) NOT NULL,
  `title` varchar(100) NOT NULL,
  `credits` int(11) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `lecturer_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`course_code`),
  KEY `lecturer_id` (`lecturer_id`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`lecturer_id`) REFERENCES `lecturer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: borrow_record
CREATE TABLE `borrow_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_isbn` varchar(20) DEFAULT NULL,
  `student_id` int(11) DEFAULT NULL,
  `borrow_date` date DEFAULT curdate(),
  `due_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `status` enum('borrowed','returned','overdue') DEFAULT 'borrowed',
  `fine_amount` decimal(10,2) DEFAULT 0.00,
  PRIMARY KEY (`id`),
  KEY `book_isbn` (`book_isbn`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `borrow_record_ibfk_1` FOREIGN KEY (`book_isbn`) REFERENCES `book` (`isbn`) ON DELETE CASCADE,
  CONSTRAINT `borrow_record_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: reservation
CREATE TABLE `reservation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_isbn` varchar(20) DEFAULT NULL,
  `student_id` int(11) DEFAULT NULL,
  `reservation_date` date DEFAULT curdate(),
  `status` enum('pending','fulfilled','cancelled') DEFAULT 'pending',
  `notification_sent` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `book_isbn` (`book_isbn`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`book_isbn`) REFERENCES `book` (`isbn`) ON DELETE CASCADE,
  CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: score
CREATE TABLE `score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `course_code` varchar(20) NOT NULL,
  `cat_score` decimal(5,2) DEFAULT NULL CHECK (`cat_score` >= 0 and `cat_score` <= 30),
  `exam_score` decimal(5,2) DEFAULT NULL CHECK (`exam_score` >= 0 and `exam_score` <= 70),
  `total_score` decimal(5,2) GENERATED ALWAYS AS (`cat_score` + `exam_score`) STORED,
  `grade` varchar(2) DEFAULT NULL,
  `academic_year` varchar(20) DEFAULT NULL,
  `semester` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_student_course` (`student_id`,`course_code`,`academic_year`,`semester`),
  KEY `course_code` (`course_code`),
  CONSTRAINT `score_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE,
  CONSTRAINT `score_ibfk_2` FOREIGN KEY (`course_code`) REFERENCES `course` (`course_code`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: student_course
CREATE TABLE `student_course` (
  `student_id` int(11) NOT NULL,
  `course_code` varchar(20) NOT NULL,
  `enrollment_date` date DEFAULT curdate(),
  PRIMARY KEY (`student_id`,`course_code`),
  KEY `course_code` (`course_code`),
  CONSTRAINT `student_course_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE,
  CONSTRAINT `student_course_ibfk_2` FOREIGN KEY (`course_code`) REFERENCES `course` (`course_code`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- View: book_search
CREATE VIEW `book_search` AS 
select `book`.`isbn` AS `isbn`,`book`.`title` AS `title`,`book`.`author` AS `author`,`book`.`edition` AS `edition`,`book`.`year_published` AS `year_published`,`book`.`total_copies` AS `total_copies`,`book`.`available_copies` AS `available_copies`,`book`.`total_copies` - `book`.`available_copies` AS `borrowed_copies`,
case when `book`.`available_copies` > 0 then 'Available' else 'Borrowed Out' end AS `status` 
from `book`;

-- View: overdue_books
CREATE VIEW `overdue_books` AS 
select `br`.`id` AS `borrow_id`,`b`.`title` AS `title`,`b`.`isbn` AS `isbn`,`p`.`first_name` AS `first_name`,`p`.`last_name` AS `last_name`,`s`.`registration_number` AS `registration_number`,`br`.`borrow_date` AS `borrow_date`,`br`.`due_date` AS `due_date`,
to_days(curdate()) - to_days(`br`.`due_date`) AS `days_overdue` 
from `borrow_record` `br` 
join `book` `b` on `br`.`book_isbn` = `b`.`isbn`
join `student` `s` on `br`.`student_id` = `s`.`id`
join `person` `p` on `s`.`id` = `p`.`id`
where `br`.`status` = 'overdue' or (`br`.`status` = 'borrowed' and `br`.`due_date` < curdate());

-- View: student_result_slip
CREATE VIEW `student_result_slip` AS 
select `s`.`registration_number` AS `registration_number`,`p`.`first_name` AS `first_name`,`p`.`last_name` AS `last_name`,`sc`.`course_code` AS `course_code`,`c`.`title` AS `course_title`,`sc`.`cat_score` AS `cat_score`,`sc`.`exam_score` AS `exam_score`,`sc`.`total_score` AS `total_score`,`sc`.`grade` AS `grade` 
from `student` `s` 
join `person` `p` on `s`.`id` = `p`.`id`
join `score` `sc` on `s`.`id` = `sc`.`student_id`
join `course` `c` on `sc`.`course_code` = `c`.`course_code`;
