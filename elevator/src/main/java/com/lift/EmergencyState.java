package main.java.com.lift;

public class EmergencyState implements IElevatorState {
    @Override public void moveUp(ElevatorCar car)   { reject(car); }
    @Override public void moveDown(ElevatorCar car) { reject(car); }
    @Override public void idle(ElevatorCar car)     { reject(car); }
    @Override public void triggerEmergency(ElevatorCar car) { /* already in emergency */ }
    @Override public void requestMaintenance(ElevatorCar car) { reject(car); }
    @Override public boolean canAcceptRequests() { return false; }

    private void reject(ElevatorCar car) {
        System.out.println("Car " + car.getCarId() + " is in EMERGENCY. All ops halted.");
    }
}