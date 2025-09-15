package elevatorSystem;

import java.util.List;

public interface IExternalAssignmentStrategy {
    ElevatorCar assignLift(List<ElevatorCar> elevators, int inFloor, int outFloor);
}