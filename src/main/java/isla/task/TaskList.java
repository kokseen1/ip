package isla.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import isla.IslaException;

/**
 * TaskList class to represent a list of Task objects.
 */
public class TaskList {
    private enum TaskType {
        TODO,
        DEADLINE,
        EVENT;
    }
    private final List<Task> tasks;

    /**
     * Constructs a new empty TaskList.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Constructs a new TaskList from an array of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns an enumeration of the task list as a string.
     */
    public String getEnumeration() {
        return IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                .collect(Collectors.joining("\n"));
    }

    private static TaskType getTaskType(String taskTypeString) throws IslaException {
        switch (taskTypeString) {
        case "T":
            return TaskType.TODO;
        case "D":
            return TaskType.DEADLINE;
        case "E":
            return TaskType.EVENT;
        default:
            throw new IslaException("Invalid task type: " + taskTypeString);
        }
    }

    /**
     * Serializes the tasks in the list to a string array suitable for saving to file.
     *
     * @return List of serialized tasks.
     */
    public List<String> serialize() {
        return tasks.stream()
                .map(Task::serialize)
                .toList();
    }

    private static Task makeTask(String[] taskComponents) throws IslaException {
        TaskType taskType = getTaskType(taskComponents[0]);
        boolean isDone = Boolean.parseBoolean(taskComponents[1]);
        String description = taskComponents[2];
        Task task;

        switch (taskType) {
        case TODO:
            task = new Todo(description);
            break;

        case DEADLINE:
            LocalDate by;
            String byString = taskComponents[3];
            try {
                by = LocalDate.parse(byString);
            } catch (DateTimeParseException e) {
                throw new IslaException("Invalid date format in save file.");
            }
            task = new Deadline(description, by);
            break;

        case EVENT:
            String from = taskComponents[3];
            String to = taskComponents[4];
            task = new Event(description, from, to);
            break;

        default:
            throw new IslaException("Invalid task type: " + taskType);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Deserializes a task from a serialized string back to a Task.
     *
     * @param serializedTask Serialized task string to be deserialized.
     * @return The deserialized task.
     * @throws IslaException If error is encountered when deserializing.
     */
    public static Task deserialize(String serializedTask) throws IslaException {
        try {
            String[] taskComponents = serializedTask.split("\\|");
            return makeTask(taskComponents);
        } catch (RuntimeException e) {
            throw new IslaException("Corrupted task: " + serializedTask);
        }
    }

    /**
     * Returns the task at the specified 1-based index.
     */
    public Task getTask(Integer taskId) throws IslaException {
        try {
            return tasks.get(taskId - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new IslaException("Target index out of bounds.");
        }
    }

    /**
     * Adds a new task to the task list.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes the task at the given index.
     */
    public Task deleteTask(Integer taskId) throws IslaException {
        Task task;
        try {
            task = tasks.get(taskId - 1);
            tasks.remove(taskId - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new IslaException("Target index is out of bounds.");
        }
        return task;
    }

    /**
     * Returns the size of the task list.
     */
    public Integer getSize() {
        return tasks.size();
    }

    /**
     * Returns a TaskList with tasks matching the given keyword.
     */
    public TaskList find(String keyword) {
        return new TaskList(tasks.stream()
                .filter(task -> task.description.toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList()));
    }
}
