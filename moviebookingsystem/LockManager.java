package moviebookingsystem;

import java.util.List;

interface LockManager {
    boolean lockSeats(Show show, List<String> seats, User user);
    boolean unlockSeats(Show show, List<String> seats, User user);
    boolean isSeatLocked(Show show, String seatID);    
}
