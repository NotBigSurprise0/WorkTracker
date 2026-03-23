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
import java.util.Objects;
import java.util.Scanner;

public class WorkTracker
{
    /**
     * Binds Strings of job names in lowercase to the job with the name in the first given case like "abc" binds to "ABc" if the user first enter "ABc" as the job name.
     */
    private HashMap<String, Job> jobLookUp;
    private HashMap<Job, ArrayList<Shift>> shifts;
    private Path destinationPath;

    /**
     * Method used to initialize fields from an existing file that is being read.
     * 
     * @param scanner The {@code Scanner} object reading the file (cannot be {@code null})
     * @param filePath The {@code String} path to the file for error messages (cannot be {@code null})
     * @throws NullPointerException If {@code scanner} or {@code filePath} is {@code null}
     * @throws IllegalArgumentException If the file being read is incorrectly formatted
     */
    private void initFieldsFromFile(Scanner scanner, String filePath) throws IllegalArgumentException
    {
        Objects.requireNonNull(scanner, "Scanner cannot be null");
        Objects.requireNonNull(filePath, "File path cannot be null");

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

            try
            {
                if (collectingJobs)
                {
                    Job job = Job.parseJob(line);
                    this.jobLookUp.put(job.getName().toLowerCase(), job);
                    this.shifts.put(job, new ArrayList<>());
                }
                else
                {
                    Shift shift = Shift.parseShift(line, this.jobLookUp.values());
                    this.shifts.get(shift.getJob()).add(shift);
                }
            }
            catch (IllegalArgumentException e)
            {
                throw new IllegalArgumentException("Line " + lineNumber + " of " + filePath + " is incorrectly formatted: " + e.getMessage());
            }
        }
        System.out.println("Existing data loaded successfully.");
    }

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
        Objects.requireNonNull(file, "File cannot be null");

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

            initFieldsFromFile(scanner, file.getPath());
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
     * @param jobName The job name to look for a matching job (cannot be {@code null})
     * @return The matching {@code Job} if one exists, otherwise {@code null}
     */
    public Job getMatchingJob(String jobName)
    {
        Objects.requireNonNull(jobName, "Job cannot be null");

        Job jobFromFullName = this.jobLookUp.get(jobName.toLowerCase());
        if (jobFromFullName != null) return jobFromFullName;

        Job jobFromShortName = null;
        for (String lowercaseName : jobLookUp.keySet())
        {
            if (lowercaseName.startsWith(jobName.toLowerCase()))
            {
                if (jobFromShortName == null) jobFromShortName = jobLookUp.get(lowercaseName);
                else return null; // Multiple possible choices Eg. (jobs: "Board", "Boston" if jobName is "bo", there are 2 options)
            }
        }
        return jobFromShortName;
    }

    /**
     * Determines if the job name exists in the set of jobs.
     * 
     * @param jobName The job name to check (cannot be {@code null})
     * @return {@code true} if {@code job} exists in the set of jobs, otherwise {@code false}
     * @throws NullPointerException If {@code job} is {@code null}
     */
    public boolean jobExists(String jobName)
    {
        Objects.requireNonNull(jobName, "Job name cannot be null");

        return this.getMatchingJob(jobName) != null;
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
        Objects.requireNonNull(job, "Job cannot be null");

        if (this.jobExists(job.getName())) return false;

        this.jobLookUp.put(job.getName().toLowerCase(), job);
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
        Objects.requireNonNull(shift, "Shift cannot be null");

        Job job = shift.getJob();
        Job matchingJob = this.getMatchingJob(job.getName());
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
     * @return The list of {@code Shift}s for the job if the job exists, otherwise {@code null}
     * @throws NullPointerException If {@code job} is {@code null}
     */
    public List<Shift> getShifts(String jobName)
    {
        Objects.requireNonNull(jobName, "Job name cannot be null");

        Job matchingJob = this.getMatchingJob(jobName);
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
        for (ArrayList<Shift> jobShifts : this.shifts.values())
            allShifts.addAll(jobShifts);
        return allShifts;
    }

    /**
     * Deletes a job based on the given name and all associated shifts.
     * 
     * @param jobName The name of the job (cannot be {@code null})
     * @return {@code true} if a job was removed, otherwise {@code false}
     * @throws NullPointerException If {@code jobName} is {@code null}
     */
    public boolean deleteJob(String jobName)
    {
        Objects.requireNonNull(jobName, "Job name cannot be null");

        Job removedJob = this.jobLookUp.remove(jobName.toLowerCase());
        if (removedJob != null)
            this.shifts.remove(removedJob);

        return removedJob != null;
    }

    /**
     * Deletes a shift if the shift has the same id as one in all shifts.
     * 
     * @param shift The shift to delete (cannot be {@code null})
     * @return {@code true} if {@code shift} was found in shifts and successfully removed, otherwise {@code false}
     * @throws NullPointerException If {@code shift} is {@code null}
     */
    public boolean deleteShift(Shift shift)
    {
        Objects.requireNonNull(shift, "Shift cannot be null");

        ArrayList<Shift> jobShifts = this.shifts.get(shift.getJob());
        if (jobShifts == null) return false;
        
        return jobShifts.remove(shift);
    }

    /**
     * Resets (clears) all jobs and shifts. Cannot be undone.
     */
    public void reset()
    {
        this.jobLookUp.clear();
        this.shifts.clear();
    }

    /**
     * Saves job and shift data to the file being tracked.
     * 
     * @return {@code true} If the data was saved successfully, otherwise {@code false}
     */
    public boolean save()
    {
        List<Job> sortedJobs = Job.sortJobsById(this.getJobs());
        List<Shift> sortedShifts = Shift.sortShiftsById(this.getAllShifts());
        String tempPath = this.destinationPath.getParent() + "\\temp.txt";
        try (FileWriter writer = new FileWriter(tempPath))
        {
            for (Job job : sortedJobs)
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