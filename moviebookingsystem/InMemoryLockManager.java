package moviebookingsystem;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InMemoryLockManager implements LockManager {
    private static InMemoryLockManager instance;
    private ConcurrentHashMap<String, HashMap<String, String>> lockedSeats;
    private int LOCK_TIMEOUT_IN_SECONDS = 5;
    //ScheduledExecutorService extends ExecutorService and provided additional methods like schedule
    private ScheduledExecutorService scheduledExecutor;

    private InMemoryLockManager()
    {
        this.lockedSeats = new ConcurrentHashMap<>();
        scheduledExecutor = Executors.newScheduledThreadPool(3);
    }

    public synchronized static InMemoryLockManager getInstance()
    {
        if(instance == null)
        {
            instance = new InMemoryLockManager();
        }

        return instance;
    }

    //Return false to signify failed
    public boolean lockSeats(Show show, List<String> seats, User user)
    {
        synchronized(show)
        {
            HashMap<String, String> showLocks = lockedSeats.computeIfAbsent(show.getID(), k->new HashMap<>()); 

            for(var seat:seats)
            {
                if(showLocks.containsKey(seat))
                {
                    return false;
                }
            }

            for(var seat:seats)
            {
                showLocks.put(seat, user.getID());
            }

            scheduledExecutor.schedule(()->unlockSeats(show, seats, user), LOCK_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
            System.out.println("Locked seats for " + user.getName());
            return true;
        }
    }

    public boolean isSeatLocked(Show show, String seatID)
    {
        synchronized(show)
        {
            HashMap<String, String> showLocks = lockedSeats.computeIfAbsent(show.getID(), k->new HashMap<>()); 
            return showLocks.containsKey(seatID);
        }
    }

    //Return false to signify failed
    public boolean unlockSeats(Show show, List<String> seatIds, User user)
    {
        synchronized(show)
        {
            HashMap<String, String> showLocks = lockedSeats.computeIfAbsent(show.getID(), k->new HashMap<>()); 

            for(var seat:seatIds)
            {
                if(!showLocks.containsKey(seat) || showLocks.get(seat) != user.getID())
                {
                    return false;
                }
            }

            for(var seat:seatIds)
            {
                showLocks.remove(seat);
            }

            System.out.println("Unlocked seats occupied by " + user.getName());
            return true;
        }
    }
}
