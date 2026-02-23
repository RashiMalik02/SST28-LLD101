public class AddOnPricing implements FeeComponent {
    private final Money price;

    public AddOnPricing(Money price) {
        this.price = price;
    }

    @Override
    public Money monthlyFee() {
        return price;
    }
}