import java.util.Comparator;
import java.util.List;

public class NearestSlotAssignmentStrategy implements SlotAssignmentStrategy {

    @Override
    public Slot assignSlot(List<Level> levels, SlotType slotType, String gateId) {
        return levels.stream()
                .flatMap(level -> level.getAvailableSlotsByType(slotType).stream())
                .min(Comparator.comparingInt(slot -> slot.getDistanceFromGate(gateId)))
                .orElse(null);
    }
}
