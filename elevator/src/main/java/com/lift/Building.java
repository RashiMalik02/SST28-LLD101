package main.java.com.lift;

public class Building {
    private final int totalFloors;
    private final ElevatorController controller;

    public Building(int totalFloors) {
        this.totalFloors = totalFloors;
        this.controller  = new ElevatorController(new NearestCarStrategy());
    }

    public void addCar(ElevatorCar car) { controller.addCar(car); }
    public ElevatorController getController() { return controller; }
    public int getTotalFloors() { return totalFloors; }
}
