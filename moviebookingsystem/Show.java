package moviebookingsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Show {
    private String id;
    private Movie movie;
    private Theatre theatre;
    private Screen screen;
    private LocalDateTime startTime;
    private List<ShowSeat> showSeats;

    public Show(Movie movie, Theatre theatre, Screen screen, LocalDateTime startTime)
    {
        this.id = UUID.randomUUID().toString();
        this.movie = movie;
        this.theatre = theatre;
        this.screen = screen;
        this.startTime = startTime;
        this.showSeats = new ArrayList<>();
        populateShowSeats();
    }

    private void populateShowSeats()
    {
        for(var seat: screen.getSeats())
        {
            ShowSeat showSeat = new ShowSeat(seat);
            showSeats.add(showSeat);
        }
    }

    public String getID()
    {
        return id;
    }

    public List<ShowSeat> getShowSeats()
    {
        return showSeats;
    }

    @Override
    public String toString()
    {
        return "Show for movie: " + movie.getName() + " starts in theatre: " + theatre.getName() + ", screen: " + screen.getName() + " from " + startTime;
    }
}
