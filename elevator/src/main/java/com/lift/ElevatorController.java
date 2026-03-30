package main.java.com.lift;

import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
    private final List<ElevatorCar> cars = new ArrayList<>();
    private IDispatchStrategy strategy; 

    public ElevatorController(IDispatchStrategy strategy) {
        this.strategy = strategy;
    }

    public void addCar(ElevatorCar car) {
         cars.add(car); 
    }
    public void setStrategy(IDispatchStrategy s) {
         this.strategy = s; 
    }

    public void handleRequest(ElevatorRequest request) {
        ElevatorCar chosen = strategy.selectCar(cars, request);
        if (chosen == null) {
            System.out.println("No available car for request from floor "
                + request.getSourceFloor());
            return;
        }
        dispatch(chosen, request);
    }

    private void dispatch(ElevatorCar car, ElevatorRequest request) {
        if (request.getDirection() == Direction.UP) {
            car.moveUp();
        } else {
            car.moveDown();
        }
    }
}
