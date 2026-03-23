public class InkRefillStrategy implements RefillStrategy {
    @Override
    public void refill(String color) {
        System.out.println("Ink pen refilled with " + color + " ink by dipping nib.");
    }
}
