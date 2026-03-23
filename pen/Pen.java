public abstract class Pen {
    protected String color;
    protected boolean started;
    protected WriteStrategy writeStrategy;
    protected RefillStrategy refillStrategy;

    public Pen(String color, WriteStrategy writeStrategy, RefillStrategy refillStrategy) {
        this.color = color;
        this.started = false;
        this.writeStrategy = writeStrategy;
        this.refillStrategy = refillStrategy;
    }

    public void start() {
        writeStrategy.start();
        started = true;
    }

    public void close() {
        writeStrategy.close();
        this.started = false;
    }

    public void write(String text) {
        if (!started) {
            throw new IllegalStateException("Pen not started. Call start() before writing.");
        }
        writeStrategy.write(text, color);
    }

    public void refill(String color) {
        this.color = color;
        refillStrategy.refill(color);
    }
}
