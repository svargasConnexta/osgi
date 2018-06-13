package conversionbot;

import interfaces.ConversionPlugin;

import java.util.List;

public class TemperatureConverterPlugin implements ConversionPlugin {

  private final TemperatureConverter temperatureConverter;

  public TemperatureConverterPlugin() {
    temperatureConverter = new TemperatureConverter();
  }

  @Override
  public String convertMessage(String message) {
    final List<Temperature> temps = temperatureConverter.extractTemperatures(message);
    return temperatureConverter.convertTemperatureListAsString(temps);
  }
}
