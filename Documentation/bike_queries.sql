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
WHERE bike_type = ‘street’;

*/

SELECT * FROM bike
WHERE bike_type = ?;

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