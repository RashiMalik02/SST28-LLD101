package main.java.com.lift;

public class MaintenanceState implements IElevatorState {
    @Override public void moveUp(ElevatorCar car)   { reject(car); }
    @Override public void moveDown(ElevatorCar car) { reject(car); }
    @Override public void idle(ElevatorCar car)     { reject(car); }
    @Override public void triggerEmergency(ElevatorCar car) {
        // Emergency overrides maintenance
        car.setState(new EmergencyState());
    }
    @Override public void requestMaintenance(ElevatorCar car) { /* already in maintenance */ }
    @Override public boolean canAcceptRequests() { return false; }

    private void reject(ElevatorCar car) {
        System.out.println("Car " + car.getCarId() + " is under maintenance. Request rejected.");
    }
}
