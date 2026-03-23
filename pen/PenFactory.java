public class PenFactory {

    public static Pen createPen(PenType penType, String color, Cap cap) {
        WriteStrategy writeStrategy = resolveWriteStrategy(cap);

        return switch (penType) {
            case INK       -> new InkPen(color, writeStrategy);
            case BALLPOINT -> new BallPointPen(color, writeStrategy);
            case GEL       -> new GelPen(color, writeStrategy);
        };
    }

    public static Pen createGrippedPen(PenType penType, String color, Cap cap) {
        return new GrippedPen(createPen(penType, color, cap));
    }

    private static WriteStrategy resolveWriteStrategy(Cap cap) {
        return switch (cap) {
            case WITHOUT_CAP -> new ClickWriteStrategy();
            case WITH_CAP    -> new CappedWriteStrategy();
        };
    }
}
