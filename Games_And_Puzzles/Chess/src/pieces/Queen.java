package pieces;

import core.Board;
import entities.Position;
import enums.Color;
import enums.PieceType;

public class Queen extends Piece {
    public Queen(Color color) {
        super(color, PieceType.QUEEN);
    }

    @Override
    public boolean canMove(Board board, Position from, Position to) {
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();

        boolean isStraight = rowDiff == 0 || colDiff == 0;
        boolean isDiagonal = Math.abs(rowDiff) == Math.abs(colDiff);

        if (!isStraight && !isDiagonal) {
            return false;
        }

        if (!isPathClear(board, from, to)) {
            return false;
        }

        Piece target = board.getPiece(to);
        return target == null || target.getColor() != this.color;
    }

    private boolean isPathClear(Board board, Position from, Position to) {
        int rowDir = Integer.signum(to.getRow() - from.getRow());
        int colDir = Integer.signum(to.getCol() - from.getCol());

        int currentRow = from.getRow() + rowDir;
        int currentCol = from.getCol() + colDir;

        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            if (board.getPiece(new Position(currentRow, currentCol)) != null) {
                return false;
            }
            currentRow += rowDir;
            currentCol += colDir;
        }
        return true;
    }
}
