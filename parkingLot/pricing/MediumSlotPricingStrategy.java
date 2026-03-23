public class MediumSlotPricingStrategy implements PricingStrategy {
    private static final double HOURLY_RATE = 40.0;

    @Override
    public double calculatePrice(long hours) {
        return hours * HOURLY_RATE;
    }
}
