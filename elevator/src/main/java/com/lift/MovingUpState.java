package main.java.com.lift;

public class MovingUpState implements IElevatorState {
    @Override 
    public void moveUp(ElevatorCar car)   { /* already moving up */ }

    @Override 
    public void moveDown(ElevatorCar car) {
        car.setState(new IdleState()); car.setState(new MovingDownState());
    }

    @Override 
    public void idle(ElevatorCar car) {
         car.setState(new IdleState()); 
    }
    @Override 
    public void triggerEmergency(ElevatorCar car) {
        car.setState(new EmergencyState());
    }
    @Override 
    public void requestMaintenance(ElevatorCar car) {
        System.out.println("Car " + car.getCarId() + ": will enter maintenance after next stop.");
    }
    @Override
    public boolean canAcceptRequests() { 
        return true; 
    }
}
 
