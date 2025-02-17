package isla.ui;

import isla.IslaException;
import isla.Parser;
import isla.Storage;
import isla.task.TaskList;

/**
 * Isla class to handle main execution of the chatbot.
 */
public class Isla {
    private static final String DEFAULT_FILE_PATH = "./data/tasks.txt";

    private final Storage storage;
    private TaskList tasks;

    /**
     * Constructs and initializes the chatbot.
     */
    public Isla(String filePath) {
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (IslaException e) {
            System.out.println("Could not load tasks: " + e.getMessage());
            tasks = new TaskList();
        }
    }

    public Isla() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Returns the response of the chatbot from processing the command.
     */
    public String getResponse(String command) {
        String response;
        try {
            response = Parser.executeAndGetResponse(command, tasks, storage);
        } catch (IslaException e) {
            response = e.getMessage();
        }
        return response;
    }
}
