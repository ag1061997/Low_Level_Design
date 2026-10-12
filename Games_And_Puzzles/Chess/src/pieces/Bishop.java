package pieces;

import core.Board;
import entities.Position;
import enums.Color;
import enums.PieceType;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public boolean canMove(Board board, Position from, Position to) {
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();

        if (Math.abs(rowDiff) != Math.abs(colDiff) || rowDiff == 0) {
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
