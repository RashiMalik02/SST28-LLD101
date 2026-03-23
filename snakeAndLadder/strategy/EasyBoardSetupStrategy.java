import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EasyBoardSetupStrategy implements BoardSetupStrategy {
    private static final double SNAKE_MAX_DROP_RATIO = 0.15;
    private static final double LADDER_MIN_CLIMB_RATIO = 0.15;

    @Override
    public void setup(Board board) {
        int n = board.totalCells();
        int count = board.getSize();
        Random random = new Random();
        Set<Integer> occupied = new HashSet<>();

        // Place snakes
        int placed = 0;
        while (placed < count) {
            int head = 2 + random.nextInt(n - 2); // 2 to n-1 (never cell 1 or n)
            if (occupied.contains(head)) continue;

            int maxDrop = Math.max(1, (int)(n * SNAKE_MAX_DROP_RATIO));
            int tail = Math.max(1, head - (1 + random.nextInt(maxDrop)));

            if (occupied.contains(tail) || tail >= head) continue;

            occupied.add(head);
            occupied.add(tail);
            board.getCell(head).setSnake(new Snake(head, tail));
            placed++;
        }

        // Place ladders
        placed = 0;
        while (placed < count) {
            int start = 1 + random.nextInt(n - 1); // 1 to n-1
            if (occupied.contains(start)) continue;

            int minClimb = Math.max(1, (int)(n * LADDER_MIN_CLIMB_RATIO));
            int end = start + minClimb + random.nextInt(n - start - minClimb + 1);
            end = Math.min(end, n);

            if (occupied.contains(end) || end <= start || end == n) continue;

            occupied.add(start);
            occupied.add(end);
            board.getCell(start).setLadder(new Ladder(start, end));
            placed++;
        }
    }
}