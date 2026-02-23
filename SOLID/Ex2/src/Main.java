import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Cafeteria Billing ===");

        TaxPolicy taxPolicy = new DefaultTaxPolicy();
        DiscountPolicy discountPolicy = new DefaultDiscountPolicy();
        InvoiceCalculator calculator = new InvoiceCalculator(taxPolicy, discountPolicy);
        InvoiceFormatter formatter = new InvoiceFormatter();
        InvoiceRepository repository = new FileStore();


        CafeteriaSystem sys = new CafeteriaSystem(calculator, formatter, repository);
        sys.addToMenu(new MenuItem("M1", "Veg Thali", 80.00));
        sys.addToMenu(new MenuItem("C1", "Coffee", 30.00));
        sys.addToMenu(new MenuItem("S1", "Sandwich", 60.00));

        List<OrderLine> order = List.of(
                new OrderLine("M1", 2),
                new OrderLine("C1", 1)
        );

        sys.checkout("student", order);
    }
}
