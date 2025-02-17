package isla.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void serialize_validInputs_success() throws Exception {
        assertEquals("T|false|test", new Todo("test").serialize());
    }

    @Test
    public void testStringConversion() throws Exception {
        assertEquals("[T][ ] test", new Todo("test").toString());
    }
}
