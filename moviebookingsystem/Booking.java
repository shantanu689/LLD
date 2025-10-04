package moviebookingsystem;

import java.util.List;
import java.util.UUID;

public class Booking {
    private String id;
    private User user;
    private Show show;
    private List<String> showSeatIds;
    private BookingStatus status;

    public Booking(User user, Show show, List<String> showSeatIds)
    {
        id = UUID.randomUUID().toString();
        this.user = user;
        this.show = show;
        this.showSeatIds = showSeatIds;
        this.status = BookingStatus.INPROGRESS;
    }

    public String getID()
    {
        return id;
    }

    public void setStatus(BookingStatus status)
    {
        this.status = status;
    }

    public BookingStatus getStatus()
    {
        return status;
    }
}
