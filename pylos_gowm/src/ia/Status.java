package pylos_gowm.src.ia;

import model.Ball;
import model.Board;
import model.Player;

public class Status {
    private int currentPlayer;
    private int otherPlayer;
    private int[][][] status = new int[4][][];

    public Status(Board board, Player current, Player other) {
        currentPlayer = 
        Ball ball;
        for (int z = 0; z < 4; z++) {
            status[z] = new int[4 - z][4 - z];
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    ball = board.ballAt(x, y, z);
                    if (ball != null)
                        status[z][y][x] = ball.getOwner() == current ? currentPlayer : otherPlayer;
                    else
                        status[z][y][x] = 0;
                }
            }
        }
    }

    public Status(Status status) {
        this.currentPlayer = status.currentPlayer;//.clone()
    }

    public int evaluateStatus() {
        int score = 0;
        return score;
    }
}
