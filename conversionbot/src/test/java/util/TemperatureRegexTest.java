package util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TemperatureRegexTest {

    @Test
    public void cTof() {
        // TODO: Don't test the string building logic, only the conversion and regex logic.
        final TemperatureConverter converter = new TemperatureConverter();
        final String result = converter.getConversions("50 C in Phoenix, I'm used to 120f. What's 30c in F?");
        assertEquals("(50.00 C -> 122.00 F, 120.00 F -> 48.89 C, 30.00 C -> 86.00 F)", result);
    }
}
