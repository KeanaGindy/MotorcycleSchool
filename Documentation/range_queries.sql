/* SQL QUERIES FOR RANGE */
/* =====================================================================*/
/*
CREATE NEW RANGE:
UX will present option to create a new range and ask for range parameter values.
 We will add the new range with the data into the database with this query:

Parameter 1: range_id: INT
Parameter 2: range_type: ENUM('dirt', 'street'),
Parameter 3: max_capacity INT


Example with hardcoded values: 
INSERT INTO range_location VALUES (5, 'dirt', 20),(6,'street', 25 );
*/

INSERT INTO range_location VALUES (?, ?, ?);

/* =====================================================================*/
/*
VIEW ALL RANGES:
UX will present option to view ranges.
We will display all ranges with the data with this query:

*/

SELECT *
FROM range_location;

/* =====================================================================*/
/*
VIEW NUMBER OF SEATS LEFT FOR RANGES:
UX will present option to view a report of all ranges on a given day and the remaining seats available.
UX will ask for the date of course.
We will display all ranges for that day and number of open seats reamining with this query:

Parameter 1: course_date:DATE

Example with hardcoded values:
SELECT range_location.range_id, range_location.range_type, uses.in_session,
  (range_location.max_capacity -
    (SELECT COUNT(*)
     FROM enrolled_in
     JOIN course ON enrolled_in.course_id = course.course_id
     JOIN uses ON course.course_id = uses.course_id
     WHERE NOT IN uses.range_id = range_location.range_id AND course.course_date = '2023-1-30')) AS availability
FROM range_location
JOIN uses ON range_location.range_id = uses.range_id;
*/

SELECT range_location.range_id, range_location.range_type, uses.in_session,
  (range_location.max_capacity -
    (SELECT COUNT(*)
     FROM enrolled_in
     JOIN course ON enrolled_in.course_id = course.course_id
     JOIN uses ON course.course_id = uses.course_id
     WHERE uses.range_id = range_location.range_id AND course.course_date = '????-?-??')) AS availability
FROM range_location
JOIN uses ON range_location.range_id = uses.range_id;


/* =====================================================================*/
/*
VIEW RANGE REPORT - AVAILABLE RANGES ON A GIVEN DAY:
UX will present option to view a range's report of all ranges available on a given day.
We will display all available ranges for that day for the report with this query:

Parameter 1: course_date:DATE

Example with hardcoded values:
SELECT range_location.range_id,
CASE
WHEN sub_query.session_count = 1 THEN
(SELECT CASE in_session
WHEN 'AM' THEN 'PM'
WHEN 'PM' THEN 'AM'
ELSE 'NONE'
END
FROM uses
WHERE uses.range_id = range_location.range_id
AND uses.course_id IN (
SELECT course.course_id
FROM course
WHERE course.course_date = '2023-01-30'
)
LIMIT 1
)
WHEN sub_query.session_count = 2 THEN 'NONE'
ELSE 'AM & PM'
END as session_available
FROM range_location
LEFT JOIN (
SELECT uses.range_id,
COUNT(uses.in_session) as session_count
FROM uses
JOIN course
ON uses.course_id = course.course_id
AND course.course_date = '2023-01-30'
GROUP BY uses.range_id
) sub_query
ON range_location.range_id = sub_query.range_id
LIMIT 0, 500

*/

SELECT range_location.range_id,
CASE
WHEN sub_query.session_count = 1 THEN
(SELECT CASE in_session
WHEN 'AM' THEN 'PM'
WHEN 'PM' THEN 'AM'
ELSE 'NONE'
END
FROM uses
WHERE uses.range_id = range_location.range_id
AND uses.course_id IN (
SELECT course.course_id
FROM course
WHERE course.course_date = '????-??-??'
)
LIMIT 1
)
WHEN sub_query.session_count = 2 THEN 'NONE'
ELSE 'AM & PM'
END as session_available
FROM range_location
LEFT JOIN (
SELECT uses.range_id,
COUNT(uses.in_session) as session_count
FROM uses
JOIN course
ON uses.course_id = course.course_id
AND course.course_date = '????-??-??'
GROUP BY uses.range_id
) sub_query
ON range_location.range_id = sub_query.range_id
LIMIT 0, 500


/* =====================================================================*/

/*
EDIT RANGE MAX CAPACITY:
UX will present option to edit a range's data and ask for range id and new value.
We will update the desired field of the desired student with this query so 
it contains the new value:

Parameter 1: range_id: INT
Parameter 2: max_capacity INT

harded example:
UPDATE range_location
SET max_capacity = 16
WHERE range_id = 6;

*/

UPDATE range_location
SET mac_capacity = ?
WHERE range_id = ?;

/* =====================================================================*/

/*
EDIT RANGE TYPE:
UX will present option to edit a range's data and ask for range id and new value.
We will update the desired field of the desired range with this query so 
it contains the new value:

Parameter 1: range_id: INT
Parameter 2: range_type ENUM('dirt', 'street')

Hardcoded exmaple:
UPDATE range_location
SET range_type = 'street'
WHERE range_id = 5;

*/

UPDATE range_location
SET range_type = ?
WHERE range_id = ?;

/* =====================================================================*/

/*
REMOVE range:
UX will present option to delete a range and ask for range id of the range the user wishes to delete from the system.
 We will delete the desired range and all of their associated data with this query:


Parameter 1: range_id: INT

Example with hardcoded values: 
DELETE FROM range_location
WHERE range_id =  6;

*/

DELETE FROM range_location
WHERE range_id = ?;