package main.java.com.lift;

public class AlarmButton {
    private final ElevatorCar car; 

    public AlarmButton(ElevatorCar car) { this.car = car; }

    public void press() { car.triggerEmergency(); }
}
