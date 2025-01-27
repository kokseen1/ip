import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Isla {
    public static ArrayList<Task> taskList;
    public static Path savePath = Paths.get("./data/isla.txt");

    public static Task deserialize(String serializedTask) throws IslaException {
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
                String by = taskComponents[3];
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

    public static void loadList() throws IslaException {
        List<String> serializedList;

        try {
            serializedList = Files.readAllLines(savePath);
        } catch (IOException e) {
            throw new IslaException("Error when reading save file.");
        }

        for (String serializedTask : serializedList) {
            try {
                taskList.add(deserialize(serializedTask));
            } catch (IslaException e) {
                throw new IslaException("Error when deserializing save file.");
            }
        }
    }

    public static void saveList() throws IslaException {
        List<String> serializedList = taskList.stream().map(Task::serialize).toList();

        try {
            Files.createDirectories(Paths.get("./data"));
            Files.write(savePath, serializedList, UTF_8);
        } catch (IOException e) {
            throw new IslaException("Encountered error when saving.");
        }
    }

    public static void addTask(Task task) throws IslaException {
        taskList.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskList.size() + " task(s) in the list.");
    }

    public static void handleParameters(String[] answerArray) throws IslaException {
        String command = answerArray[0];
        switch (command) {

            case "todo": {
                String description = String.join(" ", Arrays.copyOfRange(answerArray,
                        1, answerArray.length));
                if (description.isEmpty()) {
                    throw new IslaException("Description cannot be empty.");
                }
                addTask(new Todo(description));
                break;
            }

            case "deadline": {
                int byIndex = Arrays.asList(answerArray).indexOf("/by");
                if (byIndex == -1) {
                    throw new IslaException("Must specify /by.");
                }
                String description = String.join(" ", Arrays.copyOfRange(answerArray,
                        1, byIndex));
                String by = String.join(" ", Arrays.copyOfRange(answerArray,
                        byIndex + 1, answerArray.length));
                if (description.isEmpty()) {
                    throw new IslaException("Description cannot be empty.");
                }
                if (by.isEmpty()) {
                    throw new IslaException("Due by date cannot be empty.");
                }
                addTask(new Deadline(description, by));
                break;
            }

            case "event": {
                int fromIndex = Arrays.asList(answerArray).indexOf("/from");
                if (fromIndex == -1) {
                    throw new IslaException("Must specify /from.");
                }
                int toIndex = Arrays.asList(answerArray).indexOf("/to");
                if (toIndex == -1) {
                    throw new IslaException("Must specify /to.");
                }
                String description = String.join(" ", Arrays.copyOfRange(answerArray,
                        1, fromIndex));
                String from = String.join(" ", Arrays.copyOfRange(answerArray,
                        fromIndex + 1, toIndex));
                String to = String.join(" ", Arrays.copyOfRange(answerArray,
                        toIndex + 1, answerArray.length));
                if (description.isEmpty()) {
                    throw new IslaException("Description cannot be empty.");
                }
                if (from.isEmpty()) {
                    throw new IslaException("From date cannot be empty.");
                }
                if (to.isEmpty()) {
                    throw new IslaException("To date cannot be empty.");
                }
                addTask(new Event(description, from, to));
                break;
            }

            case "delete": {
                int targetIndex;
                Task task;
                try {
                    targetIndex = Integer.parseInt(answerArray[1]) - 1;
                    task = taskList.get(targetIndex);
                    taskList.remove(targetIndex);
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
                    task = taskList.get(Integer.parseInt(answerArray[1]) - 1);
                } catch (NumberFormatException e) {
                    throw new IslaException("Target index must be a number.");
                } catch (IndexOutOfBoundsException e) {
                    throw new IslaException("Target index is out of bounds.");
                }
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(task);
                break;
            }

            case "unmark": {
                Task task;
                try {
                    task = taskList.get(Integer.parseInt(answerArray[1]) - 1);
                } catch (NumberFormatException e) {
                    throw new IslaException("Target index must be a number.");
                } catch (IndexOutOfBoundsException e) {
                    throw new IslaException("Target index is out of bounds.");
                }
                task.markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(task);
                break;
            }

            default:
                throw new IslaException("Unknown command.");
        }
        saveList();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        taskList = new ArrayList<>();
        try {
            loadList();
        } catch (IslaException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Hello, I am Isla.\nWhat can I do for you?");

        while (true) {
            String answer = input.nextLine();

            switch (answer) {
                case "bye":
                    System.out.println("Bye. Hope to see you again.");
                    return;

                case "list":
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskList.size(); i++) {
                        Task task = taskList.get(i);
                        System.out.println(i + 1 + "." + task);
                    }
                    break;

                default:
                    String[] answerArray = answer.split(" ");
                    try {
                        handleParameters(answerArray);
                    } catch (IslaException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
            }
        }
    }
}
