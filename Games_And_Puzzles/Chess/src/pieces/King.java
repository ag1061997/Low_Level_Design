package pieces;

import core.Board;
import entities.Position;
import enums.Color;
import enums.PieceType;

public class King extends Piece {
    public King(Color color) {
        super(color, PieceType.KING);
    }

    @Override
    public boolean canMove(Board board, Position from, Position to) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());

        if (rowDiff <= 1 && colDiff <= 1 && (rowDiff + colDiff > 0)) {
            Piece target = board.getPiece(to);
            return target == null || target.getColor() != this.color;
        }

        if (rowDiff == 0 && colDiff == 2 && !hasMoved) {
            return canCastle(board, from, to);
        }

        return false;
    }

    private boolean canCastle(Board board, Position from, Position to) {
        int row = from.getRow();
        int direction = to.getCol() > from.getCol() ? 1 : -1;
        int rookCol = direction == 1 ? 7 : 0;

        Piece rook = board.getPiece(new Position(row, rookCol));
        if (rook == null || rook.getPieceType() != PieceType.ROOK
                || rook.hasMoved()) {
            return false;
        }

        int startCol = Math.min(from.getCol(), rookCol) + 1;
        int endCol = Math.max(from.getCol(), rookCol);
        for (int col = startCol; col < endCol; col++) {
            if (board.getPiece(new Position(row, col)) != null) {
                return false;
            }
        }

        return true;
    }
}
