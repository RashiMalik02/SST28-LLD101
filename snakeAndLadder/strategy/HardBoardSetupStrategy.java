import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HardBoardSetupStrategy implements BoardSetupStrategy {

    // Hard: snakes are long (big drops), ladders are short (small jumps)
    private static final double SNAKE_MIN_DROP_RATIO = 0.2;
    private static final double LADDER_MAX_CLIMB_RATIO = 0.1;

    @Override
    public void setup(Board board) {
        int n = board.totalCells();
        int count = board.getSize();
        Random random = new Random();
        Set<Integer> occupied = new HashSet<>();

        // Place snakes
        int placed = 0;
        while (placed < count) {
            int head = 2 + random.nextInt(n - 2);
            if (occupied.contains(head)) continue;

            int minDrop = Math.max(1, (int)(n * SNAKE_MIN_DROP_RATIO));
            if (head - minDrop < 1) continue;

            int tail = 1 + random.nextInt(head - minDrop);

            if (occupied.contains(tail) || tail >= head) continue;

            occupied.add(head);
            occupied.add(tail);
            board.getCell(head).setSnake(new Snake(head, tail));
            placed++;
        }

        // Place ladders
        placed = 0;
        while (placed < count) {
            int start = 1 + random.nextInt(n - 1);
            if (occupied.contains(start)) continue;

            int maxClimb = Math.max(1, (int)(n * LADDER_MAX_CLIMB_RATIO));
            int end = start + 1 + random.nextInt(maxClimb);
            end = Math.min(end, n);

            if (occupied.contains(end) || end <= start || end == n) continue;

            occupied.add(start);
            occupied.add(end);
            board.getCell(start).setLadder(new Ladder(start, end));
            placed++;
        }
    }
}