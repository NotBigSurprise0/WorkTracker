package app;

public class Job
{
    private String name;
    private double hourlyPay;

    public Job(String name)
    {
        this.name = name;
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

        this.name = newName;
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
    public boolean equals(Object other)
    {
        if (!(other instanceof Job)) return false;

        Job otherJob = (Job)other;
        return this.name.equals(otherJob.name) && this.hourlyPay == otherJob.hourlyPay;
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + ((Double)hourlyPay).hashCode();
        return result;
    }
}