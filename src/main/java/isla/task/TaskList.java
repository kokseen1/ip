package isla.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import isla.IslaException;

/**
 * TaskList class to represent a list of task objects.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructs a new TaskList with an existing List of serialized tasks.
     * @throws IslaException If error is encountered when deserializing.
     */
    public TaskList(List<String> serializedList) throws IslaException {
        tasks = new ArrayList<>();
        for (String serializedTask : serializedList) {
            Task deserializedTask;

            try {
                deserializedTask = deserialize(serializedTask);
            } catch (IslaException e) {
                throw new IslaException("Error when deserializing task.");
            }

            tasks.add(deserializedTask);
        }
    }

    /**
     * Returns an enumeration of the task list as a string.
     */
    public String getEnumeration() {
        return IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Serializes the tasks in the list to a String array suitable for saving to file.
     *
     * @return List of serialized tasks.
     */
    public List<String> serialize() {
        return tasks.stream()
                .map(Task::serialize)
                .toList();
    }

    /**
     * Deserializes a task from a serialized String back to a Task.
     *
     * @param serializedTask Serialized task String to be deserialized.
     * @return The deserialized task.
     * @throws IslaException If error is encountered when deserializing.
     */
    public Task deserialize(String serializedTask) throws IslaException {
        String[] taskComponents = serializedTask.split("\\|");
        String taskType = taskComponents[0];
        boolean isDone = Boolean.parseBoolean(taskComponents[1]);
        String description = taskComponents[2];

        Task newTask;

        switch (taskType) {
        case "T":
            newTask = new Todo(description);
            break;

        case "D":
            LocalDate by;
            String byString = taskComponents[3];
            try {
                by = LocalDate.parse(byString);
            } catch (DateTimeParseException e) {
                throw new IslaException("Invalid date format.");
            }
            newTask = new Deadline(description, by);
            break;

        case "E":
            String from = taskComponents[3];
            String to = taskComponents[4];
            newTask = new Event(description, from, to);
            break;

        default:
            throw new IslaException("Invalid task type: " + taskType);
        }

        if (isDone) {
            newTask.markAsDone();
        }

        return newTask;
    }

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

    public Integer getSize() {
        return tasks.size();
    }

    /**
     * Returns a TaskList with tasks matching the given keyword String.
     */
    public TaskList find(String keyword) {
        return new TaskList((ArrayList<Task>) tasks.stream()
                .filter(task -> task.description.toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList()));
    }
}
