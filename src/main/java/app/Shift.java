package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import app.enums.DisplayMode;

public class Shift
{
    private static final String DEFAULT_NAME = "Unnamed";
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");

    private static int nextId = 1;

    private String name;
    private final int id;
    private final Job job;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Duration duration;
    private double hourlyWage;

    /**
     * Creates a new shift for a job from the given start and end time with the given name.
     * 
     * @param name The name (identifier) for the shift (cannot be {@code null})
     * @param job The job the shift is for (cannot be {@code null})
     * @param start The start time of the shift (cannot be {@code null}) (cannot be after {@code end})
     * @param end The end time of the shift (cannot be {@code null}) (cannot be before {@code start})
     * @throws NullPointerException If any argument is {@code null}
     * @throws IllegalArgumentException If {@code start} is after {@code end} or the hourly wage of {@code job} is negative
     */
    public Shift(String name, Job job, LocalDateTime start, LocalDateTime end)
    {
        if (name == null || job == null || start == null || end == null) throw new NullPointerException("Arguments cannot be null");
        if (job.getCurrentHourlyWage() < 0) throw new IllegalArgumentException("The hourly wage for the job cannot be negative");

        Duration calculatedDuration = Duration.between(start, end);
        if (calculatedDuration.isNegative()) throw new IllegalArgumentException("End cannot be before start");

        this.name = name;
        this.id = Shift.nextId;
        this.job = job;
        this.start = start;
        this.end = end;
        this.duration = calculatedDuration;
        this.hourlyWage = job.getCurrentHourlyWage();
        Shift.nextId++;
    }

    /**
     * Creates a new shift for a job from the given start and end time.
     * <p>
     * The name of the shift will be Unnamed.
     * 
     * @param job The job the shift is for (cannot be {@code null})
     * @param start The start time of the shift (cannot be {@code null}) (cannot be after {@code end})
     * @param end The end time of the shift (cannot be {@code null}) (cannot be before {@code start})
     * @throws NullPointerException If any argument is {@code null}
     * @throws IllegalArgumentException If {@code start} is after {@code end} or the hourly wage of {@code job} is negative
     */
    public Shift(Job job, LocalDateTime start, LocalDateTime end)
    {
        this(Shift.DEFAULT_NAME + " - " + Shift.nextId, job, start, end);
    }

    /**
     * Creates a new shift for a job from the given start and duration with the given name.
     * 
     * @param name The name (identifier) for the shift (cannot be {@code null})
     * @param job The job the shift is for (cannot be {@code null})
     * @param start The start time of the shift (cannot be {@code null})
     * @param duration The duration of the shift (cannot be {@code null}) (cannot be negative)
     * @throws NullPointerException If any argument is {@code null}
     * @throws IllegalArgumentException If {@code duration} is negative or the hourly wage of {@code job} is negative
     */
    public Shift(String name, Job job, LocalDateTime start, Duration duration)
    {
        if (name == null || job == null || start == null || duration == null) throw new NullPointerException("Arguments cannot be null");
        if (job.getCurrentHourlyWage() < 0) throw new IllegalArgumentException("The hourly wage for the job cannot be negative");
        if (duration.isNegative()) throw new IllegalArgumentException("Duration cannot be negative");

        LocalDateTime calculatedEnd = start.plus(duration);

        this.name = name;
        this.id = Shift.nextId;
        this.job = job;
        this.start = start;
        this.end = calculatedEnd;
        this.duration = duration;
        this.hourlyWage = job.getCurrentHourlyWage();
        Shift.nextId++;
    }

    /**
     * Creates a new shift for a job from the given start and duration.
     * <p>
     * The name of the shift will be Unnamed.
     * 
     * @param job The job the shift is for (cannot be {@code null})
     * @param start The start time of the shift (cannot be {@code null}) (cannot be after {@code end})
     * @param duration The duration of the shift (cannot be {@code null}) (cannot be negative)
     * @throws NullPointerException If any argument is {@code null}
     * @throws IllegalArgumentException If {@code duration} is negative or the hourly wage of {@code job} is negative
     */
    public Shift(Job job, LocalDateTime start, Duration duration)
    {
        this(Shift.DEFAULT_NAME + " - " + Shift.nextId, job, start, duration);
    }

