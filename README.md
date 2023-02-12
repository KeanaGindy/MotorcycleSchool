# SER322-Del4

## Overview
There are two options to build and run the application: 1) javac and java 2) gradle.

### Option 1: Building and Running the Application using javac/java

- Step 1) From the root directory of the project run: `javac -d ./classes/ ./src/ser322/Main.java <add other java files>`
javac -d ./classes/ ./src/ser322/Main.java ./src/ser322/Students.java
- Step 2) Before running these commands, you must import the `create_school_db.sql` AND  `insert_sample_data.sql`file into your database.
- Step 3) `java -cp '.\lib\mysql-connector-java-5.1.45-bin.jar;classes' ser322.Main <url> <user> <pwd> <driver>`

Note: For Windows (note the `;` If not on windows use `:`)

Example:
`javac -d ./classes/ ./src/ser322/Main.java ./src/ser322/Students.java`

`java -cp '.\lib\mysql-connector-java-5.1.45-bin.jar;classes' ser322.Main "jdbc:mysql://localhost:3306/motorcycleschool?autoconnect=true&useSSL=false" root example com.mysql.jdbc.Driver`

### Option 2: Building and Running the Application using Gradle
- Step 1) `./gradlew clean`
- Step 2) `./gradlew build`
- Step 3) Before running these commands, you must import the `create_school_db.sql` AND  `insert_sample_data.sql`file into your database.
- Step 4) `./gradlew run --args="<url> <user> <pwd> <driver>"`

Example: `./gradlew run --args="'jdbc:mysql://localhost:3306/motorcycleschool?autoconnect=true&useSSL=false' root example com.mysql.jdbc.Driver"`
