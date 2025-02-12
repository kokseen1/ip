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
    private final Ui ui;
    private TaskList tasks;

    /**
     * Constructor to initialize the chatbot.
     */
    public Isla(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (IslaException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public Isla() {
        this(DEFAULT_FILE_PATH);
    }

    public String getResponse(String command) {
        String response;
        try {
            response = Parser.executeAndGetResponse(command, tasks, ui, storage);
        } catch (IslaException e) {
            response = e.getMessage();
        }
        return response;
    }
}
