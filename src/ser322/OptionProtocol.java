package ser322;

import java.util.Scanner;

import java.sql.Connection;

public interface OptionProtocol {
    void openMenu(Connection conn, Scanner scr);
    void displayMenuOptions();
    void view(Connection conn, Scanner scr);
    void create(Connection conn, Scanner scr);
    void delete(Connection conn, Scanner scr);
}
