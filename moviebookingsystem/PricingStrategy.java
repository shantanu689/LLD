package moviebookingsystem;

import java.util.List;

interface PricingStrategy {
    Double calculatePrice(List<ShowSeat> seats);
}
