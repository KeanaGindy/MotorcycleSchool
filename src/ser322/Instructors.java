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


public class Instructors {
    /*
     * Method to display instructor submenu and get/validate user option. 
     * Method calls appropriate submenu based on user input.
     */
    public void showInstructorMenu(Connection conn, Scanner scr) {
        boolean isDone = false;
        
        String userOpt = "-1";
        do {
            displayInstructorMenu();
            userOpt = scr.nextLine();
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    createInstructor(conn, scr);
                    break;
                case "2":
                    viewInstructorOptions(conn, scr);
                    break;
                case "3":
                    editInstructorOptions(conn, scr);
                    break;
                case "4":
                    deleteInstructor(conn, scr);
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
     * Method to display instructor menu.
    */
    public static void displayInstructorMenu() {
        System.out.println("-----------------------------------------");
        System.out.println("Manage instructors");
        System.out.println("-----------------------------------------");
        System.out.println("\t1 - Create New Instructor");
        System.out.println("\t2 - View instructors");
        System.out.println("\t3 - Edit Instructor");
        System.out.println("\t4 - Delete Instructor");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-4)");
    }
    
    /*
     * Method to add a new instructor to the database.
    */
    public void createInstructor(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        int instructor_id = -1;
        String instructor_name = "";
        java.sql.Date dob = null;
        String address = "";
        String phone = "";
        boolean coach_dirt_bike = false;
        boolean coach_street_bike = false;
        boolean teach_street_bike = false;
        boolean instructorExists = false;

        //get user input
        System.out.println("Please enter the instructor's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        instructor_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        System.out.println("Please enter the instructor's full name: ");
        instructor_name = scr.nextLine();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        System.out.println("Please enter the instructor's date of birth (format: `YYYY-MM-DD`): ");
        String dob_str = "";
        dob_str = scr.nextLine();
        try {
            java.util.Date temp_date = formatter.parse(dob_str);
            dob = new java.sql.Date(temp_date.getTime());  
        } catch (ParseException e) {
            System.out.println("Date was not in correct format");
            e.printStackTrace();
        }

        System.out.println("Please enter the instructor's address: ");
        address = scr.nextLine();

        //TODO add error checking for number of digits in phone number
        System.out.println("Please enter the instructor's phone number (no dashes/no spaces): ");
        phone = scr.nextLine();

        String userOpt;

        System.out.println("Does the instructor coach dirt bike? (y/n)"); 
        userOpt = scr.nextLine();
            //validate user input
            switch (userOpt) {
                case "y":
                    coach_dirt_bike =  true;
                    break;
                case "n":
                    coach_dirt_bike = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again with a valid option (y/n).");
                    break;
            } 

        System.out.println("Does the instructor coach street bike? (y/n)"); 
        userOpt = scr.nextLine();
            //validate user input
            switch (userOpt) {
                case "y":
                    coach_street_bike =  true;
                    break;
                case "n":
                    coach_street_bike = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again with a valid option (y/n).");
                    break;
            }

            System.out.println("Does the instructor teach street bike? (y/n)"); 
            userOpt = scr.nextLine();
                //validate user input
                switch (userOpt) {
                    case "y":
                        teach_street_bike =  true;
                        break;
                    case "n":
                        teach_street_bike = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again with a valid option (y/n).");
                        break;
                }    
        

        //check to make sure instructor doesn't already exist
        //if doesn't exist write to db
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM instructor WHERE instructor_id = ?");
            psCheckDupe.setInt(1, instructor_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs != null && i > 0) {
                //instructor exists
                System.out.println("Instructor already exists! Returning to menu...");
                instructorExists = true;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (!instructorExists) {
                ps = conn.prepareStatement("INSERT INTO instructor VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                ps.setInt(1, instructor_id);
                ps.setString(2, instructor_name);
                ps.setDate(3, dob);
                ps.setString(4, address);
                ps.setString(5, phone);
                ps.setBoolean(6, coach_dirt_bike);
                ps.setBoolean(7, coach_street_bike);
                ps.setBoolean(8, teach_street_bike);
                if (ps.executeUpdate() > 0) {
                    System.out.println("Inserted instructor OK");
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
     * Method to delete a new instructor from the database.
     */
    public void deleteInstructor(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        int instructor_id = -1;
        boolean instructorExists = true;

        //get user input
        System.out.println("Please enter the instructor's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        instructor_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        //check to make sure instructor exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM instructor WHERE instructor_id = ?");
            psCheckDupe.setInt(1, instructor_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //instructor does not exist
                System.out.println("Instructor does not exist! Returning to menu...");
                instructorExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (instructorExists) {
                ps = conn.prepareStatement("DELETE FROM instructor WHERE instructor_id = ?;");
                ps.setInt(1, instructor_id);
                if (ps.executeUpdate() > 0) {
                    System.out.println("Removed instructor OK");
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
    public void viewInstructorOptions(Connection conn, Scanner scr) {
        boolean isComplete = false;
        String userOpt = "-1";
        do {
            //ask user if want to view all instructors of instructor report
            System.out.println("-----------------------------------------");
            System.out.println("View instructors Menu");
            System.out.println("-----------------------------------------");
            System.out.println("\t1 - View all instructors");
            System.out.println("\t2 - View available instructors for specific date");
            System.out.println("\t3 - View instructor schedule based on instructor id");
            System.out.println("\t0 - Return to Instructor Menu");
            System.out.println("Please select a valid menu option (0-3)");

            userOpt = scr.nextLine();
            
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    viewAllinstructors(conn, scr);
                    break;
                case "2":
                    viewInstructorAvailability(conn, scr);
                    break;
                case "3":
                    viewInstructorSchedule(conn, scr);
                    break;
                case "0":
                    //exit to main menu
                    isComplete = true;
                    System.out.println("Returning to instructor menu..");
                    break;
                default:
                    //invalid input
                    System.out.println("Invalid menu option. Please try again with a valid option (0-2).");
                    break;
            } 
        } while(isComplete == false);
   
    }

    /*
     * Method to view all instructors in db
    */
    public void viewAllinstructors(Connection conn, Scanner scr) {
        Statement stmt = null;
        ResultSet rs = null;
        System.out.println("Displaying all instructors: ");
    
        //check to make sure instructor exists
	    try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM instructor;");
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s", "instructor_id", "instructor_name", "dob", "address", "phone", "coach_dirt_bike","coach_street_bike","teach_street_bike");
            System.out.println();
            // Display the results
			while (rs.next()) {
                System.out.printf("%-20d",  rs.getInt("instructor_id"));
                System.out.printf("%-20s",  rs.getString("instructor_name"));
                System.out.printf("%-20s",  rs.getDate("dob"));
                System.out.printf("%-20s",  rs.getString(4));
                System.out.printf("%-20s",  rs.getString("phone"));
                System.out.printf("%-20s",  rs.getBoolean("coach_dirt_bike"));
                System.out.printf("%-20s",  rs.getBoolean("coach_street_bike"));
                System.out.printf("%-20s",  rs.getBoolean("teach_street_bike"));
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
     * Method to view instructor schedule based on id
    */
    public void viewInstructorSchedule(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        ResultSet reportRs = null;
        int instructor_id = -1;
        boolean instructorExists = true;
    
        //get user input
        System.out.println("Please enter the instructor's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        instructor_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        //check to make sure instructor exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM instructor WHERE instructor_id = ?");
            psCheckDupe.setInt(1, instructor_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //instructor does not exist
                System.out.println("Instructor does not exist! Returning to menu...");
                instructorExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (instructorExists) {
                System.out.println("INSTRUCTOR SCHEDULE FOR INSTRUCTOR WITH ID NUMBER: " + instructor_id);
                ps = conn.prepareStatement("SELECT i.instructor_id AS instructor_id, i.instructor_name AS instructor_name, c.course_id AS course_id, c.course_name AS course_name, c.course_type, c.course_date, s.in_session FROM instructor i JOIN instructs s ON i.instructor_id = s.instructor_id JOIN course c ON s.course_id = c.course_id WHERE i.instructor_id = ?");
                ps.setInt(1, instructor_id);
                reportRs = ps.executeQuery();

                // Display the results
                System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s", "instructor_id", "instructor_name", "course_id", "course_name", "course_type","course_date","s.in_session");
                System.out.println();
                while (reportRs.next()) {
                    System.out.printf("%-16d",  reportRs.getInt("instructor_id"));
                    System.out.printf("%-16s",  reportRs.getString("instructor_name"));
                    System.out.printf("%-16d",  reportRs.getInt("course_id"));
                    System.out.printf("%-16d",  reportRs.getString("course_name"));
                    System.out.printf("%-16d",  reportRs.getString("course_type"));
                    System.out.printf("%-16d",  reportRs.getDate("course_date"));
                    System.out.printf("%-16d",  reportRs.getString("s.in_session"));
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
     * Method to view instructor availability based on date  NEED TO FINISH
    */

    public void viewInstructorAvailability(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet reportRs = null;
        boolean instructorExists = true;
        java.sql.Date dateSearch = null;
    
        //get user input
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        System.out.println("Please enter the date to search for availibity (format: `YYYY-MM-DD`): ");
        String date_str = "";
        date_str = scr.nextLine();
        try {
            java.util.Date temp_date = formatter.parse(date_str);
            dateSearch = new java.sql.Date(temp_date.getTime());  
        } catch (ParseException e) {
            System.out.println("Date was not in correct format");
            e.printStackTrace();
        }
        scr.nextLine(); // consume extra newline

        //check to make sure instructor exists
	    try {
            if (instructorExists) {
                System.out.println("AVAILIBILITY REPORT FOR: " + dateSearch);
                ps = conn.prepareStatement("SELECT instructor_id, instructor_name, coach_dirt_bike, coach_street_bike, teach_street_bike FROM instructor where instructor_id NOT IN (SELECT instructor_id FROM instructs WHERE course_id IN (SELECT course_id FROM course WHERE course_date=?));");
                ps.setDate(1, dateSearch);
                reportRs = ps.executeQuery();

                // Display the results
                System.out.printf("%-15s %-15s %-15s %-15s %-15s", "instructor_id", "instructor_name", "coach_dirt_bike", "coach_street_bike", "teach_street_bike");
                System.out.println();
                boolean coachDirt;
                boolean coachStreet;
                boolean teachStreet;
                
                while (reportRs.next()) {
                    System.out.printf("%-16d",  reportRs.getInt("instructor_id"));
                    System.out.printf("%-16s",  reportRs.getString("instructor_name"));
                    coachDirt = reportRs.getBoolean("coach_dirt_bike");
                    if (coachDirt == true) {
                        System.out.printf("%-16s",  "yes");
                    } else {
                        System.out.printf("%-16s",  "no");
                    }
                    coachStreet = reportRs.getBoolean("coach_street_bike");
                    if (coachStreet == true) {
                        System.out.printf("%-16s",  "yes");
                    } else {
                        System.out.printf("%-16s",  "no");
                    }
                    teachStreet = reportRs.getBoolean("teach_street_bike");
                    if (teachStreet == true) {
                        System.out.printf("%-16s",  "yes");
                    } else {
                        System.out.printf("%-16s",  "no");
                    }
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
    public void editInstructorOptions(Connection conn, Scanner scr) {
        boolean isComplete = false;
        String userOpt = "-1";
        do {
            //ask user if want to view all instructors of instructor report
            System.out.println("-----------------------------------------");
            System.out.println("Edit instructors Menu");
            System.out.println("-----------------------------------------");
            System.out.println("\t1 - Edit Instructor Name");
            System.out.println("\t2 - Edit Instructor Address");
            System.out.println("\t3 - Edit Instructor Phone");
            System.out.println("\t4 - Edit Instructor Coaching Dirt Bike Status");
            System.out.println("\t5 - Edit Instructor Coaching Street Bike Status");
            System.out.println("\t6 - Edit Instructor Teaching Street Bike Status");
            System.out.println("\t0 - Return to Instructor Menu");
            System.out.println("Please select a valid menu option (0-6)");

            userOpt = scr.nextLine();
            
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    editInstructorName(conn, scr);
                    break;
                case "2":
                    editInstructorAddress(conn, scr);
                    break;
                case "3":
                    editInstructorPhone(conn, scr);
                    break;
                case "4":
                    editInstructorCoachDirt(conn, scr);
                    break;
                case "5":
                    editInstructorCoachStreet(conn, scr);
                    break;
                case "6":
                    editInstructorTeachStreet(conn, scr);
                    break;
                case "0":
                    //exit to main menu
                    isComplete = true;
                    System.out.println("Returning to instructor menu..");
                    break;
                default:
                    //invalid input
                    System.out.println("Invalid menu option. Please try again with a valid option (0-6).");
                    break;
            } 
        } while(isComplete == false);
   
    }

    /*
     * Method to edit a instructor name based on instructor id.
    */
    public void editInstructorName(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String new_name = "";
        int instructor_id = -1;
        boolean instructorExists = true;
    
        //get user input
        System.out.println("Please enter the instructor's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        instructor_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        //check to make sure instructor exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM instructor WHERE instructor_id = ?");
            psCheckDupe.setInt(1, instructor_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //instructor does not exist
                System.out.println("Instructor does not exist! Returning to menu...");
                instructorExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (instructorExists) {
                //get new name
                System.out.println("Please enter the instructor's new name: ");
                new_name = scr.nextLine();

                //make update
                System.out.println("EDIT INSTRUCTOR NAME TO " + new_name + " FOR INSTRUCTOR WITH ID NUMBER: " + instructor_id);
                ps = conn.prepareStatement("UPDATE instructor SET instructor_name = ? WHERE instructor_id = ?;");
                ps.setString(1, new_name);
                ps.setInt(2, instructor_id);
                
                if (ps.executeUpdate() > 0) {
                    System.out.println("Updated instructor OK");
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
     * Method to edit a instructor address based on instructor id.
    */
    public void editInstructorAddress(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String new_address = "";
        int instructor_id = -1;
        boolean instructorExists = true;
    
        //get user input
        System.out.println("Please enter the instructor's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        instructor_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        //check to make sure instructor exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM instructor WHERE instructor_id = ?");
            psCheckDupe.setInt(1, instructor_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //instructor does not exist
                System.out.println("Instructor does not exist! Returning to menu...");
                instructorExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (instructorExists) {
                //get new name
                System.out.println("Please enter the instructor's new address: ");
                new_address = scr.nextLine();

                //make update
                System.out.println("EDIT INSTRUCTOR ADDRESS TO " + new_address + " FOR INSTRUCTOR WITH ID NUMBER: " + instructor_id);
                ps = conn.prepareStatement("UPDATE instructor SET address = ? WHERE instructor_id = ?;");
                ps.setString(1, new_address);
                ps.setInt(2, instructor_id);
                
                if (ps.executeUpdate() > 0) {
                    System.out.println("Updated instructor OK");
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
     * Method to edit a instructor phone number based on instructor id.
    */
    public void editInstructorPhone(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String new_phone= "";
        int instructor_id = -1;
        boolean instructorExists = true;
    
        //get user input
        System.out.println("Please enter the instructor's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        instructor_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        //check to make sure instructor exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM instructor WHERE instructor_id = ?");
            psCheckDupe.setInt(1, instructor_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //instructor does not exist
                System.out.println("Instructor does not exist! Returning to menu...");
                instructorExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (instructorExists) {
                //get new name
                System.out.println("Please enter the instructor's new phone number: ");
                new_phone = scr.nextLine();

                //make update
                System.out.println("EDIT INSTRUCTOR ADDRESS TO " + new_phone + " FOR INSTRUCTOR WITH ID NUMBER: " + instructor_id);
                ps = conn.prepareStatement("UPDATE instructor SET phone = ? WHERE instructor_id = ?;");
                ps.setString(1, new_phone);
                ps.setInt(2, instructor_id);
                
                if (ps.executeUpdate() > 0) {
                    System.out.println("Updated instructor OK");
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
     * Method to edit a instructor coaching status for dirt bikes based on instructor id.
    */
    public void editInstructorCoachDirt(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String newStatus= "";
        int instructor_id = -1;
        boolean instructorExists = true;
        boolean statusUpdate = true;
    
        //get user input
        System.out.println("Please enter the instructor's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        instructor_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        //check to make sure instructor exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM instructor WHERE instructor_id = ?");
            psCheckDupe.setInt(1, instructor_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //instructor does not exist
                System.out.println("Instructor does not exist! Returning to menu...");
                instructorExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (instructorExists) {
                //get new name
                System.out.println("Please enter the instructor's new coaching status for dirt bikes: ");

                newStatus = scr.nextLine();
                //validate user input
                switch (newStatus) {
                    case "y":
                        statusUpdate =  true;
                        break;
                    case "n":
                        statusUpdate = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again with a valid option (y/n).");
                        break;
                }

                //make update
                System.out.println("EDIT INSTRUCTOR COACHING DIRT BIKE STATUS TO " + statusUpdate + " FOR INSTRUCTOR WITH ID NUMBER: " + instructor_id);
                ps = conn.prepareStatement("UPDATE instructor SET coach_dirt_bike = ? WHERE instructor_id = ?;");
                ps.setBoolean(1, statusUpdate);
                ps.setInt(2, instructor_id);
                
                if (ps.executeUpdate() > 0) {
                    System.out.println("Updated instructor OK");
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
     * Method to edit a instructor coaching status for street bikes based on instructor id.
    */
    public void editInstructorCoachStreet(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String newStatus= "";
        int instructor_id = -1;
        boolean instructorExists = true;
        boolean statusUpdate = true;
    
        //get user input
        System.out.println("Please enter the instructor's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        instructor_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        //check to make sure instructor exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM instructor WHERE instructor_id = ?");
            psCheckDupe.setInt(1, instructor_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //instructor does not exist
                System.out.println("Instructor does not exist! Returning to menu...");
                instructorExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (instructorExists) {
                //get new name
                System.out.println("Please enter the instructor's new coaching status for street bikes: ");

                newStatus = scr.nextLine();
                //validate user input
                switch (newStatus) {
                    case "y":
                        statusUpdate =  true;
                        break;
                    case "n":
                        statusUpdate = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again with a valid option (y/n).");
                        break;
                }

                //make update
                System.out.println("EDIT INSTRUCTOR COACHING STREET BIKE STATUS TO " + statusUpdate + " FOR INSTRUCTOR WITH ID NUMBER: " + instructor_id);
                ps = conn.prepareStatement("UPDATE instructor SET coach_street_bike = ? WHERE instructor_id = ?;");
                ps.setBoolean(1, statusUpdate);
                ps.setInt(2, instructor_id);
                
                if (ps.executeUpdate() > 0) {
                    System.out.println("Updated instructor OK");
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
     * Method to edit a instructor teaching status for street bikes based on instructor id.
    */
    public void editInstructorTeachStreet(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String newStatus= "";
        int instructor_id = -1;
        boolean instructorExists = true;
        boolean statusUpdate = true;
    
        //get user input
        System.out.println("Please enter the instructor's id number: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        instructor_id = scr.nextInt();
        scr.nextLine(); // consume extra newline

        //check to make sure instructor exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM instructor WHERE instructor_id = ?");
            psCheckDupe.setInt(1, instructor_id);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //instructor does not exist
                System.out.println("Instructor does not exist! Returning to menu...");
                instructorExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (instructorExists) {
                //get new name
                System.out.println("Please enter the instructor's new teaching status for street bikes: ");

                newStatus = scr.nextLine();
                //validate user input
                switch (newStatus) {
                    case "y":
                        statusUpdate =  true;
                        break;
                    case "n":
                        statusUpdate = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again with a valid option (y/n).");
                        break;
                }

                //make update
                System.out.println("EDIT INSTRUCTOR TEACHING STREET BIKE STATUS TO " + statusUpdate + " FOR INSTRUCTOR WITH ID NUMBER: " + instructor_id);
                ps = conn.prepareStatement("UPDATE instructor SET teach_street_bike = ? WHERE instructor_id = ?;");
                ps.setBoolean(1, statusUpdate);
                ps.setInt(2, instructor_id);
                
                if (ps.executeUpdate() > 0) {
                    System.out.println("Updated instructor OK");
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
