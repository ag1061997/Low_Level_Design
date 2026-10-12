package entities;

import exceptions.ChessException;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new ChessException(
                    "Invalid position: (" + row + ", " + col + ")");
        }
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}
