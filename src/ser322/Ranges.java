package ser322;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ranges extends Option implements OptionProtocol {

    boolean isView = false;
    boolean isUpdate = false;

    // Main Display for Ranges
    public void openMenu(Connection conn, Scanner scr) {

        do {
            displayMenuOptions();
            userOpt = scr.nextLine();
            System.out.println("You selected option: " + userOpt);
            // validate user input
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

    // View Path for Ranges Section
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

    // Update Path for Ranges Section
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

    // Handle Various View Options
    private void viewOpts(Connection conn, Scanner scr, ViewType vt) {

        // Generate Prepared Statement
        PreparedStatement ps = vt.getPreparedStatement(conn);
        ResultSet rs = null;
        java.sql.Date _date = null;

        // Parse Date String
        if (vt != ViewType.all) {
            String _dateStr = scanForString(scr, "Enter in a date: YYYY-MM-DD");
            _date = parseDate(_dateStr);
        }

        // Determine which view selection to proceed.
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Flow View
        viewDB(rs);
    }

    // Update Options
    public void updateOpts(Connection conn, Scanner scr, UpdateType ut) {
        // Input Store Variables
        Integer _rangeId = null;
        Integer _newCapacity = null;
        String _newType = null;
        PreparedStatement ps = null;

        // Capture Range ID
        _rangeId = scanForInt(scr, "Enter ID of the range you'd like to update:");

        // Determine Update Path
        try {
            ps = ut.getPreparedStatement(conn);
            ps.setInt(2, _rangeId);
            switch (ut) {
                case capacity:
                    _newCapacity = scanForInt(scr, "Enter the new capacity:");
                    ps.setInt(1, _newCapacity);
                    break;
                case type:
                    _newType = scanForRangeType(scr);
                    ps.setString(1, _newType);
                    break;
            }
            // Update Database with Range Edit
            if (updateDB(ps, conn)) {
                System.out.println("Ranges table updated successfuly.");
            } else {
                System.out.println("Failed to update Ranges table.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete Range Path
    public void delete(Connection conn, Scanner scr) {
        // Input Store Variables
        Integer _rangeId = null;
        PreparedStatement ps = null;
        _rangeId = scanForInt(scr, "Enter range ID to delete:");
        String deleteStmt = "DELETE FROM range_location WHERE range_id = ?";
        try {
            ps = conn.prepareStatement(deleteStmt, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setInt(1, _rangeId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Update DB with Range Deletion
        if (updateDB(ps, conn)) {
            System.out.println("Record deleted successfully.");
        } else {
            System.out.println("No records found to delete.");
        }
    }

    // Create Path for Range
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
        _rangeId = scanForInt(scr, "Enter range ID:");
        _rangeType = scanForRangeType(scr);

        for (int atmp = 0; atmp < 3; atmp++) {
            _maxCapacity = scanForInt(scr, "Enter max capacity:");
            
            // Create Dirt Range
            if (_rangeType.toLowerCase().equals("dirt")) {
                if (_maxCapacity > 15) {
                    System.out.println("ERROR: Dirt Ranges have a Max Capacity of 15.");
                } else {
                    break;
                }

            // Create Street Range
            } else if (_rangeType.toLowerCase().equals("street")) {
                if (_maxCapacity > 30) {
                    System.out.println("ERROR: Street Ranges have a Max Capacity of 30.");
                } else {
                    break;
                }
            }
        }

        // Check for Duplicate
        String lookupStmt = "SELECT * FROM range_location WHERE range_id = ?";
        try {
            psdc = conn.prepareStatement(lookupStmt, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            psdc.setInt(1, _rangeId);
            duplicate = checkDuplicate(psdc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return if Duplicate Found
        if (duplicate) {
            System.out.println("\nDUPLICATE ENTRY:: Range with provided ID already exists. Returning to menu.\n");
            return;
        }

        // Proceed with Insertion Flow
        String insertStmt = "INSERT INTO range_location VALUES (?, ?, ?);";
        try {
            ps = conn.prepareStatement(insertStmt, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
            } catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }
    }

    // STATIC DISPLAY OPTIONS

    // Main Menu Options
    public void displayMenuOptions() {
        System.out.println("Manage Ranges");
        System.out.println("\t1 - Create New Range");
        System.out.println("\t2 - View Ranges");
        System.out.println("\t3 - Edit Range");
        System.out.println("\t4 - Delete Range");
        System.out.println("\t0 - Return to Main Menu");
        System.out.println("Please select a valid menu option (0-4)");
    }

    // View Options
    public void displayViewOptions() {
        System.out.println("Ranges: View Options");
        System.out.println("\t1 - View all Ranges");
        System.out.println("\t2 - View Seats Remaining");
        System.out.println("\t3 - View Range Report");
        System.out.println("\t0 - Back to Ranges Menu");
        System.out.println("Please select a valid menu option (0-4)");
    }

    // Update Options
    public void displayUpdateOptions() {
        System.out.println("Ranges: Update Options");
        System.out.println("\t1 - Update Max Capacity");
        System.out.println("\t2 - Update Type");
        System.out.println("\t0 - Back to Ranges Menu");
        System.out.println("Please select a valid menu option (0-2)");
    }

    // MODELS

    // View Type Model for View Path
    enum ViewType {
        available(
                "SELECT range_location.range_id, range_location.range_type, uses.in_session, (range_location.max_capacity - (SELECT COUNT(*) FROM enrolled_in JOIN course ON enrolled_in.course_id = course.course_id JOIN uses ON course.course_id = uses.course_id WHERE uses.range_id = range_location.range_id AND course.course_date = ?)) AS availability FROM range_location JOIN uses ON range_location.range_id = uses.range_id;"),
        all("SELECT * from range_location;"),
        report("SELECT range_location.range_id, CASE WHEN sub_query.session_count = 1 THEN (SELECT CASE in_session WHEN 'AM' THEN 'PM' WHEN 'PM' THEN 'AM' ELSE 'NONE' END FROM uses WHERE uses.range_id = range_location.range_id AND uses.course_id IN (SELECT course.course_id FROM course WHERE course.course_date = ?) LIMIT 1) WHEN sub_query.session_count = 2 THEN 'NONE' ELSE 'AM & PM' END as session_available FROM range_location LEFT JOIN (SELECT uses.range_id, COUNT(uses.in_session) as session_count FROM uses JOIN course ON uses.course_id = course.course_id AND course.course_date = ? GROUP BY uses.range_id) sub_query ON range_location.range_id = sub_query.range_id LIMIT 0, 500");

        private final String query;

        // View Type Constructor
        private ViewType(String query) {
            this.query = query;
        }

        // Get Query String
        public String getQuery() {
            return this.query;
        }

        // Create PreparedStatement for View Query
        public PreparedStatement getPreparedStatement(Connection conn) {
            PreparedStatement _ps = null;
            try {
                _ps = conn.prepareStatement(this.query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return _ps;

        }
    }

    // Update Type Model for Update Path
    enum UpdateType {
        capacity("UPDATE range_location SET max_capacity = ? WHERE range_id = ?;"),
        type("UPDATE range_location SET range_type = ? WHERE range_id = ?;");

        private final String query;

        // Update Type Constructor
        private UpdateType(String query) {
            this.query = query;
        }

        // Get Query String
        public String getQuery() {
            return this.query;
        }

        // Create PreapredStatement for Update Query
        public PreparedStatement getPreparedStatement(Connection conn) {
            PreparedStatement _ps = null;
            try {
                _ps = conn.prepareStatement(this.query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return _ps;
        }
    }
}
