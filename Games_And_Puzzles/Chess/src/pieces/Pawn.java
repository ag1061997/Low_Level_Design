package pieces;

import core.Board;
import entities.Position;
import enums.Color;
import enums.PieceType;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public boolean canMove(Board board, Position from, Position to) {
        int direction = (color == Color.WHITE) ? -1 : 1;
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();

        if (colDiff == 0 && rowDiff == direction) {
            return board.getPiece(to) == null;
        }

        if (colDiff == 0 && rowDiff == 2 * direction && !hasMoved) {
            Position intermediate = new Position(
                    from.getRow() + direction, from.getCol());
            return board.getPiece(intermediate) == null
                    && board.getPiece(to) == null;
        }

        if (Math.abs(colDiff) == 1 && rowDiff == direction) {
            Piece target = board.getPiece(to);
            if (target != null && target.getColor() != this.color) {
                return true;
            }

            Piece adjacent = board.getPiece(new Position(from.getRow(), to.getCol()));
            return board.getPiece(to) == null
                    && adjacent != null
                    && adjacent.getPieceType() == PieceType.PAWN
                    && adjacent.getColor() != this.color;
        }

        return false;
    }
}
