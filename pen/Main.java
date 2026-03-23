
public class Main {
    public static void main(String[] args) {
        Pen gelPen = PenFactory.createPen(
            PenFactory.PenType.GEL, "red", PenFactory.Cap.WITHOUT_CAP
        );
        gelPen.start();
        gelPen.write("LLD Pen design");
        gelPen.refill("bloack");
        gelPen.close();
        Pen grippedPen = PenFactory.createGrippedPen(
            PenFactory.PenType.BALLPOINT, "black", PenFactory.Cap.WITH_CAP
        );
        grippedPen.start();
        grippedPen.write("Decirator Design Pattern");
        grippedPen.close();
        try {
            Pen inkPen = PenFactory.createPen(
                PenFactory.PenType.INK, "green", PenFactory.Cap.WITH_CAP
            );
            inkPen.write("Should throw");
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
