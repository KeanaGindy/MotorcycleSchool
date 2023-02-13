package ser322;

import java.util.Scanner;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ranges extends Option implements OptionProtocol {


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

    public void displayMenuOptions() {
        System.out.println("Manage Ranges");
        System.out.println("\t1 - Create New Range");
        System.out.println("\t2 - View Ranges");
        System.out.println("\t3 - Edit Range");
        System.out.println("\t4 - Delete Range");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-4)");
    }

    public void view(Connection conn) {
        String queryStmt = "SELECT * from range_location";
        try {
            Statement stmt = conn.createStatement();
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

    public void update(Connection conn, Scanner scr) {
        // Input Store Variables
        Integer _pk = null;
        Integer _newCapacity = null;

        PreparedStatement ps = null;

        System.out.println("Enter ID of range you'd like to update:");
        _pk = scr.nextInt();
        System.out.println("Enter ID of range you'd like to update:");
        _newCapacity = scr.nextInt();

        String updateStmt = "UPDATE range_location SET max_capacity = ? WHERE range_id = ?;";
        try {
            ps = conn.prepareStatement(updateStmt);
            ps.setInt(1, _newCapacity);
            ps.setInt(2, _pk);
            
            if (updateDB(ps, conn)) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("No records found to update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
