/* SQL QUERIES FOR INSTRUCTOR */
/* =====================================================================*/
/*
CREATE NEW INSTRUCTOR:
UX will present option to create a new instuctor and ask for instuctor parameter values.
 We will add the new instuctor with the data into the database with this query:

Parameter 1: instructor_id INT
Parameter 2: instructor_name VARCHAR(250)
Parameter 3: dob DATE
Parameter 4: address VARCHAR(250)
Parameter 5: phone VARCHAR(15)
Parameter 6: coach_dirt_bike BOOL
Parameter 7: coach_street_bike BOOL
Parameter 8: teach_street_bi

Example with hardcoded values: 
INSERT INTO instructor VALUE (7, 'Sansa Stark', '1988-03-22', '523 CandyCane Lane',  9031234567, true, false, true);

*/

INSERT INTO instructor VALUES (?, ?, ?, ?, ?, ?, ?, ?);

/* =====================================================================*/
/*
VIEW ALL INSTRUCTOR:
UX will present option to view instructors.
We will display all instructors with the data with this query:

*/

SELECT *
FROM instructor;

/* =====================================================================*/
/*
VIEW AVAILABLE INSTRUCTORS:
UX will present option to view instructors available based on a certain day.
We will display instructor's id, name, and qualifications data with this query:

Example with hardcoded values: 
SELECT DISTINCT course_id, course_date, in_session, I.instructor_id, I.instructor_name, coach_dirt_bike, coach_street_bike, teach_street_bike
FROM instructor as I, course, instructs
WHERE I.instructor_id IN (
SELECT I.instructor_id
FROM instructs
WHERE course_id IN (
SELECT course_id
FROM course
WHERE course_date = '2023-01-31'
));

*/

SELECT course_date, in_session, instructor_id, instructor_name, coach_dirt_bike, coach_street_bike, teach_street_bike
FROM instructor, course, instructs
WHERE instructor_id IN (
SELECT instructor_id
FROM instructs
WHERE course_id IN (
SELECT course_id
FROM course
WHERE course_date = ?
));

/* =====================================================================*/
/*
VIEW INSTRUCTOR SCHEDULE:
UX will present option to view instructors.
We will display all instructors with the data with this query:

SELECT 
  i.instructor_id AS instructor_id, 
  i.instructor_name AS instructor_name, 
  c.course_id AS course_id, 
  c.course_name AS course_name, 
  c.course_type, 
  c.course_date, 
  s.in_session
FROM instructor i
JOIN instructs s ON i.instructor_id = s.instructor_id
JOIN course c ON s.course_id = c.course_id
WHERE i.instructor_id = 1;

*/

SELECT 
  i.instructor_id AS instructor_id, 
  i.instructor_name AS instructor_name, 
  c.course_id AS course_id, 
  c.course_name AS course_name, 
  c.course_type, 
  c.course_date, 
  s.in_session
FROM instructor i
JOIN instructs s ON i.instructor_id = s.instructor_id
JOIN course c ON s.course_id = c.course_id
WHERE i.instructor_id = ?;

/* =====================================================================*/
/*
EDIT INSTRUCTOR NAME:
UX will present option to edit a range's data and ask for range id and new value.
We will update the desired field of the desired student with this query so
it contains the new value:

Parameter 1 instructor_id: INT
Parameter 2 instructor_name VARCHAR(250)

Hardcoded example:
UPDATE instructor
SET instructor_name = 'Sansa Stark'
WHERE instructor_id = 1;
*/

UPDATE instructor
SET instructor_name = ?
WHERE instructor_id = ?;

/* =====================================================================*/
/*
EDIT INSTRUCTOR ADDRESS:
UX will present option to edit a instructor's data and ask for instructor id and new value.
We will update the desired field of the desired instructor with this query so 
it contains the new value:

Parameter 1: instructor_id INT
Parameter 2: address VARCHAR(250)

Hardcoded exmaple:
UPDATE instructor
SET address = '123 Hamburger Drive State Zip'
WHERE instructor_id = 1;

*/

UPDATE instructor
SET address = ?
WHERE instructor_id = ?;

/* =====================================================================*/
/*
EDIT INSTRUCTOR phone:
UX will present option to edit an instructor's data and ask for instructor id and new value.
We will update the desired field of the desired instructor with this query so 
it contains the new value:

Parameter 1: instructor_id INT
Parameter 2: phone VARCHAR(15)

Hardcoded example:
UPDATE instructor
SET phone = 5555555566
WHERE instructor_id = 1;

*/

UPDATE instructor
SET phone = ?
WHERE instructor_id = ?;

/* =====================================================================*/
/*
EDIT INSTRUCTOR coach_dirt_bike:
UX will present option to edit an instructor's data and ask for instructor id and new value.
We will update the desired field of the desired instructor with this query so 
it contains the new value:

Parameter 1: instructor_id INT
Parameter 2: coach_dirt_bike BOOL

Hardcoded example:
UPDATE instructor
SET coach_dirt_bike = FALSE
WHERE instructor_id = 1;

*/

UPDATE instructor
SET coach_dirt_bike = ?
WHERE instructor_id = ?;

/* =====================================================================*/
/*
EDIT INSTRUCTOR coach_street_bike:
UX will present option to edit an instructor's data and ask for instructor id and new value.
We will update the desired field of the desired instructor with this query so 
it contains the new value:

Parameter 1: instructor_id INT
Parameter 2: coach_street_bike BOOL

Hardcoded example:
UPDATE instructor
SET coach_street_bike = TRUE
WHERE instructor_id = 1;

*/

UPDATE instructor
SET coach_street_bike = ?
WHERE instructor_id = ?;

/* =====================================================================*/
/*
EDIT INSTRUCTOR teach_street_bike:
UX will present option to edit an instructor's data and ask for instructor id and new value.
We will update the desired field of the desired instructor with this query so 
it contains the new value:

Parameter 1: instructor_id INT
Parameter 2: teach_street_bike BOOL

Hardcoded example:
UPDATE instructor
SET teach_street_bike = FALSE
WHERE instructor_id = 1;

*/

UPDATE instructor
SET teach_street_bike = ?
WHERE instructor_id = ?;

/* =====================================================================*/
/*
REMOVE INSTRUCTOR:
UX will present option to delete an instructor and ask for instructor id of the instructor the user wishes to delete from the system.
 We will delete the desired instructor and all of their associated data with this query:

Parameter 1: instructor_id: INT

Example with hardcoded values: 
DELETE FROM instructor
WHERE instructor_id =  1;

*/

DELETE FROM instructor
WHERE instructor_id = ?;