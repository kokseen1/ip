package isla.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void deserialize_validSerializedTask_success() throws Exception {
        Todo todo = (Todo) new TaskList().deserialize("T|false|todoDesc");
        assertEquals("todoDesc", todo.description);
        assertFalse(todo.isDone);

        Deadline deadline = (Deadline) new TaskList().deserialize("D|true|deadlineDesc|2025-12-12");
        assertEquals("deadlineDesc", deadline.description);
        assertTrue(deadline.isDone);
        assertEquals(LocalDate.of(2025, 12, 12), deadline.by);

        Event event = (Event) new TaskList().deserialize("E|false|eventDesc|1234|4321");
        assertEquals("eventDesc", event.description);
        assertFalse(event.isDone);
        assertEquals("1234", event.from);
        assertEquals("4321", event.to);
    }

    @Test
    public void deserialize_invalidSerializedTask_exceptionThrown() {
        try {
            new TaskList().deserialize("D|false|test|2025-13-12");
            fail();
        } catch (Exception e) {
            assertEquals("Invalid date format.", e.getMessage());
        }

        try {
            new TaskList().deserialize("X|false|test");
            fail();
        } catch (Exception e) {
            assertEquals("Invalid task type: X", e.getMessage());
        }
    }
}
