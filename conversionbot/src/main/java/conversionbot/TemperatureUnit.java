package conversionbot;

public enum TemperatureUnit {
  Celcius("C"),
  Fahrenheit("F");

  private final String symbol;

  TemperatureUnit(String symbol) {
    this.symbol = symbol;
  }

  String getSymbol() {
    return this.symbol;
  }
}
