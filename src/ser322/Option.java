package ser322;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

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
                int width = columnWidths[i] == 0 ? 10 : columnWidths[i];
                System.out.printf("%-" + width + "s   ", columnNames[i]);
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
            System.out.println("No record found!");
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

    public java.sql.Date scanForDate(Scanner scanner) {
        Pattern datePattern = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
        java.sql.Date date = null;

        int numTries = 0;
        while (numTries < 3) {
            System.out.println("Enter a date: (DD-MM-YYYY)");
            String input = scanner.nextLine();
            if (datePattern.matcher(input).matches()) {
                date = parseDate(input);
            } else {
                System.out.println("Invalid date format. Please enter date in DD-MM-YYYY format.");
                numTries++;
            }
        }
        return parseDate("01-01-2000");
    }
    
    public int scanForInt(Scanner scr, String prompt) {
        int maxAttempts = 3;
        int intValue = 0;
        int attempt = 0;

        do {
            attempt++;
            System.out.println(prompt);

            if (scr.hasNextInt()) {
                intValue = scr.nextInt();
                consumeNewLine(scr);
                break;
            } else {
                System.out.println("Invalid input: please enter an Integer.");
                consumeNewLine(scr);
            }

        } while (attempt < maxAttempts);

        return intValue;
    }

    public String scanForString(Scanner scr, String prompt) {
        System.out.println(prompt);
        return scr.nextLine();
    }

    public String scanForRangeType(Scanner scanner) {
        int maxAttempts = 3;
        String inputString = "";
        int attemptCount = 0;
    
        do {
            attemptCount++;
            System.out.println("Enter range type: (street/dirt)");
    
            if (scanner.hasNext()) {
                inputString = scanner.next();
                if (inputString.equals("street") || inputString.equals("dirt")) {
                    break;
                } else {
                    System.out.println("Invalid input: please enter either 'street' or 'dirt'.");
                    consumeNewLine(scanner);
                }
            } else {
                System.out.println("Invalid input: please enter either 'street' or 'dirt'.");
                consumeNewLine(scanner);
            }
    
        } while (attemptCount < maxAttempts);
    
        return inputString;
    }


}

