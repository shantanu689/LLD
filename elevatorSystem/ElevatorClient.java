package elevatorSystem;

public class ElevatorClient {
    public static void main(String args[])
    {
        ElevatorController controller = new ElevatorController(2, 5, new OptimalLiftStrategy());
        
        controller.GetFloorPanel(2).PressButton(5);
        controller.GetFloorPanel(2).PressButton(0);
        controller.GetFloorPanel(5).PressButton(0);
        controller.GetFloorPanel(5).PressButton(2);
        
    }
}
