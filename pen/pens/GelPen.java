
public class GelPen extends Pen {

    public GelPen(String color, WriteStrategy writeStrategy) {
        super(color, writeStrategy, new GelRefillStrategy());
    }
}
