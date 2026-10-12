package entities;

import enums.GameStatus;
import enums.Symbol;
import exceptions.InvalidMoveException;

public class Game {
    private final Board board;
    private final Player[] players;
    private int currentPlayerIndex;
    private GameStatus status;

    public Game(Player player1, Player player2, int boardSize) {
        this.board = new Board(boardSize);
        this.players = new Player[]{player1, player2};
        this.currentPlayerIndex = 0;
        this.status = GameStatus.IN_PROGRESS;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public GameStatus getStatus() {
        return status;
    }

    public Player getWinner() {
        if (status == GameStatus.WINNER_O) {
            return players[0].getSymbol() == Symbol.O ? players[0] : players[1];
        } else if (status == GameStatus.WINNER_X) {
            return players[0].getSymbol() == Symbol.X ? players[0] : players[1];
        }
        return null;
    }

    public void printBoard() {
        board.printBoard();
    }

    public synchronized void makeMove(int row, int col) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new InvalidMoveException("Game is already over!");
        }

        if (!board.isCellEmpty(row, col)) {
            throw new InvalidMoveException("Cell (" + row + ", " + col + ") is already occupied");
        }

        Player currentPlayer = players[currentPlayerIndex];
        board.placeSymbol(row, col, currentPlayer.getSymbol());

        if(checkWin(row, col, currentPlayer.getSymbol())) {
            status = currentPlayer.getSymbol() == Symbol.X ? GameStatus.WINNER_X : GameStatus.WINNER_O;
            return;
        }

        if (board.isFull()) {
            status = GameStatus.DRAW;
            return;
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
    }

    private boolean checkWin(int row, int col, Symbol symbol) {
        int size = board.getSize();

        boolean win = true;
        for (int i = 0; i < size; i++) {
            if (board.getCell(row, i).getSymbol() != symbol) { win = false; break; }
        }
        if (win == true) return true;

        win = true;
        for (int i = 0; i < size; i++) {
            if (board.getCell(i, col).getSymbol() != symbol) { win = false; break; }
        }
        if (win == true) return true;

        if (row == col) {
            win = true;
            for (int i = 0; i < size; i++) {
                if (board.getCell(i, i).getSymbol() != symbol) { win = false; break; }
            }
            if (win == true) return true;
        }

        if (row + col == size - 1) {
            win = true;
            for (int i = 0; i < size; i++) {
                if (board.getCell(i, size - 1 - i).getSymbol() != symbol) { win = false; break; }
            }
            if (win == true) return true;
        }

        return false;
    }
}
