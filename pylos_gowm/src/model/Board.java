package model;

import java.util.ArrayList;
import java.util.List;

import static model.Model.HEIGHT;

public class Board {
    public final List<Ball> balls = new ArrayList<>();

    public boolean isEmpty() {
        for (Ball ball : balls) {
            if (ball.getOwner().allBallsOnBoard())
                return false;
        }
        return true;
    }

    public boolean anyBallAt(Position position) {
        for (Ball ball : balls) {
            if (ball.getOwner().allBallsOnBoard() && ball.getPosition() == position)
                return true;
        }
        return false;
    }

    public Ball ballAt(Position position) {
        for (Ball ball : balls) {
            if (ball.isOnBoard() && ball.getPosition() == position)
                return ball;
        }
        return null;
    }

    public String toString() {
        StringBuilder board = new StringBuilder();
        board = new StringBuilder("-----------------\t-------------\t---------\t-----\n");
        Position p;
        boolean isValid = true;
        for (int y = 0; y < HEIGHT; y++) {
            for (int z = 0; z < HEIGHT; z++) {
                for (int x = 0; x < HEIGHT - z; x++) {
                    isValid = Position.isValid(x, y, z);
                    if (!isValid)
                        break;
                    board.append("|");
                    p = Position.at(x, y, z);
                    board.append(Ball.ballToString(ballAt(p)));
                }
                if (isValid)
                    board.append("|\t");
            }
            board.append("\n");
            if (y == 0)
                board.append("-----------------\t-------------\t---------\t-----\n");
            if (y == 1)
                board.append("-----------------\t-------------\t---------\n");
            if (y == 2)
                board.append("-----------------\t-------------\n");
            if (y == 3)
                board.append("-----------------\n");
        }
        return board.toString();
    }

    public String toStringList(List<Ball> l) {
        StringBuilder board = new StringBuilder();
        board = new StringBuilder("-----------------\t-------------\t---------\t-----\n");
        Position p;
        Ball b;
        boolean isValid = true;
        for (int y = 0; y < HEIGHT; y++) {
            for (int z = 0; z < HEIGHT; z++) {
                for (int x = 0; x < HEIGHT - z; x++) {
                    isValid = Position.isValid(x, y, z);
                    if (!isValid)
                        break;
                    board.append("|");
                    p = Position.at(x, y, z);
                    b = ballAt(p);
                    if (l.contains(b))
                        board.append("\u001B[32m").append(Ball.ballToString(b)).append("\u001B[0m");
                    else
                        board.append("\u001B[31m").append(Ball.ballToString(b)).append("\u001B[0m");
                }
                if (isValid)
                    board.append("|\t");
            }
            board.append("\n");
            if (y == 0)
                board.append("-----------------\t-------------\t---------\t-----\n");
            if (y == 1)
                board.append("-----------------\t-------------\t---------\n");
            if (y == 2)
                board.append("-----------------\t-------------\n");
            if (y == 3)
                board.append("-----------------\n");
        }
        return board.toString();
    }
}
