package moviebookingsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Screen {
    private String id;
    private String name;
    private List<Seat> seats;

    public Screen(String name)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.seats = new ArrayList<>();
    }

    public void addSeat(Seat seat)
    {
        seats.add(seat);
    }

    public List<Seat> getSeats()
    {
        return seats;
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
