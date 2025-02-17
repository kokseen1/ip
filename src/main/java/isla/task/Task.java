package isla.task;

import isla.IslaException;

/**
 * Task class to represent an abstract task.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a new Task with the given description.
     */
    public Task(String description) throws IslaException {
        if (description.isEmpty()) {
            throw new IslaException("Description cannot be empty.");
        }
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the appropriate status icon of the task.
     *
     * @return 'X' if task is done, ' ' otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the task as a serialized string.
     */
    public String serialize() {
        return this.isDone + "|" + this.description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}
