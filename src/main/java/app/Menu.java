package app;

import java.util.ArrayList;
import java.util.List;

public class Menu
{
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
}