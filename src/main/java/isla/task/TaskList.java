package isla.task;

import isla.IslaException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

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

    public void enumerate() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println(i + 1 + "." + task);
        }
    }

    public List<String> serialize() {
        return tasks.stream().map(Task::serialize).toList();
    }

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

    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + tasks.size() + " task(s) in the list.");
    }

    public Task deleteTask(Integer taskId) {
        Task task = tasks.get(taskId - 1);
        tasks.remove(taskId - 1);
        return task;
    }

    public Integer getSize() {
        return tasks.size();
    }

    public TaskList find(String keyword) {
        return new TaskList((ArrayList<Task>) taskList.stream().filter(task -> task.description.toLowerCase()
                .contains(keyword.toLowerCase())).collect(Collectors.toList()));
    }
}
