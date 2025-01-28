package isla.ui;

import java.util.Scanner;

public class Ui {
    private Scanner input;

    public Ui() {
        input = new Scanner(System.in);
    }

    public void showGreeting() {
        System.out.println("Hello, I am isla.ui.Isla.");
        System.out.println("What can I do for you?");
    }

    public void showFarewell() {
        System.out.println("Bye. Hope to see you again.");
    }

    public String readCommand() {
        return input.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________");
    }

    public void showLoadingError() {
        System.out.println("Could not load tasks from storage.");
    }
}
