package moviebookingsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingService {
    private HashMap<String, Booking> bookings;
    private final PaymentService paymentService;
    private static BookingService instance;
    
    private BookingService(PaymentService paymentService)
    {
        this.paymentService = paymentService;
    }

    public synchronized static BookingService getInstance(PaymentService paymentService)
    {
        if(instance == null)
        {
            instance = new BookingService(paymentService);
        }
        return instance;
    }

    public Booking createBooking(User user, Show show, List<ShowSeat> showSeats, LockManager lockManager, PricingStrategy pricingStrategy)
    {
        //Lock Seats
        List<String> showSeatIds = new ArrayList<>();
        for(var seat: showSeats)
        {
            showSeatIds.add(seat.getID());
        }

        boolean seatsLocked = lockManager.lockSeats(show, showSeatIds, user);
        Booking obj = new Booking(user, show, showSeatIds);
        if(!seatsLocked)
        {
            obj.setStatus(BookingStatus.FAILED);
            System.out.println("Failed to lock desired seats, retry booking with different seats");
            return obj;
        }

        //Seats are locked, Calculate total fare
        Double fare = pricingStrategy.calculatePrice(showSeats);

        //Continue with payment
        Boolean paymentSuccess = paymentService.processPayment(fare);
        if(!paymentSuccess)
        {
            obj.setStatus(BookingStatus.FAILED);
        }
        else
        {
            for(var showSeat: showSeats)
            {
                showSeat.setStatus(SeatStatus.BOOKED);
            }
            obj.setStatus(BookingStatus.CONFIRMED);
        }
        
        System.out.println("Booking " + obj.getStatus() + " for user: " + user.getName());
        return obj;
    }

    public void cancelBooking(String bookingID)
    {
        if(bookings.containsKey(bookingID))
        {
            Booking obj = bookings.get(bookingID);
            // Implement
        }
        else
        {
            System.out.println("Invalid booking id provided for cancellation");
        }
    }
}
