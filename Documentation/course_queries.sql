/* SQL QUERIES FOR COURSE */
/* =====================================================================*/
/* 
CREATE QUERIES FOR COURSE

Hardcoded Queries Add Two Courses:
Course 4 (Dirt Bike on 3/30/2023) Range 4 AM/PM
Course 5 (Street Bike on 4/22/2023) Classroom B in AM and Range 2 in PM

*/
/* =====================================================================*/
/*
CREATE NEW COURSE:
UX will present option to create a new course and ask for course parameter values.
 We will add the new course with the data into the database with this query:

Parameter 1: course_id: INT
Parameter 2: course_name: VARCHAR(250)
Parameter 3: course_description: VARCHAR(500)
Parameter 4: course_date DATE
Parameter 5: cost INT
Parameter 6: course_type ENUM('dirt', 'street') 


Example with hardcoded values: 
INSERT INTO course VALUES (4, 'advanced dirt bike skills', 'advanced dirt class for experts', '2023-03-30', 250, 'dirt');
INSERT INTO course VALUES (5, 'street bike care 101', 'learn how to take care of your street bike', '2023-04-22', 50, 'street');
*/

INSERT INTO course VALUES (?, ?, ?, ?, ?, ?);

/* =====================================================================*/
/*
ADD INSTRUCTOR TO COURSE
UX will present option to assign an instructor to a course and will ask for instructor id, course id, 
session(s) that the instructor will teach, and their role.
 We will add the instructor to the course with this query:

Parameter 1: instructor_id: INT
Parameter 2: course_id: INT
Parameter 3: in_session: ENUM('AM', 'PM', 'BOTH')
Parameter 4: instructor_role: ENUM('dirt_coach', 'street_coach', 'street_teacher', 'street_coach_teacher')


Example with hardcoded values: 
INSERT INTO instructs VALUES (2, 4, 'BOTH', 'dirt_coach');
*/

INSERT INTO instructs VALUES (?, ?, ?, ?);

/* =====================================================================*/
/*
ADD STUDENT TO COURSE
UX will present option to enroll a student in a course and will ask for both student and course id.
We will also need to ask whether payment is complete, and scores of the exam.
NOTE: Since scores cannot be null, they will be entered as -1 if exams not yet taken.

We will enroll a student into a course with this query:

Parameter 1: course_id: INT
Parameter 2: student_id: INT 
Parameter 3: is_payment_completed: BOOL
Parameter 4: written_score: INT 
Parameter 5: exercise_1_score: INT 
Parameter 6: exercise_2_score: INT 
Parameter 7: exercise_3_score: INT 
Parameter 8: exercise_4_score: INT 
Parameter 9: exercise_5_score: INT 


Example with hardcoded values: 
INSERT INTO enrolled_in VALUES (4, 1, FALSE, -1, -1, -1, -1, -1, -1);
*/

INSERT INTO enrolled_in VALUES (?,?,?,?,?,?,?,?,?);

/* =====================================================================*/
/*
ASSIGN RANGE LOCATION FOR DIRT BIKE COURSE
UX will present option to assign a range for a dirt bike course and automatically
assign it to the only dirt bike range that exists (range 4) in both sessions since 
dirt bikes are all day courses.

Parameter 1: course_id: INT

Example with hardcoded values: 
INSERT INTO uses VALUES (4, 4, 'BOTH');
*/

INSERT INTO uses VALUES (?,4,'BOTH');

/* =====================================================================*/
/*
ASSIGN RANGE LOCATION FOR STREET BIKE COURSE
UX will present option to assign a street bike course to a specific range
for a specific session.

Parameter 1: course_id: INT
Parameter 2: range_id: INT
Parameter 3: in_session: ENUM('AM', 'PM', 'BOTH')

Example with hardcoded values: 
INSERT INTO uses VALUES (5,2,'PM');
*/

INSERT INTO uses VALUES (?,?,?);

/* =====================================================================*/
/*
ASSIGN CLASSROOM LOCATION FOR STREET BIKE COURSE
UX will present option to assign a street bike course to a specific classroom for a 
specific session.

Parameter 1: course_id: INT
Parameter 2: classroom_id: CHAR
Parameter 3: in_session: ENUM('AM', 'PM', 'BOTH')

Example with hardcoded values: 
INSERT INTO taught_in VALUES (5,B,'AM');
*/

INSERT INTO taught_in VALUES (?,?,?);

/* =====================================================================*/

/*
VIEW ALL COURSE:
UX will present option to view course.
We will display all courses with the data with this query:

SELECT c.course_id, i.instructor_id, s.student_id, t.classroom_id, u.range_id, t.in_session AS classroom_session, u.in_session AS range_session, c.course_date
FROM course c 
LEFT JOIN instructs i ON c.course_id = i.course_id
LEFT JOIN enrolled_in s ON s.course_id = c.course_id 
LEFT JOIN taught_in t ON t.course_id = s.course_id 
LEFT JOIN uses u ON c.course_id = u.course_id;

*/

SELECT c.course_id, i.instructor_id, s.student_id, t.classroom_id, u.range_id, t.in_session AS classroom_session, u.in_session AS range_session, c.course_date
FROM course c 
LEFT JOIN instructs i ON c.course_id = i.course_id
LEFT JOIN enrolled_in s ON s.course_id = c.course_id 
LEFT JOIN taught_in t ON t.course_id = s.course_id 
LEFT JOIN uses u ON c.course_id = u.course_id;

