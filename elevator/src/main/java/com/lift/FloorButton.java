package main.java.com.lift;

public class FloorButton {
    private final int floor;
    private final Direction direction;
    private final ElevatorController controller;

    public FloorButton(int floor, Direction direction, ElevatorController controller) {
        this.floor      = floor;
        this.direction  = direction;
        this.controller = controller;
    }

    public void press() {
        System.out.println("Floor button pressed at floor " + floor + " going " + direction);
        controller.handleRequest(new ElevatorRequest(floor, direction == Direction.UP ? floor + 1 : floor - 1));
    }
}
