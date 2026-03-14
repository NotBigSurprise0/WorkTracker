package app;

import java.time.Duration;
import java.time.LocalDateTime;

public class Shift
{
    private static final String DEFAULT_NAME = "Unnamed";

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
     * @throws IllegalArgumentException If any argument is {@code null} or {@code start} is after {@code end}
     */
    public Shift(String name, Job job, LocalDateTime start, LocalDateTime end)
    {
        if (name == null || job == null || start == null || end == null) throw new IllegalArgumentException("Arguments cannot be null.");

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
     * @throws IllegalArgumentException If any argument is {@code null} or {@code start} is after {@code end}
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
     * @throws IllegalArgumentException If any argument is {@code null} or {@code duration} is negative
     */
    public Shift(String name, Job job, LocalDateTime start, Duration duration)
    {
        if (name == null || job == null || start == null || duration == null) throw new IllegalArgumentException("Arguments cannot be null.");
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
     * @throws IllegalArgumentException If any argument is {@code null} or {@code duration} is negative
     */
    public Shift(Job job, LocalDateTime start, Duration duration)
    {
        this(Shift.DEFAULT_NAME, job, start, duration);
    }
}