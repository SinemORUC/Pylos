package ia;

import model.Ball;
import model.Board;
import model.Player;
import model.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ia.IA.HEIGHT;

public class Status {
    private static final int END_POINT = 500;
    private static final int REMAIN_POINT = 140;
    private static final int SQUARE_POINT = 90;
    private static final int MIDDLE_POINT = 35;
    private static final int REMOVABLE_POINT = 15;

    private static final int ia = 1;
    private static final int player = 2;
    private int currentPlayer;
    private int otherPlayer;
    private int[][][] board = new int[HEIGHT][][];
    private int[] remains = new int[3];

    public Status(Board board, Player current) {
        currentPlayer = ia;
        otherPlayer = player;
        remains[currentPlayer] = 15;
        remains[otherPlayer] = 15;
        Ball ball;
        int player;
        for (int z = 0; z < HEIGHT; z++) {
            this.board[z] = new int[HEIGHT - z][HEIGHT - z];
            for (int y = 0; y < HEIGHT - z; y++) {
                for (int x = 0; x < HEIGHT - z; x++) {
                    ball = board.ballAt(Position.at(x, y, z));
                    if (ball != null) {
                        player = ball.getOwner() == current ? currentPlayer : otherPlayer;
                        this.board[z][y][x] = player;
                        remains[player]--;
                    } else {
                        this.board[z][y][x] = 0;
                    }
                }
            }
        }
    }

    public Status(Status status) {
        currentPlayer = status.currentPlayer;
        otherPlayer = status.otherPlayer;
        remains = status.remains.clone();
        for (int z = 0; z < HEIGHT; z++) {
            board[z] = new int[HEIGHT - z][];
            for (int y = 0; y < HEIGHT - z; y++)
                board[z][y] = status.board[z][y].clone();
        }
    }

    public boolean isMountable(Position p) {
        if (p.z == 0)
            return false;
        for (int ix = p.x; ix <= p.x + 1; ix++)
            for (int iy = p.y; iy <= p.y + 1; iy++)
                if (board[p.z - 1][iy][ix] == 0)
                    return false;
        return !positionsToMount(p).isEmpty();
    }

    public boolean isEnd() {
        return board[HEIGHT - 1][0][0] != 0;
    }

    public boolean square(Position p) {
        return square(p, currentPlayer);
    }

    private boolean square(Position pos, int player) {
        if (pos.z >= 2)
            return false;
        boolean isSquare;
        for (List<Position> square : Position.getSquares(pos)) {
            isSquare = true;
            for (Position p : square) {
                int cell = board[p.z][p.y][p.x];
                if ((cell == 0 && p != pos) || (cell != 0 && cell != player)) {
                    isSquare = false;
                    break;
                }
            }
            if (isSquare)
                return true;
        }
        return false;
    }

    public boolean isRemovable(Position p) {
        if (board[p.z][p.y][p.x] == 0)
            return false;
        for (int iy = p.y - 1; iy <= p.y; iy++)
            for (int ix = p.x - 1; ix <= p.x; ix++)
                if (Position.isValid(ix, iy, p.z + 1) && board[p.z + 1][iy][ix] != 0)
                    return false;
        return true;
    }

    public List<Position> positionsToMount(Position pos) {
        if (pos.z == HEIGHT - 1)
            return null;
        List<Position> removes = queryRemovables();
        removes.removeAll(Position.getToMount(pos));
        removes.removeIf(p -> p.z >= pos.z);
        return removes;
    }

    public boolean isPlayable(int x, int y, int z) {
        if (board[z][y][x] != 0)
            return false;
        if (z == 0)
            return true;
        for (int ix = x; ix <= x + 1; ix++)
            for (int iy = y; iy <= y + 1; iy++)
                if (board[z - 1][iy][ix] == 0)
                    return false;
        return true;
    }

    public List<Position> queryRemovables() {
        List<Position> removables = new ArrayList<>();
        // Go through the board
        for (int z = 0; z < HEIGHT; z++)
            for (int y = 0; y < HEIGHT - z; y++)
                for (int x = 0; x < HEIGHT - z; x++)
                    if (board[z][y][x] == currentPlayer && isRemovable(Position.at(x, y, z)))
                        removables.add(Position.at(x, y, z));
        return removables;
    }

    public List<Position> queryPlayables() {
        List<Position> playables = new ArrayList<>();
        for (int z = 0; z < HEIGHT; z++)
            for (int y = 0; y < HEIGHT - z; y++)
                for (int x = 0; x < HEIGHT - z; x++)
                    if (board[z][y][x] == 0 && isPlayable(x, y, z))
                        playables.add(Position.at(x, y, z));
        return playables;
    }

    public void switchPlayers() {
        int tmp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = tmp;
    }

    public void makeMove(Move move) {
        Position placeAt = move.placeAt;
        if (placeAt != null) {
            int x = placeAt.x;
            int y = placeAt.y;
            int z = placeAt.z;
            board[z][y][x] = currentPlayer;
            remains[currentPlayer]--;
        }

        Position[] removes = move.getRemoves();
        if (removes != null)
            for (Position remove : removes) {
                if (remove != null) {
                    board[remove.z][remove.y][remove.x] = 0;
                    remains[currentPlayer]++;
                }
            }
    }

    public int evaluateStatus() {
        int score = remains[ia] * REMAIN_POINT - remains[player] * REMAIN_POINT;
        for (int z = 0; z < HEIGHT; z++)
            for (int y = 0; y < HEIGHT - z; y++)
                for (int x = 0; x < HEIGHT - z; x++) {
                    int cell = board[z][y][x];
                    if (cell == ia) {
                        if (Position.isTop(Position.at(x, y, z)))
                            score += END_POINT;
                        else if (Position.isMiddle(x, y, z))
                            score += MIDDLE_POINT;
                        else if (isRemovable(Position.at(x, y, z)))
                            score += REMOVABLE_POINT;
                    } else if (cell == player) {
                        if (Position.isTop(Position.at(x, y, z)))
                            score -= END_POINT;
                        else if (Position.isMiddle(x, y, z))
                            score += MIDDLE_POINT;
                        else if (isRemovable(Position.at(x, y, z)))
                            score -= REMOVABLE_POINT;
                    } else {
                        if (square(Position.at(x, y, z), ia))
                            score += SQUARE_POINT;
                        if (square(Position.at(x, y, z), player))
                            score -= SQUARE_POINT;
                    }
                }
        return score;
    }
}
