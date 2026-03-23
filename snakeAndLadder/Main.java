public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter board size (n for nxn board): ");
        int n = sc.nextInt();

        System.out.print("Enter number of players: ");
        int x = sc.nextInt();

        System.out.print("Enter difficulty level (easy/hard): ");
        String difficulty = sc.next();

        List<String> playerNames = new ArrayList<>();
        for (int i = 1; i <= x; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            playerNames.add(sc.next());
        }

        BoardSetupStrategy strategy = BoardSetupStrategyFactory.getStrategy(difficulty);
        Game game = new Game(n, playerNames, strategy);
        game.start();

        sc.close();
    }
}
