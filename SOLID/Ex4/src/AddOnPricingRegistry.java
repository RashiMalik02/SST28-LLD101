import java.util.*;


public class AddOnPricingRegistry {
    private final Map<AddOn, Money> prices = new HashMap<>();

    public AddOnPricingRegistry() {
        prices.put(AddOn.MESS,    new Money(1000.0));
        prices.put(AddOn.LAUNDRY, new Money(500.0));
        prices.put(AddOn.GYM,     new Money(300.0));
    }

    public AddOnPricing getPricing(AddOn addOn) {
        Money price = prices.getOrDefault(addOn, new Money(0.0));
        return new AddOnPricing(price);
    }
}