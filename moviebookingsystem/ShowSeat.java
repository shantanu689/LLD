package moviebookingsystem;

import java.util.UUID;

public class ShowSeat {
    private Seat seat;
    private String id;
    private SeatStatus status;

    public ShowSeat(Seat seat)
    {
        this.seat = seat;
        this.id = UUID.randomUUID().toString();
        this.status = SeatStatus.AVAILABLE;
    }

    public void setStatus(SeatStatus status)
    {
        this.status = status;
    }

    public String getID()
    {
        return id;
    }

    public SeatStatus getStatus()
    {
        return status;
    }

    public Seat getBaseSeat()
    {
        return seat;
    }

    @Override
    public String toString()
    {
        return seat.getRow() + seat.getCol() + ", STATUS: " + status;
    }
}
