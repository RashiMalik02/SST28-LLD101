package main.java.com.lift;

public class MovingDownState implements IElevatorState {
    @Override
    public void moveUp(ElevatorCar car)   {
        car.setState(new IdleState()); car.setState(new MovingUpState());
    }
    @Override
    public void moveDown(ElevatorCar car) { /* already moving down */ }
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
