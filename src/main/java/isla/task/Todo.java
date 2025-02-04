package isla.task;

/**
 * Class to represent a To-do task object.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String serialize() {
        return "T|" + super.serialize();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
