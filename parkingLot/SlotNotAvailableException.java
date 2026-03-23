public class SlotNotAvailableException extends RuntimeException {
    public SlotNotAvailableException(String slotType) {
        super("No available slot of type [" + slotType + "] in the parking lot.");
    }
}