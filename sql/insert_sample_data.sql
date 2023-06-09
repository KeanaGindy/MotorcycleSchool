INSERT INTO student VALUES (1, 'Jon Snow', '1980-12-20', '23 Winter Lane', 9493225911),(2,'Daenerys Targaryen', '2001-01-02', '123 Blossom Street', 1234567890),(3,'Sansa Stark','1888-02-03', '123 Candy Street',1234561234),(4,'Tyrion Lannister','1823-03-01', '420 Fun Street',2341235432),(5,'Rob Stark','1999-05-03', '691 Forever street',9879998888),(6,'Joffrey Baratheon','2011-03-03', '1 Blossom Avenue',12341231234),(7,'Samwell Tarley','2001-03-03', '20 Rose Street',6789678123);

INSERT INTO instructor VALUE (1, 'Cersei Lannister', '1984-03-22', '523 CandyCane Lane',  9034234553, false, true, false),(2, 'Arya Stark', '1989-05-23', '100 SnowBerry Drive', 2382113532, true, true, false),(3, 'Theon Greyjoy', '1987-02-14', '301 Waterwork Way', 9021113242, true, true, false),(4, 'Khal Grogo', '1986-10-30', '900 Gingerstone Drive', 9092334234, true, true, true),(5, 'Jorah Mormont', '1980-01-30', '1030 JackRipper Street', 8929132333, true, false, true), (6, 'Jamie Lannister', '1982-03-07', '8022 Crazyday Street', 2132223451, true, true, true);

INSERT INTO course VALUES (1, 'basic dirt skill', 'beginner dirt class', '2023-01-30', 100, 'dirt'),(2, 'advanced street skill', 'advanced street class with great instructors', '2023-01-29', 120.00 , 'street'),(3, 'basic street skill', 'beginner street class, very easy', '2023-01-31', 100, 'street');

INSERT INTO bike VALUES ('1GHS8363429D49K2B', '123456789', true, 'Mitsubishi','dirt', 250),('A7B6SDN9DD7H372J8','CUTE',false,'Tesla','dirt',420),('K6HDATRJ372J8','FUN',true,'Honda','street',690),('8IAS67H372J8','MESSED',true,'Subaru','street',696),('90765ASD7H372J8','STA5K',true,'Ducati','dirt',424),('43478HSFD7NJ8','C0LLEN',false,'Harley','street',123),('NYH2334DD7234','SW1N',true,'Harley','dirt',456);

INSERT INTO range_location VALUES (1, 'street', 15),(2,'street', 15 ),(3,'street', 15 ),(4,'dirt', 15);

INSERT INTO classroom_location VALUES ('A', 15),('B', 30);

INSERT INTO repair_bike VALUES ('1GHS8363429D49K2B','2023-01-01', 1000, NULL, 'electric problem'),('A7B6SDN9DD7H372J8','2023-01-01', 1500, NULL, 'light problem'),('K6HDATRJ372J8','2022-01-30', 1200, '2022-01-30', 'gas problem');

INSERT INTO enrolled_in VALUES (2,1,true,70,80,90,100,20,70),(1,2,true,70,80,90,70,40,70),(1,3,true,56,86,99,99,70,40),(3,4,true,20,20,10,90,20,50),(3,5,false,60,60,60,50,90,70),(1,6,true,80,100,99,100,100,89),(1,7,false,70,80,90,100,20,70),(2,7,true,90,88,99,100,36,75);

INSERT INTO uses VALUES (1,4,'BOTH'),(2,2,'AM'),(3,3,'PM');

INSERT INTO taught_in VALUES (2,'A','PM'),(3,'A','AM');

INSERT INTO instructs VALUES(6,3,'AM','street_teacher'),(1,3,'PM','street_coach'),(2,3,'PM', 'street_coach'),(3,3,'PM', 'street_coach'),(4,2,'BOTH','street_coach_teacher'),(2,2,'AM','street_coach'),(3,2, 
'AM', 'street_coach'),(4,1,'BOTH','dirt_coach'),(5,1,'BOTH','dirt_coach'),(6,1,'BOTH','dirt_coach');

INSERT INTO assigned_to VALUES (1, 'NYH2334DD7234'),(2, '43478HSFD7NJ8'),(3, '8IAS67H372J8');

/**
Basic Dirt Bike Course (course id 1)
Range Coach 1: Khal Drogo (id 4)
Range Coach 2: Jorah Mormont (id 5)
Range Coach 3: Jamie Lannister (id 6)
Location AM: Range 4
Location PM: Range 4
Bike(s):NYH2334DD7234' (dirt)
Student(s): student 2(Daenerys Targaryen), 3(Sansa Stark), 6 (Joffrey Baratheon), 7 (Samwell Tarley)

Advanced Street (course id 2)
Teacher: Khal Drogo (id 4) (session PM)
Range Coach 1: Khal Drogo (id 4) 
Range Coach 2: Arya Stark (id 2)
Range Coach 3: Theon Greyjoy (id 3)
Location AM:  Range 2
Location PM: Classroom A
Bike(s): '43478HSFD7NJ8' Street
Student(s): Jon Snow (1), Samwell Tarley (7)

Basic Street (course id 3)
Teacher: jamie lannister (id 6) (session AM)
Range Coach 1: Cersei Lannister (id 1)
Range Coach 2: Arya Stark (id 2)
Range Coach 3: Theon Greyjoy (id 3)
Location AM: Classroom A
Location PM: Range 3
Student(s): Tyrion Lannister (4) and Rob Stark (5)

**location must align with whether teacher or range coach is assigned for that session)
**Instructor must be able to teach/coach what they are teaching.
**Appropriate bike type must be assigned.
**/