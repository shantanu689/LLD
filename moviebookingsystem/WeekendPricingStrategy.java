package moviebookingsystem;

import java.util.List;

public class WeekendPricingStrategy implements PricingStrategy{
    private final Double surcharge = 2.0;

    public Double calculatePrice(List<ShowSeat> seats)
    {
        Double res = 0.0;
        for(var seat: seats)
        {
            res += seat.getBaseSeat().getType().getBaseFare();
        }

        return res*surcharge;
    }
}
