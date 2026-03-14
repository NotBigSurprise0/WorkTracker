package app;

import java.io.File;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            MenuManager menuManager = new MenuManager(new File("src/main/resources/file.txt"));
            menuManager.run();
        }
        catch (IOException e)
        {
            System.out.println("An error occurred");
        }
    }
}