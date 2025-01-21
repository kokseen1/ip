import java.util.Scanner;
import java.util.ArrayList;

public class Isla {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Task> taskList = new ArrayList<>();
        String answer;

        System.out.println("Hello, I am Isla.\nWhat can I do for you?");

        while (true) {
            answer = input.nextLine();

            switch (answer) {
                case "bye":
                    System.out.println("Bye. Hope to see you again.");
                    return;

                case "list":
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskList.size(); i++) {
                        Task task = taskList.get(i);
                        System.out.println(i + 1 + ". " + task.getFormattedString());
                    }
                    break;

                default:
                    String[] splittedAnswer = answer.split(" ");
                    String command = splittedAnswer[0];
                    switch (command) {
                        case "mark": {
                            Task task = taskList.get(Integer.parseInt(splittedAnswer[1]) - 1);
                            task.markAsDone();
                            System.out.println("Nice! I've marked this task as done:");
                            System.out.println(task.getFormattedString());
                            break;
                        }

                        case "unmark": {
                            Task task = taskList.get(Integer.parseInt(splittedAnswer[1]) - 1);
                            task.markAsNotDone();
                            System.out.println("OK, I've marked this task as not done yet:");
                            System.out.println(task.getFormattedString());
                            break;
                        }

                        default:
                            taskList.add(new Task(answer));
                            System.out.println("Added: " + answer);
                            break;
                    }
            }
        }
    }
}
