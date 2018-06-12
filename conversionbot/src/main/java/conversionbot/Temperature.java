package conversionbot;

import static conversionbot.TemperatureUnit.Celsius;
import static conversionbot.TemperatureUnit.Fahrenheit;

public class Temperature {
  private final TemperatureUnit unit;
  private final double value;

  Temperature(TemperatureUnit unit, double value) {
    this.unit = unit;
    this.value = value;
  }

  public TemperatureUnit getUnit() {
    return unit;
  }

  public double getValue() {
    return value;
  }

  public Temperature toOpposite() {
    if (unit == Fahrenheit) {
      return toCelsius();
    }

    return toFahrenheit();
  }

  public Temperature toFahrenheit() {
    if (unit == Fahrenheit) {
      return this;
    }

    return new Temperature(Fahrenheit, 1.8 * value + 32.0);
  }

  public Temperature toCelsius() {
    if (unit == Celsius) {
      return this;
    }

    return new Temperature(Celsius, (value - 32) * 0.5556);
  }
}
