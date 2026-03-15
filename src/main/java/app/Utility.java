package app;

import java.time.Duration;

public class Utility 
{
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

    private static String formatHours(long hours)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(hours);
        sb.append(" hour");
        if (hours != 1) sb.append("s");
        return sb.toString();
    }

    private static String formatMinutes(long minutes)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(minutes);
        sb.append(" minute");
        if (minutes != 1) sb.append("s");
        return sb.toString();
    }

    private static String formatSeconds(long seconds)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(seconds);
        sb.append(" second");
        if (seconds != 1) sb.append("s");
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
        if (duration == null) throw new NullPointerException("Duration cannot be null.");

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        if (hours == 0 && minutes == 0 && seconds == 0) 
        {
            return Utility.formatHours(hours);
        }
        else if (hours == 0 && minutes == 0 && seconds != 0)
        {
            return Utility.formatSeconds(seconds);
        }
        else if (hours == 0 && minutes != 0 && seconds == 0)
        {
            return Utility.formatMinutes(minutes);
        }
        else if (hours != 0 && minutes == 0 && seconds == 0)
        {
            return Utility.formatHours(hours);
        }
        else if (hours == 0 && minutes != 0 && seconds != 0)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(Utility.formatMinutes(minutes));
            sb.append(" and ");
            sb.append(Utility.formatSeconds(seconds));
            return sb.toString();
        }
        else if (hours != 0 && minutes == 0 && seconds != 0)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(Utility.formatHours(hours));
            sb.append(" and ");
            sb.append(Utility.formatSeconds(seconds));
            return sb.toString();
        }
        else if (hours != 0 && minutes != 0 && seconds == 0)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(Utility.formatHours(hours));
            sb.append(" and ");
            sb.append(Utility.formatMinutes(minutes));
            return sb.toString();
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            sb.append(Utility.formatHours(hours));
            sb.append(", ");
            sb.append(Utility.formatMinutes(minutes));
            sb.append(", and ");
            sb.append(Utility.formatSeconds(seconds));
            return sb.toString();
        }
    }
}