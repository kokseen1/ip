package isla.task;

import isla.IslaException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private ArrayList<Task> taskList;

    public TaskList() {
        taskList = new ArrayList<>();
    }

    public TaskList(List<String> serializedList) throws IslaException {
        taskList = new ArrayList<>();
        for (String serializedTask : serializedList) {
            Task deserializedTask;

            try {
                deserializedTask = deserialize(serializedTask);
            } catch (IslaException e) {
                throw new IslaException("Error when deserializing task.");
            }

            taskList.add(deserializedTask);
        }
    }

    public void enumerate() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            System.out.println(i + 1 + "." + task);
        }
    }

    public List<String> serialize() {
        return taskList.stream().map(Task::serialize).toList();
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

        if (isDone)
            newTask.markAsDone();

        return newTask;
    }

    public Task getTask(Integer taskId) {
        return taskList.get(taskId - 1);
    }

    public void addTask(Task task) {
        taskList.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskList.size() + " task(s) in the list.");
    }

    public Task deleteTask(Integer taskId) {
        Task task = taskList.get(taskId - 1);
        taskList.remove(taskId - 1);
        return task;
    }

    public Integer size() {
        return taskList.size();
    }
}
