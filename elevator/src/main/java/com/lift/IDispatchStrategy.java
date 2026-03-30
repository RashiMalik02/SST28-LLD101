package main.java.com.lift;

import java.util.List;

public interface IDispatchStrategy {
    ElevatorCar selectCar(List<ElevatorCar> cars, ElevatorRequest request);
}
