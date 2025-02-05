package isla.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * Enumerates and prints all the tasks in the list.
     */
    public String enumerate() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            lines.add(i + 1 + "." + task);
        }
        return String.join("\n", lines);
    }

    /**
     * Serialize the tasks in the list to a String array suitable for saving to file.
     *
     * @return List of serialized tasks.
     */
    public List<String> serialize() {
        return tasks.stream().map(Task::serialize).toList();
    }

    /**
     * Deserialize a task from a serialized String back to a Task.
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

    public Task getTask(Integer taskId) {
        return tasks.get(taskId - 1);
    }

    /**
     * Adds a new task to the task list.
     */
    public String addTask(Task task) {
        ArrayList<String> lines = new ArrayList<>();
        tasks.add(task);
        lines.add("Got it. I've added this task:");
        lines.add(String.valueOf(task));
        lines.add("Now you have " + this.getSize() + " task(s) in the list.");
        return String.join("\n", lines);
    }

    /**
     * Deletes the task at the given index.
     */
    public Task deleteTask(Integer taskId) {
        Task task = tasks.get(taskId - 1);
        tasks.remove(taskId - 1);
        return task;
    }

    public Integer getSize() {
        return tasks.size();
    }

    /**
     * Returns a TaskList with tasks matching the given keyword String.
     */
    public TaskList find(String keyword) {
        return new TaskList((ArrayList<Task>) tasks.stream().filter(task -> task.description.toLowerCase()
                .contains(keyword.toLowerCase())).collect(Collectors.toList()));
    }
}
