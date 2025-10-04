package moviebookingsystem;

import java.util.HashMap;

public class TheatreService {
    private HashMap<String, Theatre> theatres;
    private static TheatreService instance;

    private TheatreService()
    {
        this.theatres = new HashMap<>();
    }

    public synchronized static TheatreService getInstance()
    {   
        if(instance == null)
        {
            instance = new TheatreService();
        }
        return instance;
    } 

    public Theatre addTheatre(String theatreName, String address)
    {   
        Theatre obj = new Theatre(theatreName, address);
        theatres.put(obj.getID(), obj);
        return obj;
    }

    public Screen addScreen(String theatreID, String screenName)
    {
        Screen screen = new Screen(screenName);
        theatres.get(theatreID).addScreen(screen);
        return screen;
    }

    public Seat addSeat(Screen screen, String row, int col, SeatType seatType)
    {
        Seat obj = new Seat(row, col, seatType);
        screen.addSeat(obj);
        return obj;
    }
}
