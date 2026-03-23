import java.util.Map;

public class Slot {
    private final String slotId;
    private final SlotType slotType;
    private final int levelNumber;
    private final Map<String, Integer> distanceFromGates;
    private boolean isOccupied;

    public Slot(String slotId, SlotType slotType, int levelNumber,
                Map<String, Integer> distanceFromGates) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.levelNumber = levelNumber;
        this.distanceFromGates = distanceFromGates;
        this.isOccupied = false;
    }

    public String getSlotId()        { return slotId; }
    public SlotType getSlotType()    { return slotType; }
    public int getLevelNumber()      { return levelNumber; }
    public boolean isOccupied()      { return isOccupied; }

    public int getDistanceFromGate(String gateId) {
        return distanceFromGates.getOrDefault(gateId, Integer.MAX_VALUE);
    }

    public void occupy()   { this.isOccupied = true; }
    public void vacate()   { this.isOccupied = false; }

    @Override
    public String toString() {
        return "Slot[" + slotId + ", " + slotType + ", Level-" + levelNumber + "]";
    }
}
