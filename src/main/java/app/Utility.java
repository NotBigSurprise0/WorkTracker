package app;

import java.time.Duration;
import java.util.Objects;

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
        if (seconds != 0)
        {
            sb.append(" ");
            sb.append(seconds);
            sb.append(" second");
            if (seconds != 1) sb.append("s");
        }
        return sb.toString().strip();
    }
}