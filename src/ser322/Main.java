package ser322;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/*
 * Author: Team 3
 * Attribution: Based on ASU's SER322 Example Code for Module 5.
 * Assignment: Deliverable 4 for Group Project
 * Date: 02/09/2023
 * 
 * This class is the main class to start the application that allows the admin of the school to manage the 
 * school's associated data.
 */

public class Main {
    public static void main(String[] args) {
        Boolean isValidInput = false;
        Connection conn = null;

        //check for correct number of command line args
        if (args.length < 4)
		{
			System.out.println("USAGE: java ser322.Main <url> <user> <pwd> <driver>");
			System.exit(0);
		}

        //parse args and create db connection
        try {
            String _url = args[0];

            // Step 1: Load the JDBC driver
            Class.forName(args[3]);

            // Step 2: make a connection
            conn = DriverManager.getConnection(_url, args[1], args[2]);
            // if autocommit is true then a transaction will be executed
            // on each DDL or DML statement immediately, usually you want
            // to set to false to batch within a single transaction.
            conn.setAutoCommit(false);

            //welcome user and display main menu
            System.out.println("Welcome to the Motorcycle School Admin System!");
            System.out.println("================================================");
            System.out.println("MAIN MENU");
            System.out.println("\t1 - Manage Students");
            System.out.println("\t2 - Manage Instructors");
            System.out.println("\t3 - Manage Courses");
            System.out.println("\t4 - Manage Ranges");
            System.out.println("\t5 - Manage Classrooms");
            System.out.println("\t6 - Manage Bikes");
            System.out.println("\t0 - Exit");

            System.out.println("Please select a valid menu option (0-6)");
            Scanner scr;
            while(isValidInput == false) {
                scr = new Scanner(System.in);
                String userOpt = scr.nextLine();
                System.out.println("You selected option: " + userOpt);  
                //validate user input
                switch (userOpt) {
                    case "1":
                        //Students.showStudentMenu(conn);
                        isValidInput = true;
                        break;
                    case "2":
                        //Instructors
                        isValidInput = true;
                        break;
                    case "3":
                        //Courses
                        isValidInput = true;
                        break;
                    case "4":
                        //Ranges
                        isValidInput = true;
                        break;
                    case "5":
                        //Classrooms
                        isValidInput = true;
                        break;
                    case "6": 
                        //Bikes
                        isValidInput = true;
                        break;
                    case "0":
                        //exit program
                        System.out.println("Exiting...");
                        isValidInput = true;
                        System.exit(0);
                    default:
                        //invalid input
                        System.out.println("Invalid menu option. Please try again with a valid integer (0-6).");
                }
                scr.close();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (conn != null) {
                    // check if the connection is open, and if so do a rollback
                    // to avoid a transaction context sitting open on the server
                    conn.rollback();
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                System.out.println("Not all DB resources freed!");
            }
        }
    
    }
    
}
