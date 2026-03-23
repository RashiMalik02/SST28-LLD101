

public class ClickWriteStrategy implements WriteStrategy{
    @Override
    public void start() {
        System.out.println("Pen started: tip extended by clicking.");
    }

    @Override
    public void write(String text, String color) {
        System.out.println("Writing [" + color + "]: " + text);
    }

    @Override
    public void close() {
        System.out.println("Pen closed: tip retracted by clicking.");
    }
}
