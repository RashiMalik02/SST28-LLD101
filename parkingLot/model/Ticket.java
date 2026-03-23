import java.time.LocalDateTime;

public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final Slot slot;
    private final EntryGate entryGate;
    private final LocalDateTime entryTime;

    public Ticket(String ticketId, Vehicle vehicle, Slot slot,
                  EntryGate entryGate, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryGate = entryGate;
        this.entryTime = entryTime;
    }

    public String getTicketId()       { return ticketId; }
    public Vehicle getVehicle()       { return vehicle; }
    public Slot getSlot()             { return slot; }
    public EntryGate getEntryGate()   { return entryGate; }
    public LocalDateTime getEntryTime(){ return entryTime; }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + ticketId +
                ", vehicle=" + vehicle +
                ", slot=" + slot +
                ", gate=" + entryGate +
                ", entryTime=" + entryTime +
                '}';
    }
}
