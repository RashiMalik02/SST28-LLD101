import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hostel Fee Calculator ===");
        BookingRequest req = new BookingRequest(LegacyRoomTypes.DOUBLE, List.of(AddOn.LAUNDRY, AddOn.MESS));
        RoomPricingRegistry roomRegistry = new RoomPricingRegistry();
        AddOnPricingRegistry addOnRegistry = new AddOnPricingRegistry();
        HostelFeeCalculator calc = new HostelFeeCalculator(new FakeBookingRepo(), roomRegistry, addOnRegistry);
        calc.process(req);
    }
}