    /**
     * Gets the name representing the {@code Shift}.
     * 
     * @return The name representing the {@code Shift}
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the job for the {@code Shift}.
     * 
     * @return The {@code Shift}'s job
     */
    public Job getJob()
    {
        return this.job;
    }

    /**
     * Gets the total duration of a collection of {@code Shift}s.
     * 
     * @param shifts The Collection of Shifts to get the total duration (cannot be {@code null})
     * @return A {@code Duration} with the duration of the total duration of all the shifts
     * @throws NullPointerException If {@code shifts} is {@code null}
     */
    public static Duration getTotalDuration(Collection<Shift> shifts)
    {
        Objects.requireNonNull(shifts, "Shifts cannot be null");

        Duration totalDuration = Duration.ZERO;
        for (Shift shift : shifts)
        {
            totalDuration = totalDuration.plus(shift.duration);
        }
        return totalDuration;
    }

    /**
     * Gets the total payment from the given collection of {@code Shift}s
     * 
     * @param shifts The Collection of Shifts to get the total payment (cannot be {@code null})
     * @return A {@code double} representing the total amount of money received in dollars for the shifts
     * @throws NullPointerException If {@code shifts} is {@code null}
     */
    public static double getTotalPay(Collection<Shift> shifts)
    {
        Objects.requireNonNull(shifts, "Shifts cannot be null");

        double total = 0;
        for (Shift shift : shifts)
        {
            double durationInHours = (double)shift.duration.toSeconds() / 3600;
            double pay = shift.hourlyWage * durationInHours;
            total += pay;
        }
        return total;
    }

    /**
     * Gets all shifts with the given name from the given list of shifts.
     * <p>
     * Case is ignored ("nAmE1" == "naMe1")
     * 
     * @param shifts The shifts to select from
     * @param name The name to match to the shifts
     * @return A {@code List} of {@code Shift} where all the shifts have the name {@code name} case insensitive
     * @throws NullPointerException If {@code shifts} or {@code name} is {@code null}
     */
    public static List<Shift> getShiftsWithName(List<Shift> shifts, String name)
    {
        Objects.requireNonNull(shifts, "Shifts cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");

        List<Shift> shiftswithName = new ArrayList<>();
        for (Shift shift : shifts)
            if (shift.name.equalsIgnoreCase(name))
                shiftswithName.add(shift);
        return shiftswithName;
    }

    /**
     * Returns the given Collection of shifts sorted by id.
     * <p>
     * {@code shifts} is not modified.
     * 
     * @param shifts The shifts to sort (not modified) (cannot be {@code null})
     * @return A {@code List} of {@code Shift} that is sorted by id from the given Collection
     * @throws NullPointerException If {@code shifts} is {@code null}
     */
    public static List<Shift> sortShiftsById(Collection<Shift> shifts)
    {
        Objects.requireNonNull(shifts, "Shifts cannot be null");

        List<Shift> sortedShifts = new ArrayList<>(shifts);
        sortedShifts.sort(Comparator.comparing((Shift s) -> s.id));
        return sortedShifts;
    }

    /**
     * Gets the string starting after the first instance of {@code identifier} in {@code originalString} up until the next comma (excluding the comma) if possible.
     * 
     * @param originalString The string to search through
     * @param identifier The identifier to look for in {@code originalString}
     * @return The {@code String} if one exists, otherwise {@code null}
     */
    private static String getStringBetweenStringAndComma(String originalString, String identifier)
    {
        if (originalString == null || identifier == null) return null;

        int identifierIndex = originalString.indexOf(identifier);
        if (identifierIndex == -1) return null;

        int commaIndex = originalString.indexOf(",", identifierIndex + identifier.length());
        if (commaIndex != -1)
            return originalString.substring(identifierIndex + identifier.length(), commaIndex).strip();
        else
            return originalString.substring(identifierIndex + identifier.length()).strip();
    }

