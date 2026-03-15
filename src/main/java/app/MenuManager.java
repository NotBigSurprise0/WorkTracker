package app;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuManager
{
    private static final Menu MAIN_MENU = new Menu("---Main Menu---");
    private static final Scanner scanner = new Scanner(System.in);

    private List<Shift> currentShifts;
    private final WorkTracker workTracker;

    static 
    {
        MAIN_MENU.addOption("Add a job");
        MAIN_MENU.addOption("Add a shift");
        MAIN_MENU.addOption("Show all jobs");
        MAIN_MENU.addOption("Show all shifts for a job");
        MAIN_MENU.addOption("Show all shifts");
    }

    public MenuManager(File file) throws IOException
    {
        this.currentShifts = new ArrayList<>();
        this.workTracker = new WorkTracker(file);
    }

    public void run()
    {
        int choice = -1;
        while (choice != 0)
        {
            choice = MAIN_MENU.display();
            switch (choice)
            {
                case 1 -> this.addJobMenu();
                case 2 -> this.addShiftMenu();
                default -> {}
            }
        }
    }

    public void addJobMenu()
    {
        Job job = null;
        boolean jobExists = true;
        while (job == null || jobExists)
        {
            System.out.print("Enter the name of the job (enter '!' to exit): ");
            String name = scanner.nextLine().trim();
            if (name.equals("!"))
            {
                System.out.println("Exiting...\n");
                return;
            }
            
            job = new Job(name);
            jobExists = workTracker.jobExists(job);
            if (jobExists) System.out.println("That job already exists!");
        }
        double pay = -2;
        while (pay < 0 && pay != -1)
        {
            System.out.print("Enter the hourly pay for the job in dollars (enter -1 if unknown and '!' to exit)");
            if (scanner.hasNextInt())
            {
                pay = scanner.nextInt();
                if (pay < 0 && pay != -1)
                {
                    System.out.println("Invalid amount! Can't be negative.");
                }
            }
            else
            {
                if (scanner.nextLine().trim().equals("!"))
                {
                    System.out.println("Exiting...\n");
                    return;
                }

                System.out.println("That is not a number!");
                scanner.next();
            }
        }
        if (pay != -1) job.updateHourlyPay(pay);

        boolean success = workTracker.addJob(job);
        if (success)
            System.out.println("Successfully added job " + job.getName() + ".");
        else
            System.out.println("An error occurred and the job was not added. Try again.");
        System.out.println();
    }

    /**
     * Determines if a string represents an integer value and is not negative.
     * 
     * @param str The String to check
     * @return {@code true} if {@code str} can be converted to an integer and is not negative, otherwise {@code false}
     */
    private static boolean isNonNegativeInteger(String str)
    {
        try
        {
            int num = Integer.parseInt(str);
            return num >= 0;
        }
        catch (NumberFormatException n)
        {
            return false;
        }
    }

    /**
     * Gets a LocalTime from user input.
     * 
     * @param name The name shown when prompting the user
     * @return The {@code LocalTime} if the user didn't exit, otherwise {@code null} because the user exited
     */
    private static LocalTime getTime(String name)
    {
        LocalTime time = null;
        while (time == null)
        {
            System.out.print("Enter the " + name + " in the format 'xx:xx am/pm' Eg. 10:15 pm or even 8:00:20 am (can include seconds) ('!' to exit): ");
            String result = scanner.nextLine().trim();
            if (result.equals("!"))
            {
                System.out.println("Exiting...\n");
                return null;
            }

            String[] sections = result.split(" ");
            if (sections.length != 2)
            {
                System.out.println("Invalid time. Must have a time and am/pm section only.");
                continue;
            }

            if (!sections[1].equalsIgnoreCase("am") && !sections[1].equalsIgnoreCase("pm"))
            {
                System.out.println("Invalid time. Must say am/pm.");
                continue;
            }

            boolean isPm = sections[1].equalsIgnoreCase("pm");
            String[] timeParts = sections[0].split(":");
            if (timeParts.length != 2 && timeParts.length != 3)
            {
                System.out.println("Invalid time. The hour/minute/second part is formatted incorrectly.");
                continue;
            }

            if (!isNonNegativeInteger(timeParts[0]))
            {
                System.out.println("Invalid time. The hours part is not a valid number.");
                continue;
            }
            int hours = Integer.parseInt(timeParts[0]);
            if (hours == 0)
            {
                System.out.println("Invalid time. Hours cannot be 0.");
                continue;
            }
            // Convert to 24 hour time
            if (hours == 12 && !isPm) hours = 0;
            else if (hours < 12 && isPm) hours += 12;

            if (!isNonNegativeInteger(timeParts[1]))
            {
                System.out.println("Invalid time. The minutes part is not a valid number.");
                continue;
            }
            int minutes = Integer.parseInt(timeParts[1]);
            if (minutes < 0 || minutes >= 60)
            {
                System.out.println("Invalid time. Minutes part must be between 0 and 59.");
                continue;
            }

            int seconds = 0;
            if (timeParts.length == 3)
            {
                if (!isNonNegativeInteger(timeParts[2]))
                {
                    System.out.println("Invalid time. Seconds part is not a valid number.");
                    continue;
                }
                seconds = Integer.parseInt(timeParts[2]);
                if (seconds < 0 || seconds >= 60)
                {
                    System.out.println("Invalid time. Seconds part must be between 0 and 59.");
                    continue;
                }
            }

            time = LocalTime.of(hours, minutes, seconds);
        }
        return time;
    }

    /**
     * Gets a LocalDate from user input.
     * 
     * @param name The name shown when prompting the user
     * @return The {@code LocalDate} if the user didn't exit, otherwise {@code null} because the user exited
     */
    private static LocalDate getDate(String name)
    {
        if (name == null) throw new NullPointerException("Name cannot be null.");

        System.out.print("Was the " + name + " today or a few days ago? ('y' for yes, '!' to exit, anything else no): ");
        String choice = scanner.nextLine().trim();
        if (choice.equals("!"))
        {
            System.out.println("Exiting...\n");
            return null;
        }

        if (choice.equalsIgnoreCase("Y"))
        {
            int daysAgo = -1;
            while (daysAgo < 0)
            {
                System.out.print("Enter the number of days ago the " + name + " was (0 for today, '!' to exit): ");
                if (scanner.hasNextInt())
                {
                    daysAgo = scanner.nextInt();
                    if (daysAgo < 0) System.out.println("Negative days ago??? Come on man, be normal.");
                }
                else
                {
                    if (scanner.nextLine().trim().equals("!"))
                    {
                        System.out.println("Exiting...\n");
                        return null;
                    }

                    System.out.println("That is not a number man.");
                }
            }

            LocalDate date = LocalDate.now().minus(Duration.ofDays(daysAgo));
            System.out.println("Date: " + date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
            return date;
        }
        else
        {
            LocalDate date = null;
            while (date == null)
            {
                System.out.print("Enter the " + name + " in the format 'yyyy-mm-dd' Eg. 2020-11-15 for November 15, 2020 ('!' to exit): ");
                String result = scanner.nextLine().trim();
                if (result.equals("!"))
                {
                    System.out.println("Exiting...\n");
                    return null;
                }

                try
                {
                    date = LocalDate.parse(result, DateTimeFormatter.ISO_LOCAL_DATE);
                }
                catch (DateTimeParseException d)
                {
                    System.out.println("Invalid date");
                }
            }
            return date;
        }
    }

    public void addShiftMenu()
    {
        List<Job> jobs = workTracker.getJobs();
        if (jobs.isEmpty())
        {
            System.out.println("No jobs exist. Have you tried adding a job? Exiting...\n");
            return;
        } 

        Job matchingJob = null;
        boolean jobExists = false;
        while (matchingJob == null || !jobExists)
        {
            System.out.println("Enter the job this shift is for ('!' to exit)");
            System.out.println("Valid jobs: " + jobs.toString());
            String name = scanner.nextLine();
            if (name.equals("!"))
            {
                System.out.println("Exiting...\n");
                return;
            }

            Job job = new Job(name);
            jobExists = workTracker.jobExists(job);
            if (!jobExists)
            {
                System.out.println("Invalid job! That was not an option.");
            }
            else
            {
                matchingJob = workTracker.getMatchingJob(job);
            }
        }

        LocalDate startDate = getDate("start date");
        if (startDate == null) return;

        LocalTime startTime = getTime("start time");
        if (startTime == null) return;

        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        
    }
}
