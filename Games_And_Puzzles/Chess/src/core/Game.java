package core;

import entities.Move;
import entities.Player;
import entities.Position;
import enums.Color;
import enums.GameStatus;
import enums.PieceType;
import exceptions.ChessException;
import pieces.Piece;
import pieces.Queen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private final Board board;
    private final Player[] players;
    private Color currentTurn;
    private final List<Move> moveHistory;
    private GameStatus status;
    private Color winner;

    public Game(Player whitePlayer, Player blackPlayer) {
        this.board = new Board();
        this.players = new Player[]{whitePlayer, blackPlayer};
        this.currentTurn = Color.WHITE;
        this.moveHistory = new ArrayList<>();
        this.status = GameStatus.ACTIVE;
        this.winner = null;
    }

    public boolean makeMove(Position from, Position to) {
        if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE
                || status == GameStatus.RESIGNED) {
            throw new ChessException("Game is already over: " + status);
        }

        Piece piece = board.getPiece(from);

        if (piece == null) {
            throw new ChessException("No piece at " + from);
        }
        if (piece.getColor() != currentTurn) {
            throw new ChessException("Not your turn. Current turn: " + currentTurn);
        }

        if (!piece.canMove(board, from, to)) {
            throw new ChessException("Illegal move for " + piece.getPieceType()
                    + " from " + from + " to " + to);
        }

        if (piece.getPieceType() == PieceType.KING
                && Math.abs(to.getCol() - from.getCol()) == 2) {
            if (isInCheck(currentTurn)) {
                throw new ChessException("Cannot castle while in check");
            }
            int step = (to.getCol() > from.getCol()) ? 1 : -1;
            Position pass = new Position(from.getRow(), from.getCol() + step);
            board.movePiece(from, pass);
            boolean passAttacked = isInCheck(currentTurn);
            board.movePiece(pass, from);
            if (passAttacked) {
                throw new ChessException(
                        "Cannot castle through an attacked square");
            }
        }

        Move move = new Move(from, to, piece);
        Piece capturedPiece = board.getPiece(to);
        boolean wasEnPassant = false;

        if (piece.getPieceType() == PieceType.PAWN
                && Math.abs(to.getCol() - from.getCol()) == 1
                && capturedPiece == null) {
            Position capturedPawnPos = new Position(from.getRow(), to.getCol());
            Piece capturedPawn = board.getPiece(capturedPawnPos);
            if (capturedPawn != null
                    && capturedPawn.getPieceType() == PieceType.PAWN
                    && capturedPawn.getColor() != currentTurn
                    && isValidEnPassant(from, to, capturedPawnPos)) {
                capturedPiece = capturedPawn;
                board.setPiece(capturedPawnPos, null);
                wasEnPassant = true;
            } else {
                throw new ChessException("Invalid en passant");
            }
        }

        move.setCapturedPiece(capturedPiece);

        boolean pieceHadMoved = piece.hasMoved();

        board.movePiece(from, to);
        piece.setHasMoved(true);

        if (piece.getPieceType() == PieceType.KING
                && Math.abs(to.getCol() - from.getCol()) == 2) {
            executeCastlingRookMove(from, to);
        }

        if (piece.getPieceType() == PieceType.PAWN) {
            int promotionRow = (piece.getColor() == Color.WHITE) ? 0 : 7;
            if (to.getRow() == promotionRow) {
                Queen promotedQueen = new Queen(piece.getColor());
                promotedQueen.setHasMoved(true);
                board.setPiece(to, promotedQueen);
                move.setPromotion(true, PieceType.QUEEN);
            }
        }

        if (isInCheck(currentTurn)) {
            // Undo the move and restore the piece's prior move state
            undoMove(move, from, to, piece, capturedPiece, wasEnPassant);
            piece.setHasMoved(pieceHadMoved);
            throw new ChessException(
                    "Move leaves your king in check");
        }

        moveHistory.add(move);

        currentTurn = currentTurn.opposite();

        updateGameStatus();

        return true;
    }

    private boolean isValidEnPassant(Position from, Position to,
                                     Position capturedPawnPos) {
        if (moveHistory.isEmpty()) return false;
        Move lastMove = moveHistory.get(moveHistory.size() - 1);
        return lastMove.getPiece().getPieceType() == PieceType.PAWN
                && Math.abs(lastMove.getDestination().getRow()
                - lastMove.getSource().getRow()) == 2
                && lastMove.getDestination().equals(capturedPawnPos);
    }

    private void executeCastlingRookMove(Position kingFrom, Position kingTo) {
        int row = kingFrom.getRow();
        if (kingTo.getCol() > kingFrom.getCol()) {
            Position rookFrom = new Position(row, 7);
            Position rookTo = new Position(row, 5);
            Piece rook = board.getPiece(rookFrom);
            board.movePiece(rookFrom, rookTo);
            rook.setHasMoved(true);
        } else {
            Position rookFrom = new Position(row, 0);
            Position rookTo = new Position(row, 3);
            Piece rook = board.getPiece(rookFrom);
            board.movePiece(rookFrom, rookTo);
            rook.setHasMoved(true);
        }
    }

    private void undoMove(Move move, Position from, Position to,
                          Piece piece, Piece capturedPiece, boolean wasEnPassant) {
        board.setPiece(from, piece);

        if (wasEnPassant) {
            board.setPiece(to, null);
            Position capturedPawnPos = new Position(from.getRow(), to.getCol());
            board.setPiece(capturedPawnPos, capturedPiece);
        } else {
            board.setPiece(to, capturedPiece);
        }

        if (piece.getPieceType() == PieceType.KING
                && Math.abs(to.getCol() - from.getCol()) == 2) {
            undoCastlingRookMove(from, to);
        }

    }

    private void undoCastlingRookMove(Position kingFrom, Position kingTo) {
        int row = kingFrom.getRow();
        Position rookFrom = (kingTo.getCol() > kingFrom.getCol())
                ? new Position(row, 5) : new Position(row, 3);
        Position rookTo = (kingTo.getCol() > kingFrom.getCol())
                ? new Position(row, 7) : new Position(row, 0);
        Piece rook = board.getPiece(rookFrom);
        board.movePiece(rookFrom, rookTo);
        if (rook != null) {
            rook.setHasMoved(false);
        }
    }

    public boolean isInCheck(Color color) {
        Position kingPos = board.findKing(color);
        Color opponent = color.opposite();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece != null && piece.getColor() == opponent) {
                    if (piece.canMove(board, new Position(row, col), kingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(Color color) {
        if (!isInCheck(color)) return false;
        return !hasAnyLegalMove(color);
    }

    public boolean isStalemate(Color color) {
        if (isInCheck(color)) return false;
        return !hasAnyLegalMove(color);
    }

    private boolean hasAnyLegalMove(Color color) {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Position from = new Position(fromRow, fromCol);
                Piece piece = board.getPiece(from);
                if (piece == null || piece.getColor() != color) continue;

                for (int toRow = 0; toRow < 8; toRow++) {
                    for (int toCol = 0; toCol < 8; toCol++) {
                        Position to = new Position(toRow, toCol);
                        if (from.equals(to)) continue;
                        if (isLegalMove(from, to)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMove(Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (piece == null || !piece.canMove(board, from, to)) {
            return false;
        }
        Color color = piece.getColor();
        boolean isCastle = piece.getPieceType() == PieceType.KING
                && Math.abs(to.getCol() - from.getCol()) == 2;

        if (isCastle) {
            if (isInCheck(color)) return false;
            int step = (to.getCol() > from.getCol()) ? 1 : -1;
            Position pass = new Position(from.getRow(), from.getCol() + step);
            board.movePiece(from, pass);
            boolean passAttacked = isInCheck(color);
            board.movePiece(pass, from);
            if (passAttacked) return false;
        }

        Piece captured = board.getPiece(to);
        Position enPassantPos = null;
        if (piece.getPieceType() == PieceType.PAWN
                && Math.abs(to.getCol() - from.getCol()) == 1
                && captured == null) {
            Position capturedPawnPos = new Position(from.getRow(), to.getCol());
            Piece capturedPawn = board.getPiece(capturedPawnPos);
            if (capturedPawn != null
                    && capturedPawn.getPieceType() == PieceType.PAWN
                    && capturedPawn.getColor() != color
                    && isValidEnPassant(from, to, capturedPawnPos)) {
                captured = capturedPawn;
                enPassantPos = capturedPawnPos;
                board.setPiece(capturedPawnPos, null);
            } else {
                return false;
            }
        }

        board.movePiece(from, to);
        if (isCastle) executeCastlingRookMove(from, to);

        boolean inCheck = isInCheck(color);

        board.setPiece(from, piece);
        if (enPassantPos != null) {
            board.setPiece(to, null);
            board.setPiece(enPassantPos, captured);
        } else {
            board.setPiece(to, captured);
        }
        if (isCastle) undoCastlingRookMove(from, to);

        return !inCheck;
    }

    private void updateGameStatus() {
        if (isCheckmate(currentTurn)) {
            status = GameStatus.CHECKMATE;
            winner = currentTurn.opposite();
        } else if (isStalemate(currentTurn)) {
            status = GameStatus.STALEMATE;
        } else if (isInCheck(currentTurn)) {
            status = GameStatus.CHECK;
        } else {
            status = GameStatus.ACTIVE;
        }
    }

    public void resign(Color color) {
        if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE
                || status == GameStatus.RESIGNED) {
            throw new ChessException("Game is already over");
        }
        status = GameStatus.RESIGNED;
        winner = color.opposite();
    }

    public GameStatus getStatus() { return status; }
    public Color getWinner() { return winner; }
    public Board getBoard() { return board; }
    public Color getCurrentTurn() { return currentTurn; }
    public List<Move> getMoveHistory() { return Collections.unmodifiableList(moveHistory); }
}
