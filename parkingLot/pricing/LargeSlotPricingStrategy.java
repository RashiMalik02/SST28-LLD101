public class LargeSlotPricingStrategy implements PricingStrategy {
    private static final double HOURLY_RATE = 80.0;

    @Override
    public double calculatePrice(long hours) {
        return hours * HOURLY_RATE;
    }
}