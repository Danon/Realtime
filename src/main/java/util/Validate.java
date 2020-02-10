package util;

import java.util.regex.Pattern;

public class Validate {
    public static boolean isInt(String expression) {
        return expression != null && expression.matches("[0-9]+");
    }

    public static boolean between(double value, double min, double max) {
        if (min > max) throw new RuntimeException("Bottom cap cannot be higher than the max cap.");
        return (min <= value) && (value <= max);
    }

    public static class Specific {
        private static final Pattern ptnValidUsername = Pattern.compile("[a-zA-Z0-9_]{3,64}");

        public static boolean username(String value) {
            return value != null && ptnValidUsername.matcher(value).matches();
        }

        public static boolean password(String value) {
            return value != null && value.trim().length() != 0;
        }
    }
}
