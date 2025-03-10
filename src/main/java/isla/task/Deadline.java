package isla.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import isla.IslaException;

/**
 * Deadline class to represent a deadline task.
 */
public class Deadline extends Task {
    protected LocalDate by;

    /**
     * Constructs a new Deadline object with a description and `by` field.
     */
    public Deadline(String description, LocalDate by) throws IslaException {
        super(description);
        this.by = by;
    }

    @Override
    public String serialize() {
        return "D|" + super.serialize() + "|" + this.by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }
}
