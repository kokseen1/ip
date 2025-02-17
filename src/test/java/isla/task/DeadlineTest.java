package isla.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;


public class DeadlineTest {

    @Test
    public void serialize_validInputs_success() throws Exception {
        assertEquals("D|false|test|2025-12-12",
                new Deadline("test", LocalDate.of(2025, 12, 12)).serialize());
    }

    @Test
    public void testStringConversion() throws Exception {
        assertEquals("[D][ ] test (by: Dec 12 2025)",
                new Deadline("test", LocalDate.of(2025, 12, 12)).toString());
    }
}
