package isla.task;

import isla.IslaException;

/**
 * Event class to represent an event task.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Constructs a new Event object with a description and `from` and `to` fields.
     */
    public Event(String description, String from, String to) throws IslaException {
        super(description);
        if (from.isEmpty()) {
            throw new IslaException("From cannot be empty.");
        }
        if (to.isEmpty()) {
            throw new IslaException("To cannot be empty.");
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public String serialize() {
        return "E|" + super.serialize() + "|" + this.from + "|" + this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }
}
