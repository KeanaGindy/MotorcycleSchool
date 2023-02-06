/* SQL CREATE QUERIES FOR COURSE

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
