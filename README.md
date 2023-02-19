# SER322 Deliverable 4 | Team 3

## Overview
There are two options to build and run the application: 1) javac and java 2) gradle.

### Option 1: Building and Running the Application using javac/java

- Step 1) From the root directory of the project run: `javac -d ./classes/ ./src/ser322/Main.java <add other java files>`
javac -d ./classes/ ./src/ser322/Main.java ./src/ser322/Students.java
- Step 2) Before running these commands, you must import the `create_school_db.sql` AND  `insert_sample_data.sql`file into your database.
- Step 3) `java ser322.Main <url> <user> <pwd> <driver>` (should also include `-cp` with your driver and classpath as shown in example below)

Example:
`javac -d ./classes/ ./src/ser322/Main.java ./src/ser322/Students.java ./src/ser322/Course.java ./src/ser322/Option.java ./src/ser322/OptionProtocol.java ./src/ser322/Ranges.java ./src/ser322/Bike.java ./src/ser322/Classrooms.java ./src/ser322/Instructors.java` 

`java -cp '.\lib\mysql-connector-java-5.1.45-bin.jar;classes' ser322.Main "jdbc:mysql://localhost:3306/motorcycleschool?autoconnect=true&useSSL=false" root example com.mysql.jdbc.Driver`

Notes: 
- For Windows (note the `;` If not on windows use `:`)
- name of the db is `motercycleschool`

### Option 2: Building and Running the Application using Gradle
- Step 0) Ensure you can you use gradle wrapper by running: `gradle wrapper --gradle version 7.0`
- Step 1) `./gradlew clean`
- Step 2) `./gradlew build`
- Step 3) Before running these commands, you must import the `create_school_db.sql` AND  `insert_sample_data.sql`file into your database.
- Step 4) `./gradlew run --args="<url> <user> <pwd> <driver>"`

Example: `./gradlew run --args="'jdbc:mysql://localhost:3306/motorcycleschool?autoconnect=true&useSSL=false' root example com.mysql.jdbc.Driver"`
