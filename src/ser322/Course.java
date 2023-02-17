package ser322;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Course extends Option implements OptionProtocol {

  
    public void openMenu(Connection conn, Scanner scr) {
        do {
            displayMenuOptions();
            userOpt = scr.nextLine();
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    create(conn, scr);
                    break;
                case "2":
                    view(conn, scr);
                    break;
                case "3":
                    //edit
                    break;
                case "4":
                    delete(conn, scr);
                    break;
                case "0":
                    returnToMainMenu();
                    break;
                default:
                    invalidInput("4");
                    break;
            } 
        } while (isDone == false);        
    }

    public void viewOpts() {

    }

    public void displayMenuOptions() {
        System.out.println("Manage Courses");
        System.out.println("\t1 - Create New Course");
        System.out.println("\t2 - View Courses");
        System.out.println("\t3 - Edit Course");
        System.out.println("\t4 - Delete Course");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-4)");
    }

    public void view(Connection conn, Scanner scr) {
        String queryStmt = "SELECT * from course";
        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(queryStmt);
            viewDB(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Connection conn, Scanner scr) {
        // Input Store Variables
        Integer _pk = null;

        PreparedStatement ps = null;
        
        System.out.println("Enter course to delete: pk(course_id):");
        _pk = scr.nextInt();

        String deleteStmt = "DELETE FROM course WHERE course_id = ?";
        try {
            ps = conn.prepareStatement(deleteStmt);
            ps.setInt(1, _pk);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (updateDB(ps, conn)) {
            System.out.println("Record deleted successfully.");
        } else {
            System.out.println("No records found to delete.");
        }
    }

    public void create(Connection conn, Scanner scr) {
        // Input Store Variables
        Integer _courseId = null;
        String _courseName = null;
        String _courseDescription = null;
        java.sql.Date _courseDate = null;
        Integer _cost = null;
        String _courseType = "dirt";



        // Statement & Duplicate Check Variables
        PreparedStatement ps = null;
        PreparedStatement psdc = null;
        Boolean duplicate = null;

        // Prompt for Input
        System.out.print("Enter course id: \n");
        _courseId = scr.nextInt();
        System.out.print("Enter course name: \n");
        scr.nextLine();
        _courseName = scr.nextLine();
        System.out.print("Enter course description: \n");
        _courseDescription = scr.nextLine();
        System.out.print("Enter course date: (YYYY-MM-DD) \n");
        String _dateStr = scr.nextLine();
        _courseDate = parseDate(_dateStr);
        System.out.print("Enter course cost: \n");
        _cost = scr.nextInt();
        System.out.print("Enter course type: (dirt/street)\n");
        _courseType = scr.next();



  
        // Check for Duplicate
        String lookupStmt = "SELECT * FROM course WHERE course_id = ?";
        try {
            psdc = conn.prepareStatement(lookupStmt);
            psdc.setInt(1, _courseId);
            duplicate = checkDuplicate(psdc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return if Duplicate Found
        if ( duplicate ) {
            System.out.println("\nDUPLICATE ENTRY:: Course with provided ID already exists. Returning to menu.\n");
            return;
        }

        // Proceed with Insertion Flow
        String insertStmt = "INSERT INTO course VALUES (?, ?, ?, ?, ?, ?);";
        try {
            ps = conn.prepareStatement(insertStmt);

            ps.setInt(1, _courseId);
            ps.setString(2, _courseName);
            ps.setString(3, _courseDescription);
            ps.setDate(4, _courseDate);
            ps.setInt(5, _cost);
            ps.setString(6, _courseType);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (updateDB(ps, conn)) {
                System.out.println("Successfully added course to DB");
            } else {
                System.out.println("Failed to add course to DB");
            }
            try {
                // Ensure Statement is Closed
                if (ps != null) {
                    ps.close();
                }
            }
            catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }

    
        if (_courseType.toLowerCase() == "street") {

        } else if (_courseType.equals("dirt")) {

            System.out.println("\n##### " + "Creating new Dirt Bike Course \n");


            Integer _numStudents = null;
            System.out.println("Enter the number students you'd like to add to this course: (max 15)");
            _numStudents = scr.nextInt();
 
            if (_numStudents > 15) {
                _numStudents = 15;
                System.out.println("You entered a value greater than 15. Students will be capped at 15.");
            }

            Integer[] instructors = new Integer[3];

            // Add Instructors
            try {
                for(int i = 0; i < 3; i++) {
                    System.out.println("Enter ID for instructor #" + (i + 1));
                    instructors[i] = scr.nextInt();
                    PreparedStatement psInst = conn.prepareStatement("INSERT INTO instructs VALUES (?, ?, ?, ?);");
                    psInst.setInt(1, instructors[i]);
                    psInst.setInt(2, _courseId);
                    psInst.setString(3, "BOTH");
                    psInst.setString(4, "dirt_coach");
                    updateDB(psInst, conn);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            boolean addingStudents = true;
            int j = 0;

            while(addingStudents && j < _numStudents) {
                StudentModel sm = new StudentModel();

                System.out.println("Enter ID for Student #" + (j + 1));
                sm.student_id = scr.nextInt();

                System.out.println("Has the student completed payment for the course? (1/0)");
                sm.is_payment_completed = scr.nextInt();

                System.out.println("What is the student's written score?");
                sm.written_score = scr.nextInt();

                System.out.println("What is the students score for exercise #1?");
                sm.exercise_1_score = scr.nextInt();

                System.out.println("What is the students score for exercise #2?");
                sm.exercise_2_score = scr.nextInt();
                
                System.out.println("What is the students score for exercise #3?");
                sm.exercise_3_score = scr.nextInt();

                System.out.println("What is the students score for exercise #4?");
                sm.exercise_4_score = scr.nextInt();

                System.out.println("What is the students score for exercise #5?");
                sm.exercise_5_score = scr.nextInt();

                updateDB(sm.createEnrollmentEntry(conn, _courseId), conn);
                j++;
            }

        }
    }

    class StudentModel {
        int student_id;
        int is_payment_completed;
        int written_score;
        int exercise_1_score;
        int exercise_2_score;
        int exercise_3_score;
        int exercise_4_score;
        int exercise_5_score;

        public PreparedStatement createEnrollmentEntry(Connection conn, int course_id) {
            PreparedStatement ps = null;
            String query = "INSERT INTO enrolled_in VALUES (?,?,?,?,?,?,?,?,?);";
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, course_id);
                ps.setInt(2, this.student_id);
                ps.setInt(3, this.is_payment_completed);
                ps.setInt(4, written_score);
                ps.setInt(5, this.exercise_1_score);
                ps.setInt(6, this.exercise_2_score);
                ps.setInt(7, this.exercise_3_score);
                ps.setInt(8, this.exercise_4_score);
                ps.setInt(9, this.exercise_5_score);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ps;
        }
    }
    
    
}
