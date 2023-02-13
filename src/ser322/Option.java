package ser322;

public class Option {
    boolean isDone = false;
    String userOpt = "-1";

    void invalidInput() {
        System.out.println("Invalid menu option. Please try again with a valid integer (0-4).");
    }

    void returnToMainMenu() {
        isDone = true;
        System.out.println("Returning to main menu..");
    }
}
