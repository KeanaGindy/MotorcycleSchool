package ser322;

import java.sql.Connection;
import java.sql.Date;
import java.util.Locale;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Students {

    public static void showStudentMenu(Connection conn, Scanner scr) {
        boolean isDone = false;
        
        String userOpt = "-1";
        do {
            displayStudentMenu();
            userOpt = scr.nextLine();
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    createStudent(conn, scr);
                    break;
                case "2":
                    //view
                    break;
                case "3":
                    //edit
                    break;
                case "4":
                    deleteStudent(conn, scr);
                    break;
                case "0":
                    //exit to main menu
                    isDone = true;
                    System.out.println("Returning to main menu..");
                    break;
                default:
                    //invalid input
                    System.out.println("Invalid menu option. Please try again with a valid integer (0-4).");
                    break;
            } 
        } while (isDone == false);
    }
    /* Method to display student menu*/
    public static void displayStudentMenu() {
        System.out.println("Manage Students");
        System.out.println("\t1 - Create New Student");
        System.out.println("\t2 - View Students");
        System.out.println("\t3 - Edit Student");
        System.out.println("\t4 - Delete Student");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-4)");
    }
    
    /*
     * Method to add a new student to the database
     */
    public static void createStudent(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        int student_id = -1;
        String student_name = "";
        java.sql.Date dob = null;
        String address = "";
        String phone = "";
        boolean studentExists = false;

        //get user input
        System.out.println("Please enter the student's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        student_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        System.out.println("Please enter the student's full name: ");
        student_name = scr.nextLine();

        //TODO add error message for incorrect date format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        System.out.println("Please enter the student's date of birth (format: `YYYY-MM-DD`): ");
        String dob_str = "";
        dob_str = scr.nextLine();
        try {
            java.util.Date temp_date = formatter.parse(dob_str);
            dob = new java.sql.Date(temp_date.getTime());  
        } catch (ParseException e) {
            System.out.println("Date was not in correct format");
            e.printStackTrace();
        }

        System.out.println("Please enter the student's address: ");
        address = scr.nextLine();

        //TODO add error checking for number of digits in phone number
        System.out.println("Please enter the student's phone number (no dashes/no spaces): ");
        phone = scr.nextLine();

        //check to make sure student doesn't already exist
        //if doesn't exist write to db
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM student WHERE student_id = ?");
            psCheckDupe.setInt(1, student_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs != null && i > 0) {
                //student exists
                System.out.println("Student already exists! Returning to menu...");
                studentExists = true;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (!studentExists) {
                ps = conn.prepareStatement("INSERT INTO student VALUES (?, ?, ?, ?, ?);");
                ps.setInt(1, student_id);
                ps.setString(2, student_name);
                ps.setDate(3, dob);
                ps.setString(4, address);
                ps.setString(5, phone);
                if (ps.executeUpdate() > 0) {
                    System.out.println("Inserted student OK");
                }
                ps.clearParameters();
                ps.close();

                // Have to do this to write changes to a DB
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) 
                    ps.close();
                if (psCheckDupe != null)
                    psCheckDupe.close();
            }
            catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }

    }

     /*
     * Method to delete a new student from the database
     */
    public static void deleteStudent(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        int student_id = -1;
        boolean studentExists = true;

        //get user input
        System.out.println("Please enter the student's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        student_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        //check to make sure student exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM student WHERE student_id = ?");
            psCheckDupe.setInt(1, student_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //student does not exist
                System.out.println("Student does not exist! Returning to menu...");
                studentExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (studentExists) {
                ps = conn.prepareStatement("DELETE FROM student WHERE student_id = ?;");
                ps.setInt(1, student_id);
                if (ps.executeUpdate() > 0) {
                    System.out.println("Removed student OK");
                }
                ps.clearParameters();
                ps.close();

                // Have to do this to write changes to a DB
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) 
                    ps.close();
                if (psCheckDupe != null)
                    psCheckDupe.close();
            }
            catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }

    }
}
