package main.java.com.lift;

public class ElevatorRequest {
    private final int sourceFloor;
    private final int targetFloor;
    private final Direction direction;

    public ElevatorRequest(int sourceFloor, int targetFloor) {
        this.sourceFloor = sourceFloor;
        this.targetFloor = targetFloor;
        this.direction = targetFloor > sourceFloor ? Direction.UP : Direction.DOWN;
    }

    public int getSourceFloor()    { return sourceFloor; }
    public int getTargetFloor()    { return targetFloor; }
    public Direction getDirection(){ return direction; }
}
