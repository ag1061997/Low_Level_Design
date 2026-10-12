import entities.Game;
import entities.Player;
import enums.Symbol;

public class Main {
    public static void main(String[] args) {
        Player player1 = new Player("Abc", Symbol.X);
        Player player2 = new Player("Pqr", Symbol.O);

        Game game = new Game(player1, player2, 3);

        System.out.println("========== TIC TAC TOE ==========");

        // Alice (X) completes the top row and wins
        game.makeMove(0, 0);  // X at (0,0)
        game.makeMove(1, 0);  // O at (1,0)
        game.makeMove(0, 1);  // X at (0,1)
        game.makeMove(1, 1);  // O at (1,1)
        game.makeMove(0, 2);  // X at (0,2) - Alice wins!

        game.printBoard();

        System.out.println("Result: " + game.getStatus());
        Player winner = game.getWinner();
        if (winner != null) {
            System.out.println("Winner: " + winner.getName());
        }
    }
}