    /**
     * Returns a new {@code Shift} initialized to be the value represented by the specified {@code String}.
     * <p>
     * The string should be formatted as the result of the toString() method on a Shift object.
     * 
     * @param str The string to be parsed
     * @return The {@code Shift} value represented by the string argument if valid, otherwise {@code null}
     */
    public static Shift parseShift(String str)
    {
        if (str == null) return null;

        String name = getStringBetweenStringAndComma(str, "Name: ");
        if (name == null) return null;

    }

    /**
     * Displays all the fields for the {@code Shift}.
     * 
     * @return The string containing the fields of the {@code Shift} in a readable format
     */
    private String detailedDisplay()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Shift (");
        sb.append(this.id);
        sb.append(") - ");
        sb.append("Name: ");
        sb.append(this.name);
        sb.append(", Job: ");
        sb.append(this.job.toString());
        sb.append(", Start: ");
        sb.append(this.start.toString());
        sb.append(", End: ");
        sb.append(this.end.toString());
        sb.append(", Duration: ");
        sb.append(this.duration.toString());
        sb.append(", Hourly wage: ");
        sb.append(this.hourlyWage);
        return sb.toString();
    }

    /**
     * Displays the name, job, date started, and duration of the {@code Shift}
     * 
     * @return The formatted string
     */
    private String durationDisplay()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(" (");
        sb.append(this.job.getName());
        sb.append("): ");
        sb.append("Started on ");
        sb.append(this.start.format(Shift.DEFAULT_DATETIME_FORMAT));
        sb.append(" for ");
        sb.append(Utility.formatDuration(this.duration));
        return sb.toString();
    }

    /**
     * Displays the name, job, date started, and date ended for the {@code Shift}
     * 
     * @return The formatted string
     */
    private String timesDisplay()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(" (");
        sb.append(this.job.getName());
        sb.append("): ");
        if (this.start.toLocalDate().equals(this.end.toLocalDate()))
        {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
            sb.append(this.start.format(dateFormatter));
            sb.append(" from ");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
            sb.append(this.start.format(timeFormatter));
            sb.append(" to ");
            sb.append(this.end.format(timeFormatter));
        }
        else
        {
            sb.append(this.start.format(Shift.DEFAULT_DATETIME_FORMAT));
            sb.append(" to ");
            sb.append(this.end.format(Shift.DEFAULT_DATETIME_FORMAT));
        }
        return sb.toString();
    }

    /**
     * Displays the {@code Shift} in a format based on {@code displayMode}
     * <p>
     * If {@code displayMode} == {@code DisplayMode.Detailed} then all of the {@code Shift}'s fields are displayed in a readable format.
     * If {@code displayMode} == {@code DisplayMode.Duration} then the {@code Shift} is displayed in a readable format that contains the start date and duration of the {@code Shift}.
     * If {@code displayMode} == {@code DisplayMode.Times} then the {@code Shift} is displayed in a readable format that contains the start date and end date of the {@code Shift}.
     * 
     * @param displayMode The method in which the {@code Shift} will be displayed (cannot be null)
     * @return The formatted string
     * @throws NullPointerException If {@code displayMode} is {@code null}
     */
    public String display(DisplayMode displayMode)
    {
        Objects.requireNonNull(displayMode, "DisplayMode cannot be null");

        return switch (displayMode)
        {
            case Detailed -> detailedDisplay();
            case Duration -> durationDisplay();
            case Times -> timesDisplay();
            default -> "";
        };
    }

    @Override
    public String toString()
    {
        return this.display(DisplayMode.Detailed);
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null) return false;

        if (!(other instanceof Shift)) return false;

        Shift othershift = (Shift)other;
        return this.id == othershift.id;
    }
    
    @Override
    public int hashCode()
    {
        return this.id;
    }
}