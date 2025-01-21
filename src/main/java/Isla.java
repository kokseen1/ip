import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class Isla {
    public static ArrayList<Task> taskList;

    public static void addTask(Task task) {
        taskList.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskList.size() + " task(s) in the list.");
    }

    public static void handleParameters(String[] answerArray) {
        String command = answerArray[0];
        switch (command) {

            case "todo": {
                String description = String.join(" ", Arrays.copyOfRange(answerArray,
                        1, answerArray.length));
                addTask(new Todo(description));
                break;
            }

            case "deadline": {
                int byIndex = Arrays.asList(answerArray).indexOf("/by");
                String description = String.join(" ", Arrays.copyOfRange(answerArray,
                        1, byIndex));
                String by = String.join(" ", Arrays.copyOfRange(answerArray,
                        byIndex + 1, answerArray.length));
                addTask(new Deadline(description, by));
                break;
            }

            case "event": {
                int fromIndex = Arrays.asList(answerArray).indexOf("/from");
                int toIndex = Arrays.asList(answerArray).indexOf("/to");
                String description = String.join(" ", Arrays.copyOfRange(answerArray,
                        1, fromIndex));
                String from = String.join(" ", Arrays.copyOfRange(answerArray,
                        fromIndex + 1, toIndex));
                String to = String.join(" ", Arrays.copyOfRange(answerArray,
                        toIndex + 1, answerArray.length));
                addTask(new Event(description, from, to));
                break;
            }

            case "mark": {
                Task task = taskList.get(Integer.parseInt(answerArray[1]) - 1);
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(task);
                break;
            }

            case "unmark": {
                Task task = taskList.get(Integer.parseInt(answerArray[1]) - 1);
                task.markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(task);
                break;
            }

            default:
                break;
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        taskList = new ArrayList<>();

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
                    handleParameters(answerArray);
                    break;
            }
        }
    }
}
