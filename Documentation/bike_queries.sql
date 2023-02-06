/* SQL QUERIES FOR BIKES */
/* =====================================================================*/
/*
CREATE NEW BIKE:
UX will present option to create a new bike and ask for bike parameter values.
 We will add the new bike with the data into the database with this query:

Parameter 1: vin VARCHAR(250)
Parameter 2: license_plate VARCHAR(250)
Parameter 3: repair_status BOOL
Parameter 4: brand VARCHAR(250)
Parameter 5: bike_type ENUM('dirt', 'street')
Parameter 6: cc INT

Example with hardcoded values: 
INSERT INTO bike VALUES('SDE123SDA', '1234-ABC', 0, 'Mistubishi', 'street', 250);
*/

INSERT INTO bike VALUES(?, ?, ?, ?, ?, ?);

/* =====================================================================*/
/*
VIEW BIKES BASED ON BIKE TYPE:
UX will present option to view bike.
We will display all bike with the data with this query:

Example with hardcoded values: 
SELECT * FROM bike
WHERE bike_type = 'dirt';

SELECT * FROM bike
WHERE bike_type = 'street';
*/

SELECT * FROM bike
WHERE bike_type = ?;

/*
VIEW BIKE AVAILABILITY:
UX will present option to view bikes available based on a certain.
We will display bike's vin and bike type with the query:

Example with hardcorded values:
SELECT vin, bike_type FROM bike
WHERE vin NOT IN (
SELECT vin FROM assigned_to 
WHERE course_id IN (
SELECT course_id FROM course WHERE = '2023-01-31'));

*/

SELECT vin, bike_type FROM bike
WHERE vin NOT IN (
SELECT vin FROM assigned_to 
WHERE course_id IN (
SELECT course_id FROM course WHERE = ?));

/*
VIEW BIKES IN REPAIR:
UX will present option to view a list of bikes in repair.
We will display bike's vin, bike type, problem date, and problem description with the query:

Example with hardcoded values:
SELECT bike.vin, bike.bike_type, repair_bike.problem_date, repair_bike.problem_description 
FROM repair_bike
INNER JOIN bike ON bike.vin = repair_bike.vin;

*/

/*
VIEW BIKE HISTORY:
UX will present option to view a bike's current history.
We will display the bike's vin, bike type, repair status, problem date, problem description, repair date, course id and course date attached to the bike with the query:

Example with hardcoded values:
SELECT bike.vin, bike.bike_type, bike.repair_status, repair_bike.problem_date, repair_bike.problem_description, repair_bike.repair_date, course.course_id, course.course_date 
FROM bike 
LEFT JOIN repair_bike ON bike.vin = repair_bike.vin 
LEFT JOIN assigned_to ON bike.vin = assigned_to.vin
LEFT JOIN course ON assigned_to.course_id = course.course_id
WHERE bike.vin = '1GHS8363429D49K2B';
LIMIT 0, 500;

SELECT bike.vin, bike.bike_type, bike.repair_status, repair_bike.problem_date, repair_bike.problem_description, repair_bike.repair_date, course.course_id, course.course_date 
FROM bike 
LEFT JOIN repair_bike ON bike.vin = repair_bike.vin 
LEFT JOIN assigned_to ON bike.vin = assigned_to.vin
LEFT JOIN course ON assigned_to.course_id = course.course_id
WHERE bike.vin = ?;
LIMIT 0, 500;

*/

SELECT 
/* =====================================================================*/
/*
EDIT BIKE LICENSE PLATE:
UX will present option to edit a bike's license plate and ask for bike's vin and new value.
We will update the desired field of the desired bike with this query so
it contains the new value:

Parameter 1: vin VARCHAR(250)
Parameter 2: license_plate VARCHAR(250)

Hardcoded example:
UPDATE bike
SET license_plate='ABC-1234'
WHERE vin='NYH2334DD7234';
*/

UPDATE bike
SET license_plate = ?
WHERE vin = ?;

/* =====================================================================*/
/*
EDIT BIKE REPAIR STATUS:
UX will present option to edit a bike's repair status and ask for bike's vin and new value.
We will update the desired field of the desired bike with this query so
it contains the new value:

Parameter 1: vin VARCHAR(250)
Parameter 2: repair_status BOOL

Hardcoded example:
UPDATE bike
SET repair_status = FALSE
WHERE vin='NYH2334DD7234';
*/

UPDATE bike
SET repair_status = ?
WHERE vin = ?;

/* =====================================================================*/
/*
REMOVE BIKE:
UX will present option to delete bike and ask for the vin of the bike the user wishes to delete from the system.
 We will delete the desired vin and all of their associated data with this query:


Parameter 1: vin VARCHAR(250)

Example with hardcoded values: 
DELETE FROM bike
WHERE vin =  'NYH2334DD7234';

*/

DELETE FROM bike
WHERE vin = ?;