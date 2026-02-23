public class RoomPricing implements FeeComponent {
    private final Money baseMonthly;

    public RoomPricing(Money baseMonthly) {
        this.baseMonthly = baseMonthly;
    }

    @Override
    public Money monthlyFee() {
        return baseMonthly;
    }
}
