package isla.ui;

import java.util.Scanner;

/**
 * Ui class to handle interactions with the user.
 */
public class Ui {
    private static final String HELP_MESSAGE = "Available Commands:\n"
                                               + "list : list all tasks.\n"
                                               + "help : show help message.\n"
                                               + "bye : exit the application.\n"
                                               + "\n"
                                               + "todo [desc] : create a Todo with a description.\n"
                                               + "deadline [desc] /by [when] : create a Deadline with a description and a deadline.\n"
                                               + "event [desc] /from [date] /to [date] : create an Event with a description and a date range.\n"
                                               + "\n"
                                               + "mark [task no.] : mark a Task as completed.\n"
                                               + "unmark [task no.] : unmark a Task as not completed.\n"
                                               + "\n"
                                               + "find [keyword] : search for a task with a matching description.\n";
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
     * Returns the default farewell message of the chatbot.
     */
    public static String getHelpMessage() {
        return HELP_MESSAGE;
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
