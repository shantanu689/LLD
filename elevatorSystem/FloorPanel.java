package elevatorSystem;

public class FloorPanel {
    private int floor;
    private ElevatorController elevatorController;

    public FloorPanel(int floor, ElevatorController controller)
    {
        this.floor = floor;
        this.elevatorController = controller;
    }

    public int PressButton(int targetFloor)
    {
        if(targetFloor == floor)
        {
            System.out.println("Choose a destination other than the current floor");
            return -1;
        }
        int elevatorID = elevatorController.HandleRequest(floor, targetFloor);
        return elevatorID;
    }
}
