package ser322;

import java.util.Scanner;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ranges extends Option implements OptionProtocol {

    boolean isView = false;
    boolean isUpdate = false;

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
                    update(conn, scr);
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

    public void displayMenuOptions() {
        System.out.println("Manage Ranges");
        System.out.println("\t1 - Create New Range");
        System.out.println("\t2 - View Ranges");
        System.out.println("\t3 - Edit Range");
        System.out.println("\t4 - Delete Range");
        System.out.println("\t0 - Return to Main Menu");
        System.out.println("Please select a valid menu option (0-4)");
    }

    public void displayViewOptions() {
        System.out.println("Ranges: View Options");
        System.out.println("\t1 - View all Ranges");
        System.out.println("\t2 - View Seats Remaining");
        System.out.println("\t3 - View Range Report");
        System.out.println("\t0 - Back to Ranges Menu");
        System.out.println("Please select a valid menu option (0-4)");
    }

    public void displayUpdateOptions() {
        System.out.println("Ranges: Update Options");
        System.out.println("\t1 - Update Max Capacity");
        System.out.println("\t2 - Update Type");
        System.out.println("\t0 - Back to Ranges Menu");
        System.out.println("Please select a valid menu option (0-2)");
    }

    public void view(Connection conn, Scanner scr) {
        isView = true;
        do {
            displayViewOptions();
            userOpt = scr.nextLine();
            System.out.println("You selected option: " + userOpt);

            switch (userOpt) {
                case "1":
                    viewOpts(conn, scr, ViewType.all);
                    break;
                case "2":
                    viewOpts(conn, scr, ViewType.available);
                    break;
                case "3":
                    viewOpts(conn, scr, ViewType.report);
                    break;
                case "0":
                    // Back to Menu
                    isView = false;
                    break;
                default:
                    invalidInput("3");
                    break;
            }
        } while (isView == true);        
    }

    public void update(Connection conn, Scanner scr) {
        isUpdate = true;
        do {
            displayUpdateOptions();
            userOpt = scr.nextLine();
            System.out.println("You selected option: " + userOpt);

            switch (userOpt) {
                case "1":
                    updateOpts(conn, scr, UpdateType.capacity);
                    // max capacity
                    break;
                case "2":
                    updateOpts(conn, scr, UpdateType.type);
                    // type
                    break;
                case "0":
                    isUpdate = false;
                    break;
                default:
                    invalidInput("2");
            }

        } while (isUpdate == true);
    }

    private void viewOpts(Connection conn, Scanner scr, ViewType vt) {
        System.out.println("Enter in a date: (YYYY-MM-DD)");
        PreparedStatement ps = vt.getPreparedStatement(conn);
        ResultSet rs = null;
        java.sql.Date _date = null;
        String _dateStr = scr.nextLine();
        _date = parseDate(_dateStr);

        try {
            switch (vt) {
                case all:
                    break;
                case available:
                    ps.setDate(1, _date);
                    break;
                case report:
                    ps.setDate(1, _date);
                    ps.setDate(2, _date);
                    break;
            }
            rs = ps.executeQuery();
        } catch(Exception e) {
            e.printStackTrace();
        }

        viewDB(rs);
    }

    public void updateOpts(Connection conn, Scanner scr, UpdateType ut) {
        // Input Store Variables
        Integer _rangeId = null;
        Integer _newCapacity = null;
        String _newType = null;
        PreparedStatement ps = null;

        System.out.println("Enter ID of the range you'd like to update:");
        _rangeId = scr.nextInt();

        try {
            ps = ut.getPreparedStatement(conn);
            ps.setInt(2, _rangeId);
            switch(ut) {
                case capacity:
                    System.out.println("Enter the new capacity:");
                    _newCapacity = scr.nextInt();
                    ps.setInt(1, _newCapacity);
                    break;
                case type:
                    System.out.println("Enter the new type:");
                    _newType = scr.next();
                    ps.setString(1, _newType);
                    break;
            }
             if (updateDB(ps, conn)) {
                System.out.println("Ranges table updated successfuly.");
             } else {
                System.out.println("Failed to update Ranges table.");
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void delete(Connection conn, Scanner scr) {
        // Input Store Variables
        Integer _pk = null;

        PreparedStatement ps = null;
        
        System.out.println("Enter range to delete: pk(range_id):");
        _pk = scr.nextInt();

        String deleteStmt = "DELETE FROM range_location WHERE range_id = ?";
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
        Integer _rangeId = null;
        String _rangeType = null;
        Integer _maxCapacity = null;

        // Statement & Duplicate Check Variables
        PreparedStatement ps = null;
        PreparedStatement psdc = null;
        Boolean duplicate = null;

        // Prompt for Input
        System.out.print("Enter range id: \n");
        _rangeId = scr.nextInt();
        System.out.print("Enter range type: \n");
        scr.nextLine();
        _rangeType = scr.nextLine();
        System.out.print("Enter max capacity: \n");
        _maxCapacity = scr.nextInt();

  
        // Check for Duplicate
        String lookupStmt = "SELECT * FROM range_location WHERE range_id = ?";
        try {
            psdc = conn.prepareStatement(lookupStmt);
            psdc.setInt(1, _rangeId);
            duplicate = checkDuplicate(psdc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return if Duplicate Found
        if ( duplicate ) {
            System.out.println("\nDUPLICATE ENTRY:: Range with provided ID already exists. Returning to menu.\n");
            return;
        }

        // Proceed with Insertion Flow
        String insertStmt = "INSERT INTO range_location VALUES (?, ?, ?);";
        try {
            ps = conn.prepareStatement(insertStmt);
            ps.setInt(1, _rangeId);
            ps.setString(2, _rangeType);
            ps.setInt(3, _maxCapacity);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (updateDB(ps, conn)) {
                System.out.println("Successfully added range to DB");
            } else {
                System.out.println("Failed to add range to DB");
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

    // MODELS

    enum ViewType {
        available("SELECT range_location.range_id, range_location.range_type, uses.in_session, (range_location.max_capacity - (SELECT COUNT(*) FROM enrolled_in JOIN course ON enrolled_in.course_id = course.course_id JOIN uses ON course.course_id = uses.course_id WHERE uses.range_id = range_location.range_id AND course.course_date = ?)) AS availability FROM range_location JOIN uses ON range_location.range_id = uses.range_id"),
        all("SELECT * from range_location"),
        report("SELECT range_location.range_id, CASE WHEN sub_query.session_count = 1 THEN (SELECT CASE in_session WHEN 'AM' THEN 'PM' WHEN 'PM' THEN 'AM' ELSE 'NONE' END FROM uses WHERE uses.range_id = range_location.range_id AND uses.course_id IN (SELECT course.course_id FROM course WHERE course.course_date = ?) LIMIT 1) WHEN sub_query.session_count = 2 THEN 'NONE' ELSE 'AM & PM' END as session_available FROM range_location LEFT JOIN (SELECT uses.range_id, COUNT(uses.in_session) as session_count FROM uses JOIN course ON uses.course_id = course.course_id AND course.course_date = ? GROUP BY uses.range_id) sub_query ON range_location.range_id = sub_query.range_id LIMIT 0, 500");

        private final String query;

        private ViewType(String query) {
            this.query = query;
        }

        public String getQuery() {
            return this.query;
        }

        public PreparedStatement getPreparedStatement(Connection conn) {
            PreparedStatement _ps = null;
            try {
                _ps = conn.prepareStatement(this.query);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return _ps;

        }
    }

    enum UpdateType {
        capacity("UPDATE range_location SET max_capacity = ? WHERE range_id = ?;"),
        type("UPDATE range_location SET range_type = ? WHERE range_id = ?;");

        private final String query;

        private UpdateType(String query) {
            this.query = query;
        }


        public String getQuery() {
            return this.query;
        }

        public PreparedStatement getPreparedStatement(Connection conn) {
            PreparedStatement _ps = null;
            try {
                _ps = conn.prepareStatement(this.query);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return _ps;
        }
    }
}
