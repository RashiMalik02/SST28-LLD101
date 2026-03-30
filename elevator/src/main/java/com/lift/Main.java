package main.java.com.lift;

public class Main {
    public static void main(String[] args) {
        Building building = new Building(20);

        ElevatorCar carA = new ElevatorCar("A", 1, 600f);  // 600 kg
        ElevatorCar carB = new ElevatorCar("B", 5, 800f);  // 800 kg
        building.addCar(carA);
        building.addCar(carB);

        ElevatorController ctrl = building.getController();

        // Simulate overweight – doors must not close
        carA.loadWeight(700f);
        carA.moveUp();  

        carA.loadWeight(400f);

        ctrl.handleRequest(new ElevatorRequest(3, 9));
        ctrl.handleRequest(new ElevatorRequest(1, 15));

        carB.requestMaintenance();
        ctrl.handleRequest(new ElevatorRequest(2, 8));  // only carA can serve

        
        AlarmButton alarm = new AlarmButton(carA);
        alarm.press();
        ctrl.handleRequest(new ElevatorRequest(1, 10)); // neither car available
    }
}

