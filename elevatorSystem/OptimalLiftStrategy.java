package elevatorSystem;

import java.util.List;
import java.util.TreeSet;

public class OptimalLiftStrategy implements IExternalAssignmentStrategy {
    public ElevatorCar assignLift(List<ElevatorCar> elevators, int inFloor, int outFloor)
    {
        ElevatorCar sameDirectionElevator = null;
        int sameDirectionCost = Integer.MAX_VALUE;

        ElevatorCar diffDirectionElevator = null;
        int diffDirectionCost = Integer.MAX_VALUE;

        ElevatorCar idleElevator = null;
        int idleElevatorCost = Integer.MAX_VALUE;

        Direction requestDirection = (inFloor < outFloor) ? Direction.UP : Direction.DOWN;

        for(ElevatorCar elevatorCar : elevators)
        {
            TreeSet<Integer> mainQueue = (requestDirection == Direction.UP)?elevatorCar.upDestination:elevatorCar.downDestination;

            if(mainQueue.contains(inFloor))
            {                
                return elevatorCar;
            }

            if((requestDirection == Direction.UP && elevatorCar.currentFloor < inFloor && elevatorCar.direction == requestDirection) 
                || (requestDirection == Direction.DOWN && elevatorCar.currentFloor>inFloor && elevatorCar.direction == requestDirection))
            {            
                int diff = Math.abs(inFloor-elevatorCar.currentFloor);
                if(diff<sameDirectionCost)
                {
                    sameDirectionCost = diff;
                    sameDirectionElevator = elevatorCar;
                }
            }            
            else if(requestDirection == Direction.IDLE)
            {
                int diff = Math.abs(inFloor-elevatorCar.currentFloor);
                if(diff<idleElevatorCost)
                {
                    idleElevatorCost = diff;
                    idleElevator = elevatorCar;
                }
            } 
            else
            {
                int cost = elevatorCar.upDestination.size()+elevatorCar.downDestination.size();
                if(cost < diffDirectionCost)
                {
                    diffDirectionCost = cost;
                    diffDirectionElevator = elevatorCar;
                }
            }
        }

        if(sameDirectionElevator != null)
        {
            return sameDirectionElevator;
        }

        if(idleElevator != null)
        {
            return idleElevator;
        }

        return diffDirectionElevator;
    }
}
