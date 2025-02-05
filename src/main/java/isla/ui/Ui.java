package isla.ui;

import java.util.Scanner;

/**
 * Ui class to handle interactions with the user.
 */
public class Ui {
    private Scanner input;

    public Ui() {
        input = new Scanner(System.in);
    }

    /**
     * Prints the default greeting message of the chatbot.
     */
    public void showGreeting() {
        System.out.println("Hello, I am isla.ui.Isla.");
        System.out.println("What can I do for you?");
    }

    /**
     * Returns the default farewell message of the chatbot.
     */
    public static String getFarewell() {
        return "Bye. Hope to see you again.";
    }

    /**
     * Reads the command input from the user and returns it.
     */
    public String readCommand() {
        return input.nextLine();
    }

    /**
     * Returns the chat delimiter message.
     */
    public String getLine() {
        return "____________________________";
    }

    /**
     * Prints the loading error message.
     */
    public void showLoadingError() {
        System.out.println("Could not load tasks from storage.");
    }
}
