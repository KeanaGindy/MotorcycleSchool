package ser322;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

public class Option {
    Boolean isDone = false;
    String userOpt = "-1";

    void invalidInput(String max) {
        System.out.println("Invalid menu option. Please try again with a valid integer (0-" + max + ").");
    }

    void returnToMainMenu() {
        isDone = true;
        System.out.println("Returning to main menu..");
    }

    // Used for Update Executions on DB
    Boolean updateDB(PreparedStatement ps, Connection conn) {
        Boolean success = null;
        try {
            success = ps.executeUpdate() != 0;
            ps.clearParameters();
            ps.close();
            conn.commit();
        } catch (Exception e) {
            // Failed to Update
            e.printStackTrace();
        }
        return success;
    }

    


    void viewDB(ResultSet rs) {
        try {
            int columnCount = rs.getMetaData().getColumnCount();
            String[] columnNames = new String[columnCount];
            int[] columnWidths = new int[columnCount];



            while(rs.next()) {
                for(int i = 0; i < columnCount; i++) {
                    columnNames[i] = rs.getMetaData().getColumnName(i + 1);
                    Integer str = rs.getString(i + 1).length();
                    if (str > columnWidths[i]) {
                        columnWidths[i] = str + 8;
                    }
                }
            }
        
            rs.beforeFirst();

            // Print the header
            for (int i = 0; i < columnCount; i++) {
                System.out.printf("%-" + columnWidths[i] + "s   ", columnNames[i]);
            }
            System.out.println();

            // Print the data
            while (rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    System.out.printf("%-" + columnWidths[i] + "s   ", rs.getString(i + 1));
                }
                System.out.println();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Used for Duplicate Checks on DB
    Boolean checkDuplicate(PreparedStatement psdc) throws SQLException {
        ResultSet rs = null;
        try {
            rs = psdc.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = 0;
        while(rs.next()) {
            i++;
        }
        return rs != null && i > 0;
    }

    java.sql.Date parseDate(String dateString) {
        java.sql.Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            java.util.Date temp_date = formatter.parse(dateString);
            date = new java.sql.Date(temp_date.getTime());  
        } catch (ParseException e) {
            System.out.println("Date was not in correct format");
            e.printStackTrace();
        }
        return date;
    }

    public void consumeNewLine(Scanner scr) {
        scr.nextLine();
    }


}
