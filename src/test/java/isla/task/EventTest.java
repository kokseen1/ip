package isla.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void serialize_validInputs_success() {
        assertEquals("E|false|test|1234|4321", new Event("test", "1234", "4321").serialize());
    }

    @Test
    public void testStringConversion() {
        assertEquals("[E][ ] test (from: 1234 to: 4321)", new Event("test", "1234", "4321").toString());
    }
}
