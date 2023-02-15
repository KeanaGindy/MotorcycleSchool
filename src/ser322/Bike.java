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




public class Bike {

    /**
     * Bike type enum
     */
    public enum BikeType {
        DIRT,
        STREET
    }

    /**
     * Method to show user the manage bike menu
     * @param conn
     * @param scr
     */
    public void showBikeMenu(Connection conn, Scanner scr) {
        boolean isDone = false;
        
        String userOpt = "-1";
        do {
            displayBikeMenu();
            userOpt = scr.nextLine();
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    addBike(conn, scr);
                    break;
                case "2":
                    viewBikes(conn, scr);
                    break;
                case "3":
                    editBike(conn, scr);
                    break;
                case "4":
                    removeBike(conn, scr);
                    break;
                case "5":
                    //repair bike
                case "0":
                    //exit to main menu
                    isDone = true;
                    System.out.println("Returning to main menu..");
                    break;
                default:
                    //invalid input
                    System.out.println("Invalid menu option. Please try again with a valid integer (0-5).");
                    break;
            } 
        } while (isDone == false);
    }
    /**
     * Method to display the bike menu
     */
    public static void displayBikeMenu() {
        System.out.println("-----------------------------------------");
        System.out.println("Manage Bikes");
        System.out.println("-----------------------------------------");
        System.out.println("\t1 - Add New Bike");
        System.out.println("\t2 - View Bikes");
        System.out.println("\t3 - Edit Bike");
        System.out.println("\t4 - Remove Bike");
        System.out.println("\t5 - Add New Bike Repair");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-5)");
    }
    
    /**
     * Method to add a new bike to the database
     * @param conn
     * @param scr
     */
    public void addBike(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String vin = "";
        String license_plate = "";
        boolean repair_status = false;
        String brand = "";
        BikeType bike_type = null;
        int cc = -1;
        boolean bikeExists = false;

        //get user input
        System.out.println("Please enter the bike's vin number: ");
        vin = scr.next();
        scr.nextLine(); // consume extra newline

        System.out.println("Please enter the bike's license plate: ");
        license_plate = scr.nextLine();

        Scanner scanner = new Scanner(System.in);
        int selection;

        //add repair status of bike
        do {
            System.out.println("Please select the repair status of the bike:");
            System.out.println("1. Yes, the bike is in repair");
            System.out.println("2. No, the bike is NOT in repair");
            selection = scanner.nextInt();
            if (selection == 1) {
                System.out.println("You have selected the bike is in repair.");
                repair_status = true;
            } else if (selection == 2) {
                System.out.println("You have selected the bike is NOT in repair.");
                repair_status = false;
            } else {
                System.out.println("Invalid selection. Please select a valid option.");
            }
        } while (selection != 1 && selection != 2);

        //add brand of the bike
        System.out.println("Please enter the brand of the bike: ");
        brand = scr.nextLine();

        //add type of bike
        do {
            System.out.println("Please select the type of the bike: ");
            System.out.println("1. Street");
            System.out.println("2. Dirt");
            selection = scanner.nextInt();
            if (selection == 1) {
                System.out.println("You have selected a street bike.");
                bike_type = BikeType.STREET;
            } else if (selection == 2) {
                System.out.println("You have selected a dirt bike.");
                bike_type = BikeType.DIRT;
            } else {
                System.out.println("Invalid selection. Please select a valid option.");
            }
        } while (selection != 1 && selection != 2);

        System.out.println("Please enter the bike's cc: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        cc = scr.nextInt();
        scr.nextLine(); //

        //check to make sure bike doesn't already exist
        //if doesn't exist write to db
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM bike WHERE vin = ?");
            psCheckDupe.setString(1, vin);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs != null && i > 0) {
                //bike exists
                System.out.println("Bike already exists! Returning to menu...");
                bikeExists = true;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (!bikeExists) {
                ps = conn.prepareStatement("INSERT INTO bike VALUES (?, ?, ?, ?, ?, ?);");
                ps.setString(1, vin);
                ps.setString(2, license_plate);
                ps.setBoolean(3, repair_status);
                ps.setString(4, brand);
                ps.setString(5, bike_type.name());
                ps.setInt(6, cc);
                if (ps.executeUpdate() > 0) {
                    System.out.println("Inserted bike OK");
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

    /**
     * Method to delete a bike from the database by vin
     * @param conn
     * @param scr
     */
    public void removeBike(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String vin = "";
        boolean bikeExists = true;

        //get user input
        System.out.println("Please enter the bike's vin: ");
        vin = scr.next();
        scr.nextLine(); // consume extra newline

        //check to make sure student exists
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM bike WHERE vin = ?");
            psCheckDupe.setString(1, vin);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs == null || i == 0) {
                //student does not exist
                System.out.println("Bike does not exist! Returning to menu...");
                bikeExists = false;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (bikeExists) {
                ps = conn.prepareStatement("DELETE FROM bike WHERE vin = ?;");
                ps.setString(1, vin);
                if (ps.executeUpdate() > 0) {
                    System.out.println("Removed bike OK");
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

    /**
     * Method to ask user which bike view option they would like
     * @param conn
     * @param scr
     */
    public void viewBikes(Connection conn, Scanner scr) {
        boolean isComplete = false;
        String userOpt = "-1";
        do {
            //ask user if want to view all students of student report
            System.out.println("-----------------------------------------");
            System.out.println("View Bike Menu");
            System.out.println("-----------------------------------------");
            System.out.println("\t1 - View all Street Bikes");
            System.out.println("\t2 - View all Dirt Bikes");
            System.out.println("\t3 - View all available Bikes on a date");
            System.out.println("\t4 - View all Bikes in Repair");
            System.out.println("\t5 - View the History of a Bike");
            System.out.println("\t0 - Return to Bike Menu");
            System.out.println("Please select a valid menu option (0-5)");

            userOpt = scr.nextLine();
            
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    viewStreetBikes(conn, scr);
                    break;
                case "2":
                    viewDirtBikes(conn, scr);
                    break;
                case "3":
                    viewBikeAvailability(conn, scr);
                    break;
                case "4":
                // all bikes in repair
                    break;
                case "5":
                    // bike's history
                    break;
                case "0":
                    //exit to main menu
                    isComplete = true;
                    System.out.println("Returning to bike menu..");
                    break;
                default:
                    //invalid input
                    System.out.println("Invalid menu option. Please try again with a valid option (0-2).");
                    break;
            } 
        } while(isComplete == false);
    }

    /**
     * Method to view all street bikes
     * @param conn
     * @param scr
     */
    public void viewStreetBikes(Connection conn, Scanner scr) {
        Statement stmt = null;
        ResultSet rs = null;
        System.out.println("Displaying all street type bikes: ");
    
        //check to make sure student exists
	    try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM bike WHERE bike_type = 'street';");
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s", "vin", "license_plate", "repair_status", "brand", "bike_type", "cc");
            System.out.println();
            // Display the results
			while (rs.next()) {
                System.out.printf("%-20s",  rs.getString("vin"));
                System.out.printf("%-20s",  rs.getString("license_plate"));
                System.out.printf("%-20s",  rs.getBoolean("repair_status"));
                System.out.printf("%-20s",  rs.getString("brand"));
                System.out.printf("%-20s",  rs.getString("bike_type"));
                System.out.printf("%-20s",  rs.getInt("cc"));
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

    /**
     * Method to view all dirt bikes
     * @param conn
     * @param scr
     */
    public void viewDirtBikes(Connection conn, Scanner scr) {
        Statement stmt = null;
        ResultSet rs = null;
        System.out.println("Displaying all street type bikes: ");
    
        //check to make sure student exists
	    try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM bike WHERE bike_type = 'dirt';");
            System.out.printf("%-20s %-20s %-20s %-20s %-20s", "vin", "license_plate", "repair_status", "brand", "bike_type", "cc");
            System.out.println();
            // Display the results
			while (rs.next()) {
                System.out.printf("%-20s",  rs.getString("vin"));
                System.out.printf("%-20s",  rs.getString("license_plate"));
                System.out.printf("%-20s",  rs.getBoolean("repair_status"));
                System.out.printf("%-20s",  rs.getString("brand"));
                System.out.printf("%-20s",  rs.getString("bike_type"));
                System.out.printf("%-20s",  rs.getInt("cc"));
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

    public void viewBikeAvailability(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        ResultSet rs = null;
    
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        System.out.println("Please enter the day you want to see what bikes are available: (format: `YYYY-MM-DD`): ");
        String date_str = "";
        date_str = scr.nextLine();
        try {
            java.util.Date temp_date = formatter.parse(date_str);
            java.sql.Date date = new java.sql.Date(temp_date.getTime());
            ps = conn.prepareStatement("SELECT vin, bike_type FROM bike WHERE vin NOT IN (SELECT vin FROM assigned_to WHERE course_id IN (SELECT course_id FROM course WHERE course.course_date = ?));");
            ps.setDate(1, date);
            rs = ps.executeQuery();
            System.out.println("\n\nHere are a list of bikes available on " + date + ": \n------------------------");
            System.out.printf("%-20s %-20s%n", "vin", "bike_type");
            while (rs.next()) {
                System.out.printf("%-20s %-20s%n", rs.getString("vin"), rs.getString("bike_type"));
            }
            conn.commit();
        } catch (ParseException e) {
            System.out.println("Date was not in correct format");
            e.printStackTrace();
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

    /**
     * Method to ask user which edit option they would like
     * @param conn
     * @param scr
     */
    public void editBike(Connection conn, Scanner scr) {
        boolean isComplete = false;
        String userOpt = "-1";
        do {
            System.out.println("-----------------------------------------");
            System.out.println("Edit Bike Menu");
            System.out.println("-----------------------------------------");
            System.out.println("\t1 - Edit Bike's License Plate Number");
            System.out.println("\t2 - Edit Bike's Repair Status");
            System.out.println("\t0 - Return to Bike Menu");
            System.out.println("Please select a valid menu option (0-2)");

            userOpt = scr.nextLine();
            
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    editBikeLicensePlate(conn, scr);
                    break;
                case "2":
                    editBikeRepairStatus(conn, scr);
                    break;
                case "0":
                    //exit to main menu
                    isComplete = true;
                    System.out.println("Returning to bike menu..");
                    break;
                default:
                    //invalid input
                    System.out.println("Invalid menu option. Please try again with a valid option (0-2).");
                    break;
            } 
        } while(isComplete == false);
    }

    /**
     * Method to edit bike's license plate number
     * @param conn
     * @param scr
     */
    private void editBikeLicensePlate(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String vin = "";
        String license_plate = "";
        boolean bikeExists = true;

        //get user input
        System.out.println("Please enter the vin of the bike you want to edit: ");
        vin = scr.next();
        scr.nextLine(); // consume extra newline

        System.out.println("Please enter the bike's new license plate: ");
        license_plate = scr.nextLine();

        try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM bike WHERE vin = ?");
            psCheckDupe.setString(1, vin);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs != null && i > 0) {
                //bike exists
                System.out.println("Bike already exists! Returning to menu...");
                bikeExists = true;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }if (bikeExists) {
                ps = conn.prepareStatement("UPDATE bike SET license_plate = ? WHERE vin = ?;");
                ps.setString(1, license_plate);
                ps.setString(2, vin);
                if (ps.executeUpdate() > 0) {
                    System.out.println("Edited bike's license plate number OK");
                }
                ps.clearParameters();
                ps.close();

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

    /**
     * Method to edit bike's current repair status
     * @param conn
     * @param scr
     */
    private void editBikeRepairStatus(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String vin = "";
        boolean bikeExists = false;
        boolean repair_status = false;

        Scanner scanner = new Scanner(System.in);
        int selection;

        //get user input
        System.out.println("Please enter the vin of the bike you want to edit: ");
        vin = scr.next();
        scr.nextLine(); // co

        //get user input
        do {
            System.out.println("Please select the new repair status of the bike:");
            System.out.println("1. Yes, the bike is in repair");
            System.out.println("2. No, the bike is NOT in repair");
            selection = scanner.nextInt();
            if (selection == 1) {
                System.out.println("You have selected the bike is in repair.");
                repair_status = true;
            } else if (selection == 2) {
                System.out.println("You have selected the bike is NOT in repair.");
                repair_status = false;
            } else {
                System.out.println("Invalid selection. Please select a valid option.");
            }
        } while (selection != 1 && selection != 2);

        try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM bike WHERE vin = ?");
            psCheckDupe.setString(1, vin);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs != null && i > 0) {
                //bike exists
                System.out.println("Bike already exists! Returning to menu...");
                bikeExists = true;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }if (bikeExists) {
                ps = conn.prepareStatement("UPDATE bike SET repair_status = ? WHERE vin = ?;");
                ps.setBoolean(1, repair_status);
                ps.setString(2, vin);
                if (ps.executeUpdate() > 0) {
                    System.out.println("Edited bike's repair status OK");
                }
                ps.clearParameters();
                ps.close();

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

    /**
     * Method to add a new bike repair entry
     * @param conn
     * @param scr
     */
    public void addBikeRepair(Connection conn, Scanner scr) {
        PreparedStatement ps = null;
        PreparedStatement psCheckDupe = null;
        ResultSet rs = null;
        String vin = "";
        java.sql.Date problem_date = null;
        int repair_cost = 0;
        java.sql.Date repair_date = null;
        String problem_description = "";
        boolean bikeExists = false;

        //get user input
        System.out.println("Please enter the bike's vin number in repair: ");
        vin = scr.next();
        scr.nextLine(); // consume extra newline

        //add error message for incorrect date format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        System.out.println("Please enter the date when the bike showed initial problems (format: `YYYY-MM-DD`): ");
        String problem_date_str = "";
        problem_date_str = scr.nextLine();
        try {
            java.util.Date temp_date = formatter.parse(problem_date_str);
            problem_date = new java.sql.Date(temp_date.getTime());  
        } catch (ParseException e) {
            System.out.println("Date was not in correct format");
            e.printStackTrace();
        }

        //add repair cost of bike
        System.out.println("Please enter the repair cost of the bike in repair: ");
        while (!scr.hasNextInt()) {
            System.out.println("Error: That was not a number. Please enter an integer!");
            scr.next(); 
        }
        repair_cost = scr.nextInt();
        scr.nextLine(); //

        //add error message for incorrect date format
        System.out.println("Please enter the completed repair date of the bike: (format: `YYYY-MM-DD`): ");
        String repair_date_str = "";
        repair_date_str = scr.nextLine();
        try {
            java.util.Date temp_date = formatter.parse(repair_date_str);
            repair_date = new java.sql.Date(temp_date.getTime());  
        } catch (ParseException e) {
            System.out.println("Date was not in correct format");
            e.printStackTrace();
        }

        System.out.println("Please enter bike's problem description in repair: ");
        problem_description = scr.next();
        scr.nextLine(); // consume extra newline

        //check to make sure bike doesn't already exist
        //if doesn't exist write to db
	    try {
            psCheckDupe = conn.prepareStatement("SELECT * FROM bike WHERE vin = ?");
            psCheckDupe.setString(1, vin);
            rs = psCheckDupe.executeQuery();
            //get size of result set
            int i = 0;
            while(rs.next()) {
                i++;
            }           
            if (rs != null && i > 0) {
                //bike exists
                System.out.println("Bike already exists! Returning to menu...");
                bikeExists = true;
                psCheckDupe.clearParameters();
                psCheckDupe.close();
            }
            if (!bikeExists) {
                ps = conn.prepareStatement("INSERT INTO repair_bike VALUES(?, ?, ?, ?, ?);");
                ps.setString(1, vin);
                ps.setDate(2, problem_date);
                ps.setInt(3, repair_cost);
                ps.setDate(4, repair_date);
                ps.setString(5, problem_description);

                if (ps.executeUpdate() > 0) {
                    System.out.println("Inserted bike repair entry OK");
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
