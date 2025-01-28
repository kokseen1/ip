package isla.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {

    @Test
    public void serialize_validInputs_success() {
        assertEquals("T|false|test", new Todo("test").serialize());
    }

    @Test
    public void testStringConversion() {
        assertEquals("[T][ ] test", new Todo("test").toString());
    }
}
