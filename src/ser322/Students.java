package ser322;

import java.sql.Connection;
import java.sql.Date;
import java.util.Locale;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/* 
 * Class to manage student data in db. Handles create, update, delete methods for students.
 * 
*/
public class Students {

    /*
     * Method to display student submenu and get/validate user option. 
     * Method calls appropriate submenu based on user input.
     */
    public void showStudentMenu(Connection conn, Scanner scr) {
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
                    viewStudentOptions(conn, scr);
                    break;
                case "3":
                    editStudentOptions(conn, scr);
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
                    System.out.println("Invalid menu option. Please try again with a valid integer (0-4).");
                    break;
            } 
        } while (isDone == false);
    }

    /* 
     * Method to display student menu.
    */
    public static void displayStudentMenu() {
        System.out.println("-----------------------------------------");
        System.out.println("Manage Students");
        System.out.println("-----------------------------------------");
        System.out.println("\t1 - Create New Student");
        System.out.println("\t2 - View Students");
        System.out.println("\t3 - Edit Student");
        System.out.println("\t4 - Delete Student");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-4)");
    }
    
    /*
     * Method to add a new student to the database.
    */
    public void createStudent(Connection conn, Scanner scr) {
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
     * Method to delete a new student from the database.
     */
    public void deleteStudent(Connection conn, Scanner scr) {
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

    /*
     * Method to ask user which view option they would like.
    */
    public void viewStudentOptions(Connection conn, Scanner scr) {
        boolean isComplete = false;
        String userOpt = "-1";
        do {
            //ask user if want to view all students of student report
            System.out.println("-----------------------------------------");
            System.out.println("View Students Menu");
            System.out.println("-----------------------------------------");
            System.out.println("\t1 - View all Students");
            System.out.println("\t2 - View Student Report");
            System.out.println("\t0 - Return to Student Menu");
            System.out.println("Please select a valid menu option (0-2)");

            userOpt = scr.nextLine();
            
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    viewAllStudents(conn, scr);
                    break;
                case "2":
                    viewStudentReport(conn, scr);
                    break;
                case "0":
                    //exit to main menu
                    isComplete = true;
                    System.out.println("Returning to student menu..");
                    break;
                default:
                    //invalid input
                    System.out.println("Invalid menu option. Please try again with a valid option (0-2).");
                    break;
            } 
        } while(isComplete == false);
   
    }

    /*
     * Method to view all students in db
    */
    public void viewAllStudents(Connection conn, Scanner scr) {
        Statement stmt = null;
        ResultSet rs = null;
        System.out.println("Displaying all students: ");
    
        //check to make sure student exists
	    try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM student;");
            System.out.printf("%-20s %-20s %-20s %-20s %-20s", "student_id", "student_name", "dob", "address", "phone");
            System.out.println();
            // Display the results
			while (rs.next()) {
                System.out.printf("%-20d",  rs.getInt("student_id"));
                System.out.printf("%-20s",  rs.getString("student_name"));
                System.out.printf("%-20s",  rs.getDate("dob"));
                System.out.printf("%-20s",  rs.getString(4));
                System.out.printf("%-20s",  rs.getString("phone"));
                System.out.println();
			}

            // Have to do this to write changes to a DB
            conn.commit();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) 
                    stmt.close();
                if (rs != null)
                    rs.close();
            }
            catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }

    }

    /*
     * Method to view a report for a student
    */
    public void viewStudentReport(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        ResultSet reportRs = null;
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
                System.out.println("STUDENT REPORT FOR STUDENT WITH ID NUMBER: " + student_id);
                ps = conn.prepareStatement("SELECT * FROM enrolled_in WHERE student_id = ? ORDER BY course_id;");
                ps.setInt(1, student_id);
                reportRs = ps.executeQuery();

                // Display the results
                System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s", "course_id", "student_id", "payment", "written_score", "ex1_score","ex2_score","ex3_score", "ex4_score", "ex5_score");
                System.out.println();
                boolean paymentVal;
                while (reportRs.next()) {
                    System.out.printf("%-16d",  reportRs.getInt("course_id"));
                    System.out.printf("%-16s",  reportRs.getInt("student_id"));
                    paymentVal = reportRs.getBoolean("is_payment_completed");
                    if (paymentVal == true) {
                        System.out.printf("%-16s",  "yes");
                    } else {
                        System.out.printf("%-16s",  "no");
                    }
                    System.out.printf("%-16d",  reportRs.getInt("written_score"));
                    System.out.printf("%-16d",  reportRs.getInt("exercise_1_score"));
                    System.out.printf("%-16d",  reportRs.getInt("exercise_2_score"));
                    System.out.printf("%-16d",  reportRs.getInt("exercise_3_score"));
                    System.out.printf("%-16d",  reportRs.getInt("exercise_4_score"));
                    System.out.printf("%-16d",  reportRs.getInt("exercise_5_score"));
                    System.out.println();
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
                if (rs != null)
                    rs.close();
                if (reportRs != null)
                    reportRs.close();
            }
            catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }

    }

    /*
     * Method to ask user which edit option they would like.
    */
    public void editStudentOptions(Connection conn, Scanner scr) {
        boolean isComplete = false;
        String userOpt = "-1";
        do {
            //ask user if want to view all students of student report
            System.out.println("-----------------------------------------");
            System.out.println("Edit Students Menu");
            System.out.println("-----------------------------------------");
            System.out.println("\t1 - Edit Student Name");
            System.out.println("\t2 - Edit Student Address");
            System.out.println("\t3 - Edit Student Phone");
            System.out.println("\t0 - Return to Student Menu");
            System.out.println("Please select a valid menu option (0-3)");

            userOpt = scr.nextLine();
            
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    editStudentName(conn, scr);
                    break;
                case "2":
                    editStudentAddress(conn, scr);
                    break;
                case "3":
                    editStudentPhone(conn, scr);
                    break;
                case "0":
                    //exit to main menu
                    isComplete = true;
                    System.out.println("Returning to student menu..");
                    break;
                default:
                    //invalid input
                    System.out.println("Invalid menu option. Please try again with a valid option (0-3).");
                    break;
            } 
        } while(isComplete == false);
   
    }

    /*
     * Method to edit a student name based on student id.
    */
    public void editStudentName(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String new_name = "";
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
                //get new name
                System.out.println("Please enter the student's new name: ");
                new_name = scr.nextLine();

                //make update
                System.out.println("EDIT STUDENT NAME TO " + new_name + " FOR STUDENT WITH ID NUMBER: " + student_id);
                ps = conn.prepareStatement("UPDATE student SET student_name = ? WHERE student_id = ?;");
                ps.setString(1, new_name);
                ps.setInt(2, student_id);
                
                if (ps.executeUpdate() > 0) {
                    System.out.println("Updated student OK");
                } else {
                    System.out.println("Update failed.");
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
                if (rs != null)
                    rs.close();
            }
            catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }

    }

    /*
     * Method to edit a student address based on student id.
    */
    public void editStudentAddress(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String new_address = "";
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
                //get new name
                System.out.println("Please enter the student's new address: ");
                new_address = scr.nextLine();

                //make update
                System.out.println("EDIT STUDENT ADDRESS TO " + new_address + " FOR STUDENT WITH ID NUMBER: " + student_id);
                ps = conn.prepareStatement("UPDATE student SET address = ? WHERE student_id = ?;");
                ps.setString(1, new_address);
                ps.setInt(2, student_id);
                
                if (ps.executeUpdate() > 0) {
                    System.out.println("Updated student OK");
                } else {
                    System.out.println("Update failed.");
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
                if (rs != null)
                    rs.close();
            }
            catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }

    }

    /*
     * Method to edit a student phone number based on student id.
    */
    public void editStudentPhone(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String new_phone= "";
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
                //get new name
                System.out.println("Please enter the student's new phone number: ");
                new_phone = scr.nextLine();

                //make update
                System.out.println("EDIT STUDENT ADDRESS TO " + new_phone + " FOR STUDENT WITH ID NUMBER: " + student_id);
                ps = conn.prepareStatement("UPDATE student SET phone = ? WHERE student_id = ?;");
                ps.setString(1, new_phone);
                ps.setInt(2, student_id);
                
                if (ps.executeUpdate() > 0) {
                    System.out.println("Updated student OK");
                } else {
                    System.out.println("Update failed.");
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
                if (rs != null)
                    rs.close();
            }
            catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }

    }

}
