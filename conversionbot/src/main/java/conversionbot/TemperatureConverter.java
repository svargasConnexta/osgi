package conversionbot;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.*;
import java.util.stream.Collectors;

public class TemperatureConverter {

  final Pattern temperaturePattern = Pattern.compile("([+-]?[\\d,.]+)\\s?([CcFf])");

  public List<Temperature> extractTemperatures(String message) {
      return extractTemperatures(message, Locale.ROOT);
  }

  public List<Temperature> extractTemperatures(String message, Locale locale) {
    final Matcher matcher = temperaturePattern.matcher(message);
    final List<Temperature> result = new ArrayList<>();

    while (matcher.find()) {
      String strValue = matcher.group(1);
      String tmpType = matcher.group(2);

      // Remove '+' as NumberFormat doesn't like it during parsing.
      if (strValue.startsWith("+")) {
        strValue = strValue.substring(1);
      }

      NumberFormat format = NumberFormat.getInstance(locale);
      Number number;
      try {
        number = format.parse(strValue);
      } catch (ParseException e) {
          throw new RuntimeException(e);
      }
      double value = number.doubleValue();
      final TemperatureUnit unit;
      if (tmpType.equalsIgnoreCase(TemperatureUnit.Celsius.getSymbol())) {
        unit = TemperatureUnit.Celsius;
      } else {
        unit = TemperatureUnit.Fahrenheit;
      }
      result.add(new Temperature(unit, value));
    }

    return result;
  }

  public List<Temperature> convertTemperatureList(List<Temperature> temperatures) {
    return temperatures.stream().map(Temperature::toOpposite).collect(Collectors.toList());
  }

  public String convertTemperatureListAsString(List<Temperature> temperatures) {
    final List<Temperature> converted = convertTemperatureList(temperatures);
    final StringBuilder output = new StringBuilder("");
    for (int i = 0; i < converted.size(); ++i) {
      final Temperature original = temperatures.get(i);
      final Temperature convert = converted.get(i);
      output.append(
          String.format(
              "%.2f %s -> %.2f %s, ",
              original.value, original.unit.getSymbol(), convert.value, convert.unit.getSymbol()));
    }

    // Remove the trailing ',\s' from the string. Surround with '()'
    if (output.length() > 0) {
      output.delete(output.length() - 2, output.length());
      output.insert(0, "(");
      output.append(")");
    }

    return output.toString();
  }
}
