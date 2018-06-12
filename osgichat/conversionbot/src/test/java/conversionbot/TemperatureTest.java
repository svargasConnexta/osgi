package conversionbot;

import org.junit.Test;

import static conversionbot.TemperatureUnit.Celsius;
import static conversionbot.TemperatureUnit.Fahrenheit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TemperatureTest {

    private final double delta = 0.1;

    @Test
    public void convertSameUnitToSameUnit() {
        final Temperature tempFahrenheit = new Temperature(Fahrenheit, 0);
        assertSame(tempFahrenheit.toFahrenheit(), tempFahrenheit);
        final Temperature tempCelsius = new Temperature(Celsius, 0);
        assertSame(tempCelsius.toCelsius(), tempCelsius);
    }

    @Test
    public void convertCelsiusToFahrenheit() {
        final Temperature freezingCelsius = new Temperature(Celsius, 0);
        final Temperature freezingFahrenheit = freezingCelsius.toFahrenheit();
        assertEquals(32.0, freezingFahrenheit.getValue(), delta);
    }

    @Test
    public void convertFahrenheitToCelsius() {
        final Temperature freezingFahrenheit = new Temperature(Fahrenheit, 32);
        final Temperature freezingCelsius = freezingFahrenheit.toCelsius();
        assertEquals(0, freezingCelsius.getValue(), delta);
    }

}