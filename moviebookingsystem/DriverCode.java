package moviebookingsystem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DriverCode {
    public static void main(String[] args)
    {
        User alice = new User("alice");
        User bob = new User("bob");

        MovieService movieService = MovieService.getInstance();
        TheatreService theatreService = TheatreService.getInstance();

        Movie movie1 = movieService.addMovie("Movie1", 120.0);
        Movie movie2 = movieService.addMovie("Movie2", 60.0);

        Theatre theatre1 = theatreService.addTheatre("Theatre1", "address1");
        Theatre theatre2 = theatreService.addTheatre("Theatre2", "address2");

        var screen1Theatre1 = theatreService.addScreen(theatre1.getID(), "Screen1");
        var screen2Theatre1 = theatreService.addScreen(theatre1.getID(), "Screen2");
        var screen1Theatre2 = theatreService.addScreen(theatre2.getID(), "Screen1");

        theatreService.addSeat(screen2Theatre1, "A", 1, SeatType.REGULAR);
        theatreService.addSeat(screen2Theatre1, "A", 2, SeatType.REGULAR);
        theatreService.addSeat(screen2Theatre1, "B", 1, SeatType.PREMIUM);
        theatreService.addSeat(screen2Theatre1, "B", 2, SeatType.PREMIUM);

        theatreService.addSeat(screen1Theatre2, "A", 1, SeatType.REGULAR);
        theatreService.addSeat(screen1Theatre2, "A", 2, SeatType.REGULAR);
        theatreService.addSeat(screen1Theatre2, "B", 1, SeatType.PREMIUM);
        theatreService.addSeat(screen1Theatre2, "B", 2, SeatType.PREMIUM);

        ShowService showService = ShowService.getInstance();
        Show movie1ShowTheatre1 = showService.addShow(movie1, theatre1, screen2Theatre1, LocalDateTime.now().plusHours(1));
        Show movie2ShowTheatre1 = showService.addShow(movie2, theatre1, screen2Theatre1, LocalDateTime.now().plusHours(1));
        Show movie1ShowTheatre2 = showService.addShow(movie1, theatre2, screen1Theatre2, LocalDateTime.now().plusHours(3));

        //Get all shows for a movie
        List<Show> shows = showService.getShows(movie1);
        for(var show: shows)
        {
            System.out.println(show); 
        }
             
        var inMemoryLockManager = InMemoryLockManager.getInstance();
        //get available seats for show in theatre1, screen2
        var seats = printSeats(showService, movie1ShowTheatre1, inMemoryLockManager);
        BookingService bookingService = BookingService.getInstance(new CreditCardPaymentService());

        //book a premium and regular seat
        List<ShowSeat> chosenSeats = Arrays.asList(seats.get(0), seats.get(2));
        bookingService.createBooking(alice, movie1ShowTheatre1, chosenSeats, inMemoryLockManager, new WeekendPricingStrategy());
        
        //print seats after above are locked
        printSeats(showService, movie1ShowTheatre1, inMemoryLockManager);
        sleep(5000);
        printSeats(showService, movie1ShowTheatre1, inMemoryLockManager);
        bookingService.createBooking(alice, movie1ShowTheatre1, chosenSeats, inMemoryLockManager, new WeekendPricingStrategy());

        sleep(6000);
        printSeats(showService, movie1ShowTheatre1, inMemoryLockManager);
    }

    private static List<ShowSeat> printSeats(ShowService showService, Show show, InMemoryLockManager lockManager)
    {
        List<ShowSeat> seats = showService.getAvailableSeats(show, lockManager);
        System.out.println("Available seats for show: movie1 in theatre1");
        for(var seat: seats)
        {
            System.out.println(seat);
        }
        return seats;
    }

    private static void sleep(int millis)
    {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
