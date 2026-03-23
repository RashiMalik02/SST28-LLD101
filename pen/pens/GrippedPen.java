
public class GrippedPen extends Pen {

    private final Pen wrappedPen;

    public GrippedPen(Pen pen) {
        super(pen.color, pen.writeStrategy, pen.refillStrategy);
        this.wrappedPen = pen;
    }

    @Override
    public void start() {
        System.out.println("[Grip] Ergonomic grip engaged.");
        wrappedPen.start();
        this.started = wrappedPen.started;
    }

    @Override
    public void write(String text) {
        System.out.println("[Grip] Writing with a comfortable grip.");
        wrappedPen.write(text);
        this.started = wrappedPen.started;
    }

    @Override
    public void refill(String color) {
        wrappedPen.refill(color);
        this.color = wrappedPen.color;
    }

    @Override
    public void close() {
        wrappedPen.close();
        this.started = wrappedPen.started;
    }
}
