import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final InvoiceCalculator calculator;
    private final InvoiceFormatter formatter;
    private final InvoiceRepository repository;
    private int invoiceSeq = 1000;

    public CafeteriaSystem(InvoiceCalculator calculator, InvoiceFormatter formatter, InvoiceRepository repository) {
        this.calculator = calculator;
        this.formatter = formatter;
        this.repository = repository;
    }

    public void addToMenu(MenuItem i) { menu.put(i.id, i); }

    // Intentionally SRP-violating: menu mgmt + tax + discount + format + persistence.
    public void checkout(String customerType, List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);
        InvoiceSummary summary = calculator.calculate(customerType, menu, lines);
        String printable = formatter.format(invId, lines, menu, summary);
        System.out.print(printable);
        repository.save(invId, printable);
        System.out.println("Saved invoice: " + invId + " (lines=" + repository.countLines(invId) + ")");
    }
}
