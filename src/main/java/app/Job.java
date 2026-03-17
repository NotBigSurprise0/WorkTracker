package app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Job
{
    private static final String UNKNOWN_WAGE_STRING = "Unknwon wage";

    private static int nextId = 1;

    private String name;
    private double hourlyWage;
    private final int id;

    /**
     * Creates a job with the given name and an unspecified hourly wage.
     * 
     * @param name The name of the job (cannot be null)
     * @throws IllegalArgumentException If the name is null
     */
    public Job(String name)
    {
        Objects.requireNonNull(name, "Cannot create a job with a null name");

        this.name = name.strip();
        this.hourlyWage = -1;
        this.id = Job.nextId++;
    }

    /**
     * Creates a job with the given name and hourly wage.
     * 
     * @param name The name of the job (cannot be null)
     * @param hourlyWage The hourly wage (cannot be negative)
     * @throws IllegalArgumentException If the name is null or hourly wage is negative
     */
    public Job(String name, double hourlyWage)
    {
        Objects.requireNonNull(name, "Cannot create a job with a null name");
        if (hourlyWage < 0) throw new IllegalArgumentException("Cannot have an hourly wage that is negative.");

        this.name = name.strip();
        this.hourlyWage = hourlyWage;
        this.id = Job.nextId++;
    }

    /**
     * Gets the name of the job.
     * 
     * @return The name of the job
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the hourly wage for the job.
     * 
     * @return The hourly wage
     */
    public double getCurrentHourlyWage()
    {
        return this.hourlyWage;
    }

    /**
     * Updates the name of the job if {@code newName} is not {@code null}.
     * 
     * @param newName The new name of the job
     * @return {@code true} if the name was updated successfully ({@code newName} was not {@code null}), otherwise {@code false}
     */
    public boolean updateName(String newName)
    {
        if (newName == null) return false;

        this.name = newName.strip();
        return true;
    }

    /**
     * Updates the hourly wage for the job to the new rate. If {@code newRate} is negative, hourly wage is not updated.
     * 
     * @param newRate The new hourly wage for the job
     * @return {@code true} if the hourly wage was updated successfully ({@code newRate} was not negative), otherwise {@code false}
     */
    public boolean updateHourlyWage(double newRate)
    {
        if (newRate < 0) return false;

        this.hourlyWage = newRate;
        return true;
    }

    /**
     * Returns the given Collection of Jobs sorted by id.
     * <p>
     * {@code jobs} is not modified.
     * 
     * @param jobs The jobs to sort (not modified) (cannot be {@code null})
     * @return A {@code List} of {@code Job} that is sorted by id from the given Collection
     * @throws NullPointerException If {@code jobs} is {@code null}
     */
    public static List<Job> sortJobsById(Collection<Job> jobs)
    {
        Objects.requireNonNull(jobs, "Jobs cannot be null");

        List<Job> sortedJobs = new ArrayList<>(jobs);
        sortedJobs.sort(Comparator.comparing((Job j) -> j.id));
        return sortedJobs;
    }

    /**
     * Returns a new {@code Job} initialized to be the value represented by the specified {@code String}.
     * <p>
     * The string should be formatted as the result of the toString() method on a Job object.
     * 
     * @param str The string to be parsed
     * @return The {@code Job} value represented by the string argument if valid
     * @throws NullPointerException If {@code str} is {@code null}
     * @throws IllegalArgumentException If {@code str} is formatted incorrectly
     */
    public static Job parseJob(String str) throws IllegalArgumentException
    {
        Objects.requireNonNull(str, "String must not be null");

        String[] parts = str.split(":");
        if (parts.length != 2) throw new IllegalArgumentException("String: " + str + " could not be converted to a Job as it is formatted incorrectly");

        String name = parts[0].strip();
        if (name.isBlank()) throw new IllegalArgumentException("String: " + str + " could not be converted to a Job as name is blank");

        String wageString = parts[1].strip().replace("$", "");
        if (wageString.equals(UNKNOWN_WAGE_STRING))
            return new Job(name);

        try
        {
            double wage = Double.parseDouble(wageString);
            return new Job(name, wage);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("String: " + str + " could not be converted to a Job as wage is not a number");
        }
    }

    @Override
    public String toString()
    {
        if (this.hourlyWage == -1) return this.name + ": " + UNKNOWN_WAGE_STRING;

        return this.name + ": " + Utility.formatPay(this.hourlyWage);
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null) return false;
        if (!(other instanceof Job)) return false;

        Job otherJob = (Job)other;
        return this.name.equals(otherJob.name);
    }

    @Override
    public int hashCode()
    {
        return this.id;
    }
}