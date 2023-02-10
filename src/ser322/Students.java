package ser322;

import java.sql.Connection;
import java.util.Scanner;

public class Students {
    
    public static void showStudentMenu(Connection conn, Scanner scr) {
        boolean isDone = false;

        String userOpt = "-1";
        do {
            displayStudentMenu();
            userOpt = scr.nextLine();
            System.out.println("You selected option: " + userOpt);  
            //validate user input
            switch (userOpt) {
                case "1":
                    //create
                    break;
                case "2":
                    //view
                    break;
                case "3":
                    //edit
                    break;
                case "4":
                    //delete
                    break;
                case "0":
                    //exit to main menu
                    isDone = true;
                    System.out.println("Returning to main menu..");
                    break;
                default:
                    //invalid input
                    System.out.println("Invalid menu option. Please try again with a valid integer (0-4).");
                    break;
            } 
        } while (isDone == false);
    }

    public static void displayStudentMenu() {
        System.out.println("Manage Students");
        System.out.println("\t1 - Create New Student");
        System.out.println("\t2 - View Students");
        System.out.println("\t3 - Edit Student");
        System.out.println("\t4 - Delete Student");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-4)");
    }
    
}
