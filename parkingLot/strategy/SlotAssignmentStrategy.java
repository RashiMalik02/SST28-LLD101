import java.util.List;

public interface SlotAssignmentStrategy {
    // Returns the best available slot across all levels for a given type and gate
    Slot assignSlot(List<Level> levels, SlotType slotType, String gateId);
}
