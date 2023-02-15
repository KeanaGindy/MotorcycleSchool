package ser322;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Classrooms extends Option implements OptionProtocol {

    @Override
    public void openMenu(Connection conn, Scanner scr) {
        // TODO Auto-generated method stub

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
        System.out.println("Manage Classrooms");
        System.out.println("\t1 - Create New Classroom");
        System.out.println("\t2 - View Classroom");
        System.out.println("\t3 - Edit Classroom");
        System.out.println("\t4 - Delete Classroom");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-4)");

    }

    @Override
    public void view(Connection conn) {
        // TODO Auto-generated method stub

        String queryStmt = "SELECT * from classroom_location";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryStmt);
            viewDB(rs);
        } catch (SQLException e) {
            e.printStackTrace();
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
            System.out.println("\nDUPLICATE ENTRY:: Range with provided ID already exists. Returning to menu.\n");
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
                System.out.println("Successfully added range to DB");
            } else {
                System.out.println("Failed to add range to DB");
            }
            // try {
            //     // Ensure Statement is Closed
            //     if (ps != null) {
            //         ps.close();
            //     }
            // } catch (SQLException se2) {
            //     se2.printStackTrace();
            //     System.out.println("Not all DB resources freed!");
            // }
        }
    }

    @Override
    public void delete(Connection conn, Scanner scr) {
        // Input Store Variables
        String _pk = null;

        PreparedStatement ps = null;

        System.out.println("Enter classroom to delete: pk(classroom_id):");
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
