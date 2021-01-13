package pylos_gowm.src.ia;

import model.Board;
import model.Player;
import model.Ball;

public class Status {
    private int currentPlayer;
    private int otherPlayer;
    private int[][][] board = new int[4][][];

    public Status(Board board, Player current, Player other) {
        currentPlayer = 1;
        otherPlayer = 2;
        Ball ball;
        for (int z = 0; z < 4; z++) {
            this.board[z] = new int[4 - z][4 - z];
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    ball = board.ballAt(x, y, z);
                    if (ball != null)
                        this.board[z][y][x] = ball.getOwner() == current ? currentPlayer : otherPlayer;
                    else
                        this.board[z][y][x] = 0;
                }
            }
        }
    }

    public Status(Status status) {
        currentPlayer = status.currentPlayer;
        otherPlayer = status.otherPlayer;
        for (int z = 0; z < 4; z++) {
            board[z] = new int[4 - z][4 - z];
            for (int y = 0; y < 4; y++)
                for (int x = 0; x < 4; x++)
                    board[z][y][x] = status.board[z][y][x];
        }
    }

    public void switchPlayers(){
        int tmp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = tmp;
    }

    public int evaluateStatus() {
        int score = 0;
        return score;
    }
}
