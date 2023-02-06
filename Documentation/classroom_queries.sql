/* SQL QUERIES FOR CLASSROOM */
/* =====================================================================*/
/*
CREATE NEW CLASSROOM:
UX will present option to create a new classroom and ask for classroom parameter values.
 We will add the new classroom with the data into the database with this query:

Parameter 1: classroom_id: CHAR
Parameter 2: max_capacity: INT



Example with hardcoded values: 
INSERT INTO classroom_location VALUES ('C', 25),('D', 40);
*/

INSERT INTO classroom_location VALUES (?, ?);

/* =====================================================================*/
/*
VIEW ALL CLASSROOMS:
UX will present option to view classrooms.
We will display all classroom with the data with this query:

*/

SELECT *
FROM class_location;

/* =====================================================================*/
/*
VIEW CLASSROOM Avaibility:
UX will present option to view classroom availabilities and ask for the date of course.
We will display what sessions are available for a classroom with this query:

Parameter 1: course_date:DATE

Example with hardcoded values:
SELECT classroom_location.classroom_id,
CASE
WHEN sub_query.session_count = 1 THEN
(SELECT CASE in_session
WHEN 'AM' THEN 'PM'
WHEN 'PM' THEN 'AM'
ELSE 'NONE'
END
FROM taught_in
WHERE taught_in.classroom_id = classroom_location.classroom_id
AND taught_in.course_id IN (
SELECT course.course_id
FROM course
WHERE course.course_date = '2023-01-31'
)
LIMIT 1
)
WHEN sub_query.session_count = 2 THEN 'NONE'
ELSE 'AM & PM'
END as session_available
FROM classroom_location
LEFT JOIN (
SELECT taught_in.classroom_id,
COUNT(taught_in.in_session) as session_count
FROM taught_in
JOIN course
ON taught_in.course_id = course.course_id
AND course.course_date = '2023-01-31'
GROUP BY taught_in.classroom_id
) sub_query
ON classroom_location.classroom_id = sub_query.classroom_id
LIMIT 0, 500

*/

SELECT classroom_location.classroom_id,
CASE
WHEN sub_query.session_count = 1 THEN
(SELECT CASE in_session
WHEN 'AM' THEN 'PM'
WHEN 'PM' THEN 'AM'
ELSE 'NONE'
END
FROM taught_in
WHERE taught_in.classroom_id = classroom_location.classroom_id
AND taught_in.course_id IN (
SELECT course.course_id
FROM course
WHERE course.course_date = '????-??-??'
)
LIMIT 1
)
WHEN sub_query.session_count = 2 THEN 'NONE'
ELSE 'AM & PM'
END as session_available
FROM classroom_location
LEFT JOIN (
SELECT taught_in.classroom_id,
COUNT(taught_in.in_session) as session_count
FROM taught_in
JOIN course
ON taught_in.course_id = course.course_id
AND course.course_date = '????-??-??'
GROUP BY taught_in.classroom_id
) sub_query
ON classroom_location.classroom_id = sub_query.classroom_id
LIMIT 0, 500


/* =====================================================================*/
/*
EDIT CLASSROOM MAX CAPACITY:
UX will present option to edit a classroom's data and ask for classroom id and new value.
We will update the desired field of the desired classroom with this query so 
it contains the new value:

Parameter 1: max_capacity: INT

Hardcoded exmaple:
UPDATE classroom_location
SET max_capacity = 35
WHERE classroom_id = 'A';

*/

UPDATE classroom_location
SET max_capacity = ?
WHERE classroom_id = ?;

/* =====================================================================*/

/*
REMOVE CLASSROOM:
UX will present option to delete a classroom and ask for classroom id of the classroom the user wishes to delete from the system.
 We will delete the desired classroom and all of their associated data with this query:


Parameter 1: classroom_id: INT

Example with hardcoded values: 
DELETE FROM classroom_location
WHERE classroom_id =  'A';

*/

DELETE FROM classroom_location
WHERE classroom_id = ?;