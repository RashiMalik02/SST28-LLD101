import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {

    private final Board board;
    private final Dice dice;
    private final Queue<Player> activePlayers;
    private final List<Player> winners;
    private final int totalCells;

    public Game(int n, List<String> playerNames, BoardSetupStrategy strategy) {
        this.board = new Board(n);
        strategy.setup(board);

        this.dice = new Dice();
        this.activePlayers = new LinkedList<>();
        this.winners = new ArrayList<>();
        this.totalCells = board.totalCells();

        for (String name : playerNames)
            activePlayers.add(new Player(name));
    }

    public void start() {
        System.out.println("Game started! Board size: " + board.getSize()
                + "x" + board.getSize() + " (" + totalCells + " cells)\n");

        while (activePlayers.size() >= 2) {
            Player player = activePlayers.poll();
            takeTurn(player);

            if (player.getPosition() == totalCells) {
                winners.add(player);
                System.out.println(">>> " + player.getName()
                        + " has WON! (Rank #" + winners.size() + ") <<<\n");
            } else {
                activePlayers.add(player);
            }
        }

        Player lastPlayer = activePlayers.poll();
        System.out.println(lastPlayer.getName() + " is the last one remaining. Game over!");

        System.out.println("\n=== Final Rankings ===");
        for (int i = 0; i < winners.size(); i++)
            System.out.println("Rank #" + (i + 1) + ": " + winners.get(i).getName());
    }

    private void takeTurn(Player player) {
        int diceValue = dice.roll();
        int currentPos = player.getPosition();
        int newPos = currentPos + diceValue;

        System.out.print(player.getName() + " rolled " + diceValue
                + " | " + currentPos + " -> ");

        if (newPos > totalCells) {
            System.out.println(currentPos + " (overshot, no move)");
            return;
        }

        player.setPosition(newPos);

        Cell cell = board.getCell(newPos);

        if (cell.getSnake() != null) {
            int tail = cell.getSnake().getTail();
            System.out.println(newPos + " [SNAKE! Slides down to " + tail + "]");
            player.setPosition(tail);

        } else if (cell.getLadder() != null) {
            int end = cell.getLadder().getEnd();
            System.out.println(newPos + " [LADDER! Climbs up to " + end + "]");
            player.setPosition(end);

        } else {
            System.out.println(newPos);
        }
    }
}