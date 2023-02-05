/*
SER 322 Deliverable 3 - DDL Statements
Group 3: 
Andronick Martusheff
Keana Gindlesperger
Batbold Altansukh
Giovanna Stern
Na Lee
*/

CREATE DATABASE `school`;

USE `school`;

CREATE TABLE `type` 
    (
        `id` INT NOT NULL,
        `type_value` VARCHAR(25) NOT NULL,
        CONSTRAINT `PK_type` PRIMARY KEY (`id`)
    );
CREATE TABLE `person_type`
    (
        `id` INT NOT NULL,
        `person` VARCHAR(25) NOT NULL,
        CONSTRAINT `PK_person_type` PRIMARY KEY (`id`)
    );
CREATE TABLE `session`
    (
        `id` INT NOT NULL,
        `session_value` VARCHAR(25) NOT NULL,
        CONSTRAINT `PK_session` PRIMARY KEY (`id`)
    );
CREATE TABLE `status`
    (
        `id` INT NOT NULL,
        `status_value` VARCHAR(25) NOT NULL,
        CONSTRAINT `PK_status` PRIMARY KEY (`id`)
    );
CREATE TABLE `person`
    (
        `id` INT NOT NULL,
        `name` VARCHAR(100) NOT NULL,
        `address` VARCHAR(200) NOT NULL,
        `dob` DATE NOT NULL,
        `phone` VARCHAR(25) NOT NULL,
        `person_type` INT NOT NULL,
        `type` INT,
        CONSTRAINT `PK_person` PRIMARY KEY (`id`)
    );
CREATE TABLE `course`
    (
        `id` INT NOT NULL,
        `name` VARCHAR(100) NOT NULL,
        `type` INT NOT NULL,
        `cost` DECIMAL NOT NULL,
        `description` VARCHAR(600) NOT NULL,
        `date` DATE NOT NULL,
        CONSTRAINT `PK_course` PRIMARY KEY (`id`)
    );
CREATE TABLE `enrollment`
    (
        `student_id` INT NOT NULL,
        `course_id` INT NOT NULL,
        CONSTRAINT `PK_enrollment` PRIMARY KEY (`student_id`, `course_id`)
    );
CREATE TABLE `practical`
    (
        `student_id` INT NOT NULL,
        `course_id` INT NOT NULL,
        `exercise_1` INT NOT NULL,
        `exercise_2` INT NOT NULL,
        `exercise_3` INT NOT NULL,
        `exercise_4` INT NOT NULL,
        `exercise_5` INT NOT NULL,
        `total` INT NOT NULL,
        `isPassing` BOOLEAN NOT NULL,
        CONSTRAINT `PK_practical` PRIMARY KEY (`student_id`, `course_id`, `total`)
    );
CREATE TABLE `report`
    (
        `student_id` INT NOT NULL,
        `course_id` INT NOT NULL,
        `written` INT NOT NULL,
        `practical` INT NOT NULL,
        `test_date` DATE NOT NULL,
        `isPaymentComplete` BOOLEAN NOT NULL,
        CONSTRAINT `PK_report` PRIMARY KEY (`student_id`, `course_id`, `isPaymentComplete`)
    );
CREATE TABLE `bike`
    (
        `vin` INT NOT NULL,
        `license_plate` VARCHAR(25) NOT NULL,
        `repair_status` INT NOT NULL,
        `bike_type` INT NOT NULL,
        `cc` INT,
        `brand` VARCHAR(50),
        CONSTRAINT `PK_bike` PRIMARY KEY (`vin`)
    );
CREATE TABLE `assigned_bikes`
    (
        `vin` INT NOT NULL,
        `course_id` INT NOT NULL,
        CONSTRAINT `PK_assigned_bikes` PRIMARY KEY (`vin`, 'course_id')
    );
CREATE TABLE `repair`
    (
        `vin` INT NOT NULL,
        `problem_date` DATE NOT NULL,
        'repair_date' DATE,
        'repair_cost' INT,
        'description' VARCHAR(350),
        CONSTRAINT `PK_repair` PRIMARY KEY (`vin`, 'problem_date')
    );
CREATE TABLE `classroom`
    (
        `id` CHAR NOT NULL,
        `status` INT NOT NULL,
        `max_capacity` INT NOT NULL,
        CONSTRAINT `PK_classroom` PRIMARY KEY (`id`)
    );
