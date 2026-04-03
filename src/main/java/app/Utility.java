package app;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

public class Utility 
{
    /**
     * Formats a double representing an amount of money as a string. Eg. 20 -> "20.00", 32.567 -> "32.57".
     * 
     * @param pay The amount of money
     * @return A {@code String} formatted as an amount of money
     */
    public static String formatPay(double pay)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("$");
        int wholePart = (int)pay;
        sb.append(wholePart);
        sb.append(".");
        int decimalPart = (int)Math.round(pay * 100) % 100;
        int tenth = decimalPart / 10;
        int hundredth = decimalPart % 10;
        sb.append(tenth);
        sb.append(hundredth);
        return sb.toString();
    }

    /**
     * Formats a duration in hours minutes and seconds when they aren't 0.
     * 
     * @param duration The duration to format
     * @return The duration formatted as a String
     * @throws NullPointerException If {@code duration} is {@code null}
     */
    public static String formatDuration(Duration duration)
    {
        Objects.requireNonNull(duration, "Duration cannot be null");
        
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        StringBuilder sb = new StringBuilder();

        if (days != 0)
        {
            sb.append(days);
            sb.append(" day");
            if (days != 1) sb.append("s");
        }
        if (hours != 0)
        {
            sb.append(" ");
            sb.append(hours);
            sb.append(" hour");
            if (hours != 1) sb.append("s");
        }
        if (minutes != 0)
        {
            sb.append(" ");
            sb.append(minutes);
            sb.append(" minute");
            if (minutes != 1) sb.append("s");
        }
        if (seconds != 0 || (days == 0 && hours == 0 && minutes == 0))
        {
            sb.append(" ");
            sb.append(seconds);
            sb.append(" second");
            if (seconds != 1) sb.append("s");
        }
        return sb.toString().strip();
    }

    /**
     * Determines if a String represents an integer and satisfyies the given condition.
     * <p>
     * Satisfying the condition means the function return true when applied to the integer.
     * 
     * @param str The String to check (cannot be {@code null})
     * @param condition The condition that returns true when the integer is considered valid and false otherwise (can be {@code null} to not check any condition)
     * @return {@code true} if {@code str} is an integer and (satisfies {@code condition} or condition is {@code null}), otherwise {@code false}
     * @throws NullPointerException If {@code str} is {@code null}
     */
    private static boolean isIntegerSatisfyingCondition(String str, Function<Integer, Boolean> condition)
    {
        Objects.requireNonNull(str, "Str cannot be null");

        try
        {
            int num = Integer.parseInt(str.strip());
            if (condition != null)
                return condition.apply(num);
            else
                return true;
        }
        catch (NumberFormatException n)
        {
            return false;
        }
    }

    /**
     * Determines if a String represents an integer and is within {@code min} and {@code max} inclusive on both ends.
     * 
     * @param str The String to check (cannot be {@code null})
     * @param min The minimum value that is considered valid for the number
     * @param max The maximum value that is considered valid for the number
     * @return {@code true} if {@code str} is an integer and is between {@code min} and {@code max}, otherwise {@code false}
     * @throws NullPointerException If {@code str} is {@code null}
     */
    public static boolean isIntegerInBounds(String str, int min, int max)
    {
        return isIntegerSatisfyingCondition(str, num -> num >= min && num <= max);
    }

    /**
     * Determines if a String represents an integer and is greater than or equal to {@code min}
     * 
     * @param str The String to check (cannot be {@code null})
     * @param min The minimum value that is considered valid for the number
     * @return {@code true} if {@code str} is an integer and is greater than or equal to {@code min}, otherwise {@code false}
     * @throws NullPointerException If {@code str} is {@code null}
     */
    public static boolean isIntegerAtLeast(String str, int min)
    {
        return isIntegerSatisfyingCondition(str, num -> num >= min);
    }

    /**
     * Determines if a String represents an integer and is less than or equal to {@code max}
     * 
     * @param str The String to check (cannot be {@code null})
     * @param max The maximum value that is considered valid for the number
     * @return {@code true} if {@code str} is an integer and is less than or equal to {@code max}, otherwise {@code false}
     * @throws NullPointerException If {@code str} is {@code null}
     */
    public static boolean isIntegerAtMost(String str, int max)
    {
        return isIntegerSatisfyingCondition(str, num -> num <= max);
    }

    /**
     * Determines if a String represents an integer.
     * 
     * @param str The String to check (cannot be {@code null})
     * @return {@code true} if {@code str} is an integer, otherwise {@code false}
     * @throws NullPointerException If {@code str} is {@code null}
     */
    public static boolean isInteger(String str)
    {
        return isIntegerSatisfyingCondition(str, null);
    }
}