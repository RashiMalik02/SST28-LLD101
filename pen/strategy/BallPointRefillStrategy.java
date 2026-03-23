public class BallPointRefillStrategy implements RefillStrategy {

    @Override
    public void refill(String color) {
        System.out.println("Ballpoint pen refilled: cartridge replaced with " + color + " ink.");
    }
}
