package ser322;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Option {
    Boolean isDone = false;
    String userOpt = "-1";

    void invalidInput() {
        System.out.println("Invalid menu option. Please try again with a valid integer (0-4).");
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

            // Get the column names and determine the widths of the columns
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = rs.getMetaData().getColumnName(i + 1);
                columnWidths[i] = Math.max(columnNames[i].length(), rs.getMetaData().getColumnDisplaySize(i + 1));
            }

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


}
