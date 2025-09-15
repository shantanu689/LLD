package elevatorSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElevatorController {
    private List<ElevatorCar> elevators;
    private HashMap<Integer, FloorPanel> floorPanels;
    private IExternalAssignmentStrategy externalAssignmentStrategy;

    public ElevatorController(int elevatorCount, int floors, IExternalAssignmentStrategy externalAssignmentStrategy)
    {
        this.elevators = new ArrayList<>();
        this.floorPanels = new HashMap<>();
        this.externalAssignmentStrategy = externalAssignmentStrategy;
        
        for(int i=1;i<=elevatorCount;i++)
        {
            ElevatorCar elevator= new ElevatorCar(i);
            elevators.add(elevator);
            Thread t = new Thread(elevator);
            t.start();
        }

        for(int i=0;i<=floors;i++)
        {
            floorPanels.put(i, new FloorPanel(i, this));
        }
    }

    int HandleRequest(int inFloor, int outFloor)
    {
        ElevatorCar chosenElevator = externalAssignmentStrategy.assignLift(elevators, inFloor, outFloor);
        System.out.println("Lift " + chosenElevator.id + " has been chosen for going from " + inFloor + " to " + outFloor);
        chosenElevator.AddFloorRequest(inFloor, outFloor);
        return chosenElevator.id;
    }

    FloorPanel GetFloorPanel(int floor)
    {
        return floorPanels.get(floor);
    }
}
