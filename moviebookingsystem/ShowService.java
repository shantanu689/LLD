package moviebookingsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowService {
    private static ShowService instance;
    private HashMap<String, Show> shows;
    private HashMap<String, List<Show>> movieToShowsMap;

    private ShowService() {
        shows = new HashMap<>();
        movieToShowsMap = new HashMap<>();
    }

    public synchronized static ShowService getInstance()
    {
        if(instance == null)
        {
            instance = new ShowService();
        }
        return instance;
    }

    public Show addShow(Movie movie, Theatre theatre, Screen screen, LocalDateTime startTime)
    {
        Show show = new Show(movie, theatre, screen, startTime);
        shows.put(show.getID(), show);
        movieToShowsMap.computeIfAbsent(movie.getID(), k->new ArrayList<>()).add(show);
        return show;
    }

    public List<Show> getShows(Movie movie)
    {
        return movieToShowsMap.get(movie.getID());
    }

    public List<ShowSeat> getAvailableSeats(Show show, LockManager lockManager)
    {
        var result = new ArrayList<ShowSeat>();
        for(var showSeat : show.getShowSeats())
        {
            if(showSeat.getStatus() == SeatStatus.AVAILABLE)
            {
                if(!lockManager.isSeatLocked(show, showSeat.getID()))
                {
                    result.add(showSeat);
                }
            }
        }
        return result;
    }
}
