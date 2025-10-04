package moviebookingsystem;

public enum SeatType {
    REGULAR(100.0),
    PREMIUM(250.0);

    private double baseFare;

    private SeatType(double baseFare)
    {
        this.baseFare = baseFare;
    }

    public double getBaseFare()
    {
        return baseFare;
    }
}