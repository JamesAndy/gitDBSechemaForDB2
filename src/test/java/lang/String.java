package lang;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link String}.
 */
@RunWith(JUnit4.class)
public class String {
    @Test
    public void testAdd() {
        assertEquals(42, Integer.sum(19, 23));
    }
}
