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
            displayClassroomMenuOptions();
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
                    //update(conn, scr);
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

    public void displayClassroomMenuOptions() {
        System.out.println("Manage Classrooms");
        System.out.println("\t1 - Create New Classroom");
        System.out.println("\t2 - View Classroom");
        System.out.println("\t3 - Edit Classroom");
        System.out.println("\t4 - Delete Classroom");
        System.out.println("\t0 - Return to Main Menu");

        System.out.println("Please select a valid menu option (0-4)");
    }

    @Override
    public void displayMenuOptions() {
        // TODO Auto-generated method stub
        
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
        // TODO Auto-generated method stub


        
    }

    @Override
    public void delete(Connection conn, Scanner scr) {
        // TODO Auto-generated method stub
        
    }
}
