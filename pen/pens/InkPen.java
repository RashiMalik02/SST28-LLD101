
public class InkPen extends Pen {
    public InkPen(String color, WriteStrategy writeStrategy) {
        super(color, writeStrategy, new InkRefillStrategy());
    }
}
