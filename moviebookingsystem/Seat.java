package moviebookingsystem;

import java.util.UUID;

public class Seat {
    private String id;
    private String row;
    private int col;
    private SeatType seatType;

    public Seat(String row, int col, SeatType seatType)
    {
        this.id = UUID.randomUUID().toString();
        this.row = row;
        this.col = col;
        this.seatType = seatType;
    }
    
    public String getID()
    {
        return this.id;
    }

    public String getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public SeatType getType()
    {
        return seatType;
    }
}
