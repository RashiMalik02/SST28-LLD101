package main.java.com.lift;

public class FCFSStrategy implements IDispatchStrategy {
    @Override
    public ElevatorCar selectCar(List<ElevatorCar> cars, ElevatorRequest request) {
        return cars.stream()
            .filter(ElevatorCar::canAcceptRequests)
            .findFirst()
            .orElse(null);
    }
}