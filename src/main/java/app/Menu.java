package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu
{
    private static final Scanner scanner = new Scanner(System.in);

    private String header;
    private List<String> options;

    /**
     * Creates a menu with no options or header.
     */
    public Menu()
    {
        this.header = "";
        this.options = new ArrayList<>();
    }

    /**
     * Creates a menu with the given header and no options.
     * 
     * @param header The header String to appear above the menu (cannot be {@code null})
     * @throws NullPointerException If {@code header} is {@code null}
     */
    public Menu(String header)
    {
        if (header == null) throw new NullPointerException("Header cannot be null.");

        this.header = header;
        this.options = new ArrayList<>();
    }
    
    /**
     * Creates a menu with the given options and no header.
     * 
     * @param options The list of options to show on the menu (cannot be {@code null})
     * @throws NullPointerException If {@code options} is {@code null}
     */
    public Menu(List<String> options)
    {
        if (options == null) throw new NullPointerException("Options cannot be null.");

        this.header = "";
        this.options = new ArrayList<>(options);
    }

    /**
     * Creates a menu with the given options and header.
     * 
     * @param header The header String to appear above the menu (cannot be {@code null})
     * @param options The list of options to show on the menu (cannot be {@code null})
     * @throws NullPointerException If {@code header} or {@code options} is {@code null}
     */
    public Menu(String header, List<String> options)
    {
        if (header == null) throw new NullPointerException("Header cannot be null.");
        if (options == null) throw new NullPointerException("Options cannot be null.");

        this.header = header;
        this.options = new ArrayList<>(options);
    }

    /**
     * Displays the menu and gets the choice option from the user. 0 is always permitted for exiting.
     * 
     * @return The choice number
     */
    public int display()
    {
        int size = this.options.size();

        int choice = -1;
        while (choice < 0 || choice > size)
        {
            if (!this.header.equals("")) System.out.println(this.header);

            for (int i = 0; i < size; i++)
            {
                System.out.print((i + 1) + ": ");
                System.out.println(this.options.get(i));
            }
            System.out.println("0: Exit");
            System.out.println();
            System.out.print("Choice: ");
            if (scanner.hasNextInt())
            {
                choice = scanner.nextInt();
                if (choice < 0 || choice > size)
                    System.out.println("Invalid choice. Must be one of the options 0-" + size);
            }
            else
            {
                System.out.println("That is not a number.");
                scanner.next();
            }
        }
        return choice;
    }
}