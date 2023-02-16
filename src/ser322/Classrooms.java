package ser322;

import java.util.Locale;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Classrooms extends Option implements OptionProtocol {

    @Override
    public void openMenu(Connection conn, Scanner scr) {

        do {
            displayMenuOptions();
            userOpt = scr.next();
            System.out.println("You selected option: " + userOpt);
            // validate user input
            switch (userOpt) {
                case "1":
                    create(conn, scr);
                    break;
                case "2":
                    view(conn);
                    break;
                case "3":
                    update(conn, scr);
                    break;
                case "4":
                    delete(conn, scr);
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

    @Override
    public void displayMenuOptions() {
        // TODO Auto-generated method stub
        System.out.println("-----------------------------------------");
        System.out.println("Manage Classrooms");
        System.out.println("-----------------------------------------");
        System.out.println("\t1 - Create New Classroom");
        System.out.println("\t2 - View Classroom");
        System.out.println("\t3 - Edit Classroom");
        System.out.println("\t4 - Delete Classroom");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-4)");

    }

    public void displayClassroomOptions() {
        System.out.println("-----------------------------------------");
        System.out.println("View Classroom Menu");
        System.out.println("-----------------------------------------");
        System.out.println("\t1 - View all Classroom");
        System.out.println("\t2 - View Available Seats on Given Date");
        System.out.println("\t3 - View  Available Classrooms on Given Date");
        System.out.println("\t0 - Return to Main Menu");
        System.out.println("Please select a valid menu option (0-3)");
    }

    @Override
    public void view(Connection conn) {

        Scanner scr = new Scanner(System.in);

        do {
            displayClassroomOptions();
            userOpt = scr.next();
            System.out.println("You selected option: " + userOpt);
            // validate user input
            switch (userOpt) {
                case "1":
                    viewAllClassroom(conn);
                    break;
                case "2":
                    viewAvailableSeats(conn);
                    break;
                case "3":
                    viewAvailableClassroom(conn);
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

    public void viewAllClassroom(Connection conn) {

        System.out.println("-----------------------------------------");
        System.out.println("View All Classroom");
        System.out.println("-----------------------------------------");
        String queryStmt = "SELECT * from classroom_location";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryStmt);
            viewDB(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAvailableSeats(Connection conn) {

        System.out.println("-----------------------------------------");
        System.out.println("View Avaialble Seats in Given Day");
        System.out.println("-----------------------------------------");
        PreparedStatement preStat = null;
        java.sql.Date classDate = null;
        ResultSet rs = null;
        Scanner scr = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        System.out.println(
                "Please enter the specific date for available seats in each classroom(format: `YYYY-MM-DD`): ");
        String dob_str = "";
        dob_str = scr.nextLine();

        try {
            java.util.Date temp_date = formatter.parse(dob_str);
            classDate = new java.sql.Date(temp_date.getTime());
            preStat = conn.prepareStatement(
                    "SELECT t.classroom_id, c.course_id, c.course_name, c.course_date,  c.course_type, t.in_session,(cl.max_capacity - (SELECT COUNT(*)  FROM enrolled_in e WHERE e.course_id = t.course_id)) as availability FROM taught_in t JOIN course c ON t.course_id = c.course_id JOIN classroom_location cl ON t.classroom_id = cl.classroom_id WHERE c.course_date = ?");
            preStat.setDate(1, classDate);
            rs = preStat.executeQuery();
            viewDB(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Date was not in correct format");
        }
    }

    public void viewAvailableClassroom(Connection conn) {
        System.out.println("-----------------------------------------");
        System.out.println("View Avaialble Classroom in Given Day");
        System.out.println("-----------------------------------------");
        PreparedStatement preStat = null;
        java.sql.Date classDate = null;
        ResultSet rs = null;
        Scanner scr = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        System.out.println("Please enter the specific date for available classroom (format: `YYYY-MM-DD`): ");
        String dob_str = "";
        dob_str = scr.nextLine();

        try {
            java.util.Date temp_date = formatter.parse(dob_str);
            classDate = new java.sql.Date(temp_date.getTime());
            preStat = conn.prepareStatement(
                    "SELECT classroom_location.classroom_id, CASE WHEN sub_query.session_count = 1 THEN (SELECT CASE in_session WHEN 'AM' THEN 'PM' WHEN 'PM' THEN 'AM' ELSE 'NONE' END FROM taught_in WHERE taught_in.classroom_id = classroom_location.classroom_id AND taught_in.course_id IN (SELECT course.course_id FROM course WHERE course.course_date = ?) LIMIT 1) WHEN sub_query.session_count = 2 THEN 'NONE'ELSE 'AM & PM' END as session_available FROM classroom_location LEFT JOIN ( SELECT taught_in.classroom_id, COUNT(taught_in.in_session) as session_count FROM taught_in JOIN course ON taught_in.course_id = course.course_id AND course.course_date = ? GROUP BY taught_in.classroom_id) sub_query ON classroom_location.classroom_id = sub_query.classroom_id LIMIT 0, 500");
            preStat.setDate(1, classDate);
            preStat.setDate(2, classDate);
            rs = preStat.executeQuery();
            viewDB(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Date was not in correct format");
        }
    }

    @Override
    public void create(Connection conn, Scanner scr) {

        // Input Store Variables
        String _classroom_id = null;
        Integer _maxCapacity = null;

        // Statement & Duplicate Check Variables
        PreparedStatement ps = null;
        PreparedStatement psdc = null;
        Boolean duplicate = null;

        // Prompt for Input
        System.out.print("Enter classroom id: \n");
        _classroom_id = scr.next();
        System.out.print("Enter classroom max capacity: \n");
        _maxCapacity = scr.nextInt();

        // Check for Duplicate
        String lookupStmt = "SELECT * FROM range_location WHERE range_id = ?";
        try {
            psdc = conn.prepareStatement(lookupStmt);
            psdc.setString(1, _classroom_id);
            duplicate = checkDuplicate(psdc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return if Duplicate Found
        if (duplicate) {
            System.out.println("\nDUPLICATE ENTRY:: Classroom with provided ID already exists. Returning to menu.\n");
            return;
        }

        // Proceed with Insertion Flow
        String insertStmt = "INSERT INTO classroom_location VALUES (?, ?);";
        try {
            ps = conn.prepareStatement(insertStmt);
            ps.setString(1, _classroom_id);
            ps.setInt(2, _maxCapacity);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (updateDB(ps, conn)) {
                System.out.println("Successfully added classroom to DB");
            } else {
                System.out.println("Failed to add classroom to DB");
            }
            // try {
            // // Ensure Statement is Closed
            // if (ps != null) {
            // ps.close();
            // }
            // } catch (SQLException se2) {
            // se2.printStackTrace();
            // System.out.println("Not all DB resources freed!");
            // }
        }
    }

    @Override
    public void delete(Connection conn, Scanner scr) {
        // Input Store Variables
        String _pk = null;

        PreparedStatement ps = null;

        System.out.println("Enter classroom to delete: (classroom_id):");
        _pk = scr.next();

        String deleteStmt = "DELETE FROM classroom_location WHERE classroom_id = ?";
        try {
            ps = conn.prepareStatement(deleteStmt);
            ps.setString(1, _pk);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (updateDB(ps, conn)) {
            System.out.println("Record deleted successfully.");
        } else {
            System.out.println("No records found to delete.");
        }
    }

    public void update(Connection conn, Scanner scr) {
        // Input Store Variables
        String _pk = null;
        Integer _newCapacity = null;

        PreparedStatement ps = null;

        System.out.println("Enter ID of classroom you'd like to update:");
        _pk = scr.next();
        System.out.println("Enter new classroom max capacity: ");
        _newCapacity = scr.nextInt();

        String updateStmt = "UPDATE classroom_location SET max_capacity = ? WHERE classroom_id = ?;";
        try {
            ps = conn.prepareStatement(updateStmt);
            ps.setInt(1, _newCapacity);
            ps.setString(2, _pk);

            if (updateDB(ps, conn)) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("No records found to update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
