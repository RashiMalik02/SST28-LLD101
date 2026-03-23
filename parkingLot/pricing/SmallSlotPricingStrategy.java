public class SmallSlotPricingStrategy implements PricingStrategy {
    private static final double HOURLY_RATE = 20.0;

    @Override
    public double calculatePrice(long hours) {
        return hours * HOURLY_RATE;
    }
}