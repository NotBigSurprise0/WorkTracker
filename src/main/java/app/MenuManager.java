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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import app.enums.DisplayMode;

public class MenuManager
{
    private static final Menu MAIN_MENU = new Menu("---Main Menu---");
    private static final Scanner scanner = new Scanner(System.in);

    private final HashSet<Shift> currentShifts;
    private final WorkTracker workTracker;

    static 
    {
        MAIN_MENU.addOption("Add a job");
        MAIN_MENU.addOption("Add a shift");
        MAIN_MENU.addOption("Show all jobs");
        MAIN_MENU.addOption("Show current shifts");
        MAIN_MENU.addOption("Clear current shifts");
        MAIN_MENU.addOption("Load shifts for a job");
        MAIN_MENU.addOption("Load all shifts");
    }

    /**
     * Creates a new MenuManager, managing the given file.
     * 
     * @param file The file to manage
     * @throws IOException If an error occurrs with the file
     */
    public MenuManager(File file) throws IOException
    {
        this.currentShifts = new HashSet<>();
        this.workTracker = new WorkTracker(file);
    }

    /**
     * Waits for the user to press enter before continuing.
     */
    public static void pause()
    {
        System.out.print("Press enter to continue...");
        scanner.nextLine();
    }

    /**
     * Runs the MenuManager allowing for the user to choose from menus.
     */
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
                case 3 -> this.showAllJobsMenu();
                case 4 -> this.showCurrentShiftsMenu();
                case 5 -> this.clearCurrentShiftsMenu();
                case 6 -> this.loadShiftsForJobMenu();
                case 7 -> this.loadAllShiftsMenu();
                default -> {}
            }
            if (choice != 0)
            {
                System.out.println();
                pause();
                System.out.println();
            }

        }
    }

    /**
     * Sorts the current shifts and returns it as a list.
     * 
     * @return The {@code List} sorted with all shifts of the same job together.
     */
    private List<Shift> getSortedShifts()
    {
        List<Shift> shifts = new ArrayList<>(this.currentShifts);
        shifts.sort(Comparator.comparing((Shift s) -> s.getJob().hashCode()).thenComparing((Shift s) -> s.hashCode()));
        return shifts;
    }

    /**
     * Gets a list of the names of each job in the list of jobs.
     * 
     * @param jobs The {@code List} of jobs
     * @return A {@Code List} of {@code String}s with the names of each {@Code Job}
     */
    private static List<String> getJobNames(List<Job> jobs)
    {
        List<String> jobNames = new ArrayList<>();
        for (Job job : jobs)
        {
            jobNames.add(job.getName());
        }
        return jobNames;
    }

    /**
     * Adds a job.
     */
    private void addJobMenu()
    {
        Job job = null;
        boolean jobExists = true;
        while (job == null || jobExists)
        {
            System.out.print("Enter the name of the job (enter '!' to exit): ");
            String name = scanner.nextLine().trim();
            if (name.equals("!"))
            {
                System.out.println("Exiting...");
                return;
            }
            else if (name.isBlank())
            {
                System.out.println("Cannot have a blank name for a job...");
                continue;
            }
            
            job = new Job(name);
            jobExists = workTracker.jobExists(job);
            if (jobExists) System.out.println("That job already exists!");
        }
        double pay = -2;
        while (pay < 0 && pay != -1)
        {
            System.out.print("Enter the hourly pay for the job in dollars (enter -1 if unknown or '!' to exit): ");
            if (scanner.hasNextDouble())
            {
                pay = scanner.nextDouble();
                scanner.nextLine();
                if (pay < 0 && pay != -1)
                {
                    System.out.println("Invalid amount! Can't be negative.");
                }
            }
            else
            {
                if (scanner.nextLine().trim().equals("!"))
                {
                    System.out.println("Exiting...");
                    return;
                }

                System.out.println("That is not a number!");
            }
        }
        if (pay != -1) job.updateHourlyPay(pay);

        boolean success = workTracker.addJob(job);
        if (success)
            System.out.println("Successfully added job " + job.getName() + ".");
        else
            System.out.println("An error occurred and the job was not added. Try again.");
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
     * Gets the hours, minutes, and seconds in 24-hour time of a time based on the parts of a time formatted as 'xx:yy:zz' and if it is am or pm.
     * <p>
     * Displays error messages if the time could not be converted into hours, minutes, and seconds
     * 
     * @param parts Each part of the time and can be length 2 or 3 if seconds are not included (should not be {@code null})
     * @param isPm Determines if the time is in the afternoon or not
     * @return A Integer list containing the hours (0-23), minutes (0-59), and seconds (0-59) of the time if the parts could be converted, otherwise {@code null}
     */
    private static Integer[] getHoursMinutesSecondsFromTimeParts(String[] parts, boolean isPm)
    {
        if (!isNonNegativeInteger(parts[0]))
        {
            System.out.println("Invalid time. The hours part is not a valid number.");
            return null;
        }
        int hours = Integer.parseInt(parts[0]);
        if (hours == 0)
        {
            System.out.println("Invalid time. Hours cannot be 0.");
            return null;
        }
        // Convert to 24 hour time
        if (hours == 12 && !isPm) hours = 0;
        else if (hours < 12 && isPm) hours += 12;

        if (!isNonNegativeInteger(parts[1]))
        {
            System.out.println("Invalid time. The minutes part is not a valid number.");
            return null;
        }
        int minutes = Integer.parseInt(parts[1]);
        if (minutes < 0 || minutes >= 60)
        {
            System.out.println("Invalid time. Minutes part must be between 0 and 59.");
            return null;
        }

        int seconds = 0;
        if (parts.length == 3)
        {
            if (!isNonNegativeInteger(parts[2]))
            {
                System.out.println("Invalid time. Seconds part is not a valid number.");
                return null;
            }
            seconds = Integer.parseInt(parts[2]);
            if (seconds < 0 || seconds >= 60)
            {
                System.out.println("Invalid time. Seconds part must be between 0 and 59.");
                return null;
            }
        }
        return new Integer[]{hours, minutes, seconds};
    }

    /**
     * Gets a LocalTime from user input.
     * 
     * @param name The name shown when prompting the user (cannot be {@code null})
     * @return The {@code LocalTime} if the user didn't exit, otherwise {@code null} because the user exited
     * @throws NullPointerException if {@code name} is {@code null}
     */
    private static LocalTime getTime(String name)
    {
        if (name == null) throw new NullPointerException("Name cannot be null.");

        LocalTime time = null;
        while (time == null)
        {
            System.out.print("Enter the " + name + " in the format 'xx:xx am/pm' Eg. 10:15 pm or even 8:00:20 am (can include seconds) ('!' to exit): ");
            String result = scanner.nextLine().trim();
            if (result.equals("!"))
            {
                System.out.println("Exiting...");
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

            Integer[] numbers = getHoursMinutesSecondsFromTimeParts(timeParts, isPm);
            if (numbers == null) continue;

            time = LocalTime.of(numbers[0], numbers[1], numbers[2]);
        }
        return time;
    }

    /**
     * Gets a date from user input based on the number of days ago the date was.
     * 
     * @param name The name shown when prompting the user (cannot be {@code null})
     * @return The {@code LocalDate} if the user didn't exit, otherwise {@code null} because the user exited
     * @throws NullPointerException if {@code name} is {@code null}
     */
    private static LocalDate getDateFromDaysAgo(String name)
    {
        if (name == null) throw new NullPointerException("Name cannot be null.");

        LocalDate date = null;
        int daysAgo = -1;
        while (daysAgo < 0)
        {
            System.out.print("Enter the number of days ago the " + name + " was (0 for today, '!' to exit): ");
            if (scanner.hasNextInt())
            {
                daysAgo = scanner.nextInt();
                scanner.nextLine();
                if (daysAgo < 0)
                    System.out.println("Negative days ago??? Come on man, be normal.");
                else
                {
                    date = LocalDate.now().minusDays(daysAgo);
                    System.out.println("Date: " + date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
                    System.out.print("Is this correct? ('y' for yes, anything else for no): ");
                    if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) daysAgo = -1;
                }
            }
            else
            {
                if (scanner.nextLine().trim().equals("!"))
                {
                    System.out.println("Exiting...");
                    return null;
                }

                System.out.println("That is not a number man.");
            }
        }

        return date;
    }

    /**
     * Gets a date from user input based on the user-provided string formatted as a date.
     * 
     * @param name The name shown when prompting the user (cannot be {@code null})
     * @return The {@code LocalDate} if the user didn't exit, otherwise {@code null} because the user exited
     * @throws NullPointerException if {@code name} is {@code null}
     */
    private static LocalDate getDateFromDateString(String name)
    {
        if (name == null) throw new NullPointerException("Name cannot be null.");

        LocalDate date = null;
        while (date == null)
        {
            System.out.print("Enter the " + name + " in the format 'yyyy-mm-dd' Eg. 2020-05-15 for May 15, 2020 ('!' to exit): ");
            String result = scanner.nextLine().trim();
            if (result.equals("!"))
            {
                System.out.println("Exiting...");
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

    /**
     * Gets a LocalDate from user input.
     * 
     * @param name The name shown when prompting the user
     * @return The {@code LocalDate} if the user didn't exit, otherwise {@code null} because the user exited
     * @throws NullPointerException if {@code name} is {@code null}
     */
    private static LocalDate getDate(String name)
    {
        if (name == null) throw new NullPointerException("Name cannot be null.");

        System.out.print("Was the " + name + " recent (you know the number of days since the " + name + ")? ('y' for yes, '!' to exit, anything else no): ");
        String choice = scanner.nextLine().trim();
        if (choice.equals("!"))
        {
            System.out.println("Exiting...");
            return null;
        }

        if (choice.equalsIgnoreCase("Y"))
            return getDateFromDaysAgo(name);
        else
            return getDateFromDateString(name);
    }

    /**
     * Gets a non negative integer from the user and reprompts until the number is valid, or the user types '!'.
     * 
     * @param message The message to display to the user when prompting for the number
     * @return The non negative integer from the user if the user didn't exit, otherwise {@code null} because the user exited
     */
    private static Integer getNonNegativeInteger(String message)
    {
        Integer num = -1;
        while (num < 0)
        {
            System.out.print(message);
            if (scanner.hasNextInt())
            {
                num = scanner.nextInt();
                scanner.nextLine();
                if (num < 0) System.out.println("Number cannot be negative!");
            }
            else
            {
                if (scanner.nextLine().trim().equals("!"))
                {
                    System.out.println("Exiting...");
                    return null;
                }

                System.out.println("That is not a number!");
            }
        }
        return num;
    }

    /**
     * Gets a duration from user input.
     * 
     * @param name The name shown when prompting the user
     * @return The {@code Duration} if the user didn't exit, otherwise {@code null} because the user exited
     * @throws NullPointerException if {@code name} is {@code null}
     */
    private static Duration getDuration(String name)
    {
        if (name == null) throw new NullPointerException("Name cannot be null.");

        Duration duration = null;
        while (duration == null)
        {
            Integer hours = getNonNegativeInteger("Enter the number of hours the " + name + " was ('!' to exit): ");
            if (hours == null) return null;

            Integer minutes = getNonNegativeInteger("Enter the number of minutes the " + name + " was ('!' to exit): ");
            if (minutes == null) return null;

            Integer seconds = getNonNegativeInteger("Enter the number of seconds the " + name + " was ('!' to exit): ");
            if (seconds == null) return null;

            duration = Duration.ofHours(hours);
            duration = duration.plus(Duration.ofMinutes(minutes));
            duration = duration.plus(Duration.ofSeconds(seconds));
            if (duration.isZero())
            {
                System.out.print("The duration is 0. Are you sure this is what you want? ('y' for yes, anything else for no): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) duration = null;
            }
        }
        return duration;
    }

    /**
     * Gets the valid Job object that exists in the tracked jobs from user input.
     * 
     * @param message The message displayed the the user when prompting
     * @return The {@code Job} if the user didn't exit, otherwise {@code null} because the user exited
     */
    private Job getValidJob(String message)
    {
        List<String> jobNames = getJobNames(workTracker.getJobs());

        Job matchingJob = null;
        boolean jobExists = false;
        while (matchingJob == null || !jobExists)
        {
            System.out.println(message + " ('!' to exit)");
            System.out.println("Valid jobs: " + jobNames);
            String name = scanner.nextLine().trim();
            if (name.equals("!"))
            {
                System.out.println("Exiting...");
                return null;
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
        return matchingJob;
    }

    /**
     * Adds a shift for a job based on user input.
     */
    private void addShiftMenu()
    {
        List<Job> jobs = workTracker.getJobs();
        if (jobs.isEmpty())
        {
            System.out.println("No jobs exist. Have you tried adding a job?");
            return;
        } 

        Job matchingJob = this.getValidJob("Enter the job this shift is for");
        if (matchingJob == null) return;

        LocalDate startDate = getDate("start date");
        if (startDate == null) return;

        LocalTime startTime = getTime("start time");
        if (startTime == null) return;

        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        Duration duration = getDuration("shift");
        if (duration == null) return;

        System.out.print("This shift is from " + start.format(Shift.DEFAULT_DATETIME_FORMAT) + " to " + start.plus(duration).format(Shift.DEFAULT_DATETIME_FORMAT) + " Is this correct? ('y' for yes, anything else for no): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("Y"))
        {
            System.out.println("Exiting...");
            return;
        }

        System.out.print("Do you want to name the shift or use a default name ('y' for name, anything else for default): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y"))
        {
            System.out.print("Enter the name ('!' to exit): ");
            String name = scanner.nextLine().trim();
            if (name.equals("!"))
            {
                System.out.println("Exiting...");
                return;
            }

            Shift shift = new Shift(name, matchingJob, start, duration);
            boolean success = this.workTracker.addShift(shift);
            if (success)
                System.out.println("Shift: " + shift.getName() + " added successfully.");
            else
                System.out.println("An error occurred and the shift was not added successfully.");
        }
        else
        {
            Shift shift = new Shift(matchingJob, start, duration);
            boolean success = this.workTracker.addShift(shift);
            if (success)
                System.out.println("Shift: " + shift.getName() + " added successfully.");
            else
                System.out.println("An error occurred and the shift was not added successfully.");
        }
    }

    /**
     * Shows all the tracked jobs.
     */
    private void showAllJobsMenu()
    {
        List<Job> jobs = workTracker.getJobs();
        if (jobs.isEmpty())
        {
            System.out.println("No jobs exist. Have you tried adding a job?");
            return;
        }

        List<String> jobNames = getJobNames(jobs);
        System.out.println(jobNames);
    }

    /**
     * Shows the currently tracked shifts.
     */
    private void showCurrentShiftsMenu()
    {
        if (this.currentShifts.isEmpty())
        {
            System.out.println("There are currently no tracked shifts. Have you tried loading shifts or changing your filter?");
            return;
        }

        for (Shift shift : this.getSortedShifts())
        {
            System.out.println(shift.display(DisplayMode.Times));
        }
    }

    /**
     * Clears the currently tracked shifts.
     */
    private void clearCurrentShiftsMenu()
    {
        int elementsCleared = this.currentShifts.size();
        this.currentShifts.clear();
        System.out.print("Cleared " + elementsCleared + " element");
        if (elementsCleared != 1) System.out.print("s");
        System.out.println(" from current shifts.");
    }

    /**
     * Loads the shifts for a job into the currently tracked shifts.
     */
    private void loadShiftsForJobMenu()
    {
        if (this.workTracker.getJobs().isEmpty())
        {
            System.out.println("There are no jobs to get shifts from.");
            return;
        }

        Job validJob = this.getValidJob("Enter the job to get shifts from");
        if (validJob == null) return;

        int currentSize = this.currentShifts.size();
        List<Shift> shifts = this.workTracker.getShifts(validJob);
        this.currentShifts.addAll(shifts);
        int newSize = this.currentShifts.size();
        int newElements = newSize - currentSize;
        System.out.print(newElements + " new element");
        if (newElements == 1) System.out.print(" was");
        else System.out.print("s were");
        System.out.println(" added to current shifts.");
    }

    /**
     * Loads all the shifts into the currently tracked shifts.
     */
    private void loadAllShiftsMenu()
    {
        int currentSize = this.currentShifts.size();
        List<Shift> allShifts = this.workTracker.getAllShifts();
        this.currentShifts.addAll(allShifts);
        int newSize = this.currentShifts.size();
        int newElements = newSize - currentSize;
        System.out.print(newElements + " new element");
        if (newElements == 1) System.out.print(" was");
        else System.out.print("s were");
        System.out.println(" added to current shifts.");
    }

}