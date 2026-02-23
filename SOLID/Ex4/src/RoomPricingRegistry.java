import java.util.*;

public class RoomPricingRegistry {
    private final Map<Integer, Money> prices = new HashMap<>();

    public RoomPricingRegistry() {
        prices.put(LegacyRoomTypes.SINGLE, new Money(14000.0));
        prices.put(LegacyRoomTypes.DOUBLE, new Money(15000.0));
        prices.put(LegacyRoomTypes.TRIPLE, new Money(12000.0));
        prices.put(LegacyRoomTypes.DELUXE, new Money(16000.0));
    }

    public RoomPricing getPricing(int roomType) {
        Money price = prices.getOrDefault(roomType, new Money(16000.0));
        return new RoomPricing(price);
    }
}