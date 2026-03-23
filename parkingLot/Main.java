import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        EntryGate gateA = new EntryGate("A");
        EntryGate gateB = new EntryGate("B");

        Slot l1s1 = new Slot("L1-S1", SlotType.SMALL,  1, Map.of("A", 10, "B", 50));
        Slot l1s2 = new Slot("L1-S2", SlotType.SMALL,  1, Map.of("A", 20, "B", 40));
        Slot l1m1 = new Slot("L1-M1", SlotType.MEDIUM, 1, Map.of("A", 15, "B", 35));
        Slot l1l1 = new Slot("L1-L1", SlotType.LARGE,  1, Map.of("A", 30, "B", 20));
        Level level1 = new Level(1, List.of(l1s1, l1s2, l1m1, l1l1));

        Slot l2s1 = new Slot("L2-S1", SlotType.SMALL,  2, Map.of("A", 60, "B", 10));
        Slot l2m1 = new Slot("L2-M1", SlotType.MEDIUM, 2, Map.of("A", 70, "B", 15));
        Slot l2l1 = new Slot("L2-L1", SlotType.LARGE,  2, Map.of("A", 80, "B", 25));
        Level level2 = new Level(2, List.of(l2s1, l2m1, l2l1));

        ParkingLotService service = new ParkingLotService(
                List.of(level1, level2),
                new NearestSlotAssignmentStrategy()
        );
        Vehicle car = new Vehicle("KA-01-AB-1234", VehicleType.CAR);
        LocalDateTime entryTime = LocalDateTime.now().minusHours(3);
        Ticket carTicket = service.park(car, SlotType.MEDIUM, gateA, entryTime);
        System.out.println("Parked: " + carTicket);

        Vehicle bike = new Vehicle("KA-02-CD-5678", VehicleType.TWO_WHEELER);
        Ticket bikeTicket = service.park(bike, SlotType.SMALL, gateB, entryTime);
        System.out.println("Parked: " + bikeTicket);
        System.out.println("\nAvailable SMALL slots: " + service.getAvailableSlots(SlotType.SMALL));

        // --- Exit ---
        LocalDateTime exitTime = LocalDateTime.now();
        double carFee  = service.exit(carTicket, exitTime);
        double bikeFee = service.exit(bikeTicket, exitTime);

        System.out.println("\nCar parking fee  : ₹" + carFee);
        System.out.println("Bike parking fee : ₹" + bikeFee);
        try {
            Vehicle bus = new Vehicle("KA-03-EF-9999", VehicleType.BUS);
            service.park(bus, SlotType.LARGE, gateA, entryTime);
            service.park(bus, SlotType.LARGE, gateA, entryTime);
            service.park(bus, SlotType.LARGE, gateA, entryTime); 
        } catch (Exception e) {
            System.out.println("\nException: " + e.getMessage());
        }
    }
}