package util;

import java.util.regex.*;

public class TemperatureConverter {

  final Pattern temperaturePattern = Pattern.compile("(\\d+)\\s?([CcFf])");

  public String getConversions(String message) {
    final Matcher matcher = temperaturePattern.matcher(message);
    final StringBuilder result = new StringBuilder();

    result.append("(");
    while (matcher.find()) {
      String strValue = matcher.group(1);
      String tmpType = matcher.group(2);
      double value = Double.parseDouble(strValue);
      boolean isCelcius = tmpType.equalsIgnoreCase("C");
      double converted = isCelcius ? 1.8 * value + 32.0 : (value - 32.0) * 0.5556;
      char originalScale = isCelcius ? 'C' : 'F';
      char convertedScale = isCelcius ? 'F' : 'C';
      result.append(
          String.format("%.2f %c -> %.2f %c, ", value, originalScale, converted, convertedScale));
    }

    if (result.length() > 2) {
      result.deleteCharAt(result.length() - 1); // TODO: Lazy way to remove trailing ' '
      result.deleteCharAt(result.length() - 1); // TODO: Lazy way to remove trailing ','
    }
    result.append(")");
    return result.toString();
  }
}
