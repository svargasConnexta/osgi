package conversionbot;

public enum TemperatureUnit {
  Celsius("C"),
  Fahrenheit("F");

  private final String symbol;

  TemperatureUnit(String symbol) {
    this.symbol = symbol;
  }

  String getSymbol() {
    return this.symbol;
  }
}
