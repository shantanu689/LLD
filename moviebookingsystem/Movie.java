package moviebookingsystem;

import java.util.UUID;

public class Movie {
    private String id;
    private String name;
    private Double duration;

    public Movie(String name, Double duration)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.duration = duration;
    }

    public String getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
