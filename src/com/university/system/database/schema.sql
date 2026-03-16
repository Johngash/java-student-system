/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.8.3-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: university_library_system
-- ------------------------------------------------------
-- Server version	11.8.3-MariaDB-0+deb13u1 from Debian

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `book_search`
--

DROP TABLE IF EXISTS `book_search`;
/*!50001 DROP VIEW IF EXISTS `book_search`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `book_search` AS SELECT
 1 AS `isbn`,
  1 AS `title`,
  1 AS `author`,
  1 AS `edition`,
  1 AS `year_published`,
  1 AS `total_copies`,
  1 AS `available_copies`,
  1 AS `borrowed_copies`,
  1 AS `status` */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `borrow_record`
--

DROP TABLE IF EXISTS `borrow_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_code` varchar(20) NOT NULL,
  `title` varchar(100) NOT NULL,
  `credits` int(11) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `lecturer_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`course_code`),
  KEY `lecturer_id` (`lecturer_id`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`lecturer_id`) REFERENCES `lecturer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lecturer`
--

DROP TABLE IF EXISTS `lecturer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturer` (
  `id` int(11) NOT NULL,
  `staff_number` varchar(20) NOT NULL,
  `department` varchar(100) DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `specialization` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `staff_number` (`staff_number`),
  CONSTRAINT `lecturer_ibfk_1` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `overdue_books`
--

DROP TABLE IF EXISTS `overdue_books`;
/*!50001 DROP VIEW IF EXISTS `overdue_books`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `overdue_books` AS SELECT
 1 AS `borrow_id`,
  1 AS `title`,
  1 AS `isbn`,
  1 AS `first_name`,
  1 AS `last_name`,
  1 AS `registration_number`,
  1 AS `borrow_date`,
  1 AS `due_date`,
  1 AS `days_overdue` */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `person_type` enum('student','lecturer') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `registration_number` varchar(20) NOT NULL,
  `programme` varchar(100) NOT NULL,
  `enrollment_date` date DEFAULT NULL,
  `current_year` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `registration_number` (`registration_number`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student_course`
--

DROP TABLE IF EXISTS `student_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_course` (
  `student_id` int(11) NOT NULL,
  `course_code` varchar(20) NOT NULL,
  `enrollment_date` date DEFAULT curdate(),
  PRIMARY KEY (`student_id`,`course_code`),
  KEY `course_code` (`course_code`),
  CONSTRAINT `student_course_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE,
  CONSTRAINT `student_course_ibfk_2` FOREIGN KEY (`course_code`) REFERENCES `course` (`course_code`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `student_result_slip`
--

DROP TABLE IF EXISTS `student_result_slip`;
/*!50001 DROP VIEW IF EXISTS `student_result_slip`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `student_result_slip` AS SELECT
 1 AS `registration_number`,
  1 AS `first_name`,
  1 AS `last_name`,
  1 AS `course_code`,
  1 AS `course_title`,
  1 AS `cat_score`,
  1 AS `exam_score`,
  1 AS `total_score`,
  1 AS `grade` */;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `book_search`
--

/*!50001 DROP VIEW IF EXISTS `book_search`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `book_search` AS select `book`.`isbn` AS `isbn`,`book`.`title` AS `title`,`book`.`author` AS `author`,`book`.`edition` AS `edition`,`book`.`year_published` AS `year_published`,`book`.`total_copies` AS `total_copies`,`book`.`available_copies` AS `available_copies`,`book`.`total_copies` - `book`.`available_copies` AS `borrowed_copies`,case when `book`.`available_copies` > 0 then 'Available' else 'Borrowed Out' end AS `status` from `book` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `overdue_books`
--

/*!50001 DROP VIEW IF EXISTS `overdue_books`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `overdue_books` AS select `br`.`id` AS `borrow_id`,`b`.`title` AS `title`,`b`.`isbn` AS `isbn`,`p`.`first_name` AS `first_name`,`p`.`last_name` AS `last_name`,`s`.`registration_number` AS `registration_number`,`br`.`borrow_date` AS `borrow_date`,`br`.`due_date` AS `due_date`,to_days(curdate()) - to_days(`br`.`due_date`) AS `days_overdue` from (((`borrow_record` `br` join `book` `b` on(`br`.`book_isbn` = `b`.`isbn`)) join `student` `s` on(`br`.`student_id` = `s`.`id`)) join `person` `p` on(`s`.`id` = `p`.`id`)) where `br`.`status` = 'overdue' or `br`.`status` = 'borrowed' and `br`.`due_date` < curdate() */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `student_result_slip`
--

/*!50001 DROP VIEW IF EXISTS `student_result_slip`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `student_result_slip` AS select `s`.`registration_number` AS `registration_number`,`p`.`first_name` AS `first_name`,`p`.`last_name` AS `last_name`,`sc`.`course_code` AS `course_code`,`c`.`title` AS `course_title`,`sc`.`cat_score` AS `cat_score`,`sc`.`exam_score` AS `exam_score`,`sc`.`total_score` AS `total_score`,`sc`.`grade` AS `grade` from (((`student` `s` join `person` `p` on(`s`.`id` = `p`.`id`)) join `score` `sc` on(`s`.`id` = `sc`.`student_id`)) join `course` `c` on(`sc`.`course_code` = `c`.`course_code`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-03-16 16:02:21
