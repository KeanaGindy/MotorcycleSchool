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
        Boolean isDone = false;
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

            //display user welcome
            System.out.println("Welcome to the Motorcycle School Admin System!");

            Scanner scr;
            String userOpt = "-1";
            do {
                displayMainMenu();
                scr = new Scanner(System.in);
                userOpt = scr.nextLine();
                System.out.println("You selected option: " + userOpt);  
                //validate user input
                switch (userOpt) {
                    case "1":
                        Students students = new Students();
                        students.showStudentMenu(conn, scr);
                        break;
                    case "2":
                        Instructors instructors = new Instructors();
                        instructors.showInstructorMenu(conn, scr);
                        break;
                    case "3":
                        Course course = new Course();
                        course.openMenu(conn, scr);
                        break;
                    case "4":
                        Ranges ranges = new Ranges();
                        ranges.openMenu(conn, scr);
                        break;
                    case "5":
                        //Classrooms
                        break;
                    case "6": 
                        //Bikes
                        break;
                    case "0":
                        //exit program
                        isDone = true;
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        //invalid input
                        System.out.println("Invalid menu option. Please try again with a valid integer (0-6).");
                        break;
                }
            } while (isDone == false);
            scr.close();
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

    public static void displayMainMenu() {
        System.out.println("================================================");
        System.out.println("MAIN MENU");
        System.out.println("================================================");
        System.out.println("\t1 - Manage Students");
        System.out.println("\t2 - Manage Instructors");
        System.out.println("\t3 - Manage Courses");
        System.out.println("\t4 - Manage Ranges");
        System.out.println("\t5 - Manage Classrooms");
        System.out.println("\t6 - Manage Bikes");
        System.out.println("\t0 - Exit");
        System.out.println("Please select a valid menu option (0-6)");
    }
    
}
