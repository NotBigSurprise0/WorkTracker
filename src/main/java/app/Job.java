package app;

public class Job
{
    private String name;
    private double hourlyPay;

    /**
     * Creates a job with the given name and an unspecified hourly pay.
     * 
     * @param name The name of the job (cannot be null)
     * @throws IllegalArgumentException If the name is null
     */
    public Job(String name)
    {
        if (name == null) throw new NullPointerException("Cannot create a job with a null name.");

        this.name = name.trim();
        this.hourlyPay = -1;
    }

    /**
     * Creates a job with the given name and hourly pay.
     * 
     * @param name The name of the job (cannot be null)
     * @param hourlyPay The hourly pay (cannot be negative)
     * @throws IllegalArgumentException If the name is null or hourly pay is negative
     */
    public Job(String name, double hourlyPay)
    {
        if (name == null) throw new NullPointerException("Cannot create a job with a null name.");
        if (hourlyPay < 0) throw new IllegalArgumentException("Cannot have an hourly pay that is negative.");

        this.name = name.trim();
        this.hourlyPay = hourlyPay;
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
     * Gets the hourly pay for the job.
     * 
     * @return The hourly pay
     */
    public double getCurrentHourlyPay()
    {
        return this.hourlyPay;
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

        this.name = newName.trim();
        return true;
    }

    /**
     * Updates the hourly pay for the job to the new rate. If {@code newRate} is negative, hourly pay is not updated.
     * 
     * @param newRate The new hourly pay for the job
     * @return {@code true} if the hourly pay was updated successfully ({@code newRate} was not negative), otherwise {@code false}
     */
    public boolean updateHourlyPay(double newRate)
    {
        if (newRate < 0) return false;

        this.hourlyPay = newRate;
        return true;
    }

    @Override
    public String toString()
    {
        if (this.hourlyPay == -1) return this.name + ": Unknown pay";

        return this.name + ": " + Utility.formatPay(this.hourlyPay);
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