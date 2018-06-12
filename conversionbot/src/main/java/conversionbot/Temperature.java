package conversionbot;

import static conversionbot.TemperatureUnit.Celcius;
import static conversionbot.TemperatureUnit.Fahrenheit;

public class Temperature {
  TemperatureUnit unit;
  double value;

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
      return toCelcius();
    }

    return toFahrenheit();
  }

  public Temperature toFahrenheit() {
    if (unit == Fahrenheit) {
      return this;
    }

    return new Temperature(Fahrenheit, 1.8 * value + 32.0);
  }

  public Temperature toCelcius() {
    if (unit == Celcius) {
      return this;
    }

    return new Temperature(Celcius, (value - 32) * 0.5556);
  }
}
