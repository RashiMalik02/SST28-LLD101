package main.java.com.lift;

public interface IElevatorState {
    void moveUp(ElevatorCar car);
    void moveDown(ElevatorCar car);
    void idle(ElevatorCar car);
    void triggerEmergency(ElevatorCar car);
    void requestMaintenance(ElevatorCar car);
    boolean canAcceptRequests();
}
