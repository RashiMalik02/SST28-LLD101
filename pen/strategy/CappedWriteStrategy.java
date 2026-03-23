public class CappedWriteStrategy implements WriteStrategy {

    @Override
    public void start() {
        System.out.println("Pen started: cap removed.");
    }

    @Override
    public void write(String text, String color) {
        System.out.println("Writing [" + color + "]: " + text);
    }

    @Override
    public void close() {
        System.out.println("Pen closed: cap placed back.");
    }
}
