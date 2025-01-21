import java.util.Scanner;

public class Isla {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String[] list = new String[100];
        int listSize = 0;

        System.out.println("Hello, I am Isla.\nWhat can I do for you?");
        String answer = input.nextLine();
        while (true) {
            switch (answer) {
                case "bye":
                    System.out.println("Bye. Hope to see you again.");
                    return;

                case "list":
                    for (int i = 0; i < listSize; i++) {
                        System.out.println(i+1 + ". " + list[i]);
                    }
                    break;

                default:
                    list[listSize] = answer;
                    listSize++;
                    System.out.println("Added: " + answer);
                    break;
            }
            answer = input.nextLine();
        }
    }
}
