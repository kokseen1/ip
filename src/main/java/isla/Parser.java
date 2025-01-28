package isla;

import isla.task.Deadline;
import isla.task.Event;
import isla.task.Task;
import isla.task.TaskList;
import isla.task.Todo;
import isla.ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Parser class to handle parsing and execution of user commands.
 */
public class Parser {
    public static int parseAndExecute(String command, TaskList tasks, Ui ui, Storage storage) throws IslaException {
        switch (command) {
        case "bye":
            ui.showFarewell();
            return 1;

        case "list":
            tasks.enumerate();
            break;

        default:
            handleParameters(command.split(" "), tasks, ui, storage);
            break;
        }

        return 0;
    }

    public static void handleParameters(String[] commandArray, TaskList tasks, Ui ui, Storage storage) throws
            IslaException {
        String action = commandArray[0];

        switch (action) {
        case "todo": {
            String description = String.join(" ", Arrays.copyOfRange(commandArray,
                    1, commandArray.length));

            if (description.isEmpty()) {
                throw new IslaException("Description cannot be empty.");
            }

            tasks.addTask(new Todo(description));
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

            tasks.addTask(new Deadline(description, by));
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

            tasks.addTask(new Event(description, from, to));
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

            System.out.println("Removed:");
            System.out.println(task);
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

            task.markAsDone();

            System.out.println("Nice! I've marked this task as done:");
            System.out.println(task);
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

            task.markAsNotDone();

            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println(task);
            break;
        }

        default:
            throw new IslaException("Unknown command.");
        }

        storage.save(tasks.serialize());
    }
}
