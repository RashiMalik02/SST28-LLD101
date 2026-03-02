import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;

/**
 * Starter demo that shows why mutability is risky.
 *
 * After refactor:
 * - direct mutation should not compile (no setters)
 * - external modifications to tags should not affect the ticket
 * - service "updates" should return a NEW ticket instance
 */
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        IncidentTicket t = service.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created: " + t);

        // Demonstrate post-creation mutation through service
        t = service.assign(t, "agent@example.com");
        t = service.escalateToCritical(t);
        System.out.println("\nAfter service mutations: " + t);

        // Demonstrate external mutation via leaked list reference
        try {
            t.getTags().add("HACKED_FROM_OUTSIDE");
        } catch (Exception e) {
            System.out.println("\nTags are immutable: " + e);
        }

        System.out.println("\nFinal ticket: " + t);

        // Starter compiles; after refactor, you should redesign updates to create new objects instead.
    }
}