/* EDIT QUERIES FOR COURSE */
/* =====================================================================*/
/*
EDIT COURSE DATE:
UX will present option to update an existing course date by prompting user for course_id and the new date.
We will update the course date in the database with this query:

Parameter 1: course_id: INT
Parameter 2: course_date: DATE

Example with hardcoded values: 
UPDATE course
SET course_date = '1980-05-22'
WHERE course_id = 5;
*/

UPDATE course
SET course_date = ?
WHERE course_id = ?;

/* =====================================================================*/
/*
EDIT STUDENT PAYMENT RECORD FOR A COURSE :
UX will present option to edit a student's data and ask for student id and new value.
We will update the desired field of the desired student with this query so 
it contains the new value:

Parameter 1: course_id: INT
Parameter 2: student_id: INT
Parameter 3: is_payment_completed: BOOL

Hardcoded example:
UPDATE enrolled_in
SET is_payment_completed = TRUE
WHERE student_id = 1 AND course_id = 4;
*/

UPDATE enrolled_in
SET is_payment_completed = ?
WHERE student_id = ? AND course_id = ?;

/* =====================================================================*/
/*
EDIT STUDENT WRITTEN SCORE FOR A COURSE :
UX will present option to edit a student's written score for a particular course and 
and will ask for course id, student id and new score value.
We will update the score for this student's course with this query so it contains the new value:

Parameter 1: course_id: INT
Parameter 2: student_id: INT
Parameter 3: written_score: INT

Hardcoded example:
UPDATE enrolled_in
SET written_score = 80
WHERE student_id = 1 AND course_id = 4;
*/

UPDATE enrolled_in
SET written_score = ?
WHERE student_id = ? AND course_id = ?;

/* =====================================================================*/
/*
EDIT STUDENT EXERCISE 1 SCORE FOR A COURSE :
UX will present option to edit a student's exercise 1 score for a particular course and 
and will ask for course id, student id and new score value.
We will update the score for this student's course with this query so it contains the new value:

Parameter 1: course_id: INT
Parameter 2: student_id: INT
Parameter 3: exercise_1_score: INT

Hardcoded example:
UPDATE enrolled_in
SET exercise_1_score = 30
WHERE student_id = 1 AND course_id = 4;
*/

UPDATE enrolled_in
SET exercise_1_score = ?
WHERE student_id = ? AND course_id = ?;

/* =====================================================================*/
/*
EDIT STUDENT EXERCISE 2 SCORE FOR A COURSE :
UX will present option to edit a student's exercise 2 score for a particular course and 
and will ask for course id, student id and new score value.
We will update the score for this student's course with this query so it contains the new value:

Parameter 1: course_id: INT
Parameter 2: student_id: INT
Parameter 3: exercise_2_score: INT

Hardcoded example:
UPDATE enrolled_in
SET exercise_2_score = 100
WHERE student_id = 1 AND course_id = 4;
*/

UPDATE enrolled_in
SET exercise_2_score = ?
WHERE student_id = ? AND course_id = ?;

/* =====================================================================*/
/*
EDIT STUDENT EXERCISE 3 SCORE FOR A COURSE :
UX will present option to edit a student's exercise 3 score for a particular course and 
and will ask for course id, student id and new score value.
We will update the score for this student's course with this query so it contains the new value:

Parameter 1: course_id: INT
Parameter 2: student_id: INT
Parameter 3: exercise_3_score: INT

Hardcoded example:
UPDATE enrolled_in
SET exercise_3_score = 68
WHERE student_id = 1 AND course_id = 4;
*/

UPDATE enrolled_in
SET exercise_3_score = ?
WHERE student_id = ? AND course_id = ?;

/* =====================================================================*/
/*
EDIT STUDENT EXERCISE 4 SCORE FOR A COURSE :
UX will present option to edit a student's exercise 4 score for a particular course and 
and will ask for course id, student id and new score value.
We will update the score for this student's course with this query so it contains the new value:

Parameter 1: course_id: INT
Parameter 2: student_id: INT
Parameter 3: exercise_4_score: INT

Hardcoded example:
UPDATE enrolled_in
SET exercise_4_score = 82
WHERE student_id = 1 AND course_id = 4;
*/

UPDATE enrolled_in
SET exercise_4_score = ?
WHERE student_id = ? AND course_id = ?;

/* =====================================================================*/
/*
EDIT STUDENT EXERCISE 5 SCORE FOR A COURSE :
UX will present option to edit a student's exercise 5 score for a particular course and 
and will ask for course id, student id and new score value.
We will update the score for this student's course with this query so it contains the new value:

Parameter 1: course_id: INT
Parameter 2: student_id: INT
Parameter 3: exercise_5_score: INT

Hardcoded example:
UPDATE enrolled_in
SET exercise_5_score = 91
WHERE student_id = 1 AND course_id = 4;
*/

UPDATE enrolled_in
SET exercise_5_score = ?
WHERE student_id = ? AND course_id = ?;

/* =====================================================================*/
/*
REMOVE COURSE:
UX will present option to delete a course and ask for course id of the course the user wishes to delete from the system.
 We will delete the desired course and all of their associated data with this query:


Parameter 1: course_id: INT

Example with hardcoded values: 
DELETE FROM course
WHERE course_id = 2;

*/

DELETE FROM course
WHERE course_id = ?;