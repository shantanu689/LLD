package moviebookingsystem;

import java.util.HashMap;
import java.util.UUID;

public class Theatre {
    private String id;
    private String name;
    private String address;
    private HashMap<String, Screen> screens; 

    public Theatre(String name, String address)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.screens = new HashMap<>();
    }

    public void addScreen(Screen obj)
    {
        screens.put(obj.getID(), obj);
    }

    public String getID()
    {
        return this.id;
    }

    public String getName()
    {
        return name;
    }
}
