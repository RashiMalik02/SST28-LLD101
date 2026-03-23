public class GelRefillStrategy implements RefillStrategy {

    @Override
    public void refill(String color) {
        System.out.println("Gel pen refilled: gel cartridge replaced with " + color + " gel ink.");
    }
}
