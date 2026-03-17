package app;

import java.util.Objects;

public class Job
{
    private static final String UNKNOWN_WAGE_STRING = "Unknwon wage";

    private String name;
    private double hourlyWage;

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
     * Returns a new {@code Job} initialized to be the value represented by the specified {@code String}.
     * <p>
     * The string should be formatted as the result of the toString() method on a Job object.
     * 
     * @param str The string to be parsed
     * @return The {@code Job} value represented by the string argument if valid, otherwise {@code null}
     */
    public static Job parseJob(String str)
    {
        if (str == null) return null;

        String[] parts = str.split(":");
        if (parts.length != 2) return null;

        String name = parts[0].strip();
        if (name.isBlank()) return null;

        String wageString = parts[1].strip().replace("$", "");
        if (wageString.equalsIgnoreCase(UNKNOWN_WAGE_STRING))
            return new Job(name);

        try
        {
            double wage = Double.parseDouble(wageString);
            return new Job(name, wage);
        }
        catch (IllegalArgumentException e)
        {
            return null;
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
        return name.hashCode();
    }
}