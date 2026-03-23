public class BoardSetupStrategyFactory {

    public static BoardSetupStrategy getStrategy(String level) {
        DifficultyLevel difficulty = DifficultyLevel.valueOf(level.toUpperCase());
        return switch (difficulty) {
            case EASY -> new EasyBoardSetupStrategy();
            case HARD -> new HardBoardSetupStrategy();
        };
    }
}