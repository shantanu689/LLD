package elevatorSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

public class ElevatorCar implements Runnable {
    public int currentFloor;
    public Direction direction;
    public int id;
    public TreeSet<Integer> upDestination, downDestination;
    public HashMap<Integer, ArrayList<Integer>> requestMap; 

    public ElevatorCar(int id)
    {
        this.id = id;
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
        upDestination = new TreeSet<>();
        downDestination = new TreeSet<>(Collections.reverseOrder());
        requestMap = new HashMap<>();
    }

    private synchronized void AddDestinationFloor(int floor)
    {
        if(currentFloor < floor)
        {
            upDestination.add(floor);
        }
        else
        {
            downDestination.add(floor);
        }
    }

    public void AddFloorRequest(int inFloor, int outFloor)
    {
        requestMap.computeIfAbsent(inFloor, x-> new ArrayList<>()).add(outFloor);
        AddDestinationFloor(inFloor);
    }

    @Override
    public void run()
    {
        while(true)
        {
            while(upDestination.size()==0 && downDestination.size()==0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            
            if(direction == Direction.IDLE)
            {
                direction = (upDestination.size() != 0)?Direction.UP:Direction.DOWN;
            }
            
            MoveLift();
        }
    }

    private void MoveLift()
    {
        TreeSet<Integer> mainQueue = (direction == Direction.UP) ? upDestination : downDestination;
        while(mainQueue.size() != 0)
        {            
            try {
                Thread.sleep(2000);                
                currentFloor += (direction == Direction.UP)?1:-1;
                System.out.println("Lift: " + id + ", Moving " + direction + ", CurrentFloor: " + currentFloor);
                synchronized(this)
                {
                    if(currentFloor == mainQueue.first())
                    {           
                        System.out.println("Lift: " + id + " opening/closing doors");             
                        mainQueue.remove(currentFloor);
                        if(requestMap.containsKey(currentFloor))
                        {                            
                            for(Integer pendingRequest: requestMap.get(currentFloor))
                            {
                                AddDestinationFloor(pendingRequest);
                            }
                        }

                        if(mainQueue.size() == 0)
                        {
                            HandleDirectionChange();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void HandleDirectionChange()
    {
        if(direction == Direction.UP && downDestination.size()>0)
        {   
            direction = Direction.DOWN;
        }
        else if(direction == Direction.DOWN && upDestination.size()>0)
        {
            direction = Direction.UP;
        }
        else
        {
            System.out.println("Lift " + id + " is now IDLE");
            direction = Direction.IDLE;
        }
    }
}
