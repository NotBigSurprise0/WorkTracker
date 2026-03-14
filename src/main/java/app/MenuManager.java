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
        System.out.println("Enter the name of the job: ");
    }

    public void addShiftMenu()
    {

    }
}
