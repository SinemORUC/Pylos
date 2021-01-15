package model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public static final int nbBalls = Model.BALLS / 2;
    public final Ball[] balls = new Ball[nbBalls];
    public final int side;

    public Player(int side) {
        this.side = side;
        for (int i = 0; i < balls.length; i++) {
            balls[i] = new Ball(this);
        }
    }

    @Override
    public String toString() {
        return "Player" + sideToString() + "got " + nbBallsOnSide() + " balls on side\n";
    }

    public String sideToString() {
        if (side == -1)
            return " \u001B[35mX\u001B[0m ";
        else
            return " \u001B[34mO\u001B[0m ";
    }

    public int nbBallsOnSide() {
        int nb = 0;
        for (Ball b : balls)
            if (!b.isOnBoard())
                nb++;
        return nb;
    }

    public Player other() {
        return Model.getPlayer1() == this ? Model.getPlayer2() : Model.getPlayer1();
    }

//    public List<Ball> partitionBalls(List<Ball> ballsOnboard) {
//        List<Ball> ballsOnSide = new ArrayList<>();
//        for (Ball ball : balls) {
//            if (ball.isOnBoard()) {
//                ballsOnboard.add(ball);
//            } else {
//                ballsOnSide.add(ball);
//            }
//        }
//        return ballsOnSide;
//    } //todo delete

    public void putBallOnBoard(Position position) {
        Ball last = lastBallOnSide();
        last.placeAt(position);
    }

    private Ball lastBallOnSide() {
        for (int i = nbBalls - 1; i >= 0; i--) {
            if (!balls[i].isOnBoard()) {
                return balls[i];
            }
        }
        return null;
    }

    public List<Ball> getRemovableBalls() {
        List<Ball> list = new ArrayList<>();
        for (Ball ball : balls) {
            if (ball.isRemovable())
                list.add(ball);
        }
        return list;
    }

    public List<Ball> getMountableBalls() {
        List<Ball> list = new ArrayList<>();
        for (Ball ball : balls) {
            if (ball.isMountable())
                list.add(ball);
        }
        return list;
    }

    public boolean isSquare(Position position) {
        if (position.z >= 2)
            return false;
        for (List<Position> list : Position.getSquares(position)) {
            boolean validSquare = true;
            for (Position pos : list) {
                Board board = Model.getBoard();
                if (!board.anyBallAt(pos) || (board.ballAt(pos) != null && board.ballAt(pos).getOwner() != this)) {
                    validSquare = false;
                    break;
                }
            }
            if (validSquare)
                return true;
        }
        return false;
    }

    public void mountBall(Ball ball) {
        ball.removeFromBoard();
    }

    public void removeBall(Ball ball) {
        ball.removeFromBoard();
    }

    public boolean allBallsOnBoard() {
        for (Ball ball : balls) {
            if (!ball.isOnBoard())
                return false;
        }
        return true;
    }
}
