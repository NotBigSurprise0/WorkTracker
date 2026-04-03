package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Menu
{
    private static final Scanner scanner = new Scanner(System.in);

    private String header;
    private final List<String> options;
    private final boolean hasOption0;

    /**
     * Creates a menu with no options or header with a default option 0.
     */
    public Menu()
    {
        this.header = "";
        this.options = new ArrayList<>();
        this.hasOption0 = true;
    }

    /**
     * Creates a menu with no options or header with a default option 0 if {@code hasOption0} is {@code true}.
     * 
     * @param hasOption0 Determines if the menu should have a default option 0
     */
    public Menu(boolean hasOption0)
    {
        this.header = "";
        this.options = new ArrayList<>();
        this.hasOption0 = hasOption0;
    }

    /**
     * Creates a menu with the given header and no options with a default option 0.
     * 
     * @param header The header String to appear above the menu (cannot be {@code null})
     * @throws NullPointerException If {@code header} is {@code null}
     */
    public Menu(String header)
    {
        Objects.requireNonNull(header, "Header cannot be null");

        this.header = header;
        this.options = new ArrayList<>();
        this.hasOption0 = true;
    }

    /**
     * Creates a menu with the given header and no options with a default option 0 if {@code hasOption0} is {@code true}.
     * 
     * @param header The header String to appear above the menu (cannot be {@code null})
     * @param hasOption0 Determines if the menu should have a default option 0
     * @throws NullPointerException If {@code header} is {@code null}
     */
    public Menu(String header, boolean hasOption0)
    {
        Objects.requireNonNull(header, "Header cannot be null");

        this.header = header;
        this.options = new ArrayList<>();
        this.hasOption0 = hasOption0;
    }
    
    /**
     * Creates a menu with the given options and no header with a default option 0.
     * 
     * @param options The list of options to show on the menu (cannot be {@code null})
     * @throws NullPointerException If {@code options} is {@code null}
     */
    public Menu(List<String> options)
    {
        Objects.requireNonNull(options, "Options cannot be null");

        this.header = "";
        this.options = new ArrayList<>(options);
        this.hasOption0 = true;
    }

    /**
     * Creates a menu with the given options and no header with a default option 0 if {@code hasOption0} is {@code true}.
     * 
     * @param options The list of options to show on the menu (cannot be {@code null})
     * @param hasOption0 Determines if the menu should have a default option 0
     * @throws NullPointerException If {@code options} is {@code null}
     */
    public Menu(List<String> options, boolean hasOption0)
    {
        Objects.requireNonNull(options, "Options cannot be null");

        this.options = options;
        this.hasOption0 = hasOption0;
    }

    /**
     * Creates a menu with the given options and header with a default option 0.
     * 
     * @param header The header String to appear above the menu (cannot be {@code null})
     * @param options The list of options to show on the menu (cannot be {@code null})
     * @throws NullPointerException If {@code header} or {@code options} is {@code null}
     */
    public Menu(String header, List<String> options)
    {
        Objects.requireNonNull(header, "Header cannot be null");
        Objects.requireNonNull(options, "Options cannot be null");

        this.header = header;
        this.options = new ArrayList<>(options);
        this.hasOption0 = true;
    }

    /**
     * Creates a menu with the given options and header with a default option 0 if {@code hasOption0} is {@code true}.
     * 
     * @param header The header String to appear above the menu (cannot be {@code null})
     * @param options The list of options to show on the menu (cannot be {@code null})
     * @param hasOption0 Determines if the menu should have a default option 0
     * @throws NullPointerException If {@code header} or {@code options} is {@code null}
     */
    public Menu(String header, List<String> options, boolean hasOption0)
    {
        Objects.requireNonNull(header, "Header cannot be null");
        Objects.requireNonNull(options, "Options cannot be null");

        this.header = header;
        this.options = options;
        this.hasOption0 = hasOption0;
    }

    /**
     * Adds an option to the list of options.
     * 
     * @param option The option (cannot be {@code null})
     * @throws NullPointerException if {@code option} is {@code null}
     */
    public void addOption(String option)
    {
        Objects.requireNonNull(option, "Option cannot be null");

        options.add(option);
    }

    /**
     * Displays the menu and gets the choice option from the user. 0 is always permitted for exiting.
     * 
     * @return The choice number
     * @throws IllegalStateException If {@code hasOption0} is {@code false} and there are no options
     */
    public int display()
    {
        int size = this.options.size();

        int minChoice;
        if (this.hasOption0) minChoice = 0;
        else minChoice = 1;

        if (minChoice > size) throw new IllegalStateException("Cannot select an option when no options available.");

        int choice = -1;
        while (choice == -1)
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
            String choiceAsString = scanner.nextLine().strip();
            if (!Utility.isIntegerInBounds(choiceAsString, minChoice, size))
            {
                System.out.println("Invalid choice. Must be one of the options " + minChoice + "-" + size + "\n");
                continue;
            }

            choice = Integer.parseInt(choiceAsString);
        }
        return choice;
    }
}