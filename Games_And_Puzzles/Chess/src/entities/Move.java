package entities;

import enums.PieceType;
import pieces.Piece;

public class Move {
    private final Position source;
    private final Position destination;
    private final Piece piece;
    private Piece capturedPiece;
    private boolean isPromotion;
    private PieceType promotedTo;

    public Move(Position source, Position destination, Piece piece) {
        this.source = source;
        this.destination = destination;
        this.piece = piece;
    }

    public Position getSource() { return source; }
    public Position getDestination() { return destination; }
    public Piece getPiece() { return piece; }
    public Piece getCapturedPiece() { return capturedPiece; }
    public void setCapturedPiece(Piece piece) { this.capturedPiece = piece; }
    public PieceType getPromotedTo() { return promotedTo; }
    public boolean isPromotion() { return isPromotion; }
    public void setPromotion(boolean promotion, PieceType promotedTo) {
        this.isPromotion = promotion;
        this.promotedTo = promotedTo;
    }
}
