package main.java.com.lift;

public class CarButton {
    private final String carId;
    private final int targetFloor;
    private final ElevatorController controller;

    public CarButton(String carId, int targetFloor, ElevatorController controller) {
        this.carId       = carId;
        this.targetFloor = targetFloor;
        this.controller  = controller;
    }

    public void press(int currentFloor) {
        System.out.println("Car button pressed in " + carId + " for floor " + targetFloor);
        controller.handleRequest(new ElevatorRequest(currentFloor, targetFloor));
    }
}
