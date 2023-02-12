CREATE DATABASE motorcycleschool;
USE motorcycleschool;


CREATE TABLE instructor
(
instructor_id INT NOT NULL,
instructor_name VARCHAR(250) NOT NULL,
dob DATE,
address VARCHAR(250),
phone VARCHAR(15),
coach_dirt_bike BOOL NOT NULL,
coach_street_bike BOOL NOT NULL,
teach_street_bike BOOL NOT NULL,
CONSTRAINT PK_instructor PRIMARY KEY (instructor_id)
);


CREATE TABLE course
(
course_id INT NOT NULL,
course_name VARCHAR(250) NOT NULL,
course_description VARCHAR(500) NOT NULL,
course_date DATE NOT NULL,
cost INT NOT NULL,
course_type ENUM('dirt', 'street') NOT NULL,
CONSTRAINT PK_course PRIMARY KEY (course_id)
);


CREATE TABLE instructs
(
instructor_id INT NOT NULL,
course_id INT NOT NULL,
in_session ENUM('AM', 'PM', 'BOTH') NOT NULL,
instructor_role ENUM('dirt_coach', 'street_coach', 'street_teacher', 'street_coach_teacher') NOT NULL
);


CREATE TABLE student
(
student_id INT NOT NULL,
student_name VARCHAR(250) NOT NULL,
dob DATE,
address VARCHAR(250),
phone VARCHAR(15),
CONSTRAINT PK_student PRIMARY KEY (student_id)
);


CREATE TABLE enrolled_in
(
course_id INT NOT NULL,
student_id INT NOT NULL,
is_payment_completed BOOL NOT NULL,
written_score INT NOT NULL,
exercise_1_score INT NOT NULL,
exercise_2_score INT NOT NULL,
exercise_3_score INT NOT NULL,
exercise_4_score INT NOT NULL,
exercise_5_score INT NOT NULL
);


CREATE TABLE range_location
(
range_id INT NOT NULL,
range_type ENUM('dirt', 'street'),
max_capacity INT NOT NULL,
CONSTRAINT PK_range PRIMARY KEY (range_id)
);

CREATE TABLE uses
(
course_id INT NOT NULL,
range_id INT NOT NULL,
in_session ENUM('AM', 'PM', 'BOTH')
);


CREATE TABLE bike
(
vin VARCHAR(250) NOT NULL,
license_plate VARCHAR(250) NOT NULL,
repair_status BOOL NOT NULL,
brand VARCHAR(250) NOT NULL,
bike_type ENUM('dirt', 'street'),
cc INT NOT NULL,
CONSTRAINT PK_bike PRIMARY KEY (vin)
);


CREATE TABLE assigned_to
(
course_id INT NOT NULL,
vin VARCHAR(250) NOT NULL
);


CREATE TABLE repair_bike
(
vin VARCHAR(250) NOT NULL,
problem_date DATE,
repair_cost INT,
repair_date DATE,
problem_description VARCHAR(500)
);


CREATE TABLE classroom_location
(
classroom_id CHAR NOT NULL,
max_capacity INT NOT NULL,
CONSTRAINT PK_classroom_location PRIMARY KEY (classroom_id)
);


CREATE TABLE taught_in
(
course_id INT NOT NULL,
classroom_id CHAR NOT NULL,
in_session ENUM('AM', 'PM', 'BOTH')
);

ALTER TABLE instructs ADD FOREIGN KEY (instructor_id) REFERENCES instructor(instructor_id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE instructs ADD FOREIGN KEY (course_id) REFERENCES course(course_id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE enrolled_in ADD FOREIGN KEY (course_id) REFERENCES course(course_id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE enrolled_in ADD FOREIGN KEY (student_id) REFERENCES student(student_id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE uses ADD FOREIGN KEY (course_id) REFERENCES course(course_id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE uses ADD FOREIGN KEY (range_id) REFERENCES range_location(range_id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE assigned_to ADD FOREIGN KEY (course_id) REFERENCES course(course_id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE assigned_to ADD FOREIGN KEY (vin) REFERENCES bike(vin) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE repair_bike ADD FOREIGN KEY (vin) REFERENCES bike(vin) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE taught_in ADD FOREIGN KEY (course_id) REFERENCES course(course_id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE taught_in ADD FOREIGN KEY (classroom_id) REFERENCES classroom_location(classroom_id) ON UPDATE CASCADE ON DELETE CASCADE;