public class PricingStrategyFactory {

    public static PricingStrategy getStrategy(SlotType slotType) {
        return switch (slotType) {
            case SMALL  -> new SmallSlotPricingStrategy();
            case MEDIUM -> new MediumSlotPricingStrategy();
            case LARGE  -> new LargeSlotPricingStrategy();
        };
    }
}
