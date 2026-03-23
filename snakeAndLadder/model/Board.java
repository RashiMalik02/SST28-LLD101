public class Board {
    private final int size;
    private final Cell[][] cells;

    public Board(int size) {
        this.size = size;
        this.cells = new Cell[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                cells[i][j] = new Cell();
    }

    public int getSize()   { return size; }
    public int totalCells(){ return size * size; }

    public Cell getCell(int position) {
        // positions are 1-indexed
        int idx = position - 1;
        return cells[idx / size][idx % size];
    }
}