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

    /**
     * Parse a given command and executes the desired action.
     *
     * @param command Command string input
     * @param tasks Current TaskList object
     * @param ui Current Ui object
     * @param storage Current Storage object
     * @return The response message from executing the action.
     * @throws IslaException If error is encountered when processing the command.
     */
    public static String executeAndGetResponse(String command, TaskList tasks, Ui ui, Storage storage) throws
            IslaException {
        String[] commandArray = command.split(" ");
        String action = commandArray[0];
        switch (action) {
        case "bye":
            return Ui.getFarewellMessage();

        case "list":
            return tasks.enumerate();

        case "help":
            return Ui.getHelpMessage();

        default:
            return handleParameterizedCommand(commandArray, tasks, ui, storage);
        }
    }

    /**
     * Handle advanced commands with multiple parameters.
     *
     * @param commandArray String array of whitespace-split command.
     * @param tasks Current TaskList object
     * @param ui Current Ui object
     * @param storage Current Storage object
     * @return The response message from executing the action.
     * @throws IslaException If error is encountered when processing the command.
     */
    private static String handleParameterizedCommand(String[] commandArray, TaskList tasks, Ui ui, Storage storage)
            throws IslaException {
        String action = commandArray[0];
        assert !action.isEmpty();
        String response;

        switch (action) {
        case "todo": {
            String description = String.join(" ", Arrays.copyOfRange(commandArray,
                    1, commandArray.length));

            if (description.isEmpty()) {
                throw new IslaException("Description cannot be empty.");
            }

            response = tasks.addTask(new Todo(description));
            break;
        }

        case "deadline": {
            int byIndex = Arrays.asList(commandArray).indexOf("/by");
            if (byIndex == -1) {
                throw new IslaException("Must specify /by.");
            }

            String description = String.join(" ", Arrays.copyOfRange(commandArray,
                    1, byIndex));
            if (description.isEmpty()) {
                throw new IslaException("Description cannot be empty.");
            }

            String byString = String.join(" ", Arrays.copyOfRange(commandArray,
                    byIndex + 1, commandArray.length));
            if (byString.isEmpty()) {
                throw new IslaException("Due by date cannot be empty.");
            }

            LocalDate by;
            try {
                by = LocalDate.parse(byString);
            } catch (DateTimeParseException e) {
                throw new IslaException("Could not parse date.");
            }

            response = tasks.addTask(new Deadline(description, by));
            break;
        }

        case "event": {
            int fromIndex = Arrays.asList(commandArray).indexOf("/from");
            if (fromIndex == -1) {
                throw new IslaException("Must specify /from.");
            }

            int toIndex = Arrays.asList(commandArray).indexOf("/to");
            if (toIndex == -1) {
                throw new IslaException("Must specify /to.");
            }

            String description = String.join(" ", Arrays.copyOfRange(commandArray,
                    1, fromIndex));
            if (description.isEmpty()) {
                throw new IslaException("Description cannot be empty.");
            }

            String from = String.join(" ", Arrays.copyOfRange(commandArray,
                    fromIndex + 1, toIndex));
            if (from.isEmpty()) {
                throw new IslaException("From date cannot be empty.");
            }

            String to = String.join(" ", Arrays.copyOfRange(commandArray,
                    toIndex + 1, commandArray.length));
            if (to.isEmpty()) {
                throw new IslaException("To date cannot be empty.");
            }

            response = tasks.addTask(new Event(description, from, to));
            break;
        }

        case "delete": {
            Task task;

            try {
                task = tasks.deleteTask(Integer.parseInt(commandArray[1]));
            } catch (IndexOutOfBoundsException e) {
                throw new IslaException("Target index is out of bounds.");
            } catch (NumberFormatException e) {
                throw new IslaException("Target index must be a number.");
            }

            assert task != null;
            response = "Removed: " + task;
            break;
        }

        case "mark": {
            Task task;

            try {
                task = tasks.getTask(Integer.parseInt(commandArray[1]));
            } catch (IndexOutOfBoundsException e) {
                throw new IslaException("Target index is out of bounds.");
            } catch (NumberFormatException e) {
                throw new IslaException("Target index must be a number.");
            }

            assert task != null;
            task.markAsDone();

            response = "Nice! I've marked this task as done:" + task;
            break;
        }

        case "unmark": {
            Task task;

            try {
                task = tasks.getTask(Integer.parseInt(commandArray[1]));
            } catch (IndexOutOfBoundsException e) {
                throw new IslaException("Target index is out of bounds.");
            } catch (NumberFormatException e) {
                throw new IslaException("Target index must be a number.");
            }

            assert task != null;
            task.markAsNotDone();

            response = "OK, I've marked this task as not done yet:" + task;
            break;
        }

        case "find": {
            String keyword = String.join(" ", Arrays.copyOfRange(commandArray,
                    1, commandArray.length));

            if (keyword.isEmpty()) {
                throw new IslaException("Keyword cannot be empty.");
            }

            response = tasks.find(keyword).enumerate();
            break;
        }

        default:
            throw new IslaException("Unknown command.");
        }

        storage.save(tasks.serialize());
        assert response != null;
        return response;
    }
}
