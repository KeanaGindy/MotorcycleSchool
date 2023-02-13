package ser322;

import java.util.Scanner;

import java.sql.Connection;

public class Ranges extends Option implements OptionProtocol {


    public void openMenu(Connection conn, Scanner scr) {
        
        do {
            displayMenuOptions();
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
}
