package isla.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {

    @Test
    public void serialize_validInputs_success() {
        assertEquals("D|false|test|2025-12-12", new Deadline("test", LocalDate.of(2025, 12, 12)).serialize());
    }

    @Test
    public void testStringConversion() {
        assertEquals("[D][ ] test (by: Dec 12 2025)", new Deadline("test", LocalDate.of(2025, 12, 12)).toString());
    }
}
