package net.lixir.vminus.core.conditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NumericCondition implements IVisionCondition {
    protected abstract Number getTargetValue(VisionConditionArguments args);

    @Override
    public boolean test(VisionConditionArguments args, String value) {
        try {
            Pattern pattern = Pattern.compile("([<>!=]=?|=)\\s*(-?\\d+(\\.\\d+)?)");
            Matcher matcher = pattern.matcher(value.trim());

            if (!matcher.matches()) {
                return false;
            }

            String operator = matcher.group(1);
            double number = Double.parseDouble(matcher.group(2));
            double targetValue = getTargetValue(args).doubleValue();

            return switch (operator) {
                case "="  -> targetValue == number;
                case "!=" -> targetValue != number;
                case ">"  -> targetValue > number;
                case "<"  -> targetValue < number;
                case ">=" -> targetValue >= number;
                case "<=" -> targetValue <= number;
                default   -> false;
            };
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
