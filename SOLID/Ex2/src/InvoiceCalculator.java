import java.util.*;

public class InvoiceCalculator {

    private final TaxPolicy taxPolicy;
    private final DiscountPolicy discountPolicy;

    public InvoiceCalculator(TaxPolicy taxPolicy, DiscountPolicy discountPolicy) {
        this.taxPolicy = taxPolicy;
        this.discountPolicy = discountPolicy;
    }
    public InvoiceSummary calculate(String customerType, Map<String, MenuItem> menu, List<OrderLine> lines) {

        double subtotal = 0.0;

        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            subtotal += item.price * l.qty;
        }

        double taxPct = taxPolicy.taxPercent(customerType);
        double tax = subtotal * (taxPct / 100.0);

        double discount = discountPolicy.discountAmount(customerType, subtotal, lines.size());

        double total = subtotal + tax - discount;

        return new InvoiceSummary(subtotal, taxPct, tax, discount, total);
    }
}