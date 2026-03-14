package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class WorkTracker
{
    private HashSet<Job> jobs;
    private HashMap<Job, ArrayList<Shift>> shifts;
    private File file;

    /**
     * Initializes jobs and shifts from the given file.
     * 
     * @param file The file to read from to get job and shift data or to create if one doesn't exist (cannot be {@code null})
     * @throws IOException I actually do not know when this occurrs
     * @throws NullPointerException If {@code file} is {@code null}
     */
    public WorkTracker(File file) throws IOException, NullPointerException
    {
        if (file == null) throw new NullPointerException("File cannot be null");

        this.file = file;
        if (file.createNewFile())
        {
            this.jobs = new HashSet<>();
            this.shifts = new HashMap<>();
        }
        else
        {
            try (Scanner scanner = new Scanner(file))
            {
                //! COMPLETE
                this.jobs = new HashSet<>();
                this.shifts = new HashMap<>();
            }
            catch (IOException e)
            {
                System.out.println("An unexpected file error occurred.");
                System.out.println(e);
            }
        }
    }

    /**
     * Determines if the job exists in the set of jobs.
     * 
     * @param job The job to check (cannot be {@code null})
     * @return {@code true} if {@code job} exists in the set of jobs, otherwise {@code false}
     * @throws NullPointerException If {@code job} is {@code null}
     */
    public boolean jobExists(Job job)
    {
        if (job == null) throw new NullPointerException("Job cannot be null.");

        return this.jobs.contains(job);
    }

    /**
     * Adds a job to the set of jobs if it doesn't already exist.
     * 
     * @param job The job to add (cannot be {@code null})
     * @return {@code true} if {@code job} was successfully added to jobs ({@code job} was not in the set of jobs already), otherwise {@code false}
     * @throws NullPointerException If {@code job} is {@code null}
     */
    public boolean addJob(Job job)
    {
        if (job == null) throw new NullPointerException("Job cannot be null.");

        if (this.jobExists(job)) return false;

        this.jobs.add(job);
        this.shifts.put(job, new ArrayList<>());
        return true;
    }

    /**
     * Adds a shift if the shift is valid.
     * 
     * @param shift The shift to add with a valid job (cannot be {@code null})
     * @return {@code true} if {@code shift} was successfully added ({@code shift}'s job existed in jobs), otherwise {@code false}
     * @throws NullPointerException If {@code shift} is {@code null}
     */
    public boolean addShift(Shift shift)
    {
        if (shift == null) throw new NullPointerException("Shift cannot be null.");

        Job job = shift.getJob();
        if (!this.jobExists(job)) return false;

        ArrayList<Shift> jobShifts = this.shifts.get(job);
        jobShifts.add(shift);
        return true;
    }

    /**
     * Saves the jobs and shifts data to the file.
     */
    public void save()
    {

    }
}