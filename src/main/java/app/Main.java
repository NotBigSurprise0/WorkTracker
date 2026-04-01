package app;

import java.io.File;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        if (args.length > 1)
        {
            System.out.println("Program only excepts at most one argument.");
            return;
        }

        String path;
        if (args.length == 1)
        {
            path = args[0];
            System.out.println("Using file path: " + path);
        }
        else
        {
            path = "src/main/resources/file.txt";
            System.out.println("Using default file path: " + path);
        }
        try
        {
            MenuManager menuManager = new MenuManager(new File(path));
            menuManager.run();
        }
        catch (IOException e)
        {
            System.out.println("An error occurred");
        }
    }
}