CREATE TABLE `range`
    (
        `id` INT NOT NULL,
        `status` INT NOT NULL,
        `max_capacity` INT NOT NULL,
        `type` INT NOT NULL,
        CONSTRAINT `PK_range` PRIMARY KEY (`id`)
    );
CREATE TABLE `SCHEDULE`
    (
        `course_id` INT NOT NULL,
        `teacher_id` INT,
        `type` INT NOT NULL,
        `coach_id` INT NOT NULL,
        `date` DATE NOT NULL,
        `classroom` CHAR,
        `range` INT NOT NULL,
        `session` INT NOT NULL,
        CONSTRAINT `PK_schedule` PRIMARY KEY (`course_id`, `date`, `session`)
    );

ALTER TABLE `person` ADD CONSTRAINT `FK_person_person_type` FOREIGN KEY (`person_type`) 
REFERENCES `person_type`(`id`);

ALTER TABLE `person` ADD CONSTRAINT `FK_person_instructor_type` FOREIGN KEY (`type`) 
REFERENCES `type`(`id`);

ALTER TABLE `course` ADD CONSTRAINT `FK_course_type` FOREIGN KEY (`type`) 
REFERENCES `type`(`id`);

ALTER TABLE `enrollment` ADD CONSTRAINT `FK_enrollment_student` FOREIGN KEY (`student_id`) 
REFERENCES `person`(`id`);

ALTER TABLE `enrollment` ADD CONSTRAINT `FK_enrollment_course` FOREIGN KEY (`course_id`) 
REFERENCES `course`(`id`);

ALTER TABLE `practical` ADD CONSTRAINT `FK_practical_ids` FOREIGN KEY (`student_id`, `course_id`) 
REFERENCES `enrollment`(`student_id`, `course_id`);

ALTER TABLE `report` ADD CONSTRAINT `FK_report_ids` FOREIGN KEY (`student_id`, `course_id`) 
REFERENCES `enrollment`(`student_id`, `course_id`);

ALTER TABLE `report` ADD CONSTRAINT `FK_report_practical` FOREIGN KEY (`practical_score`) 
REFERENCES `practical`(`total`);

ALTER TABLE `bike` ADD CONSTRAINT `FK_bike_status` FOREIGN KEY (`repair_status`) 
REFERENCES `status`(`id`);

ALTER TABLE `assigned_bikes` ADD CONSTRAINT `FK_assigned_bikes_vin` FOREIGN KEY (`vin`) 
REFERENCES `bike`(`vin`);

ALTER TABLE `assigned_bikes` ADD CONSTRAINT `FK_assigned_bikes_course` FOREIGN KEY (`course_id`) 
REFERENCES `course`(`course_id`);

ALTER TABLE `repair` ADD CONSTRAINT `FK_repair_vin` FOREIGN KEY (`vin`) 
REFERENCES `bike`(`vin`);

ALTER TABLE `classroom` ADD CONSTRAINT `FK_classroom_status` FOREIGN KEY (`status`) 
REFERENCES `status`(`id`);

ALTER TABLE `range` ADD CONSTRAINT `FK_range_status` FOREIGN KEY (`status`) 
REFERENCES `status`(`id`);

ALTER TABLE `range` ADD CONSTRAINT `FK_range_type` FOREIGN KEY (`type`) 
REFERENCES `type`(`id`);

ALTER TABLE `schedule` ADD CONSTRAINT `FK_schedule_course` FOREIGN KEY (`course_id`) 
REFERENCES `course`(`course_id`);

ALTER TABLE `schedule` ADD CONSTRAINT `FK_schedule_teacher` FOREIGN KEY (`teacher_id`) 
REFERENCES `person`(`id`);

ALTER TABLE `schedule` ADD CONSTRAINT `FK_schedule_coach` FOREIGN KEY (`coach_id`) 
REFERENCES `person`(`id`);

ALTER TABLE `schedule` ADD CONSTRAINT `FK_schedule_type` FOREIGN KEY (`type`) 
REFERENCES `type`(`id`);

ALTER TABLE `schedule` ADD CONSTRAINT `FK_schedule_classroom` FOREIGN KEY (`classroom`) 
REFERENCES `classroom`(`id`);

ALTER TABLE `schedule` ADD CONSTRAINT `FK_schedule_range` FOREIGN KEY (`range`) 
REFERENCES `range`(`id`);






