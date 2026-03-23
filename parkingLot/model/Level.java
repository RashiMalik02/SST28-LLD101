import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Level {
    private final int levelNumber;
    private final List<Slot> slots;

    public Level(int levelNumber, List<Slot> slots) {
        this.levelNumber = levelNumber;
        this.slots = new ArrayList<>(slots);
    }

    public int getLevelNumber() { return levelNumber; }

    public List<Slot> getAvailableSlotsByType(SlotType slotType) {
        return slots.stream()
                .filter(s -> s.getSlotType() == slotType && !s.isOccupied())
                .collect(Collectors.toList());
    }

    public List<Slot> getAllSlotsByType(SlotType slotType) {
        return slots.stream()
                .filter(s -> s.getSlotType() == slotType)
                .collect(Collectors.toList());
    }
}