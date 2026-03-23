public class Cell {
    private Snake snake;
    private Ladder ladder;

    public Snake getSnake()   { return snake; }
    public Ladder getLadder() { return ladder; }

    public void setSnake(Snake snake)   { this.snake = snake; }
    public void setLadder(Ladder ladder){ this.ladder = ladder; }
}