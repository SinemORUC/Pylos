package ia;

import model.Ball;
import model.Board;
import model.Player;
import model.Position;

import java.util.ArrayList;
import java.util.List;

import static ia.IA.HEIGHT;

public class Status {
    private static final int END_POINT = 500;
    private static final int REMAIN_POINT = 130;
    private static final int SQUARE_POINT = 90;
    private static final int MIDDLE_POINT = 35;
    private static final int REMOVABLE_POINT = 15;

    private static final int ia = 1;
    private static final int player = 2;
    private int currentPlayer;
    private int otherPlayer;
    private int[][][] board = new int[HEIGHT][][];
    private int[] remains = new int[3];
    private List<Position> playables = new ArrayList<>();
    private List<Position> removables = new ArrayList<>();

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
                        if (isPlayable(x, y, z))
                            playables.add(Position.at(x, y, z));
                    }
                }
            }
        }
        queryRemovables();
    }

    private Status(Status status) {
        currentPlayer = status.currentPlayer;
        otherPlayer = status.otherPlayer;
        remains = status.remains.clone();
        board = status.board.clone();
        playables = new ArrayList<>(status.playables);
        removables = null;
    }

    public List<Position> getPlayables() {
        return new ArrayList<>(playables);
    }

    public boolean isPlayable(int x, int y, int z) {
        if (z == 0 && board[z][y][x] == 0)
            return true;
        for (Position p : Position.getToMount(Position.at(x, y, z)))
            if (board[z - 1][p.y][p.x] == 0)
                return false;
        return true;
    }

    public boolean isMountable(Position p) {
        if (p.z == 0)
            return false;
        for (Position pos : Position.getToMount(p))
            if (board[pos.z][pos.y][pos.x] == 0)
                return false;
        List<Position> removes = removablesToMount(p);
        return removes != null && !removes.isEmpty();
    }

    public boolean isEnd() {
        return board[HEIGHT -1][0][0] != 0;
    }

    public List<Position> removablesToMount(Position pos) {
        if (pos.z == HEIGHT - 1)
            return null;
        List<Position> removes = new ArrayList<>(removables);
        removes.removeAll(Position.getToMount(pos));
        removes.removeIf(p -> p.z >= pos.z);
        return removes;
    }

    public List<Position> getRemovables() {
        return new ArrayList<>(removables);
    }

    public boolean isRemovable(Position p) {
        int z = p.z;
        int y = p.y;
        int x = p.x;
        if (board[z][y][x] == 0) {
            return false;
        }
        for (int iy = y - 1; iy < y; iy++)
            for (int ix = x - 1; ix < x; x++)
                if (Position.isValid(ix, iy, z + 1) && board[z + 1][iy][ix] != 0)
                    return false;
        return true;
    }

    public boolean squareOn(Position p) {
        return squareOn(p, currentPlayer);
    }

    private boolean squareOn(Position pos, int player) {
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

    private void queryRemovables() {
        List<Position> removables = new ArrayList<>();
        // Go through the board
        for (int z = 0; z < HEIGHT; z++)
            for (int y = 0; y < HEIGHT - z; y++)
                for (int x = 0; x < HEIGHT - z; x++)
                    if (board[z][y][x] == currentPlayer && isRemovable(Position.at(x, y, z)))
                        removables.add(Position.at(x, y, z));
    }

    public void switchPlayers() {
        int tmp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = tmp;
    }

    public Status makeMove(Move move) {
        Status next = new Status(this);
        Position placeAt = move.placeAt;
        if (placeAt != null) {
            int x = placeAt.x;
            int y = placeAt.y;
            int z = placeAt.z;
            next.board[z][y][x] = next.currentPlayer;
            next.playables.remove(placeAt);
            next.remains[currentPlayer]--;
            for (int iy = y - 1; iy < y; iy++)
                for (int ix = x - 1; ix < x; x++)
                    if (Position.isValid(ix, iy, z + 1) && isPlayable(ix, iy, z + 1))
                        next.playables.add(Position.at(ix, iy, z + 1));
        }

        List<Position> removes = move.getRemoves();
        if (removes != null && !removes.isEmpty()) {
            for (Position remove : removes) {
                next.board[remove.z][remove.y][remove.x] = 0;
                next.playables.add(remove);
                next.remains[currentPlayer]++;
            }
            for (Position play : playables) {
                if (!next.isPlayable(play.x, play.y, play.z))
                    next.playables.remove(play);
            }
        }
        next.queryRemovables();
        next.switchPlayers();
        return next;
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
                            score += REMOVABLE_POINT;
                        else if (isRemovable(Position.at(x, y, z)))
                            score += MIDDLE_POINT;
                    } else if (cell == player) {
                        if (Position.isTop(Position.at(x, y, z)))
                            score -= END_POINT;
                        else if (Position.isMiddle(x, y, z))
                            score += REMOVABLE_POINT;
                        else if (isRemovable(Position.at(x, y, z)))
                            score -= MIDDLE_POINT;
                    } else {
                        if (squareOn(Position.at(x, y, z), ia))
                            score += SQUARE_POINT;
                        if (squareOn(Position.at(x, y, z), player))
                            score -= SQUARE_POINT;
                    }
                }
        return score;
    }
}
