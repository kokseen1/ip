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
    public void showGreetingMessage() {
        System.out.println("Hello, I am isla.ui.Isla.");
        System.out.println("What can I do for you?");
    }

    /**
     * Returns the default farewell message of the chatbot.
     */
    public static String getFarewellMessage() {
        return "Bye. Hope to see you again.";
    }

    /**
     * Reads the command input from the user and returns it.
     */
    public String readNextCommand() {
        return input.nextLine();
    }

    /**
     * Returns the chat delimiter message.
     */
    public String getChatDelimiter() {
        return "____________________________";
    }

    /**
     * Prints the loading error message.
     */
    public void showLoadingError() {
        System.out.println("Could not load tasks from storage.");
    }
}
