package model;

import ia.IA;

import java.util.ArrayList;
import java.util.List;

public abstract class Model {
    public static final int HEIGHT = 4;
    public static final int BALLS = 30;
    private static Board board;
    private static Player player1;
    private static Player player2;
    private static Player[] players;

    private static Player currentPlayer;

    public static void initialize(boolean ia) {
        board = new Board();
        player1 = new Player(+1);
        if (ia)
            player2 = new IA();
        else
            player2 = new Player(-1);
        players = new Player[]{player1, player2};
        currentPlayer = player1;
    }

    public static Board getBoard() {
        return board;
    }

    public static Player getPlayer1() {
        return player1;
    }

    public static Player getPlayer2() {
        return player2;
    }

    public static Player[] getPlayers() {
        return players;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void switchPlayers() {
        currentPlayer = currentPlayer.other();
    }

    public static Player otherPlayer() {
        return currentPlayer.other();
    }

    public static boolean isWinner() {
        return board.anyBallAt(Position.getTop());
    }

    public static boolean canPlaceBallAt(Position position) {
        if (board.anyBallAt(position))
            return false;
        if (position.z == 0)
            return true;
        for (int x = position.x; x < position.x + 2; x++) {
            for (int y = position.y; y < position.y + 2; y++) {
                if (!board.anyBallAt(Position.at(x, y, position.z - 1)))
                    return false;
            }
        }
        return true;
    }

    public static boolean canPlaceBallAtIgnoring(Position position, Position ignored) {
        if (board.anyBallAt(position))
            return position == ignored;
        if (position.z == 0)
            return true;
        for (int x = position.x; x < position.x + 2; x++) {
            for (int y = position.y; y < position.y + 2; y++) {
                Position pos = Position.at(x, y, position.z - 1);
                if (!board.anyBallAt(pos) || pos == ignored)
                    return false;
            }
        }
        return true;
    }

    public static List<Position> getPositionBalls() {
        List<Position> list = new ArrayList<>();
        for (int level = 0; level < HEIGHT; level++) {
            for (Position position : accessibleBalls(level)) {
                list.add(position);
            }
        }
        return list;
    }

    public static List<Position> accessibleBalls(int level) {
        List<Position> list = new ArrayList<>();
        Position position;
        for (int x = 0; x < HEIGHT - level; x++) {
            for (int y = 0; y < HEIGHT - level; y++) {
                position = Position.at(x, y, level);
                if (canPlaceBallAt(position))
                    list.add(position);
            }
        }
        return list;
    }

    public static List<Position> getPositionsToMount(Ball ball) {
        List<Position> list = new ArrayList<>();
        for (int z = ball.getPosition().z + 1; z < HEIGHT; z++) {
            for (int x = 0; x < 4 - z; x++) {
                for (int y = 0; y < 4 - z; y++) {
                    if (canPlaceBallAtIgnoring(Position.at(x, y, z), ball.getPosition()))
                        list.add(Position.at(x, y, z));
                }
            }
        }
        return list;
    }

    public static int ballsBySideAtLevel(int level) {
        return HEIGHT - level;
    }

    public static String modelToString() {
        return player1.toString() +
                player2.toString() +
                board.toString();
    }
}
