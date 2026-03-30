package main.java.com.lift;

public class IdleState implements IElevatorState {
    @Override public void moveUp(ElevatorCar car)   { car.setState(new MovingUpState()); }
    @Override public void moveDown(ElevatorCar car) { car.setState(new MovingDownState()); }
    @Override public void idle(ElevatorCar car)     { /* already idle */ }
    @Override public void triggerEmergency(ElevatorCar car) {
        car.setState(new EmergencyState());
    }
    @Override public void requestMaintenance(ElevatorCar car) {
        car.setState(new MaintenanceState());
    }
    @Override public boolean canAcceptRequests() { return true; }
}