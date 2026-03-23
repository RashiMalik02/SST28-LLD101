import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ParkingLotService {

    private final List<Level> levels;
    private final SlotAssignmentStrategy assignmentStrategy;
    private final Map<String, Ticket> activeTickets = new ConcurrentHashMap<>();

    public ParkingLotService(List<Level> levels, SlotAssignmentStrategy assignmentStrategy) {
        this.levels = levels;
        this.assignmentStrategy = assignmentStrategy;
    }

    
    public Ticket park(Vehicle vehicle, SlotType slotType,
                       EntryGate entryGate, LocalDateTime entryTime) {

        Slot slot = assignmentStrategy.assignSlot(levels, slotType, entryGate.getGateId());

        if (slot == null) {
            throw new SlotNotAvailableException(slotType.name());
        }

        slot.occupy();

        String ticketId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Ticket ticket = new Ticket(ticketId, vehicle, slot, entryGate, entryTime);
        activeTickets.put(ticketId, ticket);

        return ticket;
    }

    public double exit(Ticket ticket, LocalDateTime exitTime) {
        Slot slot = ticket.getSlot();
        slot.vacate();
        activeTickets.remove(ticket.getTicketId());

        long hours = ChronoUnit.HOURS.between(ticket.getEntryTime(), exitTime);
        hours = Math.max(1, hours); // minimum 1 hour charge

        PricingStrategy pricingStrategy = PricingStrategyFactory.getStrategy(slot.getSlotType());
        return pricingStrategy.calculatePrice(hours);
    }

    
    public List<Slot> getAvailableSlots(SlotType slotType) {
        return levels.stream()
                .flatMap(level -> level.getAvailableSlotsByType(slotType).stream())
                .collect(Collectors.toList());
    }
}
