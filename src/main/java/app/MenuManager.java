package app;

import java.util.ArrayList;
import java.util.List;

public class MenuManager
{
    private List<Shift> currentShifts;
    private static final Menu MAIN_MENU = new Menu();

    public MenuManager()
    {
        this.currentShifts = new ArrayList<>();
    }
}
