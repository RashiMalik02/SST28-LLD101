package main.java.com.lift;

public class ElevatorCar implements IMovable, IEmergencyHandler {

    private final String carId;
    private int currentFloor;
    private IElevatorState state;
    private final WeightSensor weightSensor;  

    public ElevatorCar(String carId, int startFloor, float weightLimit) {
        this.carId         = carId;
        this.currentFloor  = startFloor;
        this.state         = new IdleState();
        this.weightSensor  = new WeightSensor(weightLimit);
    }

    @Override
    public void moveUp() {
        if (weightSensor.isOverWeightLimit()) {
            System.out.println("Car " + carId + ": weight limit exceeded. Doors will not close.");
            return;
        }
        state.moveUp(this);
        currentFloor++;
        System.out.println("Car " + carId + " moving UP → floor " + currentFloor);
    }

    @Override
    public void moveDown() {
        if (weightSensor.isOverWeightLimit()) {
            System.out.println("Car " + carId + ": weight limit exceeded. Doors will not close.");
            return;
        }
        state.moveDown(this);
        currentFloor--;
        System.out.println("Car " + carId + " moving DOWN → floor " + currentFloor);
    }

    @Override
    public void idle() {
        state.idle(this);
        System.out.println("Car " + carId + " is IDLE at floor " + currentFloor);
    }

    @Override
    public void triggerEmergency() {
        state.triggerEmergency(this);
        System.out.println("!!! EMERGENCY triggered in car " + carId + " !!!");
    }

    public void requestMaintenance() {
         state.requestMaintenance(this); 
    }

    public boolean canAcceptRequests(){
         return state.canAcceptRequests(); 
        }
    public String getCarId() { 
        return carId; 
    }
    public int getCurrentFloor() {
         return currentFloor; 
    }
    public void setState(IElevatorState s){
        this.state = s; 
    }
    public IElevatorState getState() { 
        return state;
    }
    public WeightSensor getWeightSensor() { 
        return weightSensor; 
    }

    public void loadWeight(float weight) {
        weightSensor.updateWeight(weight);
    }
}
