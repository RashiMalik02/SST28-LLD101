package main.java.com.lift;

public class NearestCarStrategy implements IDispatchStrategy {
    @Override
    public ElevatorCar selectCar(List<ElevatorCar> cars, ElevatorRequest request) {
        return cars.stream()
            .filter(ElevatorCar::canAcceptRequests)
            .min(Comparator.comparingInt(c ->
                Math.abs(c.getCurrentFloor() - request.getSourceFloor())))
            .orElse(null);
    }
}
