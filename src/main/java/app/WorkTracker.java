package app;

import java.io.File;
import java.io.FileNotFoundException;
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
     * @param file The file that
     * @throws FileNotFoundException
     */
    public WorkTracker(File file) throws IOException
    {
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
     * Saves the jobs and shifts data to the file.
     */
    public void save()
    {

    }
}