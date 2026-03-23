public class BallPointPen extends Pen {

    public BallPointPen(String color, WriteStrategy writeStrategy) {
        super(color, writeStrategy, new BallPointRefillStrategy());
    }
}
