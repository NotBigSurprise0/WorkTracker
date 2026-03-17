package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class WorkTracker
{
    /**
     * Binds jobs with names in lowercase to the job with the name in the first given case like "abc" binds to "ABc" if the user first enter "ABc" as the job name.
     */
    private HashMap<Job, Job> jobLookUp;
    private HashMap<Job, ArrayList<Shift>> shifts;
    private Path destinationPath;

    /**
     * Initializes jobs and shifts from the given file.
     * 
     * @param file The file to read from to get job and shift data or to create if one doesn't exist (cannot be {@code null})
     * @throws IOException If an error occurrs with the file.
     * @throws NullPointerException If {@code file} is {@code null}
     * @throws IllegalArgumentException If the file given exists and is formatted incorrectly
     */
    public WorkTracker(File file) throws IOException, NullPointerException, IllegalArgumentException
    {
        if (file == null) throw new NullPointerException("File cannot be null");

        this.destinationPath = Paths.get(file.getPath());
        if (!file.exists())
        {
            this.jobLookUp = new HashMap<>();
            this.shifts = new HashMap<>();
            return;
        }

        try (Scanner scanner = new Scanner(file))
        {
            this.jobLookUp = new HashMap<>();
            this.shifts = new HashMap<>();

            boolean collectingJobs = true;
            int lineNumber = 0;
            while (scanner.hasNextLine())
            {
                lineNumber++;
                String line = scanner.nextLine();
                if (line.isBlank())
                {
                    collectingJobs = false;
                    continue;
                }

                if (collectingJobs)
                {
                    Job job = Job.parseJob(line);
                    if (job == null) throw new IllegalArgumentException("Line " + lineNumber + " of " + file.getPath() + " is an incorrectly formatted Job");
                    Job lowercaseJob = new Job(job.getName().toLowerCase(), job.getCurrentHourlyWage());
                    this.jobLookUp.put(lowercaseJob, job);
                }
                else
                {
                    Shift shift = Shift.parseShift(line, this.jobLookUp.values());
                    if (shift == null) throw new IllegalArgumentException("Line " + lineNumber + " of " + file.getPath() + " is an incorrectly formatted Shift");
                    this.shifts.putIfAbsent(shift.getJob(), new ArrayList<>());
                    this.shifts.get(shift.getJob()).add(shift);
                }
            }
            System.out.println("Existing data loaded successfully.");
        }
        catch (IOException e)
        {
            System.out.println("An unexpected file error occurred.");
            System.out.println(e);
        }
    }

    /**
     * Gets the matching recorded job with the name in whatever case the user provided if that exists.
     * 
     * @param job The job to look for a matching job (cannot be {@code null})
     * @return The matching {@code Job} if one exists, otherwise {@code null}
     */
    public Job getMatchingJob(Job job)
    {
        if (job == null) throw new NullPointerException("Job cannot be null.");

        Job jobWithLowercaseName = new Job(job.getName().toLowerCase());
        return this.jobLookUp.get(jobWithLowercaseName);
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
        return this.getMatchingJob(job) != null;
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

        this.jobLookUp.put(new Job(job.getName().toLowerCase()), job);
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
        Job matchingJob = this.getMatchingJob(job);
        if (matchingJob == null) return false;

        ArrayList<Shift> jobShifts = this.shifts.get(matchingJob);
        jobShifts.add(shift);
        return true;
    }

    /**
     * Gets the list of jobs.
     * 
     * @return The list of jobs
     */
    public List<Job> getJobs()
    {
        return new ArrayList<>(jobLookUp.values());
    }

    /**
     * Gets the shifts for a job if the job exists.
     * 
     * @param job The job to get all the shifts for (cannot be {@code null})
     * @return The list of {@code Shift}s for the job if the job exists, otherwise null
     * @throws NullPointerException If {@code job} is {@code null}
     */
    public List<Shift> getShifts(Job job)
    {
        if (job == null) throw new NullPointerException("Job cannot be null.");

        Job matchingJob = this.getMatchingJob(job);
        if (matchingJob == null) return null;

        List<Shift> jobShifts = this.shifts.get(matchingJob);
        return jobShifts;
    }

    /**
     * Gets all the shifts that exist.
     * 
     * @return The list of all {@code Shift}s
     */
    public List<Shift> getAllShifts()
    {
        List<Shift> allShifts = new ArrayList<>();
        for (Job job : this.jobLookUp.values())
        {
            List<Shift> jobShifts = this.getShifts(job);
            allShifts.addAll(jobShifts);
        }
        return allShifts;
    }

    /**
     * Saves job and shift data to the file being tracked.
     * 
     * @return {@code true} If the data was saved successfully, otherwise {@code false}
     */
    public boolean save()
    {
        List<Job> jobs = this.getJobs();
        List<Shift> sortedShifts = Shift.sortShiftsById(this.getAllShifts());
        String tempPath = this.destinationPath.getParent() + "\\temp.txt";
        try (FileWriter writer = new FileWriter(tempPath))
        {
            for (Job job : jobs)
            {
                writer.write(job.toString());
                writer.write("\n");
            }
            writer.write("\n");
            for (Shift shift : sortedShifts)
            {
                writer.write(shift.toString());
                writer.write("\n");
            }
            writer.flush();
        }
        catch (IOException e)
        {
            return false;
        }
        
        try
        {
            Path source = Paths.get(tempPath);
            Path target = destinationPath;
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }
}