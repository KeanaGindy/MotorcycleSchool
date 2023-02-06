/* SQL EDIT QUERIES FOR COURSE */
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