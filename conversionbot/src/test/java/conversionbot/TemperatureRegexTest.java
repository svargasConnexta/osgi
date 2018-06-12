package conversionbot;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static conversionbot.TemperatureUnit.Celcius;
import static conversionbot.TemperatureUnit.Fahrenheit;
import static org.junit.Assert.*;

public class TemperatureRegexTest {

  private final double delta = 0.1;

  @Test
  public void extractTemperaturesEmptyString() {
    final String message = "";
    final TemperatureConverter converter = new TemperatureConverter();
    final List<Temperature> actualResult = converter.extractTemperatures(message);
    assertEquals(0, actualResult.size());
  }

  @Test
  public void extractTemperaturesSingle() {
    final String message = "30F";
    final TemperatureConverter converter = new TemperatureConverter();
    final List<Temperature> actualResult = converter.extractTemperatures(message);
    assertEquals(1, actualResult.size());
    assertEquals(Fahrenheit, actualResult.get(0).unit);
    assertEquals(30, actualResult.get(0).value, delta);
  }

  @Test
  public void extractTemperaturesMultiple() {
    final String message = "1F 2C 33 f 444 c";
    final double[] expectedValues = new double[] {1, 2, 33, 444};
    final TemperatureUnit[] expectedUnits =
        new TemperatureUnit[] {Fahrenheit, Celcius, Fahrenheit, Celcius};
    final TemperatureConverter converter = new TemperatureConverter();
    final List<Temperature> actualResult = converter.extractTemperatures(message);
    assertEquals(expectedValues.length, expectedUnits.length);
    assertEquals(actualResult.size(), expectedUnits.length);

    for (int i = 0; i < expectedUnits.length; ++i) {
      assertEquals(actualResult.get(i).value, expectedValues[i], delta);
      assertEquals(actualResult.get(i).unit, expectedUnits[i]);
    }
  }

  @Test
  public void convertTemperatureListSingle() {
    final List<Temperature> temp = Collections.singletonList(new Temperature(Fahrenheit, 120));
    final TemperatureConverter converter = new TemperatureConverter();
    final List<Temperature> actualResult = converter.convertTemperatureList(temp);
    assertEquals(1, actualResult.size());
    assertEquals(Celcius, actualResult.get(0).unit);
    assertEquals(48.88, actualResult.get(0).value, delta);
  }

  @Test
  public void convertTemperatureListAsString() {
    final List<Temperature> temp =
        Arrays.asList(new Temperature(Celcius, 0), new Temperature(Fahrenheit, 32));
    final TemperatureConverter converter = new TemperatureConverter();
    final String actualResult = converter.convertTemperatureListAsString(temp);
    assertEquals("(0.00 C -> 32.00 F, 32.00 F -> 0.00 C)", actualResult);
  }
}
