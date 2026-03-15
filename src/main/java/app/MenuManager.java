package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuManager
{
    private static final Menu MAIN_MENU = new Menu("---Main Menu---");
    private static final Scanner scanner = new Scanner(System.in);

    private List<Shift> currentShifts;
    private WorkTracker workTracker;

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
    }

    public void addShiftMenu()
    {

    }
}
