import java.util.Scanner;

public class Isla {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Hello, I am Isla.\nWhat can I do for you?");
        String answer = input.nextLine();
        while (!answer.equals("bye")) {
            System.out.println(answer);
            answer = input.nextLine();
        }
        System.out.println("Bye. Hope to see you again.");
    }
}
