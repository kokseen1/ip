package isla;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import isla.task.Deadline;
import isla.task.Event;
import isla.task.Task;
import isla.task.TaskList;
import isla.task.Todo;
import isla.ui.Ui;

/**
 * Parser class to handle parsing and execution of user commands.
 */
public class Parser {

    private enum Action {
        BYE,
        LIST,
        HELP,

        TODO,
        DEADLINE,
        EVENT,

        DELETE,
        MARK,
        UNMARK,
        FIND,
    }

    /**
     * Parses a given command and executes the desired action, and returns the response.
     *
     * @param command Command string input.
     * @param tasks Current TaskList object.
     * @param ui Current Ui object.
     * @param storage Current Storage object.
     * @return The response message from executing the action.
     * @throws IslaException If error is encountered when processing the command.
     */
    public static String executeAndGetResponse(String command, TaskList tasks, Ui ui, Storage storage) throws
            IslaException {
        String[] commandArray = command.split(" ");
        String[] parameters = Arrays.copyOfRange(commandArray, 1, commandArray.length);
        Action action = getAction(commandArray[0]);

        switch (action) {
        case BYE:
            return Ui.getFarewellMessage();

        case LIST:
            return "Here are the tasks in your list:"
                    + tasks.getEnumeration();

        case HELP:
            return Ui.getHelpMessage();

        default:
            return executeParameterizedCommand(action, parameters, tasks, ui, storage);
        }
    }

    /**
     * Returns a slice of the command array in the specified range, joined by whitespace.
     *
     * @param commandArray String array of whitespace-split command.
     * @param from Starting index.
     * @param to Final index.
     * @return The specified slice of the command.
     */
    private static String getSubCommand(String[] commandArray, int from, int to) {
        return String.join(" ", Arrays.copyOfRange(commandArray, from, to));
    }

    private static Task makeTask(Action action, String[] parameters) throws IslaException {
        String description;
        Task task = null;

        switch (action) {
        case TODO:
            description = getSubCommand(parameters, 0, parameters.length);
            task = new Todo(description);
            return task;

        case DEADLINE:
            int byIndex = Arrays.asList(parameters).indexOf("/by");
            if (byIndex == -1) {
                throw new IslaException("Must specify /by.");
            }

            description = getSubCommand(parameters, 0, byIndex);
            String byString = getSubCommand(parameters, byIndex + 1, parameters.length);
            LocalDate by;

            try {
                by = LocalDate.parse(byString);
            } catch (DateTimeParseException e) {
                throw new IslaException("Could not parse date. Ensure date is in correct format: YYYY-MM-DD.");
            }

            task = new Deadline(description, by);
            return task;

        case EVENT:
            int fromIndex = Arrays.asList(parameters).indexOf("/from");
            if (fromIndex == -1) {
                throw new IslaException("Must specify /from.");
            }

            int toIndex = Arrays.asList(parameters).indexOf("/to");
            if (toIndex == -1) {
                throw new IslaException("Must specify /to.");
            }

            if (fromIndex >= toIndex) {
                throw new IslaException("/from must be specified before /to.");
            }

            description = getSubCommand(parameters, 0, fromIndex);
            String from = getSubCommand(parameters, fromIndex + 1, toIndex);
            String to = getSubCommand(parameters, toIndex + 1, parameters.length);

            task = new Event(description, from, to);
            return task;

        default:
            throw new IslaException("Unknown Task type: " + action);
        }
    }

    private static Action getAction(String actionString) throws IslaException {
        switch (actionString) {
        case "bye":
            return Action.BYE;
        case "list":
            return Action.LIST;
        case "help":
            return Action.HELP;
        case "todo":
            return Action.TODO;
        case "deadline":
            return Action.DEADLINE;
        case "event":
            return Action.EVENT;
        case "delete":
            return Action.DELETE;
        case "mark":
            return Action.MARK;
        case "unmark":
            return Action.UNMARK;
        case "find":
            return Action.FIND;
        default:
            throw new IslaException("Unknown action: " + actionString);
        }
    }

    private static String modifyTask(Action action, int idx, TaskList tasks) throws IslaException {
        Task task;

        switch (action) {
        case DELETE:
            task = tasks.deleteTask(idx);
            assert task != null;
            return "Removed: " + task;

        case MARK:
            task = tasks.getTask(idx);
            assert task != null;
            task.markAsDone();
            return "Nice! I've marked this task as done:" + task;

        case UNMARK:
            task = tasks.getTask(idx);
            assert task != null;
            task.markAsNotDone();
            return "OK, I've marked this task as not done yet:" + task;

        default:
            throw new IslaException("Unknown modify action: " + action);
        }
    }

    private static String findTask(String keyword, TaskList tasks) throws IslaException {
        if (keyword.isEmpty()) {
            throw new IslaException("Keyword cannot be empty.");
        }
        return "Search results for '" + keyword + "' :\n"
                + tasks.find(keyword).getEnumeration();
    }

    private static String addTask(Action action, String[] parameters, TaskList tasks) throws IslaException {
        Task task = makeTask(action, parameters);
        tasks.addTask(task);
        return "Got it. I've added this task:\n"
                + task
                + "\n"
                + "Now you have " + tasks.getSize() + " task(s) in the list.";
    }

    private static void saveTasks(Storage storage, TaskList tasks) throws IslaException {
        storage.save(tasks.serialize());
    }

    /**
     * Executes advanced commands with multiple parameters, and returns the response.
     *
     * @param parameters String array of whitespace-split command.
     * @param tasks Current TaskList object.
     * @param ui Current Ui object.
     * @param storage Current Storage object.
     * @return The response message from executing the action.
     * @throws IslaException If error is encountered when processing the command.
     */
    private static String executeParameterizedCommand(
            Action action, String[] parameters, TaskList tasks, Ui ui, Storage storage) throws IslaException {
        String response;

        switch (action) {
        case TODO:
        case DEADLINE:
        case EVENT: {
            response = addTask(action, parameters, tasks);
            break;
        }

        case DELETE:
        case MARK:
        case UNMARK: {
            int targetIdx;
            try {
                targetIdx = Integer.parseInt(parameters[0]);
            } catch (NumberFormatException e) {
                throw new IslaException("Target index must be a number.");
            }
            response = modifyTask(action, targetIdx, tasks);
            break;
        }

        case FIND: {
            String keyword = getSubCommand(parameters, 0, parameters.length);
            response = findTask(keyword, tasks);
            break;
        }

        default:
            throw new IslaException("Unknown action.");
        }

        saveTasks(storage, tasks);
        return response;
    }
}
