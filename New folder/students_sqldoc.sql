/* SQL QUERIES FOR STUDENT */
/* =====================================================================*/
/*
CREATE NEW STUDENT:
UX will present option to create a new student and ask for student parameter values.
 We will add the new student with the data into the database with this query:

Parameter 1: id: INT
Parameter 2: student_name: String
Parameter 3: student_dob: DATE
Parameter 4: student_address: String
Parameter 5: phone: VARCHAR(15) or String

Example with hardcoded values: 
INSERT INTO student VALUES (83738938, 'Gio', '1995-01-01', '555 Street State ZIP', '5555555555');

*/
INSERT INTO student VALUES (id, student_name, dob, student_address, phone);

/* =====================================================================*/
/*
VIEW ALL STUDENTS:
UX will present option to view students.
We will display all students with the data with this query:

*/
SELECT *
FROM student;

/* =====================================================================*/
/*
VIEW STUDENT REPORT:
UX will present option to view a specific student's report and ask for the id of the desired student.
 We will display all of the student's data for the report with this query:

Parameter 1: id: INT

Example with hardcoded values:
SELECT *
FROM enrolled_in 
WHERE student_id = 7
ORDER BY course_id;
*/
SELECT *
FROM enrolled_in 
WHERE student_id = id
ORDER BY course_id;

/* =====================================================================*/
/*
EDIT STUDENT ID -- DO NOT NEED? IF WRONG ID, JUST DELETE 
UX will present option to edit a student's data and ask for student id and new value.
We will update the desired field of the desired student with this query so 
it contains the new value:

Parameter 1: student_id: INT
Parameter 2: new_value: INT

Example with hardcoded values:
UPDATE student
SET student_id = 8
WHERE student_id = 7;
*/
/*
UPDATE student
SET student_id = new_value
WHERE student_id = student_id;
*/
/* =====================================================================*/

/*
EDIT STUDENT NAME:
UX will present option to edit a student's data and ask for student id and new value.
We will update the desired field of the desired student with this query so 
it contains the new value:

Parameter 1: student_id: INT
Parameter 2: new_value: STRING

harded example:
UPDATE student
SET student_name = 'Sam Taco'
WHERE student_id = 7;

*/
UPDATE student
SET student_name = new_value
WHERE student_id = student_id;

/* =====================================================================*/
/*
EDIT STUDENT DOB - DO NOT NEED:
UX will present option to edit a student's data and ask for student id and new value.
We will update the desired field of the desired student with this query so 
it contains the new value:

Parameter 1: student_id: INT
Parameter 2: new_value: DATE

UPDATE student
SET dob = new_value
WHERE student_id = student_id;
*/
/* =====================================================================*/

/*
EDIT STUDENT ADDRESS:
UX will present option to edit a student's data and ask for student id and new value.
We will update the desired field of the desired student with this query so 
it contains the new value:

Parameter 1: student_id: INT
Parameter 2: new_value: STRING

Hardcoded exmaple:
UPDATE student
SET address = '444 Pizza Drive State Zip'
WHERE student_id = 7;

*/
UPDATE student
SET address = new_value
WHERE student_id = student_id;
/* =====================================================================*/

/*
EDIT STUDENT phone:
UX will present option to edit a student's data and ask for student id and new value.
We will update the desired field of the desired student with this query so 
it contains the new value:

Parameter 1: student_id: INT
Parameter 2: new_value: STRING

Hardcoded example:
UPDATE student
SET phone = 5555555566
WHERE student_id = 7;

*/
UPDATE student
SET phone = new_value
WHERE student_id = student_id;
/* =====================================================================*/

/*
REMOVE STUDENT:
UX will present option to create a new student and ask for student parameter values.
 We will add the new student with the data into the database with this query:

Parameter 1: student_id: INT

Example with hardcoded values: 
DELETE FROM student
WHERE student_id =  1;

*/
DELETE FROM student
WHERE student_id = student_id;