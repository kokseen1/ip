package isla.task;

import isla.IslaException;

/**
 * Class to represent a To-do task.
 */
public class Todo extends Task {
    public Todo(String description) throws IslaException {
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
