package conversionbot;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static conversionbot.TemperatureUnit.Celsius;
import static conversionbot.TemperatureUnit.Fahrenheit;
import static org.junit.Assert.*;

public class TemperatureConverterTest {

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
    assertEquals(Fahrenheit, actualResult.get(0).getUnit());
    assertEquals(30, actualResult.get(0).getValue(), delta);
  }

  @Test
  public void extractTemperaturesMultiple() {
    final String message = "1F 2C 33 f 444 c";
    final double[] expectedValues = new double[] {1, 2, 33, 444};
    final TemperatureUnit[] expectedUnits =
        new TemperatureUnit[] {Fahrenheit, Celsius, Fahrenheit, Celsius};
    final TemperatureConverter converter = new TemperatureConverter();
    final List<Temperature> actualResult = converter.extractTemperatures(message);
    assertEquals(expectedValues.length, expectedUnits.length);
    assertEquals(actualResult.size(), expectedUnits.length);

    for (int i = 0; i < expectedUnits.length; ++i) {
      assertEquals(expectedValues[i], actualResult.get(i).getValue(), delta);
      assertEquals(expectedUnits[i], actualResult.get(i).getUnit());
    }
  }

  @Test
  public void extractTemperaturesMultiple_HardMode_US_Locale() {
    final String message = "Phoenix is: 10.5f -31.3c 1,300c, +1,500,000 C, -1,345,385.32F";
    final double[] expectedValues = new double[] {10.5, -31.3, 1_300, 1_500_000, -1_345_385.32};
    final TemperatureConverter converter = new TemperatureConverter();
    final TemperatureUnit[] expectedUnits =
        new TemperatureUnit[] {Fahrenheit, Celsius, Celsius, Celsius, Fahrenheit};
    final List<Temperature> actualResult = converter.extractTemperatures(message);
    assertEquals(expectedValues.length, expectedUnits.length);
    assertEquals(actualResult.size(), expectedUnits.length);

    for (int i = 0; i < expectedUnits.length; ++i) {
      assertEquals(expectedValues[i], actualResult.get(i).getValue(), delta);
      assertEquals(expectedUnits[i], actualResult.get(i).getUnit());
    }
  }

  @Test
  public void extractTemperature_MalformedNumber_US_Locale() {
    final String message = "The temperature is 3,3,3.3 F, it's hot.";
    final TemperatureConverter converter = new TemperatureConverter();
    final List<Temperature> actualResult = converter.extractTemperatures(message);
    assertEquals(333.3, actualResult.get(0).getValue(), delta);
  }

  @Test
  public void extractTemperaturesMultiple_HardMode_Spain_Locale() {
    final String message = "Phoenix is: 10,5f -31,3c 1.300c +1.500.000,0 C, -1.345.385,32F";
    final double[] expectedValues = new double[] {10.5, -31.3, 1_300, 1_500_000, -1_345_385.32};
    final TemperatureConverter converter = new TemperatureConverter();
    final TemperatureUnit[] expectedUnits =
        new TemperatureUnit[] {Fahrenheit, Celsius, Celsius, Celsius, Fahrenheit};
    // Spain is a country where numbers are written as "xxx.xxx.xxx,xx" so we use that locale here.
    Locale spanish = new Locale("es", "ES");
    final List<Temperature> actualResult = converter.extractTemperatures(message, spanish);
    assertEquals(expectedValues.length, expectedUnits.length);
    assertEquals(actualResult.size(), expectedUnits.length);

    for (int i = 0; i < expectedUnits.length; ++i) {
      assertEquals(expectedValues[i], actualResult.get(i).getValue(), delta);
      assertEquals(expectedUnits[i], actualResult.get(i).getUnit());
    }
  }

  @Test
  public void convertTemperatureListSingle() {
    final List<Temperature> temp = Collections.singletonList(new Temperature(Fahrenheit, 120));
    final TemperatureConverter converter = new TemperatureConverter();
    final List<Temperature> actualResult = converter.convertTemperatureList(temp);
    assertEquals(1, actualResult.size());
    assertEquals(Celsius, actualResult.get(0).getUnit());
    assertEquals(48.88, actualResult.get(0).getValue(), delta);
  }

  @Test
  public void convertTemperatureListAsString() {
    final List<Temperature> temp =
        Arrays.asList(new Temperature(Celsius, 0), new Temperature(Fahrenheit, 32));
    final TemperatureConverter converter = new TemperatureConverter();
    final String actualResult = converter.convertTemperatureListAsString(temp);
    assertEquals("(0.00 C -> 32.00 F, 32.00 F -> 0.00 C)", actualResult);
  }
}
