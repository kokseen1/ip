package isla.ui;

/**
 * Utility class to handle interactions with the user.
 */
public class Ui {
    public static final String HELP_MESSAGE = "Available Commands:\n"
            + "list : list all tasks.\n"
            + "help : show help message.\n"
            + "bye : exit the application.\n"
            + "\n"
            + "todo [desc] : create a Todo with a description.\n"
            + "deadline [desc] /by [YYYY-MM-DD] : create a Deadline with a description and a deadline.\n"
            + "event [desc] /from [date] /to [date] : create an Event with a description and a date range.\n"
            + "\n"
            + "mark [task no.] : mark a Task as completed.\n"
            + "unmark [task no.] : unmark a Task as not completed.\n"
            + "\n"
            + "find [keyword] : search for a task with a matching description.\n";

    /**
     * Default greeting message of the chatbot.
     */
    public static final String GREETING_MESSAGE = "Hello, I am Isla.\n"
            + "What can I do for you?";

    /**
     * Default farewell message of the chatbot.
     */
    public static final String FAREWELL_MESSAGE = "Bye. Hope to see you again.";
}
