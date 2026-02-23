import java.util.*;

public class HostelFeeCalculator {
    private final FakeBookingRepo repo;
    private final RoomPricingRegistry roomRegistry;
    private final AddOnPricingRegistry addOnRegistry;

    public HostelFeeCalculator(FakeBookingRepo repo,
                                RoomPricingRegistry roomRegistry,
                                AddOnPricingRegistry addOnRegistry) {
        this.repo = repo;
        this.roomRegistry = roomRegistry;
        this.addOnRegistry = addOnRegistry;
    }

    public void process(BookingRequest req) {
        List<FeeComponent> components = buildComponents(req);

        Money monthly = new Money(0.0);
        for (FeeComponent component : components) {
            monthly = monthly.plus(component.monthlyFee());
        }
        components.add(new LateFee(new Money(200.0)));
        
        Money deposit = new Money(5000.00);

        ReceiptPrinter.print(req, monthly, deposit);

        String bookingId = "H-" + (7000 + new Random(1).nextInt(1000));
        repo.save(bookingId, req, monthly, deposit);
    }

    private List<FeeComponent> buildComponents(BookingRequest req) {
        List<FeeComponent> components = new ArrayList<>();

        components.add(roomRegistry.getPricing(req.roomType));

        for (AddOn addOn : req.addOns) {
            components.add(addOnRegistry.getPricing(addOn));
        }

        return components;
    }
}