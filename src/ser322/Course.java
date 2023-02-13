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
                    view(conn);
                    break;
                case "3":
                    //edit
                    break;
                case "4":
                    //delete
                    break;
                case "0":
                    returnToMainMenu();
                    break;
                default:
                    invalidInput();
                    break;
            } 
        } while (isDone == false);        
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

    public void view(Connection conn) {
        String queryStmt = "SELECT * from course";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryStmt);
            viewDB(rs);
        } catch (SQLException e) {
            e.printStackTrace();
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
        System.out.print("Enter course type: \n");
        _courseType = scr.nextLine();
        System.out.print("Enter course date: (YYYY-MM-DD) \n");
        String _dateStr = scr.nextLine();
        _courseDate = parseDate(_dateStr);
        System.out.print("Enter course cost: \n");
        _cost = scr.nextInt();


  
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
    }
    
}
