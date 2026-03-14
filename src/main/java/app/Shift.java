package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import app.enums.DisplayMode;

public class Shift
{
    private static final String DEFAULT_NAME = "Unnamed";
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");

    private static int nextId = 1;

    private String name;
    private int id;
    private Job job;
    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;
    private double hourlyPay;

    /**
     * Creates a new shift for a job from the given start and end time with the given name.
     * 
     * @param name The name (identifier) for the shift (cannot be {@code null})
     * @param job The job the shift is for (cannot be {@code null})
     * @param start The start time of the shift (cannot be {@code null}) (cannot be after {@code end})
     * @param end The end time of the shift (cannot be {@code null}) (cannot be before {@code start})
     * @throws NullPointerException If any argument is {@code null}
     * @throws IllegalArgumentException If {@code start} is after {@code end}
     */
    public Shift(String name, Job job, LocalDateTime start, LocalDateTime end)
    {
        if (name == null || job == null || start == null || end == null) throw new NullPointerException("Arguments cannot be null.");

        Duration calculatedDuration = Duration.between(start, end);
        if (calculatedDuration.isNegative()) throw new IllegalArgumentException("End cannot be before start.");

        this.name = name;
        this.id = Shift.nextId;
        this.job = job;
        this.start = start;
        this.end = end;
        this.duration = calculatedDuration;
        this.hourlyPay = job.getCurrentHourlyPay();
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
     * @throws IllegalArgumentException If {@code start} is after {@code end}
     */
    public Shift(Job job, LocalDateTime start, LocalDateTime end)
    {
        this(Shift.DEFAULT_NAME, job, start, end);
    }

    /**
     * Creates a new shift for a job from the given start and duration with the given name.
     * 
     * @param name The name (identifier) for the shift (cannot be {@code null})
     * @param job The job the shift is for (cannot be {@code null})
     * @param start The start time of the shift (cannot be {@code null})
     * @param duration The duration of the shift (cannot be {@code null}) (cannot be negative)
     * @throws NullPointerException If any argument is {@code null}
     * @throws IllegalArgumentException If {@code duration} is negative
     */
    public Shift(String name, Job job, LocalDateTime start, Duration duration)
    {
        if (name == null || job == null || start == null || duration == null) throw new NullPointerException("Arguments cannot be null.");
        if (duration.isNegative()) throw new IllegalArgumentException("Duration cannot be negative.");

        LocalDateTime calculatedEnd = start.plus(duration);

        this.name = name;
        this.id = Shift.nextId;
        this.job = job;
        this.start = start;
        this.end = calculatedEnd;
        this.duration = duration;
        this.hourlyPay = job.getCurrentHourlyPay();
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
     * @throws IllegalArgumentException If {@code duration} is negative
     */
    public Shift(Job job, LocalDateTime start, Duration duration)
    {
        this(Shift.DEFAULT_NAME, job, start, duration);
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
        sb.append(", Hourly pay: ");
        sb.append(this.hourlyPay);
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
        if (displayMode == null) throw new NullPointerException("DisplayMode cannot be null.");

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